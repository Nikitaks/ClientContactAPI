package com.ClientContactAPI.service;

import com.ClientContactAPI.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client saveClient(Client client);

    List<Client> findAllClients();

    Optional<Client> findClientById(Long clientId);
}
