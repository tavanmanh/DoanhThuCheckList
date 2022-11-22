package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CabinetsSourceACDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CabinetsSourceACBO")
@Table(name = "CABINETS_SOURCE_AC")
public class CabinetsSourceACBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "CABINETS_SOURCE_AC_SEQ")})

	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "CABINETS_SOURCE_NAME", length = 200)
	private String cabinetsSourceName;
	@Column(name = "PHASE_NUMBER", length = 22)
	private Long phaseNumber;
	@Column(name = "STATUS", length = 22)
	private Long status;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}


	public String getCabinetsSourceName() {
		return cabinetsSourceName;
	}


	public void setCabinetsSourceName(String cabinetsSourceName) {
		this.cabinetsSourceName = cabinetsSourceName;
	}


	public Long getPhaseNumber() {
		return phaseNumber;
	}


	public void setPhaseNumber(Long phaseNumber) {
		this.phaseNumber = phaseNumber;
	}


	public Long getStatus() {
		return status;
	}


	public void setStatus(Long status) {
		this.status = status;
	}


	@Override
	public CabinetsSourceACDTO toDTO() {
		// TODO Auto-generated method stub
		CabinetsSourceACDTO dto = new CabinetsSourceACDTO();
		dto.setId(this.id);
		dto.setDeviceId(this.deviceId);
		dto.setCabinetsSourceName(this.cabinetsSourceName);
		dto.setPhaseNumber(this.phaseNumber);
		dto.setStatus(this.status);
		return dto;
	}

}
