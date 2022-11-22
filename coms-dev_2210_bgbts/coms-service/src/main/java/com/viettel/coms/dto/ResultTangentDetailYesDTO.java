package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ResultTangentDetailYesBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "RESULT_TANGENT_DETAIL_YESBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultTangentDetailYesDTO extends ComsBaseFWDTO<ResultTangentDetailYesBO> {

	private Long resultTangentDetailYesId;
	private Long resultTangentId;
	private String information1;
	private String information2;
	private String information3;
	private Double information41;
	private String information42;
	private String information43;
	private Double information51;
	private String information52;
	private String information53;
	private String information54;
	private String information55;
	private String information6;
	private String information7;
	private Double information81;
	private Double information82;
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private String createdDateDb;

	// DUONGHV13 ADD 07012022
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateOfBirth;
	private String jobCustomer;
	private String information1_1;

	private String information1_21;
	private String information1_22;
	private String information1_23;

	private String information1_3;

	private Long information3_1;
	private Long information3_2;
	private Long information3_3;
	private Long information3_4;
	private Long information3_5;
	private Long information3_6;
	private Long information3_7;
	private Long information3_8;
	private Long information3_9;

	private String contentBonus;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date scheduleBuild;
	
	private String provinceConstruction;
	private String districtConstruction;
	private String communeConstruction;
	private String detailAddressConstruction;
	
	private List<UtilAttachDocumentDTO> lstImageDesign;
	// DUONGHV13 ADD 07012022
	
	// DUONGHV13 ADD 14022022
	private DetailTangentCustomerDTO detailCustomer;
	// DUONGHV13 end 14022022
	private DetailTangentCustomerGPTHDTO detailCustomerGPTH;
	
	public String getCreatedDateDb() {
		return createdDateDb;
	}

	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}

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
	
	public List<UtilAttachDocumentDTO> getLstImageDesign() {
		return lstImageDesign;
	}

	public void setLstImageDesign(List<UtilAttachDocumentDTO> lstImageDesign) {
		this.lstImageDesign = lstImageDesign;
	}
	
	public DetailTangentCustomerDTO getDetailCustomer() {
		return detailCustomer;
	}

	public void setDetailCustomer(DetailTangentCustomerDTO detailCustomer) {
		this.detailCustomer = detailCustomer;
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

	public DetailTangentCustomerGPTHDTO getDetailCustomerGPTH() {
		return detailCustomerGPTH;
	}

	public void setDetailCustomerGPTH(DetailTangentCustomerGPTHDTO detailCustomerGPTH) {
		this.detailCustomerGPTH = detailCustomerGPTH;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return resultTangentDetailYesId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return resultTangentDetailYesId;
	}

	@Override
	public ResultTangentDetailYesBO toModel() {
		// TODO Auto-generated method stub
		ResultTangentDetailYesBO bo = new ResultTangentDetailYesBO();
		bo.setResultTangentDetailYesId(this.getResultTangentDetailYesId());
		bo.setResultTangentId(this.getResultTangentId());
		bo.setInformation1(this.getInformation1());
		bo.setInformation2(this.getInformation2());
		bo.setInformation3(this.getInformation3());
		bo.setInformation41(this.getInformation41());
		bo.setInformation42(this.getInformation42());
		bo.setInformation43(this.getInformation43());
		bo.setInformation51(this.getInformation51());
		bo.setInformation52(this.getInformation52());
		bo.setInformation53(this.getInformation53());
		bo.setInformation54(this.getInformation54());
		bo.setInformation55(this.getInformation55());
		bo.setInformation6(this.getInformation6());
		bo.setInformation7(this.getInformation7());
		bo.setInformation81(this.getInformation81());
		bo.setInformation82(this.getInformation82());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.getUpdatedDate());
		bo.setDateOfBirth(this.getDateOfBirth());
		bo.setJobCustomer(this.getJobCustomer());
		bo.setInformation1_1(this.getInformation1_1());
		bo.setInformation1_21(this.getInformation1_21());
		bo.setInformation1_22(this.getInformation1_22());
		bo.setInformation1_23(this.getInformation1_23());
		bo.setInformation1_3(this.getInformation1_3());
		bo.setInformation3_1(this.getInformation3_1());
		bo.setInformation3_2(this.getInformation3_2());
		bo.setInformation3_3(this.getInformation3_3());
		bo.setInformation3_4(this.getInformation3_4());
		bo.setInformation3_5(this.getInformation3_5());
		bo.setInformation3_6(this.getInformation3_6());
		bo.setInformation3_7(this.getInformation3_7());
		bo.setInformation3_8(this.getInformation3_8());
		bo.setInformation3_9(this.getInformation3_9());
		bo.setContentBonus(this.getContentBonus());
		bo.setScheduleBuild(this.getScheduleBuild());
		bo.setProvinceConstruction(this.getProvinceConstruction());
		bo.setDistrictConstruction(this.getDistrictConstruction());
		bo.setCommuneConstruction(this.getCommuneConstruction());
		bo.setDetailAddressConstruction(this.getDetailAddressConstruction());
		return bo;
	}

}
