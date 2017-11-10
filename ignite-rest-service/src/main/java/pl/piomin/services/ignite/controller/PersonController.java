package pl.piomin.services.ignite.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.Person;
import pl.piomin.services.ignite.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	PersonRepository repository;
	
	@PostMapping
	public Person add(@RequestBody Person person) {
		person.init();
		return repository.save(person.getId(), person);
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}
	
	@GetMapping("/contacts/{firstName}/{lastName}")
	public List<Person> findByNameWithContacts(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		List<Person> persons = repository.findByFirstNameAndLastName(firstName, lastName);
		List<Contact> contacts = repository.selectContacts(firstName, lastName);
		contacts.stream().filter(c -> c.getPersonId().equals(0)).collect(Collectors.toList());
		LOGGER.info("PersonController.findByIdWithContacts: {}", contacts);
		return persons;
	}
	
}
