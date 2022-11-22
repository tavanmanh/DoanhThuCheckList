package com.viettel.coms.dto;

import com.viettel.coms.bo.WoScheduleConfigBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoScheduleConfigDTO extends ComsBaseFWDTO<WoScheduleConfigBO> {

    private Long scheduleConfigId;
    private String scheduleConfigCode;
    private String scheduleConfigName;
    private String userCreated;
    private Date createdDate;
    private int status;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date startTime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date endTime;
    private Long cycleLength;
    private Long state;
    private Long cycleType;
    private Long scheduleWorkItemId;
    private Long quotaTime;
    private String cdLevel1;
    private Long trId;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date woTime;
    private String cdLevel2;
    private String cdLevel1Name;
    private String cdLevel2Name;
    private String woTRCode;
    private String startTimeString;
    private String endTimeString;
    private Long woTypeId;
    private Long woNameId;
    private String startDateFrom;
    private String startDateTo;
    private String endDateFrom;
    private String endDateTo;
    private String statusS;
    
    
    public Long getScheduleConfigId() {
        return scheduleConfigId;
    }

    public void setScheduleConfigId(Long scheduleConfigId) {
        this.scheduleConfigId = scheduleConfigId;
    }

    public String getScheduleConfigCode() {
        return scheduleConfigCode;
    }

    public void setScheduleConfigCode(String scheduleConfigCode) {
        this.scheduleConfigCode = scheduleConfigCode;
    }

    public String getScheduleConfigName() {
        return scheduleConfigName;
    }

    public void setScheduleConfigName(String scheduleConfigName) {
        this.scheduleConfigName = scheduleConfigName;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(Long cycleLength) {
        this.cycleLength = cycleLength;
    }

    public Long getCycleType() {
        return cycleType;
    }

    public void setCycleType(Long cycleType) {
        this.cycleType = cycleType;
    }

    public Long getScheduleWorkItemId() {
        return scheduleWorkItemId;
    }

    public void setScheduleWorkItemId(Long scheduleWorkItemId) {
        this.scheduleWorkItemId = scheduleWorkItemId;
    }

    public Long getQuotaTime() {
        return quotaTime;
    }

    public void setQuotaTime(Long quotaTime) {
        this.quotaTime = quotaTime;
    }

    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    public Date getWoTime() {
        return woTime;
    }

    public void setWoTime(Date woTime) {
        this.woTime = woTime;
    }

    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    public String getWoTRCode() {
        return woTRCode;
    }

    public void setWoTRCode(String woTRCode) {
        this.woTRCode = woTRCode;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }
    
    
    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    public Long getWoNameId() {
        return woNameId;
    }

    public void setWoNameId(Long woNameId) {
        this.woNameId = woNameId;
    }

	public String getStartDateFrom() {
		return startDateFrom;
	}

	public void setStartDateFrom(String startDateFrom) {
		this.startDateFrom = startDateFrom;
	}

	public String getStartDateTo() {
		return startDateTo;
	}

	public void setStartDateTo(String startDateTo) {
		this.startDateTo = startDateTo;
	}

	public String getEndDateFrom() {
		return endDateFrom;
	}

	public void setEndDateFrom(String endDateFrom) {
		this.endDateFrom = endDateFrom;
	}

	public String getEndDateTo() {
		return endDateTo;
	}

	public void setEndDateTo(String endDateTo) {
		this.endDateTo = endDateTo;
	}

	public String getStatusS() {
		return statusS;
	}

	public void setStatusS(String statusS) {
		this.statusS = statusS;
	}

	@Override
    public WoScheduleConfigBO toModel() {
        WoScheduleConfigBO woScheduleConfigBO = new WoScheduleConfigBO();
        woScheduleConfigBO.setScheduleConfigId(this.scheduleConfigId);
        woScheduleConfigBO.setScheduleConfigCode(this.scheduleConfigCode);
        woScheduleConfigBO.setScheduleConfigName(this.scheduleConfigName);
        woScheduleConfigBO.setUserCreated(this.userCreated);
        woScheduleConfigBO.setCreatedDate(this.createdDate);
        woScheduleConfigBO.setStatus(this.status);
        woScheduleConfigBO.setStartTime(this.startTime);
        woScheduleConfigBO.setEndTime(this.endTime);
        woScheduleConfigBO.setCycleLength(this.cycleLength);
        woScheduleConfigBO.setState(this.state);
        woScheduleConfigBO.setCycleType(this.cycleType);
        woScheduleConfigBO.setScheduleWorkItemId(this.scheduleWorkItemId);
        woScheduleConfigBO.setQuotaTime(this.quotaTime);
        woScheduleConfigBO.setCdLevel1(this.cdLevel1);
        woScheduleConfigBO.setTrId(this.trId);
        woScheduleConfigBO.setWoTime(this.woTime);
        woScheduleConfigBO.setCdLevel2(this.cdLevel2);
        woScheduleConfigBO.setCdLevel1Name(this.cdLevel1Name);
        woScheduleConfigBO.setCdLevel2Name(this.cdLevel2Name);
        woScheduleConfigBO.setWoTRCode(this.woTRCode);
        woScheduleConfigBO.setWoTypeId(this.woTypeId);
        woScheduleConfigBO.setWoNameId(this.woNameId);
        return woScheduleConfigBO;
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
