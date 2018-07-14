package rsa;
import java.math.BigInteger;

import java.util.Random;
import java.security.SecureRandom;


public class Keys {
    int numDigits;
    public BigInteger p;
    public BigInteger q;
    public BigInteger n;
    public BigInteger lambda;
    public BigInteger e = BigInteger.valueOf((int) Math.pow(2, 16) + 1);
    public BigInteger d;
    Random random = new Random();

    public Keys(int bits){
        numDigits = bits;
        //TODO: Issues with random function.
        //TODO: Implement secure random
        int digi_difference = random.nextInt(2); //No more than 4 digits between p and q
        p = BigInteger.probablePrime(bits, new SecureRandom());
        q = BigInteger.probablePrime(bits, new SecureRandom());
        n = p.multiply(q);
        d = e.modInverse((p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))));
    }

    public String toString(){
        return "Private Keys: p = " + p.toString() + ", q = " + q.toString() + ", Lambda = " +lambda +
                "\nPublic Keys: n = " + n.toString() + ", d = " + d.toString() + ", e = " + e.toString();
    }
}
