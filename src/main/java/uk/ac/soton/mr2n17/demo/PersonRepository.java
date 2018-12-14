package uk.ac.soton.mr2n17.demo;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

public interface PersonRepository extends ReactiveCouchbaseRepository<Person, String> {
}
