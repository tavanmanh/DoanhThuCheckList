package com.viettel.coms.dto;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.cat.dto.ConstructionImageInfo;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ConstructionTaskDTOResponse {

    private List<ConstructionImageInfo> listImage;
    private List<ConstructionTaskDTO> lstConstrucitonTask;
    private ResultInfo resultInfo;
    private List<ConstructioIocDTO> lstConstruction;
    private List<ConstructionTotalValueDTO> lstConstructionTotalValue;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    public List<ConstructionImageInfo> getListImage() {
        return listImage;
    }

    public void setListImage(List<ConstructionImageInfo> listImage) {
        this.listImage = listImage;
    }

    public List<ConstructionTaskDTO> getLstConstrucitonTask() {
        return lstConstrucitonTask;
    }

    public void setLstConstrucitonTask(List<ConstructionTaskDTO> lstConstrucitonTask) {
        this.lstConstrucitonTask = lstConstrucitonTask;
    }

	public List<ConstructioIocDTO> getLstConstruction() {
		return lstConstruction;
	}

	public void setLstConstruction(List<ConstructioIocDTO> lstConstruction) {
		this.lstConstruction = lstConstruction;
	}

	public List<ConstructionTotalValueDTO> getLstConstructionTotalValue() {
		return lstConstructionTotalValue;
	}

	public void setLstConstructionTotalValue(List<ConstructionTotalValueDTO> lstConstructionTotalValue) {
		this.lstConstructionTotalValue = lstConstructionTotalValue;
	}

}
