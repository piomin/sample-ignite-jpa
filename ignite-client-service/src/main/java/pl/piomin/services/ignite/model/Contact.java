package pl.piomin.services.ignite.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Contact implements Serializable {

	private static final long serialVersionUID = -5350789537473512833L;
	private static final AtomicLong ID_GEN = new AtomicLong();
	
	@QuerySqlField(index = true)
	private Long id;
	@QuerySqlField
	private ContactType type;
	@QuerySqlField(index = true)
	private String location;
	@QuerySqlField(index = true)
	private Long personId;

	public void init() {
		this.id = ID_GEN.incrementAndGet();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

}
