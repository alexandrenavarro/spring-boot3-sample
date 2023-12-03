package com.github.alexandrenavarro.springboot3sample.utils;

import org.jspecify.annotations.Nullable;

public final class StringUtils {

    public static boolean isEmpty(@Nullable final String s) {
        return s == null || s.isEmpty();
    }

}
