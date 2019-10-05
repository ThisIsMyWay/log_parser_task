package com.test.recordparser;

import java.util.Objects;

public class LogItem {

    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;

    public LogItem(String id, String state, String type, String host, Long timestamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
    }

    public LogItem(String id, String state, Long timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public Long getTimestamp() {
        return timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogItem)) return false;
        LogItem logItem = (LogItem) o;
        return Objects.equals(getId(), logItem.getId()) &&
                Objects.equals(getState(), logItem.getState()) &&
                Objects.equals(getType(), logItem.getType()) &&
                Objects.equals(getHost(), logItem.getHost()) &&
                Objects.equals(getTimestamp(), logItem.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getState(), getType(), getHost(), getTimestamp());
    }
}
