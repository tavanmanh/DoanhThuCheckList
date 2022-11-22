package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "COMPLAIN_ORDER_REQUEST_DETAIL_LOG_HISTORY")
public class ComplainOrderRequestDetailLogHistoryBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "COMPLAIN_ORDER_REQUEST_DETAIL_LOG_HISTORY_SEQ") })
	
	@Column(name = "COMPLAIN_ORDER_REQUEST_DETAIL_ID", length = 10)
	private Long complainOrderRequestDetailId;
	
	@Column(name = "COMPLAIN_ORDER_REQUEST_ID", length = 10)
	private Long complainOrderRequestId;
	
	@Column(name = "ACTION", length = 100)
	private String action;
	@Column(name = "REASON", length = 255)
	private String reason;
	
	@Column(name = "NOTE", length = 255)
	private String note;
	
	@Column(name = "STATUS", length = 1)
	private Long status;
	
	@Column(name = "TIMES", length = 1)
	private Long times;
	
	@Column(name = "CREATE_USER", length = 255)
	private String createUser;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	
	@Column(name = "EXTEND_DATE", length = 255)
	private Date extendDate;
	
	@Column(name = "SOURCE", length = 100)
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		ComplainOrderRequestDetailLogHistoryDTO dto = new ComplainOrderRequestDetailLogHistoryDTO();
		dto.setComplainOrderRequestDetailId(this.complainOrderRequestDetailId);
		dto.setAction(this.action);
		dto.setStatus(this.status);
		dto.setReason(this.reason);
		dto.setNote(this.note);
		dto.setTimes(this.times);
		dto.setCreateDate(this.createDate);
		dto.setCreateUser(this.createUser);
		dto.setExtendDate(this.extendDate);
		dto.setComplainOrderRequestId(this.complainOrderRequestId);
		dto.setSource(this.source);
		return dto;
	}
}
