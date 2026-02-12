package com.rental.PropertyRentalApi.Service;

<<<<<<< HEAD
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

@Service
=======
import ua_parser.Client;
import ua_parser.Parser;

>>>>>>> 5c3f839 (complete user device tracking)
public class DeviceInfoService {

    private final Parser parser = new Parser();

    public Client parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return null;
        }
        return parser.parse(userAgent);
    }

}
