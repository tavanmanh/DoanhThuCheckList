/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.service.base.model.BaseFWModelImpl;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

public class OrdersCallbotConversationDTO {

    private Long orderId;
    private Long orderStatus;
    @JsonProperty("first_name")
    private Long attempLevel;
    @JsonProperty("callAt")
    private Long call_at;
    @JsonProperty("call_price")
    private Long callPrice;
    @JsonProperty("callcenter_phone")
    private String callCenterPhone;
    @JsonProperty("conversation_id")
    private String conversationId;
    @JsonProperty("conversation_Type")
    private Long conversationType;
    @JsonProperty("create_at")
    private Long createAt;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("hangup_at")
    private Long hangupAt;
    @JsonProperty("pickup_at")
    private String pickupAt;
    @JsonProperty("private_link")
    private String privateLink;
    @JsonProperty("public_link")
    private String publicLink;
    @JsonProperty("report")
    private String report;
    @JsonProperty("ring_duration")
    private Long ringDuration;
    @JsonProperty("scenario")
    private String scenario;
    @JsonProperty("scenario_publication")
    private String scenarioPublication;
    @JsonProperty("content")
    private List<OrdersCallbotConversationContentDTO> content;

    public Long getOrderId() {

        return orderId;
    }

    public void setOrderId(Long orderId) {

        this.orderId = orderId;
    }

    public Long getOrderStatus() {

        return orderStatus;
    }

    public void setOrderStatus(Long orderStatus) {

        this.orderStatus = orderStatus;
    }

    public Long getAttempLevel() {

        return attempLevel;
    }

    public void setAttempLevel(Long attempLevel) {

        this.attempLevel = attempLevel;
    }

    public Long getCall_at() {

        return call_at;
    }

    public void setCall_at(Long call_at) {

        this.call_at = call_at;
    }

    public Long getCallPrice() {

        return callPrice;
    }

    public void setCallPrice(Long callPrice) {

        this.callPrice = callPrice;
    }

    public String getCallCenterPhone() {

        return callCenterPhone;
    }

    public void setCallCenterPhone(String callCenterPhone) {

        this.callCenterPhone = callCenterPhone;
    }

    public String getConversationId() {

        return conversationId;
    }

    public void setConversationId(String conversationId) {

        this.conversationId = conversationId;
    }

    public Long getConversationType() {

        return conversationType;
    }

    public void setConversationType(Long conversationType) {

        this.conversationType = conversationType;
    }

    public Long getCreateAt() {

        return createAt;
    }

    public void setCreateAt(Long createAt) {

        this.createAt = createAt;
    }

    public String getCustomerPhone() {

        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {

        this.customerPhone = customerPhone;
    }

    public Long getHangupAt() {

        return hangupAt;
    }

    public void setHangupAt(Long hangupAt) {

        this.hangupAt = hangupAt;
    }

    public String getPickupAt() {

        return pickupAt;
    }

    public void setPickupAt(String pickupAt) {

        this.pickupAt = pickupAt;
    }

    public String getPrivateLink() {

        return privateLink;
    }

    public void setPrivateLink(String privateLink) {

        this.privateLink = privateLink;
    }

    public String getPublicLink() {

        return publicLink;
    }

    public void setPublicLink(String publicLink) {

        this.publicLink = publicLink;
    }

    public String getReport() {

        return report;
    }

    public void setReport(String report) {

        this.report = report;
    }

    public Long getRingDuration() {

        return ringDuration;
    }

    public void setRingDuration(Long ringDuration) {

        this.ringDuration = ringDuration;
    }

    public String getScenario() {

        return scenario;
    }

    public void setScenario(String scenario) {

        this.scenario = scenario;
    }

    public String getScenarioPublication() {

        return scenarioPublication;
    }

    public void setScenarioPublication(String scenarioPublication) {

        this.scenarioPublication = scenarioPublication;
    }

    public List<OrdersCallbotConversationContentDTO> getContent() {

        return content;
    }

    public void setContent(List<OrdersCallbotConversationContentDTO> content) {

        this.content = content;
    }

    public OrdersCallbotConversationDTO() {

    }
}
