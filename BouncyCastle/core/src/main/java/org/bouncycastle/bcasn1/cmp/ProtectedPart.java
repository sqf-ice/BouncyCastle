package org.bouncycastle.bcasn1.cmp;

import org.bouncycastle.bcasn1.ASN1EncodableVector;
import org.bouncycastle.bcasn1.ASN1Object;
import org.bouncycastle.bcasn1.ASN1Primitive;
import org.bouncycastle.bcasn1.ASN1Sequence;
import org.bouncycastle.bcasn1.DERSequence;

public class ProtectedPart
    extends ASN1Object
{
    private PKIHeader header;
    private PKIBody body;

    private ProtectedPart(ASN1Sequence seq)
    {
        header = PKIHeader.getInstance(seq.getObjectAt(0));
        body = PKIBody.getInstance(seq.getObjectAt(1));
    }

    public static ProtectedPart getInstance(Object o)
    {
        if (o instanceof ProtectedPart)
        {
            return (ProtectedPart)o;
        }

        if (o != null)
        {
            return new ProtectedPart(ASN1Sequence.getInstance(o));
        }

        return null;
    }

    public ProtectedPart(PKIHeader header, PKIBody body)
    {
        this.header = header;
        this.body = body;
    }

    public PKIHeader getHeader()
    {
        return header;
    }

    public PKIBody getBody()
    {
        return body;
    }

    /**
     * <pre>
     * ProtectedPart ::= SEQUENCE {
     *                    header    PKIHeader,
     *                    body      PKIBody
     * }
     * </pre>
     * @return a basic ASN.1 object representation.
     */
    public ASN1Primitive toASN1Primitive()
    {
        ASN1EncodableVector v = new ASN1EncodableVector(2);

        v.add(header);
        v.add(body);

        return new DERSequence(v);
    }
}