package com.berkethetechnerd.demo.repository;

import com.berkethetechnerd.demo.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> { }
