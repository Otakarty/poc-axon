package poc.synchro.old.refog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poc.domain.person.Person;

@RestController
@RequestMapping("/persons/old-refog")
public class PersonOldRefogController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("old-refog")
    PersonsSynchroRepository repository;

    @GetMapping("/{uid}")
    public Person getPerson(@PathVariable final String uid) {
        return null;// this.repository.findById(new UID(uid));
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return null;// this.repository.findAll();
    }
}
