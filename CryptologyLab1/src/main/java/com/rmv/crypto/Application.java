package com.rmv.crypto;

import com.rmv.crypto.gcd.extendedeuclidean.ExtendedEuclidean;
import com.rmv.crypto.gcd.extendedeuclidean.ExtendedEuclideanResult;

import java.math.BigInteger;

public class Application {

    public static void main(String[] args) {
        ExtendedEuclideanResult res = ExtendedEuclidean.compute(BigInteger.valueOf(21), BigInteger.valueOf(17));
        System.out.println(res.getBezoutX());
        System.out.println(res.getBezoutY());
        System.out.println(res.getGcd());
    }
}
