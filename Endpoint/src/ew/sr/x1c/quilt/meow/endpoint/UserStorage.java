package ew.sr.x1c.quilt.meow.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class UserStorage {

    private final ReentrantReadWriteLock mutex;
    private final Lock readLock;
    private final Lock writeLock;
    private final Map<String, RemoteUser> nameUser;

    public UserStorage() {
        mutex = new ReentrantReadWriteLock();
        readLock = mutex.readLock();
        writeLock = mutex.writeLock();
        nameUser = new HashMap<>();
    }

    public List<RemoteUser> getAllUser() {
        readLock.lock();
        try {
            return new ArrayList<>(nameUser.values());
        } finally {
            readLock.unlock();
        }
    }

    public void registerUser(RemoteUser user) {
        writeLock.lock();
        try {
            nameUser.put(user.getRemoteName(), user);
            MainGUI.getInstance().userAdd(user.getRemoteName());
        } finally {
            writeLock.unlock();
        }
    }

    public void deregisterUser(String name) {
        writeLock.lock();
        try {
            nameUser.remove(name);
            MainGUI.getInstance().userRemove(name);
        } finally {
            writeLock.unlock();
        }
    }

    public RemoteUser getUserByName(String name) {
        readLock.lock();
        try {
            return nameUser.get(name);
        } finally {
            readLock.unlock();
        }
    }
}
