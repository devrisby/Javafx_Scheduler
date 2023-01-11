package org.devrisby.c195.modules.contact;

import org.devrisby.c195.data.SQLiteDataSource;

import java.util.List;

/** Class for Contact Service business logic */
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService() {
        this.contactRepository = new ContactRepository(SQLiteDataSource.getInstance().getConnection());
    }

    public List<Contact> findAll() {
        return this.contactRepository.findAll();
    }

}
