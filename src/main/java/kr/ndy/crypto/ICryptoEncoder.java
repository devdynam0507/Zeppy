package kr.ndy.crypto;

public interface ICryptoEncoder {

    byte[] encode();
    String toHexString(byte[] src);

}
