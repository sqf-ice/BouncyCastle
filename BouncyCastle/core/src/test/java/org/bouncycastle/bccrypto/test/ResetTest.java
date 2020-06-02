package org.bouncycastle.bccrypto.test;

import org.bouncycastle.bccrypto.BufferedBlockCipher;
import org.bouncycastle.bccrypto.DataLengthException;
import org.bouncycastle.bccrypto.InvalidCipherTextException;
import org.bouncycastle.bccrypto.engines.DESEngine;
import org.bouncycastle.bccrypto.params.KeyParameter;
import org.bouncycastle.bcutil.encoders.Hex;
import org.bouncycastle.bcutil.test.SimpleTest;

public class ResetTest
    extends SimpleTest
{
    private static final byte[]   input = Hex.decode("4e6f77206973207468652074696d6520666f7220616c6c20");
    private static final byte[]   output = Hex.decode("3fa40e8a984d48156a271787ab8883f9893d51ec4b563b53");
    public String getName()
    {
        return "Reset";
    }

    public void performTest()
        throws Exception
    {
        BufferedBlockCipher cipher = new BufferedBlockCipher(new DESEngine());

        KeyParameter param = new KeyParameter(Hex.decode("0123456789abcdef"));

        basicTrial(cipher, param);

        cipher.init(false, param);

        byte[] out = new byte[input.length];
        
        int len2 = cipher.processBytes(output, 0, output.length - 1, out, 0);

        try
        {
            cipher.doFinal(out, len2);
            fail("no DataLengthException - short input");
        }
        catch (DataLengthException e)
        {
            // ignore
        }

        len2 = cipher.processBytes(output, 0, output.length, out, 0);

        cipher.doFinal(out, len2);

        if (!areEqual(input, out))
        {
            fail("failed reversal one got " + new String(Hex.encode(out)));
        }

        len2 = cipher.processBytes(output, 0, output.length - 1, out, 0);

        try
        {
            cipher.doFinal(out, len2);
            fail("no DataLengthException - short output");
        }
        catch (DataLengthException e)
        {
            // ignore
        }

        len2 = cipher.processBytes(output, 0, output.length, out, 0);

        cipher.doFinal(out, len2);

        if (!areEqual(input, out))
        {
            fail("failed reversal two got " + new String(Hex.encode(out)));
        }
    }

    private void basicTrial(BufferedBlockCipher cipher, KeyParameter param)
        throws InvalidCipherTextException
    {
        cipher.init(true, param);

        byte[]  out = new byte[input.length];

        int len1 = cipher.processBytes(input, 0, input.length, out, 0);

        cipher.doFinal(out, len1);

        if (!areEqual(out, output))
        {
            fail("failed - " + "expected " + new String(Hex.encode(output)) + " got " + new String(Hex.encode(out)));
        }
    }

    public static void main(
        String[]    args)
    {
        runTest(new ResetTest());
    }
}
