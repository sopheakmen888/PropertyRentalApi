package com.rental.PropertyRentalApi.Service;

import ua_parser.Client;
import ua_parser.Parser;

public class DeviceInfoService {

    private final Parser parser = new Parser();

    public Client parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return null;
        }
        return parser.parse(userAgent);
    }

}
