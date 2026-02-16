package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import jakarta.servlet.http.HttpServletRequest;

public interface DeviceTrackingService {

    UserDeviceResponse trackUserDevice(Users users, HttpServletRequest request);
}
