package com.viettel.coms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricExplosionFactoryDTO;
import com.viettel.coms.dto.ElectricLightningCutFilterDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricLightningCutFilterBO")
@Table(name = "ELECTRIC_LIGHTNING_CUT_FILTER")
public class ElectricLightningCutFilterBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_LIGHTNING_CUT_FILTER_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC_LIGHTNING_CUT_FILTER_NAME", length = 200)
	private String electricLightningCutFilterName;
	@Column(name = "PRIMARY_STATUS", length = 1)
	private Long primaryStatus;
	@Column(name = "PRIMARY_QUANTITY", length = 1)
	private Long primaryQuantity;
	@Column(name = "PRIMARY_CONDITION", length = 1)
	private Long primaryCondition;
	@Column(name = "PRIMARY_SPECIES", length = 200)
	private String primarySpecies;
	@Column(name = "RESISTOR", length = 10)
	private Long resistor;
	@Column(name = "SECONDARY_STATUS", length = 1)
	private Long secondaryStatus;
	@Column(name = "SECONDARY_QUANTITY", length = 10)
	private Long secondaryQuantity;
	@Column(name = "SECONDARY_CONDITION", length = 1)
	private Long secondaryCodition;
	@Column(name = "SECONDARY_SPECIES", length = 200)
	private String secondarySpecies;
	@Column(name = "OTHER_LIGHTNING_CUT_FILTER_NAME", length = 200)
	private String otherLightningCutFilterName;
	@Column(name = "OTHER_LIGHTNING_CUT_FILTER_STATUS", length = 1)
	private Long otherLightningCutFilterStatus;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
	@Column(name = "LAST_MAINTENANCE_TIME")
	private Date lastMaintenanceTime;


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


	public String getElectricLightningCutFilterName() {
		return electricLightningCutFilterName;
	}


	public void setElectricLightningCutFilterName(String electricLightningCutFilterName) {
		this.electricLightningCutFilterName = electricLightningCutFilterName;
	}


	public Long getPrimaryStatus() {
		return primaryStatus;
	}


	public void setPrimaryStatus(Long primaryStatus) {
		this.primaryStatus = primaryStatus;
	}


	public Long getPrimaryQuantity() {
		return primaryQuantity;
	}


	public void setPrimaryQuantity(Long primaryQuantity) {
		this.primaryQuantity = primaryQuantity;
	}


	public Long getPrimaryCondition() {
		return primaryCondition;
	}


	public void setPrimaryCondition(Long primaryCondition) {
		this.primaryCondition = primaryCondition;
	}


	public String getPrimarySpecies() {
		return primarySpecies;
	}


	public void setPrimarySpecies(String primarySpecies) {
		this.primarySpecies = primarySpecies;
	}


	public Long getResistor() {
		return resistor;
	}


	public void setResistor(Long resistor) {
		this.resistor = resistor;
	}


	public Long getSecondaryStatus() {
		return secondaryStatus;
	}


	public void setSecondaryStatus(Long secondaryStatus) {
		this.secondaryStatus = secondaryStatus;
	}


	public Long getSecondaryQuantity() {
		return secondaryQuantity;
	}


	public void setSecondaryQuantity(Long secondaryQuantity) {
		this.secondaryQuantity = secondaryQuantity;
	}


	public Long getSecondaryCodition() {
		return secondaryCodition;
	}


	public void setSecondaryCodition(Long secondaryCodition) {
		this.secondaryCodition = secondaryCodition;
	}


	public String getSecondarySpecies() {
		return secondarySpecies;
	}


	public void setSecondarySpecies(String secondarySpecies) {
		this.secondarySpecies = secondarySpecies;
	}


	public String getOtherLightningCutFilterName() {
		return otherLightningCutFilterName;
	}


	public void setOtherLightningCutFilterName(String otherLightningCutFilterName) {
		this.otherLightningCutFilterName = otherLightningCutFilterName;
	}


	public Long getOtherLightningCutFilterStatus() {
		return otherLightningCutFilterStatus;
	}


	public void setOtherLightningCutFilterStatus(Long otherLightningCutFilterStatus) {
		this.otherLightningCutFilterStatus = otherLightningCutFilterStatus;
	}


	public Date getTimeIntoUse() {
		return timeIntoUse;
	}


	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}


	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}


	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}


	@Override
	public ElectricLightningCutFilterDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricLightningCutFilterDTO obj = new ElectricLightningCutFilterDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricLightningCutFilterName(this.electricLightningCutFilterName);
		obj.setPrimaryStatus(this.primaryStatus);
		obj.setPrimaryQuantity(this.primaryQuantity);
		obj.setPrimaryCondition(this.primaryCondition);
		obj.setPrimarySpecies(this.primarySpecies);
		obj.setResistor(this.resistor);
		obj.setSecondaryStatus(this.secondaryStatus);
		obj.setSecondaryQuantity(this.secondaryQuantity);
		obj.setSecondaryCodition(this.secondaryCodition);
		obj.setSecondarySpecies(this.secondarySpecies);
		obj.setOtherLightningCutFilterName(this.otherLightningCutFilterName);
		obj.setOtherLightningCutFilterStatus(this.otherLightningCutFilterStatus);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}



}
