package com.viettel.cat.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

//import com.viettel.Common.CommonDTO.wmsBaseDTO;

import com.viettel.cat.bo.CatProvinceBO;
import com.viettel.wms.dto.wmsBaseDTO;
//import com.viettel.erp.constant.ApplicationConstants;

/**
 * @author hailh10
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "CAT_PROVINCEBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatProvinceDTO extends wmsBaseDTO<CatProvinceBO> {

  private java.lang.Long catProvinceId;
  private java.lang.String code;
  private java.lang.String name;
  private java.lang.String status;
  private java.lang.String groupName;
  private int start;
  private int maxResult;

  @Override
  public CatProvinceBO toModel() {
    CatProvinceBO catProvinceBO = new CatProvinceBO();
    catProvinceBO.setCatProvinceId(this.catProvinceId);
    catProvinceBO.setCode(this.code);
    catProvinceBO.setName(this.name);
    catProvinceBO.setStatus(this.status);
    catProvinceBO.setGroupName(this.groupName);
    return catProvinceBO;
  }

    @Override
    public Long getFWModelId() {
        return catProvinceId;
    }

    @Override
    public String catchName() {
        return getCatProvinceId().toString();
    }

    @JsonProperty("catProvinceId")
    public java.lang.Long getCatProvinceId() {
        return catProvinceId;
    }

    public void setCatProvinceId(java.lang.Long catProvinceId) {
        this.catProvinceId = catProvinceId;
    }

    @JsonProperty("code")
    public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @JsonProperty("status")
    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    @JsonProperty("start")
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @JsonProperty("maxResult")
    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

  @JsonProperty("groupName")
  public java.lang.String getGroupName() {
    return groupName;
  }

  public void setGroupName(java.lang.String groupName) {
    this.groupName = groupName;
  }

  private String areaCode;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	private Long areaId;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	//Huypq-04082020-start
	private Long districtId;
	private String districtCode;
	private String districtName;

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	//Huy-end
	
	private List<String> lstProvince;

	public List<String> getLstProvince() {
		return lstProvince;
	}

	public void setLstProvince(List<String> lstProvince) {
		this.lstProvince = lstProvince;
	}
	
    
}
