package poc.domain.persons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import poc.domain.NullableObject;
import poc.domain.exceptions.InvalidIngestionCommandException;
import poc.domain.person.IngestionPersonDpo;
import poc.domain.person.Person;

public class PersonsTest {

    @Test
    public void update_person_from_ingestion() {
        IngestionPersonDpo dpo = new IngestionPersonDpo.Builder().firstName(NullableObject.empty())
            .name(NullableObject.empty()).build();

        Person p = new Person();
        try {
            p.updateFromIngestion(dpo);
        } catch (InvalidIngestionCommandException e) {
            Assertions.assertEquals(2, e.getExceptions().stream().map(IllegalArgumentException::getMessage)
                .filter(message -> message.contains("should not be null")).count());
        }
    }
}
