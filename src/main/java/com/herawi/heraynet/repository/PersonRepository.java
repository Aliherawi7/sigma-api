package com.herawi.heraynet.repository;

import com.herawi.heraynet.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person getPersonByEmail(String email);
    Collection<Person> findAllByNameOrLastName(String nameOrLastName);

}
