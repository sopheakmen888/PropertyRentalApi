package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.RegisterDeviceRequest;
import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.Users;

import java.util.List;

public interface UserDeviceService {

    UserDeviceResponse registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress
    );

    List<UserDeviceResponse> getMyDevices(Users users);
}
