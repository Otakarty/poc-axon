package poc.exposition.api;

import java.util.List;
import java.util.stream.Collectors;

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

import poc.application.person.PersonDTO;
import poc.application.person.PersonService;
import poc.domain.person.FirstName;
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
    public PersonDTO newPerson(@RequestBody final PersonDTO person) {
        this.logger.info("Creating new person ", person);
        this.service.createPerson(new Person.Builder().uid(new UID(person.getUid()))
            .firstName(new FirstName(person.getFirstName())).name(new Name(person.getName())).build());
        return person;
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

    @GetMapping("/{uid}")
    public Person getPersonFromEvents(@PathVariable final String uid) {
        return this.service.getPersonFromEvents(new UID(uid));
    }

    @GetMapping("{uid}/events")
    public List<Object> getEvents(@PathVariable final String uid) {
        return this.eventStore.readEvents(uid).asStream().map(Message::getPayload).collect(Collectors.toList());
    }
}
