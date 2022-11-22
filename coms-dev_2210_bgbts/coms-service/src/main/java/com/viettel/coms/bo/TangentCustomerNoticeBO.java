/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.coms.dto.OrdersCallbotConversationContentDTO;
import com.viettel.coms.dto.OrdersDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "TANGENT_CUSTOMER_CALLBOT")
public class TangentCustomerNoticeBO extends BaseFWModelImpl {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
        @org.hibernate.annotations.Parameter(name = "sequence", value = "TANGENT_CUSTOMER_CALLBOT_SEQ")})
    private Long id;
    @Column(name = "tangent_customer_id")
    private Long tangentCustomerId;
    @Column(name = "tangent_customer_status")
    private Long tangentCustomerStatus;
    @Column(name = "attempt_level")
    private Long attemptLevel;
    @Column(name = "call_at")
    private Long call_at;
    @Column(name = "call_price")
    private Long callPrice;
    @Column(name = "callcenter_phone")
    private String callCenterPhone;
    @Column(name = "conversation_id")
    private String conversationId;
    @Column(name = "conversation_type")
    private Long conversationType;
    @Column(name = "create_at")
    private Long createAt;
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "hangup_at")
    private Long hangupAt;
    @Column(name = "pickup_at")
    private Long pickupAt;
    @Column(name = "private_link")
    private String privateLink;
    @Column(name = "public_link")
    private String publicLink;
    @Column(name = "report")
    private String report;
    @Column(name = "ring_duration")
    private Long ringDuration;
    @Column(name = "scenario")
    private String scenario;
    @Column(name = "scenario_publication")
    private String scenarioPublication;
    @Column(name = "audio_url")
    private String audioUrl;

    @Override
    public BaseFWDTOImpl toDTO() {

        return null;
    }
}
