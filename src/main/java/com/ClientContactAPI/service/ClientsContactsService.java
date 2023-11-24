package com.ClientContactAPI.service;

import com.ClientContactAPI.entity.Client;
import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.entity.ContactType;
import com.ClientContactAPI.repo.ClientsRepository;
import com.ClientContactAPI.repo.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsContactsService {
    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private ClientsRepository clientsRepository;

    public Contact saveContact(Contact contact) {
        return contactsRepository.save(contact);
    }

    public Client saveClient(Client client) {
        return clientsRepository.save(client);
    }

    public List<Contact> findContactsByClient(Long clientId, ContactType contactType) {
        if (contactType != null) {
            return contactsRepository.findByclientIdAndContactType(clientId, contactType);
        }
        else {
            return contactsRepository.findByclientId(clientId);
        }
    }

    public List<Client> findAllClients() {
        return clientsRepository.findAll();
    }

    public Optional<Client> findClientById(Long clientId) {
        return clientsRepository.findById(clientId);
    }
}
