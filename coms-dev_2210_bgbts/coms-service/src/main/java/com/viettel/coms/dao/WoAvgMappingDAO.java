package com.viettel.coms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoAvgMappingEntityBO;
import com.viettel.coms.dto.avg.GetWoAvgOutputDto;
import com.viettel.coms.dto.avg.WoAvgVhktInputDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoAvgMappingDAO extends BaseFWDAOImpl<WoAvgMappingEntityBO, Long> {
    public Long getNextSeqVal() {
        return this.getNextValSequence("WO_AVG_MAPPING_SEQ");
    }

    public WoAvgMappingEntityBO findByOrderId(String orderCodeAvg, String orderCodeTgdd) {
        StringBuilder sql = new StringBuilder("SELECT ORDER_CODE_TGDD \"orderCodeTgdd\", ORDER_CODE_AVG \"orderCodeAvg\", CUSTOMER_NAME \"customerName\", PHONE_NUMBER \"phoneNumber\", PERSONAL_ID \"personalId\", ADDRESS \"address\",  PRODUCT_CODE \"productCode\", PAYMENT_STATUS \"paymentStatus\" FROM  WO_MAPPING_AVG where ORDER_CODE_TGDD = :orderCodeTgdd AND ORDER_CODE_AVG = :orderCodeAvg ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("orderCodeTgdd", orderCodeTgdd);
        query.setParameter("orderCodeAvg", orderCodeAvg);
        query.setResultTransformer(Transformers.aliasToBean(WoAvgMappingEntityBO.class));
        query.addScalar("orderCodeTgdd", new StringType());
        query.addScalar("orderCodeAvg", new StringType());
        List<WoAvgMappingEntityBO> lst = query.list();
        return lst != null && lst.size() > 0 ? lst.get(0) : null;
    }

    public List<GetWoAvgOutputDto> doSearch(WoAvgVhktInputDTO request) {
        StringBuilder sql = new StringBuilder("SELECT ORDER_CODE_TGDD \"orderCodeTgdd\", ORDER_CODE_AVG \"orderCodeAvg\", CUSTOMER_NAME \"customerName\", PHONE_NUMBER \"phoneNumber\", PERSONAL_ID \"personalId\", ADDRESS \"address\", PRODUCT_CODE \"productCode\", PAYMENT_STATUS \"paymentStatus\"  FROM  WO_MAPPING_AVG INNER JOIN WO ON WO.ID  = WO_MAPPING_AVG.WO_ID  WHERE ");
        Map<String, Object> params = new HashedMap();
        if (request.getWoId() != null) {
            sql.append(" WO_ID like :woId OR ");
            params.put("woId", "%" + request.getWoId());
        }
        if (request.getOrderCodeTgdd() != null) {
            sql.append(" ORDER_CODE_TGDD like :orderIdTgdd OR ");
            params.put("orderIdTgdd", "%" + request.getOrderCodeTgdd() + "%");

        }
        if (request.getOrderCodeAvg() != null) {
            sql.append(" ORDER_CODE_AVG like :orderIdAvg OR ");
            params.put("orderIdAvg", "%" + request.getOrderCodeAvg() + "%");

        }
        if (request.getProductCode() != null) {
            sql.append(" PRODUCT_CODE like :productCode OR ");
            params.put("productCode", "%" + request.getProductCode() + "%");

        }
        if (request.getPhoneNumber() != null) {
            sql.append(" PHONE_NUMBER like :phoneNumber OR ");
            params.put("phoneNumber", "%" + request.getPhoneNumber() + "%");

        }

//        if (request.getChipNumber() != null) {
//            sql.append(" CHIP_NUMBER like :chipNumber OR ");
//            params.put("chipNumber", "%" + request.getChipNumber() + "%");
//
//        }
//        if (request.getCardNumber() != null) {
//            sql.append(" CARD_NUMBER like :serialNumber OR ");
//            params.put("serialNumber", "%" + request.getCardNumber() + "%");
//        }
        if (request.getUserCreated() != null) {
            sql.append(" USER_CREATED like :userCreated OR ");
            params.put("userCreated", "%" + request.getUserCreated() + "%");
        }

        if (request.getCompleteDateFrom() != null && request.getCompleteDateTo() != null) {
            sql.append(" FINISH_DATE BETWEEN  :finishDateFrom AND :finishDateTo ");
            params.put("finishDateFrom", "%" + request.getCompleteDateFrom() + "%");
            params.put("finishDateTo", "%" + request.getCompleteDateTo() + "%");
        }
        String flatQUery = sql.toString().trim();
        if (flatQUery.endsWith(" WHERE")) {
            flatQUery = flatQUery.substring(0, flatQUery.length() - 5);
        }
        if (sql.toString().trim().endsWith(" OR")) {
            flatQUery = flatQUery.substring(0, flatQUery.length() - 2);
        }
        SQLQuery query = getSession().createSQLQuery(flatQUery);
        query.setResultTransformer(Transformers.aliasToBean(GetWoAvgOutputDto.class));
        params.forEach((s, o) -> query.setParameter(s, o));
        return query.list();
    }

    public WoAvgMappingEntityBO getWoAvgInformation(Long woId) {
        StringBuilder sql = new StringBuilder(" SELECT ORDER_CODE_TGDD \"orderCodeTgdd\", ORDER_CODE_AVG \"orderCodeAvg\", CUSTOMER_NAME \"customerName\", PHONE_NUMBER \"phoneNumber\", PERSONAL_ID \"personalId\", ADDRESS \"address\", PRODUCT_CODE \"productCode\", PAYMENT_STATUS \"paymentStatus\",SERVICE_PACKAGE \"servicePackage\" FROM  WO_MAPPING_AVG where WO_ID = :woId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.setResultTransformer(Transformers.aliasToBean(WoAvgMappingEntityBO.class));
        //query.addScalar("woId", new LongType());
        List<WoAvgMappingEntityBO> lst = query.list();
        return lst != null && lst.size() > 0 ? lst.get(0) : null;
    }
}

