package com.thanhnb.jwtauth.repository;

import com.thanhnb.jwtauth.models.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privileges, Integer> {

    @Query(value = "SELECT p.* FROM privileges p " +
            " INNER JOIN roles_privileges rp ON (p.id = rp.privilege_id) " +
            " INNER JOIN roles r ON (r.id = rp.role_id) " +
            " WHERE r.code = ?1 ", nativeQuery = true)
    List<Privileges> loadPrivilegesBy(String roleCode);
}
