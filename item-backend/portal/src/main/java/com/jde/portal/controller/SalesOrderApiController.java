package com.jde.portal.controller;

import com.jde.portal.dto.SalesOrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Allows your React app to connect without CORS errors
public class SalesOrderApiController {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${jde.orchestrator.enabled:true}")
    private boolean orchestratorEnabled;

    @Value("${jde.orchestrator.url:}")
    private String orchestratorUrl;

    @Value("${jde.orchestrator.token:}")
    private String orchestratorToken;

    @Value("${jde.orchestrator.username:}")
    private String orchestratorUsername;

    @Value("${jde.orchestrator.password:}")
    private String orchestratorPassword;

    // Using a Queue allows multiple users to submit without overwriting data
    private static final ConcurrentLinkedQueue<SalesOrderDto> orderQueue = new ConcurrentLinkedQueue<>();

    // Frontend submits sales order; backend forwards to JDE Orchestrator
    @PostMapping("/create")
    public ResponseEntity<String> receiveOrder(@RequestBody SalesOrderDto orderDto) {
        orderQueue.add(orderDto);

        if (!orchestratorEnabled) {
            return ResponseEntity.ok("Order queued locally. JDE orchestrator forwarding is disabled.");
        }

        if (!StringUtils.hasText(orchestratorUrl)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Order queued locally, but JDE orchestrator URL is not configured.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        applyAuthHeaders(headers);

        HttpEntity<SalesOrderDto> request = new HttpEntity<>(orderDto, headers);

        try {
            ResponseEntity<String> orchestratorResponse =
                    restTemplate.postForEntity(orchestratorUrl, request, String.class);

            if (orchestratorResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("Order sent to JDE Orchestrator successfully.");
            }

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Order queued, but JDE Orchestrator returned status: "
                            + orchestratorResponse.getStatusCode().value());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Order queued, but failed to call JDE Orchestrator: " + ex.getMessage());
        }
    }

    // 2. Orchestrator Connector pulls data from here
    @GetMapping("/get-pending")
    public SalesOrderDto getPendingOrder() {
        return orderQueue.poll(); // Returns and removes the oldest order
    }

    private void applyAuthHeaders(HttpHeaders headers) {
        if (StringUtils.hasText(orchestratorToken)) {
            headers.setBearerAuth(orchestratorToken);
            return;
        }

        if (StringUtils.hasText(orchestratorUsername) && StringUtils.hasText(orchestratorPassword)) {
            String credentials = orchestratorUsername + ":" + orchestratorPassword;
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        }
    }
}