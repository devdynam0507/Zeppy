package kr.ndy;

import java.util.ArrayList;
import java.util.List;

public class DNSCache {

    private List<String> addressCache;

    public DNSCache()
    {
        this.addressCache = new ArrayList<>();
    }

    public void push(String address)
    {
        if(!addressCache.contains(address))
        {
            addressCache.add(address);
        }
    }

    public String get(String address)
    {
        String pick = null;
        int dnsSize = addressCache.size();

        if(dnsSize > 0)
        {
            do
            {
                int rand = (int) (Math.random() * addressCache.size());
                pick = addressCache.get(rand);

                if(address.equals(pick) && dnsSize == 1)
                {
                    pick = null;
                    break;
                }
            } while(address.equals(pick) && dnsSize > 0);
        }

        return pick;
    }

}
