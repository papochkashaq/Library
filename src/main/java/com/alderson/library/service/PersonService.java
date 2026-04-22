package com.alderson.library.service;

import java.util.List;
import java.util.Optional;

import com.alderson.library.model.Person;
import com.alderson.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public Person findPersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return person.get();
        } else {
            return null;
        }
    }

    public void updatePerson(Long id, Person person) {
        Person updatedPerson = personRepository.findById(id).get();
        updatedPerson.setFullName(person.getFullName());
        updatedPerson.setYearOfBirth(person.getYearOfBirth());
        personRepository.save(updatedPerson);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}
