package com.thanhnb.jwtauth.repository.company;

import com.thanhnb.jwtauth.models.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "companyRepository")
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
