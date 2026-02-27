package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.RegisterDeviceRequest;
import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.UserDevices;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.UserDeviceRepository;
import com.rental.PropertyRentalApi.Service.UserDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("unused")
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Override
    public UserDeviceResponse registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress
    ) {
        Optional<UserDevices> existDevice =
                userDeviceRepository.findByUserIdAndDeviceId(
                        users.getId(), request.getDeviceId()
                );

        UserDevices device;

        if (existDevice.isPresent()) {
            device = existDevice.get();
            device.setLastSeenAt(Instant.now());
            device.setIpAddress(ipAddress);
        } else {
            Instant now = Instant.now();
            device = new UserDevices();
            device.setUser(users);
            device.setDeviceId(request.getDeviceId());
            device.setDeviceType(request.getDeviceType());
            device.setDeviceName(request.getDeviceName());
            device.setOs(request.getOs());
            device.setBrowser(request.getBrowser());
            device.setIpAddress(ipAddress);
            device.setFirstSeenAt(now);
            device.setLastSeenAt(now);

            userDeviceRepository.save(device);
        }

        return new UserDeviceResponse(
                device.getId(),
                device.getDeviceType(),
                device.getDeviceName(),
                device.getOs(),
                device.getBrowser(),
                device.getLastSeenAt(),
                device.isActive()
        );
    }

    @Override
    public List<UserDeviceResponse> getMyDevices(Users users) {
        return userDeviceRepository.findAllByUserId(users.getId())
                .stream()
                .map(device -> new UserDeviceResponse(
                        device.getId(),
                        device.getDeviceType(),
                        device.getDeviceName(),
                        device.getOs(),
                        device.getBrowser(),
                        device.getLastSeenAt(),
                        device.isActive()
                ))
                .toList();
    }
}
