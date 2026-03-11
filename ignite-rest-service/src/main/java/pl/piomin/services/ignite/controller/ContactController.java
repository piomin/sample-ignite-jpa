package pl.piomin.services.ignite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.repository.ContactRepository;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    ContactRepository repository;

    @PostMapping
    public Contact add(@RequestBody Contact contact) {
        contact.init();
        return repository.save(contact.getId(), contact);
    }

    @GetMapping("/{id}")
    public Contact findById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/location/{location}")
    public List<Contact> findById(@PathVariable String location) {
        return repository.findByLocation(location);
    }

}
