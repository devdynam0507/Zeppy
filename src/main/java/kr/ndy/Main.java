package kr.ndy;

import kr.ndy.core.merkletree.MerkleTree;
import kr.ndy.core.transaction.Transaction;
import kr.ndy.core.transaction.TransactionAccelerator;
import kr.ndy.core.transaction.TransactionBuilder;
import kr.ndy.core.transaction.TransactionInfo;
import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletAddress;
import kr.ndy.core.wallet.WalletGenerator;
import kr.ndy.crypto.DigitalSignatureVerify;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Security;
import java.util.List;

public class Main {

    public static void main(String... args)
    {
        Security.addProvider(new BouncyCastleProvider());
//        Wallet wallet1 = WalletGenerator.create();
//        WalletAddress address1 = wallet1.getAddress();
//
//        Wallet wallet2 = WalletGenerator.create();
//        WalletAddress address2 = wallet2.getAddress();
//
//        System.out.println("##################Wallets################");
//        address1.print();
//        System.out.println(" ");
//        address2.print();
//        System.out.println("##################Transactions##############");
//
//        Transaction transaction = TransactionBuilder.builder()
//                .sender(address1.getWalletAddress())
//                .receiver(address2.getWalletAddress())
//                .senderPrivateKey(address1.toHexString(address1.getPrivateKey().getEncoded()))
//                .senderPublicKey(address1.toHexString(address1.getPublicKey().getEncoded()))
//                .amount(1.001)
//                .createdAt(null)
//                .build();
//        TransactionAccelerator accelerator = TransactionAccelerator.accelerator(transaction);
//        TransactionInfo txInfo = transaction.getTxInfo();
//        System.out.println("Tx json : " + txInfo.toJson());
//        String sign = accelerator.sign(transaction.getSenderPrivateKey(), transaction.getSenderPublicKey());
//        System.out.println("sign:" + sign);
//
//        DigitalSignatureVerify dsv = new DigitalSignatureVerify(TransactionInfo.fromJsonWithAcc(sign));
//        boolean bVerify = dsv.decode();
//
//        System.out.println("verify:" + bVerify);

        MerkleTree merkleTree = new MerkleTree();

        for(int i = 0; i < 8; i++)
        {
            Wallet wallet1 = WalletGenerator.create();
            WalletAddress address1 = wallet1.getAddress();

            Wallet wallet2 = WalletGenerator.create();
            WalletAddress address2 = wallet2.getAddress();

            Transaction transaction = TransactionBuilder.builder()
                    .sender(address1.getWalletAddress())
                    .receiver(address2.getWalletAddress())
                    .senderPrivateKey(address1.toHexString(address1.getPrivateKey().getEncoded()))
                    .senderPublicKey(address1.toHexString(address1.getPublicKey().getEncoded()))
                    .amount(1.001)
                    .createdAt(null)
                    .build();

            merkleTree.add(transaction);
        }

        List<byte[]> list = merkleTree.toMerkleTree();
        merkleTree.print(list);

          /*
    *
    * 863c74f4957382193b5d6568aeccadfa7a23100f47db8f60289355c4a094ffa 59a0255223467c58e86eb78682b370fb5c76b7087a079299e0dda253c6e70a78 31f4d8ef08e79af47f9118a589da461aef3a4ab2be1b02f2db8e34a12d101205 1db94a1f48d8986de6f6b38f44112781ed0bce29d7f8675586df77657105be04 7a0ea9e1f5c73e68617bb4c1f9b43b9f06d9a761811b2c87232f6b09d0072dc6 33b391addee4088d1ee9b79c8148738b7d6c397b5876fe836f608ad10b577dce -694c4d1d61a764c95cfa084ea9ea31aa1f4301d84818d33900b32e3a0e1ba88 652ebfafec0faba932897653aecc14707ac53eff16f3e5d3caefe10a03b08d81
                                    c08efcfc83019a6339353c442d914525f6d01d694ffbbc1e8aa3c806752a8a2                                                                     20eec0b0535d5e1d3ce21f5f8cd9a5d46c3073c5265f1d5e54b1aef3b54ee93d                                                               5b7bdb1db2bde36cd25bed728c90be36cfcacd5deb143815af5c94f777016ca4                                                                        -3233234ea3bbb1d444e7fd92dc950bae6eafc52f43295331709a24ba0d770533
                                                                                                        -44a33ac5de8ebdf3d83822e280ed754b42f31e17cb871312d91911cf8a6bb9e8                                                                845e91d2844e24bbaa8736cd5f2d2d5c54b1d3f69010b303fcb3b064123ad82
                                                                                                                                                                        -4add4952d0432a5a6cc301058e57c868b98630eabdb3ea78f82990181508fd99
    * */
        byte[] hash1 = new BigInteger("863c74f4957382193b5d6568aeccadfa7a23100f47db8f60289355c4a094ffa", 16).toByteArray();
        byte[] hash2 = new BigInteger("59a0255223467c58e86eb78682b370fb5c76b7087a079299e0dda253c6e70a78", 16).toByteArray();
        byte[] merged = merkleTree.merge(hash1, hash2);

        System.out.println("hash1: " + new BigInteger(hash1).toString(16));
        System.out.println("hash2: " + new BigInteger(hash2).toString(16));
        System.out.println("merged: " + new BigInteger(merged).toString(16));

        byte[] hash3 = new BigInteger("31f4d8ef08e79af47f9118a589da461aef3a4ab2be1b02f2db8e34a12d101205", 16).toByteArray();
        byte[] hash4 = new BigInteger("1db94a1f48d8986de6f6b38f44112781ed0bce29d7f8675586df77657105be04", 16).toByteArray();
        byte[] merged2 = merkleTree.merge(hash3, hash4);

        System.out.println("hash3: " + new BigInteger(hash3).toString(16));
        System.out.println("hash4: " + new BigInteger(hash4).toString(16));
        System.out.println("merged: " + new BigInteger(merged2).toString(16));

        byte[] merged3 = merkleTree.merge(merged, merged2);
        System.out.println("merged 1 2 3 4 : " + new BigInteger(merged3).toString(16));
    }

}
