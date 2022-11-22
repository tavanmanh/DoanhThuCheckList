package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ManageCertificateBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

//duonghv13-start 21092021
@XmlRootElement(name = "MANAGE_CERTIFICATEBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageCertificateDTO extends ComsBaseFWDTO<ManageCertificateBO> {

	private Long certificateId;
    private String certificateCode;
    private String certificateName;
    private Long careerId;
    private String careerCode;
    private String careerName;
    private Double practicePoint;
    private Double theoreticalPoint;
    
    private String unitCreated;
    private Long sysGroupId;
    private String sysGroupCode;
    private String sysGroupName;
    private Long sysUserId;
    private String loginName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String positionName;
    
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
   	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date startDate;
    private Long status;
    private Long certificateStatus;
    private Long approveStatus;
    
    private List<String> statusList;
    private List<String> approveList;
    
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
   	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private Long createdUserId;
	private Long updatedUserId;
	private String attachNewFileName;
	private String attachFilePath;
	private String loggedInUser;
	
	private String woIdList;
	
	private Long certificateExtendId;
	
	private UtilAttachDocumentDTO utilAttachDocumentDTO;
	
    @Override
    public ManageCertificateBO toModel() {
    	ManageCertificateBO manageCertificateBO = new ManageCertificateBO();
    	
    	manageCertificateBO.setCertificateId(this.certificateId);
    	manageCertificateBO.setCertificateCode(this.certificateCode);
    	manageCertificateBO.setCertificateName(this.certificateName);
    	manageCertificateBO.setCareerId(this.careerId);
    	manageCertificateBO.setPracticePoint(this.practicePoint);
    	manageCertificateBO.setTheoreticalPoint(this.theoreticalPoint);
    	manageCertificateBO.setUnitCreated(this.unitCreated);
    	manageCertificateBO.setSysUserId(this.sysUserId);;
    	manageCertificateBO.setEmail(this.email);
    	manageCertificateBO.setPhoneNumber(this.phoneNumber);
    	manageCertificateBO.setPositionName(this.positionName);
    	manageCertificateBO.setFinishDate(this.finishDate);
    	manageCertificateBO.setStartDate(this.startDate);
    	manageCertificateBO.setStatus(this.status);
    	manageCertificateBO.setApproveStatus(this.approveStatus);
    	manageCertificateBO.setCreatedDate(this.createdDate);
    	manageCertificateBO.setCreatedUserId(this.createdUserId);
    	manageCertificateBO.setUpdatedDate(this.updatedDate);
    	manageCertificateBO.setUpdatedUserId(this.updatedUserId);
        return manageCertificateBO;
    }
 
    @Override
    public String catchName() {
        return getCertificateId().toString();
    }



	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return certificateId;
	}



	public Long getCertificateId() {
		return certificateId;
	}



	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}



	public String getCertificateCode() {
		return certificateCode;
	}



	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}



	public String getCertificateName() {
		return certificateName;
	}



	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}



	public Long getCareerId() {
		return careerId;
	}



	public void setCareerId(Long careerId) {
		this.careerId = careerId;
	}



	public String getCareerCode() {
		return careerCode;
	}



	public void setCareerCode(String careerCode) {
		this.careerCode = careerCode;
	}

	public String getCareerName() {
		return careerName;
	}

	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}

	public Double getPracticePoint() {
		return practicePoint;
	}


	public void setPracticePoint(Double practicePoint) {
		this.practicePoint = practicePoint;
	}



	public Double getTheoreticalPoint() {
		return theoreticalPoint;
	}



	public void setTheoreticalPoint(Double theoreticalPoint) {
		this.theoreticalPoint = theoreticalPoint;
	}



	public String getUnitCreated() {
		return unitCreated;
	}



	public void setUnitCreated(String unitCreated) {
		this.unitCreated = unitCreated;
	}



	public Long getSysGroupId() {
		return sysGroupId;
	}



	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}



	public String getSysGroupCode() {
		return sysGroupCode;
	}



	public void setSysGroupCode(String sysGroupCode) {
		this.sysGroupCode = sysGroupCode;
	}



	public String getSysGroupName() {
		return sysGroupName;
	}



	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}



	public Long getSysUserId() {
		return sysUserId;
	}



	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}



	public String getLoginName() {
		return loginName;
	}



	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}



	public String getFullName() {
		return fullName;
	}



	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getPositionName() {
		return positionName;
	}



	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}



	public Date getFinishDate() {
		return finishDate;
	}



	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Long getStatus() {
		return status;
	}



	public void setStatus(Long status) {
		this.status = status;
	}



	public Long getApproveStatus() {
		return approveStatus;
	}



	public void setApproveStatus(Long approveStatus) {
		this.approveStatus = approveStatus;
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

	public Long getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(Long certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public String getAttachNewFileName() {
		return attachNewFileName;
	}

	public void setAttachNewFileName(String attachNewFileName) {
		this.attachNewFileName = attachNewFileName;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public UtilAttachDocumentDTO getUtilAttachDocumentDTO() {
		return utilAttachDocumentDTO;
	}

	public void setUtilAttachDocumentDTO(UtilAttachDocumentDTO utilAttachDocumentDTO) {
		this.utilAttachDocumentDTO = utilAttachDocumentDTO;
	}

	public String getAttachFilePath() {
		return attachFilePath;
	}

	public void setAttachFilePath(String attachFilePath) {
		this.attachFilePath = attachFilePath;
	}
	
	public List<String> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getWoIdList() {
		return woIdList;
	}

	public void setWoIdList(String woIdList) {
		this.woIdList = woIdList;
	}

	public Long getCertificateExtendId() {
		return certificateExtendId;
	}

	public void setCertificateExtendId(Long certificateExtendId) {
		this.certificateExtendId = certificateExtendId;
	}

	public List<String> getApproveList() {
		return approveList;
	}

	public void setApproveList(List<String> approveList) {
		this.approveList = approveList;
	}
	
	
	
	
//Duong -end
}
