package org.bouncycastle.bccrypto.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.bouncycastle.bccrypto.KeyParser;
import org.bouncycastle.bccrypto.params.AsymmetricKeyParameter;
import org.bouncycastle.bccrypto.params.DHParameters;
import org.bouncycastle.bccrypto.params.DHPublicKeyParameters;
import org.bouncycastle.bcutil.io.Streams;

public class DHIESPublicKeyParser
    implements KeyParser
{
    private DHParameters dhParams;

    public DHIESPublicKeyParser(DHParameters dhParams)
    {
        this.dhParams = dhParams;
    }

    public AsymmetricKeyParameter readKey(InputStream stream)
        throws IOException
    {
        byte[] V = new byte[(dhParams.getP().bitLength() + 7) / 8];

        Streams.readFully(stream, V, 0, V.length);

        return new DHPublicKeyParameters(new BigInteger(1, V), dhParams);
    }
}