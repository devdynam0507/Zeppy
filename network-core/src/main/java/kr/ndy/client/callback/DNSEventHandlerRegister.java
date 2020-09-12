package kr.ndy.client.callback;

import java.util.HashSet;
import java.util.Set;

public class DNSEventHandlerRegister {

    private Set<DNSEvent> registeredHandlers;

    public DNSEventHandlerRegister()
    {
        this.registeredHandlers = new HashSet<>();
    }

    public void register(DNSEvent event)
    {
        registeredHandlers.add(event);
    }

    public void callOnGetNodeAddress(String content)
    {
        for(DNSEvent event : registeredHandlers)
        {
            event.onGetNodeAddress(content);
        }
    }

}
