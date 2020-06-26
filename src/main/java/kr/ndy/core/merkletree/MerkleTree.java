package kr.ndy.core.merkletree;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.crypto.SHA256;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    private List<byte[]> transactionHashList;

    public MerkleTree()
    {
        this.transactionHashList = new ArrayList<>();
    }

    public synchronized void add(Transaction transaction) {
        try
        {
            transactionHashList.add(new SHA256(transaction.getTxInfo().toJson().getBytes("UTF-8")).encode());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public List<byte[]> toMerkleTree()
    {
        int txListSize = transactionHashList.size();
        boolean bSizeOfOdd = txListSize == 1 || txListSize % 2 == 0 ? false : true;
        int merkleTreeSize = bSizeOfOdd ? txListSize * 2 : txListSize * 2 - 1;
        List<byte[]> merkleTree = new ArrayList<>(merkleTreeSize);
        int j = txListSize;

        transactionHashList.forEach(hash -> merkleTree.add(hash));

        for(int i = 0; i < merkleTreeSize - 1; i += 2)
        {
            byte[] hash1 = merkleTree.get(i);
            byte[] hash2 = merkleTree.get(i + 1);
            byte[] merged;

            if(bSizeOfOdd && hash2 == null)
            {
                merged = merge(hash1, hash1);
            } else
            {
                merged = merge(hash1, hash2);
            }

            merkleTree.add(j, merged);
            ++j;
        }

        return merkleTree;
    }

    /*
    *
    *                o
    *           o         o
    *        o     o     o   o
    *       o  o  o  o  o o o o
    *
    * */
    public void print(List<byte[]> merkleTree)
    {
        int j = transactionHashList.size();
        int mkSize = j;
        int p = 4;
        int i = 0;

        for(i = 0; i < 8; i++)
        {
            String hex = new BigInteger(merkleTree.get(i)).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 12; i++)
        {
            String hex = new BigInteger(merkleTree.get(i)).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 14; i++)
        {
            String hex = new BigInteger(merkleTree.get(i)).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 15; i++)
        {
            String hex = new BigInteger(merkleTree.get(i)).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
    }

    /*
    *
    * 863c74f4957382193b5d6568aeccadfa7a23100f47db8f60289355c4a094ffa 59a0255223467c58e86eb78682b370fb5c76b7087a079299e0dda253c6e70a78 31f4d8ef08e79af47f9118a589da461aef3a4ab2be1b02f2db8e34a12d101205 1db94a1f48d8986de6f6b38f44112781ed0bce29d7f8675586df77657105be04 7a0ea9e1f5c73e68617bb4c1f9b43b9f06d9a761811b2c87232f6b09d0072dc6 33b391addee4088d1ee9b79c8148738b7d6c397b5876fe836f608ad10b577dce -694c4d1d61a764c95cfa084ea9ea31aa1f4301d84818d33900b32e3a0e1ba88 652ebfafec0faba932897653aecc14707ac53eff16f3e5d3caefe10a03b08d81
                                    c08efcfc83019a6339353c442d914525f6d01d694ffbbc1e8aa3c806752a8a2                                                                     20eec0b0535d5e1d3ce21f5f8cd9a5d46c3073c5265f1d5e54b1aef3b54ee93d                                                               5b7bdb1db2bde36cd25bed728c90be36cfcacd5deb143815af5c94f777016ca4                                                                        -3233234ea3bbb1d444e7fd92dc950bae6eafc52f43295331709a24ba0d770533
                                                                                                        -44a33ac5de8ebdf3d83822e280ed754b42f31e17cb871312d91911cf8a6bb9e8                                                                845e91d2844e24bbaa8736cd5f2d2d5c54b1d3f69010b303fcb3b064123ad82
                                                                                                                                                                        -4add4952d0432a5a6cc301058e57c868b98630eabdb3ea78f82990181508fd99
    * */

    public byte[] merge(byte[] arr1, byte[] arr2)
    {
        byte[] merged = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, merged, 0, arr1.length - 1);
        System.arraycopy(arr2, 0, merged, arr1.length, arr2.length - 1);

        return new SHA256(merged).encode();
    }

}
