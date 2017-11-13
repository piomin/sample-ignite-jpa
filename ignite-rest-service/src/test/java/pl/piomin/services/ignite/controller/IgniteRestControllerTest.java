package pl.piomin.services.ignite.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	
	DecimalFormat f = new DecimalFormat("000000000");
	
	@Test
	public void testAddPerson() throws InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();
		for (int j = 0; j < 10; j++) {
			es.execute(() -> {
				TestRestTemplate restTemplateLocal = new TestRestTemplate();
				Random r = new Random();
				for (int i = 0; i < 100000; i++) {
					Person p = restTemplateLocal.postForObject("http://localhost:8090/person", createTestPerson(), Person.class);
					int x = r.nextInt(6);
					for (int k = 0; k < x; k++) {
						restTemplateLocal.postForObject("http://localhost:8090/contact", createTestContact(p.getId()), Contact.class);
					}
				}
			}); 
		}
		es.shutdown();
		es.awaitTermination(60, TimeUnit.MINUTES);
	}
	
	@Test
	public void testFindById() {
		TestRestTemplate restTemplate = new TestRestTemplate();
		for (int i = 0; i < 1000; i++) {
			restTemplate.getForObject("http://localhost:8090/person/{id}", Person.class, i+1);
		}
	}
	
	@Test
	public void testFindContactByLocation() {
		TestRestTemplate restTemplate = new TestRestTemplate();
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/contact/location/{location}", Contact[].class, "location-" + n);
		}
	}
	
	@Test
	public void testFindByName() {
		TestRestTemplate restTemplate = new TestRestTemplate();
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/person/{firstName}/{lastName}", Person[].class, "Test" + n, "Test" + n);
		}
	}
	
	@Test
	public void testFindByNameWithContacts() {
		TestRestTemplate restTemplate = new TestRestTemplate();
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			int n = r.nextInt(100000);
			restTemplate.getForObject("http://localhost:8090/person/contacts/{firstName}/{lastName}", Person[].class, "Test" + n, "Test" + n);
		}
	}
	
	private Person createTestPerson() {
		Random r = new Random();
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
		Random r = new Random();
		Contact c = new Contact();
		c.setPersonId(personId);
		c.setType(ContactType.values()[r.nextInt(4)]);
		c.setLocation("location-" + f.format(Math.abs(r.nextInt())));
		return c;
	}
	
}
