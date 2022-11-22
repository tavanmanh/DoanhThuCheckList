package com.viettel.coms.bo;

import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ManageUsedMaterialBO")
@Table(name = "MANAGE_USED_MATERIAL")
/**
 *
 * @author: tatph1
 */
public class ManageUsedMaterialBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "MANAGE_USED_MATERIAL_SEQ") })
	@Column(name = "MANAGE_USED_MATERIAL_ID")
	private Long manageUsedMaterialId;
	@Column(name = "CONSTRUCTION_ID", length = 200)
	private Long constructionId;
	@Column(name = "CONSTRUCTION_CODE", length = 200)
	private String constructionCode;
	@Column(name = "CONSTRUCTION_NAME", length = 200)
	private String constructionName;
	@Column(name = "CONTRACT_CODE", length = 200 )
	private String contractCode;
	@Column(name = "CONTRACT_NAME", length = 200 )
	private String contractName;
	@Column(name = "PROVINCE_CODE", length = 200 )
	private String provinceCode;
	@Column(name = "PROVINCE_NAME", length = 200 )
	private String provinceName;
	@Column(name = "PXK_CODE", length = 200 )
	private String pxkCode;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "UPDATE_DATE", length = 22)
	private Date updateDate;
	@Column(name = "CREATE_USER_ID", length = 20 )
	private String createUserId;
	@Column(name = "UPDATE_USER_ID", length = 20 )
	private String updateUserId;
	@Column(name = "PXK_DATE", length = 22)
	private Date pxkDate;
	@Column(name = "PXK_NAME", length = 200 )
	private String pxkName;
	@Column(name = "VTTB_NAME", length = 200 )
	private String vttbName;
	@Column(name = "VTTB_CODE", length = 200 )
	private String vttbCode;
	@Column(name = "SO_LUONG_SU_DUNG")
	private Long soLuongSuDung;
	@Column(name = "GIA_TRI_SU_DUNG")
	private Long giaTriSuDung;
	
    public Long getConstructionId() {
		return constructionId;
	}



	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
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



	public Date getPxkDate() {
		return pxkDate;
	}



	public void setPxkDate(Date pxkDate) {
		this.pxkDate = pxkDate;
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
	


	public Long getManageUsedMaterialId() {
		return manageUsedMaterialId;
	}



	public void setManageUsedMaterialId(Long manageUsedMaterialId) {
		this.manageUsedMaterialId = manageUsedMaterialId;
	}



	@Override
    public ManageUsedMaterialDTO toDTO() {
		ManageUsedMaterialDTO manageVttbDTO = new ManageUsedMaterialDTO(); 
        manageVttbDTO.setManageUsedMaterialId(this.getManageUsedMaterialId());
        manageVttbDTO.setConstructionCode(this.getConstructionCode());
        manageVttbDTO.setConstructionName(this.getConstructionName());
        manageVttbDTO.setContractCode(this.getContractCode());
        manageVttbDTO.setContractName(this.getContractName());
        manageVttbDTO.setProvinceCode(this.getProvinceCode());
        manageVttbDTO.setProvinceName(this.getProvinceName());
        manageVttbDTO.setPxkCode(this.getPxkCode());
        manageVttbDTO.setCreateDate(this.getCreateDate());
        manageVttbDTO.setUpdateDate(this.getUpdateDate());
        manageVttbDTO.setCreateUserId(this.getCreateUserId());
        manageVttbDTO.setUpdateUserId(this.getUpdateUserId());
        manageVttbDTO.setPxkName(this.getPxkName());
        manageVttbDTO.setPxkDate(this.getPxkDate());
        manageVttbDTO.setVttbName(this.getVttbName());
        manageVttbDTO.setVttbCode(this.getVttbCode());
        manageVttbDTO.setConstructionId(this.getConstructionId());
        manageVttbDTO.setSoLuongSuDung(this.getSoLuongSuDung());
        manageVttbDTO.setGiaTriSuDung(this.getGiaTriSuDung());
        return manageVttbDTO;
    }
}
