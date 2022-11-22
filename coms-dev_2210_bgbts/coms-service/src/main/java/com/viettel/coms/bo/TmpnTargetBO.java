/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.bo;

import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "TMPN_TARGET")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class TmpnTargetBO extends BaseFWModelImpl {

    private java.lang.Long tmpnTargetId;
    private java.lang.Long totalMonthPlanId;
    private java.lang.Long sysGroupId;
    private java.lang.Long month;
    private java.lang.Long year;
    private java.lang.Long quantity;
    private java.lang.Long complete;
    private java.lang.Long revenue;
    
    private java.lang.Long hshcCp;
    private java.lang.Long hshcXl;
    private java.lang.Long hshcNtd;
    private java.lang.Long hshcXddd;
    private java.lang.Long hshcHtct;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "TMPN_TARGET_SEQ")})
    @Column(name = "TMPN_TARGET_ID", length = 22)
    public java.lang.Long getTmpnTargetId() {
        return tmpnTargetId;
    }

    public void setTmpnTargetId(java.lang.Long tmpnTargetId) {
        this.tmpnTargetId = tmpnTargetId;
    }
    
    
    @Column(name = "HSHC_CP", length = 22)
    public java.lang.Long getHshcCp() {
		return hshcCp;
	}

	public void setHshcCp(java.lang.Long hshcCp) {
		this.hshcCp = hshcCp;
	}
	 @Column(name = "HSHC_XL", length = 22)
	public java.lang.Long getHshcXl() {
		return hshcXl;
	}

	public void setHshcXl(java.lang.Long hshcXl) {
		this.hshcXl = hshcXl;
	}
	 @Column(name = "HSHC_NTD", length = 22)
	public java.lang.Long getHshcNtd() {
		return hshcNtd;
	}

	public void setHshcNtd(java.lang.Long hshcNtd) {
		this.hshcNtd = hshcNtd;
	}
	 @Column(name = "HSHC_XDDD", length = 22)
	public java.lang.Long getHshcXddd() {
		return hshcXddd;
	}

	public void setHshcXddd(java.lang.Long hshcXddd) {
		this.hshcXddd = hshcXddd;
	}
	 @Column(name = "HSHC_HTCT", length = 22)
	public java.lang.Long getHshcHtct() {
		return hshcHtct;
	}

	public void setHshcHtct(java.lang.Long hshcHtct) {
		this.hshcHtct = hshcHtct;
	}


	@Column(name = "TOTAL_MONTH_PLAN_ID", length = 22)
    public java.lang.Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(java.lang.Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    @Column(name = "SYS_GROUP_ID", length = 22)
    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "MONTH", length = 22)
    public java.lang.Long getMonth() {
        return month;
    }

    public void setMonth(java.lang.Long month) {
        this.month = month;
    }

    @Column(name = "YEAR", length = 22)
    public java.lang.Long getYear() {
        return year;
    }

    public void setYear(java.lang.Long year) {
        this.year = year;
    }

    @Column(name = "QUANTITY", length = 22)
    public java.lang.Long getQuantity() {
        return quantity;
    }

    public void setQuantity(java.lang.Long quantity) {
        this.quantity = quantity;
    }

    @Column(name = "COMPLETE", length = 22)
    public java.lang.Long getComplete() {
        return complete;
    }

    public void setComplete(java.lang.Long complete) {
        this.complete = complete;
    }

    @Column(name = "REVENUE", length = 22)
    public java.lang.Long getRevenue() {
        return revenue;
    }

    public void setRevenue(java.lang.Long revenue) {
        this.revenue = revenue;
    }

    private double mil = 1000000;

    @Override
    public TmpnTargetDTO toDTO() {
        TmpnTargetDTO tmpnTargetDTO = new TmpnTargetDTO();
        // set cac gia tri
        tmpnTargetDTO.setTmpnTargetId(this.tmpnTargetId);
        tmpnTargetDTO.setTotalMonthPlanId(this.totalMonthPlanId);
        tmpnTargetDTO.setSysGroupId(this.sysGroupId);
        tmpnTargetDTO.setMonth(this.month);
        tmpnTargetDTO.setYear(this.year);
        tmpnTargetDTO.setQuantity(this.quantity != null ? this.quantity / mil : this.quantity);
        tmpnTargetDTO.setComplete(this.complete != null ? this.complete / mil : this.complete);
        tmpnTargetDTO.setRevenue(this.revenue != null ? this.revenue / mil : this.revenue);
        
        tmpnTargetDTO.setHshcCp(this.hshcCp != null ? this.hshcCp / mil : this.hshcCp);
        tmpnTargetDTO.setHshcXl(this.hshcXl != null ? this.hshcXl / mil : this.hshcXl);
        tmpnTargetDTO.setHshcNtd(this.hshcNtd != null ? this.hshcNtd / mil : this.hshcNtd);
        tmpnTargetDTO.setHshcXddd(this.hshcXddd != null ? this.hshcXddd / mil : this.hshcXddd);
        tmpnTargetDTO.setHshcHtct(this.hshcHtct != null ? this.hshcHtct / mil : this.hshcHtct);
        return tmpnTargetDTO;
    }
}
