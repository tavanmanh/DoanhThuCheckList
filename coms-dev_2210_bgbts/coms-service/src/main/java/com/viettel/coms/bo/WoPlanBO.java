package com.viettel.coms.bo;

import com.viettel.coms.dto.WoPlanDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_PLAN")
public class WoPlanBO extends BaseFWModelImpl {
    private Long id;
    private String code;
    private String name;
    private Long ftId;
    private String planType;
    private Date createdDate;
    private Date fromDate;
    private Date toDate;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_PLAN_SEQ")})
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FT_ID", length = 11)
    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    @Column(name = "PLAN_TYPE")
    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "FROM_DATE")
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name = "TO_DATE")

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Column(name = "STATUS", length = 1)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }


    @Override
    public WoPlanDTO toDTO() {
        WoPlanDTO wOPlanDTO = new WoPlanDTO();
        wOPlanDTO.setId(this.id);
        wOPlanDTO.setCode(this.code);
        wOPlanDTO.setName(this.name);
        wOPlanDTO.setFtId(this.ftId);
        wOPlanDTO.setPlanType(this.planType);
        wOPlanDTO.setCreatedDate(this.createdDate);
        wOPlanDTO.setFromDate(this.fromDate);
        wOPlanDTO.setToDate(this.toDate);
        wOPlanDTO.setStatus(this.status);
        return wOPlanDTO;
    }
}
