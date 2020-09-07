package kr.ndy;

import kr.ndy.client.DNSClient;
import kr.ndy.client.MessageClient;
import kr.ndy.p2p.P2P;
import kr.ndy.server.*;

public class Main {

    public static void main(String... args)
    {
        P2P peers = new P2P();
        DNSClient dnsClient = new DNSClient(peers);

        ProtocolExecutor executor = new ProtocolExecutor
        (
                dnsClient,
                new MessageServer(ServerOptions.TEST_MESSAGE_SERVER_PORT, peers, dnsClient),
                new FileTransferSever(ServerOptions.TEST_FILE_TRANSFER_SERVER_PORT)
        );
        executor.executeService();

        MessageClient client = new MessageClient(ServerOptions.TEST_MESSAGE_SERVER_PORT);
        client.enable();
    }

}
