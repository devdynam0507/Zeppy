package kr.ndy.server.task;

public interface FileTransferEvent {
    void onTransferFinish(Thread thread);
}
