package com.rental.PropertyRentalApi.DTO.response;

import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private List<String> roles;

    public UserResponse(Users users) {
        this.id = users.getId();
        this.fullname = users.getFullname();
        this.username = users.getUsername();
        this.email = users.getEmail();
        this.roles = users.getRoles()
                .stream()
                .map(Roles::getName) // extract name
                .collect(Collectors.toList());
    }
}

