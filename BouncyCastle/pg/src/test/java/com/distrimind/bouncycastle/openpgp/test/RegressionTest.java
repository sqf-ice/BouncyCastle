package com.distrimind.bouncycastle.openpgp.test;

import java.security.Security;

import com.distrimind.bouncycastle.util.test.SimpleTest;
import com.distrimind.bouncycastle.util.test.Test;

public class RegressionTest
{
    public static Test[] tests = {
        new BcPGPKeyRingTest(),
        new PGPKeyRingTest(),
        new BcPGPRSATest(),
        new PGPRSATest(),
        new BcPGPDSATest(),
        new PGPDSATest(),
        new BcPGPDSAElGamalTest(),
        new PGPDSAElGamalTest(),
        new BcPGPPBETest(),
        new PGPPBETest(),
        new PGPMarkerTest(),
        new PGPPacketTest(),
        new PGPArmoredTest(),
        new PGPSignatureTest(),
        new PGPClearSignedSignatureTest(),
        new PGPCompressionTest(),
        new PGPNoPrivateKeyTest(),
        new PGPECDSATest(),
        new PGPECDHTest(),
        new PGPECMessageTest(),
        new PGPParsingTest(),
        new PGPEdDSATest(),
        new SExprTest(),
        new ArmoredInputStreamTest(),
        new PGPUtilTest()
    };

    public static void main(String[] args)
    {
        Security.addProvider(new com.distrimind.bouncycastle.jce.provider.BouncyCastleProvider());

        SimpleTest.runTests(tests);
    }
}