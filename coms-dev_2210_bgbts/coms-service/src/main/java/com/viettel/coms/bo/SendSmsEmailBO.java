package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "CTCT_CAT_OWNER.SEND_SMS_EMAIL")
public class SendSmsEmailBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "SEND_SMS_EMAIL_SEQ") })
	@Column(name = "SEND_SMS_EMAIL_ID", length = 38)
	private Long sendSmsEmailId;
	@Column(name = "SUBJECT", length = 10)
	private String subject;
	@Column(name = "CONTENT", length = 20)
	private String content;
	@Column(name = "TYPE", length = 2)
	private String type;
	@Column(name = "STATUS", length = 2)
	private String status;
	@Column(name = "RESEND", length = 2)
	private String resend;
	@Column(name = "REASON", length = 10)
	private String reason;
	@Column(name = "RECEIVE_PHONE_NUMBER", length = 10)
	private String receivePhoneNumber;
	@Column(name = "RECEIVE_EMAIL", length = 10)
	private String receiveEmail;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 11)
	private Long createdUserId;
	@Column(name = "CREATED_GROUP_ID", length = 11)
	private Long createdGroupId;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "UPDATED_USER_ID", length = 11)
	private Long updatedUserId;
	@Column(name = "UPDATED_GROUP_ID", length = 11)
	private Long updatedGroupId;
	@Column(name = "WORK_ITEM_ID", length = 11)
	private Long workItemId;

	public Long getSendSmsEmailId() {
		return sendSmsEmailId;
	}

	public void setSendSmsEmailId(Long sendSmsEmailId) {
		this.sendSmsEmailId = sendSmsEmailId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResend() {
		return resend;
	}

	public void setResend(String resend) {
		this.resend = resend;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReceivePhoneNumber() {
		return receivePhoneNumber;
	}

	public void setReceivePhoneNumber(String receivePhoneNumber) {
		this.receivePhoneNumber = receivePhoneNumber;
	}

	public String getReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
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

	public Long getCreatedGroupId() {
		return createdGroupId;
	}

	public void setCreatedGroupId(Long createdGroupId) {
		this.createdGroupId = createdGroupId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public Long getUpdatedGroupId() {
		return updatedGroupId;
	}

	public void setUpdatedGroupId(Long updatedGroupId) {
		this.updatedGroupId = updatedGroupId;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		return null;
	}

}
