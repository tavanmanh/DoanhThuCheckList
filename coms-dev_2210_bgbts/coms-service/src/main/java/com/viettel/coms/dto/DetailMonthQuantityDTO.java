package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.DetailMonthQuantityBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "DETAIL_MONTH_QUANTITYBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailMonthQuantityDTO extends ComsBaseFWDTO<DetailMonthQuantityBO>{

	private Long detailMonthQuantityId;
	private Long detailMonthPlanId;
	private Long cntContractId;
	private String cntContractCode;
	private Double quantity;
	private Double revenue;
	private String otherTarget;
	private String supervisorId;
    private Long performerId;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date endDate;
    private String errorFilePath;
    private String supervisorName;
    private String performerName;
    
    private Double quantityTarget;
	private Double revenueTarget;

    public Double getQuantityTarget() {
		return quantityTarget;
	}

	public void setQuantityTarget(Double quantityTarget) {
		this.quantityTarget = quantityTarget;
	}

	public Double getRevenueTarget() {
		return revenueTarget;
	}

	public void setRevenueTarget(Double revenueTarget) {
		this.revenueTarget = revenueTarget;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }
    
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}
	public Long getPerformerId() {
		return performerId;
	}
	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}
	public String getOtherTarget() {
		return otherTarget;
	}
	public void setOtherTarget(String otherTarget) {
		this.otherTarget = otherTarget;
	}
	public Long getDetailMonthQuantityId() {
		return detailMonthQuantityId;
	}
	public void setDetailMonthQuantityId(Long detailMonthQuantityId) {
		this.detailMonthQuantityId = detailMonthQuantityId;
	}
	public Long getDetailMonthPlanId() {
		return detailMonthPlanId;
	}
	public void setDetailMonthPlanId(Long detailMonthPlanId) {
		this.detailMonthPlanId = detailMonthPlanId;
	}
	public Long getCntContractId() {
		return cntContractId;
	}
	public void setCntContractId(Long cntContractId) {
		this.cntContractId = cntContractId;
	}
	public String getCntContractCode() {
		return cntContractCode;
	}
	public void setCntContractCode(String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return detailMonthQuantityId.toString();
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return detailMonthQuantityId;
	}
	@Override
	public DetailMonthQuantityBO toModel() {
		DetailMonthQuantityBO bo = new DetailMonthQuantityBO();
		bo.setDetailMonthQuantityId(this.getDetailMonthQuantityId());
		bo.setDetailMonthPlanId(this.getDetailMonthPlanId());
		bo.setCntContractId(this.getCntContractId());
		bo.setCntContractCode(this.getCntContractCode());
		bo.setQuantity(this.getQuantity());
		bo.setRevenue(this.getRevenue());
		bo.setOtherTarget(this.otherTarget);
		return bo;
	}
}
