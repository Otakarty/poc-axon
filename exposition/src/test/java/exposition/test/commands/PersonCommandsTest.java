package exposition.test.commands;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.commands.CreatePerson;
import poc.domain.person.events.PersonCreated;

public class PersonCommandsTest {
	private FixtureConfiguration<Person> fixture;

	@Before
	public void setUp() {
		fixture = new AggregateTestFixture<Person>(Person.class);
	}

	@Test
	public void testFirstFixture() {
		UID uid = new UID("a76764");
		Name name = new Name("DUCASSE");
		FirstName firstName = new FirstName("Romain");
		fixture.given().when(new CreatePerson(uid, name, firstName)).expectSuccessfulHandlerExecution()
				.expectEvents(new PersonCreated(uid, name, firstName));
	}

}