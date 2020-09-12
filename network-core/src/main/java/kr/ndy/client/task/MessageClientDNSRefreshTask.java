package kr.ndy.client.task;

import kr.ndy.client.DNSClient;
import kr.ndy.client.MessageClient;

public class MessageClientDNSRefreshTask extends Thread {

    private MessageClient messageClient;
    private DNSClient dnsClient;

    public MessageClientDNSRefreshTask(MessageClient messageClient, DNSClient dnsClient)
    {
        this.messageClient = messageClient;
        this.dnsClient     = dnsClient;
    }

    @Override
    public void run()
    {
        try
        {
            dnsClient.requestAddress();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
