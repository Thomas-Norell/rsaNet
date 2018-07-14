package rsa;

import java.io.Serializable;

import java.math.BigInteger;

public class PrivateKeys implements Serializable{
    public BigInteger d;
    public BigInteger p;
    public BigInteger q;
    public BigInteger lambda;
    public BigInteger n;
    public BigInteger e;
    public PrivateKeys(Keys key){
        d = key.d;
        p = key.p;
        q = key.q;
        lambda = key.lambda;
        n = key.n;
        e = key.e;
    }
}
