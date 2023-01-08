package org.devrisby.c195.services;

import org.devrisby.c195.data.ContactRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Contact;

import java.util.List;

public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService() {
        this.contactRepository = new ContactRepository(DB.getConnection());
    }

    public List<Contact> findAll() {
        return this.contactRepository.findAll();
    }

}
