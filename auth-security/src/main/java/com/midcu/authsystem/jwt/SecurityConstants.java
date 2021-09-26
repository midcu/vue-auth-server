package com.midcu.authsystem.jwt;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public final class SecurityConstants {

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys

    public static Key JWT_SECRET = null;

    public static final Key JWT_SECRET1 = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT token defaults
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    public static void SET_JWT_SECRET(Key secret) {
        JWT_SECRET = secret;
    }

}
