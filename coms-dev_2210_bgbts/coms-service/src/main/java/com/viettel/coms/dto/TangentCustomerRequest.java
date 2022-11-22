package com.viettel.coms.dto;
import java.util.List;

public class TangentCustomerRequest {
    private SysUserRequest sysUserRequest;
//    private SysUserRequest sysUserReceiver;
    private TangentCustomerDTO tangentCustomerDTO;
    private TangentCustomerGPTHDTO tangentCustomerGPTHDTO;
    private int totalConfirm;
    private int totalNoConfirm;
    private int totalRedct;
    private List<TangentCustomerDTO> lstImage;
    private List<TangentCustomerDTO> lstTangentCustomer;
    private List<TangentCustomerGPTHDTO> lstImageGPTH;
    private List<TangentCustomerGPTHDTO> lstTangentCustomerGPTH;
    
    private String source;
    
    public List<TangentCustomerDTO> getLstImage() {
		return lstImage;
	}

	public void setLstImage(List<TangentCustomerDTO> lstImage) {
		this.lstImage = lstImage;
	}

	public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

	public TangentCustomerDTO getTangentCustomerDTO() {
		return tangentCustomerDTO;
	}

	public void setTangentCustomerDTO(TangentCustomerDTO tangentCustomerDTO) {
		this.tangentCustomerDTO = tangentCustomerDTO;
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

	public List<TangentCustomerDTO> getLstTangentCustomer() {
		return lstTangentCustomer;
	}

	public void setLstTangentCustomer(List<TangentCustomerDTO> lstTangentCustomer) {
		this.lstTangentCustomer = lstTangentCustomer;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public TangentCustomerGPTHDTO getTangentCustomerGPTHDTO() {
		return tangentCustomerGPTHDTO;
	}

	public void setTangentCustomerGPTHDTO(TangentCustomerGPTHDTO tangentCustomerGPTHDTO) {
		this.tangentCustomerGPTHDTO = tangentCustomerGPTHDTO;
	}

	public List<TangentCustomerGPTHDTO> getLstTangentCustomerGPTH() {
		return lstTangentCustomerGPTH;
	}

	public void setLstTangentCustomerGPTH(List<TangentCustomerGPTHDTO> lstTangentCustomerGPTH) {
		this.lstTangentCustomerGPTH = lstTangentCustomerGPTH;
	}

	public List<TangentCustomerGPTHDTO> getLstImageGPTH() {
		return lstImageGPTH;
	}

	public void setLstImageGPTH(List<TangentCustomerGPTHDTO> lstImageGPTH) {
		this.lstImageGPTH = lstImageGPTH;
	}
	
}
