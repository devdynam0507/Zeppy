package kr.ndy.core.merkletree;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.crypto.SHA256;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

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

    public List<byte[]> getMerkleBranch(byte[][] merkleTree, int index)
    {
        int treeSize = transactionHashList.size();
        List<byte[]> branch = new ArrayList<>();
        int j = 0;

        for(int i = treeSize; i > 1; i = (i + 1) / 2)
        {
            int d = Math.min(index ^ 1, treeSize - 1);
            branch.add(merkleTree[j + d]);
            index >>= 1;
            j += i;
        }
        System.out.println("indexof:" + index);

        return branch;
    }

    public byte[] verify(byte[][] merkleTree, byte[] target)
    {
        int merkleTreeSize = merkleTree.length * 2;
        int txListSize = transactionHashList.size();
        byte[] root = null;
        List<byte[]> branch = getMerkleBranch(merkleTree, indexOf(merkleTree, target));

        for(byte[] arr : branch)
        {
            System.out.println("branchIndex: " + indexOf(merkleTree, arr));
        }

        return root;
    }

    public boolean validation(byte[] target)
    {
        byte[][] merkleTree = toMerkleTree();
        boolean result;

        byte[] verify = verify(merkleTree, target);
        System.out.println(new BigInteger(merkleTree[merkleTree.length - 1]).toString(16));
        System.out.println(new BigInteger(verify).toString(16));

        result = Objects.deepEquals(merkleTree[merkleTree.length - 1], verify);

        return result;
    }

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

    public byte[] merge(byte[] arr1, byte[] arr2)
    {
        byte[] merged = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, merged, 0, arr1.length);
        System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);

        return new SHA256(merged).encode();
    }

}
