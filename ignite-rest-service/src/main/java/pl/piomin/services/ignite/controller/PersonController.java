package pl.piomin.services.ignite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/contacts/{id}")
	public Person findByIdWithContacts(@PathVariable("id") Long id) {
//		List<Cache.Entry> p = repository.selectPerson(id);
		LOGGER.info("PersonController.findByIdWithContacts: {}", repository.selectPerson(id));
		return null;
	}
	
}
