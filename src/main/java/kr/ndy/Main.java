package kr.ndy;

import kr.ndy.core.transaction.Transaction;
import kr.ndy.core.transaction.TransactionAccelerator;
import kr.ndy.core.transaction.TransactionBuilder;
import kr.ndy.core.transaction.TransactionInfo;
import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletAddress;
import kr.ndy.core.wallet.WalletGenerator;
import kr.ndy.crypto.DigitalSignatureSign;
import kr.ndy.crypto.DigitalSignatureVerify;
import kr.ndy.util.DateUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Date;

public class Main {

    public static void main(String... args)
    {
        Security.addProvider(new BouncyCastleProvider());
        Wallet wallet1 = WalletGenerator.create();
        WalletAddress address1 = wallet1.getAddress();

        Wallet wallet2 = WalletGenerator.create();
        WalletAddress address2 = wallet2.getAddress();

        System.out.println("##################Wallets################");
        address1.print();
        System.out.println(" ");
        address2.print();
        System.out.println("##################Transactions##############");

        Transaction transaction = TransactionBuilder.builder()
                .sender(address1.getWalletAddress())
                .receiver(address2.getWalletAddress())
                .senderPrivateKey(address1.toHexString(address1.getPrivateKey().getEncoded()))
                .senderPublicKey(address1.toHexString(address1.getPublicKey().getEncoded()))
                .amount(1.001)
                .createdAt(null)
                .build();
        TransactionAccelerator accelerator = TransactionAccelerator.accelerator(transaction);
        TransactionInfo txInfo = transaction.getTxInfo();
        System.out.println("Tx json : " + txInfo.toJson());
        String sign = accelerator.sign(transaction.getSenderPrivateKey(), transaction.getSenderPublicKey());
        System.out.println("sign:" + sign);

        DigitalSignatureVerify dsv = new DigitalSignatureVerify(TransactionInfo.fromJsonWithAcc(sign));
        boolean bVerify = dsv.decode();

        System.out.println("verify:" + bVerify);
    }

}
