package com.example.majorproject;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyUser implements UserDetails {

    private static final String DELIMITER = ":";


    private int id;


    private String userId;
    private String name;
    private int age;
    private String email;
    private String phone;

    private String authorities;
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String [] authoritiesArray = this.authorities.split(DELIMITER);
        return Arrays.stream(authoritiesArray)
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
