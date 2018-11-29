package poc.application.person;

import java.util.concurrent.ExecutionException;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import poc.application.commands.CommandInfo;
import poc.application.commands.CommandWrapper;
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
    @Qualifier("refog")
    private Persons repository;

    public void createPerson(final Person person) {
        CommandInfo info = new CommandInfo(ServiceEnum.IHM);
        CreatePerson command = new CreatePerson(info, person);
        Registry.getCommandGateway().send(new CommandWrapper(command));
    }

    public void changePersonName(final UID uid, final Name newName) {
        CommandInfo info = new CommandInfo(ServiceEnum.IHM);
        ChangePersonName command = new ChangePersonName(info, uid, newName);
        Registry.getCommandGateway().send(new CommandWrapper(command));
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
