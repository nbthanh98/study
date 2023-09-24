package com.thanhnb.jwtauth.repository.jwt;

import com.thanhnb.jwtauth.models.jwt.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users u WHERE u.user_name = :username", nativeQuery = true)
    User findByUsername(@Param(value = "username") String userName);
}
