package com.viettel.coms.bo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricDetailDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricDetailBO")
@Table(name = "ELECTRIC_DETAIL")
public class ElectricDetailBO extends BaseFWModelImpl{
	
	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_DETAIL_SEQ")})
	@Column(name = "ID", length = 22)
	private Long electricDetailId;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC", length = 22)
	private Long electric;
	@Column(name = "DISTANCE", length = 22)
	private Long distance;
	@Column(name = "ELECTRIC_QUOTA_CB_ELECTRIC_METER_A", length = 22)
	private Long electricQuotaCBElectricMeterA;
	@Column(name = "ELECTRIC_QUOTA_CB_STATION_A", length = 22)
	private Long electricQuotaCBStationA;
	@Column(name = "WIRE_TYPE", length = 22)
	private Long wireType;
	@Column(name = "SUPPILER", length = 200)
	private String suppiler;
	@Column(name = "VOLTAGE_AC", length = 22)
	private Long voltageAC;
	@Column(name = "HOST_OPINION", length = 200)
	private String hostOpinion;
	@Column(name = "RATE_CAPACITY_STATION", length = 22)
	private Long rateCapacityStation;
	@Column(name = "PRICE", length = 22)
	private Long price;
	@Column(name = "SECTION", length = 22)
	private Long section;
	@Column(name = "CONDUTOR_QUALITY", length = 22)
	private Long condutorQuality;
	@Column(name = "SUPERFICIES", length = 22)
	private Long superficies;
	@Column(name = "MAX_SIZE", length = 22)
	private String maxSize;

	@Override
	public ElectricDetailDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricDetailDTO dto = new ElectricDetailDTO();
		dto.setElectricDetailId(this.electricDetailId);
		dto.setDeviceId(this.deviceId);
		dto.setElectric(this.electric);
		dto.setDistance(this.distance);
		dto.setElectricQuotaCBElectricMeterA(this.electricQuotaCBElectricMeterA);
		dto.setElectricQuotaCBStationA(this.electricQuotaCBStationA);
		dto.setWireType(this.wireType);
		dto.setSuppiler(this.suppiler);
		dto.setVoltageAC(this.voltageAC);
		dto.setHostOpinion(this.hostOpinion);
		dto.setRateCapacityStation(this.rateCapacityStation);
		dto.setPrice(this.price);
		dto.setSection(this.section);
		dto.setCondutorQuality(this.condutorQuality);
		dto.setSuperficies(this.superficies);
		dto.setMaxSize(this.maxSize);
		return dto;
	}

	
	public Long getElectricDetailId() {
		return electricDetailId;
	}


	public void setElectricDetailId(Long electricDetailId) {
		this.electricDetailId = electricDetailId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getElectric() {
		return electric;
	}

	public void setElectric(Long electric) {
		this.electric = electric;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getElectricQuotaCBElectricMeterA() {
		return electricQuotaCBElectricMeterA;
	}

	public void setElectricQuotaCBElectricMeterA(Long electricQuotaCBElectricMeterA) {
		this.electricQuotaCBElectricMeterA = electricQuotaCBElectricMeterA;
	}

	public Long getElectricQuotaCBStationA() {
		return electricQuotaCBStationA;
	}

	public void setElectricQuotaCBStationA(Long electricQuotaCBStationA) {
		this.electricQuotaCBStationA = electricQuotaCBStationA;
	}

	public Long getWireType() {
		return wireType;
	}

	public void setWireType(Long wireType) {
		this.wireType = wireType;
	}

	public String getSuppiler() {
		return suppiler;
	}

	public void setSuppiler(String suppiler) {
		this.suppiler = suppiler;
	}

	public Long getVoltageAC() {
		return voltageAC;
	}

	public void setVoltageAC(Long voltageAC) {
		this.voltageAC = voltageAC;
	}

	public String getHostOpinion() {
		return hostOpinion;
	}

	public void setHostOpinion(String hostOpinion) {
		this.hostOpinion = hostOpinion;
	}

	public Long getRateCapacityStation() {
		return rateCapacityStation;
	}

	public void setRateCapacityStation(Long rateCapacityStation) {
		this.rateCapacityStation = rateCapacityStation;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getSection() {
		return section;
	}

	public void setSection(Long section) {
		this.section = section;
	}

	public Long getCondutorQuality() {
		return condutorQuality;
	}

	public void setCondutorQuality(Long condutorQuality) {
		this.condutorQuality = condutorQuality;
	}

	public Long getSuperficies() {
		return superficies;
	}

	public void setSuperficies(Long superficies) {
		this.superficies = superficies;
	}


	public String getMaxSize() {
		return maxSize;
	}


	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

}
