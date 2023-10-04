package ddc.support.util;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class LogChron {
    private Logger logger;
    private LinkedMap<String, ChronTask> tasks = new LinkedMap<>();
    private int level = 0;

    public LogChron(Logger logger) {
        this.logger = logger;
    }

    public LogChron start(String taskName, String... messages) {
        level++;
        ChronTask t = new ChronTask()
                .setTaskName(taskName);
        tasks.put(taskName, t);
        logger.debug(startMsg(t, messages));
        return this;
    }

    public LogChron end(String... messages) {
        String lastKey = tasks.lastKey();
        ChronTask t = tasks.get(lastKey);
        logger.info(endMsg(t, messages));
        tasks.remove(lastKey);
        level--;
        return this;
    }

    public LogChron addInfo(String name, Object value) {
        ChronTask t = tasks.get(tasks.lastKey());
        t.getParams().put(name, value);
        return this;
    }



    private String startMsg(ChronTask t, String... messages) {
        return compose(t,true, messages);
    }

    private String endMsg(ChronTask t, String... messages) {
        return compose(t, false, messages);
    }

    private String compose(ChronTask t, boolean isStarting, String... messages) {
        StringBuilder b = new StringBuilder(StringUtils.repeat("-", level*3));
        b.append("> ");
        b.append(t.getTaskName());
        if (isStarting)
            b.append("...");
        else
            b.append(".");
        if (!isStarting && t.getChron() != null)
            b.append(" elapsed:[" + t.getChron().toString() + "]");
        if (messages != null && messages.length > 0)
            b.append(" - " + String.join(" - ", messages));
        t.getParams().entrySet().stream().forEach(x -> b.append(" " + x.getKey() + ":[" + x.getValue() + "]"));
        return b.toString();
    }

    class ChronTask {
        private String taskName = null;
        private Chronometer chron = new Chronometer();
        private LinkedMap<String, Object> params = new LinkedMap<>();

        public String getTaskName() {
            return taskName;
        }

        public ChronTask setTaskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Chronometer getChron() {
            return chron;
        }

        public ChronTask setChron(Chronometer chron) {
            this.chron = chron;
            return this;
        }

        public LinkedMap<String, Object> getParams() {
            return params;
        }

        public ChronTask setParams(LinkedMap<String, Object> params) {
            this.params = params;
            return this;
        }
    }
}
