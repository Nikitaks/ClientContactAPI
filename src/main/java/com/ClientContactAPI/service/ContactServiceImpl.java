package com.ClientContactAPI.service;

import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.entity.ContactType;
import com.ClientContactAPI.repo.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactsRepository contactsRepository;

    @Override
    public Contact saveContact(Contact contact) {
        return contactsRepository.save(contact);
    }

    @Override
    public List<Contact> findContactsByClient(Long clientId, ContactType contactType) {
        if (contactType != null) {
            return contactsRepository.findByclientIdAndContactType(clientId, contactType);
        }
        else {
            return contactsRepository.findByclientId(clientId);
        }
    }
}
