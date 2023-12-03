package com.github.alexandrenavarro.springboot3sample.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class StringUtilsTest {

    @Test
    void shouldReturnCorrectIsEmpty() {
        assertThat(StringUtils.isEmpty("")).isTrue();
        assertThat(StringUtils.isEmpty(null)).isTrue();
        assertThat(StringUtils.isEmpty("notEmpty")).isFalse();
    }

}
