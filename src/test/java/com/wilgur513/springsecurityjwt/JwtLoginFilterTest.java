package com.wilgur513.springsecurityjwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Exchanger;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest
public class JwtLoginFilterTest {

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:8080%s", path));
    }

    @Test
    public void JWT_로그인_시도() throws URISyntaxException {
        UserLogin login = UserLogin.builder().username("user").password("123").build();
        HttpEntity<UserLogin> body = new HttpEntity<>(login);

        ResponseEntity<String> result = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);

        assertThat(result.getStatusCodeValue(), is(200));
        System.out.println(result.getHeaders().get("Authentication"));
    }
}
