package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_MAPPING_CHECKLIST")
public class WoMappingChecklistBO extends BaseFWModelImpl {
    private Long id;
    private Long woId;
    private Long checkListId;
    private String state;
    private Long status;
    private String quantityByDate;
    private Double quantityLength;
    private Double addedQuantityLength;
    private String name;
    private Integer numImgRequire;
    private String tthqResult;
    private Long classId;
    private Long actualValue;
    private String dbhtVuong;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_MAPPING_CHECKLIST_SEQ")})
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "CHECKLIST_ID")
    public Long getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(Long checkListId) {
        this.checkListId = checkListId;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "QUANTITY_BY_DATE")
    public String getQuantityByDate() {
        return quantityByDate;
    }

    public void setQuantityByDate(String quantityByDate) {
        this.quantityByDate = quantityByDate;
    }

    @Column(name = "QUANTITY_LENGTH")
    public Double getQuantityLength() {
        return quantityLength;
    }

    public void setQuantityLength(Double quantityLength) {
        this.quantityLength = quantityLength;
    }

    @Column(name = "ADDED_QUANTITY_LENGTH")
    public Double getAddedQuantityLength() {
        return addedQuantityLength;
    }

    public void setAddedQuantityLength(Double addedQuantityLength) {
        this.addedQuantityLength = addedQuantityLength;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NUM_IMG_REQUIRE")
    public Integer getNumImgRequire() {
        return numImgRequire;
    }

    public void setNumImgRequire(Integer numImgRequire) {
        this.numImgRequire = numImgRequire;
    }

    @Column(name = "TTHQ_RESULT")
    public String getTthqResult() {
        return tthqResult;
    }

    public void setTthqResult(String tthqResult) {
        this.tthqResult = tthqResult;
    }

    @Column(name = "CLASS_ID")
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    @Column(name = "ACTUAL_VALUE")
    public Long getActualValue() {
        return actualValue;
    }

    public void setActualValue(Long actualValue) {
        this.actualValue = actualValue;
    }

    @Column(name = "DBHT_VUONG")
    public String getDbhtVuong() {
        return dbhtVuong;
    }

    public void setDbhtVuong(String dbhtVuong) {
        this.dbhtVuong = dbhtVuong;
    }

    @Override
    public WoMappingChecklistDTO toDTO() {
        WoMappingChecklistDTO dto = new WoMappingChecklistDTO();

        dto.setChecklistId(this.checkListId);
        dto.setWoId(this.woId);
        dto.setId(this.id);
        dto.setState(this.state);
        dto.setStatus(this.status);
        dto.setQuantityByDate(this.quantityByDate);
        dto.setQuantityLength(this.quantityLength);
        dto.setAddedQuantityLength(this.addedQuantityLength);
        dto.setName(this.name);
        dto.setNumImgRequire(this.numImgRequire);
        dto.setTthqResult(this.tthqResult);
        dto.setClassId(this.classId);
        dto.setActualValue(this.actualValue);
        dto.setDbhtVuong(this.dbhtVuong);

        return dto;
    }
}
