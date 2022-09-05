package com.ccsw.tutorial.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.ccsw.tutorial.client.model.ClientDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/client/";

    public static final int TOTAL_CLIENTS_IN_DB = 5;
    public static final Long EXIST_CLIENT_ID = 1L;
    public static final Long NEW_CLIENT_ID = 7L;
    public static final String EXIST_CLIENT_NAME = "Aidan Payne";
    public static final String NEW_CLIENT_NAME = "NameNotUsed";
    public static final String EMPTY_CLIENT_NAME = "";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<ClientDto>> responseType = new ParameterizedTypeReference<List<ClientDto>>() {
    };

    private ResponseEntity<List<ClientDto>> findAllClients() {
        return restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
    }

    @Test
    public void findAll_ShouldReturnAllClients() {
        ResponseEntity<List<ClientDto>> response = findAllClients();

        assertNotNull(response);
        assertEquals(TOTAL_CLIENTS_IN_DB, response.getBody().size());
    }

    @Test
    public void saveWithoutId_WithNewName_ShouldCreateNewClient() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> response = findAllClients();

        assertNotNull(response);
        assertEquals(TOTAL_CLIENTS_IN_DB + 1, response.getBody().size());
    }

    @Test
    public void saveWithoutId_WithExistName_ShouldReturnConflict() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXIST_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> responseAfterSave = findAllClients();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(TOTAL_CLIENTS_IN_DB, responseAfterSave.getBody().size());
    }

    @Test
    public void saveWithoutId_WithEmptyName_ShouldReturnBadRequest() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EMPTY_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> responseAfterSave = findAllClients();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TOTAL_CLIENTS_IN_DB, responseAfterSave.getBody().size());
    }

    @Test
    public void updateWithExistId_WithNewName_ShouldUpdateClient() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXIST_CLIENT_ID);
        clientDto.setName(NEW_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXIST_CLIENT_ID, HttpMethod.PUT,
                new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> response = findAllClients();

        ClientDto updatedClient = response.getBody().stream().filter(c -> c.getId().equals(EXIST_CLIENT_ID)).findFirst()
                .orElse(null);

        assertEquals(TOTAL_CLIENTS_IN_DB, response.getBody().size());
        assertEquals(NEW_CLIENT_NAME, updatedClient.getName());
    }

    @Test
    public void updateWithExistId_WithExistName_ShouldReturnConflict() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXIST_CLIENT_ID);
        clientDto.setName(EXIST_CLIENT_NAME);

        ResponseEntity<List<ClientDto>> responseBeforeUpdate = findAllClients();
        ClientDto clientBeforeUpdate = responseBeforeUpdate.getBody().stream()
                .filter(c -> c.getId().equals(EXIST_CLIENT_ID)).findFirst().orElse(null);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXIST_CLIENT_ID,
                HttpMethod.PUT, new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> responseAfterUpdate = findAllClients();
        ClientDto clientAfterUpdate = responseAfterUpdate.getBody().stream()
                .filter(c -> c.getId().equals(EXIST_CLIENT_ID)).findFirst().orElse(null);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(clientBeforeUpdate.getName(), clientAfterUpdate.getName());
    }

    @Test
    public void updateWithExistId_WithEmptyName_ShouldReturnBadRequest() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXIST_CLIENT_ID);
        clientDto.setName(EMPTY_CLIENT_NAME);

        ResponseEntity<List<ClientDto>> responseBeforeUpdate = findAllClients();
        ClientDto clientBeforeUpdate = responseBeforeUpdate.getBody().stream()
                .filter(c -> c.getId().equals(EXIST_CLIENT_ID)).findFirst().orElse(null);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXIST_CLIENT_ID,
                HttpMethod.PUT, new HttpEntity<>(clientDto), void.class);

        ResponseEntity<List<ClientDto>> responseAfterUpdate = findAllClients();
        ClientDto clientAfterUpdate = responseAfterUpdate.getBody().stream()
                .filter(c -> c.getId().equals(EXIST_CLIENT_ID)).findFirst().orElse(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(clientBeforeUpdate.getName(), clientAfterUpdate.getName());
    }

    @Test
    public void updateWithNotExistId_ShouldReturnInternalServerError() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NEW_CLIENT_ID,
                HttpMethod.PUT, new HttpEntity<>(clientDto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistId_ShoulDeleteClient() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXIST_CLIENT_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<ClientDto>> response = findAllClients();

        assertNotNull(response);
        assertEquals(TOTAL_CLIENTS_IN_DB - 1, response.getBody().size());

    }

    @Test
    public void deleteWithNotExistId_ShouldReturnInternalServerError() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NEW_CLIENT_ID,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
