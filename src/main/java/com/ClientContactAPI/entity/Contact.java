package com.ClientContactAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
