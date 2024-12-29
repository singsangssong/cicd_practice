package org.example.apple_oauth.ios.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PemUtils {

    /**
     * Loads a private key from a PEM formatted string.
     *
     * @param pem The PEM formatted string containing the private key.
     * @param algorithm The algorithm of the key (e.g., "EC" for elliptic curve keys).
     * @return A PrivateKey object.
     * @throws Exception If the key cannot be parsed or is invalid.
     */
    public static PrivateKey loadPrivateKey(String pem, String algorithm) throws Exception {
        // Remove PEM headers and decode the base64 content
        String privateKeyPem = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Reads a PEM formatted string and returns its content.
     *
     * @param pem The PEM formatted string.
     * @return The raw byte content of the PEM file.
     * @throws Exception If the PEM file cannot be read or parsed.
     */
    public static byte[] readPemContent(String pem) throws Exception {
        try (PemReader pemReader = new PemReader(new StringReader(pem))) {
            PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        }
    }
}
