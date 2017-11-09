package pl.piomin.services.ignite.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Contact {

	@QuerySqlField(index = true)
	private Long id;
	private ContactType type;
	private String location;
	@QuerySqlField(index = true)
	private Long personId;

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
