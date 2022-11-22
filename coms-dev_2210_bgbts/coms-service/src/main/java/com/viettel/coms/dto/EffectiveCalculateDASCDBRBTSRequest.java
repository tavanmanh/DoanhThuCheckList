package com.viettel.coms.dto;

public class EffectiveCalculateDASCDBRBTSRequest {
    private SysUserRequest sysUserRequest;
    private SysUserRequest sysUserReceiver;
    private EffectiveCalculateDASCDBRDTO effectiveCalculateDASCDBRDTO;
    private EffectiveCalculateBTSDTO effectiveCalculateBTSDTO;

    public EffectiveCalculateBTSDTO getEffectiveCalculateBTSDTO() {
		return effectiveCalculateBTSDTO;
	}

	public void setEffectiveCalculateBTSDTO(
			EffectiveCalculateBTSDTO effectiveCalculateBTSDTO) {
		this.effectiveCalculateBTSDTO = effectiveCalculateBTSDTO;
	}

	public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public SysUserRequest getSysUserReceiver() {
        return sysUserReceiver;
    }

    public void setSysUserReceiver(SysUserRequest sysUserReceiver) {
        this.sysUserReceiver = sysUserReceiver;
    }

	public EffectiveCalculateDASCDBRDTO getEffectiveCalculateDASCDBRDTO() {
		return effectiveCalculateDASCDBRDTO;
	}

	public void setEffectiveCalculateDASCDBRDTO(
			EffectiveCalculateDASCDBRDTO effectiveCalculateDASCDBRDTO) {
		this.effectiveCalculateDASCDBRDTO = effectiveCalculateDASCDBRDTO;
	}

}
