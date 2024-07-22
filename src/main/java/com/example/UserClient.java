package com.example;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@Component
public class UserClient implements CommandLineRunner {
    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;

    public UserClient() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run(String... args) {
        String sessionId = getSessionId();
        addUser(sessionId);
        updateUser(sessionId);
        deleteUser(sessionId);
    }

    private String getSessionId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, entity, String.class);
        List<String> cookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        if (cookieHeaders != null && !cookieHeaders.isEmpty()) {
            String sessionId = cookieHeaders.get(0);
            System.out.println("Session ID: " + sessionId);
            return sessionId.split(";")[0];
        }
        return null;
    }

    private void addUser(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte) 30);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        System.out.println("Add User Response: " + response.getBody());
    }

    private void updateUser(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        User user = new User();
        user.setId(3L);
        user.setName("Thomas");
        user.setLastName("Shelby");
        user.setAge((byte) 30);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, request, String.class);

        System.out.println("Update User Response: " + response.getBody());
    }

    private void deleteUser(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/3", HttpMethod.DELETE, request, String.class);

        System.out.println("Delete User Response: " + response.getBody());
    }

}
