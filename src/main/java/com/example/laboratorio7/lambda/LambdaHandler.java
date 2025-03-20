package com.example.laboratorio7.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;
import java.util.HashMap;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String token = getTokenFromHeaders(input); // Extrae el token del header "Authorization"

        Map<String, Object> response = new HashMap<>();
        response.put("headers", Map.of("Content-Type", "application/json"));

        try {
            DecodedJWT jwt = CognitoTokenValidator.validateToken(token);
            String username = jwt.getClaim("username").asString();

            response.put("statusCode", 200);
            response.put("body", "{\"message\": \"Token válido\", \"user\": \"" + username + "\"}");
        } catch (Exception e) {
            response.put("statusCode", 401);
            response.put("body", "{\"error\": \"Token inválido\"}");
        }

        return response;
    }

    private String getTokenFromHeaders(Map<String, Object> input) {
        Map<String, String> headers = (Map<String, String>) input.get("headers");
        return headers != null ? headers.get("Authorization").replace("Bearer ", "") : null;
    }
}
