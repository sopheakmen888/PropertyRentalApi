package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.UserDevices;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserDeviceMapper {

    UserDeviceResponse toUserDeviceResponse(UserDevices userDevices);
}
