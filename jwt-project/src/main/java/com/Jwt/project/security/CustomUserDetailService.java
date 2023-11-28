package com.Jwt.project.security;

import com.Jwt.project.domain.Authority;
import com.Jwt.project.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String lowerCaseUsername=username.toLowerCase();
        return userRepository
                .findByUsername(lowerCaseUsername)
                .map(user -> createSpringSecurityUser(lowerCaseUsername,user))
                .orElseThrow(()-> new UserNotActivateException("User " + username + " was not found int the database"));
    }

    private User createSpringSecurityUser(String username, com.Jwt.project.domain.User user){
        if (!user.isActivated()){
            throw new UserNotActivateException("User " + username + " was not activated");
        }

        List<GrantedAuthority> grantedAuthorities=user
                .getAuthorities()
                .stream()
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(username,user.getPassword(),grantedAuthorities);
    }
}
