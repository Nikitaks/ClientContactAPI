package com.ClientContactAPI.service;

import com.ClientContactAPI.entity.Client;
import com.ClientContactAPI.repo.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientsRepository clientsRepository;

    @Override
    public Client saveClient(Client client) {
        return clientsRepository.save(client);
    }

    @Override
    public List<Client> findAllClients() {
        return clientsRepository.findAll();
    }

    @Override
    public Optional<Client> findClientById(Long clientId) {
        return clientsRepository.findById(clientId);
    }
}
