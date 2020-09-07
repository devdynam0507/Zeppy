package kr.ndy;

import kr.ndy.client.DNSClient;
import kr.ndy.client.MessageClient;
import kr.ndy.p2p.P2P;
import kr.ndy.server.*;

public class Main {

    public static void main(String... args) throws Exception
    {
        P2P peers = new P2P();
        DNSClient dnsClient = new DNSClient(peers);
        MessageClient messageClient = new MessageClient(ServerOptions.TEST_MESSAGE_SERVER_PORT, peers, dnsClient);

        //TODO: 어차피 로컬로 연결할거 아니니까 DNS Seed를 처음에 refresh 해줘야함..
        //TODO: 그러므로 DNS서버에서 풀도느 어드레스 받아오고 클라이언트는 풀노드랑 첫 연결 해야함..
        //TODO: 그러면 풀노드는 그냥 연결만 받고 피어만 반환해주면 될듯함.
        //TODO: 처음에 PING-PONG Message 주고받으면서 OK 사인 반환하면 노드 연결 확립해주면 될듯!

        //모든 Server/Client 연결
        ProtocolExecutor executor = new ProtocolExecutor
        (
                dnsClient,
                new MessageServer(ServerOptions.TEST_MESSAGE_SERVER_PORT, peers, dnsClient),
                new FileTransferSever(ServerOptions.TEST_FILE_TRANSFER_SERVER_PORT),
                messageClient
        );
        executor.executeService();

    }

}
