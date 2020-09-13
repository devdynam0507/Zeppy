package kr.ndy;

import kr.ndy.client.DNSClient;
import kr.ndy.client.MessageClient;
import kr.ndy.client.callback.DNSEventHandlerRegister;
import kr.ndy.client.task.MessageClientDNSRefreshTask;
import kr.ndy.core.ZeppyThreadPoolManager;
import kr.ndy.p2p.P2P;
import kr.ndy.server.*;

import java.util.concurrent.TimeUnit;

public class NetworkTestApp {

    public static void main(String... args) throws Exception
    {
        P2P peers = new P2P();
        ZeppyThreadPoolManager threadPoolManager = ZeppyThreadPoolManager.getInstance();
        DNSEventHandlerRegister handlerRegister  = new DNSEventHandlerRegister();
        DNSClient dnsClient                      = new DNSClient(peers, handlerRegister);
        MessageClient messageClient              = new MessageClient(ServerOptions.TEST_MESSAGE_SERVER_PORT, peers, dnsClient);
        MessageClientDNSRefreshTask refresh      = new MessageClientDNSRefreshTask(messageClient, dnsClient);

        //TODO: 어차피 로컬로 연결할거 아니니까 DNS Seed를 처음에 refresh 해줘야함..
        //TODO: 그러므로 DNS서버에서 풀도느 어드레스 받아오고 클라이언트는 풀노드랑 첫 연결 해야함..
        //TODO: 그러면 풀노드는 그냥 연결만 받고 피어만 반환해주면 될듯함.
        //TODO: 처음에 PING-PONG Message 주고받으면서 OK 사인 반환하면 노드 연결 확립해주면 될듯!

        // ============= 내일 테스트 할거  ===========
        //TODO: DNS에서  풀노드  주소 받아오기.
        //TODO: 풀노드랑  연결  후 피어들 받아오기
        //TODO: 피어들과 연결  확릴  후  주기적으로  핑메세지  보냄
        //TODO: 응답해주는 피어들 상태  출력후 확인  해야함((에러픽스_)
        // ========================================

        //TODO: 가상화폐를 네트와크와 연동

        //모든 Server/Client 연결
        ProtocolExecutor executor = new ProtocolExecutor
        (
                dnsClient,
                new MessageServer(ServerOptions.TEST_MESSAGE_SERVER_PORT, peers, dnsClient),
                new FileTransferSever(ServerOptions.TEST_FILE_TRANSFER_SERVER_PORT)
        );
        executor.executeService();
        handlerRegister.register(messageClient);

        threadPoolManager.service(refresh, 0L, 5000L, TimeUnit.MILLISECONDS);
    }

}
