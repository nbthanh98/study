package com.thanhnb.jwtauth.repository.employee;

import com.thanhnb.jwtauth.models.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
