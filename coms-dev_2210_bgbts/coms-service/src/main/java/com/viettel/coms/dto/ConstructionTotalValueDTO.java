package com.viettel.coms.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstructionTotalValueDTO {

	private Long constructionTotalValueId;
    private Long constructionId;
    private Double revenue;
    private Double quantity;
    private Double sourceWork;
    private Double sourceRevenue;
    private Double quantityNoRevenue;
    private Double sourceWorkNoConstruct;
    
	public Long getConstructionTotalValueId() {
		return constructionTotalValueId;
	}
	public void setConstructionTotalValueId(Long constructionTotalValueId) {
		this.constructionTotalValueId = constructionTotalValueId;
	}
	public Long getConstructionId() {
		return constructionId;
	}
	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getSourceWork() {
		return sourceWork;
	}
	public void setSourceWork(Double sourceWork) {
		this.sourceWork = sourceWork;
	}
	public Double getSourceRevenue() {
		return sourceRevenue;
	}
	public void setSourceRevenue(Double sourceRevenue) {
		this.sourceRevenue = sourceRevenue;
	}
	public Double getQuantityNoRevenue() {
		return quantityNoRevenue;
	}
	public void setQuantityNoRevenue(Double quantityNoRevenue) {
		this.quantityNoRevenue = quantityNoRevenue;
	}
	public Double getSourceWorkNoConstruct() {
		return sourceWorkNoConstruct;
	}
	public void setSourceWorkNoConstruct(Double sourceWorkNoConstruct) {
		this.sourceWorkNoConstruct = sourceWorkNoConstruct;
	}
    
    
}
