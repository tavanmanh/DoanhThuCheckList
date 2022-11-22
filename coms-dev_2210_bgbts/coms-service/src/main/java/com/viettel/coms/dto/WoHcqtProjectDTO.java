package com.viettel.coms.dto;

import com.viettel.coms.bo.WoHcqtProjectBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoHcqtProjectDTO  extends ComsBaseFWDTO<WoHcqtProjectBO>  {

    private Long hcqtProjectId;
    private String name;
    private String code;
    private String userCreated;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    private Long status;
    private List<Long> idRange;

    public Long getHcqtProjectId() {
        return hcqtProjectId;
    }

    public void setHcqtProjectId(Long hcqtProjectId) {
        this.hcqtProjectId = hcqtProjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public List<Long> getIdRange() {
        return idRange;
    }

    public void setIdRange(List<Long> idRange) {
        this.idRange = idRange;
    }

    @Override
    public WoHcqtProjectBO toModel() {
        WoHcqtProjectBO bo = new WoHcqtProjectBO();

        bo.setCode(this.code);
        bo.setCreatedDate(this.createdDate);
        bo.setHcqtProjectId(this.hcqtProjectId);
        bo.setName(this.name);
        bo.setUserCreated(this.userCreated);
        bo.setStatus(this.status);

        return bo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }
}
