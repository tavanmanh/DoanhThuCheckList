package com.viettel.coms.dto;

import com.viettel.coms.bo.WoPlanBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@XmlRootElement(name = "WOPLANBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoPlanDTO extends ComsBaseFWDTO<WoPlanBO> {
    private Long id;
    private String code;
    private String name;
    private Long ftId;
    private String planType;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date fromDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date toDate;
    private Long status;

    private Long numWoOfPlan;
    private Long woOk;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    private String woOkNumber;

    public String getWoOkNumber() {
        return woOkNumber;
    }

    public void setWoOkNumber(String woOkNumber) {
        this.woOkNumber = woOkNumber;
    }

    public Long getNumWoOfPlan() {
        return numWoOfPlan;
    }

    public void setNumWoOfPlan(Long numWoOfPlan) {
        this.numWoOfPlan = numWoOfPlan;
    }

    public Long getWoOk() {
        return woOk;
    }

    public void setWoOk(Long woOk) {
        this.woOk = woOk;
    }

    @Override
    public WoPlanBO toModel() {
        WoPlanBO woPlanBO = new WoPlanBO();
        woPlanBO.setId(this.id);
        woPlanBO.setCode(this.code);
        woPlanBO.setName(this.name);
        woPlanBO.setFtId(this.ftId);
        woPlanBO.setPlanType(this.planType);
        woPlanBO.setCreatedDate(this.createdDate);
        woPlanBO.setFromDate(this.fromDate);
        woPlanBO.setToDate(this.toDate);
        woPlanBO.setStatus(this.status);
        return woPlanBO;
    }

    @Override
    public String catchName() {
        return getId().toString();
    }

    @Override
    public Long getFWModelId() {
        return id;
    }

}
