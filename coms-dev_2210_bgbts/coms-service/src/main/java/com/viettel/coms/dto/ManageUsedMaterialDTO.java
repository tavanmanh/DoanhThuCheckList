package com.viettel.coms.dto;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.coms.bo.ManageUsedMaterialBO;
import com.viettel.coms.bo.ManageVttbBO;

/**
 *
 * @author tatph1
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "MANAGE_USED_MATERIALBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageUsedMaterialDTO extends ComsBaseFWDTO<ManageUsedMaterialBO> {

	
	private Long manageUsedMaterialId;
	private String constructionCode;
	private String constructionName;
	private Long constructionId;
	private String contractCode;
	private String contractName;
	private String provinceCode;
	private String provinceName;
	private String pxkCode;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updateDate;
	private String createUserId;
	private String updateUserId;
	private String pxkName;
	private Date pxkDate;
	private String pxkDateS;
	private String vttbName;
	private String vttbCode;
	
	private Long soLuongPxk;
	private Long giaTriPxk;
	private Long soLuongDuThua;
	private Long giaTriDuThua;
	private Long soLuongSuDung;
	private Long giaTriSuDung;
	private Long soLuongThuHoi;
	private Long giaTriThuHoi;
	private Long soPhieu;
	private String description;

    @Override
    public ManageUsedMaterialBO toModel() {
    	ManageUsedMaterialBO manageVttbBO = new ManageUsedMaterialBO();
    	  manageVttbBO.setManageUsedMaterialId(this.getManageUsedMaterialId());
          manageVttbBO.setConstructionCode(this.getConstructionCode());
          manageVttbBO.setConstructionName(this.getConstructionName());
          manageVttbBO.setContractCode(this.getContractCode());
          manageVttbBO.setContractName(this.getContractName());
          manageVttbBO.setProvinceCode(this.getProvinceCode());
          manageVttbBO.setProvinceName(this.getProvinceName());
          manageVttbBO.setPxkCode(this.getPxkCode());
          manageVttbBO.setCreateDate(this.getCreateDate());
          manageVttbBO.setUpdateDate(this.getUpdateDate());
          manageVttbBO.setCreateUserId(this.getCreateUserId());
          manageVttbBO.setUpdateUserId(this.getUpdateUserId());
          manageVttbBO.setPxkName(this.getPxkName());
          manageVttbBO.setPxkDate(this.getPxkDate());
          manageVttbBO.setVttbName(this.getVttbName());
          manageVttbBO.setVttbCode(this.getVttbCode());
          manageVttbBO.setConstructionId(this.getConstructionId());
          manageVttbBO.setSoLuongSuDung(this.getSoLuongSuDung());
          manageVttbBO.setGiaTriSuDung(this.getGiaTriSuDung());
        return manageVttbBO;
    }

    
	public Long getSoLuongThuHoi() {
		return soLuongThuHoi;
	}


	public void setSoLuongThuHoi(Long soLuongThuHoi) {
		this.soLuongThuHoi = soLuongThuHoi;
	}


	public Long getGiaTriThuHoi() {
		return giaTriThuHoi;
	}


	public void setGiaTriThuHoi(Long giaTriThuHoi) {
		this.giaTriThuHoi = giaTriThuHoi;
	}


	public Long getConstructionId() {
		return constructionId;
	}


	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}


	public Long getSoPhieu() {
		return soPhieu;
	}


	public void setSoPhieu(Long soPhieu) {
		this.soPhieu = soPhieu;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getSoLuongPxk() {
		return soLuongPxk;
	}


	public void setSoLuongPxk(Long soLuongPxk) {
		this.soLuongPxk = soLuongPxk;
	}


	public Long getGiaTriPxk() {
		return giaTriPxk;
	}


	public void setGiaTriPxk(Long giaTriPxk) {
		this.giaTriPxk = giaTriPxk;
	}


	public Long getSoLuongDuThua() {
		return soLuongDuThua;
	}


	public void setSoLuongDuThua(Long soLuongDuThua) {
		this.soLuongDuThua = soLuongDuThua;
	}


	public Long getGiaTriDuThua() {
		return giaTriDuThua;
	}


	public void setGiaTriDuThua(Long giaTriDuThua) {
		this.giaTriDuThua = giaTriDuThua;
	}


	public Long getSoLuongSuDung() {
		return soLuongSuDung;
	}


	public void setSoLuongSuDung(Long soLuongSuDung) {
		this.soLuongSuDung = soLuongSuDung;
	}


	public Long getGiaTriSuDung() {
		return giaTriSuDung;
	}


	public void setGiaTriSuDung(Long giaTriSuDung) {
		this.giaTriSuDung = giaTriSuDung;
	}


	public String getPxkDateS() {
		return pxkDateS;
	}


	public void setPxkDateS(String pxkDateS) {
		this.pxkDateS = pxkDateS;
	}


	public Date getPxkDate() {
		return pxkDate;
	}


	public void setPxkDate(Date pxkDate) {
		this.pxkDate = pxkDate;
	}


	

	public Long getManageUsedMaterialId() {
		return manageUsedMaterialId;
	}


	public void setManageUsedMaterialId(Long manageUsedMaterialId) {
		this.manageUsedMaterialId = manageUsedMaterialId;
	}


	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getConstructionName() {
		return constructionName;
	}

	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}


	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}


	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getPxkCode() {
		return pxkCode;
	}

	public void setPxkCode(String pxkCode) {
		this.pxkCode = pxkCode;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getPxkName() {
		return pxkName;
	}

	public void setPxkName(String pxkName) {
		this.pxkName = pxkName;
	}

	public String getVttbName() {
		return vttbName;
	}

	public void setVttbName(String vttbName) {
		this.vttbName = vttbName;
	}

	public String getVttbCode() {
		return vttbCode;
	}

	public void setVttbCode(String vttbCode) {
		this.vttbCode = vttbCode;
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
    
}
