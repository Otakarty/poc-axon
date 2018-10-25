package poc.exposition.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poc.application.commands.Command;
import poc.application.commands.Order;
import poc.application.commands.OrderInfo;
import poc.application.commands.ServiceEnum;
import poc.application.person.PersonDTO;
import poc.application.person.PersonService;
import poc.application.person.commands.ChangePersonName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;

@RestController
@RequestMapping("/persons")
public class PersonController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PersonService service;

    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventBus eventBus;
    @Autowired
    private AggregateFactory<Person> personFactory;

    @PostMapping
    public void newPerson(@RequestBody final PersonDTO person) {
        this.logger.info("Creating new person ", person);
        this.service.createPerson(person.toDomainEntity());
    }

    @PostMapping("{uid}/rename/{newName}")
    public void changePersonName(@PathVariable final String uid, @PathVariable final String newName) {
        this.logger.info("Renaming ", uid, " with name ", newName);
        this.service.changePersonName(new UID(uid), new Name(newName));
    }

    @GetMapping("/snapshot/{uid}")
    public PersonDTO getPersonSnapshot(@PathVariable final String uid) {
        return this.service.getPersonSnapshot(new UID(uid));
    }

    @Autowired
    Repository<Person> personsRepository;

    @GetMapping("/{uid}")
    public Aggregate<Person> getPersonFromEvents(@PathVariable final String uid) {
        // return this.service.getPersonFromEvents(new UID(uid));
        return this.personsRepository.load(uid);
    }

    @GetMapping("{uid}/events")
    public List<Object> getPersonEvents(@PathVariable final String uid) {
        return this.eventStore.readEvents(uid).asStream().map(Message::getPayload).collect(Collectors.toList());
    }

    // TODO: preferred way is to use query models instead of querying EventStore directly
    @GetMapping("/last-event")
    public Optional<TrackedEventMessage<?>> getLastEvents() {
        return this.eventStore.openStream(null).asStream().findFirst();
    }

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/{uid}/test-im/{expectedStatus}")
    public void testIM(@PathVariable final String uid, @PathVariable final String expectedStatus) {
        OrderInfo info = new OrderInfo(UUID.randomUUID(), ServiceEnum.IM);
        UID id = new UID(uid);
        Name newName1 = new Name("NEW");
        Name newName2 = new Name("NEWNEW");
        Name newName3 = new Name("NEWNEWNEW");
        Name newName4 = new Name("NEWNEWNEWNEW");

        List<Command<?>> commands;
        if (expectedStatus.equalsIgnoreCase("OK")) {
            commands = Arrays.asList(new ChangePersonName(id, newName1), new ChangePersonName(id, newName2));
        } else if (expectedStatus.equalsIgnoreCase("KO")) {
            commands = Arrays.asList(new ChangePersonName(id, newName3), new ChangePersonName(id, newName2),
                new ChangePersonName(id, newName2), new ChangePersonName(id, newName4));
        } else {
            throw new IllegalArgumentException("OK or KO expected");
        }
        this.commandGateway.send(new Order(info, commands, id));
    }

}
