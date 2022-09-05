package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.EmptyMandatoryFieldException;
import com.ccsw.tutorial.exception.NotAvailableForUseException;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto clientDto) throws NotAvailableForUseException, EmptyMandatoryFieldException {
        if (clientDto.getName().equals(""))
            throw new EmptyMandatoryFieldException("Name cannot be empty");

        if (!this.clientRepository.findByName(clientDto.getName()).isEmpty())
            throw new NotAvailableForUseException("Name already registered");

        Client client = null;

        if (id == null)
            client = new Client();
        else
            client = this.clientRepository.findById(id).orElse(null);

        client.setName(clientDto.getName());

        this.clientRepository.save(client);
    }

    @Override
    public void delete(Long id) {
        this.clientRepository.deleteById(id);
    }
}
