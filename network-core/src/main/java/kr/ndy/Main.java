package kr.ndy;

import kr.ndy.client.MessageClient;
import kr.ndy.server.FileTransferSever;
import kr.ndy.server.MessageServer;
import kr.ndy.server.ServerExecutor;
import kr.ndy.server.ServerOptions;

public class Main {

    public static void main(String... args)
    {
        ServerExecutor executor = new ServerExecutor
        (
                new MessageServer(ServerOptions.TEST_MESSAGE_SERVER_PORT),
                new FileTransferSever(ServerOptions.TEST_FILE_TRANSFER_SERVER_PORT)
        );
        executor.executeService();

        MessageClient client = new MessageClient(ServerOptions.TEST_MESSAGE_SERVER_PORT);
        client.enable();
    }

}
