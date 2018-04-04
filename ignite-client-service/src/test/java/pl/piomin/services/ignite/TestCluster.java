package pl.piomin.services.ignite;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.Assert;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.ContactType;
import pl.piomin.services.ignite.model.Gender;
import pl.piomin.services.ignite.model.Person;

public class TestCluster {

	TestRestTemplate template = new TestRestTemplate();
	Random r = new Random();
	int[] clusterPorts = new int[] {8901, 8902};
	
	@Test
	public void testCluster() throws InterruptedException {
		for (int i=0; i<10000; i++) {
			Person p = template.postForObject("http://localhost:8090/person", createPerson(), Person.class);
			Assert.notNull(p, "Create person failed");
			Contact c1 = template.postForObject("http://localhost:8090/contact", createContact(p.getId(), 0), Contact.class);
			Assert.notNull(c1, "Create contact failed");
			Contact c2 = template.postForObject("http://localhost:8090/contact", createContact(p.getId(), 1), Contact.class);
			Assert.notNull(c2, "Create contact failed");
			Thread.sleep(100);
			Person result = template.getForObject("http://localhost:{port}/person/{id}/withContacts", Person.class, clusterPorts[r.nextInt(2)], p.getId());
			Assert.notNull(result, "Person not found");
			Assert.notEmpty(result.getContacts(), "Contacts not found");
		}
	}

	private Contact createContact(Long personId, int index) {
		Contact c = new Contact();
		c.setPersonId(personId);
		c.setType(ContactType.values()[index]);
		c.setLocation("Test123");
		return c;
	}
	
	private Person createPerson() {
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
	
}
