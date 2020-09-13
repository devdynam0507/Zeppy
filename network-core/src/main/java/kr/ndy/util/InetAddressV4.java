package kr.ndy.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressV4 {

    public static String getLocalAddress()
    {
        return _getLocalAddress().getHostAddress();
    }

    public static InetAddress _getLocalAddress()
    {
        InetAddress address = null;

        try
        {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        return address;
    }

}
