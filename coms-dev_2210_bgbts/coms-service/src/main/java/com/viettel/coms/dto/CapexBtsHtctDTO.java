package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CapexBtsHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "CAPEX_BTS_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CapexBtsHtctDTO extends ComsBaseFWDTO<CapexBtsHtctBO> {

  private java.lang.Long capexBtsId;

  private java.lang.String itemType;

  private java.lang.String item;

  private java.lang.String workCapex;

  private java.lang.Long provinceId;

  private java.lang.String provinceCode;

  private java.lang.Double costCapexBts;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;

  private java.lang.Long createdUserId;

  private java.lang.Long updateUserId;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;


  @Override
  public CapexBtsHtctBO toModel() {
    CapexBtsHtctBO capexBtsHtctBO = new CapexBtsHtctBO();
    capexBtsHtctBO.setCapexBtsId(this.capexBtsId);
    capexBtsHtctBO.setItemType(this.itemType);
    capexBtsHtctBO.setItem(this.item);
    capexBtsHtctBO.setWorkCapex(this.workCapex);
    capexBtsHtctBO.setProvinceId(this.provinceId);
    capexBtsHtctBO.setProvinceCode(this.provinceCode);
    capexBtsHtctBO.setCostCapexBts(this.costCapexBts);
    capexBtsHtctBO.setCreatedDate(this.createdDate);
    capexBtsHtctBO.setCreatedUserId(this.createdUserId);
    capexBtsHtctBO.setUpdateUserId(this.updateUserId);
    capexBtsHtctBO.setUpdatedDate(this.updatedDate);
    return capexBtsHtctBO;
  }

  public java.lang.Long getCapexBtsId() {
    return capexBtsId;
  }

  public void setCapexBtsId(java.lang.Long capexBtsId) {
    this.capexBtsId = capexBtsId;
  }

  public java.lang.String getItemType() {
    return itemType;
  }

  public void setItemType(java.lang.String itemType) {
    this.itemType = itemType;
  }

  //
  public java.lang.String getItem() {
    return item;
  }

  public void setItem(java.lang.String item) {
    this.item = item;
  }

  //
  public java.lang.String getWorkCapex() {
    return workCapex;
  }

  public void setWorkCapex(java.lang.String workCapex) {
    this.workCapex = workCapex;
  }

  //
  public java.lang.Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(java.lang.Long provinceId) {
    this.provinceId = provinceId;
  }

  public java.lang.String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(java.lang.String provinceCode) {
    this.provinceCode = provinceCode;
  }

  //
  public java.lang.Double getCostCapexBts() {
    return costCapexBts != null ? costCapexBts : 0;
  }

  public void setCostCapexBts(java.lang.Double costCapexBts) {
    this.costCapexBts = costCapexBts;
  }

  //
  public java.util.Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.util.Date createdDate) {
    this.createdDate = createdDate;
  }

  public java.lang.Long getCreatedUserId() {
    return createdUserId;
  }

  public void setCreatedUserId(java.lang.Long createdUserId) {
    this.createdUserId = createdUserId;
  }

  public java.lang.Long getUpdateUserId() {
    return updateUserId;
  }

  public void setUpdateUserId(java.lang.Long updateUserId) {
    this.updateUserId = updateUserId;
  }

  public java.util.Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(java.util.Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Override
  public String catchName() {
    return getCapexBtsId().toString();
  }

  @Override
  public Long getFWModelId() {
    return capexBtsId;
  }

}
