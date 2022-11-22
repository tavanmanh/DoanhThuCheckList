package com.viettel.coms.dto;

import java.util.List;

public class StockTransRequest {
    private SysUserRequest sysUserRequest;
    private SysUserRequest sysUserReceiver;
    private SynStockTransDTO synStockTransDto;
    private List<SynStockTransDTO> lstSynStockTransDto;
    private SynStockTransDetailDTO synStockTransDetailDto;
    private int totalConfirm;
    private int totalNoConfirm;
    private int totalRedct;
    
    //tatph-start-17/2/2020
    private java.lang.Long receiverId;
    private java.lang.String confirmDescription;
    private AppParamDTO appParamDTO;
    private List<UtilAttachDocumentDTO> filePaths;
    private String appCode;
    private String funcCode;
    private String serviceCode;
    private Long kpiLogTimeProcessId;
    //tatph-end-17/2/2020

    
    
	public List<SynStockTransDTO> getLstSynStockTransDto() {
		return lstSynStockTransDto;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public int getTotalConfirm() {
		return totalConfirm;
	}

	public void setTotalConfirm(int totalConfirm) {
		this.totalConfirm = totalConfirm;
	}

	public int getTotalNoConfirm() {
		return totalNoConfirm;
	}

	public void setTotalNoConfirm(int totalNoConfirm) {
		this.totalNoConfirm = totalNoConfirm;
	}

	public int getTotalRedct() {
		return totalRedct;
	}

	public void setTotalRedct(int totalRedct) {
		this.totalRedct = totalRedct;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	public Long getKpiLogTimeProcessId() {
		return kpiLogTimeProcessId;
	}

	public void setKpiLogTimeProcessId(Long kpiLogTimeProcessId) {
		this.kpiLogTimeProcessId = kpiLogTimeProcessId;
	}

	public List<UtilAttachDocumentDTO> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<UtilAttachDocumentDTO> filePaths) {
		this.filePaths = filePaths;
	}

	public AppParamDTO getAppParamDTO() {
		return appParamDTO;
	}

	public void setAppParamDTO(AppParamDTO appParamDTO) {
		this.appParamDTO = appParamDTO;
	}

	public java.lang.String getConfirmDescription() {
		return confirmDescription;
	}

	public void setConfirmDescription(java.lang.String confirmDescription) {
		this.confirmDescription = confirmDescription;
	}

	public java.lang.Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(java.lang.Long receiverId) {
		this.receiverId = receiverId;
	}

	public void setLstSynStockTransDto(List<SynStockTransDTO> lstSynStockTransDto) {
		this.lstSynStockTransDto = lstSynStockTransDto;
	}

	public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public SynStockTransDTO getSynStockTransDto() {
        return synStockTransDto;
    }

    public void setSynStockTransDto(SynStockTransDTO synStockTransDto) {
        this.synStockTransDto = synStockTransDto;
    }

    public SynStockTransDetailDTO getSynStockTransDetailDto() {
        return synStockTransDetailDto;
    }

    public void setSynStockTransDetailDto(SynStockTransDetailDTO synStockTransDetailDto) {
        this.synStockTransDetailDto = synStockTransDetailDto;
    }

    public SysUserRequest getSysUserReceiver() {
        return sysUserReceiver;
    }

    public void setSysUserReceiver(SysUserRequest sysUserReceiver) {
        this.sysUserReceiver = sysUserReceiver;
    }

    //Huypq-20190907-start
    private Long page;
    private Integer pageSize;
    private Integer totalRecord;
    private String keySearch;
    private Long confirm;
    private Long state;
    private String constructionType;
    private String overDateKPI;
    
	public String getOverDateKPI() {
		return overDateKPI;
	}

	public void setOverDateKPI(String overDateKPI) {
		this.overDateKPI = overDateKPI;
	}

	public String getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}

	public Long getConfirm() {
		return confirm;
	}

	public void setConfirm(Long confirm) {
		this.confirm = confirm;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getKeySearch() {
		return keySearch;
	}

	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
    
    //Huy-end
	
	//Huypq-09042020-start
	private String  userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//Huy-end
	//    hoanm1_20200329_vsmart_start
    private List<AIOSynStockTransDetailDTO> lstStockTransDetail;

    public List<AIOSynStockTransDetailDTO> getLstStockTransDetail() {
		return lstStockTransDetail;
	}

	public void setLstStockTransDetail(
			List<AIOSynStockTransDetailDTO> lstStockTransDetail) {
		this.lstStockTransDetail = lstStockTransDetail;
	}
//	hoanm1_20200329_vsmart_end

	private Long sysUserId;
	private Long stockId;
	private String stockCode;
	private String stockTransCode;

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockTransCode() {
		return stockTransCode;
	}

	public void setStockTransCode(String stockTransCode) {
		this.stockTransCode = stockTransCode;
	}
}
