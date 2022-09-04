package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.NotAvailableForUseException;
import com.ccsw.tutorial.exception.ResourceNotFoundException;

public interface ClientService {

    List<Client> findAll();

    void save(Long id, ClientDto clientDto) throws NotAvailableForUseException, ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;

}
