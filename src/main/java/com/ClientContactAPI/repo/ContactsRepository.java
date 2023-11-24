package com.ClientContactAPI.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ClientContactAPI.entity.ContactType;
import com.ClientContactAPI.entity.Contact;

public interface ContactsRepository extends CrudRepository<Contact, Long> {
	List<Contact> findByclientIdAndContactType(Long clientId, ContactType contactType);
	List<Contact> findByclientId(Long clientId);
}
