package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.GpmbHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "GPMB_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GpmbHtctDTO extends ComsBaseFWDTO<GpmbHtctBO> {

  private java.lang.Long gpmbId;

  private java.lang.Long provinceId;

  private java.lang.String provinceCode;

  private java.lang.Long amountBts;

  private java.lang.Double costGpmb;

  private java.lang.Double costNcdn;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;

  private java.lang.Long createdUserId;

  private java.lang.Long updateUserId;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;


  @Override
  public GpmbHtctBO toModel() {
    GpmbHtctBO gpmbHtctBO = new GpmbHtctBO();
    gpmbHtctBO.setGpmbId(this.gpmbId);
    gpmbHtctBO.setProvinceId(this.provinceId);
    gpmbHtctBO.setProvinceCode(this.provinceCode);
    gpmbHtctBO.setAmountBts(this.amountBts);
    gpmbHtctBO.setCostGpmb(this.costGpmb);
    gpmbHtctBO.setCostNcdn(this.costNcdn);
    gpmbHtctBO.setCreatedDate(this.createdDate);
    gpmbHtctBO.setCreatedUserId(this.createdUserId);
    gpmbHtctBO.setUpdateUserId(this.updateUserId);
    gpmbHtctBO.setUpdatedDate(this.updatedDate);
    return gpmbHtctBO;
  }

  public java.lang.Long getGpmbId() {
    return gpmbId;
  }

  public void setGpmbId(java.lang.Long gpmbId) {
    this.gpmbId = gpmbId;
  }

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
  public java.lang.Long getAmountBts() {
    return amountBts != null ? amountBts : 0;
  }

  public void setAmountBts(java.lang.Long amountBts) {
    this.amountBts = amountBts;
  }

  //
  public java.lang.Double getCostGpmb() {
    return costGpmb != null ? costGpmb : 0;
  }

  public void setCostGpmb(java.lang.Double costGpmb) {
    this.costGpmb = costGpmb;
  }

  //
  public java.lang.Double getCostNcdn() {
    return costNcdn != null ? costNcdn : 0;
  }

  public void setCostNcdn(java.lang.Double costNcdn) {
    this.costNcdn = costNcdn;
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

  //

  @Override
  public String catchName() {
    return getGpmbId().toString();
  }

  @Override
  public Long getFWModelId() {
    return gpmbId;
  }

}
