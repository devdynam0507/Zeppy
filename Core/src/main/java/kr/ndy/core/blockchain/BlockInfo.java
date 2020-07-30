package kr.ndy.core.blockchain;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.core.transaction.TransactionAccelerator;
import kr.ndy.core.transaction.TransactionInfo;
import kr.ndy.crypto.SHA256;
import kr.ndy.util.ByteUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class BlockInfo {

    private BlockHeader block;

    public BlockInfo(BlockHeader block)
    {
        this.block = block;
    }

    public static BlockHeader fromJson(String json)
    {
        byte[] previousHash, blockHash;
        long nonce;
        String time;
        List<Transaction> transactions;
        BlockBuilder builder = BlockBuilder.builder();

        try
        {
            JSONObject object = (JSONObject) new JSONParser().parse(json);
            JSONObject transactionJsonObject = (JSONObject) object.get("transactions");
            int transactionSize = transactionJsonObject.size();

            blockHash = new BigInteger((String) object.get("block_hash"), 16).toByteArray();
            previousHash = new BigInteger((String) object.get("prev_hash"), 16).toByteArray();
            nonce = (long) object.get("nonce");
            time = (String) object.get("time");
            transactions = new ArrayList<>();

            for(int i = 0; i < transactionSize; i++)
            {
                String transactionJson = (String) transactionJsonObject.get(i + "");
                transactions.add(TransactionInfo.fromJsonWithAcc(transactionJson));
            }

            builder.hash(blockHash)
                    .previous(previousHash)
                    .nonce(nonce)
                    .time(time)
                    .transactions(transactions);
        } catch (ParseException e)
        {
            //TODO: handle  builder  exception
            e.printStackTrace();
        }

        return builder.build();
    }

    public byte[] hash()
    {
        byte[] prevHash = ByteUtil.toLittleEndian(block.getPreviousHash());
        byte[] merkleRoot = ByteUtil.toLittleEndian(block.getMerkleRoot());
        byte[] arr = new byte[24 + prevHash.length + merkleRoot.length];

        copy(arr, prevHash, merkleRoot);
        arr = new SHA256(arr).encode();
        arr = new SHA256(arr).encode();

        System.out.println(ByteUtil.toHex(arr));

        return ByteUtil.toLittleEndian(arr);
    }

    public String toJson()
    {
        JSONObject object = new JSONObject();
        JSONObject transactions = new JSONObject();
        BlockBody body = block.getBody();

        object.put("merkle_root", ByteUtil.toHex(block.getMerkleTree().toMerkleTree()[0]));
        object.put("block_hash", ByteUtil.toHex(block.getBlockHash()));
        object.put("prev_hash", ByteUtil.toHex(block.getPreviousHash()));
        object.put("version", block.getVersion());
        object.put("nonce", block.getNonce());
        object.put("diff", block.getDifficulty());
        object.put("time", block.getTimeStamp());
        object.put("fee", body.getFee());

        int i = 0;
        for(Transaction tx : body.getTransactions())
        {
            TransactionAccelerator accelerator = TransactionAccelerator.accelerator(tx);
            transactions.put(i + "", accelerator.sign(tx.getSenderPrivateKey(), tx.getSenderPublicKey()));
            ++i;
        }

        object.put("transactions", transactions);

        return object.toJSONString();
    }

    private void copy(byte[] arr, byte[] prevHash, byte[] merkleRoot)
    {
        int sum = prevHash.length + merkleRoot.length;

        System.arraycopy(merkleRoot, 0, arr, 0, merkleRoot.length);
        System.arraycopy(prevHash, 0, arr,  merkleRoot.length, prevHash.length);
        System.arraycopy(ByteUtil.toByte(block.getVersion()), 0, arr, sum, 4);
        System.arraycopy(ByteUtil.toByte(block.getNonce()), 0, arr, sum + 4, 8);
        System.arraycopy(ByteUtil.toByte(block.getDifficulty()), 0, arr, sum + 4 + 8, 4);
        System.arraycopy(ByteUtil.toByte(block.getTime()), 0, arr, sum + 4 + 8 + 4, 8);
    }

}
