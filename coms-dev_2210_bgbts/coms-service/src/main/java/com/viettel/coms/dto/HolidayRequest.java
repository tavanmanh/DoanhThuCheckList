package com.viettel.coms.dto;

public class HolidayRequest {
    private SysUserRequest sysUserRequest;
    private SysUserRequest sysUserReceiver;
    private UserHolidayDTO holidayDto;
    private int totalConfirm;
    private int totalNoConfirm;
    private int totalRedct;

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

	public UserHolidayDTO getHolidayDto() {
		return holidayDto;
	}

	public void setHolidayDto(UserHolidayDTO holidayDto) {
		this.holidayDto = holidayDto;
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

}
