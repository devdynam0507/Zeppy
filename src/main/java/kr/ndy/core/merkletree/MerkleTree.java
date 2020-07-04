package kr.ndy.core.merkletree;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.crypto.SHA256;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

public class MerkleTree {

    private List<byte[]> transactionHashList;

    public MerkleTree()
    {
        this.transactionHashList = new ArrayList<>();
    }

    public synchronized void add(Transaction transaction) {
        try
        {
            System.out.println("tx hash: " + new BigInteger(new SHA256(transaction.getTxInfo().toJson().getBytes("UTF-8")).encode()).toString(16));
            transactionHashList.add(new SHA256(transaction.getTxInfo().toJson().getBytes("UTF-8")).encode());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public byte[][] toMerkleTree()
    {
        int txListSize = transactionHashList.size();
        int merkleTreeSize = txListSize * 2;
        byte[][] merkleTree = new byte[merkleTreeSize][];
        int j = txListSize;

        for(int i = 0; i < txListSize; i++)
        {
            merkleTree[i] = transactionHashList.get(i);
        }

        for(int i = 0; i < merkleTreeSize - 2; i += 2)
        {
            boolean bOddCondition = (i < txListSize && i + 1 >= txListSize);
            byte[] hash1 = merkleTree[i];
            byte[] hash2 = bOddCondition ? hash1 : merkleTree[i + 1];
            byte[] merged = merge(hash1, hash2);

            System.out.println("hash1: " + new BigInteger(merkleTree[i]).toString(16));
            System.out.println("hash2: " + new BigInteger(hash2).toString(16));
            System.out.println();
            System.out.println("build merged{j}: ".replace("{j}", j + "") + new BigInteger(merged).toString(16));
            System.out.println("i:" + i);
            System.out.println();

            if(bOddCondition)
            {
                i -= 1;
            }

            merkleTree[j] = merged;
            ++j;
        }

        return merkleTree;
    }

    public byte[] verify(byte[][] merkleTree, byte[] target)
    {
        int merkleTreeSize = merkleTree.length * 2;
        int txListSize = transactionHashList.size();
        byte[] root = null;
            /*
            * 1cdbe7659e659204e482168e498168e27e3776c8fe47b89e0b2a74acccbc5a56 -637266c6c679f896c059ed3f437fabbaa231c64d229b5d1f7569ef683f28c4ce 33e64f533625445d496c7c8a60bac3fbf706888f6faef10f4cb8cfea8d60395a 3fb9edd2cce4c0d4ada80e603bc53ca4c441184c5e4af432672248857c88d29f 21e457f703fa5439b31ecdd320d3747f4bf667aab6575a30e0d8ef2295d192d 845cf90da5d287ec3d5335f04e75003035d4780883021e1640fbdd4f9ddd16d -3396c3879fcf2c08da1180fff63d60a3af2450fd6d34b4883e82b3bfcae98964
                                         14c4cd14e415e051149b96f87ffefccee48046423cfe9e957ef40dc5d6eb7cd2 10eddf510efe320bc092a0dc957bfb1e9025cb4400708b2429fc5f57a78ae5f4 5703058d2e70c6382f139645a6deb66671041a0f9fe5af62045ffc3b49af2d72 -6c5ca13e6ab016f8374d02af343b943811391f2e4905c3631f9a2ac887d029d7
                                                                         754a285107e177b7dbb64f42bbc907881a3ea45ee5f586ac2a08bd1a3a7b236b                                                       -6a73c8032d888124cf06267a77a11674fc1b08e9ce1f27774421fab960b9de8b
            5753e29569d37e22978d4a4652d81a4ae75975244cd5b0318f0f2688abeb8e90

            * */

        for(int i = 0; i < merkleTreeSize - 2; i += 2)
        {
            boolean bOddCondition = (i < txListSize && i + 1 >= txListSize);

            if (Objects.deepEquals(merkleTree[i], target)) {
                if(i + 1 >= merkleTreeSize)
                {
                    break;
                }
                target = merge(target, merkleTree[i + 1]);
            }
            if (Objects.deepEquals(merkleTree[i + 1], target)) {
                target = merge(merkleTree[i], target);
            }


            if(bOddCondition)
            {
                --i;
            }

            root = target;

            if(Objects.deepEquals(root, merkleTree[merkleTree.length - 1]))
            {
                break;
            }
            System.out.println("root: " + new BigInteger(root).toString(16));
        }

        return root;
    }

    public boolean validation(byte[] target)
    {
        byte[][] merkleTree = toMerkleTree();
        boolean result;

        byte[] verify = verify(merkleTree, target);
        System.out.println(new BigInteger(merkleTree[merkleTree.length -1]).toString(16));
        System.out.println(new BigInteger(verify).toString(16));

        result = Objects.deepEquals(merkleTree[merkleTree.length - 1], verify);

        return result;
    }

    private int indexOf(byte[][] merkleTree, byte[] target)
    {
        int index;
        boolean bFind = false;

        for(index = 0 ; index < merkleTree.length; index++)
        {
            if(Objects.deepEquals(target, merkleTree[index]))
            {
                bFind = true;
                break;
            }
        }

        return bFind ? index : -1;
    }

    /*
    *
    *                o
    *           o         o
    *        o     o     o   o
    *       o  o  o  o  o o o o
    *
    *
    *
    *                     o
    *             o             o
    *         o       o       o   o
    *       o   o   o   o   o   o   o
    *
    * */
    public void print(byte[][] merkleTree)
    {
        int j = transactionHashList.size();
        int mkSize = j;
        int p = 4;
        int i = 0;

        for(i = 0; i < 7; i++)
        {
            String hex = new BigInteger(merkleTree[i]).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 11; i++)
        {
            String hex = new BigInteger(merkleTree[i]).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 13; i++)
        {
            String hex = new BigInteger(merkleTree[i]).toString(16);
            System.out.print(hex + " ");
        }
        System.out.println();
        for(; i < 14; i++)
        {
            String hex = new BigInteger(merkleTree[i]).toString(16);
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
        BigInteger integer1 = new BigInteger(arr1);
        BigInteger integer2 = new BigInteger(arr2);
        integer1.add(integer2);

        byte[] merged = integer1.toByteArray();

        return new SHA256(merged).encode();
    }

}
