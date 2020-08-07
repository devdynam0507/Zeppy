package kr.ndy.codec.handler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ReadWriteable {

    private ReadWriteLock _rwLock;
    private Lock _readLock;
    private Lock _writeLock;

    protected ReadWriteable()
    {
        this._rwLock = new ReentrantReadWriteLock(false);
        this._readLock = _rwLock.readLock();
        this._writeLock = _rwLock.writeLock();
    }

    protected Lock getReadLock() { return _readLock; }
    protected Lock getWriteLock() { return _writeLock; }
}
