package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.EffectiveCalculateDasBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "EffectiveCalculateDasBO")
@JsonIgnoreProperties(ignoreUnknown = true)

public class EffectiveCalculateDasDTO extends ComsBaseFWDTO<EffectiveCalculateDasBO> {
  private java.lang.Long assumptionsId;
  private java.lang.String contentAssumptions;
  private java.lang.String unit;
  private java.lang.Double costAssumptions;
  private java.lang.String noteAssumptions;
  private java.lang.Long createdUserId;
  private java.lang.Long updatedUserId;
  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;
  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;
  private java.lang.String name;

  @Override
  public EffectiveCalculateDasBO toModel() {
    EffectiveCalculateDasBO effectiveCalculateDasBO = new EffectiveCalculateDasBO();
    effectiveCalculateDasBO.setAssumptionsId(this.assumptionsId);
    effectiveCalculateDasBO.setContentAssumptions(this.contentAssumptions);
    effectiveCalculateDasBO.setUnit(this.unit);
    effectiveCalculateDasBO.setCostAssumptions(this.costAssumptions);
    effectiveCalculateDasBO.setNoteAssumptions(this.noteAssumptions);
    effectiveCalculateDasBO.setCreatedUserId(this.createdUserId);
    effectiveCalculateDasBO.setUpdatedUserId(this.updatedUserId);
    effectiveCalculateDasBO.setCreatedDate(this.createdDate);
    effectiveCalculateDasBO.setUpdatedDate(this.updatedDate);
    return effectiveCalculateDasBO;
  }

  @JsonProperty("createdUserId")
  public java.lang.Long getCreatedUserId() {
    return createdUserId;
  }

  public void setCreatedUserId(java.lang.Long createdUserId) {
    this.createdUserId = createdUserId;
  }

  @Override
  public String catchName() {
    return assumptionsId.toString();
  }

  @Override
  public Long getFWModelId() {
    return assumptionsId;
  }


  @JsonProperty("assumptionsId")
  public java.lang.Long getAssumptionsId() {
    return assumptionsId;
  }

  public void setAssumptionsId(java.lang.Long assumptionsId) {
    this.assumptionsId = assumptionsId;
  }

  @JsonProperty("contentAssumptions")
  public java.lang.String getContentAssumptions() {
    return contentAssumptions;
  }

  public void setContentAssumptions(java.lang.String contentAssumptions) {
    this.contentAssumptions = contentAssumptions;
  }

  @JsonProperty("unit")
  public java.lang.String getUnit() {
    return unit;
  }

  public void setUnit(java.lang.String unit) {
    this.unit = unit;
  }

  @JsonProperty("costAssumptions")
  public java.lang.Double getCostAssumptions() {
    return costAssumptions != null ? costAssumptions : 0;
  }

  public void setCostAssumptions(java.lang.Double costAssumptions) {
    this.costAssumptions = costAssumptions;
  }

  @JsonProperty("noteAssumptions")
  public java.lang.String getNoteAssumptions() {
    return noteAssumptions;
  }

  public void setNoteAssumptions(java.lang.String noteAssumptions) {
    this.noteAssumptions = noteAssumptions;
  }

  @JsonProperty("updatedUserId")
  public java.lang.Long getUpdatedUserId() {
    return updatedUserId;
  }

  public void setUpdatedUserId(java.lang.Long updatedUserId) {
    this.updatedUserId = updatedUserId;
  }

  @JsonProperty("createdDate")
  public java.util.Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.util.Date createdDate) {
    this.createdDate = createdDate;
  }

  @JsonProperty("updatedDate")
  public java.util.Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(java.util.Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  @JsonProperty("name")
  public java.lang.String getName() {
    return name;
  }

  public void setName(java.lang.String name) {
    this.name = name;
  }


}
