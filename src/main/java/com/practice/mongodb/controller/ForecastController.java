package com.practice.mongodb.controller;

import com.practice.mongodb.document.AuthenticatedUser;
import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.controller.models.AuthenticationRequest;
import com.practice.mongodb.controller.models.AuthenticationResponse;
import com.practice.mongodb.service.ForecastService;
import com.practice.mongodb.service.helper.JWTUtil;
import com.practice.mongodb.service.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ForecastController {

    private final ForecastService forecastService;
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerBootstrapServer;
    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public ForecastController(ForecastService forecastService, AuthenticationManager authenticationManager,
                              MyUserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.forecastService = forecastService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("points/{lat}/{lon}")
    public UUID getForecast(@PathVariable Double lat, @PathVariable Double lon, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        forecastService.saveForecast(lat, lon, uuid, jwtUtil.getUsername(jwtUtil.resolveToken(request)));
        return uuid;
    }

    @GetMapping("forecast/{uuid}")
    public ResponseEntity<Forecast> getForecast(@PathVariable String uuid) {
        Forecast forecast = forecastService.getForecast(UUID.fromString(uuid));
        if (forecast == null ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/config")
    public Map<String, String> getMessageQueueConfiguration(HttpServletRequest request) {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("consumerBootstrapServer", consumerBootstrapServer);
        configuration.put("consumerGroupId", consumerGroupId);
        configuration.put("topic", jwtUtil.getUsername(jwtUtil.resolveToken(request)));
        return configuration;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody AuthenticatedUser user) {
        userDetailsService.saveUser(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try{
            authenticationManager.authenticate(
                    jwtUtil.getAuthentication(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password.", e);
        }

        final String jwt = jwtUtil.createToken(authenticationRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
