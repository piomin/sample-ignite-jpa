package pl.piomin.services.ignite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ignite.cache.query.annotations.QueryGroupIndex;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.annotation.Transient;

@QueryGroupIndex.List(
        @QueryGroupIndex(name = "idx1")
)
public class Person implements Serializable {

    private static final long serialVersionUID = -1271194616130404625L;
    private static final AtomicLong ID_GEN = new AtomicLong();

    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField(index = true)
    @QuerySqlField.Group(name = "idx1", order = 0)
    private String firstName;
    @QuerySqlField(index = true)
    @QuerySqlField.Group(name = "idx1", order = 1)
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private String country;
    private String city;
    private String address;
    @Transient
    private List<Contact> contacts = new ArrayList<>();

    public void init() {
        this.id = ID_GEN.incrementAndGet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
