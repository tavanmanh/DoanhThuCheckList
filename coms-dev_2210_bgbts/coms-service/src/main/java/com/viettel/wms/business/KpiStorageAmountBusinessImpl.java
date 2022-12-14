package com.viettel.wms.business;

import com.viettel.coms.dto.CommonDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.bo.KpiStorageAmountBO;
import com.viettel.wms.constant.Constants;
import com.viettel.wms.dao.KpiStorageAmountDAO;
import com.viettel.wms.dto.KpiStorageAmountDTO;
import com.viettel.wms.dto.StockDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;


@Service("kpiStorageAmountBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KpiStorageAmountBusinessImpl extends BaseFWBusinessImpl<KpiStorageAmountDAO, KpiStorageAmountDTO, KpiStorageAmountBO> implements KpiStorageAmountBusiness {

    @Autowired
    private KpiStorageAmountDAO kpiStorageAmountDAO;

    @Autowired
    private StockBusinessImpl stockBusinessImpl;


    @Autowired
    private CommonBusiness commonBusiness;


    public KpiStorageAmountBusinessImpl() {
        tModel = new KpiStorageAmountBO();
        tDAO = kpiStorageAmountDAO;
    }

    @Override
    public KpiStorageAmountDAO gettDAO() {
        return kpiStorageAmountDAO;
    }

    @Override
    public long count() {
        return kpiStorageAmountDAO.count("KpiStorageAmountBO", null);
    }

    //Hàm xử lý logic Kpi theo số lượng
    public DataListDTO doSearchKpiAmount(KpiStorageAmountDTO obj, HttpServletRequest request) throws Exception {
        String err = "";
        for (Long id : obj.getListStockId()) {
            StockDTO stockDTO = (StockDTO) stockBusinessImpl.getOneById(id);
            if (!VpsPermissionChecker.checkPermissionOnDomainData(Constants.OperationKey.REPORT, Constants.AdResourceKey.STOCK, id, request)) {
                err = StringUtils.isNotEmpty(err) ? (err + ";" + id) : ("Bạn không có quyền xem báo cáo tại kho " + id);
            }
        }

        if (StringUtils.isNotEmpty(err)) {
            throw new IllegalArgumentException(err);
        }

        if (obj.getListStockId().size() == 0) {
            List<Long> listId = commonBusiness.getListDomainData(Constants.OperationKey.REPORT, Constants.AdResourceKey.STOCK, request);
            obj.setListStockId(listId);
        }

        List<KpiStorageAmountDTO> ls = kpiStorageAmountDAO.doSearchKpiAmount(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getTotalRecord());
        data.setStart(1);
        return data;
    }
//End


    public CommonDTO getCharOneAmount(CommonDTO obj, HttpServletRequest request) {
        List<Long> listStockId = commonBusiness.getListDomainData(Constants.OperationKey.VIEW, Constants.AdResourceKey.STOCK, request);
        obj.setListStockId(listStockId);
        CommonDTO objReturn = new CommonDTO();
        List<CommonDTO> ls = kpiStorageAmountDAO.getCharOneAmount(obj);
        if (ls.size() > 0) {
            for (Iterator<CommonDTO> interator = ls.iterator(); interator.hasNext(); ) {
                CommonDTO wi = interator.next();
                objReturn.getListKPIAmount().add(wi.getKipAmount());
                objReturn.getListStockCode().add(wi.getStockCode());
            }
        }
        return objReturn;
    }

    public CommonDTO getCharOneTimes(CommonDTO obj, HttpServletRequest request) {
        List<Long> listStockId = commonBusiness.getListDomainData(Constants.OperationKey.VIEW, Constants.AdResourceKey.STOCK, request);
        obj.setListStockId(listStockId);
        CommonDTO objReturn = new CommonDTO();
        List<CommonDTO> ls = kpiStorageAmountDAO.getCharOneTimes(obj);
        if (ls.size() > 0) {
            for (Iterator<CommonDTO> interator = ls.iterator(); interator.hasNext(); ) {
                CommonDTO wi = interator.next();
                objReturn.getListKPIAmount().add(wi.getKipAmount());
                objReturn.getListStockCode().add(wi.getStockCode());
            }
        }
        return objReturn;
    }

}
