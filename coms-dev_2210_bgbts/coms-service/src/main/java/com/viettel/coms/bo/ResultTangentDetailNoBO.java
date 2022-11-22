package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ResultTangentDetailNoDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "RESULT_TANGENT_DETAIL_NO")
public class ResultTangentDetailNoBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RESULT_TANGENT_DETAIL_NO_SEQ") })
	@Column(name = "RESULT_TANGENT_DETAIL_NO_ID", length = 10)
	private Long resultTangentDetailNoId;
	@Column(name = "RESULT_TANGENT_ID", length = 10)
	private Long resultTangentId;
	@Column(name = "INFORMATION_1", length = 5)
	private String information1;
	@Column(name = "INFORMATION_21", length = 1000)
	private String information21;
	@Column(name = "INFORMATION_22", length = 1000)
	private String information22;
	@Column(name = "INFORMATION_23", length = 1000)
	private String information23;
	@Column(name = "INFORMATION_3", length = 5)
	private String information3;
	@Column(name = "INFORMATION_4", length = 5)
	private String information4;
	@Column(name = "INFORMATION_51", length = 10)
	private Double information51;
	@Column(name = "INFORMATION_52", length = 1000)
	private String information52;
	@Column(name = "INFORMATION_53", length = 5)
	private String information53;
	@Column(name = "INFORMATION_61", length = 20)
	private Long information61;
	@Column(name = "INFORMATION_62", length = 1000)
	private String information62;
	@Column(name = "INFORMATION_63", length = 1000)
	private String information63;
	@Column(name = "INFORMATION_64", length = 1000)
	private String information64;
	@Column(name = "INFORMATION_65", length = 20)
	private Long information65;
	@Column(name = "INFORMATION_71", length = 20)
	private Long information71;
	@Column(name = "INFORMATION_72", length = 5)
	private String information72;
	@Column(name = "INFORMATION_81", length = 5)
	private String information81;
	@Column(name = "INFORMATION_82", length = 1000)
	private String information82;
	@Column(name = "INFORMATION_83", length = 1000)
	private String information83;
	@Column(name = "INFORMATION_84", length = 1000)
	private String information84;
	@Column(name = "INFORMATION_85", length = 1000)
	private String information85;
	@Column(name = "INFORMATION_91", length = 5)
	private String information91;
	@Column(name = "INFORMATION_92", length = 5)
	private String information92;
	@Column(name = "INFORMATION_10", length = 5)
	private String information10;
	@Column(name = "INFORMATION_11", length = 5)
	private String information11;
	@Column(name = "INFORMATION_121", length = 10)
	private Double information121;
	@Column(name = "INFORMATION_122", length = 10)
	private Double information122;
	@Column(name = "INFORMATION_123", length = 10)
	private Double information123;
	@Column(name = "INFORMATION_131", length = 10)
	private Double information131;
	@Column(name = "INFORMATION_132", length = 10)
	private Double information132;
	@Column(name = "INFORMATION_133", length = 5)
	private String information133;
	@Column(name = "INFORMATION_134", length = 5)
	private String information134;
	@Column(name = "INFORMATION_135", length = 5)
	private String information135;
	@Column(name = "INFORMATION_14", length = 5)
	private String information14;
	@Column(name = "INFORMATION_151", length = 5)
	private String information151;
	@Column(name = "INFORMATION_152", length = 5)
	private String information152;
	@Column(name = "INFORMATION_153", length = 5)
	private String information153;
	@Column(name = "INFORMATION_154", length = 5)
	private String information154;
	@Column(name = "INFORMATION_161", length = 10)
	private Double information161;
	@Column(name = "INFORMATION_162", length = 10)
	private Double information162;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;

	public Long getResultTangentDetailNoId() {
		return resultTangentDetailNoId;
	}

	public void setResultTangentDetailNoId(Long resultTangentDetailNoId) {
		this.resultTangentDetailNoId = resultTangentDetailNoId;
	}

	public Long getResultTangentId() {
		return resultTangentId;
	}

	public void setResultTangentId(Long resultTangentId) {
		this.resultTangentId = resultTangentId;
	}

	public String getInformation1() {
		return information1;
	}

	public void setInformation1(String information1) {
		this.information1 = information1;
	}

	public String getInformation21() {
		return information21;
	}

	public void setInformation21(String information21) {
		this.information21 = information21;
	}

	public String getInformation22() {
		return information22;
	}

	public void setInformation22(String information22) {
		this.information22 = information22;
	}

	public String getInformation23() {
		return information23;
	}

	public void setInformation23(String information23) {
		this.information23 = information23;
	}

	public String getInformation3() {
		return information3;
	}

	public void setInformation3(String information3) {
		this.information3 = information3;
	}

	public String getInformation4() {
		return information4;
	}

	public void setInformation4(String information4) {
		this.information4 = information4;
	}

	public Double getInformation51() {
		return information51;
	}

	public void setInformation51(Double information51) {
		this.information51 = information51;
	}

	public String getInformation52() {
		return information52;
	}

	public void setInformation52(String information52) {
		this.information52 = information52;
	}

	public String getInformation53() {
		return information53;
	}

	public void setInformation53(String information53) {
		this.information53 = information53;
	}

	public Long getInformation61() {
		return information61;
	}

	public void setInformation61(Long information61) {
		this.information61 = information61;
	}

	public String getInformation62() {
		return information62;
	}

	public void setInformation62(String information62) {
		this.information62 = information62;
	}

	public String getInformation63() {
		return information63;
	}

	public void setInformation63(String information63) {
		this.information63 = information63;
	}

	public String getInformation64() {
		return information64;
	}

	public void setInformation64(String information64) {
		this.information64 = information64;
	}

	public Long getInformation65() {
		return information65;
	}

	public void setInformation65(Long information65) {
		this.information65 = information65;
	}

	public Long getInformation71() {
		return information71;
	}

	public void setInformation71(Long information71) {
		this.information71 = information71;
	}

	public String getInformation72() {
		return information72;
	}

	public void setInformation72(String information72) {
		this.information72 = information72;
	}

	public String getInformation81() {
		return information81;
	}

	public void setInformation81(String information81) {
		this.information81 = information81;
	}

	public String getInformation82() {
		return information82;
	}

	public void setInformation82(String information82) {
		this.information82 = information82;
	}

	public String getInformation83() {
		return information83;
	}

	public void setInformation83(String information83) {
		this.information83 = information83;
	}

	public String getInformation84() {
		return information84;
	}

	public void setInformation84(String information84) {
		this.information84 = information84;
	}

	public String getInformation85() {
		return information85;
	}

	public void setInformation85(String information85) {
		this.information85 = information85;
	}

	public String getInformation91() {
		return information91;
	}

	public void setInformation91(String information91) {
		this.information91 = information91;
	}

	public String getInformation92() {
		return information92;
	}

	public void setInformation92(String information92) {
		this.information92 = information92;
	}

	public String getInformation10() {
		return information10;
	}

	public void setInformation10(String information10) {
		this.information10 = information10;
	}

	public String getInformation11() {
		return information11;
	}

	public void setInformation11(String information11) {
		this.information11 = information11;
	}

	public Double getInformation121() {
		return information121;
	}

	public void setInformation121(Double information121) {
		this.information121 = information121;
	}

	public Double getInformation122() {
		return information122;
	}

	public void setInformation122(Double information122) {
		this.information122 = information122;
	}

	public Double getInformation123() {
		return information123;
	}

	public void setInformation123(Double information123) {
		this.information123 = information123;
	}

	public Double getInformation131() {
		return information131;
	}

	public void setInformation131(Double information131) {
		this.information131 = information131;
	}

	public Double getInformation132() {
		return information132;
	}

	public void setInformation132(Double information132) {
		this.information132 = information132;
	}

	public String getInformation133() {
		return information133;
	}

	public void setInformation133(String information133) {
		this.information133 = information133;
	}

	public String getInformation134() {
		return information134;
	}

	public void setInformation134(String information134) {
		this.information134 = information134;
	}

	public String getInformation135() {
		return information135;
	}

	public void setInformation135(String information135) {
		this.information135 = information135;
	}

	public String getInformation14() {
		return information14;
	}

	public void setInformation14(String information14) {
		this.information14 = information14;
	}

	public String getInformation151() {
		return information151;
	}

	public void setInformation151(String information151) {
		this.information151 = information151;
	}

	public String getInformation152() {
		return information152;
	}

	public void setInformation152(String information152) {
		this.information152 = information152;
	}

	public String getInformation153() {
		return information153;
	}

	public void setInformation153(String information153) {
		this.information153 = information153;
	}

	public String getInformation154() {
		return information154;
	}

	public void setInformation154(String information154) {
		this.information154 = information154;
	}

	public Double getInformation161() {
		return information161;
	}

	public void setInformation161(Double information161) {
		this.information161 = information161;
	}

	public Double getInformation162() {
		return information162;
	}

	public void setInformation162(Double information162) {
		this.information162 = information162;
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

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		ResultTangentDetailNoDTO dto = new ResultTangentDetailNoDTO();
		dto.setResultTangentDetailNoId(this.getResultTangentDetailNoId());
		dto.setResultTangentId(this.getResultTangentId());
		dto.setInformation1(this.getInformation1());
		dto.setInformation21(this.getInformation21());
		dto.setInformation22(this.getInformation22());
		dto.setInformation23(this.getInformation23());
		dto.setInformation3(this.getInformation3());
		dto.setInformation4(this.getInformation4());
		dto.setInformation51(this.getInformation51());
		dto.setInformation52(this.getInformation52());
		dto.setInformation53(this.getInformation53());
		dto.setInformation61(this.getInformation61());
		dto.setInformation62(this.getInformation62());
		dto.setInformation63(this.getInformation63());
		dto.setInformation64(this.getInformation64());
		dto.setInformation65(this.getInformation65());
		dto.setInformation71(this.getInformation71());
		dto.setInformation72(this.getInformation72());
		dto.setInformation81(this.getInformation81());
		dto.setInformation82(this.getInformation82());
		dto.setInformation83(this.getInformation83());
		dto.setInformation84(this.getInformation84());
		dto.setInformation85(this.getInformation85());
		dto.setInformation91(this.getInformation91());
		dto.setInformation92(this.getInformation92());
		dto.setInformation10(this.getInformation10());
		dto.setInformation11(this.getInformation11());
		dto.setInformation121(this.getInformation121());
		dto.setInformation122(this.getInformation122());
		dto.setInformation123(this.getInformation123());
		dto.setInformation131(this.getInformation131());
		dto.setInformation132(this.getInformation132());
		dto.setInformation133(this.getInformation133());
		dto.setInformation134(this.getInformation134());
		dto.setInformation135(this.getInformation135());
		dto.setInformation14(this.getInformation14());
		dto.setInformation151(this.getInformation151());
		dto.setInformation152(this.getInformation152());
		dto.setInformation153(this.getInformation153());
		dto.setInformation154(this.getInformation154());
		dto.setInformation161(this.getInformation161());
		dto.setInformation162(this.getInformation162());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.getUpdatedDate());
		return dto;
	}

}
