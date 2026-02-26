package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.Entity.Roles;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(config = MapperConfiguration.class)
public interface RoleMapper {

    default List<String> mapRoles(Set<Roles> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Roles::getName)
                .toList();
    }
}
