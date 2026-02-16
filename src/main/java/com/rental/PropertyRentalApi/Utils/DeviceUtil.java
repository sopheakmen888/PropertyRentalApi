package com.rental.PropertyRentalApi.Utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;

public class DeviceUtil {

    public static String generateDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String language = request.getHeader("Accept-Language");
        String ip = request.getRemoteAddr();

        String raw = userAgent + "|" + language + "|" + ip;
        return DigestUtils.sha256Hex(raw);
    }
}
