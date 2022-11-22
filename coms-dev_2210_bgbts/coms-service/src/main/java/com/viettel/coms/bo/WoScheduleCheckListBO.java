package com.viettel.coms.bo;

import com.viettel.coms.dto.WoScheduleCheckListDTO;
import com.viettel.coms.dto.WoScheduleWorkItemDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_SCHEDULE_CHECKLIST")
public class WoScheduleCheckListBO extends BaseFWModelImpl {
    private Long scheduleCheckListId;
    private String scheduleCheckListCode;
    private String scheduleCheckListName;
    private String userCreated;
    private Date createdDate;
    private int status;
    private Long scheduleWorkItemId;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_SCHEDULE_CHECKLIST_SEQ")})
    @Column(name = "ID")
    public Long getScheduleCheckListId() {
        return scheduleCheckListId;
    }

    public void setScheduleCheckListId(Long scheduleCheckListId) {
        this.scheduleCheckListId = scheduleCheckListId;
    }

    @Column(name = "CODE")
    public String getScheduleCheckListCode() {
        return scheduleCheckListCode;
    }

    public void setScheduleCheckListCode(String scheduleCheckListCode) {
        this.scheduleCheckListCode = scheduleCheckListCode;
    }

    @Column(name = "NAME")
    public String getScheduleCheckListName() {
        return scheduleCheckListName;
    }

    public void setScheduleCheckListName(String scheduleCheckListName) {
        this.scheduleCheckListName = scheduleCheckListName;
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

    @Column(name = "SCHEDULE_WORK_ITEM_ID")
    public Long getScheduleWorkItemId() {
        return scheduleWorkItemId;
    }

    public void setScheduleWorkItemId(Long scheduleWorkItemId) {
        this.scheduleWorkItemId = scheduleWorkItemId;
    }

    @Override
    public WoScheduleCheckListDTO toDTO() {
        WoScheduleCheckListDTO woScheduleCheckListDTO = new WoScheduleCheckListDTO();
        woScheduleCheckListDTO.setScheduleCheckListId(this.scheduleCheckListId);
        woScheduleCheckListDTO.setScheduleCheckListCode(this.scheduleCheckListCode);
        woScheduleCheckListDTO.setScheduleCheckListName(this.scheduleCheckListName);
        woScheduleCheckListDTO.setUserCreated(this.userCreated);
        woScheduleCheckListDTO.setCreatedDate(this.createdDate);
        woScheduleCheckListDTO.setStatus(this.status);
        woScheduleCheckListDTO.setScheduleWorkItemId(this.scheduleWorkItemId);
        return woScheduleCheckListDTO;
    }
}
