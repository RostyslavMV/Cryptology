package com.rmv.crypto;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class RC5Test {

    @Test
    void encryptionShouldEncryptExpectedValue() throws IOException {
        String outputFileName = "src\\test\\util\\textEnc.txt";
        testEncryption(outputFileName);
        assertTrue(FileUtils.contentEquals(new File(outputFileName),
                new File("src\\test\\util\\expectedTextEnc.txt")));
    }

    @Test
    void decryptionShouldDecryptExpectedValue() throws IOException {
        String outputFileName = "src\\test\\util\\textDec.txt";
        testDecryption(outputFileName);
        assertTrue(FileUtils.contentEquals(new File(outputFileName),
                new File("src\\test\\util\\text.txt")));
    }

    private void testEncryption(String outputFileName) throws IOException {
        FileInputStream readKey = new FileInputStream("src\\test\\util\\key.txt");
        FileInputStream fileIn = new FileInputStream("src\\test\\util\\text.txt");
        FileOutputStream fileOut = new FileOutputStream(outputFileName);
        byte[] key = new byte[16];
        while (readKey.available() > 0) {
            for (int i = 0; i < key.length; i++)
                key[i] = (byte) readKey.read();
        }
        String key1 = new String(key);
        int w = 64;
        Key k = new Key(w, 12, key1);
        RC5 c = new RC5(k);

        while (fileIn.available() > 0) {
            int[] b = new int[w / 8];
            for (int i = 0; i < b.length; i++) {
                if (fileIn.available() > 0)
                    b[i] = fileIn.read();
                else b[i] = 0;
            }
            int[] res = c.encryption(b);
            // int []res=c.decryption(b);
            for (int re : res) {
                if (re != 0) {
                    fileOut.write(re);
                }
            }
        }
        fileIn.close();
        fileOut.close();
    }

    private void testDecryption(String outputFileName) throws IOException {
        FileInputStream readKey = new FileInputStream("src\\test\\util\\key.txt");
        FileInputStream fileIn = new FileInputStream("src\\test\\util\\expectedTextEnc.txt");
        FileOutputStream fileOut = new FileOutputStream(outputFileName);
        byte[] key = new byte[16];
        while (readKey.available() > 0) {
            for (int i = 0; i < key.length; i++)
                key[i] = (byte) readKey.read();
        }
        String key1 = new String(key);
        int w = 64;
        Key k = new Key(w, 12, key1);
        RC5 c = new RC5(k);

        while (fileIn.available() > 0) {
            int[] b = new int[w / 8];
            for (int i = 0; i < b.length; i++) {
                if (fileIn.available() > 0)
                    b[i] = fileIn.read();
                else b[i] = 0;
            }
            int[] res = c.decryption(b);
            for (int re : res) {
                if (re != 0) {
                    fileOut.write(re);
                }
            }
        }
        fileIn.close();
        fileOut.close();
    }
}
