package com.thanhnb.jwtauth.controllers;

import com.thanhnb.jwtauth.models.company.Company;
import com.thanhnb.jwtauth.models.employee.Employee;
import com.thanhnb.jwtauth.models.jwt.ApiSecure;
import com.thanhnb.jwtauth.models.jwt.PrivilegeEnum;
import com.thanhnb.jwtauth.models.jwt.RoleEnum;
import com.thanhnb.jwtauth.repository.company.CompanyRepository;
import com.thanhnb.jwtauth.repository.employee.EmployeeRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class DemoControllers {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public DemoControllers(CompanyRepository companyRepository,
                           EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @ApiSecure(
            requiredRoles = RoleEnum.ADMIN,
            requiredPrivileges = {
                    PrivilegeEnum.READ,
                    PrivilegeEnum.VIEW
            })
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }


    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test2() {
        Company company = new Company();
        company.setName("thanhnb1");
        companyRepository.save(company);

        Employee employee = new Employee();
        employee.setName("thanhnb");
        employeeRepository.save(employee);
        return "test";
    }
}
