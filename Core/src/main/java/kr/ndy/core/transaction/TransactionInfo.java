package kr.ndy.core.transaction;

import kr.ndy.crypto.Base64Crypto;
import kr.ndy.crypto.SHA256;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;

public class TransactionInfo {

    private Transaction transaction;

    public TransactionInfo(Transaction transaction)
    {
        this.transaction = transaction;
    }

    private static JSONObject fromJsonWithAccIn(String txAccJson)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = (JSONObject) new JSONParser().parse(txAccJson);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static TransactionAcceleratorObject fromJsonWithAcc(String accJson)
    {
        TransactionAcceleratorObject acceleratorObject = null;
        try
        {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(accJson);

            String acc = (String) jsonObject.get("acc");
            String signedStr = (String) jsonObject.get("signed");

            JSONObject accIn = fromJsonWithAccIn(acc);
            //Decrypt  double  base64
            String txJsonWithBase64 = new String(new Base64Crypto(((String) accIn.get("tx")).getBytes()).decode());
            txJsonWithBase64 = new String(new Base64Crypto(txJsonWithBase64.getBytes()).decode());
            String publicKey = (String) accIn.get("pub");

            acceleratorObject = new TransactionAcceleratorObject(acc, signedStr, publicKey, (JSONObject) new JSONParser().parse(txJsonWithBase64));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return acceleratorObject;
    }

    public static Transaction fromJsonWithTx(String txJson)
    {
        Transaction transaction = null;
        try
        {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(txJson);

            String sender = (String) jsonObject.get("sender");
            String receiver = (String) jsonObject.get("receiver");
            double amount = (double) jsonObject.get("amount");
            String createdAt = (String) jsonObject.get("createdAt");

            transaction = TransactionBuilder.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .amount(amount)
                    .createdAt(createdAt)
                    .build();
        } catch (ParseException e)
        {
            //TODO: LOGGING
            e.printStackTrace();
        }

        return transaction;
    }

    public String toJsonOfAccelerator(String txJsonBase64, PublicKey publicKey)
    {
        JSONObject object = new JSONObject();

        object.put("tx", txJsonBase64);
        object.put("pub", new BigInteger(publicKey.getEncoded()).toString(16));

        return object.toJSONString();
    }

    public String toJsonOfSignedTx(String jsonAccelerator, byte[] signed)
    {
        JSONObject object = new JSONObject();

        object.put("acc", jsonAccelerator);
        object.put("signed", new BigInteger(signed).toString(16));

        return object.toJSONString();
    }

    public String toJson()
    {
        JSONObject object = new JSONObject();
        SHA256 sha256 = new SHA256(new byte[100]);
        byte[] txid = sha256.encode();

        object.put("txid"," 0x" + sha256.toHexString(txid));
        object.put("sender", transaction.getSender());
        object.put("receiver", transaction.getReceiver());
        object.put("amount", transaction.getAmount());
        object.put("createdAt", transaction.getTxDate());

        return object.toJSONString();
    }

}
