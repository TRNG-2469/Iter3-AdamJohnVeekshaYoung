package com.revature.ERS2.security;

import com.revature.ERS2.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.revature.ERS2.models.User;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //NOTE!!!!!! User naming collison with Spring's user and our user.
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        //Used fully qualified name to distinguish, should we change?
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
