package com.j4.krevetka.schema;

import com.owlike.genson.annotation.JsonProperty;

import java.util.Date;

public class Subscriber {
    @JsonProperty("chatId")
    private long chatId;
    @JsonProperty("nextExecutionTime")
    private Date nextExecutionTime;
    @JsonProperty("cronExpression")
    private String cronExpression;

    public Subscriber() {}

    public Subscriber(
            long chatId,
            Date nextExecutionTime,
            String group
    ) {
        this.chatId = chatId;
        this.nextExecutionTime = nextExecutionTime;
        this.cronExpression = group;
    }


    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String toString() {
        return "chatId: " + chatId + "\nnextExecutionTime: " + nextExecutionTime + "\ncronExpression: " + cronExpression;
    }
}
