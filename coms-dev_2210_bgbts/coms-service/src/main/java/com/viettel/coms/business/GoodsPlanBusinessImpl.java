package com.viettel.coms.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.GoodsPlanBO;
import com.viettel.coms.dao.GoodsPlanDAO;
import com.viettel.coms.dao.GoodsPlanDetailDAO;
import com.viettel.coms.dto.GoodsPlanDTO;
import com.viettel.coms.dto.GoodsPlanDetailDTO;
import com.viettel.coms.dto.RequestGoodsDTO;
import com.viettel.coms.dto.RequestGoodsDetailDTO;
import com.viettel.coms.dto.SignVofficeDTO;
import com.viettel.erp.dto.CatFileInvoiceDTO;
import com.viettel.erp.dto.ConstrCompleteRecordsMapDTO;
import com.viettel.erp.dto.ConstrGroundHandoverDTO;
import com.viettel.erp.dto.ConstrWorkLogsDTO;
import com.viettel.erp.dto.MonitorMissionAssignDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCDTDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCTCTDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.erp.dto.VConstructionHcqtDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;

@Service("goodsPlanBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GoodsPlanBusinessImpl extends BaseFWBusinessImpl<GoodsPlanDAO, GoodsPlanDTO, GoodsPlanBO>
implements GoodsPlanBusiness{
	@Autowired
	private GoodsPlanDAO goodsPlanDAO;
	
	@Autowired
	private GoodsPlanDetailDAO goodsPlanDetailDAO;
	
	public GoodsPlanBusinessImpl() {
		tModel = new GoodsPlanBO();
		tDAO = goodsPlanDAO;
	}
	
	@Override
	public GoodsPlanDAO gettDAO() {
		return goodsPlanDAO;
	}

	@Override
	public long count() {
		return goodsPlanDAO.count("GoodsPlanDetailBO", null);
	}
	
	/*public List<RequestGoodsDTO> doSearchPopupGoodsPlan(GoodsPlanDTO obj){
		List<RequestGoodsDTO> dto = goodsPlanDAO.doSearchPopupGoodsPlan(obj);
		return dto;
	}*/
	
	public DataListDTO doSearchPopupGoodsPlan(GoodsPlanDTO obj) {
		List<RequestGoodsDTO> ls = goodsPlanDAO.doSearchPopupGoodsPlan(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	
	public List<SysGroupDto> doSearchSysGroup(GoodsPlanDTO obj){
		List<SysGroupDto> dto = goodsPlanDAO.doSearchSysGroup(obj);
		return dto;
	}
	
	public List<RequestGoodsDetailDTO> doSearchReqGoodsDetail(GoodsPlanDTO obj){
		List<RequestGoodsDetailDTO> dto = goodsPlanDAO.doSearchReqGoodsDetail(obj);
		return dto;
	}
	
	public Long addGoodsPlan(GoodsPlanDTO obj) {
		boolean check = check(obj.getCode());
		if(!check){
			throw new IllegalArgumentException("Mã kế hoạch đã tồn tại !");
		}
		
		return goodsPlanDAO.saveObject(obj.toModel());
	}
	
	public Long updateGoodsPlan(GoodsPlanDTO obj) {
		return goodsPlanDAO.updateObject(obj.toModel());
	}
	
	public Long remove(GoodsPlanDTO obj) {
		return goodsPlanDAO.remove(obj);
	}
	
	public Long deleteGoodsPlanDetail(GoodsPlanDTO obj) {
		return goodsPlanDAO.deleteGoodsPlanDetail(obj);
	}
	
	public Long addGoodsPlanDetail(GoodsPlanDetailDTO obj) {
		return goodsPlanDetailDAO.saveObject(obj.toModel());
	}
	
	public void removeGoodsPlanDetail(GoodsPlanDTO obj) {
		goodsPlanDetailDAO.removeGoodsPlanDetail(obj);
	}
	
	public Boolean check(String code){
		GoodsPlanDTO obj = goodsPlanDAO.findByCode(code);
 	   if(code == null){
 		   if(obj==null){
 			   return true;
 		   } else {
 			   return false;
 		   }
 	   } else {
 		   if(obj!=null && obj.getCode().equals(code)) {
 			   return false;
 		   } else {
 			   return true;
 		   }
 	   }
    }
	
	public DataListDTO doSearchAll(GoodsPlanDTO obj) {
		List<GoodsPlanDTO> ls = goodsPlanDAO.doSearchAll(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO doSearch(GoodsPlanDTO obj) {
		List<GoodsPlanDetailDTO> ls = goodsPlanDAO.doSearch(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public List<SysUserDTO> filterSysUser(GoodsPlanDTO obj) {
		List<SysUserDTO> sysUserDTOs = goodsPlanDAO.filterSysUser(obj);
		return sysUserDTOs;
	}
	
	public SignVofficeDTO getInformationVO(String objectId){
		SignVofficeDTO signVofficeDTO = goodsPlanDAO.getInformationVO(objectId);
		return signVofficeDTO;
	}
	
	public void removeSignVO(Long signVofficeId){
		goodsPlanDAO.removeSignVO(signVofficeId);
	}
	
	public void removeSignDetailVO(Long signVofficeId){
		goodsPlanDAO.removeSignDetailVO(signVofficeId);
	}

	//HuyPQ-start
    public List<GoodsPlanDTO> getForAutoCompleteUnit(GoodsPlanDTO obj) {
        return goodsPlanDAO.getCatUnit(obj);
    }
    
    public GoodsPlanDetailDTO genDataContract(GoodsPlanDetailDTO obj) {
    	return goodsPlanDAO.genDataContract(obj);
    }
    
    public SignVofficeDTO getInformationVOReject(String objectId){
		SignVofficeDTO signVofficeDTO = goodsPlanDAO.getInformationVOReject(objectId);
		return signVofficeDTO;
	}
    //Huy-end
    
    //Huypq-07082020-start service HCQT
    public List<VConstructionHcqtDTO> getAllConstructionHcqt(Long sysGroupId){
    	return goodsPlanDAO.getAllConstructionHcqt(sysGroupId);
    }
    
    public List<MonitorMissionAssignDTO> getMonitorMissionAssignByConstrId(VConstructionHcqtDTO obj){
    	MonitorMissionAssignDTO dto = new MonitorMissionAssignDTO();
    	dto.setConstructId(obj.getConstructId());
    	dto.setContractId(obj.getContractId());
    	return goodsPlanDAO.getMonitorMissionAssignByConstrId(dto);
    }
    
    public List<ConstrGroundHandoverDTO> getAllConstrGroundHandover(VConstructionHcqtDTO obj) {
    	ConstrGroundHandoverDTO dto = new ConstrGroundHandoverDTO();
    	dto.setConstructId(obj.getConstructId());
    	dto.setContractId(obj.getContractId());
    	return goodsPlanDAO.getAllConstrGroundHandover(dto);
    }
    
    public List<ConstrWorkLogsDTO> getAllConstrWorkLogs(VConstructionHcqtDTO obj) {
    	ConstrWorkLogsDTO dto = new ConstrWorkLogsDTO();
    	dto.setConstructId(obj.getConstructId());
    	return goodsPlanDAO.getAllConstrWorkLogs(dto);
    }
    //Huy-end
    //HienLT56 start 17082020
	public List<VConstructionHcqtDTO> addNewConstruction(Long sysGroupId) {
		return goodsPlanDAO.addNewConstruction(sysGroupId);
	}

	public String autoGenCode() {
		return goodsPlanDAO.autoGenCode();
	}

	public CatFileInvoiceDTO onlyFindByTableName(String tableName) {
		return  goodsPlanDAO.onlyFindByTableName( tableName);
	}

	public Long saveTable(MonitorMissionAssignDTO monitorMissionAssign) {
		Long monitorMissionAssignId = goodsPlanDAO.saveTable(monitorMissionAssign);
		return monitorMissionAssignId;
	}

	public List<ConstrGroundHandoverDTO> getAllConstrGroundHandover(ConstrGroundHandoverDTO dto) {
		return goodsPlanDAO.getAllConstrGroundHandover(dto);
	}

	public String getCode(String tableName, String value) {
		return goodsPlanDAO.getCode(tableName, value);
	}

	public Long saveConstrGroundHand(ConstrGroundHandoverDTO constrGroundHandover) {
		Long constrGroundHandId =  goodsPlanDAO.saveConstrGroundHand(constrGroundHandover);
		return constrGroundHandId;
	}

	public Long insert(Long constructId, String tableName, String tableId, Long tableIdValue, Long userId, String code) {
		ConstrCompleteRecordsMapDTO constrCompleteRecordsMap = new ConstrCompleteRecordsMapDTO();
		constrCompleteRecordsMap.setConstructionId(constructId);

		CatFileInvoiceDTO catInvoice = goodsPlanDAO.onlyFindByTableName(tableName);
		if (catInvoice == null) {
			throw new BusinessException("not-found-cat-invoice-table-name");
		}
		Long catFileInvoiceId = catInvoice.getCatFileInvoiceId();
		constrCompleteRecordsMap.setLevelOrder(1l);
		constrCompleteRecordsMap.setDataTableName(tableName);
		constrCompleteRecordsMap.setDataTableId(tableId);
		constrCompleteRecordsMap.setDataTableIdValue(tableIdValue);
		constrCompleteRecordsMap.setCreatedDate(new Date());
		constrCompleteRecordsMap.setCatFileInvoiceId(catFileInvoiceId);
		constrCompleteRecordsMap.setStatus(0l);
		constrCompleteRecordsMap.setCreatedUserId(userId);
		constrCompleteRecordsMap.setCode(code);

		return goodsPlanDAO.saveConstrComRecMap(constrCompleteRecordsMap);
	}

	public void updateAprroval(String tableName, Long id) {
		goodsPlanDAO.updateAprroval(tableName, id);
		
	}

	public String getUpdateConstrCompleteRecod(Long qualityID, String nameTable) {
		return goodsPlanDAO.getUpdateConstrCompleteRecod(qualityID, nameTable);
	}

	public boolean checkExistsConstrWorkLogsLabel(ConstrWorkLogsDTO obj) {
		boolean check = goodsPlanDAO.checkBia(obj.getConstructId());
		if(check) {
			return true;
		} else {
			return false;
		}
	}

	public List<RoleConfigProvinceUserCDTDTO> getListEmployeeCDT(String provinceCode) {
		return goodsPlanDAO.getListEmployeeCDT(provinceCode);
	}

	public List<RoleConfigProvinceUserCTCTDTO> getListEmployeeCTCT(String provinceCode) {
		return goodsPlanDAO.getListEmployeeCTCT(provinceCode);
	}

	//HienLT56 end 17082020
}
