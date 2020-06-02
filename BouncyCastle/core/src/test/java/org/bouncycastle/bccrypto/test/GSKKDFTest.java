package org.bouncycastle.bccrypto.test;

import org.bouncycastle.bccrypto.DataLengthException;
import org.bouncycastle.bccrypto.agreement.kdf.GSKKDFParameters;
import org.bouncycastle.bccrypto.agreement.kdf.GSKKFDGenerator;
import org.bouncycastle.bccrypto.digests.SHA256Digest;
import org.bouncycastle.bcutil.encoders.Hex;
import org.bouncycastle.bcutil.test.SimpleTest;

public class GSKKDFTest
    extends SimpleTest
{
    public String getName()
    {
        return "GSKKDFTest";
    }

    public void performTest()
        throws Exception
    {
        GSKKFDGenerator gen = new GSKKFDGenerator(new SHA256Digest());

        byte[] key = new byte[16];

        gen.init(new GSKKDFParameters(Hex.decode("0102030405060708090a"), 1, Hex.decode("27252622")));

        gen.generateBytes(key, 0, key.length);
        areEqual(Hex.decode("bd9ff24b9cc4d91b70af951989b4d719"), key);
        
        gen.generateBytes(key, 0, key.length);
        areEqual(Hex.decode("d5934f681ad1e860981eb1792af68e20"), key);

        gen = new GSKKFDGenerator(new SHA256Digest());
        
        gen.init(new GSKKDFParameters(Hex.decode("0102030405060708090a"), 2, Hex.decode("27252622")));

        gen.generateBytes(key, 0, key.length);
        areEqual(Hex.decode("d5934f681ad1e860981eb1792af68e20"), key);

        gen.init(new GSKKDFParameters(Hex.decode("0102030405060708090a"), 1));

        gen.generateBytes(key, 0, key.length);
        areEqual(Hex.decode("3c6e999b2cb08d8d8dd261cd23f15ed6"), key);

        gen.generateBytes(key, 0, key.length);
        areEqual(Hex.decode("019ce1fcf81b94602f2f8678be905e0e"), key);

        try
        {
            gen.generateBytes(key, 1, key.length);
        }
        catch (DataLengthException e)
        {
            isEquals("output buffer too small", e.getMessage());
        }
    }
    
    public static void main(
        String[]    args)
    {
        runTest(new GSKKDFTest());
    }
}
