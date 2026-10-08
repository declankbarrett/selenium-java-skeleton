package api;

import config.ConfigReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.json.Json;

/**
 * Minimal HTTP client for the Test Application users API (configured by {@code apiUrl}).
 * Uses the JDK HTTP client and Selenium's JSON support, so no extra dependencies are needed.
 */
public class UsersApiClient {

    private static final Logger log = LogManager.getLogger(UsersApiClient.class);
    private static final Json JSON = new Json();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient http = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();
    private final String baseUrl = ConfigReader.get("apiUrl").replaceAll("/+$", "");

    public record ApiResponse(int status, String body) {

        public List<Map<String, Object>> asList() {
            return JSON.toType(body, Json.LIST_OF_MAPS_TYPE);
        }

        public Map<String, Object> asMap() {
            return JSON.toType(body, Json.MAP_TYPE);
        }

        /** Parses the body as JSON without assuming its shape (used to check it is an array). */
        public Object asJson() {
            return JSON.toType(body, Object.class);
        }

        @SuppressWarnings("unchecked")
        public List<Object> asRawList() {
            return (List<Object>) asJson();
        }
    }

    /** GET /users with optional query parameters; null or empty values are omitted. */
    public ApiResponse getUsers(String search, String position) {
        Map<String, String> params = new LinkedHashMap<>();
        if (search != null && !search.isEmpty()) {
            params.put("search", search);
        }
        if (position != null && !position.isEmpty()) {
            params.put("position", position);
        }
        return get("/users" + toQuery(params));
    }

    public ApiResponse getAllUsers() {
        return get("/users");
    }

    public ApiResponse getPositions() {
        return get("/users/positions");
    }

    public ApiResponse getUser(long id) {
        return get("/users/" + id);
    }

    public ApiResponse getUsersInProject(long projectId) {
        return get("/users/projects/" + projectId);
    }

    /** POST /users and return the new user's id. */
    public long createUser(String name, String surname, String email, String position) {
        String body = JSON.toJson(Map.of("name", name, "surname", surname, "email", email, "position", position));
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/users"))
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        ApiResponse response = send(request);
        if (response.status() != 201) {
            throw new IllegalStateException("Creating user " + email + " failed: " + response);
        }
        return ((Number) response.asMap().get("id")).longValue();
    }

    public ApiResponse deleteUser(long id) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/users/" + id))
                .timeout(TIMEOUT)
                .DELETE()
                .build();
        return send(request);
    }

    private ApiResponse get(String pathAndQuery) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + pathAndQuery))
                .timeout(TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();
        return send(request);
    }

    private ApiResponse send(HttpRequest request) {
        log.debug("{} {}", request.method(), request.uri());
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (IOException e) {
            throw new IllegalStateException("Request failed: " + request.method() + " " + request.uri(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request interrupted: " + request.uri(), e);
        }
    }

    private static String toQuery(Map<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8).replace("+", "%20"))
                .collect(Collectors.joining("&", "?", ""));
    }
}
