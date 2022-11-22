package com.viettel.coms.bo;

import com.viettel.coms.dto.WoNameDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_NAME")
public class WoNameBO extends BaseFWModelImpl {

    private Long id;
    private String name;
    private Long woTypeId;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_NAME_SEQ")})
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "WO_TYPE_ID")
    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoNameDTO toDTO() {
        WoNameDTO dto = new WoNameDTO();

        dto.setId(this.id);
        dto.setName(this.name);
        dto.setWoTypeId(this.woTypeId);
        dto.setStatus(this.status);

        return dto;
    }
}
