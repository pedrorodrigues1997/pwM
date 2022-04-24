package com.ossnm.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CryptographyTest {


    @Test
    public void testEncrypt(){
      String cifra=   Cryptography.encryptPassword("12@RP7123");
      assertEquals("12@RP7123", Cryptography.decryptPassword(cifra));
    }

}