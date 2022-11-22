package com.viettel.coms.dto;

import java.util.List;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

/**
 *
 * @author hailh10
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "SYS_GROUPBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysGroupDTO extends ComsBaseFWDTO<BaseFWModelImpl> {

	private java.lang.Long sysGroupId;
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.Long parentId;
	private java.lang.String parentName;
	private java.lang.String status;
	private java.lang.String path;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date effectDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date effectDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date effectDateTo;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDateTo;

	private java.lang.String groupLevel;
	private List<String> groupLevelLst;
	private String groupNameLevel1;
    private String groupNameLevel2;
    private String groupNameLevel3;
    
    //hienlt56 start 04052020
    private java.lang.String areaCode;
    @JsonProperty("areaCode")
    public java.lang.String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}
	//hienlt56 end 04052020
    
	public String getGroupNameLevel1() {
		return groupNameLevel1;
	}

	public void setGroupNameLevel1(String groupNameLevel1) {
		this.groupNameLevel1 = groupNameLevel1;
	}

	public String getGroupNameLevel2() {
		return groupNameLevel2;
	}

	public void setGroupNameLevel2(String groupNameLevel2) {
		this.groupNameLevel2 = groupNameLevel2;
	}

	public String getGroupNameLevel3() {
		return groupNameLevel3;
	}

	public void setGroupNameLevel3(String groupNameLevel3) {
		this.groupNameLevel3 = groupNameLevel3;
	}

	@JsonProperty("sysGroupId")
    public java.lang.Long getSysGroupId(){
		return sysGroupId;
    }
	
    public void setSysGroupId(java.lang.Long sysGroupId){
		this.sysGroupId = sysGroupId;
    }	
	
	@JsonProperty("code")
    public java.lang.String getCode(){
		return code;
    }
	
    public void setCode(java.lang.String code){
		this.code = code;
    }	
	
	@JsonProperty("name")
    public java.lang.String getName(){
		return name;
    }
	
    public void setName(java.lang.String name){
		this.name = name;
    }	
	
	@JsonProperty("parentId")
    public java.lang.Long getParentId(){
		return parentId;
    }
	
    public void setParentId(java.lang.Long parentId){
		this.parentId = parentId;
    }	
	
	@JsonProperty("parentName")
    public java.lang.String getParentName(){
		return parentName;
    }
	
    public void setParentName(java.lang.String parentName){
		this.parentName = parentName;
    }	
	
	@JsonProperty("status")
    public java.lang.String getStatus(){
		return status;
    }
	
    public void setStatus(java.lang.String status){
		this.status = status;
    }	
	
	@JsonProperty("path")
    public java.lang.String getPath(){
		return path;
    }
	
    public void setPath(java.lang.String path){
		this.path = path;
    }	
	
	@JsonProperty("effectDate")
    public java.util.Date getEffectDate(){
		return effectDate;
    }
	
    public void setEffectDate(java.util.Date effectDate){
		this.effectDate = effectDate;
    }	
	
	public java.util.Date getEffectDateFrom() {
    	return effectDateFrom;
    }
	
    public void setEffectDateFrom(java.util.Date effectDateFrom) {
    	this.effectDateFrom = effectDateFrom;
    }
	
	public java.util.Date getEffectDateTo() {
    	return effectDateTo;
    }
	
    public void setEffectDateTo(java.util.Date effectDateTo) {
    	this.effectDateTo = effectDateTo;
    }
	
	@JsonProperty("endDate")
    public java.util.Date getEndDate(){
		return endDate;
    }
	
    public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
    }	
	
	public java.util.Date getEndDateFrom() {
    	return endDateFrom;
    }
	
    public void setEndDateFrom(java.util.Date endDateFrom) {
    	this.endDateFrom = endDateFrom;
    }
	
	public java.util.Date getEndDateTo() {
    	return endDateTo;
    }
	
    public void setEndDateTo(java.util.Date endDateTo) {
    	this.endDateTo = endDateTo;
    }
    
    @JsonProperty("groupLevel")
    public java.lang.String getGroupLevel(){
		return groupLevel;
    }
	
    public void setGroupLevel(java.lang.String groupLevel){
		this.groupLevel = groupLevel;
    }
    
    @JsonProperty("groupLevelLst")
	public List<String> getGroupLevelLst() {
		return groupLevelLst;
	}

	public void setGroupLevelLst(List<String> groupLevelLst) {
		this.groupLevelLst = groupLevelLst;
	}
	
//	private String areaCode;
//
//	public String getAreaCode() {
//		return areaCode;
//	}
//
//	public void setAreaCode(String areaCode) {
//		this.areaCode = areaCode;
//	}
	
	private String catProvinceCode;

	public String getCatProvinceCode() {
		return catProvinceCode;
	}

	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}
	
	private String employeeCode;
	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	//HienLT56 start 01032021
	private Long provinceId;
	private String provinceName;
	private String provinceCode;
	
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//HienLT56 end 01032021
	
}
