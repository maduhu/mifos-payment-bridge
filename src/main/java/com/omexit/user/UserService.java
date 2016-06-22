package com.omexit.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

/**
 * @author Antony Ezekiel Omeri
 */

public interface UserService extends UserDetailsService {
    Collection<User> findAll();

    User findUserById(Long id) ;

    User findUserByUsername(String username) ;

    User create(User user);

    User update(User user);

    void delete(Long id);
}
