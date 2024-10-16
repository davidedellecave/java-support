package ddc.support.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class Replacer {
    private Map<String, String> map = new TreeMap<>();

    public Replacer addField(String name, String value) {
        map.put(name, value);
        return this;
    }

    public String eval(String source) {
        for (Map.Entry<String, String> e : map.entrySet()) {
            //System.out.println("key:" + e.getKey() + " - " + "value:" + e.getValue());
            source = StringUtils.replace(source, e.getKey(), e.getValue());
        }
        map.clear();
        return source;
    }
}
