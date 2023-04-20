package com.example.main.common;

import org.hashids.Hashids;

import java.util.Objects;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        int minHashLength = 7;
        this.hashids = new Hashids(salt, minHashLength);
    }

    public String getHashOf(Long id) {
        return hashids.encode(id);
    }

    public Long getIdOf(String hash) {
        long[] values = hashids.decode(hash);

        if (Objects.nonNull(values) && values.length > 0)
            return values[0];

        return null;
    }
}