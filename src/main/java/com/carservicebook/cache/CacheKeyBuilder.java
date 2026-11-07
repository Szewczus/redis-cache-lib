package com.carservicebook.cache;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

public class CacheKeyBuilder {
    private CacheKeyBuilder() {}

    public static String build(String version, List<String> list, String processId) {
        List<String> sortedAssets = list.stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .sorted()
                .toList();

        String canonical = String.join("|",
                version,
                processId,
                String.join(",", sortedAssets));

        return "proj:v1:" + sha256(canonical);
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
