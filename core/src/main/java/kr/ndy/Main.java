package kr.ndy;

import kr.ndy.core.ZeppyModule;
import kr.ndy.core.blockchain.BlockFileCache;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {

    public static void main(String... args)
    {
        Security.addProvider(new BouncyCastleProvider());
        BlockFileCache cache = ZeppyModule.getInstance().getFileCache();

        //TODO: UTXO를  이용하여  송금, 잔액확인 기능  추가해야함.

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
//
//        BlockHeader header = new BlockHeader(null);
//
//        while(true)
//        {
//            header.updateNonce();
//            System.out.println(new SHA256(null).toHexString(header.getBlockInfo().hash()));
//        }
    }

}
