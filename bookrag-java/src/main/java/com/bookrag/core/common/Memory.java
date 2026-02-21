package com.bookrag.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Memory {

    private List<Message> storage = new ArrayList<>();

    public void add(Message message) {
        if (!storage.contains(message)) {
            storage.add(message);
        }
    }

    public void addBatch(Iterable<Message> messages) {
        for (Message message : messages) {
            add(message);
        }
    }

    public List<Message> getByContent(String content) {
        return storage.stream()
                .filter(m -> m.getContent().contains(content))
                .collect(Collectors.toList());
    }

    public Message deleteNewest() {
        if (!storage.isEmpty()) {
            return storage.remove(storage.size() - 1);
        }
        return null;
    }

    public void delete(Message message) {
        storage.remove(message);
    }

    public void clear() {
        storage.clear();
    }

    public int count() {
        return storage.size();
    }

    public List<Message> get(int k) {
        if (k <= 0) return new ArrayList<>(storage);
        int start = Math.max(0, storage.size() - k);
        return new ArrayList<>(storage.subList(start, storage.size()));
    }
}
