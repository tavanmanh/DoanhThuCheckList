package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CostVtnetHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "COST_VTNET_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostVtnetHtctDTO extends ComsBaseFWDTO<CostVtnetHtctBO> {

  private java.lang.Long costVtnetId;

  private java.lang.String stationType;

  private java.lang.Double notSourceHniHcm;

  private java.lang.Double notSource61Province;

  private java.lang.Double sourceHniHcm;

  private java.lang.Double source61Province;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date createdDate;

  private java.lang.Long createdUserId;

  private java.lang.Long updateUserId;

  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  @JsonSerialize(using = CustomJsonDateSerializer.class)
  private java.util.Date updatedDate;


  @Override
  public CostVtnetHtctBO toModel() {
    CostVtnetHtctBO costVtnetHtctBO = new CostVtnetHtctBO();
    costVtnetHtctBO.setCostVtnetId(this.costVtnetId);
    costVtnetHtctBO.setStationType(this.stationType);
    costVtnetHtctBO.setNotSourceHniHcm(this.notSourceHniHcm);
    costVtnetHtctBO.setNotSource61Province(this.notSource61Province);
    costVtnetHtctBO.setSourceHniHcm(this.sourceHniHcm);
    costVtnetHtctBO.setSource61Province(this.source61Province);
    costVtnetHtctBO.setCreatedDate(this.createdDate);
    costVtnetHtctBO.setCreatedUserId(this.createdUserId);
    costVtnetHtctBO.setUpdateUserId(this.updateUserId);
    costVtnetHtctBO.setUpdatedDate(this.updatedDate);
    return costVtnetHtctBO;
  }

  public java.lang.Long getCostVtnetId() {
    return costVtnetId;
  }

  public void setCostVtnetId(java.lang.Long costVtnetId) {
    this.costVtnetId = costVtnetId;
  }

  public java.lang.String getStationType() {
    return stationType;
  }

  public void setStationType(java.lang.String stationType) {
    this.stationType = stationType;
  }

  //
  public java.lang.Double getNotSourceHniHcm() {
    return notSourceHniHcm != null ? notSourceHniHcm : 0;
  }

  public void setNotSourceHniHcm(java.lang.Double notSourceHniHcm) {
    this.notSourceHniHcm = notSourceHniHcm;
  }

  //
  public java.lang.Double getNotSource61Province() {
    return notSource61Province != null ? notSource61Province : 0;
  }

  public void setNotSource61Province(java.lang.Double notSource61Province) {
    this.notSource61Province = notSource61Province;
  }

  //
  public java.lang.Double getSourceHniHcm() {
    return sourceHniHcm != null ? sourceHniHcm : 0;
  }

  public void setSourceHniHcm(java.lang.Double sourceHniHcm) {
    this.sourceHniHcm = sourceHniHcm;
  }

  //
  public java.lang.Double getSource61Province() {
    return source61Province != null ? source61Province : 0;
  }

  public void setSource61Province(java.lang.Double source61Province) {
    this.source61Province = source61Province;
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
    return getCostVtnetId().toString();
  }

  @Override
  public Long getFWModelId() {
    return costVtnetId;
  }

}
