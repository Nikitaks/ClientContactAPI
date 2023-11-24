package com.ClientContactAPI.dto.request;

import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.entity.ContactType;


public class ClientContactDTO {
    
	private Long clientId;
    private ContactType contactType;
    private String contact;
    
    public ClientContactDTO() {
		super();
	}

	public ClientContactDTO(Long clientId, ContactType contactType, String contact) {
		super();
		this.clientId = clientId;
		this.contactType = contactType;
		this.contact = contact;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Contact toEntity() {
		return new Contact(0, this.clientId, this.contactType, this.contact);
	}
	
}
