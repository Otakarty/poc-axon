package poc.application.unit.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import poc.application.commands.CommandInfo;
import poc.application.commands.ServiceEnum;
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
public class PersonCommandsTest extends AbstractCommandTest {
    private FixtureConfiguration<Person> personFixture;

    @InjectMocks
    PersonCommandHandler personCommandHandler;

    @Spy
    private Repository<Person> axonRepo;
    @Spy
    EventBus eventBus;

    @Before
    public void setUp() {
        this.personFixture = new AggregateTestFixture<>(Person.class);
        this.eventBus = this.personFixture.getEventBus();
        this.axonRepo = this.personFixture.getRepository();
        MockitoAnnotations.initMocks(this);
        this.personFixture.registerAnnotatedCommandHandler(this.personCommandHandler);
        this.personFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void create_person_command() {
        CommandInfo info = new CommandInfo(ServiceEnum.IM);
        UID uid = new UID("a76764");
        Name name = new Name("DUCASSE");
        FirstName firstName = new FirstName("Romain");
        Person person = new Person.Builder().uid(uid).firstName(firstName).name(name).build();
        // TODO: despite givenNoPriorActivity, throw an exception because no 'given' events for the aggregate,
        // solution to find
        CreatePerson command = new CreatePerson(info, person);
        this.personFixture.givenNoPriorActivity().when(command).expectSuccessfulHandlerExecution()
            .expectEventsMatching(
                aSequenceOf(aPersonCreatedEventLike(new PersonCreated(command.getCommandId(), person))));
    }

    @Test
    public void change_person_name() {
        CommandInfo info = new CommandInfo(ServiceEnum.IM);
        UID uid = new UID("a76764");
        Name name = new Name("DUCASSE");
        Name newName = new Name("SILLIARD");
        FirstName firstName = new FirstName("Romain");
        ChangePersonName command = new ChangePersonName(info, uid, newName);
        Person person = new Person.Builder().uid(uid).firstName(firstName).name(name).build();
        this.personFixture.given(new PersonCreated(null, person)).when(command).expectSuccessfulHandlerExecution()
            .expectEventsMatching(aSequenceOf(
                aPersonNameChangedEventLike(new PersonNameChanged(command.getCommandId(), uid, newName))));
    }

    // ------------------------------------------------------------------------
    // HELPER MATCHERS
    // ------------------------------------------------------------------------

    @Factory
    public static <T> Matcher<PersonCreated> aPersonCreatedEventLike(final PersonCreated event) {
        return allOf(instanceOf(PersonCreated.class), hasProperty("uid", equalTo(event.getUid())),
            hasProperty("person", equalTo(event.getPerson())));
    }

    @Factory
    public static <T> Matcher<PersonNameChanged> aPersonNameChangedEventLike(final PersonNameChanged event) {
        return allOf(instanceOf(PersonNameChanged.class), hasProperty("uid", equalTo(event.getUid())),
            hasProperty("name", equalTo(event.getName())));
    }
}