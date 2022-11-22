package com.viettel.coms.bo;

import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ManageCareerBO")
@Table(name = "CAREER")
/**
 *
 * 
 */

//Duonghv13- start 16092021
 public class ManageCareerBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "MANAGE_CAREER_SEQ") })
	@Column(name = "CAREER_ID", length = 20 )
	private Long careerId;
	@Column(name = "CODE", length = 200 )
	private String code;
	@Column(name = "NAME", length = 200 )
	private String name;
	
	@Column(name = "WO_ID_LIST", length = 1000 )
	private String woIdList;
	
	@Column(name = "STATUS", length = 2 )
    private String status;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;

	@Column(name = "CREATED_USER", length = 200 )
	private Long createdUserId;
	@Column(name = "UPDATED_USER", length = 200 )
	private Long updatedUserId;

	
	public ManageCareerBO() {
			setColId("careerId");
			setColName("careerId");
			setUniqueColumn(new String[] { "careerId" });
	}

	public Long getCareerId() {
		return careerId;
	}



	public void setCareerId(Long careerId) {
		this.careerId = careerId;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getWoIdList() {
		return woIdList;
	}



	public void setWoIdList(String woIdList) {
		this.woIdList = woIdList;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	@Override
    public ManageCareerDTO toDTO() {
		ManageCareerDTO manageCareerDTO = new ManageCareerDTO(); 
		manageCareerDTO.setCareerId(this.careerId);
		manageCareerDTO.setCode(this.code);
		manageCareerDTO.setName(this.name);
		manageCareerDTO.setWoIdList(this.woIdList); 
		manageCareerDTO.setStatus(this.status);
		manageCareerDTO.setCreatedDate(this.createdDate);
		manageCareerDTO.setCreatedUserId(this.createdUserId);
		manageCareerDTO.setUpdatedDate(this.updatedDate);
		manageCareerDTO.setUpdatedUserId(this.updatedUserId);
        return manageCareerDTO;
    }
}
