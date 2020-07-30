package kr.ndy.core.transaction;

import kr.ndy.core.transaction.exception.TransactionSignFailedException;
import kr.ndy.crypto.Base64Crypto;
import kr.ndy.crypto.DigitalSignatureSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

public class TransactionAccelerator {

    private Transaction transaction;
    private static Logger logger = LoggerFactory.getLogger(TransactionAccelerator.class);

    private TransactionAccelerator(Transaction transaction)
    {
        this.transaction = transaction;
    }

    public static TransactionAccelerator accelerator(Transaction transaction)
    {
        return new TransactionAccelerator(transaction);
    }

    /**
     *
     * 1. 트랜잭션을 json 문자열로 만든 후 base64인코딩
     * 2. base64 인코딩된 트랙잭션 json과 public키를 담아 json 문자열 생성
     * 3. 생성된 문자열을 바탕으로 디지털 서명
     *
     * @param senderPrivateKey 서명에 필요한 송금자의 비밀키
     * @param senderPublicKey 인증에 필요한 송금자의 공개키
     *
     * @return 서명된 데이터 16진수 문자열
     * */
    public String sign(PrivateKey senderPrivateKey, PublicKey senderPublicKey) throws TransactionSignFailedException
    {
        TransactionInfo txInfo = transaction.getTxInfo();
        Base64Crypto txBase64 = new Base64Crypto(txInfo.toJson().getBytes());
        String txJsonWithBase64 = txBase64.toHexString(txBase64.encode());
        String txJsonWithAccelerator = txInfo.toJsonOfAccelerator(txJsonWithBase64, senderPublicKey); // Accelerator tx json
        DigitalSignatureSign signatureSign = new DigitalSignatureSign(txJsonWithAccelerator, senderPrivateKey);
        byte[] signed = signatureSign.encode(); //digit sign

        if(signed == null)
        {
            throw new TransactionSignFailedException("Transaction failed sign");
        }

        return txInfo.toJsonOfSignedTx(txJsonWithAccelerator, signed);
    }



}
