package pl.piomin.services.ignite.controller;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.ContactType;
import pl.piomin.services.ignite.model.Gender;
import pl.piomin.services.ignite.model.Person;

public class IgniteRestControllerTest {

	Random r = new Random();
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void testAddPerson() {
		for (int i = 0; i < 10; i++) {
			Person p = restTemplate.postForObject("http://localhost:8090/person", createTestPerson(), Person.class);
			int x = r.nextInt(6);
			for (int j = 0; j < x; j++) {
				restTemplate.postForObject("http://localhost:8090/contact", createTestContact(p.getId()), Contact.class);
			}
		}
	}
	
	private Person createTestPerson() {
		Person p = new Person();
		int n = r.nextInt(100000);
		p.setFirstName("Test" + n);
		p.setLastName("Test" + n);
		p.setGender(Gender.MALE);
		p.setCountry("PL");
		p.setCity("Test" + n);
		p.setBirthDate(new Date());
		p.setAddress("Address " + n);
		return p;
	}
	
	private Contact createTestContact(Long personId) {
		Contact c = new Contact();
		c.setPersonId(personId);
		c.setType(ContactType.values()[r.nextInt(4)]);
		c.setLocation("location" + r.nextInt(100000));
		return c;
	}
	
}
