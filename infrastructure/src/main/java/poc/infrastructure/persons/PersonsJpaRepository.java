package poc.infrastructure.persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonsJpaRepository extends JpaRepository<PersonEntry, String> {

}
