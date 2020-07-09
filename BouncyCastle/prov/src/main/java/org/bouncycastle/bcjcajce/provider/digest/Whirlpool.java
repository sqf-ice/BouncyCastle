package org.bouncycastle.bcjcajce.provider.digest;

import org.bouncycastle.bcasn1.iso.ISOIECObjectIdentifiers;
import org.bouncycastle.bccrypto.CipherKeyGenerator;
import org.bouncycastle.bccrypto.digests.WhirlpoolDigest;
import org.bouncycastle.bccrypto.macs.HMac;
import org.bouncycastle.bcjcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.bcjcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.bcjcajce.provider.symmetric.util.BaseMac;

public class Whirlpool
{
    private Whirlpool()
    {

    }

    static public class Digest
        extends BCMessageDigest
        implements Cloneable
    {
        public Digest()
        {
            super(new WhirlpoolDigest());
        }

        public Object clone()
            throws CloneNotSupportedException
        {
            Digest d = (Digest)super.clone();
            d.digest = new WhirlpoolDigest((WhirlpoolDigest)digest);

            return d;
        }
    }

    /**
     * Whirlpool HMac
     */
    public static class HashMac
        extends BaseMac
    {
        public HashMac()
        {
            super(new HMac(new WhirlpoolDigest()));
        }
    }

    public static class KeyGenerator
        extends BaseKeyGenerator
    {
        public KeyGenerator()
        {
            super("HMACWHIRLPOOL", 512, new CipherKeyGenerator());
        }
    }

    public static class Mappings
        extends DigestAlgorithmProvider
    {
        private static final String PREFIX = Whirlpool.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            provider.addAlgorithm("MessageDigest.WHIRLPOOL", PREFIX + "$Digest");
            provider.addAlgorithm("MessageDigest", ISOIECObjectIdentifiers.whirlpool, PREFIX + "$Digest");

            addHMACAlgorithm(provider, "WHIRLPOOL", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }
}