package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.ElectricWarningSystemDTO;
import com.viettel.coms.dto.GeneratorDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricWarningSystemBO")
@Table(name = "ELECTRIC_WARNING_SYSTEM")
public class ElectricWarningSystemBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_WARNING_SYSTEM_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "STATE_AC_BOX", length = 1)
	private Long stateACBox;
	@Column(name = "STATUS_AC_BOX", length = 1)
	private Long statusACBox;
	@Column(name = "STATE_LOW_BATTERY", length = 1)
	private Long stateLowBattery;
	@Column(name = "STATUS_LOW_BATTERY", length = 1)
	private Long statusLowBattery;
	@Column(name = "STATE_TEMPERATURE_WARNING", length = 1)
	private Long stateTemperatureWarning;
	@Column(name = "STATUS_TEMPERATURE_WARNING", length = 1)
	private Long statusTemperatureWarning;
	@Column(name = "STATE_SMOKE_WARNING", length = 1)
	private Long stateSmokeWarning;
	@Column(name = "STATUS_SMOKE_WARNING", length = 1)
	private Long statusSmokeWarning;
	@Column(name = "STATE_POWER_CABINET_MALFUNCTION_WARNING", length = 1)
	private Long statePowerCabinetMalfuntionWarning;
	@Column(name = "STATUS_POWER_CABINET_MALFUNCTION_WARNING", length = 1)
	private Long statusPowerCabinetMalfuntionWarning;
	@Column(name = "STATE_EXPLOSIVE_FACTORY_OPEN_WARNING", length = 1)
	private Long stateExplosiveFactoryOpenWarning;
	@Column(name = "STATUS_EXPLOSIVE_FACTORY_OPEN_WARNING", length = 1)
	private Long statusExplosiveFactoryOpenWarning;
	@Column(name = "STATE_STATION_OPEN_WARNING", length = 1)
	private Long stateStationOpenWarning;
	@Column(name = "STATUS_STATION_OPEN_WARNING", length = 1)
	private Long statusStationOpenWarning;
	@Column(name = "STATE_LOW_AC", length = 1)
	private Long stateLowAC;
	@Column(name = "STATUS_LOW_AC", length = 1)
	private Long statusLowAC;
	
	


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




	public Long getStateACBox() {
		return stateACBox;
	}




	public void setStateACBox(Long stateACBox) {
		this.stateACBox = stateACBox;
	}




	public Long getStatusACBox() {
		return statusACBox;
	}




	public void setStatusACBox(Long statusACBox) {
		this.statusACBox = statusACBox;
	}




	public Long getStateLowBattery() {
		return stateLowBattery;
	}




	public void setStateLowBattery(Long stateLowBattery) {
		this.stateLowBattery = stateLowBattery;
	}




	public Long getStatusLowBattery() {
		return statusLowBattery;
	}




	public void setStatusLowBattery(Long statusLowBattery) {
		this.statusLowBattery = statusLowBattery;
	}




	public Long getStateTemperatureWarning() {
		return stateTemperatureWarning;
	}




	public void setStateTemperatureWarning(Long stateTemperatureWarning) {
		this.stateTemperatureWarning = stateTemperatureWarning;
	}




	public Long getStatusTemperatureWarning() {
		return statusTemperatureWarning;
	}




	public void setStatusTemperatureWarning(Long statusTemperatureWarning) {
		this.statusTemperatureWarning = statusTemperatureWarning;
	}




	public Long getStateSmokeWarning() {
		return stateSmokeWarning;
	}




	public void setStateSmokeWarning(Long stateSmokeWarning) {
		this.stateSmokeWarning = stateSmokeWarning;
	}




	public Long getStatusSmokeWarning() {
		return statusSmokeWarning;
	}




	public void setStatusSmokeWarning(Long statusSmokeWarning) {
		this.statusSmokeWarning = statusSmokeWarning;
	}




	public Long getStatePowerCabinetMalfuntionWarning() {
		return statePowerCabinetMalfuntionWarning;
	}




	public void setStatePowerCabinetMalfuntionWarning(Long statePowerCabinetMalfuntionWarning) {
		this.statePowerCabinetMalfuntionWarning = statePowerCabinetMalfuntionWarning;
	}




	public Long getStatusPowerCabinetMalfuntionWarning() {
		return statusPowerCabinetMalfuntionWarning;
	}




	public void setStatusPowerCabinetMalfuntionWarning(Long statusPowerCabinetMalfuntionWarning) {
		this.statusPowerCabinetMalfuntionWarning = statusPowerCabinetMalfuntionWarning;
	}




	public Long getStateExplosiveFactoryOpenWarning() {
		return stateExplosiveFactoryOpenWarning;
	}




	public void setStateExplosiveFactoryOpenWarning(Long stateExplosiveFactoryOpenWarning) {
		this.stateExplosiveFactoryOpenWarning = stateExplosiveFactoryOpenWarning;
	}




	public Long getStatusExplosiveFactoryOpenWarning() {
		return statusExplosiveFactoryOpenWarning;
	}




	public void setStatusExplosiveFactoryOpenWarning(Long statusExplosiveFactoryOpenWarning) {
		this.statusExplosiveFactoryOpenWarning = statusExplosiveFactoryOpenWarning;
	}




	public Long getStateStationOpenWarning() {
		return stateStationOpenWarning;
	}




	public void setStateStationOpenWarning(Long stateStationOpenWarning) {
		this.stateStationOpenWarning = stateStationOpenWarning;
	}




	public Long getStatusStationOpenWarning() {
		return statusStationOpenWarning;
	}




	public void setStatusStationOpenWarning(Long statusStationOpenWarning) {
		this.statusStationOpenWarning = statusStationOpenWarning;
	}




	public Long getStateLowAC() {
		return stateLowAC;
	}




	public void setStateLowAC(Long stateLowAC) {
		this.stateLowAC = stateLowAC;
	}




	public Long getStatusLowAC() {
		return statusLowAC;
	}




	public void setStatusLowAC(Long statusLowAC) {
		this.statusLowAC = statusLowAC;
	}




	@Override
	public ElectricWarningSystemDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricWarningSystemDTO obj = new ElectricWarningSystemDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setStateACBox(this.stateACBox);
		obj.setStatusACBox(this.statusACBox);
		obj.setStateLowBattery(this.stateLowBattery);
		obj.setStatusLowBattery(this.statusLowBattery);
		obj.setStateTemperatureWarning(this.stateTemperatureWarning);
		obj.setStatusTemperatureWarning(this.statusTemperatureWarning);
		obj.setStateSmokeWarning(this.stateSmokeWarning);
		obj.setStatusSmokeWarning(this.statusSmokeWarning);
		obj.setStatePowerCabinetMalfuntionWarning(this.statePowerCabinetMalfuntionWarning);
		obj.setStatusPowerCabinetMalfuntionWarning(this.statusPowerCabinetMalfuntionWarning);
		obj.setStateExplosiveFactoryOpenWarning(this.stateExplosiveFactoryOpenWarning);
		obj.setStatusExplosiveFactoryOpenWarning(this.statusExplosiveFactoryOpenWarning);
		obj.setStateStationOpenWarning(this.stateStationOpenWarning);
		obj.setStatusStationOpenWarning(this.statusStationOpenWarning);
		obj.setStateLowAC(this.stateLowAC);
		obj.setStatusLowAC(this.statusLowAC);
		return obj;
	}



}
