package kr.ndy;

import kr.ndy.core.ZeppyModule;
import kr.ndy.core.blockchain.BlockChain;
import kr.ndy.core.blockchain.BlockFileCache;
import kr.ndy.core.transaction.Transaction;
import kr.ndy.core.transaction.UnspentTransaction;
import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static Wallet wallet1, wallet2;

    public static void streamForKeyInput()
    {
        Scanner scanner = new Scanner(System.in);
        BlockChain chain = ZeppyModule.getInstance().getBlockChain();

        new Thread(() -> {
            while(true)
            {
                String command = scanner.nextLine();
                List<Transaction> utxo = UnspentTransaction.collectUnspentTransactions(wallet1.getAddress().getWalletAddress(), chain);
            }
        }).start();
    }

    public static void main(String... args)
    {
        Security.addProvider(new BouncyCastleProvider());
        Main.wallet1 = WalletGenerator.create();
        Main.wallet2 = WalletGenerator.create();
        BlockFileCache cache = ZeppyModule.getInstance().getFileCache();
        ZeppyModule.getInstance().executeModuleTasks();

        //TODO: UTXO를  이용하여  송금, 잔액확인 기능  추가해야함.
        streamForKeyInput();

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
