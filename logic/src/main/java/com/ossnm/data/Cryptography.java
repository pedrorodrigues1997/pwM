package com.ossnm.data;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class Cryptography {
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final Logger LOGGER = LogManager.getLogger(Cryptography.class);

    private static Path path = Paths.get("C:\\Users\\pedro\\Documents\\pwManager\\cipherKey.txt");
    private static IvParameterSpec ivParameterSpec = null;

    public static String encryptPassword(String plaintext) {
        try {
            if (ivParameterSpec == null) {
                LOGGER.debug("IV not initialized");
                generateIv();
                keyGenToFile();
            }
            SecretKey encryptionKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivParameterSpec);

            byte[] cipherText = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder()
                    .encodeToString(cipherText);


        } catch (Exception e) {
            LOGGER.debug("Error when encrypting password {}", e);
            e.printStackTrace();
            return null;
        }
    }


    public static String decryptPassword(String ciphertext) {
        try {
            if (ivParameterSpec == null) {
                LOGGER.debug("IV not initialized");
                generateIv();
                keyGenToFile();
            }
            SecretKey encryptionKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey, ivParameterSpec);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(ciphertext));

            return new String(plainText);


        } catch (Exception e) {
            LOGGER.debug("Error when decrypting password {}", ciphertext);
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKey getSecretKey() throws IOException {
        String encryptionKey = readFile();
        byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    private static void generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    public static void keyGenToFile() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        try {
            writeFile(path, encodedKey);
        } catch (IOException e) {
            LOGGER.debug("Couldn't save new Encryption Key");
            e.printStackTrace();
        }

    }

    private static void writeFile(Path path, String content) throws IOException {
        // file does not exist, create and write it
        // if the file exists, override the content
        Files.writeString(path, content);
        // Append mode
        // if the file exists, append string to the end of file.
        // Files.write(path, content.getBytes(StandardCharsets.UTF_8),
        //	StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        // if file does not exist, throws NoSuchFileException
        // if the file exists, append it
        // Files.write(path, content.getBytes(StandardCharsets.UTF_8),
        //	StandardOpenOption.APPEND);
    }

    private static String readFile() throws IOException {
        return Files.readString(path);

    }
}
