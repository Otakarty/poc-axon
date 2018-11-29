package poc.exposition.api;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryBus;
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

import poc.application.commands.CommandInfo;
import poc.application.commands.CommandWrapper;
import poc.application.commands.Registry;
import poc.application.commands.ServiceEnum;
import poc.application.person.PersonDTO;
import poc.application.person.PersonService;
import poc.application.person.commands.crud.UpdatePersonFromIngestion;
import poc.domain.NullableObject;
import poc.domain.person.FirstName;
import poc.domain.person.IngestionPersonDpo;
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

    /********** End Get user from axon repository. ***********/

    @GetMapping("{uid}/events")
    public List<Object> getPersonEvents(@PathVariable final String uid) {
        return this.eventStore.readEvents(uid).asStream().filter(event -> event.getTimestamp().getNano() > 0)
            .map(Message::getPayload).collect(Collectors.toList());
    }

    @PostMapping("/{uid}/test-im/{expectedStatus}")
    public void testIM(@PathVariable final String uid, @PathVariable final String expectedStatus) {
        CommandInfo info = new CommandInfo(ServiceEnum.IM);
        UID id = new UID(uid);

        if (expectedStatus.equalsIgnoreCase("OK")) {
            IngestionPersonDpo newPersonPayload =
                new IngestionPersonDpo.Builder().firstName(NullableObject.of(new FirstName("Jean-Michel")))
                    .name(NullableObject.of(new Name("POC"))).build();
            UpdatePersonFromIngestion command = new UpdatePersonFromIngestion(info, id, newPersonPayload);
            Registry.getCommandGateway().send(new CommandWrapper(command));
        } else if (expectedStatus.equalsIgnoreCase("KO")) {
            IngestionPersonDpo newPersonPayload = new IngestionPersonDpo.Builder()
                .firstName(NullableObject.empty()).name(NullableObject.empty()).build();
            UpdatePersonFromIngestion command = new UpdatePersonFromIngestion(info, id, newPersonPayload);
            Registry.getCommandGateway().send(new CommandWrapper(command));
        } else {
            throw new IllegalArgumentException("OK or KO expected");
        }
    }

}
