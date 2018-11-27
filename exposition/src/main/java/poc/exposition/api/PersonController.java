package poc.exposition.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
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
import poc.application.commands.Registry;
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

    @GetMapping("/count")
    public Long getPersonsCount() {
        return this.service.getPersonsCount();
    }

    @GetMapping("{uid}")
    public PersonDTO findPersonsById(@PathVariable final String uid) {
        return this.service.findById(new UID(uid));
    }

    // @GetMapping("findbyFirstName/{firstName}")
    // public List<PersonDTO> findPersonsByFirstName(final String firstName) {
    // return this.service
    // }

    /*********** Get user from axon repository. *************/
    // TODO: remove
    @Autowired
    Repository<Person> personsRepository;
    @Autowired
    QueryBus queryBus;

    // TODO: preferred way is to use query models instead of querying EventStore directly
    @GetMapping("/{uid}/axon")
    public Person getPersonFromEvents(@PathVariable final String uid)
        throws InterruptedException, ExecutionException {
        // return this.service.getPersonFromEvents(new UID(uid));
        // (1) create a query message
        GenericQueryMessage<String, Person> query =
            new GenericQueryMessage<>(uid, ResponseTypes.instanceOf(Person.class));
        // (2) send a query message and print query response
        return this.queryBus.query(query).get().getPayload();

    }

    @QueryHandler
    public Person handleGetPerson(final String uid) {
        Person p = new Person();
        this.personsRepository.load(uid).execute(person -> p.copy(person));
        return p;
    }

    /********** End Get user from axon repository. ***********/

    @GetMapping("{uid}/events")
    public List<Object> getPersonEvents(@PathVariable final String uid) {
        return this.eventStore.readEvents(uid).asStream().filter(event -> event.getTimestamp().getNano() > 0)
            .map(Message::getPayload).collect(Collectors.toList());
    }

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/{uid}/test-im/{expectedStatus}")
    public void testIM(@PathVariable final String uid, @PathVariable final String expectedStatus) {
        OrderInfo info = new OrderInfo(ServiceEnum.IM);
        UID id = new UID(uid);
        Name newName1 = new Name("NEW");
        Name newName2 = new Name("NEWNEW");
        Name newName3 = new Name("NEWNEWNEW");
        Name newName4 = new Name("ERROR");

        List<Command<?>> commands;
        if (expectedStatus.equalsIgnoreCase("OK")) {
            commands =
                Arrays.asList(new ChangePersonName(info, id, newName1), new ChangePersonName(info, id, newName2));
        } else if (expectedStatus.equalsIgnoreCase("KO")) {
            commands =
                Arrays.asList(new ChangePersonName(info, id, newName3), new ChangePersonName(info, id, newName2),
                    new ChangePersonName(info, id, newName2), new ChangePersonName(info, id, newName4));
        } else {
            throw new IllegalArgumentException("OK or KO expected");
        }

        Registry.getCommandGateway().send(new Order(info, commands));
        // // Not working, need to rethrow the InvalidOrder through an InvalidateOrderCommand
        // try {
        // Registry.getCommandGateway().send(new Order(info, commands));
        // } catch (InvalidOrderException e) {
        // this.logger.info("Publishing invalid order event");
        // this.eventBus.publish(asEventMessage(e));
        // }
        // // OrderHandler.saveAndPublishOrder(new Order(info, commands, id));
    }

}
