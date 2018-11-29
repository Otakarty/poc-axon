package poc.infrastructure.persons;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import poc.domain.exceptions.InvalidIngestionCommandException;
import poc.domain.person.IngestionPersonDpo;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.UID;

@Repository
@Qualifier("refog")
public class PersonsRepository implements Persons {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PersonsJpaRepository jpaRepository;

    // TODO: catch exception (checked or restexceptionhandler)
    private PersonEntry findEntryById(final String id) {
        return Optional
            .ofNullable(this.jpaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format("Person with UID {0} not found", id))))
            .get();
    }

    @Override
    public Person findById(final UID id) {
        return this.findEntryById(id.getValue()).toDomainAggregate();
    }

    @Override
    public Person add(final Person person) {
        PersonEntry savedEntry = null;
        if (this.jpaRepository.existsById(person.getId().toString())) {
            throw new IllegalArgumentException(
                MessageFormat.format("Person with UID {0} already exists", person.getId()));
        } else {
            savedEntry = this.jpaRepository.save(new PersonEntry(person));
        }
        return savedEntry.toDomainAggregate();
    }

    @Override
    public List<Person> findAll() {
        return this.jpaRepository.findAll().stream().map(PersonEntry::toDomainAggregate)
            .collect(Collectors.toList());
    }

    @Override
    public Person changeName(final UID id, final Name newName) {
        PersonEntry entry = this.findEntryById(id.getValue());
        entry.setName(newName.getValue());
        PersonEntry savedEntry = this.jpaRepository.save(entry);
        return savedEntry.toDomainAggregate();
    }

    @Override
    public Person updateFromIngestion(final UID id, final IngestionPersonDpo newPersonPayload) {
        PersonEntry entry = this.findEntryById(id.getValue());
        Person toSave = entry.toDomainAggregate();
        try {
            toSave.updateFromIngestion(newPersonPayload);
        } catch (InvalidIngestionCommandException e) {
            this.logger.error(e.getMessage());
        }
        PersonEntry savedEntry = this.jpaRepository.save(new PersonEntry(toSave));
        return savedEntry.toDomainAggregate();
    }

    @Override
    public Person updateInfo(final Person person) {
        return this.jpaRepository.save(new PersonEntry(person)).toDomainAggregate();
    }

    @Override
    public Long totalCount() {
        return this.jpaRepository.count();
    }

}
