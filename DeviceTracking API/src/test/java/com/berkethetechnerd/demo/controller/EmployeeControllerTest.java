package com.berkethetechnerd.demo.controller;

import com.berkethetechnerd.demo.entity.Employee;
import com.berkethetechnerd.demo.entity.Position;
import com.berkethetechnerd.demo.entity.Status;
import com.berkethetechnerd.demo.exception.EmployeeNotFoundException;
import com.berkethetechnerd.demo.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests for EmployeeController API
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @InjectMocks
    EmployeeController employeeController;

    @Mock
    EmployeeService employeeService;

    @Test
    public void it_should_return_all_two_employees() {
        // Init
        Employee testEmployee1 = new Employee();
        Integer testId1 = 123;
        testEmployee1.setId(testId1);

        Employee testEmployee2 = new Employee();
        Integer testId2 = 1234;
        testEmployee2.setId(testId2);

        List<Employee> testList = Arrays.asList(testEmployee1, testEmployee2);

        // Setup
        when(employeeService.getAllEmployees()).thenReturn(testList);

        // Execute
        List<Employee> resultList = employeeController.getAllEmployees();

        // Check
        assertEquals(testList, resultList);
        assertEquals(testList.size(), resultList.size());
    }

    @Test
    public void it_should_return_no_employees() {
        // Init
        List<Employee> testList = new ArrayList<>();

        // Setup
        when(employeeService.getAllEmployees()).thenReturn(testList);

        // Execute
        List<Employee> resultList = employeeController.getAllEmployees();

        // Check
        assertEquals(testList, resultList);
        assertEquals(testList.size(), resultList.size());
    }

    @Test
    public void it_should_return_employee_when_found_by_id() {
        // Init
        Employee testEmployee = new Employee();
        Integer testId = 123;
        testEmployee.setId(testId);

        // Setup
        when(employeeService.getEmployeeById(testId)).thenReturn(testEmployee);

        // Execute
        Employee resultEmployee = employeeController.getEmployeeById(testId);

        // Check
        assertEquals(testEmployee, resultEmployee);
        assertEquals(testEmployee.getId(), resultEmployee.getId());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void it_should_throw_exception_when_employee_not_found_by_id() {
        // Init
        Integer testId = 123;
        EmployeeNotFoundException testException = new EmployeeNotFoundException(testId);

        // Setup
        when(employeeService.getEmployeeById(testId)).thenThrow(testException);

        // Execute
        employeeController.getEmployeeById(testId);
    }

    @Test
    public void it_should_add_new_employee_and_return_employee() {
        // Init
        Employee testEmployee = new Employee();
        Integer testId = 123;
        testEmployee.setId(testId);

        Status testStatus = new Status();
        Integer statusId = 1234;
        testStatus.setId(statusId);
        testEmployee.setStatus(testStatus);

        Position testPosition = new Position();
        Integer positionId = 12;
        testPosition.setId(positionId);
        testEmployee.setPosition(testPosition);

        // Setup
        when(employeeService.addNewEmployee(testEmployee)).thenReturn(testEmployee);

        // Execute
        Employee resultEmployee = employeeController.addNewEmployee(testEmployee);

        // Check
        assertEquals(testEmployee, resultEmployee);
        assertEquals(testEmployee.getId(), resultEmployee.getId());
        assertEquals(testEmployee.getPosition(), resultEmployee.getPosition());
        assertEquals(testEmployee.getPosition().getId(), resultEmployee.getPosition().getId());
        assertEquals(testEmployee.getStatus(), resultEmployee.getStatus());
        assertEquals(testEmployee.getStatus().getId(), resultEmployee.getStatus().getId());
    }

    @Test
    public void it_should_return_updated_employee_upon_update() {
        // Init
        Integer testId = 123;
        String address = "Istanbul, Turkey";

        Employee testEmployee = new Employee();
        testEmployee.setId(testId);
        testEmployee.setAddress(address);

        // Setup
        when(employeeService.updateEmployee(testId, testEmployee)).thenReturn(testEmployee);

        // Execute
        Employee resultEmployee = employeeController.updateEmployee(testId, testEmployee);

        // Check
        assertEquals(testEmployee, resultEmployee);
        assertEquals(testEmployee.getId(), resultEmployee.getId());
        assertEquals(testEmployee.getAddress(), resultEmployee.getAddress());
    }

    @Test
    public void it_should_delete_and_return_when_employee_found() {
        // Init
        Employee testEmployee = new Employee();
        Integer testId = 123;
        testEmployee.setId(testId);

        // Setup
        when(employeeService.deleteEmployee(testId)).thenReturn(testEmployee);

        // Execute
        Employee resultEmployee = employeeController.deleteEmployee(testId);

        // Check
        assertEquals(testEmployee, resultEmployee);
        assertEquals(testEmployee.getId(), resultEmployee.getId());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void it_should_throw_exception_on_delete_when_employee_not_found() {
        // Init
        Integer testId = 123;
        EmployeeNotFoundException testException = new EmployeeNotFoundException(testId);

        // Setup
        when(employeeService.deleteEmployee(testId)).thenThrow(testException);

        // Execute
        employeeController.deleteEmployee(testId);
    }
}