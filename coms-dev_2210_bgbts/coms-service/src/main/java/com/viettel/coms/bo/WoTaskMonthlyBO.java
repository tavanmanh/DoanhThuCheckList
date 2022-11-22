package com.viettel.coms.bo;

import com.viettel.coms.dto.WoTaskMonthlyDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_TASK_MONTHLY")
public class WoTaskMonthlyBO extends BaseFWModelImpl {

    private Long woTaskMonthlyId;
    private Long sysGroupId;
    private Integer amount;
    private String type;
    private String confirm;
    private Date createdDate;
    private Long createdUserId;
    private Double quantity;
    private Long woMappingChecklistId;
    private Long confirmUserId;
    private Long woId;
    private Long status;
    private Date approveDate;
    private String approveBy;


    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_TASK_MONTHLY_SEQ")})
    @Column(name = "WO_TASK_MONTHLY_ID")
    public Long getWoTaskMonthlyId() {
        return woTaskMonthlyId;
    }

    public void setWoTaskMonthlyId(Long woTaskMonthlyId) {
        this.woTaskMonthlyId = woTaskMonthlyId;
    }

    @Column(name = "SYS_GROUP_ID")
    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "AMOUNT")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "CONFIRM")
    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "CREATED_USER_ID")
    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    @Column(name = "QUANTITY")
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "WO_MAPPING_CHECKLIST_ID")
    public Long getWoMappingChecklistId() {
        return woMappingChecklistId;
    }

    public void setWoMappingChecklistId(Long woMappingChecklistId) {
        this.woMappingChecklistId = woMappingChecklistId;
    }

    @Column(name = "CONFIRM_USER_ID")
    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "APPROVE_DATE")
    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    @Column(name = "APPROVE_BY")
    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    @Override
    public WoTaskMonthlyDTO toDTO() {
        WoTaskMonthlyDTO dto = new WoTaskMonthlyDTO();

        dto.setAmount(this.amount);
        dto.setApproveBy(this.approveBy);
        dto.setApproveDate(this.approveDate);
        dto.setConfirm(this.confirm);
        dto.setConfirmUserId(this.confirmUserId);
        dto.setCreatedDate(this.createdDate);
        dto.setCreatedUserId(this.createdUserId);
        dto.setQuantity(this.quantity);
        dto.setStatus(this.status);
        dto.setSysGroupId(this.sysGroupId);
        dto.setType(this.type);
        dto.setWoId(this.woId);
        dto.setWoMappingChecklistId(this.woMappingChecklistId);
        dto.setWoTaskMonthlyId(this.woTaskMonthlyId);

        return dto;
    }
}
