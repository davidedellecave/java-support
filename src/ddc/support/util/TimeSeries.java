package ddc.support.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class TimeSeries<T> {
    //	private String measurement = null;
    private String metric = null;
    private LinkedHashMap<String, String> tags = new LinkedHashMap<>();
    //	private List<TimeValue<T>> values = new LinkedList<>();
    private List<TimeValue<T>> datapoints = new LinkedList<>();


    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public LinkedHashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(LinkedHashMap<String, String> tags) {
        this.tags = tags;
    }

    public List<TimeValue<T>> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(List<TimeValue<T>> datapoints) {
        this.datapoints = datapoints;
    }

    //	------------------------
    public TimeSeries<T> addTag(String name, String value) {
        tags.put(name, value);
        return this;
    }

    public TimeSeries<T> add(long timestamp, T value) {
        var v = new TimeValue<T>(timestamp, value);
        datapoints.add(v);
        return this;
    }
}
