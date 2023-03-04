package com.alibaba.fastjson2.issues_1000;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue1130 {
    @Test
    public void test() {
        String raw = "{\"arr1\":[\"a\"],\"numeric\":1,\"arr2\":[\"b\"]}";
        Object result = JSONPath.eval(raw, "$.arr2[0]");
        assertEquals("b", result);
    }

    @Test
    public void testArrayParseCase3() {
        //expected b, actually a
        String raw = "{\"arr1\":[\"a\"],\"numeric\":1,\"arr2\":[\"b\"]}";
        assertEquals("[\"b\"]", ((JSONArray) JSONPath.extract(raw, "$.arr2[*][0]")).toString());
        assertEquals("[\"b\"]", ((JSONArray) JSONPath.eval(raw, "$.arr2[*][0]")).toString());
    }

    @Test
    public void testArrayParseCase4() {
        //expected b, actually a
        String raw = "{\"arr1\":[\"a\"],\"numeric\":1,\"arr2\":[\"b\"]}";
        String[] paths = new String[]{"$.arr2[0]"};

        Type[] types = new Type[paths.length];
        Arrays.fill(types, String.class);
        JSONPath path = JSONPath.of(paths, types);
        Object[] results = (Object[]) path.extract(raw);
        assertEquals("b", results[0]);
    }

    @Test
    public void testArrayParseCase5() {
        String raw = "{\"arr1\":[\"a\"],\"numeric\":1,\"arr2\":[\"b\"]}";
        String[] paths = new String[]{"$.arr2[0]", "$.arr1[0]"};

        Type[] types = new Type[paths.length];
        Arrays.fill(types, String.class);
        JSONPath path = JSONPath.of(paths, types);
        Object[] results = (Object[]) path.extract(raw);
        assertEquals("b", results[0]);
        assertEquals("a", results[1]);
    }

    @Test
    public void testArrayParseCase6() {
        String raw = "{\"arr1\":[\"a\"],\"numeric\":1,\"arr2\":[\"a\", null]}";
        JSONPath path = JSONPath.of(new String[]{"$.arr2[1]", "$.arr2[1]"},
                new Type[]{String.class, Integer.class});
        Object eval = path.eval(JSONObject.parseObject(raw));

        Object[] results = (Object[]) path.extract(raw);
        assertArrayEquals(new Object[]{null, null}, results);
    }
}
