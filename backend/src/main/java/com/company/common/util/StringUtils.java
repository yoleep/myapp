package com.company.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtils {
    
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC = "0123456789";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$");
    
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }
    
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
    
    public static String capitalize(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }
        
        return sb.toString();
    }
    
    public static String generateRandomNumericString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            sb.append(NUMERIC.charAt(random.nextInt(NUMERIC.length())));
        }
        
        return sb.toString();
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static String generateShortUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
    
    public static boolean isValidEmail(String email) {
        return isNotNullOrEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhoneNumber(String phone) {
        return isNotNullOrEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        
        if (localPart.length() <= 2) {
            return localPart + "***@" + domain;
        }
        
        return localPart.substring(0, 2) + "***" + "@" + domain;
    }
    
    public static String maskPhoneNumber(String phone) {
        if (isNullOrEmpty(phone) || phone.length() < 7) {
            return phone;
        }
        
        int visibleDigits = 4;
        int maskLength = phone.length() - visibleDigits;
        
        return "*".repeat(maskLength) + phone.substring(maskLength);
    }
    
    public static String toBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String fromBase64(String base64Str) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
    
    public static String toMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
    
    public static String toSHA256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    public static String sanitizeHtml(String html) {
        if (isNullOrEmpty(html)) {
            return html;
        }
        
        return html.replaceAll("<script.*?>.*?</script>", "")
                   .replaceAll("<.*?>", "")
                   .replaceAll("javascript:", "")
                   .replaceAll("on\\w+=\".*?\"", "");
    }
    
    public static String truncate(String str, int maxLength) {
        if (isNullOrEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength - 3) + "...";
    }
    
    public static String removeSpecialCharacters(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}