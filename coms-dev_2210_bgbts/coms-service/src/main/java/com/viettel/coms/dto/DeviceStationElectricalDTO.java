package com.viettel.coms.dto;

import java.util.Date;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.DeviceStationElectricalBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
@XmlRootElement(name = "DEVICE_STATION_ELECTRICALBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DeviceStationElectricalDTO extends ComsBaseFWDTO<DeviceStationElectricalBO>{
	private java.lang.Long deviceId;
	private java.lang.String type;
	
	private java.lang.String typeName;
	private java.lang.String deviceCode;
	private java.lang.String deviceName;
	
	private java.lang.String serial;
	
	private java.lang.String status;
	private java.lang.String state;
	
	private java.lang.Long stationId;
	private String reason;
	private String stationCode;
	private Long provinceId;
	private String areaCode;
	
	private UtilAttachDocumentDTO attachFile;
	private AttachElectronicStationDTO listImage1;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDate;
	private java.lang.Long createUser;
	private String stateStr;
	private String attachFileName;
	private ElectricDetailDTO electricDetailDTO;
	private CabinetsSourceACDTO cabinetsSourceACDTO;
	private CabinetsSourceDCDTO cabinetsSourceDCDTO;
	private ElectricAirConditioningACDTO electricAirConditioningACDTO;
	private ElectricHeatExchangerDTO electricHeatExchangerDTO;
	private ElectricNotificationFilterDustDTO electricNotificationFilterDustDTO;
	private ElectricAirConditioningDCDTO electricAirConditioningDCDTO;
	private ElectricFireExtinguisherDTO electricFireExtinguisherDTO;
	private GeneratorDTO generatorDTO;
	private BatteryDTO batteryDTO;
	private ElectricWarningSystemDTO electricWarningSystemDTO;
	private ElectricATSDTO electricATSDTO;
	private ElectricExplosionFactoryDTO electricExplosionFactoryDTO;
	private ElectricLightningCutFilterDTO electricLightningCutFilterDTO;
	private ElectricEarthingSystemDTO electricEarthingSystemDTO;
	private ElectricRectifierDTO electricRectifierDTO;
	private StationInformationDTO stationInformationDTO;


	//aeg start 20220529
	private Long failureStatus;

	private Long createdUser;

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;

	private Long approvedUser;

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date approvedDate;

	private String descriptionFailure;

	private List<UtilAttachDocumentDTO> brokenImages;

	private String describeAfterMath;

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date finishDate;

	private Long qoutaTime;

	private String failure;

	private String failureString;

	//aeg end 20220529


	public java.lang.Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(java.lang.Long deviceId) {
		this.deviceId = deviceId;
	}
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.lang.String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(java.lang.String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public java.lang.String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(java.lang.String deviceName) {
		this.deviceName = deviceName;
	}
	public java.lang.String getSerial() {
		return serial;
	}
	public void setSerial(java.lang.String serial) {
		this.serial = serial;
	}
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getState() {
		return state;
	}
	public void setState(java.lang.String state) {
		this.state = state;
	}
	public java.lang.Long getStationId() {
		return stationId;
	}
	public void setStationId(java.lang.Long stationId) {
		this.stationId = stationId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(java.lang.Long createUser) {
		this.createUser = createUser;
	}
	
	public UtilAttachDocumentDTO getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(UtilAttachDocumentDTO attachFile) {
		this.attachFile = attachFile;
	}
	
	public java.lang.String getTypeName() {
		return typeName;
	}
	public void setTypeName(java.lang.String typeName) {
		this.typeName = typeName;
	}
	
	public String getAttachFileName() {
		return attachFileName;
	}
	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public ElectricDetailDTO getElectricDetailDTO() {
		return electricDetailDTO;
	}
	public void setElectricDetailDTO(ElectricDetailDTO electricDetailDTO) {
		this.electricDetailDTO = electricDetailDTO;
	}
	public CabinetsSourceACDTO getCabinetsSourceACDTO() {
		return cabinetsSourceACDTO;
	}
	public void setCabinetsSourceACDTO(CabinetsSourceACDTO cabinetsSourceACDTO) {
		this.cabinetsSourceACDTO = cabinetsSourceACDTO;
	}
	public CabinetsSourceDCDTO getCabinetsSourceDCDTO() {
		return cabinetsSourceDCDTO;
	}
	public void setCabinetsSourceDCDTO(CabinetsSourceDCDTO cabinetsSourceDCDTO) {
		this.cabinetsSourceDCDTO = cabinetsSourceDCDTO;
	}
	public ElectricAirConditioningACDTO getElectricAirConditioningACDTO() {
		return electricAirConditioningACDTO;
	}
	public void setElectricAirConditioningACDTO(ElectricAirConditioningACDTO electricAirConditioningACDTO) {
		this.electricAirConditioningACDTO = electricAirConditioningACDTO;
	}
	public ElectricHeatExchangerDTO getElectricHeatExchangerDTO() {
		return electricHeatExchangerDTO;
	}
	public void setElectricHeatExchangerDTO(ElectricHeatExchangerDTO electricHeatExchangerDTO) {
		this.electricHeatExchangerDTO = electricHeatExchangerDTO;
	}
	
	public ElectricNotificationFilterDustDTO getElectricNotificationFilterDustDTO() {
		return electricNotificationFilterDustDTO;
	}
	public void setElectricNotificationFilterDustDTO(ElectricNotificationFilterDustDTO electricNotificationFilterDustDTO) {
		this.electricNotificationFilterDustDTO = electricNotificationFilterDustDTO;
	}
	
	public ElectricAirConditioningDCDTO getElectricAirConditioningDCDTO() {
		return electricAirConditioningDCDTO;
	}
	public void setElectricAirConditioningDCDTO(ElectricAirConditioningDCDTO electricAirConditioningDCDTO) {
		this.electricAirConditioningDCDTO = electricAirConditioningDCDTO;
	}
	public ElectricFireExtinguisherDTO getElectricFireExtinguisherDTO() {
		return electricFireExtinguisherDTO;
	}
	public void setElectricFireExtinguisherDTO(ElectricFireExtinguisherDTO electricFireExtinguisherDTO) {
		this.electricFireExtinguisherDTO = electricFireExtinguisherDTO;
	}
	
	public AttachElectronicStationDTO getListImage1() {
		return listImage1;
	}
	public void setListImage1(AttachElectronicStationDTO listImage1) {
		this.listImage1 = listImage1;
	}
	
	public GeneratorDTO getGeneratorDTO() {
		return generatorDTO;
	}
	public void setGeneratorDTO(GeneratorDTO generatorDTO) {
		this.generatorDTO = generatorDTO;
	}
	
	public BatteryDTO getBatteryDTO() {
		return batteryDTO;
	}
	public void setBatteryDTO(BatteryDTO batteryDTO) {
		this.batteryDTO = batteryDTO;
	}
	
	public ElectricWarningSystemDTO getElectricWarningSystemDTO() {
		return electricWarningSystemDTO;
	}
	public void setElectricWarningSystemDTO(ElectricWarningSystemDTO electricWarningSystemDTO) {
		this.electricWarningSystemDTO = electricWarningSystemDTO;
	}
	
	public ElectricATSDTO getElectricATSDTO() {
		return electricATSDTO;
	}
	public void setElectricATSDTO(ElectricATSDTO electricATSDTO) {
		this.electricATSDTO = electricATSDTO;
	}
	
	public ElectricExplosionFactoryDTO getElectricExplosionFactoryDTO() {
		return electricExplosionFactoryDTO;
	}
	public void setElectricExplosionFactoryDTO(ElectricExplosionFactoryDTO electricExplosionFactoryDTO) {
		this.electricExplosionFactoryDTO = electricExplosionFactoryDTO;
	}
	
	public ElectricLightningCutFilterDTO getElectricLightningCutFilterDTO() {
		return electricLightningCutFilterDTO;
	}
	public void setElectricLightningCutFilterDTO(ElectricLightningCutFilterDTO electricLightningCutFilterDTO) {
		this.electricLightningCutFilterDTO = electricLightningCutFilterDTO;
	}
	
	public ElectricEarthingSystemDTO getElectricEarthingSystemDTO() {
		return electricEarthingSystemDTO;
	}
	public void setElectricEarthingSystemDTO(ElectricEarthingSystemDTO electricEarthingSystemDTO) {
		this.electricEarthingSystemDTO = electricEarthingSystemDTO;
	}
	public ElectricRectifierDTO getElectricRectifierDTO() {
		return electricRectifierDTO;
	}
	public void setElectricRectifierDTO(ElectricRectifierDTO electricRectifierDTO) {
		this.electricRectifierDTO = electricRectifierDTO;
	}
	public StationInformationDTO getStationInformationDTO() {
		return stationInformationDTO;
	}
	public void setStationInformationDTO(StationInformationDTO stationInformationDTO) {
		this.stationInformationDTO = stationInformationDTO;
	}
	
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return stationId.toString();
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return stationId;
	}
	@Override
	public DeviceStationElectricalBO toModel() {
		// TODO Auto-generated method stub
		DeviceStationElectricalBO device = new DeviceStationElectricalBO();
		device.setDeviceId(this.deviceId);
		device.setType(this.type);
		device.setDeviceCode(this.deviceCode);
		device.setDeviceName(this.deviceName);
		device.setSerial(this.serial);
		device.setStatus(this.status);
		device.setState(this.state);
		device.setStationId(this.stationId);
		device.setCreateDate(this.createDate);
		device.setCreateUser(this.createUser);
		device.setReason(this.reason);
		device.setFailureStatus(failureStatus);
		device.setCreateDate(createDate);
		device.setCreateUser(createUser);
		device.setApprovedUser(approvedUser);
		device.setApprovedDate(approvedDate);
		device.setDescriptionFailure(descriptionFailure);
        return device;
	}

	public Long getFailureStatus() {

		return failureStatus;
	}

	public void setFailureStatus(Long failureStatus) {

		this.failureStatus = failureStatus;
	}

	public Long getCreatedUser() {

		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {

		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {

		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {

		this.createdDate = createdDate;
	}

	public Long getApprovedUser() {

		return approvedUser;
	}

	public void setApprovedUser(Long approvedUser) {

		this.approvedUser = approvedUser;
	}

	public Date getApprovedDate() {

		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {

		this.approvedDate = approvedDate;
	}

	public String getDescriptionFailure() {

		return descriptionFailure;
	}

	public void setDescriptionFailure(String descriptionFailure) {

		this.descriptionFailure = descriptionFailure;
	}

	public List<UtilAttachDocumentDTO> getBrokenImages() {

		return brokenImages;
	}

	public void setBrokenImages(List<UtilAttachDocumentDTO> brokenImages) {

		this.brokenImages = brokenImages;
	}

	public String getDescribeAfterMath() {

		return describeAfterMath;
	}

	public void setDescribeAfterMath(String describeAfterMath) {

		this.describeAfterMath = describeAfterMath;
	}

	public Date getFinishDate() {

		return finishDate;
	}

	public void setFinishDate(Date finishDate) {

		this.finishDate = finishDate;
	}

	public Long getQoutaTime() {

		return qoutaTime;
	}

	public void setQoutaTime(Long qoutaTime) {

		this.qoutaTime = qoutaTime;
	}

	public String getFailure() {

		return failure;
	}

	public void setFailure(String failure) {

		this.failure = failure;
	}

	public String getFailureString() {

		return failureString;
	}

	public void setFailureString(String failureString) {

		this.failureString = failureString;
	}
}
