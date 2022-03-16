package com.yourcompany.phonebooking.service;

import com.yourcompany.phonebooking.entity.Phone;
import com.yourcompany.phonebooking.entity.PhoneDetails;
import com.yourcompany.phonebooking.service.fonoapi.DeviceEntity;
import com.yourcompany.phonebooking.service.fonoapi.FonoapiClient;
import com.yourcompany.phonebooking.service.fonoapi.FonoapiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PhoneDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneDetailsService.class);

    private final FonoapiClient fonoapiClient;

    public PhoneDetailsService(FonoapiClient fonoapiClient) {
        this.fonoapiClient = fonoapiClient;
    }

    public PhoneDetails getDetails(Phone phone) {
        try {
            DeviceEntity deviceEntity = fonoapiClient.getDeviceEntity(phone.getBrand(), phone.getBrand());
            return PhoneDetails.of(phone, deviceEntity);
        } catch (FonoapiException e) {
            logger.warn("fonoapi is not available", e);
            return PhoneDetails.of(phone);
        }
    }
}
