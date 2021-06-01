package com.rmv.crypto.modulearithmetics.montgomery;

import com.rmv.crypto.gcd.extendedeuclidean.ExtendedEuclidean;
import com.rmv.crypto.gcd.extendedeuclidean.ExtendedEuclideanResult;

import java.math.BigInteger;

public class MontgomeryArithmetics {

    private static BigInteger reduce(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger r = BigInteger.TWO.shiftLeft(n.bitLength());
        ExtendedEuclideanResult euclideanResult = ExtendedEuclidean.compute(n, r);

        BigInteger t = a.multiply(b);
        BigInteger m = t
                .multiply(euclideanResult.getBezoutX().negate())
                .and(r.subtract(BigInteger.ONE));
        BigInteger u = t
                .add(m.multiply(n))
                .shiftRight(n.bitLength());

        if (u.compareTo(n) >= 0) {
            return u.subtract(n);
        } else {
            return u;
        }
    }

    public static BigInteger modMultiply(BigInteger a, BigInteger b, BigInteger mod) {
        if (!mod.testBit(0) || mod.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException("Mod should be a positive odd number");
        }
        BigInteger uModified = reduce(
                a.shiftLeft(mod.bitLength()).mod(mod),
                b.shiftLeft(mod.bitLength()).mod(mod),
                mod
        );
        return reduce(uModified, BigInteger.ONE, mod);
    }

    public static BigInteger modPower(BigInteger a, BigInteger power, BigInteger mod) {
        if (!mod.testBit(0) || mod.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException("Mod should be a positive odd number");
        }
        BigInteger aModified = a.shiftLeft(mod.bitLength()).mod(mod);
        BigInteger xModified = BigInteger.ONE.shiftLeft(mod.bitLength()).mod(mod);
        for (int i = power.bitLength() - 1; i >= 0; i--) {
            xModified = reduce(xModified, xModified, mod);
            if (power.testBit(i)) {
                xModified = reduce(xModified, aModified, mod);
            }
        }

        return reduce(xModified, BigInteger.ONE, mod);
    }
}
