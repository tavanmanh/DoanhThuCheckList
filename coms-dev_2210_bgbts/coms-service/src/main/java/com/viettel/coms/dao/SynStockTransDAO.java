/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.StockTransVsmartBO;
import com.viettel.coms.dto.AIOStockTransDetailSerialDTO;
import com.viettel.coms.dto.AIOSynStockTransDTO;
import com.viettel.coms.dto.AIOSynStockTransDetailDTO;
import com.viettel.coms.dto.ComsBaseFWDTO;
import com.viettel.coms.dto.CountConstructionTaskDTO;
import com.viettel.coms.dto.MerEntityDTO;
import com.viettel.coms.dto.StockTransRequest;
import com.viettel.coms.dto.SynStockTransDTO;
import com.viettel.coms.dto.SynStockTransDetailDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.dto.StockDTO;

/**
 * @author TruongBX3 Reformist CuongNV2
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("synStockTransDAO")
//public class SynStockTransDAO extends BaseFWDAOImpl<SynStockTransBO, Long> {
//
//    public SynStockTransDAO() {
//        this.model = new SynStockTransBO();
//    }
//
//    public SynStockTransDAO(Session session) {
//        this.session = session;
//    }

//hoanm1_20200523_vsmart_start
//public class SynStockTransDAO extends BaseFWDAOImpl<SynStockTransBO, Long> {
	public class SynStockTransDAO extends BaseFWDAOImpl<StockTransVsmartBO, Long> {
	
	  public SynStockTransDAO() {
	      this.model = new StockTransVsmartBO();
	  }
	
	  public SynStockTransDAO(Session session) {
	      this.session = session;
	  }
//  hoanm1_20200523_vsmart_end
    // Service Mobile STOCK_TRANS
    // DASHBOARD phieu xuat kho

    /**
     * GET Current TimeStamp
     *
     * @return String CurrentTime
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String res = strDate.substring(0, 4);
        Long year = Long.parseLong(res);
        StringBuilder str = new StringBuilder("01-01-").append(year.toString());
        return str.toString();
    }

    public static String getPrevTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String res = strDate.substring(0, 4);
        Long year = Long.parseLong(res)-1l;
        StringBuilder str = new StringBuilder("01-01-").append(year.toString());
        return str.toString();
    }
    
    public static String getPrevMonthFromCurrentTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");// dd/MM/yyyy
        Calendar c = Calendar.getInstance(); 
        c.setTime(new Date()); 
        c.add(Calendar.MONTH, -6);
        
        Date d = c.getTime();
        String res = sdfDate.format(d);
        return res;
    }
    
    /**
     * GET Count
     *
     * @param StockTransRequest request
     * @return CountConstructionTaskDTO
     */
    public CountConstructionTaskDTO getCount(SysUserRequest request) {

        StringBuilder sql1 = new StringBuilder("");
        sql1.append("WITH TBL AS(SELECT syn.ORDER_CODE,syn.CODE,syn.REAL_IE_TRANS_DATE,nvl(syn.CONFIRM,0)CONFIRM  ");
        sql1.append("FROM ");
        sql1.append(
                "SYS_USER a,USER_ROLE b,SYS_ROLE  c,USER_ROLE_DATA d, DOMAIN_DATA  e, DOMAIN_TYPE g,CAT_STATION station, CONSTRUCTION cst, SYN_STOCK_TRANS syn  ");
        sql1.append("WHERE ");
        sql1.append("a.SYS_USER_ID=b.SYS_USER_ID ");
        sql1.append("AND b.SYS_ROLE_ID=c.SYS_ROLE_ID ");
        sql1.append("AND c.CODE='COMS_GOVERNOR' ");
        sql1.append("AND b.USER_ROLE_ID=d.USER_ROLE_ID ");
        sql1.append("AND d.DOMAIN_DATA_ID=e.DOMAIN_DATA_ID ");
        sql1.append("AND e.DOMAIN_TYPE_ID=g.DOMAIN_TYPE_ID ");
        sql1.append("AND g.code='KTTS_LIST_PROVINCE' ");
        sql1.append("AND e.data_id=station.cat_province_id ");
        sql1.append("AND station.cat_station_id=cst.cat_station_id ");
        sql1.append("AND syn.construction_code=cst.code ");
        sql1.append("AND syn.type=2  ");
        sql1.append("AND syn.REAL_IE_TRANS_DATE >= to_date('"+ getPrevMonthFromCurrentTime() +"','dd/MM/yyyy') ");
        sql1.append("AND a.SYS_USER_ID= '" + request.getSysUserId() + "' ");

        sql1.append("UNION ALL ");

        sql1.append("SELECT ");
        sql1.append("a.ORDER_CODE,a.CODE,a.REAL_IE_TRANS_DATE,nvl(a.CONFIRM,0)CONFIRM ");
        sql1.append("FROM ");
        sql1.append("STOCK_TRANS a ");
        sql1.append("WHERE ");
        sql1.append("type      = 2 ");
        sql1.append("AND status= 2 and a.BUSINESS_TYPE =2 ");
        sql1.append("AND shipper_id= '" + request.getSysUserId() + "' ");
        sql1.append("AND a.REAL_IE_TRANS_DATE >= to_date('"+ getPrevMonthFromCurrentTime() +"','dd/MM/yyyy')) ");
        sql1.append("SELECT ");
        sql1.append("SUM(CASE WHEN confirm = 0 THEN 1 END) chotiepnhan, ");
        sql1.append("SUM(CASE WHEN confirm = 1 THEN 1 END) datiepnhan, ");
        sql1.append("SUM(CASE WHEN confirm = 2 THEN 1 END) datuchoi ");
        sql1.append("FROM tbl ");

        SQLQuery query1 = getSession().createSQLQuery(sql1.toString());

        query1.addScalar("chotiepnhan", new LongType());
        query1.addScalar("datiepnhan", new LongType());
        query1.addScalar("datuchoi", new LongType());

        query1.setResultTransformer(Transformers.aliasToBean(CountConstructionTaskDTO.class));

        return (CountConstructionTaskDTO) query1.list().get(0);
    }

    /**
     * GET ListSysStockTrans DTO
     *
     * @param StockTransRequest request
     * @return List<SynStockTransDTO>
     */
    public List<SynStockTransDTO> getListSysStockTransDTO(StockTransRequest request) {

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");
        sql.append("syn.ORDER_CODE orderCode, ");
        sql.append("syn.REAL_CONFIRM_TRANS_DATE realConfirmTransDate,");
//        hoanm1_20181229_start
        sql.append("to_char(syn.CODE) code, ");
        sql.append("to_date(to_char(syn.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') realIeTransDate, ");
        sql.append("NVL(syn.CONFIRM,0) confirm, ");
        sql.append("'A' stockType, ");
        sql.append("NVL(syn.STATE,0) state, ");
        sql.append("cons.CONSTRUCTION_ID constructionId, ");
        sql.append("cons.CODE consCode, ");
        sql.append("syn.STOCK_NAME synStockName, ");
        sql.append("syn.CREATED_BY_NAME synCreatedByName, ");
        sql.append("syn.CREATED_DATE synCreatedDate, ");
        sql.append("syn.LAST_SHIPPER_ID lastShipperId, ");
        sql.append("syn.RECEIVER_ID receiverId, ");
        sql.append("syn.SYN_STOCK_TRANS_ID synStockTransId ");
        sql.append(",syn.CONTRACT_CODE cntContractCode ");
        sql.append(" ,case when NVL(syn.CONFIRM,0)=0 and trunc(nvl(syn.UPDATED_DATE,sysdate))- to_date(to_char(syn.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >7 then 1 else 0 end overDateKPI ");
//      hoanm1_20200329_vsmart_start
        sql.append(" ,1 stockReceiveId,null stockReceiveCode,1 typeConfirm, null businessType ");
//      hoanm1_20200329_vsmart_end
        sql.append("FROM ");
        sql.append("SYN_STOCK_TRANS syn ");
        sql.append("LEFT JOIN CONSTRUCTION cons ");
        sql.append("ON syn.CONSTRUCTION_CODE    = cons.CODE ");
        sql.append("WHERE ");
        sql.append("syn.type                    = 2 ");
//        + "and NVL(syn.CONFIRM,0)=0 ");
//        hoanm1_20190706_start
        sql.append("and syn.REQ_TYPE in(2,3) and syn.DEST_TYPE=3 and syn.stock_code not like '%CTCT_%' ");
//        hoanm1_20190706_end
        sql.append("AND syn.REAL_IE_TRANS_DATE >= to_date('" + getPrevMonthFromCurrentTime() + "','dd/MM/yyyy') ");
        sql.append("AND (syn.LAST_SHIPPER_ID    = '" + request.getSysUserRequest().getSysUserId() + "' ");
        sql.append("OR syn.RECEIVER_ID          = '" + request.getSysUserRequest().getSysUserId() + "' )");
        //Huypq-20190919-start
        if(StringUtils.isNotBlank(request.getKeySearch())) {
        	sql.append(" AND (upper(to_char(syn.CODE)) like upper(:keySearch) escape '&' "
        			+ " OR upper(cons.CODE) like upper(:keySearch) escape '&' "
        			+ ") ");
        }
        
        if(request.getConfirm()!=null && request.getConfirm()!=-1l) {
        	sql.append(" AND NVL(syn.CONFIRM,0) = :confirm ");
        }
        
        if(request.getState()!=null && request.getState()!=-1l) {
        	sql.append(" AND syn.RECEIVER_ID is not null and NVL(syn.STATE,0) = :state ");
        }
        
        if(StringUtils.isNotBlank(request.getConstructionType())) {
        	sql.append(" AND 'A' = :constructionType ");
        }
        
        if(StringUtils.isNotBlank(request.getOverDateKPI())) {
        	sql.append(" AND (case when NVL(syn.CONFIRM,0)=0 and trunc(nvl(syn.UPDATED_DATE,sysdate))- to_date(to_char(syn.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >7 then 1 else 0 end)=:overDateKPI ");
        }
        //Huy-end
        sql.append("UNION ALL ");

        sql.append("SELECT ");
        sql.append("a.ORDER_CODE, ");
        sql.append("a.REAL_CONFIRM_TRANS_DATE realConfirmTransDate,");
        sql.append("to_char(a.CODE) code, ");
        sql.append("to_date(TO_CHAR(a.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') REAL_IE_TRANS_DATE, ");
        sql.append("NVL(a.CONFIRM,0)CONFIRM, ");
        sql.append("'B' stockType, ");
        sql.append("NVL(a.STATE,0) state, ");
        sql.append("cons.CONSTRUCTION_ID, ");
        sql.append("cons.CODE, ");
        sql.append("a.STOCK_NAME, ");
        sql.append("a.CREATED_BY_NAME, ");
        sql.append("a.CREATED_DATE, ");
        sql.append("a.LAST_SHIPPER_ID, ");
        sql.append("a.RECEIVER_ID, ");
        sql.append("a.STOCK_TRANS_ID ");
        sql.append(",null cntContractCode ");
        sql.append(" ,null overDateKPI ");
//        hoanm1_20200329_vsmart_start
        sql.append(" ,a.STOCK_RECEIVE_ID stockReceiveId,a.STOCK_RECEIVE_CODE stockReceiveCode, 1 typeConfirm, a.BUSINESS_TYPE businessType ");
//        hoanm1_20200329_vsmart_end
        sql.append("FROM ");
        sql.append("STOCK_TRANS a ");
        sql.append("LEFT JOIN CONSTRUCTION cons ");
        sql.append("ON cons.CONSTRUCTION_ID   = a.CONSTRUCTION_ID ");
        sql.append("WHERE ");
        sql.append("a.TYPE                    =2 ");
        sql.append("AND (a.CONFIRM IS NULL OR a.CONFIRM = 0) ");
        sql.append("AND a.STATUS              =2 and a.BUSINESS_TYPE in (2,27,18) ");
        sql.append("AND a.REAL_IE_TRANS_DATE >=  to_date('" + getPrevMonthFromCurrentTime() + "','dd/MM/yyyy') ");
        sql.append("AND (a.LAST_SHIPPER_ID    = '" + request.getSysUserRequest().getSysUserId() + "' ");
        sql.append("OR a.RECEIVER_ID          = '" + request.getSysUserRequest().getSysUserId() + "') ");
        //Huypq-20190919-start
        if(StringUtils.isNotBlank(request.getKeySearch())) {
        	sql.append(" AND (upper(to_char(a.CODE)) like upper(:keySearch) escape '&' "
        			+ " OR upper(cons.CODE) like upper(:keySearch) escape '&' "
        			+ ") ");
        }
        
        if(request.getConfirm()!=null && request.getConfirm()!=-1l) {
        	sql.append(" AND NVL(a.CONFIRM,0) = :confirm ");
        }
        
        if(request.getState()!=null && request.getState()!=-1l) {
        	sql.append(" AND a.RECEIVER_ID is not null and NVL(a.STATE,0) = :state ");
        }
        
        if(StringUtils.isNotBlank(request.getConstructionType())) {
        	sql.append(" AND 'B' = :constructionType ");
        }
        //Huy-end
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("orderCode", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("realIeTransDate", new DateType());
        query.addScalar("realConfirmTransDate", new StringType());
        query.addScalar("confirm", new StringType());
        query.addScalar("stockType", new StringType());
        query.addScalar("state", new StringType());

        query.addScalar("constructionId", new LongType());
        query.addScalar("consCode", new StringType());
        query.addScalar("synStockName", new StringType());
        query.addScalar("synCreatedByName", new StringType());
        query.addScalar("synCreatedDate", new DateType());
        query.addScalar("lastShipperId", new LongType());
        query.addScalar("receiverId", new LongType());

        query.addScalar("synStockTransId", new LongType());
//        hoanm1_20190704_start
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("overDateKPI", new StringType());
//        hoanm1_20190704_end
//        hoanm1_20200329_vsmart_start
        query.addScalar("stockReceiveId", new LongType());
        query.addScalar("stockReceiveCode", new StringType());
        query.addScalar("typeConfirm", new LongType());
        query.addScalar("businessType", new StringType());
//        hoanm1_20200329_vsmart_end
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        
        //Huypq-20190919-start-search on mobile
        if(StringUtils.isNotBlank(request.getKeySearch())) {
        	query.setParameter("keySearch", "%"+ request.getKeySearch() +"%");
        	queryCount.setParameter("keySearch", "%"+ request.getKeySearch() +"%");
        }
        
        if(request.getConfirm()!=null  && request.getConfirm()!=-1l) {
        	query.setParameter("confirm", request.getConfirm());
        	queryCount.setParameter("confirm", request.getConfirm());
        }
        
        if(request.getState()!=null  && request.getState()!=-1l) {
        	query.setParameter("state", request.getState());
        	queryCount.setParameter("state", request.getState());
        }
        
        if(StringUtils.isNotBlank(request.getConstructionType())) {
        	query.setParameter("constructionType", request.getConstructionType());
        	queryCount.setParameter("constructionType", request.getConstructionType());
        }
        
        if(StringUtils.isNotBlank(request.getOverDateKPI())) {
        	query.setParameter("overDateKPI", request.getOverDateKPI());
        	queryCount.setParameter("overDateKPI", request.getOverDateKPI());
        }
        //Huy-end
        
        //Huypq-20190907-start phân trang
        if (request.getPage() != null && request.getPageSize() != null) {
            query.setFirstResult((request.getPage().intValue() - 1) * request.getPageSize().intValue());
            query.setMaxResults(request.getPageSize().intValue());
        }
        request.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        //huy-end
        return query.list();
    }

    /**
     * GET List SysStockTrans DetailDTO
     *
     * @param SynStockTransDTO st
     * @return List<SynStockTransDetailDTO>
     */
    public List<SynStockTransDetailDTO> getListSysStockTransDetailDTO(SynStockTransDTO st) {
        StringBuilder sql = new StringBuilder("");
        if (st.getStockType().equals("A")) {
            sql.append("SELECT ");
            sql.append("SYN_STOCK_TRANS_DETAIL_id synStockTransDetailId, ");
            sql.append("a.SYN_STOCK_TRANS_ID synStockTransId, ");
            sql.append("a.GOODS_NAME||'('||a.GOODS_UNIT_NAME ||')'  goodsName, ");
            sql.append("a.AMOUNT_REAL amountReal, ");
            sql.append("a.REAL_RECIEVE_AMOUNT realRecieveAmount, ");
            sql.append("a.REAL_RECIEVE_DATE realRecieveDate, ");
            sql.append("a.FILE_PATH filePath, ");
            sql.append("a.GOODS_CODE goodsCode, ");
            sql.append("a.AMOUNT_ORDER amountOrder, ");
            sql.append("a.GOODS_UNIT_NAME goodsUnitName ");
			
		    sql.append(",1 orderId,1 goodsType,1 goodsTypeName, ");
	        sql.append("1 goodsId,1 goodsNameImport,");
	        sql.append("1 goodsIsSerial,1 goodsState,");
	        sql.append("1 goodsStateName,");
	        sql.append("1 goodsUnitId,1 totalPrice ");
            sql.append("FROM ");
            sql.append("SYN_STOCK_TRANS_DETAIL a ");
            sql.append("WHERE ");
            sql.append("SYN_STOCK_TRANS_ID= '" + st.getSynStockTransId() + "' ");
        } else {
            sql.append("SELECT ");
            sql.append("STOCK_TRANS_DETAIL_id synStockTransDetailId, ");
            sql.append("a.STOCK_TRANS_ID synStockTransId, ");
            sql.append("a.GOODS_NAME||'('||a.GOODS_UNIT_NAME ||')'  goodsName, ");
            sql.append("a.AMOUNT_REAL amountReal ");
            sql.append(",0 realRecieveAmount, null realRecieveDate, null filePath ");
//            hoanm1_20200329_vsmart_start
            sql.append(",ORDER_ID orderId,GOODS_TYPE goodsType,GOODS_TYPE_NAME goodsTypeName, ");
            sql.append("GOODS_ID goodsId,GOODS_CODE goodsCode,GOODS_NAME goodsNameImport,");
            sql.append("GOODS_IS_SERIAL goodsIsSerial,GOODS_STATE goodsState,");
            sql.append("GOODS_STATE_NAME goodsStateName,GOODS_UNIT_NAME goodsUnitName,");
            sql.append("GOODS_UNIT_ID goodsUnitId,AMOUNT_REAL amountOrder,TOTAL_PRICE totalPrice ");
//            hoanm1_20200329_vsmart_end
            sql.append("FROM ");
            sql.append("STOCK_TRANS_DETAIL a ");
            sql.append("WHERE ");
            sql.append("STOCK_TRANS_ID = '" + st.getSynStockTransId() + "' ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("synStockTransId", new LongType());
        query.addScalar("goodsName", new StringType());
        query.addScalar("amountReal", new DoubleType());
		query.addScalar("synStockTransDetailId", new LongType());
		//        hoanm1_20200329_vsmart_start
		query.addScalar("realRecieveAmount", new DoubleType());
        query.addScalar("realRecieveDate", new StringType());
        query.addScalar("filePath", new StringType());
        query.addScalar("orderId", new LongType());
        query.addScalar("goodsType", new StringType());
        query.addScalar("goodsTypeName", new StringType());
        query.addScalar("goodsId", new LongType());
        query.addScalar("goodsCode", new StringType());
        query.addScalar("goodsNameImport", new StringType());
        query.addScalar("goodsIsSerial", new StringType());
        query.addScalar("goodsState", new StringType());
        query.addScalar("goodsStateName", new StringType());
        query.addScalar("goodsUnitName", new StringType());
        query.addScalar("goodsUnitId", new LongType());
        query.addScalar("amountOrder", new DoubleType());
        query.addScalar("totalPrice", new DoubleType());
//        hoanm1_20200329_vsmart_end    
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDetailDTO.class));

        return query.list();
    }

    /**
     * GET List SysStockTrans DetailDTO
     *
     * @param SynStockTransDTO st
     * @return List<SynStockTransDetailDTO>
     */
    public SynStockTransDetailDTO getNewestTransactionId(StockTransRequest request) {
        StringBuilder sql = new StringBuilder("");
        if (request.getSynStockTransDto().getStockType().equals("A")) {
            sql.append("SELECT ");
            sql.append("max(ST_TRANSACTION_ID) maxTransactionId ");
            sql.append("FROM ST_TRANSACTION  ");
            sql.append("WHERE ");
            sql.append("STOCK_TRANS_ID = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");
            sql.append("AND TYPE           = '1' ");
        } else {
            sql.append("SELECT ");
            sql.append("max(ST_TRANSACTION_ID) maxTransactionId ");
            sql.append("FROM ST_TRANSACTION  ");
            sql.append("WHERE ");
            sql.append("STOCK_TRANS_ID = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");
            sql.append("AND TYPE           = '0' ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("maxTransactionId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDetailDTO.class));

        return (SynStockTransDetailDTO) query.list().get(0);
    }

    /**
     * GET List MerEntity
     *
     * @param StockTransRequest request
     * @return List<MerEntityDTO>
     */
    public List<MerEntityDTO> getListMerEntity(StockTransRequest request) {
        StringBuilder sql = new StringBuilder("");
        if (request.getSynStockTransDto().getStockType().equals("A")) {
            sql.append("SELECT ");
            sql.append("a.GOODS_NAME goodsName, ");
            sql.append("a.GOODS_CODE goodsCode, ");
            sql.append("a.amount||'('||a.GOODS_UNIT_NAME ||')' quantity, ");
            sql.append("a.SERIAL serial, ");
            sql.append("'' cntConstractCode, ");
            sql.append("b.STOCK_NAME stockName, ");
            sql.append("a.PART_NUMBER partNumber, ");
            sql.append("a.CAT_MANUFACTURER_NAME manufactureName, ");
            sql.append("a.CAT_PRODUCING_COUNTRY_NAME productionCountryName ");
            sql.append("FROM ");
            sql.append("SYN_STOCK_TRANS_DETAIL_SERIAL a, ");
            sql.append("SYN_STOCK_TRANS b ");
            sql.append("WHERE ");
            sql.append("a.SYN_STOCK_TRANS_ID = b.SYN_STOCK_TRANS_ID ");
            sql.append("AND a.SYN_STOCK_TRANS_DETAIL_ID = '"
                    + request.getSynStockTransDetailDto().getSynStockTransDetailId() + "' ");

        } else {

            sql.append("SELECT ");
            sql.append("a.GOODS_NAME goodsName, ");
            sql.append("a.GOODS_CODE goodsCode, ");
            sql.append("b.QUANTITY||'('||c.GOODS_UNIT_NAME ||')' quantity, ");
            sql.append("a.SERIAL serial, ");
            sql.append("a.CNT_CONTRACT_CODE cntConstractCode, ");
            sql.append("(SELECT name FROM cat_stock WHERE cat_stock.CAT_STOCK_ID = a.STOCK_ID)stockName, ");
            sql.append("a.PART_NUMBER partNumber, ");
            sql.append("a.MANUFACTURER_NAME manufactureName, ");
            sql.append("a.PRODUCING_COUNTRY_NAME productionCountryName ");
            sql.append("FROM ");
            sql.append("MER_ENTITY a, STOCK_TRANS_DETAIL_SERIAL b, STOCK_TRANS_DETAIL c  ");
            sql.append("WHERE  ");
            sql.append("a.MER_ENTITY_ID=b.MER_ENTITY_ID ");
            sql.append("AND b.STOCK_TRANS_DETAIL_ID = c.STOCK_TRANS_DETAIL_ID ");
            sql.append("AND c.STOCK_TRANS_DETAIL_ID = '"
                    + request.getSynStockTransDetailDto().getSynStockTransDetailId() + "' ");
        }

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("goodsName", new StringType());
        query.addScalar("goodsCode", new StringType());
        query.addScalar("serial", new StringType());
        query.addScalar("cntConstractCode", new StringType());
        query.addScalar("stockName", new StringType());
        query.addScalar("partNumber", new LongType());
        query.addScalar("manufactureName", new StringType());
        query.addScalar("productionCountryName", new StringType());
        query.addScalar("quantity", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(MerEntityDTO.class));

        return query.list();
    }

    /**
     * UPDATE DeliveryMaterials
     *
     * @param StockTransRequest request
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainConfirmByReceiver(StockTransRequest request) throws ParseException {
        StringBuilder sql = new StringBuilder("");
        if (request.getSynStockTransDto().getStockType().equals("A")) {
            sql.append("UPDATE ");
            sql.append("SYN_STOCK_TRANS sst ");
            sql.append("SET ");
            sql.append("sst.STATE           = 1, ");
            sql.append("sst.CONFIRM           = 1, ");
            sql.append("sst.LAST_SHIPPER_ID = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.UPDATED_BY      = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.RECEIVER_ID     = '" + request.getSynStockTransDto().getReceiverId() + "', ");
            sql.append("sst.UPDATED_DATE    = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("sst.SYN_STOCK_TRANS_ID  = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");

        } else {

            sql.append("UPDATE ");
            sql.append("STOCK_TRANS sst ");
            sql.append("SET ");
            sql.append("sst.STATE           = 1, ");
            sql.append("sst.LAST_SHIPPER_ID = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.UPDATED_BY      = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.RECEIVER_ID     = '" + request.getSynStockTransDto().getReceiverId() + "', ");
            sql.append("sst.UPDATED_DATE    = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("sst.STOCK_TRANS_ID  = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        return query.executeUpdate();
    }

    /**
     * UPDATE DeliveryMaterials
     *
     * @param StockTransRequest request
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainByReceiver(StockTransRequest request) throws ParseException {
        StringBuilder sql = new StringBuilder("");
        if (request.getSynStockTransDto().getStockType().equals("A")) {
            sql.append("UPDATE ");
            sql.append("SYN_STOCK_TRANS sst ");
            sql.append("SET ");
            sql.append("sst.STATE               = 2, ");
            sql.append("sst.UPDATED_BY          = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.CONFIRM_DESCRIPTION = '" + request.getSynStockTransDto().getConfirmDescription() + "', ");
            sql.append("sst.UPDATED_DATE        = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("sst.SYN_STOCK_TRANS_ID  = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");

        } else {

            sql.append("UPDATE ");
            sql.append("STOCK_TRANS sst ");
            sql.append("SET ");
            sql.append("sst.STATE               = 2, ");
            sql.append("sst.UPDATED_BY          = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.CONFIRM_DESCRIPTION = '" + request.getSynStockTransDto().getConfirmDescription() + "', ");
            sql.append("sst.UPDATED_DATE        = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("sst.STOCK_TRANS_ID      = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        return query.executeUpdate();
    }

    /**
     * Update Receiver State
     *
     * @param StockTransRequest request
     * @return int result
     * @throws ParseException
     */
    public int UpdateStockTransState(StockTransRequest request) throws ParseException {
        StringBuilder sql = new StringBuilder("");
        if (request.getSynStockTransDto().getStockType().equals("A")) {
            sql.append("UPDATE ");
            sql.append("SYN_STOCK_TRANS sst ");
            sql.append("SET ");
            sql.append("sst.STATE                   = 0, ");
            sql.append("sst.UPDATED_BY              = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("sst.RECEIVER_ID             = '" + request.getSysUserReceiver().getSysUserId() + "', ");
            sql.append("sst.CONFIRM_DESCRIPTION     = '" + request.getSynStockTransDto().getDescription() + "', ");
            sql.append("sst.UPDATED_DATE            = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("sst.SYN_STOCK_TRANS_ID  = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");

        } else {

            sql.append("UPDATE ");
            sql.append("STOCK_TRANS st ");
            sql.append("SET ");
            sql.append("st.STATE                   = 0, ");
            sql.append("st.UPDATED_BY              = '" + request.getSysUserRequest().getSysUserId() + "', ");
            sql.append("st.RECEIVER_ID             = '" + request.getSysUserReceiver().getSysUserId() + "', ");
            sql.append("st.CONFIRM_DESCRIPTION     = '" + request.getSynStockTransDto().getDescription() + "', ");
            sql.append("st.UPDATED_DATE            = '" + getCurrentTime() + "' ");
            sql.append("WHERE ");
            sql.append("st.STOCK_TRANS_ID          = '" + request.getSynStockTransDto().getSynStockTransId() + "' ");

        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        return query.executeUpdate();
    }

    /**
     * Save StTransaction
     *
     * @param StockTransRequest request
     * @return int result
     * @throws ParseException
     */
    public int SaveStTransaction(StockTransRequest request) throws ParseException {

        boolean isInvestor = request.getSynStockTransDto().getStockType().equals("A");
        String type;
        if (isInvestor) {
            type = "1";
        } else {
            type = "0";
        }

        StringBuilder sql = new StringBuilder("");
        sql.append("INSERT INTO ST_TRANSACTION ");
        sql.append(
                "(ST_TRANSACTION_ID, DESCRIPTION, OLD_LAST_SHIPPER_ID, NEW_LAST_SHIPPER_ID ,STOCK_TRANS_ID, TYPE, CONFIRM, CREATED_DATE, CREATED_USER_ID) ");
        sql.append("VALUES ( ");
        sql.append("ST_TRANSACTION_seq.nextval, ");
        sql.append("'" + request.getSynStockTransDto().getDescription() + "', ");
        sql.append("'" + request.getSysUserRequest().getSysUserId() + "', ");
        sql.append("'" + request.getSysUserReceiver().getSysUserId() + "', ");
        sql.append("'" + request.getSynStockTransDto().getSynStockTransId() + "', ");
        sql.append("'" + type + "', ");
        sql.append("'0', ");
        sql.append("'" + getCurrentTime() + "', ");
        sql.append("'" + request.getSysUserRequest().getSysUserId() + "' ");
        sql.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        return query.executeUpdate();
    }

    /**
     * Update Stocktrain History
     *
     * @param StockTransRequest request, boolean isInvestor
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainHistory(StockTransRequest request, boolean isInvestor) throws ParseException {
        StringBuilder sqlConfirmred = new StringBuilder("");
        sqlConfirmred.append("UPDATE ");
        sqlConfirmred.append("ST_TRANSACTION ");
        sqlConfirmred.append("SET ");
        sqlConfirmred.append("CONFIRM_DATE         = '" + getCurrentTime() + "', ");
        // sqlConfirmred.append("LAST_SHIPPER_ID = '" +
        // request.getSysUserRequest().getSysUserId() + "', ");
        sqlConfirmred.append("CONFIRM              = '1' ");
        sqlConfirmred.append("WHERE ");
        sqlConfirmred.append("STOCK_TRANS_ID  = :stockTransId ");

        SQLQuery queryConfirmred = getSession().createSQLQuery(sqlConfirmred.toString());
        queryConfirmred.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());

        return queryConfirmred.executeUpdate();
    }

    /**
     * Update Stocktrain History By Receiver
     *
     * @param StockTransRequest request, boolean isInvestor
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainConfirmByLastShipper(StockTransRequest request, boolean isInvestor,
                                                    SynStockTransDetailDTO newestTransactionId) throws ParseException {
        StringBuilder sqlConfirmred = new StringBuilder("");
        sqlConfirmred.append("UPDATE ");
        sqlConfirmred.append("ST_TRANSACTION ");
        sqlConfirmred.append("SET ");
        sqlConfirmred.append("CONFIRM_DATE         = '" + getCurrentTime() + "', ");
        sqlConfirmred.append("CONFIRM              = '1' ");
        sqlConfirmred.append("WHERE ");
        if (newestTransactionId != null && newestTransactionId.getMaxTransactionId()!=null) {
            sqlConfirmred.append("ST_TRANSACTION_ID  = :stTransactionId ");
        } else {
            sqlConfirmred.append("STOCK_TRANS_ID  = :stockTransId ");
        }

        SQLQuery queryConfirmred = getSession().createSQLQuery(sqlConfirmred.toString());

        if (newestTransactionId != null && newestTransactionId.getMaxTransactionId()!=null) {
            queryConfirmred.setParameter("stTransactionId", newestTransactionId.getMaxTransactionId());
        } else {
            queryConfirmred.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());
        }

        return queryConfirmred.executeUpdate();
    }

    /**
     * Update Stocktrain History By Refused Confirm
     *
     * @param StockTransRequest request, boolean isInvestor
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainHistoryByRefusedByReceiver(StockTransRequest request, boolean isInvestor,
                                                          SynStockTransDetailDTO newestTransactionId) throws ParseException {
        StringBuilder sqlRefusedConfirm = new StringBuilder("");
        sqlRefusedConfirm.append("UPDATE ");
        sqlRefusedConfirm.append("ST_TRANSACTION ");
        sqlRefusedConfirm.append("SET ");
        sqlRefusedConfirm.append("CONFIRM_DATE         = '" + getCurrentTime() + "', ");
        sqlRefusedConfirm.append("CONFIRM              = '2' ");
        sqlRefusedConfirm.append("WHERE ");

        if (newestTransactionId != null) {
            sqlRefusedConfirm.append("ST_TRANSACTION_ID  = :stTransactionId ");
        } else {
            sqlRefusedConfirm.append("STOCK_TRANS_ID  = :stockTransId ");
        }

        SQLQuery queryChapNhan = getSession().createSQLQuery(sqlRefusedConfirm.toString());

        if (newestTransactionId != null) {
            queryChapNhan.setParameter("stTransactionId", newestTransactionId.getMaxTransactionId());
        } else {
            queryChapNhan.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());
        }

        return queryChapNhan.executeUpdate();
    }

    /**
     * Update Stocktrain History Refused Confirm By Receiver
     *
     * @param StockTransRequest request, boolean isInvestor
     * @return int result
     * @throws ParseException
     */
    public int UpdateStocktrainHistoryRefusedByLastShipper(StockTransRequest request, boolean isInvestor)
            throws ParseException {
        StringBuilder sqlRefusedConfirm = new StringBuilder("");
        sqlRefusedConfirm.append("UPDATE ");
        sqlRefusedConfirm.append("ST_TRANSACTION ");
        sqlRefusedConfirm.append("SET ");
        sqlRefusedConfirm.append("CONFIRM_DATE         = '" + getCurrentTime() + "', ");
        sqlRefusedConfirm.append("CONFIRM              = '2' ");
        sqlRefusedConfirm.append("WHERE ");
        sqlRefusedConfirm.append("STOCK_TRANS_ID  = :stockTransId ");

        SQLQuery queryChapNhan = getSession().createSQLQuery(sqlRefusedConfirm.toString());
        queryChapNhan.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());

        return queryChapNhan.executeUpdate();
    }

    /**
     * Update Stock Trans
     *
     * @param StockTransRequest request
     * @return int result
     */
    public int updateStockTrans(StockTransRequest request) {
        if (request.getSysUserRequest().getFlag() == 1) {
            StringBuilder sqlChapNhan = new StringBuilder("");
            sqlChapNhan.append("UPDATE ");
            sqlChapNhan.append("STOCK_TRANS st ");
            sqlChapNhan.append("SET ");
            sqlChapNhan.append("st.CONFIRM         = 1, ");
            sqlChapNhan.append("st.sign_state = case when st.confirm_status is null and st.sign_state = 1 then 1 ");
            sqlChapNhan.append( " when st.confirm_status is null and st.sign_state = 2 then 2 ");
            sqlChapNhan.append( " when st.confirm_status is null and st.sign_state = 3 then 3 ");
            sqlChapNhan.append( " when st.confirm_status = 1 and st.sign_state = 1 then 1 ");
            sqlChapNhan.append( " when st.confirm_status = 1 and st.sign_state = 2 then 3 ");
            sqlChapNhan.append( " when st.confirm_status = 1 and st.sign_state = 3 then 3 end, ");
            sqlChapNhan.append("st.UPDATED_BY      = :sysUserId, ");
            sqlChapNhan.append("st.UPDATED_DATE    = :newDate, ");
            sqlChapNhan.append("st.LAST_SHIPPER_ID = :sysUserId ");
            sqlChapNhan.append("WHERE ");
            sqlChapNhan.append("st.STOCK_TRANS_ID  = :stockTransId ");
            SQLQuery queryChapNhan = getSession().createSQLQuery(sqlChapNhan.toString());

            queryChapNhan.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
            queryChapNhan.setParameter("newDate", new Date());
            queryChapNhan.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());

            return queryChapNhan.executeUpdate();
        }
        StringBuilder sqlTuChoi = new StringBuilder("");
        sqlTuChoi.append("UPDATE ");
        sqlTuChoi.append("STOCK_TRANS st ");
        sqlTuChoi.append("SET ");
        sqlTuChoi.append("st.CONFIRM             = 2, ");
        sqlTuChoi.append("st.UPDATED_BY          = :sysUserId, ");
        sqlTuChoi.append("st.UPDATED_DATE        = :newDate, ");
        sqlTuChoi.append("st.CONFIRM_DESCRIPTION = :confirmDescription ");
        sqlTuChoi.append("WHERE ");
        sqlTuChoi.append("st.STOCK_TRANS_ID      = :stockTransId ");
        SQLQuery queryTuChoi = getSession().createSQLQuery(sqlTuChoi.toString());

        queryTuChoi.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
        queryTuChoi.setParameter("newDate", new Date());
        queryTuChoi.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());
        queryTuChoi.setParameter("confirmDescription", request.getSynStockTransDto().getConfirmDescription());

        return queryTuChoi.executeUpdate();
    }

    /**
     * Update SynStockTrans
     *
     * @param StockTransRequest request
     * @return int result
     */
    public int updateSynStockTrans(StockTransRequest request) {
        if (request.getSysUserRequest().getFlag() >= 1) {
        	
            StringBuilder sqlChapNhan = new StringBuilder("");
            sqlChapNhan.append("UPDATE ");
            sqlChapNhan.append("SYN_STOCK_TRANS ");
            sqlChapNhan.append("SET ");
            sqlChapNhan.append("CONFIRM = 1, ");
            sqlChapNhan.append("UPDATED_BY         = :sysUserId, ");
            sqlChapNhan.append("UPDATED_DATE       = :newDate, ");
            sqlChapNhan.append("REAL_CONFIRM_TRANS_DATE       = :realConfirmTransDate ");
            // sqlChapNhan.append("LAST_SHIPPER_ID = :sysUserId ");
            sqlChapNhan.append("WHERE ");
            sqlChapNhan.append("SYN_STOCK_TRANS_ID = :stockTransId ");

            SQLQuery queryChapNhan = getSession().createSQLQuery(sqlChapNhan.toString());
            queryChapNhan.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
            queryChapNhan.setParameter("newDate", new Date());
            queryChapNhan.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());
            queryChapNhan.setParameter("realConfirmTransDate", request.getSynStockTransDto().getRealConfirmTransDate());

            return queryChapNhan.executeUpdate();
        }
        StringBuilder sqlTuChoi = new StringBuilder("");

        sqlTuChoi.append("UPDATE ");
        sqlTuChoi.append("SYN_STOCK_TRANS ");
        sqlTuChoi.append("SET ");
        sqlTuChoi.append("CONFIRM = 2, ");
        sqlTuChoi.append("UPDATED_BY          = :sysUserId, ");
        sqlTuChoi.append("UPDATED_DATE        = :newDate, ");
        sqlTuChoi.append("CONFIRM_DESCRIPTION = :confirmDescription ");
        sqlTuChoi.append("WHERE ");
        sqlTuChoi.append("SYN_STOCK_TRANS_ID  = :stockTransId ");
        SQLQuery queryTuChoi = getSession().createSQLQuery(sqlTuChoi.toString());

        queryTuChoi.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
        queryTuChoi.setParameter("newDate", new Date());
        queryTuChoi.setParameter("confirmDescription", request.getSynStockTransDto().getConfirmDescription());
        queryTuChoi.setParameter("stockTransId", request.getSynStockTransDto().getSynStockTransId());

        return queryTuChoi.executeUpdate();

    }

    /**
     * get CongNo
     *
     * @param SysUserRequest request
     * @return List<MerEntityDTO>
     */
    public List<MerEntityDTO> getCongNo(SysUserRequest request) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT CASE ");
        sql.append("WHEN type=1  ");
        sql.append("THEN 'A'  ");
        sql.append("ELSE 'B'  ");
        sql.append("END stockType, ");
        sql.append("a.CONSTRUCTION_CODE constructionCode, ");
        sql.append("a.GOODS_NAME||'('||a.GOODS_UNIT_NAME||')' goodsName, ");
        sql.append("a.SERIAL,nvl(a.AMOUNT,0) numbeRepository  ");
        sql.append("FROM ");
        sql.append("SYN_STOCK_TOTAL a ");
        sql.append("WHERE ");
        sql.append("a.SYS_USER_ID = :sysUserId ");
        sql.append("ORDER BY ");
        sql.append("a.CONSTRUCTION_CODE, ");
        sql.append("SERIAL desc, ");
        sql.append("a.GOODS_NAME||'('||a.GOODS_UNIT_NAME||')' ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("stockType", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("goodsName", new StringType());
        query.addScalar("serial", new StringType());
//    	query.addScalar("goodsIsSerial", new StringType());
        query.addScalar("numbeRepository", new LongType());
        query.setParameter("sysUserId", request.getSysUserId());
//    	query.setParameter("createDate", getCurrentTimeStamp());
        query.setResultTransformer(Transformers.aliasToBean(MerEntityDTO.class));

        return query.list();
    }

    /**
     * get CongNo
     *
     * @param SysUserRequest request
     * @return List<MerEntityDTO>
     */
    public CountConstructionTaskDTO countMaterials(SysUserRequest request) {
        StringBuilder sql = new StringBuilder("");
        sql.append("WITH TBL AS ");
        sql.append("  ( ");
        sql.append("  SELECT  NVL(syn.CONFIRM,0) CONFIRM ");
        sql.append("  FROM ");
        sql.append("    SYN_STOCK_TRANS syn ");
        sql.append("    LEFT JOIN CONSTRUCTION cons ");
        sql.append("    ON syn.CONSTRUCTION_CODE    = cons.CODE ");
        sql.append("    WHERE ");
        sql.append("    syn.type                    = 2 ");
        sql.append("    and syn.REQ_TYPE in(2,3) and syn.DEST_TYPE=3 and syn.stock_code not like '%CTCT_%' ");
//        sql.append("    AND syn.REAL_IE_TRANS_DATE >= to_date('" + getCurrentTimeStamp() + "','dd/MM/yyyy') ");
        sql.append("    AND syn.REAL_IE_TRANS_DATE >= sysdate-90 ");
        sql.append("    AND (syn.LAST_SHIPPER_ID    = :sysUserId ");
        sql.append("    OR syn.RECEIVER_ID          = :sysUserId) ");
        sql.append("  UNION ALL ");
        sql.append("  SELECT  NVL(a.CONFIRM,0) ");
        sql.append("  FROM ");
        sql.append("    STOCK_TRANS a ");
        sql.append("    LEFT JOIN CONSTRUCTION cons ");
        sql.append("    ON cons.CONSTRUCTION_ID   = a.CONSTRUCTION_ID ");
        sql.append("    WHERE ");
        sql.append("    a.TYPE                    = 2 ");
        sql.append("    AND a.STATUS              = 2 and a.BUSINESS_TYPE = 2 ");
//        sql.append("    AND a.REAL_IE_TRANS_DATE >=  to_date('" + getCurrentTimeStamp() + "','dd/MM/yyyy') ");
        sql.append("    AND a.REAL_IE_TRANS_DATE >=  sysdate-90 ");
        sql.append("    AND (a.LAST_SHIPPER_ID    = :sysUserId ");
        sql.append("    OR a.RECEIVER_ID          = :sysUserId) ");
        sql.append("  ) ");
        sql.append("SELECT  ");
        sql.append("  NVL(SUM(CASE WHEN confirm = 0 THEN 1 END),0) chotiepnhan, ");
        sql.append("  NVL(SUM(CASE WHEN confirm = 1 THEN 1 END),0) datiepnhan, ");
        sql.append("  NVL(SUM(CASE WHEN confirm = 2 THEN 1 END),0) datuchoi ");
        sql.append("FROM tbl ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("sysUserId", request.getSysUserId());
        query.addScalar("chotiepnhan", new LongType());
        query.addScalar("datiepnhan", new LongType());
        query.addScalar("datuchoi", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(CountConstructionTaskDTO.class));

        return (CountConstructionTaskDTO) query.list().get(0);
    }

    /**
     * Get current time
     *
     * @return Current time
     * @throws ParseException
     */
    private String getCurrentTime() throws ParseException {
        Date now = new Date();
        String dateNow = now.toString();
        // Tue May 22 13:56:18 GMT+07:00 2018
        SimpleDateFormat dt = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date dateString = dt.parse(dateNow);
        SimpleDateFormat formater = new SimpleDateFormat("dd-MMMM-yy");
        return formater.format(dateString);
    }

    // END SERVICE MOBILE STOCK_TRANS

    //VietNT_20190116_start
    /**
     * Get all records with businessType = 1, confirm = 2, type = 2
     * @param criteria
     * @return
     */
    public List<SynStockTransDTO> doSearch(SynStockTransDTO criteria) {
        StringBuilder sql = this.createDoSearchBaseQuery();
        sql.append(", g.name sysGroupName ");
        sql.append(", cs.code customField ");
        sql.append(", NULL AS isSyn ");
        sql.append(", 1 AS synType ");
        sql.append("FROM SYN_STOCK_TRANS sst ");
        sql.append("LEFT JOIN CONSTRUCTION t on t.construction_id = sst.construction_id " +
//        hoanm1_21090706_start
//                "LEFT JOIN SYS_GROUP g on g.SYS_GROUP_ID = t.SYS_GROUP_ID " +
                "LEFT JOIN SYS_GROUP g on sst.SYS_GROUP_ID = g.SYS_GROUP_ID  " +
//                hoanm1_21090706_end
                "LEFT JOIN CAT_STATION cs on cs.cat_station_id = t.cat_station_id " +
                "WHERE 1=1 ");
        sql.append("AND sst.BUSSINESS_TYPE in(1,2) ");
//        sql.append("AND sst.CONFIRM = 2 ");
        sql.append("AND sst.TYPE = '2' ");
        //VietNT_20190219_start
        if (StringUtils.isNotEmpty(criteria.getConfirm())) {
            sql.append("AND trim(sst.CONFIRM) = :confirm ");
        } else {
            sql.append("AND trim(sst.CONFIRM) IN (0, 2) ");
        }
        //VietNT_end

        // query by
        // orderCode
        if (StringUtils.isNotEmpty(criteria.getOrderCode())) {
            sql.append("AND upper(sst.ORDER_CODE) like upper(:orderCode) escape '&' ");
        }

        // code
        if (StringUtils.isNotEmpty(criteria.getCode())) {
            sql.append("AND upper(sst.CODE) like upper(:code) escape '&' ");
        }

        // constructionCode
        if (StringUtils.isNotEmpty(criteria.getConstructionCode())) {
            sql.append("AND upper(sst.CONSTRUCTION_CODE) like upper(:constructionCode) escape '&' ");
        }

        // sysGroupId
        if (null != criteria.getSysGroupId()) {
//            sql.append("AND t.sys_group_id = :sysGroupId ");
            sql.append("AND sst.sys_group_id = :sysGroupId ");
        }

        // query by realIeTransDate
        if (null != criteria.getDateFrom()) {
            sql.append("AND TRUNC(sst.CREATED_DATE) >= :dateFrom ");
        }
        if (null != criteria.getDateTo()) {
            sql.append("AND TRUNC(sst.CREATED_DATE) <= :dateTo ");
        }

        sql.append("ORDER BY SYN_STOCK_TRANS_ID DESC ");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        //VietNT_20190219_start
        if (StringUtils.isNotEmpty(criteria.getConfirm())) {
            query.setParameter("confirm", criteria.getConfirm());
            queryCount.setParameter("confirm", criteria.getConfirm());
        }
        //VietNT_end

        if (StringUtils.isNotEmpty(criteria.getOrderCode())) {
            query.setParameter("orderCode", "%" + criteria.getOrderCode() + "%");
            queryCount.setParameter("orderCode", "%" + criteria.getOrderCode() + "%");
        }

        // code
        if (StringUtils.isNotEmpty(criteria.getCode())) {
            query.setParameter("code", "%" + criteria.getCode() + "%");
            queryCount.setParameter("code", "%" + criteria.getCode() + "%");
        }

        // constructionCode
        if (StringUtils.isNotEmpty(criteria.getConstructionCode())) {
            query.setParameter("constructionCode", "%" + criteria.getConstructionCode() + "%");
            queryCount.setParameter("constructionCode", "%" + criteria.getConstructionCode() + "%");
        }

        // sysGroupId
        if (null != criteria.getSysGroupId()) {
            query.setParameter("sysGroupId", criteria.getSysGroupId());
            queryCount.setParameter("sysGroupId", criteria.getSysGroupId());
        }

        //query by realIeTransDate
        if (null != criteria.getDateFrom()) {
            query.setParameter("dateFrom", criteria.getDateFrom());
            queryCount.setParameter("dateFrom", criteria.getDateFrom());
        }
        if (null != criteria.getDateTo()) {
            query.setParameter("dateTo", criteria.getDateTo());
            queryCount.setParameter("dateTo", criteria.getDateTo());
        }

        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        this.addQueryScalarDoSearch(query);
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("isSyn", new StringType());
        query.addScalar("synType", new LongType());
        query.addScalar("customField", new StringType());
        this.setPageSize(criteria, query, queryCount);

        return query.list();
    }

    @SuppressWarnings("Duplicates")
    public <T extends ComsBaseFWDTO> void setPageSize(T obj, SQLQuery query, SQLQuery queryCount) {
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
            query.setMaxResults(obj.getPageSize());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    }

    private StringBuilder createDoSearchBaseQuery() {
        StringBuilder sql = new StringBuilder("SELECT " +
                "sst.SYN_STOCK_TRANS_ID synStockTransId, " +
                "sst.ORDER_ID orderId, " +
                "sst.ORDER_CODE orderCode, " +
                "sst.CODE code, " +
                "sst.TYPE type, " +
                "sst.STOCK_ID stockId, " +
                "sst.STATUS status, " +
                "sst.SIGN_STATE signState, " +
                "sst.FROM_STOCK_TRANS_ID fromStockTransId, " +
                "sst.DESCRIPTION description, " +
                "sst.CREATED_BY_NAME createdByName, " +
                "sst.CREATED_DEPT_ID createdDeptId, " +
                "sst.CREATED_DEPT_NAME createdDeptName, " +
                "sst.UPDATED_BY updatedBy, " +
                "sst.UPDATED_DATE updatedDate, " +
                "sst.REAL_IE_TRANS_DATE realIeTransDate, " +
                "sst.REAL_IE_USER_ID realIeUserId, " +
                "sst.REAL_IE_USER_NAME realIeUserName, " +
                "sst.SHIPPER_ID shipperId, " +
                "sst.SHIPPER_NAME shipperName, " +
                "sst.CANCEL_DATE cancelDate, " +
                "sst.CANCEL_BY cancelBy, " +
                "sst.CANCEL_REASON_NAME cancelReasonName, " +
                "sst.CANCEL_DESCRIPTION cancelDescription, " +
                "sst.VOFFICE_TRANSACTION_CODE vofficeTransactionCode, " +
                "sst.SHIPMENT_CODE shipmentCode, " +
                "sst.CONTRACT_CODE contractCode, " +
                "sst.PROJECT_CODE projectCode, " +
                "sst.CUST_ID custId, " +
                "sst.CREATED_BY createdBy, " +
                "sst.CREATED_DATE createdDate, " +
//                hoanm1_20190706_start
//                "sst.CANCEL_BY_NAME cancelByName, " +
                " (select email from sys_user sys where sys.sys_user_id=sst.LAST_SHIPPER_ID)cancelByName, "+
//                hoanm1_20190706_end
                "sst.BUSSINESS_TYPE_NAME bussinessTypeName, " +
                "sst.IN_ROAL inRoal, " +
                "sst.DEPT_RECEIVE_NAME deptReceiveName, " +
                "sst.DEPT_RECEIVE_ID deptReceiveId, " +
                "sst.STOCK_RECEIVE_ID stockReceiveId, " +
                "sst.STOCK_RECEIVE_CODE stockReceiveCode, " +
                "sst.PARTNER_ID partnerId, " +
                "sst.SYN_TRANS_TYPE synTransType, " +
                "sst.STOCK_CODE stockCode, " +
                "sst.STOCK_NAME stockName, " +
                "sst.BUSSINESS_TYPE bussinessType, " +
                "sst.CONSTRUCTION_ID constructionId, " +
                "sst.CONFIRM confirm, " +
                "sst.CONSTRUCTION_CODE constructionCode, " +
                "sst.LAST_SHIPPER_ID lastShipperId, " +
                "sst.CONFIRM_DESCRIPTION confirmDescription, " +
                "sst.RECEIVER_ID receiverId, " +
                "sst.STATE state ");
        return sql;
    }

    private void addQueryScalarDoSearch(SQLQuery query) {
        query.addScalar("synStockTransId", new LongType());
        query.addScalar("orderId", new LongType());
        query.addScalar("orderCode", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("type", new StringType());
        query.addScalar("stockId", new LongType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("fromStockTransId", new LongType());
        query.addScalar("description", new StringType());
        query.addScalar("createdByName", new StringType());
        query.addScalar("createdDeptId", new LongType());
        query.addScalar("createdDeptName", new StringType());
        query.addScalar("updatedBy", new LongType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("realIeTransDate", new DateType());
        query.addScalar("realIeUserId", new StringType());
        query.addScalar("realIeUserName", new StringType());
        query.addScalar("shipperId", new LongType());
        query.addScalar("shipperName", new StringType());
        query.addScalar("cancelDate", new DateType());
        query.addScalar("cancelBy", new LongType());
        query.addScalar("cancelReasonName", new StringType());
        query.addScalar("cancelDescription", new StringType());
        query.addScalar("vofficeTransactionCode", new StringType());
        query.addScalar("shipmentCode", new StringType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("projectCode", new StringType());
        query.addScalar("custId", new LongType());
        query.addScalar("createdBy", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("cancelByName", new StringType());
        query.addScalar("bussinessTypeName", new StringType());
        query.addScalar("inRoal", new StringType());
        query.addScalar("deptReceiveName", new StringType());
        query.addScalar("deptReceiveId", new LongType());
        query.addScalar("stockReceiveId", new LongType());
        query.addScalar("stockReceiveCode", new StringType());
        query.addScalar("partnerId", new LongType());
        query.addScalar("synTransType", new StringType());
        query.addScalar("stockCode", new StringType());
        query.addScalar("stockName", new StringType());
        query.addScalar("bussinessType", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("confirm", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("lastShipperId", new LongType());
        query.addScalar("confirmDescription", new StringType());
        query.addScalar("receiverId", new LongType());
        query.addScalar("state", new StringType());
    }

//    public int updateForwardSynStockTrans(Long synStockTransId, Long provinceChiefId, String provinceChiefName, Date updateDate) {

    /**
     * Update field of SYN_STOCK_TRANS table for action forward to group:
     * shipper_id = provinceChiefId
     * last_shipper_id = provinceChiefId
     * shipper_name = provinceChiefName
     * update_by = logged in user
     * update_date = sysDate
     * confirm = 0
     *
     * @param updateInfo update info
     * @return Number of aff
     */
    public int updateForwardSynStockTrans(SynStockTransDTO updateInfo) {
        // Cập nhật syn_stock_trans.shipper_id,
        // syn_stock_trans.shipper_name,
        // syn_stock_trans.last_shipper_id
        // theo tỉnh trưởng của đơn vị được chọn và tỉnh của công trình.
        // Cập nhật syn_stock_trans.confirm = 0
        String sql = "UPDATE SYN_STOCK_TRANS sst " +
                "SET " +
                "sst.shipper_id = :shipperId, " +
                "sst.shipper_name = :shipperName, " +
                "sst.last_shipper_id = :shipperId, " +
                "sst.updated_by = :updateBy, " +
                "sst.updated_date = :updateDate, " +
                "sst.confirm = 0 " +

                "WHERE " +
                "sst.SYN_STOCK_TRANS_ID = :synStockTransId ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("synStockTransId", updateInfo.getSynStockTransId());
        query.setParameter("shipperId", updateInfo.getSysUserId());
        query.setParameter("shipperName", updateInfo.getSysUserName());
        query.setParameter("updateBy", updateInfo.getUpdatedBy());
        query.setParameter("updateDate", updateInfo.getUpdatedDate());
        return query.executeUpdate();
    }

    /**
     * Update field of CONSTRUCTION table for action forward to group
     * sys_group_id = sysGroupId
     *
     * @param sysGroupId     id of Group forward to
     * @param constructionId id of construction
     * @return Number of affected records
     */
    public int updateConstructionForwardSynStockTrans(Long sysGroupId, Long constructionId) {
        StringBuilder sql = new StringBuilder("UPDATE CONSTRUCTION t " +
                "SET " +
                "t.sys_group_id = :sysGroupId " +
                "WHERE " +
                "t.construction_id = :constructionId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("sysGroupId", sysGroupId);
        query.setParameter("constructionId", constructionId);

        return query.executeUpdate();
    }

    public SynStockTransDTO getProvinceChiefId(Long sysGroupId, Long constructionId) {
        String sql = "select " +
                "cfu.sys_user_id sysUserId, " +
                "su.full_name sysUserName " +
                "from config_user_province cfu " +
                "left join sys_user su on cfu.sys_user_id = su.sys_user_id " +
                "where " +
                "cfu.sys_group_id = :sysGroupId " +
                "and cfu.cat_province_id = " +
                    "(SELECT cs.cat_province_id " +
                    "FROM construction t " +
                    "LEFT JOIN cat_station cs ON cs.cat_station_id = t.cat_station_id " +
                    "WHERE t.construction_id = :constructionId) ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        query.setParameter("sysGroupId", sysGroupId);
        query.setParameter("constructionId", constructionId);
        query.addScalar("sysUserId", new LongType());
        query.addScalar("sysUserName", new StringType());

        return (SynStockTransDTO) query.uniqueResult();
    }
    //VietNT_end
    //VietNT_20190125_start
    public List<SysUserDTO> findUsersWithPermission(String permission, Long sysGroupId) {
        String sql = "SELECT " +
                "DISTINCT a.SYS_USER_ID " +
                ", a.EMAIL email " +
                ", a.PHONE_NUMBER phone " +
                "FROM " +
                "sys_user a, " +
                "user_role b, " +
                "sys_role c, " +
                "user_role_data d, " +
                "domain_data e, " +
                "role_permission role_per, " +
                "permission pe, " +
                "operation op, " +
                "ad_resource ad " +
                "WHERE " +
                "a.sys_user_id = b.sys_user_id " +
                "AND b.sys_role_id = c.sys_role_id " +
                "AND b.user_role_id = d.user_role_id " +
                "AND d.domain_data_id = e.domain_data_id " +
                "AND c.sys_role_id = role_per.sys_role_id " +
                "AND role_per.permission_id = pe.permission_id " +
                "AND pe.operation_id = op.operation_id " +
                "AND pe.ad_resource_id = ad.ad_resource_id " +
                "AND upper(op.code ||' ' ||ad.code) LIKE :permission ";
        if (null != sysGroupId) {
            sql += "AND e.data_id = :sysGroupId ";
        }

        SQLQuery query = super.getSession().createSQLQuery(sql);
        if (null != sysGroupId) {
            query.setParameter("sysGroupId", sysGroupId);
        }

        query.setParameter("permission", "%" + permission + "%");

        query.addScalar("email", new StringType());
        query.addScalar("phone", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

        return query.list();
    }

    /**
     * Get sysUser's Name & group_name_level2
     * @param sysUserId sysUserId
     * @return  SynStockTransDTO.sysUserName = full_name
     *          SynStockTransDTO.customField = group_name_level2
     */
    public SynStockTransDTO getRejectorInfo(Long sysUserId) {
        String sql = "SELECT " +
                "sg.group_name_level2 customField, " +
                "su.full_name sysUserName " +
                "FROM SYS_USER su, sys_group sg " +
                "WHERE 1=1 " +
                " and su.sys_user_id = :sysUserId " +
                "AND su.sys_group_id = sg.sys_group_id ";

        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("sysUserId", sysUserId);
        query.addScalar("sysUserName", new StringType());
        query.addScalar("customField", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        List<SynStockTransDTO> list = query.list();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Cập nhât dữ liệu trong bảng SYN_STOCK_DAILY_IMPORT_EXPORT
     * với is_confirm = 2 (đã nhận), ie_date = sysdate với điều kiện STOCK_TRANS_TYPE = 1
     * query by synStockTrans code
     */
    public int updateSynStockDailyImportExport(String code) {
        String sql = "UPDATE SYN_STOCK_DAILY_IMPORT_EXPORT " +
                "SET " +
                "IS_CONFIRM = 2 " +
                ", ie_date = :today " +
                "WHERE " +
                "STOCK_TRANS_TYPE = 1 " +
                "AND SYN_STOCK_TRANS_CODE = :code ";

        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("code", code);
        query.setParameter("today", new Date());
        return query.executeUpdate();
    }

    /**
     * update Confirm date = sysDate 1st time
     * @param id    synStockTransId
     * @return
     */
    public int updateConfirmDateFirstTime(Long id) {
        String sql = "UPDATE " +
                "SYN_STOCK_TRANS " +
                "SET " +
                "CONFIRM_DATE = :today " +
                "WHERE " +
                "SYN_STOCK_TRANS_ID = :stockTransId ";

        SQLQuery queryChapNhan = getSession().createSQLQuery(sql);
        queryChapNhan.setParameter("today", new Date());
        queryChapNhan.setParameter("stockTransId", id);

        return queryChapNhan.executeUpdate();
    }
    //VietNT_end
    //VietNT_20190219_start

    /**
     * Get user TTKT when CN reject
     * @param sysUserId         User id tinh truong CN
     * @param constructionCode  Ma cong trinh
     * @return User id tinh truong TTKT cung province
     */
//    hoanm1_20190306_start
    public SynStockTransDTO getUserTTKTProvince(Long sysUserId, String constructionCode) {
        String sql = "SELECT max(SYS_USER_ID) sysUserId,max(sys_group_id) sysGroupIdConstruction FROM config_user_province WHERE cat_province_code IN " +
                "(SELECT pro.code " +
                "FROM construction cst, cat_station cat, cat_province pro " +
                "where " +
                "cst.CAT_STATION_ID = cat.CAT_STATION_ID " +
                "and cat.cat_province_id = pro.cat_province_id " +
                "and cst.code = :constructionCode ) " +
                "and SYS_USER_ID != :sysUserId  ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        query.setParameter("constructionCode", constructionCode);
        query.setParameter("sysUserId", sysUserId);
        query.addScalar("sysUserId", new LongType());
        query.addScalar("sysGroupIdConstruction", new LongType());

        List<SynStockTransDTO> list = query.list();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
//    hoanm1_20190306_end
    /**
     * update lastShipperId = user tinh truong TTKT
     * @param lastShipperId
     * @param synStockTransId
     * @return
     */
    public int updateLastShipperSynStockTrans(Long lastShipperId, Long synStockTransId) {
        String sql = "UPDATE syn_stock_trans " +
                "SET " +
                "LAST_SHIPPER_ID = :lastShipperId " +
                ", CONFIRM = 0 " +
                "WHERE syn_stock_trans_id = :synStockTransId ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("lastShipperId", lastShipperId);
        query.setParameter("synStockTransId", synStockTransId);

        return query.executeUpdate();
    }
    //VietNT_end
    
//    hoanm1_20190306_start
    public Double getConstructionStatus(String constructionCode) {
        String sql = "SELECT nvl(round(avg(wi.status),2),0) statusConstruction FROM construction cst,work_item wi WHERE cst.construction_id=wi.construction_id " +
                " and cst.code = :constructionCode " ;

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("constructionCode", constructionCode);
        query.addScalar("statusConstruction", new DoubleType());

        List<Double> lstDoub = query.list();
        if (lstDoub != null && lstDoub.size() > 0) {
            return lstDoub.get(0);
        }
        return 0D;
    }
    
    public int updateSysGroupIdConstruction(Long sysGroupId, String constructionCode) {
        String sql = "UPDATE construction set sys_group_id = :sysGroupId where code = :constructionCode " ;
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("sysGroupId", sysGroupId);
        query.setParameter("constructionCode", constructionCode);
        return query.executeUpdate();
    }
//    hoanm1_20190306_end
    
    //HuyPQ-08042019-start
    @SuppressWarnings("unchecked")
	public List<SynStockTransDTO> reportDetailAWaitReceive(SynStockTransDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT ST.CODE code,  " + 
    			"  CASE  " + 
    			"    WHEN NVL(st.CONFIRM,0)=0  " + 
    			"    THEN 'Chờ phê duyệt'  " + 
    			"    WHEN NVL(st.CONFIRM,0)=1  " + 
    			"    THEN 'Đã phê duyệt'  " + 
    			"    WHEN NVL(st.CONFIRM,0)=2  " + 
    			"    THEN 'Đã từ chối'  " + 
    			"  END confirm,  " + 
    			"  TO_CHAR(ST.REAL_IE_TRANS_DATE, 'DD/MM/YYYY') realIeTransDateString,  " + 
    			"  ST.STOCK_CODE stockCode,  " + 
    			"  ST.CONSTRUCTION_CODE constructionCode,  " + 
    			"  NVL(  " + 
    			"  CASE  " + 
    			"    WHEN (SELECT MAX(CONSTRUCTION_id) CONSTRUCTION_id  " + 
    			"      FROM construction_task cst_task  " + 
    			"      WHERE cst.CONSTRUCTION_id=cst_task.CONSTRUCTION_id  " + 
    			"      AND cst_task.level_id    =4  " + 
    			"      AND cst_task.type        =1  " + 
    			"      AND cst_task.status      >1 ) IS NOT NULL  " + 
    			"    THEN sysG.name  " + 
    			"    ELSE  " + 
    			"      (SELECT sy_group.name  " + 
    			"      FROM  " + 
    			"        (SELECT MAX(conf.sys_group_id)sys_group_id  " + 
    			"        FROM CONFIG_USER_PROVINCE conf  " + 
    			"        WHERE conf.cat_province_id= catP.cat_province_id  " + 
    			"        AND conf.sys_group_id    !=sysG.sys_group_id  " + 
    			"        AND conf.sys_group_id    IS NOT NULL  " + 
    			"        ) conf,  " + 
    			"        sys_group sy_group  " + 
    			"      WHERE conf.sys_group_id=sy_group.sys_group_id  " + 
    			"      AND sy_group.code NOT LIKE '%XC%'  " + 
    			"      )  " + 
    			"  END,sysG.name) sysGroupName,  " + 
    			"  su.FULL_NAME fullName,  " + 
    			"  su.EMAIL email,  " + 
    			"  su.PHONE_NUMBER phoneNumber,  " + 
    			"  catP.code catProvinceCode,  " + 
    			"  catHouse.code catStationHouseCode,  " + 
    			"  cat.code catStationCode,  " + 
    			"  st.contract_code cntContractCode,  " + 
    			"  STDR.GOODS_NAME goodsName,  " + 
    			"  STD.GOODS_CODE goodsCode,  " + 
    			"  STDR.GOODS_UNIT_NAME goodsUnitName,  " + 
    			"  CASE  " + 
    			"    WHEN stdr.serial LIKE '-%'  " + 
    			"    THEN ''  " + 
    			"    ELSE TO_CHAR(stdr.serial)  " + 
    			"  END serial,  " + 
    			"  STD.AMOUNT_REAL amountReal,  " + 
    			"  stdr.amount amount,  " + 
    			"  STDR.UNIT_PRICE unitPrice,  " + 
    			"  stdr.amount* STDR.UNIT_PRICE thanhTien  " + 
    			"FROM SYN_STOCK_TRANS st  " + 
    			"JOIN syn_stock_trans_Detail std  " + 
    			"ON std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID  " + 
    			"JOIN SYN_STOCK_TRANS_DETAIL_SERIAL stdr  " + 
    			"ON stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID  " + 
    			"LEFT JOIN construction cst  " + 
    			"ON st.CONSTRUCTION_CODE=cst.code  " + 
    			"LEFT JOIN cat_station cat  " + 
    			"ON cst.cat_station_id=cat.cat_station_id  " + 
    			"LEFT JOIN cat_station_house catHouse  " + 
    			"ON cat.cat_station_house_id=catHouse.cat_station_house_id  " + 
    			"LEFT JOIN cat_province catP  " + 
    			"ON cat.cat_province_id=catP.cat_province_id  " + 
    			"LEFT JOIN sys_group sysG  " + 
    			"ON cst.sys_group_id=sysG.sys_group_id  " + 
    			"LEFT JOIN SYS_USER su  " + 
    			"ON st.LAST_SHIPPER_ID= su.SYS_USER_ID  " + 
    			"WHERE st.type        =2  " + 
    			"AND st.REQ_TYPE     IN(2,3)  " + 
    			"AND st.DEST_TYPE     =3  " + 
    			"AND st.stock_code NOT LIKE '%CTCT_%'  ");
	    	if(obj.getDateFrom()!=null) {
	    		sql.append("AND ST.REAL_IE_TRANS_DATE >=:dateFrom  "); 
	    	}
	    	if(obj.getDateFrom()!=null) {
	    		sql.append("AND ST.REAL_IE_TRANS_DATE <=:dateTo  ");
	    	}
	    	if(obj.getEmail()!=null) {
	    		sql.append("AND su.EMAIL               =:email  " );
	    	}
	    	if(obj.getLstConfirm()!=null && obj.getLstConfirm().size()!=0) {
	    		sql.append("AND NVL(st.CONFIRM,0)     IN (:lstConfirm)  ");
	    	}
	    	if(obj.getSysGroupId()!=null) {
	    		sql.append("AND NVL(  " + 
	        			"  CASE  " + 
	        			"    WHEN (SELECT MAX(CONSTRUCTION_id) CONSTRUCTION_id  " + 
	        			"      FROM construction_task cst_task  " + 
	        			"      WHERE cst.CONSTRUCTION_id=cst_task.CONSTRUCTION_id  " + 
	        			"      AND cst_task.level_id    =4  " + 
	        			"      AND cst_task.type        =1  " + 
	        			"      AND cst_task.status      >1 ) IS NOT NULL  " + 
	        			"    THEN sysG.sys_group_id  " + 
	        			"    ELSE  " + 
	        			"      (SELECT sy_group.sys_group_id  " + 
	        			"      FROM  " + 
	        			"        (SELECT MAX(conf.sys_group_id)sys_group_id  " + 
	        			"        FROM CONFIG_USER_PROVINCE conf  " + 
	        			"        WHERE conf.cat_province_id= catP.cat_province_id  " + 
	        			"        AND conf.sys_group_id    !=sysG.sys_group_id  " + 
	        			"        AND conf.sys_group_id    IS NOT NULL  " + 
	        			"        )conf,  " + 
	        			"        sys_group sy_group  " + 
	        			"      WHERE conf.sys_group_id=sy_group.sys_group_id  " + 
	        			"      AND sy_group.code NOT LIKE '%XC%'  " + 
	        			"      )  " + 
	        			"  END,sysG.sys_group_id ) =:sysGroupId  "); 
	    	}
	    	if(StringUtils.isNotEmpty(obj.getKeySearch())) {
	    		sql.append("AND (upper(st.CONSTRUCTION_CODE) LIKE upper(:keySearch) " + 
	        			"OR upper(cat.CODE) LIKE upper(:keySearch)  " + 
	        			"OR upper(st.contract_code) LIKE upper(:keySearch))");
	    	}
	    	
	    sql.append(" ORDER BY ST.CODE ");
	    StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
	    sqlCount.append(sql.toString());
	    sqlCount.append(")");

	    SQLQuery query = getSession().createSQLQuery(sql.toString());
	    SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
	    	
    	query.addScalar("code", new StringType());
    	query.addScalar("confirm", new StringType());
    	query.addScalar("realIeTransDateString", new StringType());
    	query.addScalar("stockCode", new StringType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("sysGroupName", new StringType());
    	query.addScalar("fullName", new StringType());
    	query.addScalar("email", new StringType());
    	query.addScalar("phoneNumber", new StringType());
    	query.addScalar("catProvinceCode", new StringType());
    	query.addScalar("catStationHouseCode", new StringType());
    	query.addScalar("catStationCode", new StringType());
    	query.addScalar("cntContractCode", new StringType());
    	query.addScalar("goodsName", new StringType());
    	query.addScalar("goodsCode", new StringType());
    	query.addScalar("goodsUnitName", new StringType());
    	query.addScalar("serial", new StringType());
    	query.addScalar("amountReal", new DoubleType());
    	query.addScalar("amount", new DoubleType());
    	query.addScalar("unitPrice", new DoubleType());
    	query.addScalar("thanhTien", new DoubleType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
    	
    	if(obj.getDateFrom()!=null) {
    		query.setParameter("dateFrom", obj.getDateFrom());
    		queryCount.setParameter("dateFrom", obj.getDateFrom());
    	}
    	if(obj.getDateFrom()!=null) {
    		query.setParameter("dateTo", obj.getDateTo());
    		queryCount.setParameter("dateTo", obj.getDateTo());
    	}
    	if(obj.getEmail()!=null) {
    		query.setParameter("email", obj.getEmail());
    		queryCount.setParameter("email", obj.getEmail());
    	}
    	if(obj.getLstConfirm()!=null && obj.getLstConfirm().size()!=0) {
    		query.setParameterList("lstConfirm", obj.getLstConfirm());
    		queryCount.setParameterList("lstConfirm", obj.getLstConfirm());
    	}
    	if(obj.getSysGroupId()!=null) {
    		query.setParameter("sysGroupId", obj.getSysGroupId());
    		queryCount.setParameter("sysGroupId", obj.getSysGroupId()); 
    	}
    	if(StringUtils.isNotEmpty(obj.getKeySearch())) {
    		query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    		queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%"); 
    	}
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        
        return query.list();
    }
    //Huypq-end
    
    //Huypq-20190904-start
    public List<SynStockTransDTO> doSearchAcceptManage(SynStockTransDTO obj){
		StringBuilder sql = new StringBuilder("select ");
		sql.append("syn.ORDER_CODE orderCode, ");
		sql.append("to_char(syn.CODE) code, ");
		sql.append("to_date(to_char(syn.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') realIeTransDate, ");
		sql.append("NVL(syn.CONFIRM,0) confirm, ");
		sql.append("'A' stockType, ");
		sql.append("NVL(syn.STATE,0) state, ");
		sql.append("cons.CONSTRUCTION_ID constructionId, ");
		sql.append("cons.CODE constructionCode, ");
		sql.append("syn.STOCK_NAME synStockName, ");
		sql.append("syn.CREATED_BY_NAME synCreatedByName, ");
		sql.append("syn.CREATED_DATE synCreatedDate, ");
		sql.append("syn.LAST_SHIPPER_ID lastShipperId, ");
		sql.append("su.FULL_NAME lastShipperName, ");
		sql.append("syn.RECEIVER_ID receiverId, ");
		sql.append("syn.SYN_STOCK_TRANS_ID synStockTransId ");
		sql.append(",syn.CONTRACT_CODE cntContractCode ");
		sql.append(",syn.SYS_GROUP_ID sysGroupId ");
		sql.append(",syn.CONFIRM_DESCRIPTION confirmDescription ");
		sql.append(",sysGroup.NAME sysGroupName ");
		sql.append(",cs.CODE catStationCode ");
		sql.append(",syn.SHIPPER_NAME shipperName ");
		sql.append(",syn.DESCRIPTION description ");
		sql.append(",syn.CREATED_BY_NAME createdByName ");
		sql.append(",syn.CREATED_DATE createdDate ");
		sql.append(",syn.CREATED_DEPT_NAME createdDeptName ");
		sql.append(",syn.CANCEL_BY_NAME cancelByName ");
		sql.append(",syn.CANCEL_REASON_NAME cancelReasonName ");
		sql.append(",syn.CANCEL_DATE cancelDate ");
		sql.append(",syn.CANCEL_DESCRIPTION cancelDescription ");
		sql.append(",syn.STOCK_CODE stockCode ");
		sql.append(",syn.SIGN_STATE signState ");
		sql.append(",syn.STATUS status ");
		sql.append(",1 as synType ");
		//Huypq-20200203-start
		sql.append(" ,  syn.ORDER_ID orderId," + 
				"  syn.TYPE type, " + 
				"  syn.STOCK_ID stockId, " + 
				"  syn.FROM_STOCK_TRANS_ID fromStockTransId, " + 
				"  syn.CREATED_DEPT_ID createdDeptId, " + 
				"  syn.UPDATED_BY updatedBy, " + 
				"  syn.UPDATED_DATE updatedDate, " + 
				"  syn.REAL_IE_USER_ID realIeUserId, " + 
				"  syn.REAL_IE_USER_NAME realIeUserName, " + 
				"  syn.SHIPPER_ID shipperId, " + 
				"  syn.CANCEL_BY cancelBy, " + 
				"  syn.VOFFICE_TRANSACTION_CODE vofficeTransactionCode, " + 
				"  syn.SHIPMENT_CODE shipmentCode, " + 
				"  syn.CONTRACT_CODE contractCode, " + 
				"  syn.PROJECT_CODE projectCode, " + 
				"  syn.CUST_ID custId, " + 
				"  syn.CREATED_BY createdBy, " + 
				"  syn.BUSSINESS_TYPE_NAME bussinessTypeName, " + 
				"  syn.IN_ROAL inRoal, " + 
				"  syn.DEPT_RECEIVE_NAME deptReceiveName, " + 
				"  syn.DEPT_RECEIVE_ID deptReceiveId, " + 
				"  syn.STOCK_RECEIVE_ID stockReceiveId, " + 
				"  syn.STOCK_RECEIVE_CODE stockReceiveCode, " + 
				"  syn.PARTNER_ID partnerId, " + 
				"  syn.SYN_TRANS_TYPE synTransType, " + 
				"  syn.STOCK_NAME stockName, " + 
				"  syn.BUSSINESS_TYPE bussinessType, " + 
				"  cs.code customField , " + 
				"  NULL AS isSyn ");
		//Huy-end
		sql.append(" FROM ");
		sql.append("SYN_STOCK_TRANS syn ");
		sql.append("LEFT JOIN CONSTRUCTION cons ");
		sql.append("ON syn.CONSTRUCTION_CODE    = cons.CODE ");
		sql.append("LEFT JOIN SYS_GROUP sysGroup ");
		sql.append("ON sysGroup.SYS_GROUP_ID    = syn.SYS_GROUP_ID ");
		sql.append("LEFT JOIN CAT_STATION cs on cs.cat_station_id = cons.cat_station_id ");
		//Huypq-20200418-start
		sql.append("LEFT JOIN SYS_USER su ");
		sql.append("ON su.SYS_USER_ID    = syn.LAST_SHIPPER_ID ");
		//Huy-end
		sql.append("WHERE ");
		sql.append("syn.type                    = 2 ");
		sql.append("and syn.REQ_TYPE in(2,3) and syn.DEST_TYPE=3 and syn.stock_code not like '%CTCT_%' ");
//		sql.append("AND (syn.LAST_SHIPPER_ID    = :lastShipperId ");
//		sql.append("OR syn.RECEIVER_ID          = :receiverId ) ");
//		if(obj.getProvinceIdLst()!=null && obj.getProvinceIdLst().size()>0) {
//			sql.append(" and cs.CAT_PROVINCE_ID in (:provinceIdLst) ");
//		} else {
//			sql.append("AND (syn.LAST_SHIPPER_ID    = :lastShipperId ");
//			sql.append("OR syn.RECEIVER_ID          = :receiverId) ");
//		}
//		if(obj.getLastShipperId() !=null || obj.getReceiverId() !=null){
//			sql.append("AND (syn.LAST_SHIPPER_ID    = :lastShipperId ");
//			sql.append("OR syn.RECEIVER_ID          = :receiverId) ");
//		}
		if(obj.getProvinceIdLst()!=null && obj.getProvinceIdLst().size()>0) {
			sql.append(" and cs.CAT_PROVINCE_ID in (:provinceIdLst) ");
		}
		
		if(StringUtils.isNotBlank(obj.getOrderCode())) {
			sql.append(" AND (upper(syn.ORDER_CODE) like upper(:orderCode) escape '&') ");
		}
		
		if(StringUtils.isNotBlank(obj.getCode())) {
			sql.append(" AND (upper(to_char(syn.CODE)) like upper(:synCode) escape '&') ");
		}
		
		if(obj.getDateFrom()!=null) {
			sql.append(" AND TRUNC(syn.REAL_IE_TRANS_DATE) >= :dateFrom ");
		}
		
		if(obj.getDateTo()!=null) {
			sql.append(" AND TRUNC(syn.REAL_IE_TRANS_DATE) <= :dateTo ");
		}
		
		if(StringUtils.isNotBlank(obj.getCntContractCode())) {
			sql.append(" AND (upper(syn.CONTRACT_CODE) like upper(:contractCode) escape '&') ");
		}
		
		if(StringUtils.isNotBlank(obj.getConstructionCode())) {
			sql.append(" AND (upper(cons.CODE) like upper(:constructionCode) escape '&') ");
		}
		
		if(obj.getSysGroupId()!=null) {
			sql.append(" AND syn.SYS_GROUP_ID = :sysGroupId ");
		}
		
		if (StringUtils.isNotEmpty(obj.getConfirm())) {
            sql.append(" AND trim(syn.CONFIRM) = :confirm ");
        } else {
            sql.append(" AND trim(syn.CONFIRM) IN (0, 1, 2) ");
        }
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("orderCode", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("realIeTransDate", new DateType());
        query.addScalar("confirm", new StringType());
        query.addScalar("stockType", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("synStockName", new StringType());
        query.addScalar("synCreatedByName", new StringType());
        query.addScalar("synCreatedDate", new DateType());
        query.addScalar("lastShipperId", new LongType());
        query.addScalar("receiverId", new LongType());
        query.addScalar("synStockTransId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("confirmDescription", new StringType());
        query.addScalar("catStationCode", new StringType());
        
        query.addScalar("shipperName", new StringType());
        query.addScalar("description", new StringType());
        
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdByName", new StringType());
        query.addScalar("createdDeptName", new StringType());
        
        query.addScalar("cancelByName", new StringType());
        query.addScalar("cancelDate", new DateType());
        query.addScalar("cancelReasonName", new StringType());
        query.addScalar("cancelDescription", new StringType());
        query.addScalar("stockCode", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("status", new StringType());
        
        //Huypq-20200203-start
        query.addScalar("orderId", new LongType());
        query.addScalar("type", new StringType());
        query.addScalar("stockId", new LongType());
        query.addScalar("fromStockTransId", new LongType());
        query.addScalar("createdDeptId", new LongType());
        query.addScalar("updatedBy", new LongType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("realIeUserId", new StringType());
        query.addScalar("realIeUserName", new StringType());
        query.addScalar("shipperId", new LongType());
        query.addScalar("cancelBy", new LongType());
        query.addScalar("vofficeTransactionCode", new StringType());
        query.addScalar("shipmentCode", new StringType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("projectCode", new StringType());
        query.addScalar("custId", new LongType());
        query.addScalar("createdBy", new LongType());
        query.addScalar("bussinessTypeName", new StringType());
        query.addScalar("inRoal", new StringType());
        query.addScalar("deptReceiveName", new StringType());
        query.addScalar("deptReceiveId", new LongType());
        query.addScalar("stockReceiveId", new LongType());
        query.addScalar("stockReceiveCode", new StringType());
        query.addScalar("partnerId", new LongType());
        query.addScalar("synTransType", new StringType());
        query.addScalar("stockName", new StringType());
        query.addScalar("bussinessType", new StringType());
        query.addScalar("lastShipperName", new StringType());
        //Huypq-end
        
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        
        if(obj.getProvinceIdLst()!=null && obj.getProvinceIdLst().size()>0) {
        	query.setParameterList("provinceIdLst", obj.getProvinceIdLst());
	        queryCount.setParameterList("provinceIdLst", obj.getProvinceIdLst());
		}
        
        if(StringUtils.isNotBlank(obj.getOrderCode())) {
			query.setParameter("orderCode", "%" + obj.getOrderCode() + "%");
			queryCount.setParameter("orderCode", "%" + obj.getOrderCode() + "%");
		}
		
		if(StringUtils.isNotBlank(obj.getCode())) {
			query.setParameter("synCode", "%" + obj.getCode() + "%");
			queryCount.setParameter("synCode", "%" + obj.getCode() + "%");
		}
		
		if(obj.getDateFrom()!=null) {
			query.setParameter("dateFrom",obj.getDateFrom());
			queryCount.setParameter("dateFrom",obj.getDateFrom());
		}
		
		if(obj.getDateTo()!=null) {
			query.setParameter("dateTo",obj.getDateTo());
			queryCount.setParameter("dateTo",obj.getDateTo());
		}
		
		if(StringUtils.isNotBlank(obj.getCntContractCode())) {
			query.setParameter("contractCode", "%" + obj.getCntContractCode() + "%");
			queryCount.setParameter("contractCode", "%" + obj.getCntContractCode() + "%");
		}
		
		if(StringUtils.isNotBlank(obj.getConstructionCode())) {
			query.setParameter("constructionCode", "%" + obj.getConstructionCode() + "%");
			queryCount.setParameter("constructionCode", "%" + obj.getConstructionCode() + "%");
		}
		
		if(obj.getSysGroupId()!=null) {
			query.setParameter("sysGroupId",obj.getSysGroupId());
			queryCount.setParameter("sysGroupId",obj.getSysGroupId());
		}
		
		if (StringUtils.isNotEmpty(obj.getConfirm())) {
            query.setParameter("confirm",obj.getConfirm());
			queryCount.setParameter("confirm",obj.getConfirm());
        }
        
        this.setPageSize(obj, query, queryCount);
        
        return query.list();
    }
    
    //Get data by id
    public SynStockTransDTO getAcceptManageById(SynStockTransDTO obj, boolean checkRoleTTHT){
		StringBuilder sql = new StringBuilder("select ");
		sql.append("syn.ORDER_CODE orderCode, ");
		sql.append("to_char(syn.CODE) code, ");
		sql.append("to_date(to_char(syn.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') realIeTransDate, ");
		sql.append("NVL(syn.CONFIRM,0) confirm, ");
		sql.append("'A' stockType, ");
		sql.append("NVL(syn.STATE,0) state, ");
		sql.append("cons.CONSTRUCTION_ID constructionId, ");
		sql.append("cons.CODE constructionCode, ");
		sql.append("syn.STOCK_NAME synStockName, ");
		sql.append("syn.CREATED_BY_NAME synCreatedByName, ");
		sql.append("syn.CREATED_DATE synCreatedDate, ");
		sql.append("syn.LAST_SHIPPER_ID lastShipperId, ");
		sql.append("syn.RECEIVER_ID receiverId, ");
		sql.append("syn.SYN_STOCK_TRANS_ID synStockTransId ");
		sql.append(",syn.CONTRACT_CODE cntContractCode ");
		sql.append(",syn.SYS_GROUP_ID sysGroupId ");
		sql.append(",syn.CONFIRM_DESCRIPTION confirmDescription ");
		sql.append(",sysGroup.NAME sysGroupName ");
		sql.append(",cs.CODE catStationCode ");
		sql.append(",syn.SHIPPER_NAME shipperName ");
		sql.append(",syn.DESCRIPTION description ");
		sql.append(",syn.CREATED_BY_NAME createdByName ");
		sql.append(",syn.CREATED_DATE createdDate ");
		sql.append(",syn.CREATED_DEPT_NAME createdDeptName ");
		sql.append(",syn.CANCEL_BY_NAME cancelByName ");
		sql.append(",syn.CANCEL_REASON_NAME cancelReasonName ");
		sql.append(",syn.CANCEL_DATE cancelDate ");
		sql.append(",syn.CANCEL_DESCRIPTION cancelDescription ");
		sql.append(",syn.STOCK_CODE stockCode ");
		sql.append(",syn.SIGN_STATE signState ");
		sql.append(",syn.STATUS status ");
		sql.append(",1 as synType ");
		sql.append(",cc.SIGN_DATE signDate ");
		sql.append(" FROM ");
		sql.append("SYN_STOCK_TRANS syn ");
		sql.append("LEFT JOIN CONSTRUCTION cons ");
		sql.append("ON syn.CONSTRUCTION_CODE    = cons.CODE ");
		sql.append("LEFT JOIN SYS_GROUP sysGroup ");
		sql.append("ON sysGroup.SYS_GROUP_ID    = syn.SYS_GROUP_ID ");
		sql.append("LEFT JOIN CAT_STATION cs on cs.cat_station_id = cons.cat_station_id ");
		sql.append("left join CTCT_IMS_OWNER.CNT_CONTRACT cc ");
		sql.append("on cc.CODE = syn.CONTRACT_CODE ");
		sql.append("WHERE ");
		sql.append("syn.type                    = 2 ");
		sql.append("and syn.REQ_TYPE in(2,3) and syn.DEST_TYPE=3 and syn.stock_code not like '%CTCT_%' ");
		sql.append(" AND syn.SYN_STOCK_TRANS_ID=:id ");
		if(!checkRoleTTHT) {
			sql.append(" AND (syn.LAST_SHIPPER_ID    = :lastShipperId ");
			sql.append(" OR syn.RECEIVER_ID          = :receiverId ) ");
		}
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("orderCode", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("realIeTransDate", new DateType());
        query.addScalar("confirm", new StringType());
        query.addScalar("stockType", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("synStockName", new StringType());
        query.addScalar("synCreatedByName", new StringType());
        query.addScalar("synCreatedDate", new DateType());
        query.addScalar("lastShipperId", new LongType());
        query.addScalar("receiverId", new LongType());
        query.addScalar("synStockTransId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("confirmDescription", new StringType());
        query.addScalar("catStationCode", new StringType());
        
        query.addScalar("shipperName", new StringType());
        query.addScalar("description", new StringType());
        
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdByName", new StringType());
        query.addScalar("createdDeptName", new StringType());
        
        query.addScalar("cancelByName", new StringType());
        query.addScalar("cancelDate", new DateType());
        query.addScalar("cancelReasonName", new StringType());
        query.addScalar("cancelDescription", new StringType());
        query.addScalar("stockCode", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("status", new StringType());
        
        query.addScalar("signDate", new DateType());
        
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDTO.class));
        
        if(!checkRoleTTHT) {
        	query.setParameter("lastShipperId", obj.getLastShipperId());
            queryCount.setParameter("lastShipperId", obj.getLastShipperId());
            
            query.setParameter("receiverId", obj.getReceiverId());
            queryCount.setParameter("receiverId", obj.getReceiverId());
        }
        
        query.setParameter("id", obj.getSynStockTransId());
        queryCount.setParameter("id", obj.getSynStockTransId());
        
        this.setPageSize(obj, query, queryCount);
        
        @SuppressWarnings("unchecked")
        List<SynStockTransDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;

    }
    
	public Long updateSynStockTransAccept(SynStockTransDTO obj) throws ParseException {
//		StringBuilder sqlChapNhan = new StringBuilder("");
//		sqlChapNhan.append("UPDATE ");
//		sqlChapNhan.append("SYN_STOCK_TRANS sst ");
//		sqlChapNhan.append("SET ");
//		sqlChapNhan.append("sst.STATE           = 1, ");
//		sqlChapNhan.append("sst.LAST_SHIPPER_ID = '" + obj.getSysUserId() + "', ");
//		sqlChapNhan.append("sst.UPDATED_BY      = '" + obj.getSysUserId() + "', ");
//		sqlChapNhan.append("sst.RECEIVER_ID     = '" + obj.getReceiverId() + "', ");
//		sqlChapNhan.append("sst.UPDATED_DATE    = '" + getCurrentTime() + "' ");
//		sqlChapNhan.append("WHERE ");
//		sqlChapNhan.append("sst.SYN_STOCK_TRANS_ID  = '" + obj.getSynStockTransId() + "' ");
//
//		SQLQuery queryChapNhan = getSession().createSQLQuery(sqlChapNhan.toString());
//		return (long) queryChapNhan.executeUpdate();
		
		StringBuilder sqlChapNhan = new StringBuilder("");
        sqlChapNhan.append("UPDATE ");
        sqlChapNhan.append("SYN_STOCK_TRANS ");
        sqlChapNhan.append("SET ");
        sqlChapNhan.append("CONFIRM = 1, ");
        sqlChapNhan.append("UPDATED_BY         = :sysUserId, ");
        sqlChapNhan.append("LAST_SHIPPER_ID         = :sysUserId, ");
        sqlChapNhan.append("UPDATED_DATE       = :newDate, ");
        sqlChapNhan.append("CONFIRM_DESCRIPTION = null ");
        sqlChapNhan.append("WHERE ");
        sqlChapNhan.append("SYN_STOCK_TRANS_ID = :stockTransId ");

        SQLQuery queryChapNhan = getSession().createSQLQuery(sqlChapNhan.toString());
        queryChapNhan.setParameter("sysUserId", obj.getSysUserId());
        queryChapNhan.setParameter("newDate", new Date());
        queryChapNhan.setParameter("stockTransId", obj.getSynStockTransId());

        return (long)queryChapNhan.executeUpdate();
		
	}
	
	//Từ chối lúc tiếp nhận
	public Long updateSynStockTransDeny(SynStockTransDTO obj) {
        StringBuilder sqlTuChoi = new StringBuilder("");
        sqlTuChoi.append("UPDATE ");
        sqlTuChoi.append("SYN_STOCK_TRANS ");
        sqlTuChoi.append("SET ");
        sqlTuChoi.append("CONFIRM = 2, ");
        sqlTuChoi.append("UPDATED_BY          = :sysUserId, ");
        sqlTuChoi.append("UPDATED_DATE        = :newDate, ");
        sqlTuChoi.append("CONFIRM_DESCRIPTION = :confirmDescription ");
        sqlTuChoi.append("WHERE ");
        sqlTuChoi.append("SYN_STOCK_TRANS_ID  = :stockTransId ");
        SQLQuery queryTuChoi = getSession().createSQLQuery(sqlTuChoi.toString());

        queryTuChoi.setParameter("sysUserId", obj.getSysUserId());
        queryTuChoi.setParameter("newDate", new Date());
        queryTuChoi.setParameter("confirmDescription", obj.getConfirmDescription());
        queryTuChoi.setParameter("stockTransId", obj.getSynStockTransId());

        return (long)queryTuChoi.executeUpdate();

    }
	
	//Từ chối lúc bàn giao
	public Long updateSynStockTransDenyAsign(SynStockTransDTO obj) {
        StringBuilder sqlTuChoi = new StringBuilder("");
        sqlTuChoi.append("UPDATE ");
        sqlTuChoi.append("SYN_STOCK_TRANS ");
        sqlTuChoi.append("SET ");
//        sqlTuChoi.append("CONFIRM = 1, ");
        sqlTuChoi.append("UPDATED_BY          = :sysUserId, ");
        sqlTuChoi.append("UPDATED_DATE        = :newDate, ");
        sqlTuChoi.append("CONFIRM_DESCRIPTION = :confirmDescription, ");
        sqlTuChoi.append("state = '2' ");
        sqlTuChoi.append("WHERE ");
        sqlTuChoi.append("SYN_STOCK_TRANS_ID  = :stockTransId ");
        SQLQuery queryTuChoi = getSession().createSQLQuery(sqlTuChoi.toString());

        queryTuChoi.setParameter("sysUserId", obj.getSysUserId());
        queryTuChoi.setParameter("newDate", new Date());
        queryTuChoi.setParameter("confirmDescription", obj.getConfirmDescription());
        queryTuChoi.setParameter("stockTransId", obj.getSynStockTransId());

        return (long)queryTuChoi.executeUpdate();

    }
	
	public Long UpdateStockTransState(SynStockTransDTO obj) throws ParseException {
		StringBuilder sql = new StringBuilder("");
		sql.append("UPDATE ");
		sql.append("SYN_STOCK_TRANS sst ");
		sql.append("SET ");
		sql.append("sst.STATE                   = 0, ");
		sql.append("sst.UPDATED_BY              = '" + obj.getSysUserId() + "', ");
		sql.append("sst.RECEIVER_ID             = '" + obj.getSysUserIdRecieve() + "', ");
		sql.append("sst.CONFIRM_DESCRIPTION     = " + obj.getConfirmDescription() + ", ");
		sql.append("sst.UPDATED_DATE            = '" + getCurrentTime() + "' ");
		sql.append("WHERE ");
		sql.append("sst.SYN_STOCK_TRANS_ID  = '" + obj.getSynStockTransId() + "' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		return (long)query.executeUpdate();
    }
	
	public Long SaveStTransaction(SynStockTransDTO obj) throws ParseException {
        StringBuilder sql = new StringBuilder("");
        sql.append("INSERT INTO ST_TRANSACTION ");
        sql.append(
                "(ST_TRANSACTION_ID, DESCRIPTION, OLD_LAST_SHIPPER_ID, NEW_LAST_SHIPPER_ID ,STOCK_TRANS_ID, TYPE, CONFIRM, CREATED_DATE, CREATED_USER_ID) ");
        sql.append("VALUES ( ");
        sql.append("ST_TRANSACTION_seq.nextval, ");
        sql.append("null, ");
        sql.append("'" + obj.getSysUserId() + "', ");
        sql.append("'" + obj.getSysUserIdRecieve() + "', ");
        sql.append("'" + obj.getSynStockTransId() + "', ");
        sql.append("'" + '1' + "', ");
        sql.append("'0', ");
        sql.append("'" + getCurrentTime() + "', ");
        sql.append("'" + obj.getSysUserId() + "' ");
        sql.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        return (long)query.executeUpdate();
    }
	
	public SynStockTransDetailDTO getNewestTransactionId(SynStockTransDTO obj) {
        StringBuilder sql = new StringBuilder("");
            sql.append("SELECT ");
            sql.append("max(ST_TRANSACTION_ID) maxTransactionId ");
            sql.append("FROM ST_TRANSACTION  ");
            sql.append("WHERE ");
            sql.append("STOCK_TRANS_ID = " + obj.getSynStockTransId() + " ");
            sql.append("AND TYPE           = '1' ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("maxTransactionId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(SynStockTransDetailDTO.class));

        @SuppressWarnings("unchecked")
        List<SynStockTransDetailDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;

    }
	
	public int UpdateStocktrainConfirmByLastShipper(SynStockTransDTO request, boolean isInvestor,
			SynStockTransDetailDTO newestTransactionId) throws ParseException {
		StringBuilder sqlConfirmred = new StringBuilder("");
		sqlConfirmred.append("UPDATE ");
		sqlConfirmred.append("ST_TRANSACTION ");
		sqlConfirmred.append("SET ");
		sqlConfirmred.append("CONFIRM_DATE         = '" + getCurrentTime() + "', ");
		sqlConfirmred.append("CONFIRM              = '1' ");
		sqlConfirmred.append("WHERE ");
		if (newestTransactionId != null && newestTransactionId.getMaxTransactionId()!=null) {
			sqlConfirmred.append("ST_TRANSACTION_ID  = :stTransactionId ");
		} else {
			sqlConfirmred.append("STOCK_TRANS_ID  = :stockTransId ");
		}

		SQLQuery queryConfirmred = getSession().createSQLQuery(sqlConfirmred.toString());

		if (newestTransactionId != null && newestTransactionId.getMaxTransactionId()!=null) {
			queryConfirmred.setParameter("stTransactionId", newestTransactionId.getMaxTransactionId());
		} else {
			queryConfirmred.setParameter("stockTransId", request.getSynStockTransId());
		}

		return queryConfirmred.executeUpdate();
	}
	
	public Long updateSynStockTransAcceptAssign(SynStockTransDTO obj) throws ParseException {
		StringBuilder sqlChapNhan = new StringBuilder("");
		sqlChapNhan.append("UPDATE ");
		sqlChapNhan.append("SYN_STOCK_TRANS sst ");
		sqlChapNhan.append("SET ");
		sqlChapNhan.append("sst.STATE           = 1, ");
		sqlChapNhan.append("sst.LAST_SHIPPER_ID = '" + obj.getSysUserId() + "', ");
		sqlChapNhan.append("sst.UPDATED_BY      = '" + obj.getSysUserId() + "', ");
		sqlChapNhan.append("sst.RECEIVER_ID     = '" + obj.getReceiverId() + "', ");
		sqlChapNhan.append("sst.UPDATED_DATE    = '" + getCurrentTime() + "' ");
		sqlChapNhan.append(",sst.CONFIRM    = 1 "); //Huypq-20190930-add
		sqlChapNhan.append("WHERE ");
		sqlChapNhan.append("sst.SYN_STOCK_TRANS_ID  = '" + obj.getSynStockTransId() + "' ");

		SQLQuery queryChapNhan = getSession().createSQLQuery(sqlChapNhan.toString());
		return (long) queryChapNhan.executeUpdate();
	}
    //Huy-end
	
	//Huypq-20200206-start
	public Long updateAcceptSynStockTrans(SynStockTransDetailDTO obj) {
		StringBuilder sql = new StringBuilder(" update ");
		sql.append(" SYN_STOCK_TRANS_DETAIL set ");
		if(obj.getRealRecieveAmount()!=null) {
			sql.append(" REAL_RECIEVE_AMOUNT=:amount ");
		} else {
			sql.append(" REAL_RECIEVE_AMOUNT= 0 ");
		}
		
		if(obj.getRealRecieveDate()!=null) {
			sql.append(" ,REAL_RECIEVE_DATE=:dateReal ");
		}
		sql.append(" where SYN_STOCK_TRANS_DETAIL_ID=:synId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getRealRecieveAmount()!=null) {
			query.setParameter("amount", obj.getRealRecieveAmount());
		}
		
		if(obj.getRealRecieveDate()!=null) {
			query.setParameter("dateReal", obj.getRealRecieveDate());
		}
		query.setParameter("synId", obj.getSynStockTransDetailId());
		return (long) query.executeUpdate();
	}
	
	public List<UtilAttachDocumentDTO> getDataFilePXK(SynStockTransDetailDTO obj){
		StringBuilder sql = new StringBuilder("select UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId, " + 
				"OBJECT_ID objectId, " + 
				"CODE code, " + 
				"NAME name, " + 
				"FILE_PATH filePath, " + 
				"CREATED_DATE createdDate, " + 
				"CREATED_USER_ID createdUserId, " + 
				"CREATED_USER_NAME createdUserName " + 
				"from UTIL_ATTACH_DOCUMENT "
				+ " where status!=0 and type='XNBG_PXK_A' and object_id=:synStockTranId ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("utilAttachDocumentId", new LongType());
		query.addScalar("objectId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("filePath", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("createdUserName", new StringType());
		
		query.setParameter("synStockTranId", obj.getSynStockTransId());
		queryCount.setParameter("synStockTranId", obj.getSynStockTransId());
		
		query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
		
		this.setPageSize(obj, query, queryCount);
		
		return query.list();
	}
	//Huy-end
	
	//tatph-start-11/2/2020
//	huypq30_20200526_start
//	public int updateSynStockTransDetail(SynStockTransDetailDTO request) {
	public int updateSynStockTransDetail(AIOSynStockTransDetailDTO request) {
//	huypq30_20200526_end	
     
        StringBuilder sqlTuChoi = new StringBuilder("");
        sqlTuChoi.append("UPDATE ");
        sqlTuChoi.append("SYN_STOCK_TRANS_DETAIL st ");
        sqlTuChoi.append("SET ");
        sqlTuChoi.append("st.AMOUNT_REAL             = :amountReal ");
        sqlTuChoi.append("WHERE ");
        sqlTuChoi.append("st.SYN_STOCK_TRANS_ID      = :synStockTransId ");
        SQLQuery queryTuChoi = getSession().createSQLQuery(sqlTuChoi.toString());

        queryTuChoi.setParameter("synStockTransId", request.getSynStockTransId());
        queryTuChoi.setParameter("amountReal", request.getAmountReal() != null ? request.getAmountReal() : 0L);

        return queryTuChoi.executeUpdate();
    }
	//tatph-end-11/2/2020
	
	//Huypq-20200215-start
	public List<SysUserDTO> getUserIdByRoleProvince(List<String> lstProvince){
		StringBuilder sql = new StringBuilder("select SYS_USER_ID sysUserId " + 
				"from CONFIG_USER_PROVINCE " + 
				"where 1=1  and SYS_GROUP_NAME is not null  ");
		if(lstProvince!=null && lstProvince.size()>0) {
			sql.append(" and CAT_PROVINCE_CODE in (select code from CAT_PROVINCE where CAT_PROVINCE_ID in (:lstProvince)) ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		
		if(lstProvince!=null && lstProvince.size()>0) {
			query.setParameterList("lstProvince", lstProvince);
		}
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		
		return query.list();
	}
	//Huy-end
//	hoanm1_20200523_vsmart_start
	public StockDTO getStockById(Long stockId) {
        String sql = "select " +
                "a.cat_stock_id stockId, " +
                "a.code code, " +
                "a.name name " +
                "from cat_stock a " +
                "where cat_stock_id = :stockId ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("stockId", stockId);
        query.addScalar("stockId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(StockDTO.class));
        return (StockDTO) query.uniqueResult();
    }
	 public SysUserDTO getUserInfo(Long sysUserId) {
	        String sql = "select " +
	                "a.FULL_NAME sysUserName " +
	                ", (select name from sys_group where sys_group_id = a.sys_group_id) sysGroupName " +
	                "from sys_user a " +
	                "where a.sys_user_id= :sysUserId ";
	        SQLQuery query = getSession().createSQLQuery(sql);
	        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
	        query.setParameter("sysUserId", sysUserId);
	        query.addScalar("sysUserName", new StringType());
	        query.addScalar("sysGroupName", new StringType());
	        List list = query.list();
	        if (list != null && !list.isEmpty()) {
	            return (SysUserDTO) list.get(0);
	        }
	        return null;
	}
	 public long getSequenceStock() {
	        String sql = new String(
	                "select cat_stock_seq.nextval sequenceStockId from dual ");
	        SQLQuery query = getSession().createSQLQuery(sql);
	        query.addScalar("sequenceStockId", new LongType());
	        List<Long> lstSequence = query.list();
	        if (lstSequence != null && lstSequence.size() > 0) {
	            return lstSequence.get(0);
	        }
	        return -1;
	  }
	 public List<AIOStockTransDetailSerialDTO> getListDetailSerial(
	            Long stockTransDetailId) {
	        StringBuilder sql = new StringBuilder("");
	        sql.append(" select distinct a.serial serial,b.APPLY_PRICE price,a.CELL_CODE cellCode,a.MER_ENTITY_ID merEntityId,a.GOODS_STATE goodsState,a.QUANTITY quantity,a.QUANTITY_ISSUE quantityIssue ");
	        sql.append(" from STOCK_TRANS_DETAIL_SERIAL a,MER_ENTITY b where a.MER_ENTITY_id=b.MER_ENTITY_id and a.STOCK_TRANS_DETAIL_ID = :stockTransDetailId ");
	        SQLQuery query = getSession().createSQLQuery(sql.toString());
	        query.addScalar("serial", new StringType());
	        query.addScalar("price", new DoubleType());
	        query.addScalar("cellCode", new StringType());
	        query.addScalar("merEntityId", new LongType());
	        query.addScalar("goodsState", new StringType());
	        query.addScalar("quantity", new DoubleType());
	        query.addScalar("quantityIssue", new DoubleType());
	        query.setParameter("stockTransDetailId", stockTransDetailId);
	        query.setResultTransformer(Transformers
	                .aliasToBean(AIOStockTransDetailSerialDTO.class));

	        return query.list();
	    }
	 public int updateMerStockId(Long merId, Long stockId) {
	        StringBuilder sql = new StringBuilder("");
	        sql.append("UPDATE MER_ENTITY st SET st.STATUS=4,st.STOCK_ID = :stockReceiveId WHERE st.MER_ENTITY_ID  = :merEntityId");
	        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
	        query.setParameter("stockReceiveId", stockId);
	        query.setParameter("merEntityId", merId);
	        return query.executeUpdate();
	    }
	 public int recalculateApplyPrice(Long goodsId, Long stockId) {
	        String sql = "update MER_ENTITY a " +
	                "set a.apply_price = (select " +
	                "round((sum(amount*apply_price)/sum(amount)),2) from MER_ENTITY mer " +
	                "where mer.stock_id=a.stock_id and mer.GOODS_ID=a.GOODS_ID and mer.status=4 ) " +
	                "where a.stock_id = :stockReceiveId " +
	                "and a.GOODS_ID = :goodsId " +
	                "and a.status = 4 ";
	        SQLQuery query = this.getSession().createSQLQuery(sql);
	        query.setParameter("stockReceiveId", stockId);
	        query.setParameter("goodsId", goodsId);
	        return query.executeUpdate();
	    }
	 public AIOSynStockTransDTO getStockGoodTotal(Long StockId, Long goodId) {
	        String sql = new String(
	                "select a.stock_id stockId,a.stock_goods_total_id stockGoodsTotalId, a.amount,a.amount_issue amountIssue from stock_goods_total a where stock_id = :stockId and goods_id = :goodId and goods_state = 1 ");
	        SQLQuery query = getSession().createSQLQuery(sql);
	        query.setParameter("stockId", StockId);
	        query.setParameter("goodId", goodId);
	        query.addScalar("stockGoodsTotalId", new LongType());
	        query.addScalar("stockId", new LongType());
	        query.addScalar("amount", new DoubleType());
	        query.addScalar("amountIssue", new DoubleType());
	        query.setResultTransformer(Transformers
	                .aliasToBean(AIOSynStockTransDTO.class));
	        return (AIOSynStockTransDTO) query.uniqueResult();
	    }
	 public int updateStockGoodsTotalAmount(Long stockGoodsTotalId, Double stockAmount, Double stockAmountIssue,Double amountOrder) {
			String sql = "UPDATE stock_goods_total st " +
			"SET " +
			"change_date = sysdate, " +
			"st.amount = (:amount + :stockAmount) " +
			", st.amount_issue = (:amount + :stockAmountIssue) " +
			" WHERE st.stock_goods_total_id  = :stockGoodsTotalId ";
			
			SQLQuery query = this.getSession().createSQLQuery(sql);
			query.setParameter("amount", amountOrder);
			query.setParameter("stockAmount", stockAmount, new DoubleType());
			query.setParameter("stockAmountIssue", stockAmountIssue, new DoubleType());
			query.setParameter("stockGoodsTotalId", stockGoodsTotalId);
			
			return query.executeUpdate();
	}
//	hoanm1_20200523_vsmart_end
}
