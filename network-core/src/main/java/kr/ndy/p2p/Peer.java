package kr.ndy.p2p;

public class Peer {

    private String address;
    private boolean isAlive; //피어의 인터넷이 연결되었는지

    private Peer(String address, boolean isAlive)
    {
        this.address = address;
        this.isAlive = isAlive;
    }

    public static Peer create(String address, boolean isAlive)
    {
        return new Peer(address, isAlive);
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return address.equals(((Peer) o).address);
    }

}
