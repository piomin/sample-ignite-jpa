package pl.piomin.services.ignite.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.ContactType;
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
	
	@PutMapping
	public Person update(@RequestBody Person person) {
		return repository.save(person.getId(), person);
	}
	
	@DeleteMapping("/{id}")
	public void delete(Long id) {
		repository.delete(id);
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}
	
	@GetMapping("/{firstName}/{lastName}")
	public List<Person> findByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		return repository.findByFirstNameAndLastName(firstName, lastName);
	}
	
	@GetMapping("/contacts/{firstName}/{lastName}")
	public List<Person> findByNameWithContacts(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		List<Person> persons = repository.findByFirstNameAndLastName(firstName, lastName);
		List<Contact> contacts = repository.selectContacts(firstName, lastName);
		persons.stream().forEach(it -> it.setContacts(contacts.stream().filter(c -> c.getPersonId().equals(it.getId())).collect(Collectors.toList())));
		LOGGER.info("PersonController.findByIdWithContacts: {}", contacts);
		return persons;
	}
	
	@GetMapping("/contacts2/{firstName}/{lastName}")
	public List<Person> findByNameWithContacts2(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		List<List<?>> result = repository.selectContacts2(firstName, lastName);
		List<Person> persons = new ArrayList<>();
		for (List<?> l : result) {
			persons.add(mapPerson(l));
		}
		LOGGER.info("PersonController.findByIdWithContacts: {}", result);
		return persons;
	}
	
	@GetMapping("/{id}/withContacts")
	public Person findByIdWithContacts(@PathVariable("id") Long id) {
		List<List<?>> result = repository.findByIdWithContacts(id);
		if (result.isEmpty())
			return null;
		Person p = new Person();
		for (List<?> l : result) {
			mapPerson(p, l);
		}
		LOGGER.info("PersonController.findByIdWithContacts: {}", result);
		return p;
	}
	
	private Person mapPerson(List<?> l) {
		Person p = new Person();
		Contact c = new Contact();
		p.setId((Long) l.get(0));
		p.setFirstName((String) l.get(1));
		p.setLastName((String) l.get(2));
		c.setId((Long) l.get(3));
		c.setType((ContactType) l.get(4));
		c.setLocation((String) l.get(5));
		p.addContact(c);
		return p;
	}
	
	private Person mapPerson(Person p, List<?> l) {
		Contact c = new Contact();
		p.setId((Long) l.get(0));
		p.setFirstName((String) l.get(1));
		p.setLastName((String) l.get(2));
		c.setId((Long) l.get(3));
		c.setType((ContactType) l.get(4));
		c.setLocation((String) l.get(5));
		p.addContact(c);
		return p;
	}
	
}
