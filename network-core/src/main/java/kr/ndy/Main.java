package kr.ndy;

import kr.ndy.client.Client;
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

        Client client = new Client(ServerOptions.TEST_MESSAGE_SERVER_PORT);
        client.enable();
    }

}
