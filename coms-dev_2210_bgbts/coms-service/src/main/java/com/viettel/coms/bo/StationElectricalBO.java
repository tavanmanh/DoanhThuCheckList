package com.viettel.coms.bo;

import com.viettel.coms.dto.ComsBaseFWDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@Entity
@Table(name = "STATION_ELECTRICAL")
public class StationElectricalBO  extends BaseFWModelImpl{

	@Id
	@Column(name = "STATION_ID")
	private Long stationId;
	@Column(name = "STATION_CODE")
	private String stationCode;
	@Column(name = "SYS_GROUP_ID")
	private Long sysGroupId;

	public Long getStationId() {

		return stationId;
	}

	public void setStationId(Long stationId) {

		this.stationId = stationId;
	}

	public String getStationCode() {

		return stationCode;
	}

	public void setStationCode(String stationCode) {

		this.stationCode = stationCode;
	}

	public Long getSysGroupId() {

		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {

		this.sysGroupId = sysGroupId;
	}

	@Override
	public BaseFWDTOImpl toDTO() {

		return null;
	}
}
