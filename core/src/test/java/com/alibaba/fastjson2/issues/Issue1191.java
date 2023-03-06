package com.alibaba.fastjson2.issues;

import com.alibaba.fastjson2.JSONPath;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue1191 {
    @Test
    public void test() {
        String json = "[0, 1, 2, 3]";
        JSONPath path = JSONPath.of(new String[]{"$[3]", "$[4]"}, new Type[]{Long.class, Long.class});
        Object[] result = (Object[]) path.extract(json);
        assertArrayEquals(new Long[]{3L, null}, result);
    }

    @Test
    public void test1() {
        String json = "[1,2,3]";
        JSONPath path = JSONPath.of("$[6]", Integer.class);
        Object result = path.extract(json);
        assertNull(result);
    }

    @Test
    public void test2() {
        String json = "[]";
        JSONPath path = JSONPath.of("$[6]", Integer.class);
        Object result = path.extract(json);
        assertNull(result);
    }
}
