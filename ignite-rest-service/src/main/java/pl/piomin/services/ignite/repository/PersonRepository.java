package pl.piomin.services.ignite.repository;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.Query;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import pl.piomin.services.ignite.model.Person;

@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Long> {
	
	@Query("SELECT p.id, p.firstnName FROM Person p JOIN \"ContactCache\".Contact c ON p.id=c.personId WHERE p.id=?")
	Person selectPerson(Long id);
	
}
