package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricRectifierDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricRectifierBO")
@Table(name = "ELECTRIC_RECTIFIER")
public class ElectricRectifierBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_RECTIFIER_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "SERIAL", length = 200)
	private String serial;
	@Column(name = "KTTS_STATUS", length = 1)
	private Long kttsStatus;
	@Column(name = "GOOD_CODE", length = 200)
	private String goodCode;
	@Column(name = "GOOD_NAME", length = 200)
	private String goodName;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer;
	@Column(name = "RATED_POWER", length = 10)
	private Long ratedPower;
	@Column(name = "QUANTITY_IN_USE", length = 10)
	private Long quantityInUse;
	@Column(name = "QUANTITY_CAN_ADDED", length = 10)
	private Long quantitycanAdded;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
	@Column(name = "STATE_ER", length = 1)
	private Long stateER;
	
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

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getKttsStatus() {
		return kttsStatus;
	}

	public void setKttsStatus(Long kttsStatus) {
		this.kttsStatus = kttsStatus;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getRatedPower() {
		return ratedPower;
	}

	public void setRatedPower(Long ratedPower) {
		this.ratedPower = ratedPower;
	}

	public Long getQuantityInUse() {
		return quantityInUse;
	}

	public void setQuantityInUse(Long quantityInUse) {
		this.quantityInUse = quantityInUse;
	}

	public Long getQuantitycanAdded() {
		return quantitycanAdded;
	}

	public void setQuantitycanAdded(Long quantitycanAdded) {
		this.quantitycanAdded = quantitycanAdded;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}

	public Long getStateER() {
		return stateER;
	}

	public void setStateER(Long stateER) {
		this.stateER = stateER;
	}

	@Override
	public ElectricRectifierDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricRectifierDTO obj = new ElectricRectifierDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setSerial(this.serial);
		obj.setKttsStatus(this.kttsStatus);
		obj.setGoodCode(this.goodCode);
		obj.setGoodName(this.goodName);
		obj.setModel(this.model);
		obj.setManufacturer(this.manufacturer);
		obj.setRatedPower(this.ratedPower);
		obj.setQuantityInUse(this.quantityInUse);
		obj.setQuantitycanAdded(this.quantitycanAdded);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setStateER(this.stateER);
		return obj;
	}



}
