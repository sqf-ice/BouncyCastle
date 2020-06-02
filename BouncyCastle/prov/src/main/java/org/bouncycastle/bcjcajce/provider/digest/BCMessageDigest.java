package org.bouncycastle.bcjcajce.provider.digest;

import java.security.MessageDigest;

import org.bouncycastle.bccrypto.Digest;
import org.bouncycastle.bccrypto.Xof;

public class BCMessageDigest
    extends MessageDigest
{
    protected Digest  digest;
    protected int     digestSize;

    protected BCMessageDigest(
        Digest digest)
    {
        super(digest.getAlgorithmName());

        this.digest = digest;
        this.digestSize = digest.getDigestSize();
    }

    protected BCMessageDigest(
        Xof digest, int outputSize)
    {
        super(digest.getAlgorithmName());

        this.digest = digest;
        this.digestSize = outputSize / 8;
    }

    public void engineReset() 
    {
        digest.reset();
    }

    public void engineUpdate(
        byte    input) 
    {
        digest.update(input);
    }

    public void engineUpdate(
        byte[]  input,
        int     offset,
        int     len) 
    {
        digest.update(input, offset, len);
    }

    public int engineGetDigestLength()
    {
        return digestSize;
    }

    public byte[] engineDigest() 
    {
        byte[]  digestBytes = new byte[digestSize];

        digest.doFinal(digestBytes, 0);

        return digestBytes;
    }
}
