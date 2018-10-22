package poc.exposition.test;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import poc.application.person.commands.ChangePersonName;
import poc.application.person.commands.CreatePerson;
import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

public class PersonCommandsTest {
    private FixtureConfiguration<Person> fixture;

    @Before
    public void setUp() {
        this.fixture = new AggregateTestFixture<>(Person.class);
    }

    @Test
    public void testCreatePeson() {
        UID uid = new UID("a76764");
        Name name = new Name("DUCASSE");
        FirstName firstName = new FirstName("Romain");
        Person person = new Person.Builder().uid(uid).firstName(firstName).name(name).build();
        this.fixture.given().when(new CreatePerson(person)).expectSuccessfulHandlerExecution()
            .expectEvents(new PersonCreated(person));
    }

    @Test
    public void testRenamePeson() {
        UID uid = new UID("a76764");
        Name name = new Name("DUCASSE");
        Name newName = new Name("SILLIARD");
        FirstName firstName = new FirstName("Romain");
        Person person = new Person.Builder().uid(uid).firstName(firstName).name(name).build();
        this.fixture.given(new PersonCreated(person)).when(new ChangePersonName(uid, newName))
            .expectSuccessfulHandlerExecution().expectEvents(new PersonNameChanged(uid, newName));
    }

}