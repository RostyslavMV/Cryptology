package com.rmv.crypto;

import java.util.ArrayList;

public class RC5 {
    private final Key key;
    private final ArrayList<int[]> s;

    public RC5(Key key) {
        this.key = key;
        s = key.getS();
    }

    public int[] encryption(int[] str){
        int w = key.getW();
        if (str.length * 8 != w * 2) {
            throw new IndexOutOfBoundsException("Invalid block size");
        }

        int[] A = new int[w / 8];
        int[] B = new int[w / 8];
        System.arraycopy(str, 0, A, 0, str.length / 2);
        System.arraycopy(str, str.length / 2, B, 0, str.length / 2);

        int[] bitA = this.byteToBit(A);
        int[] bitB = this.byteToBit(B);

        bitA = key.sum(bitA, s.get(0));
        bitB = key.sum(bitB, s.get(1));

        for (int i = 1; i <= key.getR(); i++) {
            bitA = key.sum(key.shift(key.xor(bitA, bitB), key.numberX(bitB)), s.get(i * 2));
            bitB = key.sum(key.shift(key.xor(bitB, bitA), key.numberX(bitA)), s.get(i * 2 + 1));
        }

        int[] tmp = new int[bitA.length * 2];
        int[] st = this.byteToBit(str);
        System.arraycopy(bitA, 0, tmp, 0, bitA.length);
        System.arraycopy(bitB, 0, tmp, tmp.length / 2, bitB.length);
        this.statistic(st, tmp);

        return bitToByte(tmp);
    }

    public int[] decryption(int[] str){
        int w = key.getW();
        if (str.length * 8 != w * 2) {
            throw new IndexOutOfBoundsException("Invalid block size");
        }
        int[] A = new int[w / 8];
        int[] B = new int[w / 8];
        System.arraycopy(str, 0, A, 0, str.length / 2);
        System.arraycopy(str, str.length / 2, B, 0, str.length / 2);
        int[] bitA = this.byteToBit(A);
        int[] bitB = this.byteToBit(B);

        for (int i = key.getR(); i >= 1; i--) {
            bitB = key.xor(key.shiftr(key.diff(bitB, s.get(i * 2 + 1)), key.numberX(bitA)), bitA);
            bitA = key.xor(key.shiftr(key.diff(bitA, s.get(i * 2)), key.numberX(bitB)), bitB);

        }
        bitB = key.diff(bitB, s.get(1));
        bitA = key.diff(bitA, s.get(0));

        int[] tmp = new int[bitA.length * 2];
        System.arraycopy(bitA, 0, tmp, 0, bitA.length);
        System.arraycopy(bitB, 0, tmp, tmp.length / 2, bitB.length);

        return this.bitToByte(tmp);
    }

    private int[] byteToBit(int[] value) {
        int[] res = new int[8 * value.length];
        for (int i = 0; i < value.length; i++) {
            int by = value[i];
            int[] tmp = new int[8];
            for (int j = 0; j < 8; j++) {
                tmp[j] = by % 2;
                by = (by - by % 2) / 2;
            }
            for (int j = 0; j < 8; j++) {
                res[i * 8 + j] = tmp[7 - j];
            }
        }
        return res;
    }

    private int[] bitToByte(int[] value) {
        int[] res = new int[value.length / 8];
        for (int i = 0; i < res.length; i++) {
            int by = 0;
            for (int j = 0; j < 8; j++) {
                by += value[i * 8 + j] * Math.pow(2, 7 - j);
            }
            res[i] = by;
        }
        return res;
    }

    private void statistic(int[] start, int[] res) {
        double k = 0;
        double one = 0;
        double zero = 0;
        for (int i = 0; i < res.length; i++) {
            k += (res[i] + start[i]) % 2;
        }
        System.out.print("Input sequence: ");
        for (int j : start) {
            System.out.print(j);
        }
        System.out.println();
        System.out.print("Output sequence: ");
        for (int re : res) {
            System.out.print(re);
            if (re == 0)
                zero++;
            else
                one++;
        }

        k = k / res.length;
        System.out.println();
        System.out.println("Coef corel: " + k);
        System.out.println("Distribution 0: " + zero / res.length);
        System.out.println("Distribution 1: " + one / res.length);
    }
}
