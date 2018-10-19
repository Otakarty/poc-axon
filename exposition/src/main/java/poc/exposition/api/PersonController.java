package poc.exposition.api;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
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

import application.person.PersonDTO;
import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.UID;
import poc.domain.person.commands.CreatePerson;

@RestController
@RequestMapping("/persons")
public class PersonController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CommandGateway commandGateway;

	@Autowired
	private EventStore eventStore;

	@PostMapping
	public PersonDTO newPerson(@RequestBody PersonDTO person) {
		commandGateway.send(new CreatePerson(new UID(person.getUid()), new Name(person.getName()),
				new FirstName(person.getFirstName())));
		return person;
	}

	@GetMapping("/{uid}")
	public PersonDTO getPerson(@PathVariable String uid) {
		return new PersonDTO(uid, null, null);
	}

	@GetMapping("{uid}/events")
	public List<Object> getEvents(@PathVariable String uid) {
		return eventStore.readEvents(uid).asStream().map(Message::getPayload).collect(Collectors.toList());
	}
}
