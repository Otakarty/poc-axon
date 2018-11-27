package poc.application.person;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import poc.application.commands.CommandWrapper;
import poc.application.commands.Order;
import poc.application.commands.OrderInfo;
import poc.application.commands.Registry;
import poc.application.commands.ServiceEnum;
import poc.application.person.commands.ChangePersonName;
import poc.application.person.commands.CreatePerson;
import poc.application.person.queries.CountTotalPersonsQuery;
import poc.application.person.queries.FindPersonById;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.UID;

@Service
public class PersonService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private EventStore eventStore;

    @Autowired
    @Qualifier("refog")
    private Persons repository;

    // @Autowired
    // private CommandJpaRepository commandRepository;

    public void createPerson(final Person person) {
        OrderInfo info = new OrderInfo(ServiceEnum.IHM);
        CreatePerson command = new CreatePerson(info, person);
        // this.commandRepository.save(toCommandEntry(command, "CREATED"));
        Registry.getCommandGateway().send(new Order(info, Collections.singletonList(command)));
        // OrderHandler.saveAndPublishOrder(new Order(info, Collections.singletonList(command), person.getUid()));
    }

    public void changePersonName(final UID uid, final Name newName) {
        OrderInfo info = new OrderInfo(ServiceEnum.IHM);
        ChangePersonName command = new ChangePersonName(info, uid, newName);
        Registry.getCommandGateway().send(new CommandWrapper(command));
        // OrderHandler.saveAndPublishOrder(new Order(info, Collections.singletonList(command), uid));
    }

    public PersonDTO getPersonSnapshot(final UID uid) {
        return new PersonDTO(this.repository.findById(uid));
    }

    @Autowired
    QueryGateway queryGateway;

    public Long getPersonsCount() {
        try {
            return this.queryGateway.query(new CountTotalPersonsQuery(), Long.class).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public PersonDTO findById(final UID uid) {
        try {
            return new PersonDTO(this.queryGateway.query(new FindPersonById(uid), Person.class).get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
