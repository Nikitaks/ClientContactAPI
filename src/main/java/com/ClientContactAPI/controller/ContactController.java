package com.ClientContactAPI.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.mapper.EntityDTOmapper;
import com.ClientContactAPI.service.ClientService;
import com.ClientContactAPI.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ClientContactAPI.dto.request.ClientContactDTO;
import com.ClientContactAPI.dto.responce.ContactDTO;
import com.ClientContactAPI.dto.Message;
import com.ClientContactAPI.entity.ContactType;
import com.ClientContactAPI.exception.InvalidDataException;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ContactController {
	@Autowired
	private ContactService contactService;
	@Autowired
	private ClientService clientService;

	@Autowired
	private EntityDTOmapper mapper;

	@Operation(summary = "Добавление нового контакта клиента")
	@PostMapping("/contacts")
	public ResponseEntity<Message> addContact(@RequestBody ClientContactDTO contactsDTO) {
		Message message = checkIfInvalidData(contactsDTO);
		if (message != null) {
			return ResponseEntity.badRequest().body(message);
		}
		Contact contact = contactService.saveContact(mapper.clientContactDTOtoEntity(contactsDTO));
		return ResponseEntity.created(null).body(new Message("Created successfully"));
	}


	@Operation(summary = "Получение списка всех контактов заданного клиента или контактов заданного типа")
	@GetMapping(path = "/contacts/{clientId}")
	public ResponseEntity<List<ContactDTO>>
		getListContactType(@PathVariable Long clientId,
						   @RequestParam(required = false) ContactType contactType)
			throws InvalidDataException {

			Message message = checkIfInvalidData(clientId);
			if (message != null) {
				throw new InvalidDataException(message);
			}
			List<Contact> contacts = contactService.findContactsByClient(clientId, contactType);
			List<ContactDTO> dto = mapper.contactListToContactDTOList(contacts);
			return ResponseEntity.ok().body(dto);
	}




	private Message checkIfInvalidData(ClientContactDTO contactsDTO) {
		if (contactsDTO.getClientId() == null) {
			return new Message("ClientId can't be null");
		}
		if ((! ContactType.EMAIL.equals(contactsDTO.getContactType()))
			  && (! ContactType.TELEPHONE.equals(contactsDTO.getContactType()))) {
			return new Message("ContactType must be \"t\" for telephone or \"e\" for email");
		}
		if ((contactsDTO.getContact() == null) || ("".equals(contactsDTO.getContact()))) {
			return new Message("Contact can't be null or empty");
		}
		if (! clientService.findClientById(contactsDTO.getClientId()).isPresent()) {
			return new Message("Client not exists");
		}
		return null;
	}

	private Message checkIfInvalidData(Long clientId) {
		if (clientId == null) {
			return new Message("ClientId can't be null");
		}
		if (! clientService.findClientById(clientId).isPresent()) {
			return new Message("Client not exists");
		}
		return null;
	}
}
