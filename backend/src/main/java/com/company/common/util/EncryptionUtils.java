package com.company.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {
    
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final int AES_KEY_SIZE = 256;
    
    // This should be externalized to configuration
    private static final String MASTER_KEY = System.getenv("ENCRYPTION_MASTER_KEY");
    
    /**
     * Encrypts a string using AES-GCM
     */
    public static String encrypt(String plainText) throws Exception {
        return encrypt(plainText, getSecretKey());
    }
    
    /**
     * Encrypts a string using AES-GCM with a specific key
     */
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        byte[] iv = generateIV();
        
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        
        // Combine IV and ciphertext
        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    /**
     * Decrypts a string using AES-GCM
     */
    public static String decrypt(String encryptedText) throws Exception {
        return decrypt(encryptedText, getSecretKey());
    }
    
    /**
     * Decrypts a string using AES-GCM with a specific key
     */
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);
        
        // Extract IV and ciphertext
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] cipherText = new byte[combined.length - GCM_IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, cipherText, 0, cipherText.length);
        
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }
    
    /**
     * Generates a new AES secret key
     */
    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }
    
    /**
     * Converts a string key to SecretKey
     */
    public static SecretKey stringToSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
    
    /**
     * Converts a SecretKey to string
     */
    public static String secretKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
    
    /**
     * Generates a random IV for GCM
     */
    private static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    /**
     * Gets the master secret key from environment
     */
    private static SecretKey getSecretKey() throws Exception {
        if (MASTER_KEY == null || MASTER_KEY.isEmpty()) {
            // Generate a default key for development (should not be used in production)
            return generateSecretKey();
        }
        return stringToSecretKey(MASTER_KEY);
    }
    
    /**
     * Hashes a string using SHA-256
     */
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    /**
     * Hashes a string using SHA-512
     */
    public static String hashSHA512(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not found", e);
        }
    }
    
    /**
     * Generates a secure random token
     */
    public static String generateSecureToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    /**
     * Generates a secure random numeric PIN
     */
    public static String generateNumericPIN(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder pin = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }
    
    /**
     * Masks sensitive data (shows only first and last few characters)
     */
    public static String maskSensitiveData(String data, int showFirst, int showLast) {
        if (data == null || data.length() <= (showFirst + showLast)) {
            return "****";
        }
        
        String first = data.substring(0, showFirst);
        String last = data.substring(data.length() - showLast);
        int maskLength = data.length() - showFirst - showLast;
        String mask = "*".repeat(Math.max(maskLength, 4));
        
        return first + mask + last;
    }
    
    /**
     * Masks email address
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "****";
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        
        if (localPart.length() <= 2) {
            return "**@" + domain;
        }
        
        return maskSensitiveData(localPart, 2, 0) + "@" + domain;
    }
    
    /**
     * Masks phone number
     */
    public static String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 7) {
            return "****";
        }
        
        // Show only last 4 digits
        return "*".repeat(phone.length() - 4) + phone.substring(phone.length() - 4);
    }
    
    /**
     * Masks credit card number
     */
    public static String maskCreditCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 12) {
            return "****";
        }
        
        // Show first 4 and last 4 digits
        return maskSensitiveData(cardNumber, 4, 4);
    }
    
    /**
     * Converts bytes to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * Validates if a string is encrypted (basic check)
     */
    public static boolean isEncrypted(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(text);
            return decoded.length > GCM_IV_LENGTH;
        } catch (Exception e) {
            return false;
        }
    }
}