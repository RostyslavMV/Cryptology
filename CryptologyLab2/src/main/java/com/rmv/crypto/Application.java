package com.rmv.crypto;

import java.io.*;

public class Application {
    public static void main(String[] args) {
        try {
            FileInputStream readKey = new FileInputStream("key.txt");
            byte[] key = new byte[16];
            while (readKey.available() > 0) {
                for (int i = 0; i < key.length; i++)
                    key[i] = (byte) readKey.read();
            }
            String key1 = new String(key);


            int w = 64;
            Key k = new Key(w, 12, key1);

            RC5 c = new RC5(k);

            FileInputStream fileIn = new FileInputStream("text.txt");
            FileOutputStream fileOut = new FileOutputStream("expectedTextEnc.txt");
//
//             FileInputStream fileIn = new FileInputStream("textEnc.txt");
//             FileOutputStream fileOut = new FileOutputStream("textDec.txt");

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
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
