/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dto;

import com.viettel.coms.bo.TangentCustomerCallbotContentBO;
import com.viettel.coms.bo.TangentCustomerNoticeBO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
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
public class TangentCustomerCallbotContentDTO extends  ComsBaseFWDTO<TangentCustomerCallbotContentBO> {

    private Long id;
    private Long tangentCustomerCallbotId;
    private String sender;
    private String message;
    private String startTime;
    private String endTime;

    @Override
    public Long getId() {

        return id;
    }

    @Override
    public void setId(Long id) {

        this.id = id;
    }

    public Long getTangentCustomerCallbotId() {

        return tangentCustomerCallbotId;
    }

    public void setTangentCustomerCallbotId(Long tangentCustomerCallbotId) {

        this.tangentCustomerCallbotId = tangentCustomerCallbotId;
    }

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

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String startTime) {

        this.startTime = startTime;
    }

    public String getEndTime() {

        return endTime;
    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;
    }

    @Override
    public TangentCustomerCallbotContentBO toModel() {

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
}
