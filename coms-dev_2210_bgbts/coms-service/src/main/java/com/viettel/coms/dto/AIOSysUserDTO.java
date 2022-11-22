package com.viettel.coms.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.AIOSysUserBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

//VietNT_20190917_create
@XmlRootElement(name = "SYS_USERBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AIOSysUserDTO extends ComsBaseFWDTO<AIOSysUserBO> {

    public static final Long STATUS_INACTIVE = new Long(0);
    public static final Long STATUS_WAIT_APPROVE = new Long(2);
    public static final Long STATUS_APPROVED = new Long(1);
    public static final Long STATUS_REJECTED = new Long(3);

    public AIOSysUserDTO() {
        sysGroupIds = new ArrayList<>();
    }

    private Long sysUserId;
    private String loginName;
    private String fullName;
    private String password;
    private String employeeCode;
    private String email;
    private String phoneNumber;
    private Long status;
    private Long newId;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date changePasswordDate;
    private Long needChangePassword;
    private Long sysGroupId;
    private String saleChannel;
    private Long parentUserId;
    private Long typeUser;
    private String address;
    private String taxCode;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;

    // dto
    private String sysUserName;
    private String passwordNew;
    private String sysGroupName;
    private String sysGroupLv2Code;
    private List<Long> sysGroupIds;
    private String parentName;
    private String parentPhone;
    private String contractCode;
    private String taxCodeUser;
    private String accountNumber;
    private String bank;
    private String bankBranch;
    private String occupation;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date userBirthday;
    private String cmt;
    private String token;
    private String fieldType;
	// Huypq-24052021-start
	private Long provinceIdCtvXddd;
	private String provinceNameCtvXddd;
	private String communeName;
	private String districtName;
	// huy-end

    public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	private String typeOption;

    public String getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(String typeOption) {
        this.typeOption = typeOption;
    }
    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getTaxCodeUser() {
        return taxCodeUser;
    }

    public void setTaxCodeUser(String taxCodeUser) {
        this.taxCodeUser = taxCodeUser;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public Date getChangePasswordDate() {
        return changePasswordDate;
    }

    public void setChangePasswordDate(Date changePasswordDate) {
        this.changePasswordDate = changePasswordDate;
    }

    public Long getNeedChangePassword() {
        return needChangePassword;
    }

    public void setNeedChangePassword(Long needChangePassword) {
        this.needChangePassword = needChangePassword;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Long getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(Long typeUser) {
        this.typeUser = typeUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public List<Long> getSysGroupIds() {
        return sysGroupIds;
    }

    public void setSysGroupIds(List<Long> sysGroupIds) {
        this.sysGroupIds = sysGroupIds;
    }

    public String getSysGroupLv2Code() {
        return sysGroupLv2Code;
    }

    public void setSysGroupLv2Code(String sysGroupLv2Code) {
        this.sysGroupLv2Code = sysGroupLv2Code;
    }

    @Override
    public AIOSysUserBO toModel() {
        AIOSysUserBO bo = new AIOSysUserBO();
        bo.setSysUserId(this.getSysUserId());
        bo.setLoginName(this.getLoginName());
        bo.setFullName(this.getFullName());
        bo.setPassword(this.getPassword());
        bo.setEmployeeCode(this.getEmployeeCode());
        bo.setEmail(this.getEmail());
        bo.setPhoneNumber(this.getPhoneNumber());
        bo.setStatus(this.getStatus());
        bo.setNewId(this.getNewId());
        bo.setChangePasswordDate(this.getChangePasswordDate());
        bo.setNeedChangePassword(this.getNeedChangePassword());
        bo.setSysGroupId(this.getSysGroupId());
        bo.setSaleChannel(this.getSaleChannel());
        bo.setParentUserId(this.getParentUserId());
        bo.setTypeUser(this.getTypeUser());
        bo.setAddress(this.getAddress());
        bo.setTaxCode(this.getTaxCode());
        bo.setCreatedDate(this.getCreatedDate());
        bo.setContractCode(this.getContractCode());
        bo.setTaxCodeUser(this.getTaxCodeUser());
        bo.setAccountNumber(this.getAccountNumber());
        bo.setBank(this.getBank());
        bo.setBankBranch(this.getBankBranch());
        bo.setOccupation(this.getOccupation());
        bo.setUserBirthday(this.getUserBirthday());
        bo.setCmt(this.getCmt());
        bo.setToken(this.getToken());
        bo.setProvinceIdXddd(this.provinceIdXddd);
        bo.setProvinceNameXddd(this.provinceNameXddd);
        bo.setFieldType(this.fieldType);
        bo.setProvinceIdCtvXddd(this.provinceIdCtvXddd);
        bo.setProvinceNameCtvXddd(this.provinceNameCtvXddd);
        bo.setCommuneName(this.communeName);
        bo.setDistrictName(this.districtName);
        return bo;
    }

    @Override
    public Long getFWModelId() {
        return sysUserId;
    }

    @Override
    public String catchName() {
        return sysUserId.toString();
    }
//    outsource_20200720_start
    private Long typeCheckUser;

	public Long getTypeCheckUser() {
		return typeCheckUser;
	}

	public void setTypeCheckUser(Long typeCheckUser) {
		this.typeCheckUser = typeCheckUser;
	}
//	outsource_20200720_end
	
	private List<UtilAttachDocumentDTO> listImageCmt;
	private List<UtilAttachDocumentDTO> listImageHk;
	private List<UtilAttachDocumentDTO> listImageHd;

	public List<UtilAttachDocumentDTO> getListImageCmt() {
		return listImageCmt;
	}

	public void setListImageCmt(List<UtilAttachDocumentDTO> listImageCmt) {
		this.listImageCmt = listImageCmt;
	}

	public List<UtilAttachDocumentDTO> getListImageHk() {
		return listImageHk;
	}

	public void setListImageHk(List<UtilAttachDocumentDTO> listImageHk) {
		this.listImageHk = listImageHk;
	}

	public List<UtilAttachDocumentDTO> getListImageHd() {
		return listImageHd;
	}

	public void setListImageHd(List<UtilAttachDocumentDTO> listImageHd) {
		this.listImageHd = listImageHd;
	}
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	private Long provinceIdXddd;
    private String provinceNameXddd;

	public Long getProvinceIdXddd() {
		return provinceIdXddd;
	}

	public void setProvinceIdXddd(Long provinceIdXddd) {
		this.provinceIdXddd = provinceIdXddd;
	}

	public String getProvinceNameXddd() {
		return provinceNameXddd;
	}

	public void setProvinceNameXddd(String provinceNameXddd) {
		this.provinceNameXddd = provinceNameXddd;
	}
    
	private String createdDateDb;

	public String getCreatedDateDb() {
		return createdDateDb;
	}

	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}

	public Long getProvinceIdCtvXddd() {
		return provinceIdCtvXddd;
	}

	public void setProvinceIdCtvXddd(Long provinceIdCtvXddd) {
		this.provinceIdCtvXddd = provinceIdCtvXddd;
	}

	public String getProvinceNameCtvXddd() {
		return provinceNameCtvXddd;
	}

	public void setProvinceNameCtvXddd(String provinceNameCtvXddd) {
		this.provinceNameCtvXddd = provinceNameCtvXddd;
	}

	public String getCommuneName() {
		return communeName;
	}

	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
}
