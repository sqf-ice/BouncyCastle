package org.bouncycastle.bcjcajce.provider.asymmetric.x509;

import java.security.cert.CertificateEncodingException;

import org.bouncycastle.bcasn1.x509.BasicConstraints;
import org.bouncycastle.bcjcajce.util.JcaJceHelper;

class X509CertificateInternal extends X509CertificateImpl
{
    private final byte[] encoding;

    X509CertificateInternal(JcaJceHelper bcHelper, org.bouncycastle.bcasn1.x509.Certificate c,
        BasicConstraints basicConstraints, boolean[] keyUsage, String sigAlgName, byte[] sigAlgParams, byte[] encoding)
    {
        super(bcHelper, c, basicConstraints, keyUsage, sigAlgName, sigAlgParams);

        this.encoding = encoding;
    }

    public byte[] getEncoded() throws CertificateEncodingException
    {
        if (null == encoding)
        {
            throw new CertificateEncodingException();
        }

        return encoding;
    }
}