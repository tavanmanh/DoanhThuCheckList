package com.viettel.erp.business;

import com.viettel.erp.bo.CntContractBO;
import com.viettel.erp.dao.CntContractDAO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("cntContractBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CntContractBusinessImpl extends BaseFWBusinessImpl<CntContractDAO, CntContractDTO, CntContractBO> implements CntContractBusiness {

    @Autowired
    private CntContractDAO cntContractDAO;

    public CntContractBusinessImpl() {
        tModel = new CntContractBO();
        tDAO = cntContractDAO;
    }

    @Override
    public CntContractDAO gettDAO() {
        return cntContractDAO;
    }

    @Override
    public long count() {
        return cntContractDAO.count("CntContractBO", null);
    }

    @Override
    public List<CntContractDTO> doSearch(CntContractDTO cntContractDTO) {
        return cntContractDAO.doSearch(cntContractDTO);
    }

    public CntContractBO getById(Long id) {
        return cntContractDAO.getContractByContractId(id);
    }
}
