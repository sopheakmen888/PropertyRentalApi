package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.RegisterDeviceRequest;
import com.rental.PropertyRentalApi.DTO.response.UserDeviceResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.UserDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    @PostMapping("/me/device")
    public UserDeviceResponse registerDevice(
            @AuthenticationPrincipal Users users,
            @RequestBody RegisterDeviceRequest request,
            HttpServletRequest httpRequest
            ) {
        String ipAddress = httpRequest.getRemoteAddr();

        return userDeviceService.registerDevice(
                users,
                request,
                ipAddress
        );
    }

    @GetMapping("/me/device")
    public List<UserDeviceResponse> myDevice(
            @AuthenticationPrincipal Users users
    ) {
        return userDeviceService.getMyDevices(users);
    }
}
