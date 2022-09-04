package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.NotAvailableForUseException;

public interface ClientService {

    List<Client> findAll();

    void save(Long id, ClientDto clientDto) throws NotAvailableForUseException;

    void delete(Long id);

}
