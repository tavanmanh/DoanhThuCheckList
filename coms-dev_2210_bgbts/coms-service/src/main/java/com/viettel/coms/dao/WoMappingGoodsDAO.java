package com.viettel.coms.dao;

import com.viettel.coms.bo.WoMappingGoodsBO;
import com.viettel.coms.dto.WoMappingGoodsDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class WoMappingGoodsDAO extends BaseFWDAOImpl<WoMappingGoodsBO, Long> {

    public WoMappingGoodsDAO() {
        this.model = new WoMappingGoodsBO();
    }

    public WoMappingGoodsDAO(Session session) {
        this.session = session;
    }

    public WoMappingGoodsBO getOneRaw(long id) {
        return this.get(WoMappingGoodsBO.class, id);
    }

    public List<WoMappingGoodsDTO> doSearch(WoMappingGoodsDTO dto) {
        String sql = " select id woMappingGoodsId, wo_id woId, goods_id goodsId, name, amount, goods_unit_name goodsUnitName, " +
                " amount_need amountNeed, amount_real amountReal, has_serial isSerial, serial, is_used isUsed " +
                " from WO_MAPPING_GOODS where 1 = 1 ";

        if (dto.getWoId() != null) {
            sql += " and wo_id = :woId ";
        }

        if (dto.getGoodsId() != null) {
            sql += " and goods_id = :goodsId ";
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql);
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql);
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (dto.getWoId() != null) {
            query.setParameter("woId", dto.getWoId());
            queryCount.setParameter("woId", dto.getWoId());
        }

        if (dto.getGoodsId() != null) {
            query.setParameter("goodsId", dto.getGoodsId());
            queryCount.setParameter("goodsId", dto.getGoodsId());
        }

        query.addScalar("woMappingGoodsId", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("goodsId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("amount", new DoubleType());
        query.addScalar("goodsUnitName", new StringType());
        query.addScalar("amountNeed", new DoubleType());
        query.addScalar("amountReal", new DoubleType());
        query.addScalar("isSerial", new LongType());
        query.addScalar("serial", new StringType());
        query.addScalar("isUsed", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingGoodsDTO.class));

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public void delMappingObjs(Long woId) {
    	Session session = getSession();
    	try {
        StringBuilder sql = new StringBuilder("DELETE FROM WO_MAPPING_GOODS WHERE WO_ID =:woId");
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.clear();
		}
    }

    public void delMappingObjsFollowGoodsId(Long goodsId) {
        StringBuilder sql = new StringBuilder("DELETE FROM WO_MAPPING_GOODS WHERE GOODS_ID =:goodsId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("goodsId", goodsId);
        query.executeUpdate();
    }
}
