/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrdersCallbotConversationContentDTO {

    @JsonProperty("sender")
    private String sender;
    @JsonProperty("message")
    private String message;
    @JsonProperty("start_time")
    private Double startTime;
    @JsonProperty("end_time")
    private String endTime;

    public String getSender() {

        return sender;
    }

    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public Double getStartTime() {

        return startTime;
    }

    public void setStartTime(Double startTime) {

        this.startTime = startTime;
    }

    public String getEndTime() {

        return endTime;
    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;
    }

    public OrdersCallbotConversationContentDTO(String sender, String message, Double startTime, String endTime) {

        this.sender = sender;
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public OrdersCallbotConversationContentDTO () {

    }
}
