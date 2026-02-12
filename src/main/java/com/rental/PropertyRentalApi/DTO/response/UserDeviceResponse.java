package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UserDeviceResponse {
    private Long id;
    private String deviceType;
    private String deviceName;
    private String os;
    private String browser;
    private Instant lastSeenAt;
    private boolean active;
}
