package com.ccsw.tutorial.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.NotAvailableForUseException;
import com.ccsw.tutorial.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    private static final String EXISTS_CLIENT_NAME = "repeatedName";
    private static final String NEW_CLIENT_NAME = "Aidan Payne";
    private static final Long EXISTS_CLIENT_ID = 1L;
    private static final Long NEW_CLIENT_ID = 7L;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void findAll_ShouldReturnAllClients() {
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    @Test
    public void saveNotExistsClientId_WithNotExistsName_ShouldInsert()
            throws NotAvailableForUseException, ResourceNotFoundException {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_CLIENT_NAME);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        clientService.save(null, clientDto);

        verify(clientRepository).save(client.capture());

        assertEquals(NEW_CLIENT_NAME, client.getValue().getName());
    }

    @Test
    public void saveNotExistsClientId_WithExistsName_ShoulThrowNameAlreadyExistsException() {
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTS_CLIENT_NAME);

        when(clientRepository.findByName(EXISTS_CLIENT_NAME)).thenReturn(list);

        try {
            clientService.save(null, clientDto);
            fail("Exception expected");
        } catch (NotAvailableForUseException e) {
            assertEquals(NotAvailableForUseException.class, e.getClass());
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void saveExistsClientId_WithNotExistsName_ShouldUpdate()
            throws NotAvailableForUseException, ResourceNotFoundException {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_CLIENT_NAME);

        Client client = mock(Client.class);

        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        clientService.save(EXISTS_CLIENT_ID, clientDto);

        verify(clientRepository).save(client);
    }

    @Test
    public void saveExistsClientId_WithExistsName_ShoulThrowNameAlreadyExistsException() {
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTS_CLIENT_NAME);

        when(clientRepository.findByName(EXISTS_CLIENT_NAME)).thenReturn(list);

        try {
            clientService.save(EXISTS_CLIENT_ID, clientDto);
            fail("Exception expected");
        } catch (NotAvailableForUseException e) {
            assertEquals(NotAvailableForUseException.class, e.getClass());
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void saveWithWrongClientId_ShouldThrowResourceNotFoundException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_CLIENT_NAME);

        when(clientRepository.findById(NEW_CLIENT_ID)).thenReturn(Optional.empty());

        try {
            clientService.save(NEW_CLIENT_ID, clientDto);
            fail("Exception expected");
        } catch (ResourceNotFoundException e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
    }
}
