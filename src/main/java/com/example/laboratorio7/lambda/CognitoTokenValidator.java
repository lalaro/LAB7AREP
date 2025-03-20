package com.example.laboratorio7.lambda;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

public class CognitoTokenValidator {

    private static final String COGNITO_JWK_SET_URI = "https://cognito-idp.us-east-1.amazonaws.com/us-east-1_I55X45CTg/.well-known/jwks.json";

    public static DecodedJWT validateToken(String token) throws Exception {
        JwkProvider provider = new JwkProviderBuilder(new URL(COGNITO_JWK_SET_URI)).build();
        DecodedJWT jwt = JWT.decode(token);
        Jwk jwk = provider.get(jwt.getKeyId());
        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

        return jwt;
    }
}
