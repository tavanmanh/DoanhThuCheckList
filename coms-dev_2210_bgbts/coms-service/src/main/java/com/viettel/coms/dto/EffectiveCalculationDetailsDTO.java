package com.viettel.coms.dto;

import java.util.Date;

import javax.persistence.Column;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.EffectiveCalculationDetailsBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectiveCalculationDetailsDTO  extends ComsBaseFWDTO<EffectiveCalculationDetailsBO>{

	private Long effectiveCalculationDetailsId;
	private String areaCode;
	private String provinceCode;
	private String stationCodeVtn;
	private String stationCodeVcc;
	private String address;
	private String stationType;
	private String maiDat;
	private Double doCaoCot;
	private String loaiCot;
	private String mongCo;
	private String loaiNha;
	private String tiepDia;
	private String dienCnkt;
	private String soCotDien;
	private String vanChuyenBo;
	private String thueAcquy;
	private Long giaThueMbThucTe;
	private Long giaThueMbTheoDinhMuc;
	private Long capexCot;
	private Long capexTiepDia;
	private Long capexAc;
	private Long capexPhongMay;
	private Long tongCapexHt;
	private Double vccChaoGiaHt;
	private Double vccChaoGiaAcquy;
	private Long tongCong;
	private String description;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date ngayGui;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date ngayHoanThanh;
	private String nguoiLap;
	private Long capexHtVcc;
	private Long npv;
	private Double irr;
	private Double thoiGianHv;
	private Double lnstDt;
	private String conclude;
	private Long woId;
	private String status;
	private String woCode;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date fromDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date toDate;
	private Long constructionId;
	private Long contractId;
	private String npvString;
	private String irrString;
	private String thoiGianHvString;
	private String lnstDtString;
	private String capexTruocVatString;
	
	public Long getEffectiveCalculationDetailsId() {
		return effectiveCalculationDetailsId;
	}

	public void setEffectiveCalculationDetailsId(Long effectiveCalculationDetailsId) {
		this.effectiveCalculationDetailsId = effectiveCalculationDetailsId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getStationCodeVtn() {
		return stationCodeVtn;
	}

	public void setStationCodeVtn(String stationCodeVtn) {
		this.stationCodeVtn = stationCodeVtn;
	}

	public String getStationCodeVcc() {
		return stationCodeVcc;
	}

	public void setStationCodeVcc(String stationCodeVcc) {
		this.stationCodeVcc = stationCodeVcc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getMaiDat() {
		return maiDat;
	}

	public void setMaiDat(String maiDat) {
		this.maiDat = maiDat;
	}

	public Double getDoCaoCot() {
		return doCaoCot;
	}

	public void setDoCaoCot(Double doCaoCot) {
		this.doCaoCot = doCaoCot;
	}

	public String getLoaiCot() {
		return loaiCot;
	}

	public void setLoaiCot(String loaiCot) {
		this.loaiCot = loaiCot;
	}

	public String getMongCo() {
		return mongCo;
	}

	public void setMongCo(String mongCo) {
		this.mongCo = mongCo;
	}

	public String getLoaiNha() {
		return loaiNha;
	}

	public void setLoaiNha(String loaiNha) {
		this.loaiNha = loaiNha;
	}

	public String getTiepDia() {
		return tiepDia;
	}

	public void setTiepDia(String tiepDia) {
		this.tiepDia = tiepDia;
	}

	public String getDienCnkt() {
		return dienCnkt;
	}

	public void setDienCnkt(String dienCnkt) {
		this.dienCnkt = dienCnkt;
	}

	public String getSoCotDien() {
		return soCotDien;
	}

	public void setSoCotDien(String soCotDien) {
		this.soCotDien = soCotDien;
	}

	public String getVanChuyenBo() {
		return vanChuyenBo;
	}

	public void setVanChuyenBo(String vanChuyenBo) {
		this.vanChuyenBo = vanChuyenBo;
	}

	public String getThueAcquy() {
		return thueAcquy;
	}

	public void setThueAcquy(String thueAcquy) {
		this.thueAcquy = thueAcquy;
	}

	public Long getGiaThueMbThucTe() {
		return giaThueMbThucTe;
	}

	public void setGiaThueMbThucTe(Long giaThueMbThucTe) {
		this.giaThueMbThucTe = giaThueMbThucTe;
	}

	public Long getGiaThueMbTheoDinhMuc() {
		return giaThueMbTheoDinhMuc;
	}

	public void setGiaThueMbTheoDinhMuc(Long giaThueMbTheoDinhMuc) {
		this.giaThueMbTheoDinhMuc = giaThueMbTheoDinhMuc;
	}

	public Long getCapexCot() {
		return capexCot;
	}

	public void setCapexCot(Long capexCot) {
		this.capexCot = capexCot;
	}

	public Long getCapexTiepDia() {
		return capexTiepDia;
	}

	public void setCapexTiepDia(Long capexTiepDia) {
		this.capexTiepDia = capexTiepDia;
	}

	public Long getCapexAc() {
		return capexAc;
	}

	public void setCapexAc(Long capexAc) {
		this.capexAc = capexAc;
	}

	public Long getCapexPhongMay() {
		return capexPhongMay;
	}

	public void setCapexPhongMay(Long capexPhongMay) {
		this.capexPhongMay = capexPhongMay;
	}

	public Long getTongCapexHt() {
		return tongCapexHt;
	}

	public void setTongCapexHt(Long tongCapexHt) {
		this.tongCapexHt = tongCapexHt;
	}
	
	public Long getTongCong() {
		return tongCong;
	}

	public void setTongCong(Long tongCong) {
		this.tongCong = tongCong;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getNgayGui() {
		return ngayGui;
	}

	public void setNgayGui(Date ngayGui) {
		this.ngayGui = ngayGui;
	}

	public Date getNgayHoanThanh() {
		return ngayHoanThanh;
	}

	public void setNgayHoanThanh(Date ngayHoanThanh) {
		this.ngayHoanThanh = ngayHoanThanh;
	}

	public String getNguoiLap() {
		return nguoiLap;
	}

	public void setNguoiLap(String nguoiLap) {
		this.nguoiLap = nguoiLap;
	}

	public Long getCapexHtVcc() {
		return capexHtVcc;
	}

	public void setCapexHtVcc(Long capexHtVcc) {
		this.capexHtVcc = capexHtVcc;
	}

	public Long getNpv() {
		return npv;
	}

	public void setNpv(Long npv) {
		this.npv = npv;
	}

	public Double getIrr() {
		return irr;
	}

	public void setIrr(Double irr) {
		this.irr = irr;
	}

	public Double getThoiGianHv() {
		return thoiGianHv;
	}

	public void setThoiGianHv(Double thoiGianHv) {
		this.thoiGianHv = thoiGianHv;
	}

	public Double getLnstDt() {
		return lnstDt;
	}

	public void setLnstDt(Double lnstDt) {
		this.lnstDt = lnstDt;
	}

	public String getConclude() {
		return conclude;
	}

	public void setConclude(String conclude) {
		this.conclude = conclude;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getNpvString() {
		return npvString;
	}

	public void setNpvString(String npvString) {
		this.npvString = npvString;
	}

	public String getIrrString() {
		return irrString;
	}

	public void setIrrString(String irrString) {
		this.irrString = irrString;
	}

	public String getThoiGianHvString() {
		return thoiGianHvString;
	}

	public void setThoiGianHvString(String thoiGianHvString) {
		this.thoiGianHvString = thoiGianHvString;
	}

	public String getLnstDtString() {
		return lnstDtString;
	}

	public void setLnstDtString(String lnstDtString) {
		this.lnstDtString = lnstDtString;
	}

	public String getCapexTruocVatString() {
		return capexTruocVatString;
	}

	public void setCapexTruocVatString(String capexTruocVatString) {
		this.capexTruocVatString = capexTruocVatString;
	}

	public Double getVccChaoGiaHt() {
		return vccChaoGiaHt;
	}

	public void setVccChaoGiaHt(Double vccChaoGiaHt) {
		this.vccChaoGiaHt = vccChaoGiaHt;
	}

	public Double getVccChaoGiaAcquy() {
		return vccChaoGiaAcquy;
	}

	public void setVccChaoGiaAcquy(Double vccChaoGiaAcquy) {
		this.vccChaoGiaAcquy = vccChaoGiaAcquy;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return getEffectiveCalculationDetailsId().toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return getEffectiveCalculationDetailsId();
	}

	@Override
	public EffectiveCalculationDetailsBO toModel() {
		EffectiveCalculationDetailsBO bo = new EffectiveCalculationDetailsBO();
		bo.setEffectiveCalculationDetailsId(this.getEffectiveCalculationDetailsId());
		bo.setAreaCode(this.getAreaCode());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setStationCodeVtn(this.getStationCodeVtn());
		bo.setStationCodeVcc(this.getStationCodeVcc());
		bo.setAddress(this.getAddress());
		bo.setStationType(this.getStationType());
		bo.setMaiDat(this.getMaiDat());
		bo.setDoCaoCot(this.getDoCaoCot());
		bo.setLoaiCot(this.getLoaiCot());
		bo.setMongCo(this.getMongCo());
		bo.setLoaiNha(this.getLoaiNha());
		bo.setTiepDia(this.getTiepDia());
		bo.setDienCnkt(this.getDienCnkt());
		bo.setSoCotDien(this.getSoCotDien());
		bo.setVanChuyenBo(this.getVanChuyenBo());
		bo.setThueAcquy(this.getThueAcquy());
		bo.setGiaThueMbThucTe(this.getGiaThueMbThucTe());
		bo.setGiaThueMbTheoDinhMuc(this.getGiaThueMbTheoDinhMuc());
		bo.setCapexCot(this.getCapexCot());
		bo.setCapexTiepDia(this.getCapexTiepDia());
		bo.setCapexAc(this.getCapexAc());
		bo.setCapexPhongMay(this.getCapexPhongMay());
		bo.setTongCapexHt(this.getTongCapexHt());
		bo.setVccChaoGiaHt(this.getVccChaoGiaHt());
		bo.setVccChaoGiaAcquy(this.getVccChaoGiaAcquy());
		bo.setTongCong(this.getTongCong());
		bo.setDescription(this.getDescription());
		bo.setNgayGui(this.getNgayGui());
		bo.setNgayHoanThanh(this.getNgayHoanThanh());
		bo.setNguoiLap(this.getNguoiLap());
		bo.setCapexHtVcc(this.getCapexHtVcc());
		bo.setNpv(this.getNpv());
		bo.setIrr(this.getIrr());
		bo.setThoiGianHv(this.getThoiGianHv());
		bo.setLnstDt(this.getLnstDt());
		bo.setConclude(this.getConclude());
		bo.setWoId(this.woId);
		bo.setStatus(this.status);
		bo.setWoCode(this.woCode);
		bo.setNpvString(this.getNpvString());
		bo.setIrrString(this.getIrrString());
		bo.setThoiGianHvString(this.getThoiGianHvString());
		bo.setLnstDtString(this.getLnstDtString());
		bo.setCapexTruocVatString(this.capexTruocVatString);
		return bo;
	}

}
