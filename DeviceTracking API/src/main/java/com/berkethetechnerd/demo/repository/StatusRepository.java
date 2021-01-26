package com.berkethetechnerd.demo.repository;

import com.berkethetechnerd.demo.entity.Status;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<Status, Integer> { }
