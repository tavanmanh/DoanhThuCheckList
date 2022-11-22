package com.viettel.coms.bo;

import com.viettel.coms.dto.WoScheduleWorkItemDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_SCHEDULE_WORK_ITEM")
public class WoScheduleWorkItemBO extends BaseFWModelImpl {
    private Long woWorkItemId;
    private String workItemCode;
    private String workItemName;
    private String userCreated;
    private Date createdDate;
    private int status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_SCHEDULE_WORK_ITEM_SEQ")})
    @Column(name = "ID")
    public Long getWoWorkItemId() {
        return woWorkItemId;
    }

    public void setWoWorkItemId(Long woWorkItemId) {
        this.woWorkItemId = woWorkItemId;
    }

    @Column(name = "CODE")
    public String getWorkItemCode() {
        return workItemCode;
    }

    public void setWorkItemCode(String workItemCode) { this.workItemCode = workItemCode; }

    @Column(name = "NAME")
    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public WoScheduleWorkItemDTO toDTO() {
        WoScheduleWorkItemDTO woScheduleWorkItemDTO = new WoScheduleWorkItemDTO();
        woScheduleWorkItemDTO.setWoWorkItemId(this.woWorkItemId);
        woScheduleWorkItemDTO.setWorkItemCode(this.workItemCode);
        woScheduleWorkItemDTO.setWorkItemName(this.workItemName);
        woScheduleWorkItemDTO.setUserCreated(this.userCreated);
        woScheduleWorkItemDTO.setCreatedDate(this.createdDate);
        woScheduleWorkItemDTO.setStatus(this.status);
        return woScheduleWorkItemDTO;
    }

}
