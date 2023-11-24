package com.ClientContactAPI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
@Table(name = "CONTACTS")
public class Contact {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private long id;
	@Column(name = "CLIENT_ID")
    private long clientId;

    @Column(name = "CONTACT_TYPE_ID")
	@Enumerated(EnumType.ORDINAL)
    private ContactType contactType;
    private String contact;
    
	public Contact(long id, long clientId, ContactType contactType, String contact) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.contactType = contactType;
		this.contact = contact;
	}
    
}
