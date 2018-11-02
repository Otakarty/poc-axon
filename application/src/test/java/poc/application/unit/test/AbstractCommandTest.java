package poc.application.unit.test;

import java.util.Arrays;
import java.util.List;

import org.axonframework.messaging.Message;
import org.axonframework.test.matchers.Matchers;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public abstract class AbstractCommandTest {

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
    public static Matcher<List<Message<?>>> aSequenceOf(final Matcher<?>... matchers) {
        Matcher<?>[] terminatedListOfMatchers = Arrays.copyOf(matchers, matchers.length + 1);
        terminatedListOfMatchers[matchers.length] = Matchers.andNoMore();
        return Matchers.payloadsMatching(Matchers.sequenceOf(terminatedListOfMatchers));
    }

}
