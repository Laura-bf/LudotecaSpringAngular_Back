package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.NotAvailableForUseException;
import com.ccsw.tutorial.exception.ResourceNotFoundException;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto clientDto) throws NotAvailableForUseException, ResourceNotFoundException {

        if (!this.clientRepository.findByName(clientDto.getName()).isEmpty())
            throw new NotAvailableForUseException("Name already registered");

        Client client = null;

        if (id == null)
            client = new Client();
        else
            client = this.clientRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        client.setName(clientDto.getName());

        this.clientRepository.save(client);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        try {
            this.clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
