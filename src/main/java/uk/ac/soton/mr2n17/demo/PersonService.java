package uk.ac.soton.mr2n17.demo;

import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private final PersonRepository personRepository;


    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    public Mono<Person> create(Person person) {
       return personRepository.existsById(person.getId())
               .flatMap(exists -> exists ? alreadyExists(person.getId()) : personRepository.save(person));
    }

    public Mono<Person> find(String id) {
        return personRepository.findById(id)
                .switchIfEmpty(doesNotExist(id));
    }

    public Mono<Person> update(String id, Person person) {
        return personRepository.existsById(id).flatMap(exists -> exists ?
                personRepository.save(new Person(id, person.getFirstName(), person.getLastName())) :
                doesNotExist(id));
    }

    public Mono<Void> delete(String id) {
        return personRepository.existsById(id)
                .flatMap(exists -> exists ? personRepository.deleteById(id) : doesNotExist(id));

    }

    private <T> Mono<T> doesNotExist(String id) {
        return Mono.error(new DocumentDoesNotExistException("Person with id " + id + " does not exist"));
    }

    private <T> Mono<T> alreadyExists(String id) {
        return Mono.error(new DocumentAlreadyExistsException("Person with id " + id + " already exists"));
    }
}
