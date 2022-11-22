package com.viettel.coms.dto;

import java.util.List;

import com.viettel.service.base.model.BaseFWModelImpl;

public class StationConstructionOverviewDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private java.lang.Long sumTaoMa;
	private java.lang.Long sumThueMB;
	private java.lang.Long sumKC;
	private java.lang.Long sumDB;
	private java.lang.Long sumPS;
	private java.lang.Long sumDT;
	
	private java.lang.String projectCode;
	private java.lang.String projectName;
	
	private java.lang.Long allTaoMa;
	private java.lang.Long allThueMB;
	private java.lang.Long allKC;
	private java.lang.Long allDB;
	private java.lang.Long allPS;
	private java.lang.Long allDT;
	
	private List<StationConstructionDTO> listStation;
	
	public java.lang.Long getSumTaoMa() {
		return sumTaoMa;
	}
	public void setSumTaoMa(java.lang.Long sumTaoMa) {
		this.sumTaoMa = sumTaoMa;
	}
	public java.lang.Long getSumThueMB() {
		return sumThueMB;
	}
	public void setSumThueMB(java.lang.Long sumThueMB) {
		this.sumThueMB = sumThueMB;
	}
	public java.lang.Long getSumKC() {
		return sumKC;
	}
	public void setSumKC(java.lang.Long sumKC) {
		this.sumKC = sumKC;
	}
	public java.lang.Long getSumDB() {
		return sumDB;
	}
	public void setSumDB(java.lang.Long sumDB) {
		this.sumDB = sumDB;
	}
	public java.lang.Long getSumPS() {
		return sumPS;
	}
	public void setSumPS(java.lang.Long sumPS) {
		this.sumPS = sumPS;
	}
	public java.lang.Long getSumDT() {
		return sumDT;
	}
	public void setSumDT(java.lang.Long sumDT) {
		this.sumDT = sumDT;
	}
	public java.lang.String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(java.lang.String projectCode) {
		this.projectCode = projectCode;
	}
	public java.lang.String getProjectName() {
		return projectName;
	}
	public void setProjectName(java.lang.String projectName) {
		this.projectName = projectName;
	}
	
	public List<StationConstructionDTO> getListStation() {
		return listStation;
	}
	public void setListStation(List<StationConstructionDTO> listStation) {
		this.listStation = listStation;
	}
	
	public java.lang.Long getAllTaoMa() {
		return allTaoMa;
	}
	public void setAllTaoMa(java.lang.Long allTaoMa) {
		this.allTaoMa = allTaoMa;
	}
	public java.lang.Long getAllThueMB() {
		return allThueMB;
	}
	public void setAllThueMB(java.lang.Long allThueMB) {
		this.allThueMB = allThueMB;
	}
	public java.lang.Long getAllKC() {
		return allKC;
	}
	public void setAllKC(java.lang.Long allKC) {
		this.allKC = allKC;
	}
	public java.lang.Long getAllDB() {
		return allDB;
	}
	public void setAllDB(java.lang.Long allDB) {
		this.allDB = allDB;
	}
	public java.lang.Long getAllPS() {
		return allPS;
	}
	public void setAllPS(java.lang.Long allPS) {
		this.allPS = allPS;
	}
	public java.lang.Long getAllDT() {
		return allDT;
	}
	public void setAllDT(java.lang.Long allDT) {
		this.allDT = allDT;
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
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
