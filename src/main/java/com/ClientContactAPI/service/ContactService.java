package com.ClientContactAPI.service;

import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.entity.ContactType;

import java.util.List;

public interface ContactService {
    Contact saveContact(Contact contact);

    List<Contact> findContactsByClient(Long clientId, ContactType contactType);
}
