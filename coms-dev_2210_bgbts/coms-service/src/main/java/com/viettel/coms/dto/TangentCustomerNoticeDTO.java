/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dto;

import com.viettel.coms.bo.TangentCustomerNoticeBO;
import com.viettel.coms.bo.TmpnFinanceBO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class TangentCustomerNoticeDTO extends ComsBaseFWDTO<TangentCustomerNoticeBO> {

    private Long tangentCustomerId;
    private Long tangentCustomerStatus;
    private Long attemptLevel;
    private Long callAt;
    private Long callPrice;
    private String callCenterPhone;
    private String conversationId;
    private Long conversationType;
    private Long createAt;
    private String customerPhone;
    private Long hangupAt;
    private Long pickupAt;
    private String privateLink;
    private String publicLink;
    private String report;
    private Long ringDuration;
    private String scenario;
    private String scenarioPublication;
    private String audioUrl;
    private List<TangentCustomerCallbotContentDTO> content;
    private Date createdDate;
    private Integer state;
    private String createdDateString;


    @Override
    public TangentCustomerNoticeBO toModel() {

        return null;
    }

    @Override
    public Long getFWModelId() {

        return null;
    }

    @Override
    public String catchName() {

        return null;
    }

    public Long getTangentCustomerId() {

        return tangentCustomerId;
    }

    public void setTangentCustomerId(Long tangentCustomerId) {

        this.tangentCustomerId = tangentCustomerId;
    }

    public Long getTangentCustomerStatus() {

        return tangentCustomerStatus;
    }

    public void setTangentCustomerStatus(Long tangentCustomerStatus) {

        this.tangentCustomerStatus = tangentCustomerStatus;
    }

    public Long getAttemptLevel() {

        return attemptLevel;
    }

    public void setAttemptLevel(Long attemptLevel) {

        this.attemptLevel = attemptLevel;
    }

    public Long getCallAt() {

        return callAt;
    }

    public void setCallAt(Long callAt) {

        this.callAt = callAt;
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

    public Long getPickupAt() {

        return pickupAt;
    }

    public void setPickupAt(Long pickupAt) {

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

    public String getAudioUrl() {

        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {

        this.audioUrl = audioUrl;
    }

    public List<TangentCustomerCallbotContentDTO> getContent() {

        return content;
    }

    public void setContent(List<TangentCustomerCallbotContentDTO> content) {

        this.content = content;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    public Integer getState() {

        return state;
    }

    public void setState(Integer state) {

        this.state = state;
    }

    public String getCreatedDateString() {

        return createdDateString;
    }

    public void setCreatedDateString(String createdDateString) {

        this.createdDateString = createdDateString;
    }
}
