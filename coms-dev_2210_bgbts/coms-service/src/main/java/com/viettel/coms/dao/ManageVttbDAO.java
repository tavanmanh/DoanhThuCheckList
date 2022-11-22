package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.ManageVttbBO;
import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CatCommonDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthPlaningDTO;
import com.viettel.coms.dto.DmpnOrderDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("manageVttbDAO")
public class ManageVttbDAO extends BaseFWDAOImpl<ManageVttbBO, Long>{


    public ManageVttbDAO() {
        this.model = new ManageVttbBO();
    }

    public ManageVttbDAO(Session session) {
        this.session = session;
    }

    
    //tatph -start - 20112019
    public List<ManageVttbDTO> doSearch(ManageVttbDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append("      SELECT" + 
        				"            T2.CODE constructionCode," + 
        				"            T1.CONSTRUCTION_ID constructionId," + 
        				"            T1.CONTRACT_CODE contractCode," + 
        				"            T1.CODE pxkCode," + 
        				"            T3.CODE stationCode," + 
        				"            T4.CODE provinceCode," + 
        				"            sum(nvl(T5.AMOUNT_REAl,0)) vttbValue ," + 
        				"            T2.STATUS constructionStatus," + 
        				"            T6.CONTRACT_TYPE contractType  " + 
        				"            FROM    SYN_STOCK_TRANS T1  " + 
        				"            left join   SYN_STOCK_TRANS_DETAIL T5      on T1.SYN_STOCK_TRANS_ID = T5.SYN_STOCK_TRANS_ID  " + 
        				"            left join   CONSTRUCTION T2           on T1.CONSTRUCTION_ID = T2.CONSTRUCTION_ID    " + 
        				"            left join   CAT_STATION T3      on T2.CAT_STATION_id=T3.CAT_STATION_ID    " + 
        				"            left join   CAT_PROVINCE T4      on T3.CAT_PROVINCE_ID = T4.CAT_PROVINCE_ID    " + 
        				"            left join    CNT_CONTRACT T6      on T1.CONTRACT_CODE = T6.CODE    " + 
        				"            WHERE " + 
        				"            1 = 1");
        		
//        if(groupIdList.size() > 0) {
//        	sql.append("  and T1.PROVINCE_CODE in (select code from CAT_PROVINCE where CAT_PROVINCE.CAT_PROVINCE_ID in (:groupIdList )) ");
//        }
        if(StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
        	sql.append(" AND  T1.REAL_IE_TRANS_DATE >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  T1.REAL_IE_TRANS_DATE <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(T1.CONSTRUCTION_CODE) LIKE upper(:keySearch) escape '&')");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(
                    " AND (upper(T2.STATUS) = :status)");
        }
        if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
        	sql.append(" AND T4.CAT_PROVINCE_ID in :provinceIds");
        }
        sql.append(" and T1.CONTRACT_CODE is not null " + 
        		"            and T1.CONSTRUCTION_CODE is not null" + 
        		"            and T1.TYPE = 2 " + 
        		"            and T1.CONFIRM = 1 " + 
        		"            group by" + 
        		"            T2.CODE ," + 
        		"            T1.CONSTRUCTION_ID ," + 
        		"            T1.CONTRACT_CODE ," + 
        		"            T1.CODE ," + 
        		"            T3.CODE ," + 
        		"            T4.CODE ," + 
        		"            T2.STATUS ," + 
        		"            T6.CONTRACT_TYPE   " + 
        		"            order by T2.CODE");
//        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            query.setParameter("status",  ValidateUtils.validateKeySearch(obj.getStatus()) );
            queryCount.setParameter("status",  ValidateUtils.validateKeySearch(obj.getStatus()) );
        }
        if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
            query.setParameter("startDate", obj.getStartDate());
            queryCount.setParameter("startDate", obj.getStartDate());
            query.setParameter("endDate", obj.getEndDate());
            queryCount.setParameter("endDate", obj.getEndDate());
        }
        if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
        	  query.setParameterList("provinceIds", obj.getProvinceIds());
              queryCount.setParameterList("provinceIds", obj.getProvinceIds());
        }
        
//        if (groupIdList.size() > 0) {
//            query.setParameterList("groupIdList", groupIdList);
//            queryCount.setParameterList("groupIdList", groupIdList);
//        }
        query.addScalar("constructionCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("contractType", new LongType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("pxkCode", new StringType());
        query.addScalar("vttbValue", new LongType());
        query.addScalar("constructionStatus", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public List<ManageVttbDTO> doSearchUsedMaterial(ManageVttbDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append("with t1 as(SELECT" + 
        				"            T2.CODE constructionCode," + 
        				"            T1.REAL_IE_TRANS_DATE realIeTransDate ," + 
        				"            T1.CONSTRUCTION_ID constructionId," + 
        				"            T1.CONTRACT_CODE contractCode," + 
        				"            T1.CODE pxkCode," + 
        				"            T3.CODE stationCode," + 
        				"            T4.CODE provinceCode," + 
        				"            T4.CAT_PROVINCE_ID catProvinceId," + 
        				"            nvl(T5.AMOUNT_REAl,            0) vttbValue ," + 
        				"            T2.STATUS constructionStatus," + 
        				"            T6.CONTRACT_TYPE contractType," + 
        				"            T1.CREATED_DATE pxkDate," + 
        				"            T7.GOODS_CODE vttbCode ," + 
        				"            T7.GOODS_NAME vttbName ," + 
        				"            nvl(T5.AMOUNT_REAL,0) soLuong ," + 
        				"            nvl((T5.AMOUNT_REAL * T7.UNIT_PRICE),0) giaTri " + 
        				"            FROM SYN_STOCK_TRANS T1 " + 
        				"            left join SYN_STOCK_TRANS_DETAIL T5 on T1.SYN_STOCK_TRANS_ID = T5.SYN_STOCK_TRANS_ID             " + 
        				"            left join SYN_STOCK_TRANS_DETAIL_SERIAL T7 on T1.SYN_STOCK_TRANS_ID = T7.SYN_STOCK_TRANS_ID             " + 
        				"            left join CONSTRUCTION T2 on T1.CONSTRUCTION_ID = T2.CONSTRUCTION_ID               " + 
        				"            left join CAT_STATION T3 on T2.CAT_STATION_id=T3.CAT_STATION_ID             " + 
        				"            left join CAT_PROVINCE T4 on T3.CAT_PROVINCE_ID = T4.CAT_PROVINCE_ID             " + 
        				"            left join CNT_CONTRACT T6 on T1.CONTRACT_CODE = T6.CODE              " + 
        				"            where" + 
        				"            T1.TYPE = 2)," + 
        				"            t2 as(" + 
        				"            select" + 
        				"            sum(soLuong) soLuongPxk ," + 
        				"            sum(giaTri) giaTriPxk ," + 
        				"            decode(t2.so_luong_su_dung ," + 
        				"            null ," + 
        				"            sum(soLuong) ," + 
        				"            t2.so_luong_su_dung ) soLuongSuDung ," + 
        				"            decode(t2.gia_tri_su_dung ," + 
        				"            null ," + 
        				"            sum(giaTri) ," + 
        				"            t2.gia_tri_su_dung ) giaTriSuDung ," + 
        				"            t2.manage_used_material_id manageUsedMaterialId," + 
        				"            vttbCode," + 
        				"            constructionCode    ," + 
        				"            contractCode ," + 
        				"            pxkCode," + 
        				"            catProvinceId," + 
        				"            realIeTransDate" + 
        				"            from            t1             " + 
        				"            left join  manage_used_material t2  on t1.constructionCode = t2.construction_code  and t1.vttbCode = t2.vttb_code             " + 
        				"            group by" + 
        				"            constructionCode," + 
        				"            catProvinceId," + 
        				"            vttbCode ," + 
        				"            t2.gia_tri_su_dung ," + 
        				"            t2.so_luong_su_dung," + 
        				"            t2.manage_used_material_id ," + 
        				"            contractCode,pxkCode,realIeTransDate) ," + 
        				"            t3 as ( SELECT" + 
        				"            T1.CONSTRUCTION_CODE constructionCode," + 
        				"            nvl(T5.AMOUNT_REAl,0) vttbValue ," + 
        				"            T7.GOODS_CODE vttbCode ," + 
        				"            nvl(T5.AMOUNT_REAL,0) soLuong ," + 
        				"            nvl((T5.AMOUNT_REAL * T7.UNIT_PRICE), 0) giaTri             " + 
        				"            FROM    SYN_STOCK_TRANS T1              " + 
        				"            left join   SYN_STOCK_TRANS_DETAIL T5   on T1.SYN_STOCK_TRANS_ID = T5.SYN_STOCK_TRANS_ID             " + 
        				"            left join   SYN_STOCK_TRANS_DETAIL_SERIAL T7  on T1.SYN_STOCK_TRANS_ID = T7.SYN_STOCK_TRANS_ID             " + 
        				"            where" + 
        				"            T1.TYPE = 1)," + 
        				"            t4 as( " + 
        				"            select" + 
        				"            sum(soLuong) soLuongThuHoi ," + 
        				"            sum(giaTri) giaTriThuHoi ," + 
        				"            constructionCode ," + 
        				"            vttbCode  " + 
        				"            from    t3             " + 
        				"            GROUP BY" + 
        				"            constructionCode ," + 
        				"            vttbCode)             " + 
        				"            select" + 
        				"            a.soLuongPxk soLuongPxk," + 
        				"            a.catProvinceId catProvinceId," + 
        				"            a.giaTriPxk giaTriPxk," + 
        				"            a.vttbCode vttbCode," + 
        				"            a.constructionCode constructionCode," + 
        				"            a.soLuongSuDung soLuongSuDung," + 
        				"            a.giaTriSuDung giaTriSuDung," + 
        				"            (soLuongPxk - soLuongSuDung) soLuongDuThua ," + 
        				"            (giaTriPxk - giaTriSuDung) giaTriDuThua ," + 
        				"            a.manageUsedMaterialId manageUsedMaterialId ," + 
        				"            a.contractCode contractCode," + 
        				"            a.pxkCode pxkCode," + 
        				"            decode(b.soLuongThuHoi,   null, 0,b.soLuongThuHoi) soLuongThuHoi," + 
        				"            decode(b.giaTriThuHoi, null ,  0,  b.giaTriThuHoi) giaTriThuHoi  " + 
        				"            from" + 
        				"            t2 a              " + 
        				"            left join" + 
        				"            t4 b              " + 
        				"            on a.constructionCode = b.constructionCode              " + 
        				"            and a.vttbCode = b.vttbCode " + 
        				"            where " + 
        				"            1 = 1 ");
        	
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(a.constructionCode) LIKE upper(:keySearch)  escape '&')");
        }
        if(StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
        	sql.append(" AND  a.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  a.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }
        if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
        	sql.append(" AND a.catProvinceId in :provinceIds ");
        }
        sql.append(" and a.constructionCode is not null  " + 
        		"            and a.contractCode is not null" + 
        		"            order by" + 
        		"            a.constructionCode ," + 
        		"            a.vttbCode desc ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
            query.setParameter("startDate", obj.getStartDate());
            queryCount.setParameter("startDate", obj.getStartDate());
            query.setParameter("endDate", obj.getEndDate());
            queryCount.setParameter("endDate", obj.getEndDate());
        }
//        if (groupIdList.size() > 0) {
//            query.setParameterList("groupIdList", groupIdList);
//            queryCount.setParameterList("groupIdList", groupIdList);
//        }
        if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
        	 query.setParameterList("provinceIds", obj.getProvinceIds());
             queryCount.setParameterList("provinceIds", obj.getProvinceIds());
        }
        query.addScalar("manageUsedMaterialId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("vttbCode", new StringType());
        query.addScalar("pxkCode", new StringType());
        
        query.addScalar("soLuongPxk", new LongType());
        query.addScalar("giaTriPxk", new LongType());
        query.addScalar("soLuongSuDung", new LongType());
        query.addScalar("giaTriSuDung", new LongType());
        query.addScalar("soLuongDuThua", new LongType());
        query.addScalar("giaTriDuThua", new LongType());
        query.addScalar("soLuongDuThua", new LongType());
        query.addScalar("giaTriDuThua", new LongType());
        query.addScalar("soLuongThuHoi", new LongType());
        query.addScalar("giaTriThuHoi", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public void saveUsedMaterial(ManageUsedMaterialDTO obj) {
		Session session = this.getSession();
		session.save(obj.toModel());
	}
    public void updateUsedMaterial(ManageUsedMaterialDTO obj) {
		Session session = this.getSession();
		session.update(obj.toModel());
	}
    
    public Long countUsedMaterial(ManageUsedMaterialDTO obj) {
		StringBuilder sql = new StringBuilder("Select count(*) "
				+ " FROM MANAGE_USED_MATERIAL T1");
		sql.append(" WHERE UPPER(T1.CONSTRUCTION_CODE) = UPPER(:constructionCode) AND UPPER(T1.VTTB_CODE) = UPPER(:vttbCode) " );
		SQLQuery query= getSession().createSQLQuery(sql.toString());
		
		if (null != obj.getConstructionCode()) {  //id hop dong
			query.setParameter("constructionCode", obj.getConstructionCode());
		}
		if (null != obj.getVttbCode()) {  //id hop dong
			query.setParameter("vttbCode", obj.getVttbCode());
		}
		
	
		Long count =((BigDecimal) query.uniqueResult()).longValue();
		return count;
	}
    
    //tatph -end - 20112019
    
}
