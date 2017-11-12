package pl.piomin.services.ignite.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.web.client.TestRestTemplate;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.ContactType;
import pl.piomin.services.ignite.model.Gender;
import pl.piomin.services.ignite.model.Person;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IgniteRestControllerTest {

	private static long index = 0;
	
	DecimalFormat f = new DecimalFormat("000000000");
	Random r = new Random();
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void testAddPerson() {
		for (int i = 0; i < 1000000; i++) {
			Person p = restTemplate.postForObject("http://localhost:8090/person", createTestPerson(), Person.class);
			int x = r.nextInt(6);
			for (int j = 0; j < x; j++) {
				restTemplate.postForObject("http://localhost:8090/contact", createTestContact(p.getId()), Contact.class);
			}
		}
	}
	
//	@Test
	public void testFindById() {
		for (int i = 0; i < 1000; i++) {
			restTemplate.getForObject("http://localhost:8090/person/{id}", Person.class, i+1);
		}
	}
	
//	@Test
	public void testFindContactByLocation() {
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/contact/{location}", Contact[].class, "location-" + n);
		}
	}
	
//	@Test
	public void testFindByName() {
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/person/{firstName}/{lastName}", Person[].class, "Test" + n, "Test" + n);
		}
	}
	
//	@Test
	public void testFindByNameWithContacts() {
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/person/contacts/{firstName}/{lastName}", Person[].class, "Test" + n, "Test" + n);
		}
	}
	
	private Person createTestPerson() {
		Person p = new Person();
		int n = r.nextInt(100000);
		p.setFirstName("Test" + n);
		p.setLastName("Test" + n);
		p.setGender(Gender.values()[r.nextInt(2)]);
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
		c.setLocation("location-" + f.format(index++));
		return c;
	}
	
}
