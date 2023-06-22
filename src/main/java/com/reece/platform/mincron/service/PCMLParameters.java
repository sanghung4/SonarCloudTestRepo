package com.reece.platform.mincron.service;

import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PCMLParameters implements Iterable<PCMLParameters.PCMLParameter> {
    @Value
    public class PCMLParameter {
        String name;
        String value;
    }

    private final List<PCMLParameter> params = new ArrayList<>();

    public PCMLParameters() {
    }

    public PCMLParameters(String... parameters) {
        if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException("Parameters must contain matching number of names and values");
        }

        for (int i = 0, cnt = parameters.length; (i + 1) < cnt; i += 2) {
            val name = parameters[i];
            val value = parameters[i + 1];
            params.add(new PCMLParameter(name, value));
        }
    }

    @Override
    public Iterator<PCMLParameter> iterator() {
        return params.iterator();
    }
}
