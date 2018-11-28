package poc.application.unit.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Arrays;
import java.util.List;

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

import poc.application.commands.Command;
import poc.application.commands.Order;
import poc.application.commands.OrderHandler;
import poc.application.commands.OrderInfo;
import poc.application.commands.Registry;
import poc.application.commands.ServiceEnum;
import poc.application.events.OrderValidated;
import poc.application.person.commands.ChangePersonName;
import poc.application.person.commands.CreatePerson;
import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

// TODO: not working, fixture cannot handle multiple aggregates (here Order + Person)
public class OrderCommandsTest extends AbstractCommandTest {
    private FixtureConfiguration<Person> personFixture;
    private FixtureConfiguration<Order> orderFixture;

    @Spy
    EventBus orderBus;
    @InjectMocks
    OrderHandler orderCommandHandler;

    @Before
    public void setUp() {
        this.personFixture = new AggregateTestFixture<>(Person.class);
        this.orderFixture = new AggregateTestFixture<>(Order.class);
        this.orderBus = this.orderFixture.getEventBus();

        MockitoAnnotations.initMocks(this);

        this.personFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
        this.orderFixture.registerAnnotatedCommandHandler(this.orderCommandHandler);
        this.orderFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());

        Registry registry = new Registry();
        registry.setPersonRepository(this.personFixture.getRepository());
    }

    @Test
    public void create_order_change_name_twice() {
        OrderInfo info = new OrderInfo(ServiceEnum.IM);
        UID uid = new UID("a76764");
        Name name = new Name("DUCASSE");
        Name newName = new Name("SILLIARD");
        Name newName2 = new Name("Otakarty");
        FirstName firstName = new FirstName("Romain");
        Person person = new Person.Builder().uid(uid).firstName(firstName).name(name).build();

        CreatePerson createPerson = new CreatePerson(info, person);
        ChangePersonName changeName1 = new ChangePersonName(info, uid, newName);
        ChangePersonName changeName2 = new ChangePersonName(info, uid, newName2);
        List<Command> commandsToApply = Arrays.asList(changeName1, changeName2);
        Order order = new Order(new OrderInfo(ServiceEnum.IM), commandsToApply);

        this.personFixture.givenNoPriorActivity().when(createPerson);

        this.orderFixture.given(new PersonCreated(createPerson.getCommandId(), person)).when(order)
            .expectSuccessfulHandlerExecution()
            .expectEventsMatching(aSequenceOf(
                aPersonCreatedEventLike(new PersonCreated(createPerson.getCommandId(), person)),
                anOrderValidatedEventLike(new OrderValidated(info.getId(), commandsToApply)),
                aPersonNameChangedEventLike(new PersonNameChanged(createPerson.getCommandId(), uid, newName)),
                aPersonNameChangedEventLike(new PersonNameChanged(createPerson.getCommandId(), uid, newName2))));
    }

    // ------------------------------------------------------------------------
    // In order to resolve exception:
    // java.lang.SecurityException: class "org.hamcrest.Matchers"'s signer information does not match signer
    // information of other classes in the same package
    // replace eclipse/plugins/org.hamcrest.core_1.3.0.v201303031735 with the version in your maven repository
    // ------------------------------------------------------------------------

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

    @Factory
    public static <T> Matcher<OrderValidated> anOrderValidatedEventLike(final OrderValidated event) {
        return allOf(instanceOf(OrderValidated.class), hasProperty("uid", equalTo(event.getId())),
            hasProperty("commandsToApply", equalTo(event.getCommandsToApply())));
    }

}