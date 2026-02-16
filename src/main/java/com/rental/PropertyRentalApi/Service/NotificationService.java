package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.Entity.UserDevices;
import com.rental.PropertyRentalApi.Entity.Users;

public interface NotificationService {
    void sendNewDeviceAlert(Users users, UserDevices userDevices);
}
