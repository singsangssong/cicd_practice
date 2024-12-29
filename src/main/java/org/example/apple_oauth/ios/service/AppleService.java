package org.example.apple_oauth.ios.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.example.apple_oauth.ios.dto.AppleDto;
import org.example.apple_oauth.ios.util.PemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AppleService {

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.login.key}")
    private String APPLE_LOGIN_KEY;

    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.key.path}")
    private String APPLE_KEY_PATH;

    private static final String APPLE_AUTH_URL = "https://appleid.apple.com";

    public String getAppleLogin() {
        return APPLE_AUTH_URL + "/auth/authorize"
                + "?client_id=" + APPLE_CLIENT_ID
                + "&redirect_uri=" + APPLE_REDIRECT_URL
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }

    public AppleDto getAppleInfo(String code) throws Exception {
        if (code == null) {
            throw new IllegalArgumentException("Failed to get authorization code");
        }

        String clientSecret = createClientSecret();
        String userId;
        String email;
        String accessToken;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", APPLE_CLIENT_ID);
            params.add("client_secret", clientSecret);
            params.add("code", code);
            params.add("redirect_uri", APPLE_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_AUTH_URL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser(response.getBody());
            JSONObject jsonObj = (JSONObject) jsonParser.parse();

            accessToken = (String) jsonObj.get("access_token");

            // Parse ID Token
            SignedJWT signedJWT = SignedJWT.parse((String) jsonObj.get("id_token"));
            JWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toString(), JSONObject.class);

            userId = (String) payload.get("sub");
            email = (String) payload.get("email");
        } catch (Exception e) {
            throw new Exception("API call failed", e);
        }

        return AppleDto.builder()
                .id(userId)
                .token(accessToken)
                .email(email)
                .build();
    }

    private String createClientSecret() throws Exception {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(APPLE_LOGIN_KEY).build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(APPLE_TEAM_ID)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                .audience(APPLE_AUTH_URL)
                .subject(APPLE_CLIENT_ID)
                .build();

        SignedJWT jwt = new SignedJWT(header, claimsSet);

        try {
            ECPrivateKey ecPrivateKey = (ECPrivateKey) getPrivateKey();
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey);
            jwt.sign(jwsSigner);
        } catch (JOSEException e) {
            throw new Exception("Failed to create client secret", e);
        }

        return jwt.serialize();
    }

    private PrivateKey getPrivateKey() throws Exception {
        URL resource = getClass().getResource(APPLE_KEY_PATH);
        if (resource == null) {
            throw new FileNotFoundException("Private key file not found at " + APPLE_KEY_PATH);
        }

        try (InputStream inputStream = resource.openStream()) {
            String privateKeyPem = IOUtils.toString(inputStream, "UTF-8");
            return PemUtils.loadPrivateKey(privateKeyPem, "EC");
        }
    }
}
