package org.bouncycastle.tls.crypto.impl.bc;

import java.io.IOException;
import java.io.OutputStream;

import org.bouncycastle.bccrypto.CryptoException;
import org.bouncycastle.bccrypto.Signer;
import org.bouncycastle.bccrypto.io.SignerOutputStream;
import org.bouncycastle.tls.AlertDescription;
import org.bouncycastle.tls.TlsFatalAlert;
import org.bouncycastle.tls.crypto.TlsStreamSigner;

class BcTlsStreamSigner
    implements TlsStreamSigner
{
    private final SignerOutputStream output;

    BcTlsStreamSigner(Signer signer)
    {
        this.output = new SignerOutputStream(signer);
    }

    public OutputStream getOutputStream() throws IOException
    {
        return output;
    }

    public byte[] getSignature() throws IOException
    {
        try
        {
            return output.getSigner().generateSignature();
        }
        catch (CryptoException e)
        {
            throw new TlsFatalAlert(AlertDescription.internal_error, e);
        }
    }
}
