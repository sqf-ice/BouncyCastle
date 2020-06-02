package org.bouncycastle.bccrypto.test;

import org.bouncycastle.bccrypto.AsymmetricBlockCipher;
import org.bouncycastle.bccrypto.AsymmetricCipherKeyPair;
import org.bouncycastle.bccrypto.InvalidCipherTextException;
import org.bouncycastle.bccrypto.encodings.OAEPEncoding;
import org.bouncycastle.bccrypto.encodings.PKCS1Encoding;
import org.bouncycastle.bccrypto.engines.RSABlindedEngine;
import org.bouncycastle.bccrypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.bccrypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.bccrypto.params.RSAKeyParameters;
import org.bouncycastle.bccrypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.bcutil.encoders.Hex;
import org.bouncycastle.bcutil.test.SimpleTest;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSABlindedTest
    extends SimpleTest
{
    static BigInteger mod = new BigInteger("b259d2d6e627a768c94be36164c2d9fc79d97aab9253140e5bf17751197731d6f7540d2509e7b9ffee0a70a6e26d56e92d2edd7f85aba85600b69089f35f6bdbf3c298e05842535d9f064e6b0391cb7d306e0a2d20c4dfb4e7b49a9640bdea26c10ad69c3f05007ce2513cee44cfe01998e62b6c3637d3fc0391079b26ee36d5", 16);
    static BigInteger  pubExp = new BigInteger("11", 16);
    static BigInteger  privExp = new BigInteger("92e08f83cc9920746989ca5034dcb384a094fb9c5a6288fcc4304424ab8f56388f72652d8fafc65a4b9020896f2cde297080f2a540e7b7ce5af0b3446e1258d1dd7f245cf54124b4c6e17da21b90a0ebd22605e6f45c9f136d7a13eaac1c0f7487de8bd6d924972408ebb58af71e76fd7b012a8d0e165f3ae2e5077a8648e619", 16);
    static BigInteger  p = new BigInteger("f75e80839b9b9379f1cf1128f321639757dba514642c206bbbd99f9a4846208b3e93fbbe5e0527cc59b1d4b929d9555853004c7c8b30ee6a213c3d1bb7415d03", 16);
    static BigInteger  q = new BigInteger("b892d9ebdbfc37e397256dd8a5d3123534d1f03726284743ddc6be3a709edb696fc40c7d902ed804c6eee730eee3d5b20bf6bd8d87a296813c87d3b3cc9d7947", 16);
    static BigInteger  pExp = new BigInteger("1d1a2d3ca8e52068b3094d501c9a842fec37f54db16e9a67070a8b3f53cc03d4257ad252a1a640eadd603724d7bf3737914b544ae332eedf4f34436cac25ceb5", 16);
    static BigInteger  qExp = new BigInteger("6c929e4e81672fef49d9c825163fec97c4b7ba7acb26c0824638ac22605d7201c94625770984f78a56e6e25904fe7db407099cad9b14588841b94f5ab498dded", 16);
    static BigInteger  crtCoef = new BigInteger("dae7651ee69ad1d081ec5e7188ae126f6004ff39556bde90e0b870962fa7b926d070686d8244fe5a9aa709a95686a104614834b0ada4b10f53197a5cb4c97339", 16);

    static String input = "4e6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e";

    //
    // to check that we handling byte extension by big number correctly.
    //
    static String edgeInput = "ff6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e";

    static byte[] oversizedSig = Hex.decode("01ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff004e6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e");
    static byte[] dudBlock = Hex.decode("000fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff004e6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e");
    static byte[] truncatedDataBlock = Hex.decode("0001ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff004e6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e");
    static byte[] incorrectPadding = Hex.decode("0001ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4e6f77206973207468652074696d6520666f7220616c6c20676f6f64206d656e");
    static byte[] missingDataBlock = Hex.decode("0001ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

    public String getName()
    {
        return "RSABlinded";
    }

    private void testStrictPKCS1Length(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        AsymmetricBlockCipher eng = new RSABlindedEngine();

        eng.init(true, privParameters);

        byte[] data = null;

        try
        {
            data = eng.processBlock(oversizedSig, 0, oversizedSig.length);
        }
        catch (Exception e)
        {
            fail("RSA: failed - exception " + e.toString(), e);
        }

        eng = new PKCS1Encoding(eng);

        eng.init(false, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);

            fail("oversized signature block not recognised");
        }
        catch (InvalidCipherTextException e)
        {
            if (!e.getMessage().equals("block incorrect size"))
            {
                fail("RSA: failed - exception " + e.toString(), e);
            }
        }

        //System.setProperty(PKCS1Encoding.STRICT_LENGTH_ENABLED_PROPERTY, "false");

        System.getProperties().put(PKCS1Encoding.STRICT_LENGTH_ENABLED_PROPERTY, "false");
        eng = new PKCS1Encoding(new RSABlindedEngine());

        eng.init(false, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (InvalidCipherTextException e)
        {
            fail("RSA: failed - exception " + e.toString(), e);
        }

        System.getProperties().remove(PKCS1Encoding.STRICT_LENGTH_ENABLED_PROPERTY);
    }

    private void testTruncatedPKCS1Block(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        checkForPKCS1Exception(pubParameters, privParameters, truncatedDataBlock, "block incorrect");
    }

    private void testDudPKCS1Block(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        checkForPKCS1Exception(pubParameters, privParameters, dudBlock, "block incorrect");
    }

    private void testWrongPaddingPKCS1Block(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        checkForPKCS1Exception(pubParameters, privParameters, incorrectPadding, "block incorrect");
    }

    private void testMissingDataPKCS1Block(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        checkForPKCS1Exception(pubParameters, privParameters, missingDataBlock, "block incorrect");
    }

    private void checkForPKCS1Exception(RSAKeyParameters pubParameters, RSAKeyParameters privParameters, byte[] inputData, String expectedMessage)
    {
        AsymmetricBlockCipher   eng = new RSABlindedEngine();

        eng.init(true, privParameters);

        byte[] data = null;

        try
        {
            data = eng.processBlock(inputData, 0, inputData.length);
        }
        catch (Exception e)
        {
            fail("RSA: failed - exception " + e.toString(), e);
        }

        eng = new PKCS1Encoding(eng);

        eng.init(false, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);

            fail("missing data block not recognised");
        }
        catch (InvalidCipherTextException e)
        {
            if (!e.getMessage().equals(expectedMessage))
            {
                fail("RSA: failed - exception " + e.toString(), e);
            }
        }
    }

    private void testOAEP(RSAKeyParameters pubParameters, RSAKeyParameters privParameters)
    {
        //
        // OAEP - public encrypt, private decrypt
        //
        AsymmetricBlockCipher eng = new OAEPEncoding(new RSABlindedEngine());
        byte[] data = Hex.decode(input);

        eng.init(true, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, privParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed OAEP Test");
        }
    }

    public void performTest()
    {
        RSAKeyParameters    pubParameters = new RSAKeyParameters(false, mod, pubExp);
        RSAKeyParameters    privParameters = new RSAPrivateCrtKeyParameters(mod, pubExp, privExp, p, q, pExp, qExp, crtCoef);
        byte[]              data = Hex.decode(edgeInput);

        //
        // RAW
        //
        AsymmetricBlockCipher   eng = new RSABlindedEngine();

        eng.init(true, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("RSA: failed - exception " + e.toString(), e);
        }

        eng.init(false, privParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!edgeInput.equals(new String(Hex.encode(data))))
        {
            fail("failed RAW edge Test");
        }

        data = Hex.decode(input);

        eng.init(true, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, privParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed RAW Test");
        }

        //
        // PKCS1 - public encrypt, private decrypt
        //
        eng = new PKCS1Encoding(eng);

        eng.init(true, pubParameters);

        if (eng.getOutputBlockSize() != ((PKCS1Encoding)eng).getUnderlyingCipher().getOutputBlockSize())
        {
            fail("PKCS1 output block size incorrect");
        }

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, privParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed PKCS1 public/private Test");
        }

        //
        // PKCS1 - private encrypt, public decrypt
        //
        eng = new PKCS1Encoding(((PKCS1Encoding)eng).getUnderlyingCipher());

        eng.init(true, privParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, pubParameters);

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed PKCS1 private/public Test");
        }

        //
        // key generation test
        //
        RSAKeyPairGenerator pGen = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters genParam = new RSAKeyGenerationParameters(
                                            BigInteger.valueOf(0x11), new SecureRandom(), 768, 25);

        pGen.init(genParam);

        AsymmetricCipherKeyPair pair = pGen.generateKeyPair();

        eng = new RSABlindedEngine();

        if (((RSAKeyParameters)pair.getPublic()).getModulus().bitLength() < 768)
        {
            fail("failed key generation (768) length test");
        }

        eng.init(true, pair.getPublic());

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, pair.getPrivate());

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed key generation (768) Test");
        }

        genParam = new RSAKeyGenerationParameters(BigInteger.valueOf(0x11), new SecureRandom(), 1024, 25);

        pGen.init(genParam);
        pair = pGen.generateKeyPair();

        eng.init(true, pair.getPublic());

        if (((RSAKeyParameters)pair.getPublic()).getModulus().bitLength() < 1024)
        {
            fail("failed key generation (1024) length test");
        }

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        eng.init(false, pair.getPrivate());

        try
        {
            data = eng.processBlock(data, 0, data.length);
        }
        catch (Exception e)
        {
            fail("failed - exception " + e.toString(), e);
        }

        if (!input.equals(new String(Hex.encode(data))))
        {
            fail("failed key generation (1024) test");
        }

        testOAEP(pubParameters, privParameters);
        testStrictPKCS1Length(pubParameters, privParameters);
        testDudPKCS1Block(pubParameters, privParameters);
        testMissingDataPKCS1Block(pubParameters, privParameters);
        testTruncatedPKCS1Block(pubParameters, privParameters);
        testWrongPaddingPKCS1Block(pubParameters, privParameters);

        try
        {
            new RSABlindedEngine().processBlock(new byte[]{ 1 }, 0, 1);
            fail("failed initialisation check");
        }
        catch (IllegalStateException e)
        {
            // expected
        }
    }


    public static void main(
        String[]    args)
    {
        runTest(new RSABlindedTest());
    }
}
