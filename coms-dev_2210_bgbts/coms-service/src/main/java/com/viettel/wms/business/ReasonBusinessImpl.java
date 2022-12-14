package com.viettel.wms.business;

import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.bo.ReasonBO;
import com.viettel.wms.dao.ReasonDAO;
import com.viettel.wms.dto.ReasonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("reasonBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReasonBusinessImpl extends BaseFWBusinessImpl<ReasonDAO, ReasonDTO, ReasonBO> implements ReasonBusiness {

    @Autowired
    private ReasonDAO reasonDAO;


    public ReasonBusinessImpl() {
        tModel = new ReasonBO();
        tDAO = reasonDAO;
    }

    @Override
    public ReasonDAO gettDAO() {
        return reasonDAO;
    }

    @Override
    public long count() {
        return reasonDAO.count("ReasonBO", null);
    }

    public DataListDTO doSearch(ReasonDTO criteria) {
        List<ReasonDTO> ls = reasonDAO.doSearch(criteria);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(criteria.getTotalRecord());
        data.setSize(criteria.getTotalRecord());
        data.setStart(1);
        return data;
    }

    @SuppressWarnings("unused")
    public Boolean checkCode(String code, Long reasonId) {
        ReasonDTO obj = reasonDAO.getbycode(code);
        if (reasonId == null) {
            if (obj == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (obj == null) {
                return true;
            } else if (obj != null && obj.getReasonId().longValue() == reasonId) {
                return true;
            } else {
                return false;
            }
        }

    }

    public Long updateReason(ReasonDTO obj, KttsUserSession objUser) throws Exception {
        if (objUser.getSysUserId() != null) {
            if (!objUser.getSysUserId().equals(obj.getCreatedBy())) {
                throw new IllegalArgumentException("Ng?????i d??ng hi???n t???i kh??ng c?? quy???n s???a b???n ghi n??y !");
            }
        }


        boolean check = checkCode(obj.getCode(), obj.getReasonId());
        if (!check) {
            throw new IllegalArgumentException("M?? l?? do ???? t???n t???i !");
        }
        return reasonDAO.updateObject(obj.toModel());
    }

    public Long createReason(ReasonDTO obj) throws Exception {

        boolean check = checkCode(obj.getCode(), null);
        if (!check) {
            throw new IllegalArgumentException("M?? l?? do ???? t???n t???i !");
        }
        return reasonDAO.saveObject(obj.toModel());
    }

    public Long deleteReason(ReasonDTO obj, KttsUserSession objUser) {
        if (!objUser.getSysUserId().equals(obj.getCreatedBy())) {
            throw new IllegalArgumentException("Ng?????i d??ng hi???n t???i kh??ng c?? quy???n x??a b???n ghi n??y !");
        }

        return reasonDAO.updateObject(obj.toModel());
    }

    public List<ReasonDTO> getForComboBox(ReasonDTO obj) {
        return reasonDAO.getForComboBox(obj);
    }

/*	public ReasonDTO getReasonById(Long id) {
		// TODO Auto-generated method stub
		return reasonDAO.getbyId(id);
	}*/
}
