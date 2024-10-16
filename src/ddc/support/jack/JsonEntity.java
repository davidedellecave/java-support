package ddc.support.jack;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public class JsonEntity {
    private String id = UUID.randomUUID().toString();
    private long createTime = 0;
    private long updateTime = 0;
    private long deleteTime = 0;

    public JsonEntity() {
        createTime = System.currentTimeMillis();
        updateTime = createTime;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        try {
            return new JsonTools().toPrettifiedString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
