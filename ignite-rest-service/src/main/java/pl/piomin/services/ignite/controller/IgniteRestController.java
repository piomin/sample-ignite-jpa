package pl.piomin.services.ignite.controller;

import org.apache.ignite.IgniteAtomicSequence;
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
public class IgniteRestController {

	@Autowired
	IgniteAtomicSequence sequence;
	@Autowired
	PersonRepository repository;
	
	@PostMapping
	public Person add(@RequestBody Person person) {
		return repository.save(sequence.incrementAndGet(), person);
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}
	
}
