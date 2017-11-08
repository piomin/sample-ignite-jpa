package pl.piomin.services.ignite.controller;

import java.util.Date;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import pl.piomin.services.ignite.model.Gender;
import pl.piomin.services.ignite.model.Person;

public class IgniteRestControllerTest {

	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void testAddPerson() {
		for (int i = 0; i < 10; i++) {
			restTemplate.postForObject("http://localhost:8081/person", createTestPerson((long) (i+1)), Person.class);
		}
	}
	
	private Person createTestPerson(Long id) {
		Person p = new Person();
		p.setId(id);
		p.setFirstName("Test" + id);
		p.setLastName("Test" + id);
		p.setGender(Gender.MALE);
		p.setCountry("PL");
		p.setCity("Test" + id);
		p.setBirthDate(new Date());
		p.setAddress("Address " + id);
		return p;
	}
}
