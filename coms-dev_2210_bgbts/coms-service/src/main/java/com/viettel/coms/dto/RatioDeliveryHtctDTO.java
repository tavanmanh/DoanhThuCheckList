package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.RatioDeliveryHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "RATIO_DELIVERY_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatioDeliveryHtctDTO extends ComsBaseFWDTO<RatioDeliveryHtctBO> {

  private java.lang.Long ratioDeliveryId;

  private java.lang.Long catProvinceId;

  private java.lang.String catProvinceCode;

  private java.lang.Double costDeliveryBts;

  private java.lang.Double costMountainsBts;

  private java.lang.Double costRoofBts;

  private java.lang.Double costDeliveryPru;

  private java.lang.Double costMountainsPru;

  private java.lang.Double costRoofPru;

  private java.lang.Double costDeliverySmallcell;

  private java.lang.Double costMountainsSmallcell;

  private java.lang.Double costRoofSmallcell;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;

  private java.lang.Long createdUserId;

  private java.lang.Long updateUserId;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;


  @Override
  public RatioDeliveryHtctBO toModel() {
    RatioDeliveryHtctBO ratioDeliveryHtctBO = new RatioDeliveryHtctBO();
    ratioDeliveryHtctBO.setRatioDeliveryId(this.ratioDeliveryId);
    ratioDeliveryHtctBO.setCatProvinceId(this.catProvinceId);
    ratioDeliveryHtctBO.setCatProvinceCode(this.catProvinceCode);
    ratioDeliveryHtctBO.setCostDeliveryBts(this.costDeliveryBts);
    ratioDeliveryHtctBO.setCostMountainsBts(this.costMountainsBts);
    ratioDeliveryHtctBO.setCostRoofBts(this.costRoofBts);
    ratioDeliveryHtctBO.setCostDeliveryPru(this.costDeliveryPru);
    ratioDeliveryHtctBO.setCostMountainsPru(this.costMountainsPru);
    ratioDeliveryHtctBO.setCostRoofPru(this.costRoofPru);
    ratioDeliveryHtctBO.setCostDeliverySmallcell(this.costDeliverySmallcell);
    ratioDeliveryHtctBO.setCostMountainsSmallcell(this.costMountainsSmallcell);
    ratioDeliveryHtctBO.setCostRoofSmallcell(this.costRoofSmallcell);
    ratioDeliveryHtctBO.setCreatedDate(this.createdDate);
    ratioDeliveryHtctBO.setCreatedUserId(this.createdUserId);
    ratioDeliveryHtctBO.setUpdateUserId(this.updateUserId);
    ratioDeliveryHtctBO.setUpdatedDate(this.updatedDate);
    return ratioDeliveryHtctBO;
  }

  public java.lang.Long getRatioDeliveryId() {
    return ratioDeliveryId;
  }

  public void setRatioDeliveryId(java.lang.Long ratioDeliveryId) {
    this.ratioDeliveryId = ratioDeliveryId;
  }

  public java.lang.Long getCatProvinceId() {
    return catProvinceId;
  }

  public void setCatProvinceId(java.lang.Long catProvinceId) {
    this.catProvinceId = catProvinceId;
  }

  public java.lang.String getCatProvinceCode() {
    return catProvinceCode;
  }

  public void setCatProvinceCode(java.lang.String catProvinceCode) {
    this.catProvinceCode = catProvinceCode;
  }

  //
  public java.lang.Double getCostDeliveryBts() {
    return costDeliveryBts != null ? costDeliveryBts : 0;
  }

  public void setCostDeliveryBts(java.lang.Double costDeliveryBts) {
    this.costDeliveryBts = costDeliveryBts;
  }

  //
  public java.lang.Double getCostMountainsBts() {
    return costMountainsBts != null ? costMountainsBts : 0;
  }

  public void setCostMountainsBts(java.lang.Double costMountainsBts) {
    this.costMountainsBts = costMountainsBts;
  }

  //
  public java.lang.Double getCostRoofBts() {
    return costRoofBts != null ? costRoofBts : 0;
  }

  public void setCostRoofBts(java.lang.Double costRoofBts) {
    this.costRoofBts = costRoofBts;
  }

  //
  public java.lang.Double getCostDeliveryPru() {
    return costDeliveryPru != null ? costDeliveryPru : 0;
  }

  public void setCostDeliveryPru(java.lang.Double costDeliveryPru) {
    this.costDeliveryPru = costDeliveryPru;
  }

  //
  public java.lang.Double getCostMountainsPru() {
    return costMountainsPru != null ? costMountainsPru : 0;
  }

  public void setCostMountainsPru(java.lang.Double costMountainsPru) {
    this.costMountainsPru = costMountainsPru;
  }

  //
  public java.lang.Double getCostRoofPru() {
    return costRoofPru != null ? costRoofPru : 0;
  }

  public void setCostRoofPru(java.lang.Double costRoofPru) {
    this.costRoofPru = costRoofPru;
  }

  //
  public java.lang.Double getCostDeliverySmallcell() {
    return costDeliverySmallcell != null ? costDeliverySmallcell : 0;
  }

  public void setCostDeliverySmallcell(java.lang.Double costDeliverySmallcell) {
    this.costDeliverySmallcell = costDeliverySmallcell;
  }

  //
  public java.lang.Double getCostMountainsSmallcell() {
    return costMountainsSmallcell != null ? costMountainsSmallcell : 0;
  }

  public void setCostMountainsSmallcell(java.lang.Double costMountainsSmallcell) {
    this.costMountainsSmallcell = costMountainsSmallcell;
  }

  //
  public java.lang.Double getCostRoofSmallcell() {
    return costRoofSmallcell != null ? costRoofSmallcell : 0;
  }

  public void setCostRoofSmallcell(java.lang.Double costRoofSmallcell) {
    this.costRoofSmallcell = costRoofSmallcell;
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
    return getRatioDeliveryId().toString();
  }

  @Override
  public Long getFWModelId() {
    return ratioDeliveryId;
  }

}
