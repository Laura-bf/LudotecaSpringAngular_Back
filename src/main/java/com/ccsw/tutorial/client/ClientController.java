package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.EmptyMandatoryFieldException;
import com.ccsw.tutorial.exception.NotAvailableForUseException;
import com.devonfw.module.beanmapping.common.api.BeanMapper;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private BeanMapper beanMapper;

    @GetMapping
    public List<ClientDto> findAll() {

        return this.beanMapper.mapList(this.clientService.findAll(), ClientDto.class);
    }

    @PutMapping(path = { "", "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto clientDto)
            throws NotAvailableForUseException, EmptyMandatoryFieldException {
        this.clientService.save(id, clientDto);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.clientService.delete(id);
    }
}
