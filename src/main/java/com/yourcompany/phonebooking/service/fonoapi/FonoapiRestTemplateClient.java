package com.yourcompany.phonebooking.service.fonoapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Service
public class FonoapiRestTemplateClient implements FonoapiClient {

    private static final Logger logger = LoggerFactory.getLogger(FonoapiRestTemplateClient.class);

    private final RestTemplate restTemplate;

    private final String tokenUrl;
    private final String deviceUrl;

    public FonoapiRestTemplateClient(RestTemplate restTemplate,
                                     @Value("${fonoapi.token-url}") String tokenUrl,
                                     @Value("${fonoapi.device-url}") String deviceUrl) {
        this.restTemplate = restTemplate;
        this.tokenUrl = tokenUrl;
        this.deviceUrl = deviceUrl;
    }

    @Override
    public DeviceEntity getDeviceEntity(String brand, String device) {
        ResponseEntity<DeviceEntity[]> responseEntity = executeRequest(brand, device);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new FonoapiException("Status code Response is null");
        }
        DeviceEntity[] response = responseEntity.getBody();
        if (response == null) {
            throw new FonoapiException("Response is null");
        }
        if (response.length == 0) {
            throw new FonoapiException("Response is empty");
        }
        if (response.length > 1) {
            logger.warn(
                    "There are more than one item in result set, requested brand {} and device {}. Result {}",
                    brand, device, Arrays.toString(response)
            );
        }
        return response[0];
    }

    private ResponseEntity<DeviceEntity[]> executeRequest(String brand, String device) {
        try {
            String token = requireNonNull(restTemplate.getForObject(tokenUrl, String.class), "unable to get token");
            Map<String, String> request = Map.of(
                    "token", token,
                    "brand", brand,
                    "device", device,
                    "position", "0"
            );
            return restTemplate.postForEntity(deviceUrl, request, DeviceEntity[].class);
        } catch (Exception e) {
            throw new FonoapiException(e);
        }
    }
}
