package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.RevokeCashMonthPlanBO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author hailh10
 */
@Repository("revokeCashMonthPlanDAO")
public class RevokeCashMonthPlanDAO extends BaseFWDAOImpl<RevokeCashMonthPlanBO, Long> {

    public RevokeCashMonthPlanDAO() {
        this.model = new RevokeCashMonthPlanBO();
    }

    public RevokeCashMonthPlanDAO(Session session) {
        this.session = session;
    }	
    
    public List<CntContractDTO> getContractOutOS(List<String> lstContractCode){
    	StringBuilder sql = new StringBuilder(" select CNT_CONTRACT_ID cntContractId, "
    			+ "code code "
    			+ "from cnt_contract "
    			+ "where STATUS!=0 "
//    			+ "and CONTRACT_TYPE_O is not null "
    			+ "and upper(code) in (:lstContractCode) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("cntContractId", new LongType());
    	query.addScalar("code", new StringType());
    	query.setParameterList("lstContractCode", lstContractCode);
    	query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));
    	
    	return query.list();
    }
    
    public List<RevokeCashMonthPlanDTO> getSysUserById(Long code) {
    	StringBuilder sql = new StringBuilder("select SYS_USER_ID sysUserId "
    			+ "from SYS_USER "
    			+ "where TO_CHAR(EMPLOYEE_CODE) = TO_CHAR(:code) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("code", code);
    	query.addScalar("sysUserId" , new LongType());
    	query.setResultTransformer(Transformers.aliasToBean(RevokeCashMonthPlanDTO.class));
    	if(query.list().size() > 0) {
    		return query.list();
    	}
    	return new ArrayList<>();
    }
    
    public RevokeCashMonthPlanDTO getProvinceBySysGroup(Long id) {
    	StringBuilder sql = new StringBuilder("select AREA_CODE areaCode, "
    			+ "PROVINCE_CODE provinceCode "
    			+ "from SYS_GROUP "
    			+ "where SYS_GROUP_ID=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("areaCode", new StringType());
    	query.addScalar("provinceCode", new StringType());
    	query.setParameter("id", id);
    	query.setResultTransformer(Transformers.aliasToBean(RevokeCashMonthPlanDTO.class));
    	return (RevokeCashMonthPlanDTO)query.uniqueResult();
    }
    
	public void updateManageValueByCommand(RevokeCashMonthPlanDTO obj) {
		StringBuilder sql = new StringBuilder("update REVOKE_CASH_MONTH_PLAN set "
				+ " PERFORMER_ID=:performerId,"
				+ " START_DATE=:startDate,"
				+ " END_DATE=:endDate,"
				+ " STATUS=2,"
				+ " DESCRIPTION=:description " );
				sql.append(" where BILL_CODE=:billCode ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("performerId", obj.getPerformerId());
		query.setParameter("startDate", obj.getStartDate());
		query.setParameter("endDate", obj.getEndDate());
		query.setParameter("description", obj.getDescription());
		query.setParameter("billCode", obj.getBillCode());
		
		query.executeUpdate();
	}
	
    public List<ConstructionDTO> getConstructionCodeByList(List<String> lstConstructionCode){
    	StringBuilder sql = new StringBuilder(" select cons.CODE code, " + 
    			"cons.CONSTRUCTION_ID constructionId, " + 
    			"cons.CAT_STATION_ID catStationId, " + 
    			"cs.CODE catStationCode " + 
    			"from CONSTRUCTION cons " + 
    			"left join CTCT_CAT_OWNER.CAT_STATION cs " + 
    			"on cons.CAT_STATION_ID = cs.CAT_STATION_ID " + 
    			"where cons.status!=0 " + 
    			"and upper(cons.CODE) in (:lstConstructionCode) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catStationId", new LongType());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("code", new StringType());
    	query.addScalar("catStationCode", new StringType());
    	query.setParameterList("lstConstructionCode", lstConstructionCode);
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));
    	
    	return query.list();
    }
    
    public List<RevokeCashMonthPlanDTO> getContractConstr(List<String> lstContractConstr){
    	StringBuilder sql = new StringBuilder(" select " + 
    			"cnt.CODE cntContractCode, " + 
    			"ccw.CONSTRUCTION_ID constructionId, " + 
    			"cons.CODE constructionCode " + 
    			"from CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccw " + 
    			"left join CTCT_IMS_OWNER.CNT_CONTRACT cnt " + 
    			"on cnt.CNT_CONTRACT_ID = ccw.CNT_CONTRACT_ID " + 
    			"left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = ccw.CONSTRUCTION_ID " + 
    			"where cnt.STATUS!=0 " + 
    			"and cons.STATUS!=0 " + 
    			"and ccw.STATUS!=0 " + 
//    			"and cnt.CONTRACT_TYPE_O is not null " +
    			"and (upper(cnt.CODE) ||'+'|| upper(cons.CODE)) in (:lstContractConstr) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("cntContractCode", new StringType());
    	query.addScalar("constructionCode", new StringType());
    	query.setParameterList("lstContractConstr", lstContractConstr);
    	query.setResultTransformer(Transformers.aliasToBean(RevokeCashMonthPlanDTO.class));
    	
    	return query.list();
    }
    
    public void deleteRevoke(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE "
    			+ "from REVOKE_CASH_MONTH_PLAN "
    			+ "where DETAIL_MONTH_PLAN_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
	public List<RevokeCashMonthPlanBO> getRevokeCashMonthPlanByPlanId(RevokeCashMonthPlanDTO obj){
    	StringBuilder sql = new StringBuilder("select ");
    	sql.append("REVOKE_CASH_MONTH_PLAN_ID revokeCashMonthPlanId, ")
    	.append("CNT_CONTRACT_CODE cntContractCode, ")
    	.append("BILL_CODE billCode, ")
    	.append("CREATED_BILL_DATE createdBillDate, ")
    	.append("nvl(BILL_VALUE,0) billValue, ")
    	.append("AREA_CODE areaCode, ")
    	.append("PROVINCE_CODE provinceCode, ")
    	.append("PERFORMER_ID performerId, ")
    	.append("START_DATE startDate, ")
    	.append("END_DATE endDate, ")
    	.append("DESCRIPTION description, ")
    	.append("CREATED_USER_ID createdUserId, ")
    	.append("CREATED_DATE createdDate, ")
    	.append("UPDATED_USER_ID updatedUserId, ")
    	.append("UPDATED_DATE updatedDate, ")
    	.append("SIGN_STATE signState, ")
    	.append("DETAIL_MONTH_PLAN_ID detailMonthPlanId, ")
    	.append("STATUS status, ")
    	.append("CONSTRUCTION_ID constructionId, ")
    	.append("CONSTRUCTION_CODE constructionCode, ")
    	.append("CAT_STATION_ID catStationId, ")
    	.append("CAT_STATION_CODE catStationCode ")
    	.append("from REVOKE_CASH_MONTH_PLAN ")
    	.append("where DETAIL_MONTH_PLAN_ID=:id ");
    	StringBuilder sqlCount = new StringBuilder("select count(*) from ("+ sql.toString() +")");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
    	
    	query.addScalar("revokeCashMonthPlanId", new LongType());
    	query.addScalar("cntContractCode", new StringType());
    	query.addScalar("billCode", new StringType());
    	query.addScalar("createdBillDate", new DateType());
    	query.addScalar("billValue", new DoubleType());
    	query.addScalar("areaCode", new StringType());
    	query.addScalar("provinceCode", new StringType());
    	query.addScalar("performerId", new LongType());
    	query.addScalar("startDate", new DateType());
    	query.addScalar("endDate", new DateType());
    	query.addScalar("description", new StringType());
    	query.addScalar("createdUserId", new LongType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("updatedUserId", new LongType());
    	query.addScalar("updatedDate", new DateType());
    	query.addScalar("signState", new StringType());
    	query.addScalar("detailMonthPlanId", new LongType());
    	query.addScalar("status", new LongType());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("catStationId", new LongType());
    	query.addScalar("catStationCode", new StringType());
    	
    	query.setParameter("id", obj.getDetailMonthPlanId());
    	queryCount.setParameter("id", obj.getDetailMonthPlanId());
    	
    	query.setResultTransformer(Transformers.aliasToBean(RevokeCashMonthPlanDTO.class));
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    	
    	return query.list();
    }

}
