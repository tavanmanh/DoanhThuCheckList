package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.service.base.dto.DataListDTO;

import java.util.List;

public class StockTransResponse {
    private ResultInfo resultInfo;
    private CountConstructionTaskDTO countStockTrans;
    private List<SynStockTransDTO> lstSynStockTransDto;
    private List<UtilAttachDocumentDTO> utilAttachDocumentDTOs;
    private List<SynStockTransDetailDTO> lstSynStockTransDetail;
    private List<MerEntityDTO> lstMerEntity;
    private com.viettel.wms.dto.AppParamDTO appParamDTO;
    private KpiLogTimeProcessDTO kpiLogTimeProcessDTO;
    
    
    
    
    
    
	public KpiLogTimeProcessDTO getKpiLogTimeProcessDTO() {
		return kpiLogTimeProcessDTO;
	}

	public void setKpiLogTimeProcessDTO(KpiLogTimeProcessDTO kpiLogTimeProcessDTO) {
		this.kpiLogTimeProcessDTO = kpiLogTimeProcessDTO;
	}

	public List<UtilAttachDocumentDTO> getUtilAttachDocumentDTOs() {
		return utilAttachDocumentDTOs;
	}

	public void setUtilAttachDocumentDTOs(List<UtilAttachDocumentDTO> utilAttachDocumentDTOs) {
		this.utilAttachDocumentDTOs = utilAttachDocumentDTOs;
	}

	public com.viettel.wms.dto.AppParamDTO getAppParamDTO() {
		return appParamDTO;
	}

	public void setAppParamDTO(com.viettel.wms.dto.AppParamDTO appParamDTO) {
		this.appParamDTO = appParamDTO;
	}

	public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    public CountConstructionTaskDTO getCountStockTrans() {
        return countStockTrans;
    }

    public void setCountStockTrans(CountConstructionTaskDTO countStockTrans) {
        this.countStockTrans = countStockTrans;
    }

    public List<SynStockTransDTO> getLstSynStockTransDto() {
        return lstSynStockTransDto;
    }

    public void setLstSynStockTransDto(List<SynStockTransDTO> lstSynStockTransDto) {
        this.lstSynStockTransDto = lstSynStockTransDto;
    }

    public List<SynStockTransDetailDTO> getLstSynStockTransDetail() {
        return lstSynStockTransDetail;
    }

    public void setLstSynStockTransDetail(List<SynStockTransDetailDTO> lstSynStockTransDetail) {
        this.lstSynStockTransDetail = lstSynStockTransDetail;
    }

    public List<MerEntityDTO> getLstMerEntity() {
        return lstMerEntity;
    }

    public void setLstMerEntity(List<MerEntityDTO> lstMerEntity) {
        this.lstMerEntity = lstMerEntity;
    }

    //huypq-20190907-start
    private DataListDTO dataList;

	public DataListDTO getDataList() {
		return dataList;
	}

	public void setDataList(DataListDTO dataList) {
		this.dataList = dataList;
	}
    
	private Long total;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	
    //huy-end
}
