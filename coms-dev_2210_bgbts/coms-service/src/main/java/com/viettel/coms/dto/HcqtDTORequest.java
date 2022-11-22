package com.viettel.coms.dto;

import com.viettel.erp.dto.ConstrGroundHandoverDTO;
import com.viettel.erp.dto.ConstrWorkLogsDTO;
import com.viettel.erp.dto.MonitorMissionAssignDTO;
import com.viettel.erp.dto.VConstructionHcqtDTO;
import com.viettel.erp.dto.WorkItemsAcceptanceDTO;

public class HcqtDTORequest{

	private SysUserRequest sysUserRequest;
	private VConstructionHcqtDTO vConstructionHcqtDTO;
	//HienLT56 start 17082020
	private MonitorMissionAssignDTO monitorMissionAssignDTO;
	private ConstrGroundHandoverDTO constrGroundHandoverDTO;
	private ConstrWorkLogsDTO constrWorkLogsDTO;
	//HienLT56 start  16092020
	private WorkItemsAcceptanceDTO workItemsAcceptanceDTO;
	
	public WorkItemsAcceptanceDTO getWorkItemsAcceptanceDTO() {
		return workItemsAcceptanceDTO;
	}

	public void setWorkItemsAcceptanceDTO(WorkItemsAcceptanceDTO workItemsAcceptanceDTO) {
		this.workItemsAcceptanceDTO = workItemsAcceptanceDTO;
	}
	//HienLT56  end 16092020
	public MonitorMissionAssignDTO getMonitorMissionAssignDTO() {
		return monitorMissionAssignDTO;
	}

	public void setMonitorMissionAssignDTO(MonitorMissionAssignDTO monitorMissionAssignDTO) {
		this.monitorMissionAssignDTO = monitorMissionAssignDTO;
	}

	public ConstrGroundHandoverDTO getConstrGroundHandoverDTO() {
		return constrGroundHandoverDTO;
	}

	public void setConstrGroundHandoverDTO(ConstrGroundHandoverDTO constrGroundHandoverDTO) {
		this.constrGroundHandoverDTO = constrGroundHandoverDTO;
	}
	
	public ConstrWorkLogsDTO getConstrWorkLogsDTO() {
		return constrWorkLogsDTO;
	}

	public void setConstrWorkLogsDTO(ConstrWorkLogsDTO constrWorkLogsDTO) {
		this.constrWorkLogsDTO = constrWorkLogsDTO;
	}
	//HienLT56 end 17082020

	public VConstructionHcqtDTO getvConstructionHcqtDTO() {
		return vConstructionHcqtDTO;
	}

	public void setvConstructionHcqtDTO(VConstructionHcqtDTO vConstructionHcqtDTO) {
		this.vConstructionHcqtDTO = vConstructionHcqtDTO;
	}

	public SysUserRequest getSysUserRequest() {
		return sysUserRequest;
	}

	public void setSysUserRequest(SysUserRequest sysUserRequest) {
		this.sysUserRequest = sysUserRequest;
	}
	
}
