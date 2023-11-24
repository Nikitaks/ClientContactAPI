package com.ClientContactAPI.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.ClientContactAPI.dto.request.ClientDTO;
import com.ClientContactAPI.dto.responce.ClientContactsDTO;
import com.ClientContactAPI.entity.Client;
import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.mapper.EntityDTOmapper;
import com.ClientContactAPI.service.ClientsContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class APIController {
	private static final String CLIENTS_PATH = "/clients";
	@Autowired
	private ClientsContactsService service;

	@Autowired
	private EntityDTOmapper mapper;

	@Operation(summary = "Получение списка клиентов")
    @ApiResponses(value = {
    	@ApiResponse(
           	responseCode = "200",
           	description = "Список клиентов",
           	content = {
               	@Content(
                   	mediaType = "application/json",
                   	array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class)))
            })
    })
	@GetMapping("/clients")
	public List<ClientDTO> getListClientsDTO() {
		return mapper.clientListToClientDTOList(service.findAllClients());
	}


	@Operation(summary = "Добавление нового клиента")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Имя клиента")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Сообщение об успешном добавлении",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Message.class))
					}),
			@ApiResponse(
					responseCode = "400",
					description = "Ошибка проверки входных данных",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Message.class))
					})
	})
	@PostMapping(path = CLIENTS_PATH)
	public ResponseEntity<Message> addClient(@RequestBody ClientDTO clientDTO) {
		if ((clientDTO.getName() == null) || ("".equals(clientDTO.getName()))) {
			return ResponseEntity.badRequest().body(new Message("Name can't be null or empty"));
		}
		Client client = service.saveClient(mapper.clientDTOtoEntity(clientDTO));
		URI uri = URI.create(CLIENTS_PATH + "/" + String.valueOf(client.getId()));
		return ResponseEntity.created(uri).body(new Message("Created successfully"));
	}


	@Operation(summary = "Добавление нового контакта клиента")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Объект, содержащий Id клиента, тип контакта(EMAIL или TELEPHONE) "
					+ "и сам контакт")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Сообщение об успешном добавлении",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Message.class))
					}),
			@ApiResponse(
					responseCode = "400",
					description = "Причина невозможности добавления",
					content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Message.class))
					})
	})
	@PostMapping("/contacts")
	public ResponseEntity<Message> addContact(@RequestBody ClientContactDTO contactsDTO) {
		Message message = checkIfInvalidData(contactsDTO);
		if (message != null) {
			return ResponseEntity.badRequest().body(message);
		}
		Contact contact = service.saveContact(mapper.clientContactDTOtoEntity(contactsDTO));
		URI uri = URI.create(CLIENTS_PATH + "/" + String.valueOf(contact.getClientId()));
		return ResponseEntity.created(uri).body(new Message("Created successfully"));
	}


	@Operation(summary = "Получение списка всех контактов заданного клиента или контактов заданного типа")
	@ApiResponses(value = {
    	@ApiResponse(
           	responseCode = "200",
           	description = "Список контактов",
           	content = {
               	@Content(
                   	mediaType = "application/json",
                   	array = @ArraySchema(schema = @Schema(implementation = ContactDTO.class)))
            }),
    	@ApiResponse(
        	responseCode = "400",
        	description = "Сообщение с причиной невозможности получения списка",
        	content = {
            	@Content(
                	mediaType = "application/json",
                	schema = @Schema(implementation = Message.class))
        })
    })
	@GetMapping(path = "/contacts/{clientId}")
	public ResponseEntity<List<ContactDTO>>
		getListContactType(@PathVariable Long clientId,
						   @RequestParam(required = false) ContactType contactType)
			throws InvalidDataException {

			Message message = checkIfInvalidData(clientId);
			if (message != null) {
				throw new InvalidDataException(message);
			}
			List<Contact> contacts = service.findContactsByClient(clientId, contactType);
			List<ContactDTO> dto = mapper.contactListToContactDTOList(contacts);
			return ResponseEntity.ok().body(dto);
	}

	@Operation(summary = "Получение информации по заданному клиенту (по id)")
    @ApiResponses(value = {
    	@ApiResponse(
           	responseCode = "200",
           	description = "Объект со всеми данными по клиенту",
           	content = {
               	@Content(
                   	mediaType = "application/json",
                   	schema = @Schema(implementation = ClientContactsDTO.class))
            }),
    	@ApiResponse(
        	responseCode = "400",
        	description = "Сообщение с причиной невозможности получения информации",
        	content = {
            	@Content(
                	mediaType = "application/json",
                	schema = @Schema(implementation = Message.class))
        })
    })
	@GetMapping("/clients/{clientId}")
	public ResponseEntity<ClientContactsDTO> getInfoClientId(@PathVariable Long clientId) throws InvalidDataException {
		if (clientId == null) {
			throw (new InvalidDataException(new Message("ClientId can't be null")));
		}
		Optional<Client> optionalClients = service.findClientById(clientId);
		if (optionalClients.isPresent()) {
			Client client = optionalClients.get();
			List<ContactDTO> contacts = getListContactType(clientId, null).getBody();
			ClientContactsDTO dto = mapper.clientContactsToClientContactsDTO(client, contacts);
			return ResponseEntity.ok().body(dto);
		} else {
			throw (new InvalidDataException(new Message("Client not exists")));
		}
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
		if (! service.findClientById(contactsDTO.getClientId()).isPresent()) {
			return new Message("Client not exists");
		}
		return null;
	}

	private Message checkIfInvalidData(Long clientId) {
		if (clientId == null) {
			return new Message("ClientId can't be null");
		}
		if (! service.findClientById(clientId).isPresent()) {
			return new Message("Client not exists");
		}
		return null;
	}

	@ExceptionHandler({Exception.class})
    public ResponseEntity<Message> handleException(Exception e, HttpServletRequest request) {
		if ("BindException".equals(e.getClass().getSimpleName()))
			return ResponseEntity.badRequest()
					.body(new Message("ContactType must be EMAIL for email or TELEPHONE for telephone"));
		if ("HttpMessageNotReadableException".equals(e.getClass().getSimpleName())) {
			return ResponseEntity.badRequest()
					.body(new Message("Parsing JSON error. May be contactType must be EMAIL for email or TELEPHONE for telephone"));
		}
		return ResponseEntity.internalServerError()
				.body(new Message("Internal server exception. Message: "
						+ e.getMessage() + " Path: "
						+ request.getServletPath().toString()
						+ ". exception: " + e.getClass().getSimpleName()));
    }

	@ExceptionHandler({InvalidDataException.class})
	public ResponseEntity<Message> handleInvalidDataException(InvalidDataException e) {
		return ResponseEntity.badRequest().body(e.getMessageDTO());
	}

}
