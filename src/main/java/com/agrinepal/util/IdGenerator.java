package com.agrinepal.util;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() {
    }

    public static String generate(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
