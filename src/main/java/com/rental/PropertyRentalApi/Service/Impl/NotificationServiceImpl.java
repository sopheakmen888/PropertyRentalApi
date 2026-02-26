package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.UserDevices;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.NotificationService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNewDeviceAlert(Users users, UserDevices userDevices) {
        System.out.println(
                "⚠️ New device login for user: " + users.getEmail()
        );
    }
}
