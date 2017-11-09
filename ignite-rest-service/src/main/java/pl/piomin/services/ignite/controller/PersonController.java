package pl.piomin.services.ignite.controller;

import java.util.List;

import org.apache.ignite.IgniteAtomicSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.ignite.model.Person;
import pl.piomin.services.ignite.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	@Qualifier("personSequence")
	IgniteAtomicSequence sequence;
	@Autowired
	PersonRepository repository;
	
	@PostMapping
	public Person add(@RequestBody Person person) {
		long id = sequence.incrementAndGet();
		person.setId(id);
		return repository.save(id, person);
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}
	
	@GetMapping("/contacts/{id}")
	public Person findByIdWithContacts(@PathVariable("id") Long id) {
		Person p = repository.selectPerson(id);
		LOGGER.info("PersonController.findByIdWithContacts: {}", p);
		return null;
	}
	
}
