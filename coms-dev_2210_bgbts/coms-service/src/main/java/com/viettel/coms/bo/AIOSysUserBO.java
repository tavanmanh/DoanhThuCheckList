package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.AIOSysUserDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

//VietNT_20190917_start
@Entity
@Table(name = "SYS_USER")
public class AIOSysUserBO extends BaseFWModelImpl {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence", value = "SYS_USER_SEQ")})
    @Column(name = "SYS_USER_ID", length = 10)
    private Long sysUserId;
    @Column(name = "LOGIN_NAME", length = 50)
    private String loginName;
    @Column(name = "FULL_NAME", length = 200)
    private String fullName;
    @Column(name = "PASSWORD", length = 200)
    private String password;
    @Column(name = "EMPLOYEE_CODE", length = 50)
    private String employeeCode;
    @Column(name = "EMAIL", length = 50)
    private String email;
    @Column(name = "PHONE_NUMBER", length = 50)
    private String phoneNumber;
    @Column(name = "STATUS", length = 10)
    private Long status;
    @Column(name = "NEW_ID", length = 10)
    private Long newId;
    @Column(name = "CHANGE_PASSWORD_DATE", length = 22)
    private Date changePasswordDate;
    @Column(name = "NEED_CHANGE_PASSWORD", length = 1)
    private Long needChangePassword;
    @Column(name = "SYS_GROUP_ID", length = 18)
    private Long sysGroupId;
    @Column(name = "SALE_CHANNEL", length = 20)
    private String saleChannel;
    @Column(name = "PARENT_USER_ID", length = 10)
    private Long parentUserId;
    @Column(name = "TYPE_USER", length = 1)
    private Long typeUser;
    @Column(name = "ADDRESS", length = 1000)
    private String address;
    @Column(name = "TAX_CODE", length = 50)
    private String taxCode;
    @Column(name = "CONTRACT_CODE", length = 50)
    private String contractCode;
    @Column(name = "TAX_CODE_USER", length = 50)
    private String taxCodeUser;
    @Column(name = "ACCOUNT_NUMBER", length = 50)
    private String accountNumber;
    @Column(name = "BANK", length = 50)
    private String bank;
    @Column(name = "BANK_BRANCH", length = 50)
    private String bankBranch;
    @Column(name = "CREATED_DATE", length = 22)
    private Date createdDate;
    @Column(name = "OCCUPATION", length = 500)
    private String occupation;
    @Column(name = "USER_BIRTHDAY")
    private Date userBirthday;
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "FIELD_TYPE")
    private String fieldType;

    public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "CMT")
    private String cmt;
    @Column(name = "PROVINCE_ID_XDDD")
    private Long provinceIdXddd;
    @Column(name = "PROVINCE_NAME_XDDD")
    private String provinceNameXddd;
    
    //Huypq-24052021-start
    @Column(name = "PROVINCE_ID_CTV_XDDD")
    private Long provinceIdCtvXddd;
    @Column(name = "PROVINCE_NAME_CTV_XDDD")
    private String provinceNameCtvXddd;
    @Column(name = "COMMUNE_NAME")
    private String communeName;
    @Column(name = "DISTRICT_NAME")
    private String districtName;
    //huy-end
    

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

	public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
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

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
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

	@Override
    public BaseFWDTOImpl toDTO() {
        AIOSysUserDTO dto = new AIOSysUserDTO();
        dto.setSysUserId(this.getSysUserId());
        dto.setLoginName(this.getLoginName());
        dto.setFullName(this.getFullName());
        dto.setPassword(this.getPassword());
        dto.setEmployeeCode(this.getEmployeeCode());
        dto.setEmail(this.getEmail());
        dto.setPhoneNumber(this.getPhoneNumber());
        dto.setStatus(this.getStatus());
        dto.setNewId(this.getNewId());
        dto.setChangePasswordDate(this.getChangePasswordDate());
        dto.setNeedChangePassword(this.getNeedChangePassword());
        dto.setSysGroupId(this.getSysGroupId());
        dto.setSaleChannel(this.getSaleChannel());
        dto.setParentUserId(this.getParentUserId());
        dto.setTypeUser(this.getTypeUser());
        dto.setAddress(this.getAddress());
        dto.setTaxCode(this.getTaxCode());
        dto.setOccupation(this.getOccupation());
        dto.setUserBirthday(this.getUserBirthday());
        dto.setCmt(this.getCmt());
        dto.setToken(this.getToken());
        dto.setProvinceIdXddd(this.provinceIdXddd);
        dto.setProvinceNameXddd(this.provinceNameXddd);
        dto.setFieldType(this.fieldType);
        dto.setProvinceIdCtvXddd(this.provinceIdCtvXddd);
        dto.setProvinceNameCtvXddd(this.provinceNameCtvXddd);
        dto.setCommuneName(this.communeName);
        dto.setDistrictName(this.districtName);
        return dto;
    }
}
