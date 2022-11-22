package com.viettel.coms.dto;

import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class WoTaskDailyDTO extends ComsBaseFWDTO<WoTaskDailyBO>  {
    private Long id;
    private Long sysGroupId;
    private Double amount; //số mét cáp
    private String type;
    private String confirm;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    private Long createdUserId;
    private Double quantity;
    private Long woMappingChecklistId;
    private Long confirmUserId;
    private Long woId;
    private Long status;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date approveDate;
    private String approveBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getWoMappingChecklistId() {
        return woMappingChecklistId;
    }

    public void setWoMappingChecklistId(Long woMappingChecklistId) {
        this.woMappingChecklistId = woMappingChecklistId;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    @Override
    public WoTaskDailyBO toModel() {
        WoTaskDailyBO bo = new WoTaskDailyBO();

        bo.setAmount(this.amount);
        bo.setApproveBy(this.approveBy);
        bo.setApproveDate(this.approveDate);
        bo.setConfirm(this.confirm);
        bo.setConfirmUserId(this.confirmUserId);
        bo.setCreatedDate(this.createdDate);
        bo.setCreatedUserId(this.createdUserId);
        bo.setQuantity(this.quantity);
        bo.setStatus(this.status);
        bo.setSysGroupId(this.sysGroupId);
        bo.setType(this.type);
        bo.setWoId(this.woId);
        bo.setWoMappingChecklistId(this.woMappingChecklistId);
        bo.setId(this.id);

        return bo;
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
