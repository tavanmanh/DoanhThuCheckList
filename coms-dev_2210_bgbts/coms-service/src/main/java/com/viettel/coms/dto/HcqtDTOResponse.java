package com.viettel.coms.dto;

import java.util.List;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.erp.dto.ConstrGroundHandoverDTO;
import com.viettel.erp.dto.ConstrWorkLogsDTO;
import com.viettel.erp.dto.MonitorMissionAssignDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCDTDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCTCTDTO;
import com.viettel.erp.dto.VConstructionHcqtDTO;

public class HcqtDTOResponse {

	private ResultInfo resultInfo;
	private List<VConstructionHcqtDTO> lstConstructionDTO;
	private List<MonitorMissionAssignDTO> lstMonitorMissionAssign;
	private List<ConstrGroundHandoverDTO> lstConstrGroundHandover;
	private List<ConstrWorkLogsDTO> lstConstrWorkLogs;
	private List<RoleConfigProvinceUserCDTDTO> lstUserCDTDTO;
	private List<RoleConfigProvinceUserCTCTDTO> lstUserCTCTDTO;
	private String contentForLogs;
	
	public String getContentForLogs() {
		return contentForLogs;
	}

	public void setContentForLogs(String contentForLogs) {
		this.contentForLogs = contentForLogs;
	}

	public List<ConstrGroundHandoverDTO> getLstConstrGroundHandover() {
		return lstConstrGroundHandover;
	}

	public void setLstConstrGroundHandover(List<ConstrGroundHandoverDTO> lstConstrGroundHandover) {
		this.lstConstrGroundHandover = lstConstrGroundHandover;
	}

	public List<ConstrWorkLogsDTO> getLstConstrWorkLogs() {
		return lstConstrWorkLogs;
	}

	public void setLstConstrWorkLogs(List<ConstrWorkLogsDTO> lstConstrWorkLogs) {
		this.lstConstrWorkLogs = lstConstrWorkLogs;
	}

	public List<MonitorMissionAssignDTO> getLstMonitorMissionAssign() {
		return lstMonitorMissionAssign;
	}

	public void setLstMonitorMissionAssign(List<MonitorMissionAssignDTO> lstMonitorMissionAssign) {
		this.lstMonitorMissionAssign = lstMonitorMissionAssign;
	}

	public List<VConstructionHcqtDTO> getLstConstructionDTO() {
		return lstConstructionDTO;
	}

	public void setLstConstructionDTO(List<VConstructionHcqtDTO> lstConstructionDTO) {
		this.lstConstructionDTO = lstConstructionDTO;
	}

	public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

	public List<RoleConfigProvinceUserCDTDTO> getLstUserCDTDTO() {
		return lstUserCDTDTO;
	}

	public void setLstUserCDTDTO(List<RoleConfigProvinceUserCDTDTO> lstUserCDTDTO) {
		this.lstUserCDTDTO = lstUserCDTDTO;
	}

	public List<RoleConfigProvinceUserCTCTDTO> getLstUserCTCTDTO() {
		return lstUserCTCTDTO;
	}

	public void setLstUserCTCTDTO(List<RoleConfigProvinceUserCTCTDTO> lstUserCTCTDTO) {
		this.lstUserCTCTDTO = lstUserCTCTDTO;
	}

}
