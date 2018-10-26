package poc.exposition.test;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import poc.application.person.commands.ChangePersonName;
import poc.application.person.commands.CreatePerson;
import poc.application.person.commands.PersonCommandHandler;
import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

//@RunWith(MockitoJUnitRunner.class)
// @SpringBootTest(classes = Application.class)
// @AutoConfigureMockMvc
// @TestPropertySource(locations = "classpath:application.properties")
public class PersonCommandsTest {
    private FixtureConfiguration<Person> fixture;

    @InjectMocks
    PersonCommandHandler personCommandHandler;

    @Spy
    private Repository<Person> axonRepo;
    @Spy
    EventBus eventBus;

    @Before
    public void setUp() {
        this.fixture = new AggregateTestFixture<>(Person.class);
        this.eventBus = this.fixture.getEventBus();
        this.axonRepo = this.fixture.getRepository();
        MockitoAnnotations.initMocks(this);
        this.fixture.registerAnnotatedCommandHandler(this.personCommandHandler);
        this.fixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
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