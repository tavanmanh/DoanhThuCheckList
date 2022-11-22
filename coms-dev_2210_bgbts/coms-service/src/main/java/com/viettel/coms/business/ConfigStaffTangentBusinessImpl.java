package com.viettel.coms.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ConfigStaffTangentBO;
import com.viettel.coms.dao.ConfigStaffTangentDAO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

@Service("configStaffTangentBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigStaffTangentBusinessImpl
		extends BaseFWBusinessImpl<ConfigStaffTangentDAO, ConfigStaffTangentDTO, ConfigStaffTangentBO>
		implements ConfigStaffTangentBusiness {

	@Autowired
	private ConfigStaffTangentDAO configStaffTangentDAO;
	
	public DataListDTO doSearch(ConfigStaffTangentDTO obj) {
		List<ConfigStaffTangentDTO> ls = configStaffTangentDAO.doSearch(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		return data;
	}
	
	public DataListDTO doSearchProvinceInPopup(CatProvinceDTO obj, HttpServletRequest request) {
    	List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
		ls = configStaffTangentDAO.doSearchProvinceByRolePopup(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchStaffByPopup(ConfigStaffTangentDTO obj, HttpServletRequest request) {
    	List<ConfigStaffTangentDTO> ls = new ArrayList<ConfigStaffTangentDTO>();
		ls = configStaffTangentDAO.doSearchStaffByPopup(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public Long saveConfig(ConfigStaffTangentDTO obj, HttpServletRequest request) throws Exception{
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setStatus("1");
		obj.setCreatedDate(new Date());
		obj.setCreatedUser(objUser.getVpsUserInfo().getSysUserId());
//		duonghv13 edit 25122021
		if(obj.getType()!=null && obj.getType().equals("2")) {
			ConfigStaffTangentDTO checkDup = configStaffTangentDAO.checkDuplicateConfig(obj.getCatProvinceId(), obj.getType());
			if(checkDup!=null) {
				throw new BusinessException("Tỉnh " + obj.getProvinceCode() + " đã được cấu hình nhân viên ");
			}
		}
//		duonghv13 end 25122021
		return configStaffTangentDAO.saveObject(obj.toModel());
	}
	
	public Long updateConfig(ConfigStaffTangentDTO obj, HttpServletRequest request) {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUser(objUser.getVpsUserInfo().getSysUserId());
		return configStaffTangentDAO.updateObject(obj.toModel());
	}
	
	public DataListDTO doSearchProvinceInPopupByRole(CatProvinceDTO obj, HttpServletRequest request) {
//    	List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
//    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//				Constant.AdResourceKey.TANGENT_CUSTOMER, request);
//		
//		List<String> provinceIdLst = ConvertData.convertStringToList(groupId, ",");
//		
//		if(provinceIdLst!=null && provinceIdLst.size()>0) {
//			ls = configStaffTangentDAO.doSearchProvinceByRolePopupByRole(obj, provinceIdLst);
//		}
		List<CatProvinceDTO> ls = configStaffTangentDAO.doSearchProvinceByRolePopupByRole(obj, null);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchStaffByConfigProvinceId(ConfigStaffTangentDTO obj) {
    	List<ConfigStaffTangentDTO> ls = new ArrayList<ConfigStaffTangentDTO>();
		ls = configStaffTangentDAO.doSearchStaffByConfigProvinceId(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public Long removeConfig(ConfigStaffTangentDTO obj, HttpServletRequest request) throws Exception{
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		ConfigStaffTangentDTO dto = configStaffTangentDAO.checkUserBeforeDelete(obj.getStaffId());
		if(dto!=null) {
			throw new IllegalArgumentException("Nhân viên đã được giao nhiệm vụ tiếp xúc, trình bày giải pháp không được xoá !");
		}
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUser(objUser.getVpsUserInfo().getSysUserId());
		obj.setStatus("0");
		return configStaffTangentDAO.updateObject(obj.toModel());
	}
}
