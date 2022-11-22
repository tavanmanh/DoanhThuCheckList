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
@Table(name = "SEND_EMAIL")
public class SendEmailBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "SEND_EMAIL_SEQ") })
	@Column(name = "SEND_EMAIL_ID", length = 11)
	private Long sendEmailId;
	@Column(name = "SUBJECT", length = 2000)
	private String subject;
	@Column(name = "CONTENT", length = 4000)
	private String content;
	@Column(name = "STATUS", length = 2)
	private Long status;
	@Column(name = "RECEIVE_EMAIL", length = 100)
	private String receiveEmail;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 11)
	private Long createdUserId;

	public Long getSendEmailId() {
		return sendEmailId;
	}

	public void setSendEmailId(Long sendEmailId) {
		this.sendEmailId = sendEmailId;
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

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
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

	@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		return null;
	}
}
