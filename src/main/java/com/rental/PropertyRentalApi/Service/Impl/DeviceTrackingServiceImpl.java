package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.UserDevices;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.UserDeviceRepository;
import com.rental.PropertyRentalApi.Service.DeviceInfoService;
import com.rental.PropertyRentalApi.Service.DeviceTrackingService;
import com.rental.PropertyRentalApi.Service.NotificationService;
import com.rental.PropertyRentalApi.Utils.DeviceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceTrackingServiceImpl implements DeviceTrackingService {

    private final UserDeviceRepository userDeviceRepository;
    private final DeviceInfoService deviceInfoService;
    private final NotificationService notificationService;

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }

    @Override
    public UserDeviceResponse trackUserDevice(Users users, HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        Client client = deviceInfoService.parse(userAgent);
        String ip = extractIp(request);
        String deviceId = DeviceUtil.generateDeviceId(request);

        String deviceFamily =
                client != null && client.device != null
                        ? client.device.family
                        : "Unknown";

        String os =
                client != null && client.os != null
                        ? client.os.family
                        : "Unknown";

        String browser =
                client != null && client.userAgent != null
                        ? client.userAgent.family
                        : "Unknown";

        Optional<UserDevices> existing =
                userDeviceRepository.findByUserIdAndDeviceId(users.getId(), deviceId);

        boolean isNewDevice = existing.isEmpty();
        UserDevices userDevices = existing.orElseGet(() -> {
            UserDevices d = new UserDevices();
            d.setUser(users);
            d.setDeviceId(deviceId);
            d.setDeviceType(deviceFamily);
            d.setDeviceName(deviceFamily);
            d.setOs(os);
            d.setBrowser(browser);
            d.setIpAddress(ip);
            d.setFirstSeenAt(Instant.now());

            return d;
        });

        if (isNewDevice) {
            notificationService.sendNewDeviceAlert(users, userDevices);
        }

        userDevices.setLastSeenAt(Instant.now());
        userDevices.setIpAddress(ip);
        userDevices.setActive(true);

        userDeviceRepository.save(userDevices);

        return new UserDeviceResponse(
                userDevices.getId(),
                userDevices.getDeviceType(),
                userDevices.getDeviceName(),
                userDevices.getOs(),
                userDevices.getBrowser(),
                userDevices.getLastSeenAt(),
                userDevices.isActive()
        );
    }
}
