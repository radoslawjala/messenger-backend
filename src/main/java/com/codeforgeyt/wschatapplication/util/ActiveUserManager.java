package com.codeforgeyt.wschatapplication.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class ActiveUserManager {

    private final Map<String, Object> map;
    private final List<ActiveUserChangeListener> listeners;
    private final ThreadPoolExecutor notifyPool;

    private ActiveUserManager() {
        map = new ConcurrentHashMap<>();
        listeners = new CopyOnWriteArrayList<>();
        notifyPool = new ThreadPoolExecutor(1, 5, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    public void add(String userName, String remoteAddress) {
        map.put(userName, remoteAddress);
        notifyListeners();
    }

    public void remove(String username) {
        map.remove(username);
        notifyListeners();
    }

    public Set<String> getAll() {
        return map.keySet();
    }

    public Set<String> getActiveUsersExceptCurrentUser(String username) {
        Set<String> users = new HashSet<>(map.keySet());
        users.remove(username);
        return users;
    }

    public void registerListener(ActiveUserChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActiveUserChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        notifyPool.submit(() -> listeners.forEach(ActiveUserChangeListener::notifyActiveUserChange));
    }

    public boolean userAlreadyExists(String username) {
        return map.containsKey(username);
    }
}

