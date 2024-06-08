package com.nea.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nea.backend.dto.UserLoginDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
public class AuthServiceTest {

    static HttpClient client;
    static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void isSessionCookieCreated() throws IOException, InterruptedException {
        UserLoginDTO userData = new UserLoginDTO("admin", "password");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/login"))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userData)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        String cookie = response.headers().firstValue("Cookie")
                .orElse(null);

        System.out.println(cookie);
//        Assertions.assertEquals(cookie, );
    }

    @Test
    public void ifBodyIsEmptyThrowException() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/auth/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
