package com.omexit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * Created by aomeri on 7/26/15.
 */
@Service
public class UserServiceBean implements UserService {
    @Autowired
    UserRepository userRepository;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public Collection<User> findAll() {
        return userRepository.findAll();
    }


    public User findUserById(Long userId) {
        User user = userRepository.findById(userId);

        return user;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User create(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    public User update(User user) {
        return userRepository.save(user);
    }


    public void delete(Long id) {
        userRepository.delete(id);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username: %s not found!", username));
        }
        logger.info(String.format("loadUserByUsername(%s) - %s.", username, user.toString()));

        return new UserRepositoryUserDetails(user);
    }

    @PostConstruct
    public void init() {
//        Retailer retailer=new Retailer();
//        retailer.setRetailerCode("1000");
//        retailer.setRetailerName("Semamobile Services Limited");
//        retailer.setRetailerPhoneNumber("+254767000000");
//        retailer=retailerRepository.save(retailer);

        User user = new User();
        user.setFullNname("Antony Omeri");
        user.setUsername("omexit");
        user.setPassword("1234");
        user.setDesignation("Software Engineer");
//        user.setIdType(IdType.NATIONAL_ID);
//        user.setRoleNumber(-1l);
//        user.setRetailer(retailer);
        user.setEnabled(true);
//        user.setPasswordExpired(false);
        create(user);
    }

    private static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;
        protected Logger logger = LoggerFactory.getLogger(this.getClass());

        User user;

        UserRepositoryUserDetails(User user) {
            super(user);
            this.user = user;

//            logger.info(String.format("UserRepositoryUserDetails() - %s.", this.getAuthorities()));
//            logger.info(String.format("UserRepositoryUserDetails() - %s.", this.getPassword()));
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_CLIENT");
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

    }


}
