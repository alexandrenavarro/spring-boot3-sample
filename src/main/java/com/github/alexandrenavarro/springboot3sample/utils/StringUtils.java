package com.github.alexandrenavarro.springboot3sample.utils;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

@UtilityClass
public final class StringUtils {

    public static boolean isEmpty(@Nullable final String s) {
        return s == null || s.isEmpty();
    }

}
