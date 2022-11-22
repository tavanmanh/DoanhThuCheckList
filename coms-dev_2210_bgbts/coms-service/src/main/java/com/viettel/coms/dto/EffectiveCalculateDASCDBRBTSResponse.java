package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;

import java.util.List;

public class EffectiveCalculateDASCDBRBTSResponse {
    private ResultInfo resultInfo;
    private List<EffectiveCalculateDASCDBRDTO> lstEffectiveCalculateDASCDBRDTO;
    private EffectiveCalculateDASCDBRDTO effectiveCalculateDASCDBRDTO;
    private List<EffectiveCalculateBTSDTO> lstEffectiveCalculateBTSDTO;
    private EffectiveCalculateBTSDTO effectiveCalculateBTSDTO;
    
    public List<EffectiveCalculateBTSDTO> getLstEffectiveCalculateBTSDTO() {
		return lstEffectiveCalculateBTSDTO;
	}

	public void setLstEffectiveCalculateBTSDTO(
			List<EffectiveCalculateBTSDTO> lstEffectiveCalculateBTSDTO) {
		this.lstEffectiveCalculateBTSDTO = lstEffectiveCalculateBTSDTO;
	}

	public EffectiveCalculateBTSDTO getEffectiveCalculateBTSDTO() {
		return effectiveCalculateBTSDTO;
	}

	public void setEffectiveCalculateBTSDTO(
			EffectiveCalculateBTSDTO effectiveCalculateBTSDTO) {
		this.effectiveCalculateBTSDTO = effectiveCalculateBTSDTO;
	}

	public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

	public List<EffectiveCalculateDASCDBRDTO> getLstEffectiveCalculateDASCDBRDTO() {
		return lstEffectiveCalculateDASCDBRDTO;
	}

	public void setLstEffectiveCalculateDASCDBRDTO(
			List<EffectiveCalculateDASCDBRDTO> lstEffectiveCalculateDASCDBRDTO) {
		this.lstEffectiveCalculateDASCDBRDTO = lstEffectiveCalculateDASCDBRDTO;
	}

	public EffectiveCalculateDASCDBRDTO getEffectiveCalculateDASCDBRDTO() {
		return effectiveCalculateDASCDBRDTO;
	}

	public void setEffectiveCalculateDASCDBRDTO(
			EffectiveCalculateDASCDBRDTO effectiveCalculateDASCDBRDTO) {
		this.effectiveCalculateDASCDBRDTO = effectiveCalculateDASCDBRDTO;
	}

}
