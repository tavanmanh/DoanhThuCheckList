package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CalculateEfficiencyHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "CALCULATE_EFFICIENCY_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateEfficiencyHtctDTO extends ComsBaseFWDTO<CalculateEfficiencyHtctBO> {

  private java.lang.Long calculateEfficiencyId;

  private java.lang.String contentCalEff;

  private java.lang.String unit;

  private java.lang.Double costCalEff;

  private java.lang.Double costNotSource;

  private java.lang.Double costSource;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;

  private java.lang.Long createdUserId;

  private java.lang.Long updateUserId;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;


  @Override
  public CalculateEfficiencyHtctBO toModel() {
    CalculateEfficiencyHtctBO calculateEfficiencyHtctBO = new CalculateEfficiencyHtctBO();
    calculateEfficiencyHtctBO.setCalculateEfficiencyId(this.calculateEfficiencyId);
    calculateEfficiencyHtctBO.setContentCalEff(this.contentCalEff);
    calculateEfficiencyHtctBO.setUnit(this.unit);
    calculateEfficiencyHtctBO.setCostCalEff(this.costCalEff);
    calculateEfficiencyHtctBO.setCostNotSource(this.costNotSource);
    calculateEfficiencyHtctBO.setCostSource(this.costSource);
    calculateEfficiencyHtctBO.setCreatedDate(this.createdDate);
    calculateEfficiencyHtctBO.setCreatedUserId(this.createdUserId);
    calculateEfficiencyHtctBO.setUpdateUserId(this.updateUserId);
    calculateEfficiencyHtctBO.setUpdatedDate(this.updatedDate);
    return calculateEfficiencyHtctBO;
  }

  public java.lang.Long getCalculateEfficiencyId() {
    return calculateEfficiencyId;
  }

  public void setCalculateEfficiencyId(java.lang.Long calculateEfficiencyId) {
    this.calculateEfficiencyId = calculateEfficiencyId;
  }

  public java.lang.String getContentCalEff() {
    return contentCalEff;
  }

  public void setContentCalEff(java.lang.String contentCalEff) {
    this.contentCalEff = contentCalEff;
  }

  //
  public java.lang.String getUnit() {
    return unit;
  }

  public void setUnit(java.lang.String unit) {
    this.unit = unit;
  }

  //
  public java.lang.Double getCostCalEff() {
    return costCalEff != null ? costCalEff : 0;
  }

  public void setCostCalEff(java.lang.Double costCalEff) {
    this.costCalEff = costCalEff;
  }

  //
  public java.lang.Double getCostNotSource() {
    return costNotSource != null ? costNotSource : 0;
  }

  public void setCostNotSource(java.lang.Double costNotSource) {
    this.costNotSource = costNotSource;
  }

  //
  public java.lang.Double getCostSource() {
    return costSource != null ? costSource : 0;
  }

  public void setCostSource(java.lang.Double costSource) {
    this.costSource = costSource;
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
    return getCalculateEfficiencyId().toString();
  }

  @Override
  public Long getFWModelId() {
    return calculateEfficiencyId;
  }

}
