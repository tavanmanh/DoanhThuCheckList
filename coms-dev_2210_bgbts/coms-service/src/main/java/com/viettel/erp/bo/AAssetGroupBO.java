/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.erp.bo;

import com.viettel.erp.dto.AAssetGroupDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "A_ASSET_GROUP")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class AAssetGroupBO extends BaseFWModelImpl {

    private java.lang.String isDeleted;
    private java.lang.Long aAssetGroupId;
    private java.lang.Long adOrgId;
    private java.lang.String value;
    private java.lang.String name;
    private java.lang.String isactive;
    private java.lang.Long createdby;
    private java.lang.Long updatedby;
    private java.util.Date created;
    private java.util.Date updated;

    @Column(name = "IS_DELETED", length = 1)
    public java.lang.String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(java.lang.String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence",
            parameters = {
                    @Parameter(name = "sequence", value = "A_ASSET_GROUP_SEQ")
            }
    )
    @Column(name = "A_ASSET_GROUP_ID", length = 22)
    public java.lang.Long getAAssetGroupId() {
        return aAssetGroupId;
    }

    public void setAAssetGroupId(java.lang.Long aAssetGroupId) {
        this.aAssetGroupId = aAssetGroupId;
    }

    @Column(name = "AD_ORG_ID", length = 22)
    public java.lang.Long getAdOrgId() {
        return adOrgId;
    }

    public void setAdOrgId(java.lang.Long adOrgId) {
        this.adOrgId = adOrgId;
    }

    @Column(name = "VALUE", length = 40)
    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    @Column(name = "NAME", length = 510)
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @Column(name = "ISACTIVE", length = 1)
    public java.lang.String getIsactive() {
        return isactive;
    }

    public void setIsactive(java.lang.String isactive) {
        this.isactive = isactive;
    }

    @Column(name = "CREATEDBY", length = 22)
    public java.lang.Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(java.lang.Long createdby) {
        this.createdby = createdby;
    }

    @Column(name = "UPDATEDBY", length = 22)
    public java.lang.Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(java.lang.Long updatedby) {
        this.updatedby = updatedby;
    }

    @Column(name = "CREATED", length = 7)
    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    @Column(name = "UPDATED", length = 7)
    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }


    @Override
    public AAssetGroupDTO toDTO() {
        AAssetGroupDTO aAssetGroupDTO = new AAssetGroupDTO();
        //set cac gia tri 
        aAssetGroupDTO.setIsDeleted(this.isDeleted);
        aAssetGroupDTO.setAAssetGroupId(this.aAssetGroupId);
        aAssetGroupDTO.setAdOrgId(this.adOrgId);
        aAssetGroupDTO.setValue(this.value);
        aAssetGroupDTO.setName(this.name);
        aAssetGroupDTO.setIsactive(this.isactive);
        aAssetGroupDTO.setCreatedby(this.createdby);
        aAssetGroupDTO.setUpdatedby(this.updatedby);
        aAssetGroupDTO.setCreated(this.created);
        aAssetGroupDTO.setUpdated(this.updated);
        return aAssetGroupDTO;
    }
}
