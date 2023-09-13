package com.thanhnb.jwtauth.service;

import com.thanhnb.jwtauth.exception.CustomException;
import com.thanhnb.jwtauth.models.Role;
import com.thanhnb.jwtauth.models.User;
import com.thanhnb.jwtauth.models.UserPrincipal;
import com.thanhnb.jwtauth.repository.RoleRepository;
import com.thanhnb.jwtauth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new CustomException("user not found");
        }
        List<Role> roles = roleRepository.loadRolesOfUserBy(username);
        UserPrincipal userPrincipal = new UserPrincipal();
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode())).collect(Collectors.toList());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setEnabled(true);
        userPrincipal.setAuthorities(authorities);
        return userPrincipal;
    }
}
