package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;

import java.util.List;

public class HolidayResponse {
    private ResultInfo resultInfo;
//    private CountConstructionTaskDTO countStockTrans;
    private List<UserHolidayDTO> lstUserHolidayDto;
    private UserHolidayDTO userHolidayDto;
    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

	public List<UserHolidayDTO> getLstUserHolidayDto() {
		return lstUserHolidayDto;
	}

	public void setLstUserHolidayDto(List<UserHolidayDTO> lstUserHolidayDto) {
		this.lstUserHolidayDto = lstUserHolidayDto;
	}

	public UserHolidayDTO getUserHolidayDto() {
		return userHolidayDto;
	}

	public void setUserHolidayDto(UserHolidayDTO userHolidayDto) {
		this.userHolidayDto = userHolidayDto;
	}
	

}
