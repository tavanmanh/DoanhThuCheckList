package com.viettel.cat.business;

import com.viettel.cat.bo.CatProvinceBO;
import com.viettel.cat.dao.CatProvinceDAO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

//import com.viettel.cat.dto.CatUnitDTO;

@Service("catProvinceBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CatProvinceBusinessImpl extends
        BaseFWBusinessImpl<CatProvinceDAO, CatProvinceDTO, CatProvinceBO>
        implements CatProvinceBusiness {

    @Autowired
    private CatProvinceDAO catProvinceDAO;

    public CatProvinceBusinessImpl() {
        tModel = new CatProvinceBO();
        tDAO = catProvinceDAO;
    }

    @Override
    public CatProvinceDAO gettDAO() {
        return catProvinceDAO;
    }

    @Override
    public CatProvinceDTO findByCode(String value) {
        return catProvinceDAO.findByCode(value);
    }

    @Override
    public List<CatProvinceDTO> doSearch(CatProvinceDTO obj) {
        return catProvinceDAO.doSearch(obj);
    }

    @Override
    public List<CatProvinceDTO> getForComboBox(CatProvinceDTO obj) {
        return catProvinceDAO.getForComboBox(obj);
    }

    public String delete(List<Long> ids, String tableName,
                         String tablePrimaryKey) {
        return catProvinceDAO.delete(ids, tableName, tablePrimaryKey);
    }

    public CatProvinceDTO getById(Long id) {
        return catProvinceDAO.getById(id);
    }

    public DataListDTO doSearchProvinceInPopup(CatProvinceDTO obj) {
        List<CatProvinceDTO> ls = catProvinceDAO.doSearchProvinceInPopup(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public DataListDTO getProvinceByDomainInPopup(CatProvinceDTO obj, HttpServletRequest request) {
    	List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
    	String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> lstProvinceId = ConvertData.convertStringToList(provinceId, ",");
		if(lstProvinceId!=null && lstProvinceId.size()>0) {
			ls = catProvinceDAO.getProvinceByDomainInPopup(obj, lstProvinceId);
		}
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    
    public List<CatProvinceDTO> autoCompleteSearch(CatProvinceDTO obj) {
		// TODO Auto-generated method stub
		return catProvinceDAO.autoCompleteSearch(obj);
	}
    
    @Override
	public List<CatProvinceDTO> getAllProvince() {
		// TODO Auto-generated method stub
		return catProvinceDAO.getAllProvince();
	}

	//Duonghv13 end-16/08/2021//

}
