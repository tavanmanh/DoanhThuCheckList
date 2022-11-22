package com.viettel.coms.bo;

import com.viettel.coms.dto.WoScheduleCheckListDTO;
import com.viettel.coms.dto.WoScheduleConfigDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_SCHEDULE_CONFIG")
public class WoScheduleConfigBO extends BaseFWModelImpl {
    private Long scheduleConfigId;
    private String scheduleConfigCode;
    private String scheduleConfigName;
    private String userCreated;
    private Date createdDate;
    private int status;
    private Date startTime;
    private Date endTime;
    private Long cycleLength;
    private Long state;
    private Long cycleType;
    private Long scheduleWorkItemId;
    private Long quotaTime;
    private String cdLevel1;
    private Long trId;
    private Date woTime;
    private String cdLevel2;
    private String cdLevel1Name;
    private String cdLevel2Name;
    private String woTRCode;
    private Long woTypeId;
    private Long woNameId;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_SCHEDULE_CONFIG_SEQ")})
    @Column(name = "ID")
    public Long getScheduleConfigId() {
        return scheduleConfigId;
    }

    public void setScheduleConfigId(Long scheduleConfigId) {
        this.scheduleConfigId = scheduleConfigId;
    }

    @Column(name = "CODE")
    public String getScheduleConfigCode() {
        return scheduleConfigCode;
    }

    public void setScheduleConfigCode(String scheduleConfigCode) {
        this.scheduleConfigCode = scheduleConfigCode;
    }

    @Column(name = "NAME")
    public String getScheduleConfigName() {
        return scheduleConfigName;
    }

    public void setScheduleConfigName(String scheduleConfigName) {
        this.scheduleConfigName = scheduleConfigName;
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

    @Column(name = "START_TIME")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "END_TIME")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "CYCLE_LENGTH")
    public Long getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(Long cycleLength) {
        this.cycleLength = cycleLength;
    }

    @Column(name = "CYCLE_TYPE")
    public Long getCycleType() {
        return cycleType;
    }

    public void setCycleType(Long cycleType) {
        this.cycleType = cycleType;
    }

    @Column(name = "WO_SCHEDULE_WORK_ITEM_ID")
    public Long getScheduleWorkItemId() {
        return scheduleWorkItemId;
    }

    public void setScheduleWorkItemId(Long scheduleWorkItemId) {
        this.scheduleWorkItemId = scheduleWorkItemId;
    }

    @Column(name = "QUOTA_TIME")
    public Long getQuotaTime() {
        return quotaTime;
    }

    public void setQuotaTime(Long quotaTime) {
        this.quotaTime = quotaTime;
    }

    @Column(name = "CD_LEVEL_1")
    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    @Column(name = "STATE")
    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    @Column(name = "WO_TR_ID")
    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    @Column(name = "WO_TIME")
    public Date getWoTime() {
        return woTime;
    }

    public void setWoTime(Date woTime) {
        this.woTime = woTime;
    }

    @Column(name = "CD_LEVEL_2")
    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    @Column(name = "CD_LEVEL_1_NAME")
    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    @Column(name = "CD_LEVEL_2_NAME")
    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    @Column(name = "WO_TR_CODE")
    public String getWoTRCode() {
        return woTRCode;
    }

    public void setWoTRCode(String woTRCode) {
        this.woTRCode = woTRCode;
    }

    @Column(name = "WO_TYPE_ID")
    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    @Column(name = "WO_NAME_ID")
    public Long getWoNameId() {
        return woNameId;
    }

    public void setWoNameId(Long woNameId) {
        this.woNameId = woNameId;
    }

    @Override
    public WoScheduleConfigDTO toDTO() {
        WoScheduleConfigDTO woScheduleConfigDTO = new WoScheduleConfigDTO();
        woScheduleConfigDTO.setScheduleConfigId(this.scheduleConfigId);
        woScheduleConfigDTO.setScheduleConfigCode(this.scheduleConfigCode);
        woScheduleConfigDTO.setScheduleConfigName(this.scheduleConfigName);
        woScheduleConfigDTO.setUserCreated(this.userCreated);
        woScheduleConfigDTO.setCreatedDate(this.createdDate);
        woScheduleConfigDTO.setStatus(this.status);
        woScheduleConfigDTO.setStartTime(this.startTime);
        woScheduleConfigDTO.setEndTime(this.endTime);
        woScheduleConfigDTO.setCycleLength(this.cycleLength);
        woScheduleConfigDTO.setState(this.state);
        woScheduleConfigDTO.setCycleType(this.cycleType);
        woScheduleConfigDTO.setScheduleWorkItemId(this.scheduleWorkItemId);
        woScheduleConfigDTO.setQuotaTime(this.quotaTime);
        woScheduleConfigDTO.setCdLevel1(this.cdLevel1);
        woScheduleConfigDTO.setTrId(this.trId);
        woScheduleConfigDTO.setWoTime(this.woTime);
        woScheduleConfigDTO.setCdLevel2(this.cdLevel2);
        woScheduleConfigDTO.setCdLevel1Name(this.cdLevel1Name);
        woScheduleConfigDTO.setCdLevel2Name(this.cdLevel2Name);
        woScheduleConfigDTO.setWoTRCode(this.woTRCode);
        woScheduleConfigDTO.setWoTypeId(this.woTypeId);
        woScheduleConfigDTO.setWoNameId(this.woNameId);
        return woScheduleConfigDTO;
    }
}
