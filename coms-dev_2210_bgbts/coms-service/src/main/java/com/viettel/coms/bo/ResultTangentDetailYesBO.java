package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ResultTangentDetailYesDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "RESULT_TANGENT_DETAIL_YES")
public class ResultTangentDetailYesBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RESULT_TANGENT_DETAIL_YES_SEQ") })
	@Column(name = "RESULT_TANGENT_DETAIL_YES_ID", length = 10)
	private Long resultTangentDetailYesId;
	@Column(name = "RESULT_TANGENT_ID", length = 10)
	private Long resultTangentId;
	@Column(name = "INFORMATION_1", length = 5)
	private String information1;
	@Column(name = "INFORMATION_2", length = 5)
	private String information2;
	@Column(name = "INFORMATION_3", length = 5)
	private String information3;
	@Column(name = "INFORMATION_41", length = 10)
	private Double information41;
	@Column(name = "INFORMATION_42", length = 1000)
	private String information42;
	@Column(name = "INFORMATION_43", length = 5)
	private String information43;
	@Column(name = "INFORMATION_51", length = 10)
	private Double information51;
	@Column(name = "INFORMATION_52", length = 100)
	private String information52;
	@Column(name = "INFORMATION_53", length = 5)
	private String information53;
	@Column(name = "INFORMATION_54", length = 5)
	private String information54;
	@Column(name = "INFORMATION_55", length = 5)
	private String information55;
	@Column(name = "INFORMATION_6", length = 5)
	private String information6;
	@Column(name = "INFORMATION_7", length = 1000)
	private String information7;
	@Column(name = "INFORMATION_81", length = 10)
	private Double information81;
	@Column(name = "INFORMATION_82", length = 10)
	private Double information82;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	
	//DUONGHV13 ADD 07012022
	@Column(name = "INFORMATION_1_1", length = 20)
	private String information1_1;
	@Column(name = "INFORMATION_1_21", length = 20)
	private String information1_21;
	@Column(name = "INFORMATION_1_22", length = 1000)
	private String information1_22;
	@Column(name = "INFORMATION_1_23", length = 20)
	private String information1_23;
	@Column(name = "INFORMATION_1_3", length = 45)
	private String information1_3;
	
	@Column(name = "INFORMATION_3_1", length = 10)
	private Long information3_1;
	@Column(name = "INFORMATION_3_2", length = 10)
	private Long information3_2;
	@Column(name = "INFORMATION_3_3", length = 10)
	private Long information3_3;
	@Column(name = "INFORMATION_3_4", length = 10)
	private Long information3_4;
	@Column(name = "INFORMATION_3_5", length = 10)
	private Long information3_5;
	@Column(name = "INFORMATION_3_6", length = 10)
	private Long information3_6;
	@Column(name = "INFORMATION_3_7", length = 10)
	private Long information3_7;
	@Column(name = "INFORMATION_3_8", length = 10)
	private Long information3_8;
	@Column(name = "INFORMATION_3_9", length = 10)
	private Long information3_9;
	
	@Column(name = "DATE_OF_BIRTH", length = 10)
	private Date dateOfBirth;
	@Column(name = "JOB_CUSTOMER", length = 45)
	private String jobCustomer;
	@Column(name = "CONTENT_BONUS", length = 45)
	private String contentBonus;
	@Column(name = "SCHEDULE_BUILD", length = 10)
	private Date scheduleBuild;
	
	@Column(name = "PROVINCE_CONSTRUCTION", length = 80)
	private String provinceConstruction;
	@Column(name = "DISTRICT_CONSTRUCTION", length = 80)
	private String districtConstruction;
	@Column(name = "COMMUNE_CONSTRUCTION", length = 80)
	private String communeConstruction;
	@Column(name = "DETAIL_ADDRESS_CONSTRUCTION", length = 200)
	private String detailAddressConstruction;
	//DUONGHV13 ADD 07012022
	
	public Long getResultTangentDetailYesId() {
		return resultTangentDetailYesId;
	}

	public void setResultTangentDetailYesId(Long resultTangentDetailYesId) {
		this.resultTangentDetailYesId = resultTangentDetailYesId;
	}

	public Long getResultTangentId() {
		return resultTangentId;
	}

	public void setResultTangentId(Long resultTangentId) {
		this.resultTangentId = resultTangentId;
	}

	public String getInformation1() {
		return information1;
	}

	public void setInformation1(String information1) {
		this.information1 = information1;
	}

	public String getInformation2() {
		return information2;
	}

	public void setInformation2(String information2) {
		this.information2 = information2;
	}

	public String getInformation3() {
		return information3;
	}

	public void setInformation3(String information3) {
		this.information3 = information3;
	}

	public Double getInformation41() {
		return information41;
	}

	public void setInformation41(Double information41) {
		this.information41 = information41;
	}

	public String getInformation42() {
		return information42;
	}

	public void setInformation42(String information42) {
		this.information42 = information42;
	}

	public String getInformation43() {
		return information43;
	}

	public void setInformation43(String information43) {
		this.information43 = information43;
	}

	public Double getInformation51() {
		return information51;
	}

	public void setInformation51(Double information51) {
		this.information51 = information51;
	}

	public String getInformation52() {
		return information52;
	}

	public void setInformation52(String information52) {
		this.information52 = information52;
	}

	public String getInformation53() {
		return information53;
	}

	public void setInformation53(String information53) {
		this.information53 = information53;
	}

	public String getInformation54() {
		return information54;
	}

	public void setInformation54(String information54) {
		this.information54 = information54;
	}

	public String getInformation55() {
		return information55;
	}

	public void setInformation55(String information55) {
		this.information55 = information55;
	}

	public String getInformation6() {
		return information6;
	}

	public void setInformation6(String information6) {
		this.information6 = information6;
	}

	public String getInformation7() {
		return information7;
	}

	public void setInformation7(String information7) {
		this.information7 = information7;
	}

	public Double getInformation81() {
		return information81;
	}

	public void setInformation81(Double information81) {
		this.information81 = information81;
	}

	public Double getInformation82() {
		return information82;
	}

	public void setInformation82(Double information82) {
		this.information82 = information82;
	}

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getInformation1_1() {
		return information1_1;
	}

	public void setInformation1_1(String information1_1) {
		this.information1_1 = information1_1;
	}

	public String getInformation1_21() {
		return information1_21;
	}

	public void setInformation1_21(String information1_21) {
		this.information1_21 = information1_21;
	}

	public String getInformation1_22() {
		return information1_22;
	}

	public void setInformation1_22(String information1_22) {
		this.information1_22 = information1_22;
	}

	public String getInformation1_23() {
		return information1_23;
	}

	public void setInformation1_23(String information1_23) {
		this.information1_23 = information1_23;
	}

	public String getInformation1_3() {
		return information1_3;
	}

	public void setInformation1_3(String information1_3) {
		this.information1_3 = information1_3;
	}

	public Long getInformation3_1() {
		return information3_1;
	}

	public void setInformation3_1(Long information3_1) {
		this.information3_1 = information3_1;
	}

	public Long getInformation3_2() {
		return information3_2;
	}

	public void setInformation3_2(Long information3_2) {
		this.information3_2 = information3_2;
	}

	public Long getInformation3_3() {
		return information3_3;
	}

	public void setInformation3_3(Long information3_3) {
		this.information3_3 = information3_3;
	}

	public Long getInformation3_4() {
		return information3_4;
	}

	public void setInformation3_4(Long information3_4) {
		this.information3_4 = information3_4;
	}

	public Long getInformation3_5() {
		return information3_5;
	}

	public void setInformation3_5(Long information3_5) {
		this.information3_5 = information3_5;
	}

	public Long getInformation3_6() {
		return information3_6;
	}

	public void setInformation3_6(Long information3_6) {
		this.information3_6 = information3_6;
	}

	public Long getInformation3_7() {
		return information3_7;
	}

	public void setInformation3_7(Long information3_7) {
		this.information3_7 = information3_7;
	}

	public Long getInformation3_8() {
		return information3_8;
	}

	public void setInformation3_8(Long information3_8) {
		this.information3_8 = information3_8;
	}

	public Long getInformation3_9() {
		return information3_9;
	}

	public void setInformation3_9(Long information3_9) {
		this.information3_9 = information3_9;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getJobCustomer() {
		return jobCustomer;
	}

	public void setJobCustomer(String jobCustomer) {
		this.jobCustomer = jobCustomer;
	}

	public String getContentBonus() {
		return contentBonus;
	}

	public void setContentBonus(String contentBonus) {
		this.contentBonus = contentBonus;
	}

	public Date getScheduleBuild() {
		return scheduleBuild;
	}

	public void setScheduleBuild(Date scheduleBuild) {
		this.scheduleBuild = scheduleBuild;
	}

	public String getProvinceConstruction() {
		return provinceConstruction;
	}

	public void setProvinceConstruction(String provinceConstruction) {
		this.provinceConstruction = provinceConstruction;
	}

	public String getDistrictConstruction() {
		return districtConstruction;
	}

	public void setDistrictConstruction(String districtConstruction) {
		this.districtConstruction = districtConstruction;
	}

	public String getCommuneConstruction() {
		return communeConstruction;
	}

	public void setCommuneConstruction(String communeConstruction) {
		this.communeConstruction = communeConstruction;
	}

	public String getDetailAddressConstruction() {
		return detailAddressConstruction;
	}

	public void setDetailAddressConstruction(String detailAddressConstruction) {
		this.detailAddressConstruction = detailAddressConstruction;
	}
	
	@Override
	public BaseFWDTOImpl toDTO() {
		ResultTangentDetailYesDTO dto = new ResultTangentDetailYesDTO();
		dto.setResultTangentDetailYesId(this.getResultTangentDetailYesId());
		dto.setResultTangentId(this.getResultTangentId());
		dto.setInformation1(this.getInformation1());
		dto.setInformation2(this.getInformation2());
		dto.setInformation3(this.getInformation3());
		dto.setInformation41(this.getInformation41());
		dto.setInformation42(this.getInformation42());
		dto.setInformation43(this.getInformation43());
		dto.setInformation51(this.getInformation51());
		dto.setInformation52(this.getInformation52());
		dto.setInformation53(this.getInformation53());
		dto.setInformation54(this.getInformation54());
		dto.setInformation55(this.getInformation55());
		dto.setInformation6(this.getInformation6());
		dto.setInformation7(this.getInformation7());
		dto.setInformation81(this.getInformation81());
		dto.setInformation82(this.getInformation82());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.getUpdatedDate());
		
		dto.setDateOfBirth(this.getDateOfBirth());
		dto.setJobCustomer(this.getJobCustomer());
		dto.setInformation1_1(this.getInformation1_1());
		dto.setInformation1_21(this.getInformation1_21());
		dto.setInformation1_22(this.getInformation1_22());
		dto.setInformation1_23(this.getInformation1_23());
		dto.setInformation1_3(this.getInformation1_3());
		dto.setInformation3_1(this.getInformation3_1());
		dto.setInformation3_2(this.getInformation3_2());
		dto.setInformation3_3(this.getInformation3_3());
		dto.setInformation3_4(this.getInformation3_4());
		dto.setInformation3_5(this.getInformation3_5());
		dto.setInformation3_6(this.getInformation3_6());
		dto.setInformation3_7(this.getInformation3_7());
		dto.setInformation3_8(this.getInformation3_8());
		dto.setInformation3_9(this.getInformation3_9());
		dto.setContentBonus(this.getContentBonus());
		dto.setScheduleBuild(this.getScheduleBuild());
		dto.setProvinceConstruction(this.getProvinceConstruction());
		dto.setDistrictConstruction(this.getDistrictConstruction());
		dto.setCommuneConstruction(this.getCommuneConstruction());
		dto.setDetailAddressConstruction(this.getDetailAddressConstruction());
		return dto;
	}

}
