package pl.piomin.services.ignite.controller;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import pl.piomin.services.ignite.model.Gender;
import pl.piomin.services.ignite.model.Person;

public class IgniteRestControllerTest {

	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void testAddPerson() {
		for (int i = 0; i < 100000; i++) {
			restTemplate.postForObject("http://localhost:8081/person", createTestPerson((long) (i+1)), Person.class);
		}
	}
	
	private Person createTestPerson(Long id) {
		Person p = new Person();
		Random r = new Random();
		int n = r.nextInt(100000);
		p.setId(id);
		p.setFirstName("Test" + n);
		p.setLastName("Test" + n);
		p.setGender(Gender.MALE);
		p.setCountry("PL");
		p.setCity("Test" + n);
		p.setBirthDate(new Date());
		p.setAddress("Address " + n);
		return p;
	}
}
