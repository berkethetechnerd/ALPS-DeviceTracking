package com.berkethetechnerd.demo.controller;

import com.berkethetechnerd.demo.entity.Employee;
import com.berkethetechnerd.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/employees")
public class EmployeeController {

    // Connected service to make queries
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public @ResponseBody List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Employee getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public @ResponseBody Employee addNewEmployee(@RequestBody Employee newEmployee) {
        return employeeService.addNewEmployee(newEmployee);
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee updateEmployee) {
        return employeeService.updateEmployee(id, updateEmployee);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody Employee deleteEmployee(@PathVariable Integer id) {
        return employeeService.deleteEmployee(id);
    }

}
