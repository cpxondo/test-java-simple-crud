package com.test.simple.crud.simplecrud.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/simple/crud")
public class SimpleController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SimpleService service;

    public SimpleController(SimpleService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<SimpleEntity>> getAll() {
        log.info("getAll");

        List<SimpleEntity> result = service.getAll();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SimpleEntity> create(@RequestBody SimpleRequest request) {
        log.info("create - Request {}", request.toJson());

        SimpleEntity result = service.create(request);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SimpleEntity> getById(@PathVariable(value = "id") Long id) {
        log.info("getById - Request {}", id);

        Optional<SimpleEntity> entityOptional = service.getById(id);
        return entityOptional
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Long id) {
        log.info("deleteById - Request {}", id);

        service.deleteById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<SimpleEntity> updateById(@PathVariable(value = "id") Long id, @RequestBody SimpleRequest request) {
        log.info("updateById - Request {}, {}", id, request.toJson());

        Optional<SimpleEntity> entityOptional = service.updateById(id, request);
        return entityOptional
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> allReport() {
        ByteArrayInputStream bis = service.getReportAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=customers.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
