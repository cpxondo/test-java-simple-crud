package com.test.simple.crud.simplecrud.crud;

import com.test.simple.crud.simplecrud.utils.PDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SimpleService {

    private final Logger log =  LoggerFactory.getLogger(getClass());

    private final SimpleRepository repository;
    private final PDFGenerator generator;

    public SimpleService(SimpleRepository repository, PDFGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    public List<SimpleEntity> getAll() {
        log.info("getAll");

        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public SimpleEntity create(SimpleRequest request) {
        log.info("create");

        SimpleEntity aux = new SimpleEntity();
        aux.setName(request.getName());
        aux.setSurname(request.getSurname());

        repository.save(aux);

        return aux;
    }

    public Optional<SimpleEntity> getById(Long id) {
        log.info("getById");

        return repository.findById(id);
    }

    public void deleteById(Long id) {
        log.info("deleteById");

        repository.deleteById(id);
    }

    public Optional<SimpleEntity> updateById(Long id, SimpleRequest request) {
        log.info("updateById");

        Optional<SimpleEntity> result =  repository.findById(id);
        if (result.isPresent()) {
            SimpleEntity aux = result.get();
            aux.setName(request.getName());
            aux.setSurname(request.getSurname());

            repository.save(aux);
            result = Optional.of(aux);
        }
        return result;
    }

    public ByteArrayInputStream getReportAll() {
        log.info("getReportAll");

        List<SimpleEntity> customers = this.getAll();

        return generator.createReport(customers);

    }
}
