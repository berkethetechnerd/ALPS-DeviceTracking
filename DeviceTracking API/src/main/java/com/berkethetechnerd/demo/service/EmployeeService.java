package com.berkethetechnerd.demo.service;

import com.berkethetechnerd.demo.entity.Employee;
import com.berkethetechnerd.demo.entity.Position;
import com.berkethetechnerd.demo.entity.Status;
import com.berkethetechnerd.demo.exception.EmployeeNotFoundException;
import com.berkethetechnerd.demo.repository.EmployeeRepository;
import com.berkethetechnerd.demo.repository.PositionRepository;
import com.berkethetechnerd.demo.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class EmployeeService {

    // Employee storage connection
    private final EmployeeRepository employeeRepository;

    // Status storage connection
    private final StatusRepository statusRepository;

    // Position storage connection
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, StatusRepository statusRepository, PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.statusRepository = statusRepository;
        this.positionRepository = positionRepository;
    }

    /**
     * Queries for all employees registered in the DB
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        employeeRepository.findAll().forEach(employeeList::add); // Turn Iterator into a List
        return employeeList;
    }

    /**
     * Queries for a specific employee instance
     * @param id: Employee id
     * @return Employee instance
     * @throws EmployeeNotFoundException when the given id is not found in DB
     */
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    /**
     * Adds a new employee instance into DB
     * @param newEmployee: Employee instance to register into DB
     * @return The registered new Employee instance
     */
    public Employee addNewEmployee(Employee newEmployee) {
        // Save status
        Status newStatus = Objects.requireNonNullElseGet(newEmployee.getStatus(), Status::new); // Create new if null
        newStatus = statusRepository.save(newStatus);
        newEmployee.setStatus(newStatus);

        // Save position
        Position newPosition = Objects.requireNonNullElseGet(newEmployee.getPosition(), Position::new); // Create new if null
        newPosition = positionRepository.save(newPosition);
        newEmployee.setPosition(newPosition);

        // Create and Update date values are both current time
        Date createTime = new Date();
        newEmployee.setCreated(createTime);
        newEmployee.setUpdated(createTime);

        // Save and return employee
        return employeeRepository.save(newEmployee);
    }

    /**
     * Updates an existing employee instance with new given employee information
     * @param id: Employee id
     * @param updateEmployee: Employee instance that have the newest information
     * @return The updated Employee instance
     * @throws EmployeeNotFoundException when the given id is not found in DB
     */
    public Employee updateEmployee(Integer id, Employee updateEmployee) {
        // Search for the given employee
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        // Employee basic information update
        employee.setAddress(updateEmployee.getAddress());
        employee.setBirthDate(updateEmployee.getBirthDate());
        employee.setName(updateEmployee.getName());

        // If status update exists
        if (updateEmployee.getStatus() != null) {
            employee.getStatus().setDescription(updateEmployee.getStatus().getDescription());
        } else {
            employee.getStatus().setDescription(null);
        }

        // If position update exists
        if (updateEmployee.getPosition() != null) {
            employee.getPosition().setDescription(updateEmployee.getPosition().getDescription());
        } else {
            employee.getPosition().setDescription(null);
        }

        // Save new status and position updates
        statusRepository.save(employee.getStatus());
        positionRepository.save(employee.getPosition());

        // UpdateTime is touched for employee
        Date updateTime = new Date();
        employee.setUpdated(updateTime);

        // Save and return employee
        return employeeRepository.save(employee);
    }

    /**
     * Deletes an existing employee instance
     * @param id: Employee id
     * @return The deleted employee instance
     * @throws EmployeeNotFoundException when the given id is not found in DB
     */
    public Employee deleteEmployee(@PathVariable Integer id) {
        // Search for the given employee
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        // Delete the employee
        employeeRepository.delete(employee);

        // Return the instance
        return employee;
    }

}
