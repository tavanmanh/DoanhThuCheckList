package com.viettel.coms.business;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.DepartmentBO;
import com.viettel.coms.dao.DepartmentDAO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

@Service("departmentBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DepartmentBusinessImpl extends BaseFWBusinessImpl<DepartmentDAO, DepartmentDTO, DepartmentBO>
        implements DepartmentBusiness {

    @Autowired
    private DepartmentDAO departmentDAO;

    public DepartmentBusinessImpl() {
        tModel = new DepartmentBO();
        tDAO = departmentDAO;
    }

    @Override
    public DepartmentDAO gettDAO() {
        return departmentDAO;
    }

    @Override
    public long count() {
        return departmentDAO.count("DepartmentBO", null);
    }

    public List<DepartmentDTO> getall(DepartmentDTO obj) {
        return departmentDAO.getall(obj);
    }

    @Override
    public List<DepartmentDTO> getDeptForAutocomplete(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        return departmentDAO.getForAutoCompleteDept(obj);
    }

    @Override
    public List<CatPartnerDTO> getAutocompleteLanHan(CatPartnerDTO obj) {
        // TODO Auto-generated method stub
        return departmentDAO.getAutocompleteLanHan(obj);
    }

    public DepartmentDTO getOne(Long id) {
        return departmentDAO.getOne(id);
    }

    public List<DepartmentDTO> getCatPartnerForAutocompleteDept(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        return departmentDAO.getCatPartnerForAutocompleteDept(obj);
    }

    public DataListDTO doSearchCatPartner(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        List<DepartmentDTO> ls = departmentDAO.doSearchCatPartner(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    //HuyPQ-start
    public List<DepartmentDTO> getForAutoCompleteDeptCheck(DepartmentDTO obj, HttpServletRequest request) {
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.ACCEPTANCE, request);
 		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        return departmentDAO.getForAutoCompleteDeptCheck(obj,groupIdList);
    }
    
    public List<DepartmentDTO> getSysGroupCheck(DepartmentDTO obj, HttpServletRequest request){
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.ACCEPTANCE, request);
 		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
    	return departmentDAO.getSysGroupCheck(obj,groupIdList);
    }
    
    public List<DepartmentDTO> getSysGroupCheckTTKTDV(DepartmentDTO obj, HttpServletRequest request){
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN,
				Constant.AdResourceKey.TTKT_DV, request);
 		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
    	return departmentDAO.getSysGroupCheck(obj,groupIdList);
    }
    
    public List<DepartmentDTO> getSysGroupCheckTTKT(DepartmentDTO obj, HttpServletRequest request){
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN,
				Constant.AdResourceKey.TTKT, request);
 		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
    	return departmentDAO.getForAutoCompleteDeptCheck(obj,groupIdList);
    }
    

    public List<DepartmentDTO> getForAutoCompleteTTKV(DepartmentDTO obj) {
        // TODO Auto-generated method stub
        return departmentDAO.getForAutoCompleteTTKV(obj);
    }
    
    public List<DepartmentDTO> getallTTKV(DepartmentDTO obj) {
        return departmentDAO.getallTTKV(obj);
    }
    //HuyPQ-end

    public DataListDTO getForAutoCompleteDeptByDomain(DepartmentDTO obj, HttpServletRequest request) {
    	List<DepartmentDTO> ls = new ArrayList<DepartmentDTO>();
    	String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> lstProvinceId = ConvertData.convertStringToList(provinceId, ",");
		if(lstProvinceId!=null && lstProvinceId.size()>0) {
			ls = departmentDAO.getForAutoCompleteDeptByDomain(obj, lstProvinceId);
		}
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    //Huypq-27112020-start
    public DataListDTO getForAutoCompleteCnkt(DepartmentDTO obj) {
    	List<DepartmentDTO> ls = new ArrayList<DepartmentDTO>();
		ls = departmentDAO.getForAutoCompleteCnkt(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    //Huy-end
    
}
