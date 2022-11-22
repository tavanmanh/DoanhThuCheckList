/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "TANGENT_CUSTOMER_CALLBOT_CONTENT")
public class TangentCustomerCallbotContentBO extends BaseFWModelImpl {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "TANGENT_CUSTOMER_CALLBOT_CONTENT_SEQ")})
    private Long id;
    @Column(name = "tangent_customer_callbot_id")
    private Long tangentCustomerCallbotId;
    @Column(name = "sender")
    private String sender;
    @Column(name = "message")
    private String message;
    @Column(name = "start_time")
    private Double startTime;
    @Column(name = "end_time")
    private String endTime;

    @Override
    public BaseFWDTOImpl toDTO() {

        return null;
    }
}
