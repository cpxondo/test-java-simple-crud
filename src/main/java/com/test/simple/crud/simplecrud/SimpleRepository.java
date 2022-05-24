package com.test.simple.crud.simplecrud;

import org.springframework.data.repository.CrudRepository;

public interface SimpleRepository extends CrudRepository<SimpleEntity, Long> {
}
