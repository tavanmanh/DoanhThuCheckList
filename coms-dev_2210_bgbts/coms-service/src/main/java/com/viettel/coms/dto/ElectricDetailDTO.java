package com.viettel.coms.dto;

import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.DeviceStationElectricalBO;
import com.viettel.coms.bo.ElectricDetailBO;

@XmlRootElement(name = "ELECTRIC_DETAILBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricDetailDTO extends ComsBaseFWDTO<ElectricDetailBO>{

	private Long electricDetailId;
	private Long id;
	private Long deviceId;
	private Long electric;
	private Long distance;
	private Long electricQuotaCBElectricMeterA;
	private Long electricQuotaCBStationA;
	private Long wireType;
	private String suppiler;
	private Long voltageAC;
	private String hostOpinion;
	private Long rateCapacityStation;
	private Long price;
	private Long section;
	private Long condutorQuality;
	private Long superficies;
	private Long state;
	private List<AttachElectronicStationDTO> listFileAttach;
	private String maxSize;
	private String provinceName;
	private String stationCode;
	
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private List<AttachElectronicStationDTO> listImage4;
	private List<AttachElectronicStationDTO> listImage5;
	private List<AttachElectronicStationDTO> listImage6;
	private List<AttachElectronicStationDTO> listImage7;
	private List<AttachElectronicStationDTO> listImage8;
	private List<AttachElectronicStationDTO> listImage9;
	private List<AttachElectronicStationDTO> listImage10;
	private List<AttachElectronicStationDTO> listImage11;
	private List<AttachElectronicStationDTO> listImage12;
	private List<AttachElectronicStationDTO> listImage13;
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

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
	}

	public String getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

	public List<AttachElectronicStationDTO> getListImage1() {
		return listImage1;
	}

	public void setListImage1(List<AttachElectronicStationDTO> listImage1) {
		this.listImage1 = listImage1;
	}

	public List<AttachElectronicStationDTO> getListImage2() {
		return listImage2;
	}

	public void setListImage2(List<AttachElectronicStationDTO> listImage2) {
		this.listImage2 = listImage2;
	}

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
	}

	public List<AttachElectronicStationDTO> getListImage4() {
		return listImage4;
	}

	public void setListImage4(List<AttachElectronicStationDTO> listImage4) {
		this.listImage4 = listImage4;
	}

	public List<AttachElectronicStationDTO> getListImage5() {
		return listImage5;
	}

	public void setListImage5(List<AttachElectronicStationDTO> listImage5) {
		this.listImage5 = listImage5;
	}

	public List<AttachElectronicStationDTO> getListImage6() {
		return listImage6;
	}

	public void setListImage6(List<AttachElectronicStationDTO> listImage6) {
		this.listImage6 = listImage6;
	}

	public List<AttachElectronicStationDTO> getListImage7() {
		return listImage7;
	}

	public void setListImage7(List<AttachElectronicStationDTO> listImage7) {
		this.listImage7 = listImage7;
	}

	public List<AttachElectronicStationDTO> getListImage8() {
		return listImage8;
	}

	public void setListImage8(List<AttachElectronicStationDTO> listImage8) {
		this.listImage8 = listImage8;
	}

	public List<AttachElectronicStationDTO> getListImage9() {
		return listImage9;
	}

	public void setListImage9(List<AttachElectronicStationDTO> listImage9) {
		this.listImage9 = listImage9;
	}

	public List<AttachElectronicStationDTO> getListImage10() {
		return listImage10;
	}

	public void setListImage10(List<AttachElectronicStationDTO> listImage10) {
		this.listImage10 = listImage10;
	}

	public List<AttachElectronicStationDTO> getListImage11() {
		return listImage11;
	}

	public void setListImage11(List<AttachElectronicStationDTO> listImage11) {
		this.listImage11 = listImage11;
	}

	public List<AttachElectronicStationDTO> getListImage12() {
		return listImage12;
	}

	public void setListImage12(List<AttachElectronicStationDTO> listImage12) {
		this.listImage12 = listImage12;
	}

	public List<AttachElectronicStationDTO> getListImage13() {
		return listImage13;
	}

	public void setListImage13(List<AttachElectronicStationDTO> listImage13) {
		this.listImage13 = listImage13;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricDetailBO toModel() {
		// TODO Auto-generated method stub
		ElectricDetailBO bo = new ElectricDetailBO();
		bo.setElectricDetailId(this.electricDetailId);
		bo.setDeviceId(this.deviceId);
		bo.setElectric(this.electric);
		bo.setDistance(this.distance);
		bo.setElectricQuotaCBElectricMeterA(this.electricQuotaCBElectricMeterA);
		bo.setElectricQuotaCBStationA(this.electricQuotaCBStationA);
		bo.setWireType(this.wireType);
		bo.setSuppiler(this.suppiler);
		bo.setVoltageAC(this.voltageAC);
		bo.setHostOpinion(this.hostOpinion);
		bo.setRateCapacityStation(this.rateCapacityStation);
		bo.setPrice(this.price);
		bo.setSection(this.section);
		bo.setCondutorQuality(this.condutorQuality);
		bo.setSuperficies(this.superficies);
		bo.setMaxSize(this.maxSize);
		return bo;
	}

}
