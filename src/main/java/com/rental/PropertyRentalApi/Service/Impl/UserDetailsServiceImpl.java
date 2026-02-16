package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String value)
            throws UsernameNotFoundException {

        return userRepository.findByEmail(value)
                .or(() -> userRepository.findByUsername(value))
                .or(() -> {
                    if (value.matches("\\d+")) {
                        return userRepository.findById(Long.valueOf(value));
                    }
                    return Optional.empty();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}