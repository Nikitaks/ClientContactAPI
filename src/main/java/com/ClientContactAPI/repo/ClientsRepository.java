package com.ClientContactAPI.repo;

import java.util.List;

import com.ClientContactAPI.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientsRepository extends CrudRepository<Client, Long> {
	List<Client> findAll();
}
