package kr.ndy.crypto;

public class CryptoObjectConverter {

    public static String toHexString(ICryptoEncoder cryptoObject)
    {
        return toHexString(cryptoObject.encode());
    }

    public static String toHexString(byte[] hash)
    {
        StringBuilder builder = new StringBuilder();

        for(byte b : hash)
        {
            builder.append(String.format("%02x", b & 0xFF));
        }

        return builder.toString();
    }

}