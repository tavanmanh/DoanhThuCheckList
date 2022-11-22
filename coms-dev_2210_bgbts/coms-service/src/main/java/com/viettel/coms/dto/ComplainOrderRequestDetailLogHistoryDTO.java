package com.viettel.coms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.coms.bo.ComplainOrderRequestDetailLogHistoryBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "COMPLAIN_ORDER_REQUEST_DETAIL_LOG_HISTORY_BO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplainOrderRequestDetailLogHistoryDTO extends ComsBaseFWDTO<ComplainOrderRequestDetailLogHistoryBO>{

	private Long complainOrderRequestDetailId;
	
	private ComplainOrderRequestDTO complainOrderRequestDTO;
	private Long complainOrderRequestId;
	
	private String action;
	private String reason;
	private String note;
	private Long status;
	private Long times;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDate;
	private String createUser;
	private String createUserName;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date extendDate;
	
	private String createDateString;
	
	private String source;
	
	public Long getComplainOrderRequestDetailId() {
		return complainOrderRequestDetailId;
	}

	public void setComplainOrderRequestDetailId(Long complainOrderRequestDetailId) {
		this.complainOrderRequestDetailId = complainOrderRequestDetailId;
	}

	public Long getComplainOrderRequestId() {
		return complainOrderRequestId;
	}

	public void setComplainOrderRequestId(Long complainOrderRequestId) {
		this.complainOrderRequestId = complainOrderRequestId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}
	
	public ComplainOrderRequestDTO getComplainOrderRequestDTO() {
		return complainOrderRequestDTO;
	}

	public void setComplainOrderRequestDTO(ComplainOrderRequestDTO complainOrderRequestDTO) {
		this.complainOrderRequestDTO = complainOrderRequestDTO;
	}
	
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return complainOrderRequestDetailId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return complainOrderRequestDetailId;
	}
	
	@Override
	public ComplainOrderRequestDetailLogHistoryBO toModel() {
		// TODO Auto-generated method stub
		ComplainOrderRequestDetailLogHistoryBO bo = new ComplainOrderRequestDetailLogHistoryBO();
		bo.setComplainOrderRequestDetailId(this.complainOrderRequestDetailId);
		bo.setAction(this.action);
		bo.setStatus(this.status);
		bo.setReason(this.reason);
		bo.setNote(this.note);
		bo.setTimes(this.times);
		bo.setCreateDate(this.createDate);
		bo.setCreateUser(this.createUser);
		bo.setExtendDate(this.extendDate);
		bo.setComplainOrderRequestId(this.complainOrderRequestId);
		bo.setSource(this.source);
		return bo;
	}

}
