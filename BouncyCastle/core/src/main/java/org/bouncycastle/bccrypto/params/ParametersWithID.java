package org.bouncycastle.bccrypto.params;

import org.bouncycastle.bccrypto.CipherParameters;

public class ParametersWithID
    implements CipherParameters
{
    private CipherParameters  parameters;
    private byte[] id;

    public ParametersWithID(
        CipherParameters parameters,
        byte[] id)
    {
        this.parameters = parameters;
        this.id = id;
    }

    public byte[] getID()
    {
        return id;
    }

    public CipherParameters getParameters()
    {
        return parameters;
    }
}