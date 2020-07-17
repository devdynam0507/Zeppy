package kr.ndy.core.merkletree;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.crypto.SHA256;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class MerkleTree {

    private List<byte[]> transactionHashList;

    public MerkleTree()
    {
        this.transactionHashList = new ArrayList<>();
    }

    public void add(Transaction transaction) {
        try
        {
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

        if(txListSize > 4)
        {
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

                if(bOddCondition)
                {
                    i -= 1;
                }

                merkleTree[j] = merged;
                ++j;
            }
        }

        return merkleTree;
    }

    private List<Integer> getMerkleBranch(int index)
    {
        int treeSize = transactionHashList.size() - 1;
        List<Integer> branch = new ArrayList<>();
        int j = 0;

        for(int i = treeSize; i > 1; i = (i + 1) / 2)
        {
            int d = Math.min(index ^ 1, treeSize - 1);
            int idx = j + d;
            branch.add(idx);
            index >>= 1;
            j += i;

            if(idx < 0)
            {
                branch.clear();
                break;
            }
        }

        return branch;
    }

    private List<Integer> getBranchCouple(List<Integer> branch, byte[][] merkleTree)
    {
        int merkleTreeSize = merkleTree.length * 2;
        List<Integer> coupleIndex = new ArrayList<>();
        boolean bSelfHash = false;

        for(int i = 0; i < merkleTreeSize - 2; i += 2)
        {
            boolean bContainsInBranch = branch.contains(i);

            if(!bSelfHash && i > transactionHashList.size())
            {
                --i;
                bSelfHash = true;
            }
            if(bContainsInBranch)
            {
                coupleIndex.add(i + 1);
            }else if(branch.contains(i + 1))
            {
                coupleIndex.add(i);
            }
        }

        return coupleIndex;
    }

    /**
     * @return merkle root
     * */
    private byte[] verify(byte[][] merkleTree, byte[] target)
    {
        byte[] root = null;
        List<Integer> branch = getMerkleBranch(indexOf(merkleTree, target));
        List<Integer> coupleBranch = getBranchCouple(branch, merkleTree);
        int index = indexOf(merkleTree, target);

        for(int i = 0; i < branch.size(); i++)
        {
            int branchIndex = branch.get(i);
            int coupleBranchIndex = coupleBranch.get(i);

            if(root == null)
            {
                root = merge(merkleTree[Math.min(branchIndex, coupleBranchIndex)], merkleTree[Math.max(branchIndex, coupleBranchIndex)]);
            }else
            {
                if((index & 1) == 1)
                {
                    root = merge(merkleTree[branchIndex], merkleTree[coupleBranchIndex]);
                }else
                {
                    root = merge(merkleTree[coupleBranchIndex], merkleTree[branchIndex]);
                }
            }

            index >>= 1;
        }

        return root;
    }

    /**
     * @return target hashing to tree top and equals merkle root
     * */
    public boolean validation(byte[] target) {
        byte[][] merkleTree = toMerkleTree();
        byte[] verify = verify(merkleTree, target);

        return verify != null && Objects.deepEquals(merkleTree[merkleTree.length - 1], verify);
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

    private byte[] merge(byte[] arr1, byte[] arr2)
    {
        byte[] merged = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, merged, 0, arr1.length);
        System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);

        return new SHA256(merged).encode();
    }

}
