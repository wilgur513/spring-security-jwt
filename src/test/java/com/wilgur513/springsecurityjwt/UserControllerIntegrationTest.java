package com.wilgur513.springsecurityjwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerIntegrationTest {

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:8080%s", path));
    }

    private String token(String username, String password) throws URISyntaxException {
        UserLogin login = UserLogin.builder().username(username).password(password).build();
        HttpEntity<UserLogin> body = new HttpEntity<>(login);
        ResponseEntity<String> result = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
        return result.getHeaders().get("Authentication").get(0).substring("Bearer ".length());
    }

    @Test
    public void 가입자는_USER페이지에_접근할_수_있다() throws URISyntaxException {
        String token = token("user", "123");
        System.out.println(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", "Bearer " + token);
        ResponseEntity<String> result = restTemplate.exchange(uri("/user"), HttpMethod.GET, new HttpEntity("", headers), String.class);
        assertThat(result.getBody(), is("user"));
    }

    @Test
    public void 비가입자는_USER페이지에_접근할_수_없다() throws URISyntaxException {
        assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(uri("/user"), HttpMethod.GET, new HttpEntity(""), String.class);
        });
    }

    @Test
    public void ADMIN_계정은_ADMIN페이지에_접근할_수_있다() throws URISyntaxException {
        String token = token("admin", "123");
        System.out.println(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", "Bearer " + token);
        ResponseEntity<String> result = restTemplate.exchange(uri("/admin"), HttpMethod.GET, new HttpEntity("", headers), String.class);
        assertThat(result.getBody(), is("admin"));
    }

    @Test
    public void USER_계정은_ADMIN페이지에_접근할_수_없다() throws URISyntaxException {
        String token = token("user", "123");
        System.out.println(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", "Bearer " + token);

        assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(uri("/admin"), HttpMethod.GET, new HttpEntity("", headers), String.class);
        });
    }
}
