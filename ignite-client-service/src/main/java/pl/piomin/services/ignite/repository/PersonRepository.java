package pl.piomin.services.ignite.repository;

import java.util.List;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.Query;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.Person;

@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Long> {
	
	List<Person> findByFirstNameAndLastName(String firstName, String lastName);
	
	@Query("SELECT p.id, p.firstName, p.lastName, c.id, c.type, c.location FROM Person p JOIN \"ContactCache\".Contact c ON p.id=c.personId WHERE p.id=?")
	List<List<?>> findByIdWithContacts(Long id);
	
	@Query("SELECT c.* FROM Person p JOIN \"ContactCache\".Contact c ON p.id=c.personId WHERE p.firstName=? and p.lastName=?")
	List<Contact> selectContacts(String firstName, String lastName);
	
	@Query("SELECT p.id, p.firstName, p.lastName, c.id, c.type, c.location FROM Person p JOIN \"ContactCache\".Contact c ON p.id=c.personId WHERE p.firstName=? and p.lastName=?")
	List<List<?>> selectContacts2(String firstName, String lastName);
}
