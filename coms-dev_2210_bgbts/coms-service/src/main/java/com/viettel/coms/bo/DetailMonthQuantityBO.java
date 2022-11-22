package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.DetailMonthQuantityDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "DETAIL_MONTH_QUANTITY")
public class DetailMonthQuantityBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "DETAIL_MONTH_QUANTITY_SEQ")})
	@Column(name = "DETAIL_MONTH_QUANTITY_ID", length = 11)
	private Long detailMonthQuantityId;
	@Column(name = "DETAIL_MONTH_PLAN_ID", length = 11)
	private Long detailMonthPlanId;
	@Column(name = "CNT_CONTRACT_ID", length = 11)
	private Long cntContractId;
	@Column(name = "CNT_CONTRACT_CODE", length = 200)
	private String cntContractCode;
	@Column(name = "QUANTITY", length = 24)
	private Double quantity;
	@Column(name = "REVENUE", length = 24)
	private Double revenue;
	@Column(name = "OTHER_TARGET", length = 24)
	private String otherTarget;
	
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
	public BaseFWDTOImpl toDTO() {
		DetailMonthQuantityDTO dto = new DetailMonthQuantityDTO();
		dto.setDetailMonthQuantityId(this.getDetailMonthQuantityId());
		dto.setDetailMonthPlanId(this.getDetailMonthPlanId());
		dto.setCntContractId(this.getCntContractId());
		dto.setCntContractCode(this.getCntContractCode());
		dto.setQuantity(this.getQuantity());
		dto.setRevenue(this.getRevenue());
		dto.setOtherTarget(this.otherTarget);
		return dto;
	}

}
