package com.viettel.coms.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.coms.bo.DesignEstimatesBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesignEstimatesDTO extends ComsBaseFWDTO<DesignEstimatesBO> {
	private Long designEstimatesId;
    private String area;
    private Long provinceId;
    private String stationVTNET;
    private String stationVCC;
    private String stationAddress;
    private Long districtId;
    private String stationType;
    private String terrain;
    private String stationLong;
    private String stationLat;
    private String pillarType;
    private String location;
    private String pillarHight;
    private String tube;
    private String fundamental;
    private String levelRockEarth;
    private String engineRoom;
    private String flootPass;
    private String explosionFactory;
    private String sourceMacro;
    private String sourceRRU;
    private String groundingPile;
    private String groundingGem;
    private String groundingDrill;
    private String wire2x25;
    private String wire2x35;
    private String wire2x50;
    private String wire2x70;
    private String wire4x25;
    private String wire4x35;
    private String pillarAvailable;
    private String tmPillar60;
    private String tmPillar70;
    private String tmPillar75;
    private String tmPillar80;
    private String tmPillar85;
    private String tmPillar100;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date designDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date designUpdateDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date estimatingDate;
    private Long designUserId;
    private Long designUpdateUser;
    private Long creatDesignEstimatesUser;
    private Long createdUser;
    private Long updateUser;
    private String node;
    private Long status;
    private String provinceName;
    private String districtName;
    private String createdUserName;
    private String designUserName;
    private String designUpdateUserName;
    private String creatDesignEstimatesUserName;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateFrom;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateTo;
    private List<UtilAttachDocumentDTO> fileLst;
    private String name;
    private String filePath;
   
    @Override
    public Long getFWModelId() {
        return designEstimatesId;
    }
    
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public String getStationVTNET() {
		return stationVTNET;
	}
	public void setStationVTNET(String stationVTNET) {
		this.stationVTNET = stationVTNET;
	}
	public String getStationVCC() {
		return stationVCC;
	}
	public void setStationVCC(String stationVCC) {
		this.stationVCC = stationVCC;
	}
	public String getStationAddress() {
		return stationAddress;
	}
	public void setStationAddress(String stationAddress) {
		this.stationAddress = stationAddress;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	public String getStationLong() {
		return stationLong;
	}
	public void setStationLong(String stationLong) {
		this.stationLong = stationLong;
	}
	public String getStationLat() {
		return stationLat;
	}
	public void setStationLat(String stationLat) {
		this.stationLat = stationLat;
	}
	public String getPillarType() {
		return pillarType;
	}
	public void setPillarType(String pillarType) {
		this.pillarType = pillarType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPillarHight() {
		return pillarHight;
	}
	public void setPillarHight(String pillarHight) {
		this.pillarHight = pillarHight;
	}
	public String getTube() {
		return tube;
	}
	public void setTube(String tube) {
		this.tube = tube;
	}
	public String getFundamental() {
		return fundamental;
	}
	public void setFundamental(String fundamental) {
		this.fundamental = fundamental;
	}
	public String getLevelRockEarth() {
		return levelRockEarth;
	}
	public void setLevelRockEarth(String levelRockEarth) {
		this.levelRockEarth = levelRockEarth;
	}
	public String getEngineRoom() {
		return engineRoom;
	}
	public void setEngineRoom(String engineRoom) {
		this.engineRoom = engineRoom;
	}
	public String getFlootPass() {
		return flootPass;
	}
	public void setFlootPass(String flootPass) {
		this.flootPass = flootPass;
	}
	public String getExplosionFactory() {
		return explosionFactory;
	}
	public void setExplosionFactory(String explosionFactory) {
		this.explosionFactory = explosionFactory;
	}
	public String getSourceMacro() {
		return sourceMacro;
	}
	public void setSourceMacro(String sourceMacro) {
		this.sourceMacro = sourceMacro;
	}
	public String getSourceRRU() {
		return sourceRRU;
	}
	public void setSourceRRU(String sourceRRU) {
		this.sourceRRU = sourceRRU;
	}
	public String getGroundingPile() {
		return groundingPile;
	}
	public void setGroundingPile(String groundingPile) {
		this.groundingPile = groundingPile;
	}
	public String getGroundingGem() {
		return groundingGem;
	}
	public void setGroundingGem(String groundingGem) {
		this.groundingGem = groundingGem;
	}
	public String getGroundingDrill() {
		return groundingDrill;
	}
	public void setGroundingDrill(String groundingDrill) {
		this.groundingDrill = groundingDrill;
	}
	public String getWire2x25() {
		return wire2x25;
	}
	public void setWire2x25(String wire2x25) {
		this.wire2x25 = wire2x25;
	}
	public String getWire2x35() {
		return wire2x35;
	}
	public void setWire2x35(String wire2x35) {
		this.wire2x35 = wire2x35;
	}
	public String getWire2x50() {
		return wire2x50;
	}
	public void setWire2x50(String wire2x50) {
		this.wire2x50 = wire2x50;
	}
	public String getWire2x70() {
		return wire2x70;
	}
	public void setWire2x70(String wire2x70) {
		this.wire2x70 = wire2x70;
	}
	public String getWire4x25() {
		return wire4x25;
	}
	public void setWire4x25(String wire4x25) {
		this.wire4x25 = wire4x25;
	}
	public String getWire4x35() {
		return wire4x35;
	}
	public void setWire4x35(String wire4x35) {
		this.wire4x35 = wire4x35;
	}
	public String getPillarAvailable() {
		return pillarAvailable;
	}
	public void setPillarAvailable(String pillarAvailable) {
		this.pillarAvailable = pillarAvailable;
	}
	public String getTmPillar60() {
		return tmPillar60;
	}
	public void setTmPillar60(String tmPillar60) {
		this.tmPillar60 = tmPillar60;
	}
	public String getTmPillar70() {
		return tmPillar70;
	}
	public void setTmPillar70(String tmPillar70) {
		this.tmPillar70 = tmPillar70;
	}
	public String getTmPillar75() {
		return tmPillar75;
	}
	public void setTmPillar75(String tmPillar75) {
		this.tmPillar75 = tmPillar75;
	}
	public String getTmPillar80() {
		return tmPillar80;
	}
	public void setTmPillar80(String tmPillar80) {
		this.tmPillar80 = tmPillar80;
	}
	public String getTmPillar85() {
		return tmPillar85;
	}
	public void setTmPillar85(String tmPillar85) {
		this.tmPillar85 = tmPillar85;
	}
	public String getTmPillar100() {
		return tmPillar100;
	}
	public void setTmPillar100(String tmPillar100) {
		this.tmPillar100 = tmPillar100;
	}
	public Date getDesignDate() {
		return designDate;
	}
	public void setDesignDate(Date designDate) {
		this.designDate = designDate;
	}
	public Date getDesignUpdateDate() {
		return designUpdateDate;
	}
	public void setDesignUpdateDate(Date designUpdateDate) {
		this.designUpdateDate = designUpdateDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getEstimatingDate() {
		return estimatingDate;
	}
	public void setEstimatingDate(Date estimatingDate) {
		this.estimatingDate = estimatingDate;
	}
	public Long getDesignUserId() {
		return designUserId;
	}
	public void setDesignUserId(Long disignUserId) {
		this.designUserId = disignUserId;
	}
	public Long getDesignUpdateUser() {
		return designUpdateUser;
	}
	public void setDesignUpdateUser(Long designUpdateUser) {
		this.designUpdateUser = designUpdateUser;
	}
	public Long getCreatDesignEstimatesUser() {
		return creatDesignEstimatesUser;
	}
	public void setCreatDesignEstimatesUser(Long creatDesignEstimatesUser) {
		this.creatDesignEstimatesUser = creatDesignEstimatesUser;
	}
	public Long getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
	
	 public Long getDesignEstimatesId() {
		return designEstimatesId;
	}

	public void setDesignEstimatesId(Long designEstimatesId) {
		this.designEstimatesId = designEstimatesId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDesignUserName() {
		return designUserName;
	}

	public void setDesignUserName(String designUserName) {
		this.designUserName = designUserName;
	}

	public String getDesignUpdateUserName() {
		return designUpdateUserName;
	}

	public void setDesignUpdateUserName(String designUpdateUserName) {
		this.designUpdateUserName = designUpdateUserName;
	}

	public String getCreatDesignEstimatesUserName() {
		return creatDesignEstimatesUserName;
	}

	public void setCreatDesignEstimatesUserName(String creatDesignEstimatesUserName) {
		this.creatDesignEstimatesUserName = creatDesignEstimatesUserName;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Date getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Date createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public Date getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Date createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public List<UtilAttachDocumentDTO> getFileLst() {
		return fileLst;
	}

	public void setFileLst(List<UtilAttachDocumentDTO> fileLst) {
		this.fileLst = fileLst;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	    public DesignEstimatesBO toModel() {
	    	DesignEstimatesBO obj = new DesignEstimatesBO();
	    	obj.setDesignEstimatesId(this.getDesignEstimatesId());
	    	obj.setArea(this.getArea());
	    	obj.setProvinceId(this.getProvinceId());
	    	obj.setStationVTNET(this.getStationVTNET());
	    	obj.setStationVCC(this.getStationVCC());
	    	obj.setStationAddress(this.getStationAddress());
	    	obj.setDistrictId(this.getDistrictId());
	    	obj.setStationType(this.getStationType());
	    	obj.setTerrain(this.getTerrain());
	    	obj.setStationLong(this.getStationLong());
	    	obj.setStationLat(this.getStationLat());
	    	obj.setPillarHight(this.getPillarHight());
	    	obj.setLocation(this.getLocation());
	    	obj.setPillarType(this.getPillarType());
	    	obj.setTube(this.getTube());
	    	obj.setFundamental(this.getFundamental());
	    	obj.setLevelRockEarth(this.getLevelRockEarth());
	    	obj.setEngineRoom(this.getEngineRoom());
	    	obj.setFlootPass(this.getFlootPass());
	    	obj.setExplosionFactory(this.getExplosionFactory());
	    	obj.setSourceMacro(this.getSourceMacro());
	    	obj.setSourceRRU(this.getSourceRRU());
	    	obj.setGroundingPile(this.getGroundingPile());
	    	obj.setGroundingGem(this.getGroundingGem());
	    	obj.setGroundingDrill(this.getGroundingDrill());
	    	obj.setStatus(this.getStatus());
	    	obj.setWire2x25(this.getWire2x25());
	    	obj.setWire2x35(this.getWire2x35());
	    	obj.setWire2x50(this.getWire2x50());
	    	obj.setWire2x70(this.getWire2x70());
	    	obj.setWire4x25(this.getWire4x25());
	    	obj.setWire4x35(this.getWire4x35());
	    	obj.setPillarAvailable(this.getPillarAvailable());
	    	obj.setTmPillar60(this.getTmPillar60());
	    	obj.setTmPillar70(this.getTmPillar70());
	    	obj.setTmPillar75(this.getTmPillar75());
	    	obj.setTmPillar80(this.getTmPillar80());
	    	obj.setTmPillar85(this.getTmPillar85());
	    	obj.setTmPillar100(this.getTmPillar100());
	    	obj.setDesignDate(this.getDesignDate());
	    	obj.setDesignUpdateDate(this.getDesignUpdateDate());
	    	obj.setCreatedDate(this.getCreatedDate());
	    	obj.setUpdateDate(this.getUpdateDate());
	    	obj.setEstimatingDate(this.getEstimatingDate());
	    	obj.setDesignUserId(this.getDesignUserId());
	    	obj.setCreatedUser(this.getCreatedUser());
	    	obj.setUpdateUser(this.getUpdateUser());
	    	obj.setDesignUpdateUser(this.getDesignUpdateUser());
	    	obj.setCreatDesignEstimatesUser(this.getCreatDesignEstimatesUser());
	    	obj.setNode(this.getNode());
	    	obj.setDistrictName(this.getDistrictName());
	        return obj;
	    }

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}
}
