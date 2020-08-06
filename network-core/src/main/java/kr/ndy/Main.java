package kr.ndy;

import kr.ndy.server.Server;
import kr.ndy.server.ServerSchema;

public class Main {

    public static void main(String... args)
    {
        Server server = new Server(ServerSchema.TEST_SERVER_PORT);
        server.enable();
    }

}
