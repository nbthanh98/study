package com.thanhnb.jwtauth.repository;

import com.thanhnb.jwtauth.models.Privileges;
import com.thanhnb.jwtauth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT r.* FROM roles r " +
            " INNER JOIN users_roles ur ON (r.id = ur.role_id) " +
            " INNER JOIN users u ON (u.id = ur.user_id) " +
            " WHERE u.user_name = ?1", nativeQuery = true)
    List<Role> loadRolesOfUserBy(String username);
}
