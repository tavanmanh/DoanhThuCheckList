package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ResultTangentDetailNoBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "RESULT_TANGENT_DETAIL_NOBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultTangentDetailNoDTO extends ComsBaseFWDTO<ResultTangentDetailNoBO> {

	private Long resultTangentDetailNoId;
	private Long resultTangentId;
	private String information1;
	private String information21;
	private String information22;
	private String information23;
	private String information3;
	private String information4;
	private Double information51;
	private String information52;
	private String information53;
	private Long information61;
	private String information62;
	private String information63;
	private String information64;
	private Long information65;
	private Long information71;
	private String information72;
	private String information81;
	private String information82;
	private String information83;
	private String information84;
	private String information85;
	private String information91;
	private String information92;
	private String information10;
	private String information11;
	private Double information121;
	private Double information122;
	private Double information123;
	private Double information131;
	private Double information132;
	private String information133;
	private String information134;
	private String information135;
	private String information14;
	private String information151;
	private String information152;
	private String information153;
	private String information154;
	private Double information161;
	private Double information162;
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private String createdDateDb;
	
	public String getCreatedDateDb() {
		return createdDateDb;
	}

	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}

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
	public String catchName() {
		// TODO Auto-generated method stub
		return resultTangentDetailNoId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return resultTangentDetailNoId;
	}

	@Override
	public ResultTangentDetailNoBO toModel() {
		// TODO Auto-generated method stub
		ResultTangentDetailNoBO bo = new ResultTangentDetailNoBO();
		bo.setResultTangentDetailNoId(this.getResultTangentDetailNoId());
		bo.setResultTangentId(this.getResultTangentId());
		bo.setInformation1(this.getInformation1());
		bo.setInformation21(this.getInformation21());
		bo.setInformation22(this.getInformation22());
		bo.setInformation23(this.getInformation23());
		bo.setInformation3(this.getInformation3());
		bo.setInformation4(this.getInformation4());
		bo.setInformation51(this.getInformation51());
		bo.setInformation52(this.getInformation52());
		bo.setInformation53(this.getInformation53());
		bo.setInformation61(this.getInformation61());
		bo.setInformation62(this.getInformation62());
		bo.setInformation63(this.getInformation63());
		bo.setInformation64(this.getInformation64());
		bo.setInformation65(this.getInformation65());
		bo.setInformation71(this.getInformation71());
		bo.setInformation72(this.getInformation72());
		bo.setInformation81(this.getInformation81());
		bo.setInformation82(this.getInformation82());
		bo.setInformation83(this.getInformation83());
		bo.setInformation84(this.getInformation84());
		bo.setInformation85(this.getInformation85());
		bo.setInformation91(this.getInformation91());
		bo.setInformation92(this.getInformation92());
		bo.setInformation10(this.getInformation10());
		bo.setInformation11(this.getInformation11());
		bo.setInformation121(this.getInformation121());
		bo.setInformation122(this.getInformation122());
		bo.setInformation123(this.getInformation123());
		bo.setInformation131(this.getInformation131());
		bo.setInformation132(this.getInformation132());
		bo.setInformation133(this.getInformation133());
		bo.setInformation134(this.getInformation134());
		bo.setInformation135(this.getInformation135());
		bo.setInformation14(this.getInformation14());
		bo.setInformation151(this.getInformation151());
		bo.setInformation152(this.getInformation152());
		bo.setInformation153(this.getInformation153());
		bo.setInformation154(this.getInformation154());
		bo.setInformation161(this.getInformation161());
		bo.setInformation162(this.getInformation162());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.getUpdatedDate());
		return bo;
	}

}
