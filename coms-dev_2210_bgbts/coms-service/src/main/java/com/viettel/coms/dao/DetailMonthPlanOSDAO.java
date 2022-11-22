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
import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CatCommonDTO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthPlaningDTO;
import com.viettel.coms.dto.DmpnOrderDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("detailMonthPlanOSDAO")
public class DetailMonthPlanOSDAO extends BaseFWDAOImpl<DetailMonthPlanBO, Long>{


    public DetailMonthPlanOSDAO() {
        this.model = new DetailMonthPlanBO();
    }

    public DetailMonthPlanOSDAO(Session session) {
        this.session = session;
    }

    public List<DetailMonthPlaningDTO> doSearch(DetailMonthPlanSimpleDTO obj, List<String> groupIdList) {
        StringBuilder sql = new StringBuilder("SELECT DMP.DETAIL_MONTH_PLAN_ID detailMonthPlanId," 
        		+ " DMP.YEAR year ,"
                + " DMP.sys_group_id sysGroupId ," 
        		+ " DMP.MONTH month ," 
                + " DMP.CREATED_DATE createdDate ,"
                + " DMP.Created_user_id createdUserId, " 
                + " DMP.Created_group_id createdGroupId ," 
                + " DMP.CODE code ,"
                + " DMP.NAME name ," 
                + " DMP.Sign_state signState ," 
                + " DMP.STATUS status , "
                + " DMP.DESCRIPTION description ," 
                + " SYSG.NAME sysName, " + " SYSG.Code sysGroupCode "
                + " FROM DETAIL_MONTH_PLAN DMP LEFT JOIN sys_group SYSG ON DMP.sys_group_id=SYSG.sys_group_id"
                + " WHERE 1=1 AND DMP.TYPE =:type ");

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(DMP.CODE) LIKE upper(:keySearch) OR  upper(DMP.YEAR) LIKE upper(:keySearch) OR upper(DMP.NAME) LIKE upper(:keySearch) escape '&')");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(" AND DMP.STATUS = :status");

        }
        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            sql.append(" AND DMP.Sign_state  in :signStateList");
        }
        if (obj.getMonthList() != null && !obj.getMonthList().isEmpty()) {
            sql.append(" AND DMP.MONTH  in :monthList");
        }
        if (obj.getYearList() != null && !obj.getYearList().isEmpty()) {
            sql.append(" AND DMP.YEAR  in :yearList");
        }

        if (obj.getSysGroupId() != null) {
            sql.append(" AND SYSG.SYS_GROUP_ID  = :sysGroupId");
        }
        if (groupIdList != null && !groupIdList.isEmpty()) {
            sql.append(" AND DMP.sys_group_id  in :groupIdList");
        }
        sql.append(" ORDER BY DMP.DETAIL_MONTH_PLAN_ID DESC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        
        query.setParameter("type", obj.getType());
        queryCount.setParameter("type", obj.getType());
        
        if (groupIdList != null && !groupIdList.isEmpty()) {
            query.setParameterList("groupIdList", groupIdList);
            queryCount.setParameterList("groupIdList", groupIdList);
        }
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }

        if (StringUtils.isNotEmpty(obj.getStatus()) && !"2".equals(obj.getStatus())) {
            query.setParameter("status", obj.getStatus());
            queryCount.setParameter("status", obj.getStatus());
        }

        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            query.setParameterList("signStateList", obj.getSignStateList());
            queryCount.setParameterList("signStateList", obj.getSignStateList());
        }
        if (obj.getMonthList() != null && !obj.getMonthList().isEmpty()) {
            query.setParameterList("monthList", obj.getMonthList());
            queryCount.setParameterList("monthList", obj.getMonthList());
        }
        if (obj.getYearList() != null && !obj.getYearList().isEmpty()) {
            query.setParameterList("yearList", obj.getYearList());
            queryCount.setParameterList("yearList", obj.getYearList());
        }
        if (obj.getSysGroupId() != null) {
            query.setParameter("sysGroupId", obj.getSysGroupId());
            queryCount.setParameter("sysGroupId", obj.getSysGroupId());
        }

        query.addScalar("detailMonthPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("month", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("sysName", new StringType());
        query.addScalar("sysGroupCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlaningDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public boolean checkMonthYearSys(Long month, Long year, Long sysGroupId, Long detailMonthId) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DETAIL_MONTH_PLAN_ID) FROM DETAIL_MONTH_PLAN where 1=1 and status = 1 and type=1 and month=:month and year=:year and sys_group_id = :sysGroupId");
        if (detailMonthId != null) {
            sql.append(" AND DETAIL_MONTH_PLAN_ID != :detailMonthId ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("sysGroupId", sysGroupId);
        if (detailMonthId != null) {
            query.setParameter("detailMonthId", detailMonthId);
        }
        return ((BigDecimal) query.uniqueResult()).intValue() > 0;
    }

    public DetailMonthPlanSimpleDTO getById(Long id) {
        StringBuilder sql = new StringBuilder("SELECT detailMonth.detail_month_plan_id detailMonthPlanId,"
                + " detailMonth.YEAR year ," + " detailMonth.MONTH month ," + " detailMonth.SYS_GROUP_ID sysGroupId ,"
                + " detailMonth.CREATED_DATE createdDate ," + " detailMonth.Created_user_id createdUserId, "
                + " detailMonth.Created_group_id createdGroupId ," + " detailMonth.CODE code ,"
                + " detailMonth.NAME name ," + " detailMonth.Sign_state signState ," + " detailMonth.STATUS status , "
                + " sg.NAME sysGroupName , " + " sg.Code sysGroupCode , " + " detailMonth.DESCRIPTION description"
                + " FROM DETAIL_MONTH_PLAN detailMonth"
                + " LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON detailMonth.SYS_GROUP_ID =sg.SYS_GROUP_ID"
                + " WHERE DETAIL_MONTH_PLAN_ID =:id ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.addScalar("detailMonthPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("month", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("sysGroupCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlanSimpleDTO.class));
        return (DetailMonthPlanSimpleDTO) query.uniqueResult();
    }

    public void remove(Long detailMonthPlanId) {
        StringBuilder sql = new StringBuilder(
                "UPDATE DETAIL_MONTH_PLAN set status = 0  where DETAIL_MONTH_PLAN_ID = :detailMonthPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("detailMonthPlanId", detailMonthPlanId);
        query.executeUpdate();

    }

    public List<TmpnTargetDetailDTO> getYearPlanDetailTarget(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                "SELECT   sum(ypd.QUANTITY) quantity,   sum(ypd.COMPLETE) complete,"
                        + " sum(ypd.REVENUE) revenue,  sg.NAME sysGroupName FROM TMPN_TARGET_OS ypd "
                        + " LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON ypd.SYS_GROUP_ID  =sg.SYS_GROUP_ID "
                        + " WHERE ypd.year       =:year AND ypd.month        = :month AND ypd.SYS_GROUP_ID = :sysGroupId "
                        + " group by sg.NAME ORDER BY sg.NAME ASC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("year", obj.getYear());
        query.setParameter("month", obj.getMonth());
        query.setParameter("sysGroupId", obj.getSysGroupId());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("complete", new DoubleType());
        query.addScalar("revenue", new DoubleType());

        query.addScalar("sysGroupName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(TmpnTargetDetailDTO.class));
        return query.list();
    }

    public List<WorkItemDetailDTO> getWorkItemDetail(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
//		chinhpxn20180710_start
//		StringBuilder sql = new StringBuilder(
//				"SELECT   wi.CODE code,wi.NAME name,cwi.NAME catWorkItemTypeName ,wi.STATUS status,wi.QUANTITY/1000000 quantity "
//						+ " FROM WORK_ITEM wi left join CONSTRUCTION ct on wi.CONSTRUCTION_ID=ct.CONSTRUCTION_ID "
//						+ "left join CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE cwi on wi.CAT_WORK_ITEM_TYPE_ID=cwi.CAT_WORK_ITEM_TYPE_ID   "
//						+ "WHERE  ct.CODE= :constructionCode");
        StringBuilder sql = new StringBuilder("WITH re AS ");
        sql.append("   (SELECT wi.code code, ");
        sql.append("     wi.status status, ");
        sql.append("     cwi.name catWorkItemTypeName, ");
        sql.append("     wi.work_item_id ");
        sql.append("   FROM work_item wi ");
        sql.append("   LEFT JOIN cat_work_item_type cwi ");
        sql.append("   ON wi.cat_work_item_type_id = cwi.cat_work_item_type_id ");
        sql.append("   WHERE wi.work_item_id      IN ");
        sql.append("     (SELECT WORK_ITEM_ID ");
        sql.append("     FROM construction_task ");
        sql.append("     WHERE type         =5 ");
        sql.append(
                "     AND construction_id= (select construction_id from construction where code = :constructionCode) ");
        sql.append("     AND DETAIL_MONTH_PLAN_ID          =:detailMonthPlanId ");
        sql.append("     ) ");
        sql.append("   ) ");
        sql.append(" SELECT re.code code, ");
        sql.append("   catWorkItemTypeName, ");
        sql.append("   re.status status, ");
        sql.append("   quantity ");
        sql.append(" FROM construction_task ct ");
        sql.append(" INNER JOIN re ");
        sql.append(" ON ct.work_item_id = re.work_item_id ");
        sql.append(" WHERE type         =5 ");
        sql.append(" AND construction_id= (select construction_id from construction where code = :constructionCode) ");
        sql.append(" AND DETAIL_MONTH_PLAN_ID          =:detailMonthPlanId ");
//		chinhpxn20180710_end
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("constructionCode", obj.getConstructionCode());
        query.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
        query.addScalar("code", new StringType());
        query.addScalar("catWorkItemTypeName", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
        return query.list();
    }

    public DepartmentDTO getSysUser(String loginName) {
        StringBuilder sql = new StringBuilder(
                "SELECT SYS_USER_ID departmentId," + " FULL_NAME name " + " FROM SYS_USER "
                        // chinhpxn 20180607 start
                        + " WHERE TYPE_USER is null and UPPER(LOGIN_NAME) = UPPER(:LOGINNAME) OR UPPER(REPLACE(EMAIL,'@viettel.com.vn','')) = UPPER(:LOGINNAME) ");
        // chinh-xn20180607 end
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("LOGINNAME", loginName.trim());
        query.addScalar("departmentId", new LongType());
        query.addScalar("name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(DepartmentDTO.class));
        List<DepartmentDTO> list = query.list();
        DepartmentDTO data = new DepartmentDTO();
        if (list != null)
            data = list.get(0);
        return data;
    }
//    hoanm1_20191114_start
    public Map<String, SysUserCOMSDTO> getSysUserLoginName(){
    	try{
    	 StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID sysUserId, FULL_NAME fullName,upper(LOGIN_NAME) loginName FROM SYS_USER ");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(SysUserCOMSDTO.class));
			query.addScalar("sysUserId", new LongType());
		    query.addScalar("fullName", new StringType());
		    query.addScalar("loginName", new StringType());
			List<SysUserCOMSDTO> lstUser = query.list();
			Map<String, SysUserCOMSDTO> sysUserMap = new HashMap<String, SysUserCOMSDTO>();
			for (SysUserCOMSDTO obj : lstUser) {
				sysUserMap.put(obj.getLoginName(), obj);
			}
			return sysUserMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    public Map<String, SysUserCOMSDTO> getSysUserEmail(){
    	try{
    	 StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID sysUserId, FULL_NAME fullName,upper(REPLACE(EMAIL,'@viettel.com.vn',''))loginName FROM SYS_USER ");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(SysUserCOMSDTO.class));
			query.addScalar("sysUserId", new LongType());
		    query.addScalar("fullName", new StringType());
		    query.addScalar("loginName", new StringType());
			List<SysUserCOMSDTO> lstUser = query.list();
			Map<String, SysUserCOMSDTO> sysUserMap = new HashMap<String, SysUserCOMSDTO>();
			for (SysUserCOMSDTO obj : lstUser) {
				sysUserMap.put(obj.getLoginName(), obj);
			}
			return sysUserMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
//    hoanm1_20191114_end
    public void updatePerforment(Long performentId, Long workItemId) {
        String sql = new String("update work_item set PERFORMER_ID = :perFormerId where work_item_id = :workItemId ");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("perFormerId", performentId);
        query.setParameter("workItemId", workItemId);
        query.executeUpdate();
    }

    public Long getSequence() {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder("Select DETAIL_MONTH_PLAN_SEQ.nextVal FROM DUAL");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        return ((BigDecimal) query.uniqueResult()).longValue();
    }

    public List<TmpnTargetDTO> getTmpnTargetForExport(Long detailMonthPlanId, Long month, Long year,
                                                      String sysGroupId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder("SELECT ypd.Total_month_plan_id totalMonthPlanId,"
                + "ypd.TMPN_TARGET_ID tmpnTargetId," + "ypd.SYS_GROUP_ID sysGroupId," + "ypd.MONTH month,"
                + "ypd.YEAR year," + "ypd.QUANTITY quantity," + "ypd.COMPLETE complete," + "ypd.REVENUE revenue, "
                + "sg.NAME sysGroupName, "
                + " (select sum(work.quantity) from WORK_ITEM work where work.CONSTRUCTOR_ID=ypd.SYS_GROUP_ID and EXTRACT(month FROM work.complete_date)>=1 and EXTRACT(month FROM work.complete_date)<ypd.month) quantityLk, "
                + " (select consH.complete_value from CONSTRUCTION consH,CONSTRUCTION_TASK b  where consH.SYS_GROUP_ID=ypd.SYS_GROUP_ID "
                + " and consH.construction_id=b.construction_id and b.type=2 and b.status =4 and (consH.status=5 or (consH.status =4 and consH.is_obstructed=1 and consH.obstructed_state=2)) "
                + " and EXTRACT(month FROM consH.approve_complete_date)>=1 and EXTRACT(month FROM consH.approve_complete_date)<ypd.month) completeLk, "
                + " (select sum(consH.approve_revenue_value) from CONSTRUCTION consH where consH.SYS_GROUP_ID=ypd.SYS_GROUP_ID and EXTRACT(month FROM consH.approve_revenue_date)>=1 and EXTRACT(month FROM consH.approve_revenue_date)<ypd.month) revenueLk, "
                + " (select sum(yq.quantity) from YEAR_PLAN_DETAIL  yq where yq.SYS_GROUP_ID = ypd.SYS_GROUP_ID) quantityInYear, "
                + " (select sum(yq.complete) from YEAR_PLAN_DETAIL  yq where yq.SYS_GROUP_ID = ypd.SYS_GROUP_ID) completeInYear, "
                + " (select sum(yq.revenue) from YEAR_PLAN_DETAIL  yq where yq.SYS_GROUP_ID = ypd.SYS_GROUP_ID) revenueInYear "
                + " FROM TMPN_TARGET ypd");
        sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON ypd.SYS_GROUP_ID =sg.SYS_GROUP_ID  ");
        sql.append(
                "WHERE ypd.Total_month_plan_id=:id and ypd.SYS_GROUP_ID =:sysGroupId and ypd.month = :month and ypd.year = :year ");
        sql.append(" ORDER BY sg.NAME ASC,ypd.month ASC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", detailMonthPlanId);
        query.setParameter("month", month);
        query.setParameter("year", year);
        query.setParameter("sysGroupId", sysGroupId);
        query.addScalar("tmpnTargetId", new LongType());
        query.addScalar("totalMonthPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("month", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("complete", new DoubleType());
        query.addScalar("revenue", new DoubleType());
        query.addScalar("quantityLk", new DoubleType());
        query.addScalar("revenueLk", new DoubleType());
        query.addScalar("completeLk", new DoubleType());
        query.addScalar("revenueInYear", new DoubleType());
        query.addScalar("completeInYear", new DoubleType());
        query.addScalar("quantityInYear", new DoubleType());
        query.addScalar("sysGroupName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(TmpnTargetDTO.class));
        return query.list();
    }

    public List<ConstructionTaskDetailDTO> getPh12ForExportDoc(Long detailMonthPlanId, String type) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                " with btsCount as( select count(CONSTRUCTION_TASK_ID) btsCount,province.cat_province_id catProvinceId,sum(ct.quantity) btsSum,ct.DIRECTOR_ID,ct.SUPERVISOR_ID FROM CONSTRUCTION_TASK ct ");
        sql.append(" LEFT JOIN CONSTRUCTION cons LEFT JOIN CAT_STATION cs ON cons.CAT_STATION_ID =cs.CAT_STATION_ID ");
        sql.append(" ON ct.CONSTRUCTION_ID =cons.CONSTRUCTION_ID LEFT JOIN cat_province province ");
        sql.append(
                " ON province.cat_province_id   = cs.cat_province_id  left join cat_construction_type cct on cct.cat_construction_type_id = cons.cat_construction_type_id  ");
        sql.append(" WHERE ct.DETAIL_MONTH_PLAN_ID = :id and cct.name Like 'Công trình BTS' AND ct.type = :type");
        if ("1".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 3 ");
        } else if ("2".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 4 ");
        }
        sql.append("  group by province.cat_province_id,ct.DIRECTOR_ID,ct.SUPERVISOR_ID), ");
        sql.append(
                " tuyenCount as( select count(CONSTRUCTION_TASK_ID) tuyenCount,province.cat_province_id catProvinceId,sum(ct.quantity) tuyenSum,ct.DIRECTOR_ID,ct.SUPERVISOR_ID   FROM CONSTRUCTION_TASK ct LEFT JOIN CONSTRUCTION cons LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID ON ct.CONSTRUCTION_ID =cons.CONSTRUCTION_ID LEFT JOIN cat_province province ");
        sql.append(
                " ON province.cat_province_id   = cs.cat_province_id  left join cat_construction_type cct on cct.cat_construction_type_id = cons.cat_construction_type_id  ");
        sql.append(
                " WHERE ct.DETAIL_MONTH_PLAN_ID = :id and cct.name like 'Công trình tuyến' AND ct.type = :type and ct.level_id = 2 ");
        if ("1".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 3 ");
        } else if ("2".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 4 ");
        }
        sql.append("group by province.cat_province_id, ct.DIRECTOR_ID,ct.SUPERVISOR_ID), ");
        sql.append(
                "  gponCount as( select count(CONSTRUCTION_TASK_ID) gponCount,province.cat_province_id catProvinceId,sum(ct.quantity) gponSum,ct.DIRECTOR_ID,ct.SUPERVISOR_ID  FROM CONSTRUCTION_TASK ct LEFT JOIN CONSTRUCTION cons LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID ON ct.CONSTRUCTION_ID =cons.CONSTRUCTION_ID LEFT JOIN cat_province province ");
        sql.append(
                " ON province.cat_province_id   = cs.cat_province_id  left join cat_construction_type cct on cct.cat_construction_type_id = cons.cat_construction_type_id  ");
        sql.append(
                " WHERE ct.DETAIL_MONTH_PLAN_ID = :id and cct.name like 'Công trình GPON' AND ct.type = :type and ct.level_id = 2 ");
        if ("1".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 3 ");
        } else if ("2".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 4 ");
        }
        sql.append("group by province.cat_province_id,ct.DIRECTOR_ID,ct.SUPERVISOR_ID), ");
        sql.append(
                "  leCount as( select count(CONSTRUCTION_TASK_ID) leCount,province.cat_province_id catProvinceId,sum(ct.quantity) leSum,ct.DIRECTOR_ID,ct.SUPERVISOR_ID  FROM CONSTRUCTION_TASK ct LEFT JOIN CONSTRUCTION cons LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID ON ct.CONSTRUCTION_ID =cons.CONSTRUCTION_ID LEFT JOIN cat_province province ");
        sql.append(
                " ON province.cat_province_id   = cs.cat_province_id  left join cat_construction_type cct on cct.cat_construction_type_id = cons.cat_construction_type_id  ");
        sql.append(
                " WHERE ct.DETAIL_MONTH_PLAN_ID = :id and cct.name like 'Công trình lẻ' AND ct.type = :type and ct.level_id = 2 ");
        if ("1".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 3 ");
        } else if ("2".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 4 ");
        }
        sql.append("group by province.cat_province_id,ct.DIRECTOR_ID,ct.SUPERVISOR_ID), ");
        sql.append(
                "  child as( SELECT ct.DIRECTOR_ID,ct.SUPERVISOR_ID,province.code catProvinceCode, province.cat_province_id catProvinceId,  captain.FULL_NAME supervisorName,  direct.FULL_NAME directorName , ");
        sql.append(
                " sum(ct.quantity) quantitySum FROM CONSTRUCTION_TASK ct LEFT JOIN SYS_USER captain ON captain.SYS_USER_id = ct.SUPERVISOR_ID LEFT JOIN SYS_USER direct ");
        sql.append(
                " ON direct.SYS_USER_id = ct.DIRECTOR_ID LEFT JOIN CONSTRUCTION cons ON ct.CONSTRUCTION_ID =cons.CONSTRUCTION_ID LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID LEFT JOIN cat_province province ON province.cat_province_id   = cs.cat_province_id WHERE ct.DETAIL_MONTH_PLAN_ID = :id ");
        sql.append(" AND ct.type                      = :type");
        if ("1".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 3 ");
        } else if ("2".equalsIgnoreCase(type)) {
            sql.append(" and ct.level_id = 4 ");
        }
        sql.append(
                " group by province.code, captain.FULL_NAME ,  direct.FULL_NAME,province.cat_province_id,ct.DIRECTOR_ID,ct.SUPERVISOR_ID ) ");
        sql.append(
                " select child.*,(select gponCount.gponCount from gponCount where gponCount.catProvinceId = child.catProvinceId and gponCount.DIRECTOR_ID= child.DIRECTOR_ID and gponCount.SUPERVISOR_ID= child.SUPERVISOR_ID) gponCount,  ");
        sql.append(
                " (select gponCount.gponSum from gponCount where gponCount.catProvinceId = child.catProvinceId and gponCount.DIRECTOR_ID= child.DIRECTOR_ID and gponCount.SUPERVISOR_ID= child.SUPERVISOR_ID) gponSum , ");
        sql.append(
                " (select leCount.leCount from leCount where leCount.catProvinceId = child.catProvinceId and leCount.DIRECTOR_ID= child.DIRECTOR_ID and leCount.SUPERVISOR_ID= child.SUPERVISOR_ID) leCount,  ");
        sql.append(
                " (select leCount.leSum from leCount where leCount.catProvinceId = child.catProvinceId and leCount.DIRECTOR_ID= child.DIRECTOR_ID and leCount.SUPERVISOR_ID= child.SUPERVISOR_ID) leSum , ");
        sql.append(
                " (select btsCount.btsCount from btsCount where btsCount.catProvinceId = child.catProvinceId and btsCount.DIRECTOR_ID= child.DIRECTOR_ID and btsCount.SUPERVISOR_ID= child.SUPERVISOR_ID) btsCount,  ");
        sql.append(
                " (select btsCount.btsSum from btsCount where btsCount.catProvinceId = child.catProvinceId and btsCount.DIRECTOR_ID= child.DIRECTOR_ID and btsCount.SUPERVISOR_ID= child.SUPERVISOR_ID) btsSum , ");
        sql.append(
                " (select tuyenCount.tuyenCount from tuyenCount where tuyenCount.catProvinceId = child.catProvinceId and tuyenCount.DIRECTOR_ID= child.DIRECTOR_ID and tuyenCount.SUPERVISOR_ID= child.SUPERVISOR_ID) tuyenCount, ");
        sql.append(
                " (select tuyenCount.tuyenSum from tuyenCount where tuyenCount.catProvinceId = child.catProvinceId and tuyenCount.DIRECTOR_ID= child.DIRECTOR_ID and tuyenCount.SUPERVISOR_ID= child.SUPERVISOR_ID) tuyenSum  ");
        sql.append(" from child child ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", detailMonthPlanId);
        query.setParameter("type", type);
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("supervisorName", new StringType());
        query.addScalar("directorName", new StringType());
        query.addScalar("quantitySum", new LongType());
        query.addScalar("gponCount", new LongType());
        query.addScalar("gponSum", new LongType());
        query.addScalar("leCount", new LongType());
        query.addScalar("leSum", new LongType());
        query.addScalar("tuyenCount", new LongType());
        query.addScalar("tuyenSum", new LongType());
        query.addScalar("btsSum", new LongType());
        query.addScalar("btsCount", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
        return query.list();
    }

    public List<ConstructionTaskDetailDTO> getPl3ForExportDoc(Long detailMonthPlanId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                " SELECT province.code catProvinceCode,  cntWork.code cntContract,  sum(consTask.quantity) quantity,consTask.description description FROM CONSTRUCTION_TASK consTask ");
        sql.append(
                " LEFT JOIN  (select distinct cnt.code, cntWork.CONSTRUCTION_ID from CNT_CONSTR_WORK_ITEM_TASK cntWork inner join CNT_CONTRACT cnt ON cnt.CNT_CONTRACT_ID = cntWork.CNT_CONTRACT_ID  and cnt.CONTRACT_TYPE = 0 and cnt.status!=0) cntWork ON cntWork.CONSTRUCTION_ID=  consTask.CONSTRUCTION_ID ");
        sql.append(" LEFT JOIN CONSTRUCTION cons on cons.CONSTRUCTION_ID = cntWork.CONSTRUCTION_ID ");
        sql.append(" LEFT JOIN CAT_STATION cs ON cons.CAT_STATION_ID =cs.CAT_STATION_ID ");
        sql.append(" LEFT JOIN cat_province province ON province.cat_province_id   = cs.cat_province_id  ");
        sql.append(
                " where consTask.DETAIL_MONTH_PLAN_ID = :id and consTask.type = 3 and consTask.level_id = 4 group by province.code,cntWork.code,consTask.description");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("cntContract", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.setParameter("id", detailMonthPlanId);
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
        return query.list();
    }

    public List<ConstructionTaskDetailDTO> getPl1ForExportExcel(Long detailMonthPlanId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                " SELECT province.code catProvinceCode,  cs.code catStationCode,  cons.Code constructionCode,  cntWork.code cntContract, ");
        sql.append(
                " consTask.task_name workItemName,  consTask.quantity quantity,  consTask.source_type sourceType,  consTask.deploy_type deployType, ");
        sql.append(
                " captain.full_Name supervisorName,  direct.full_name directorName,  perform.Full_name performerName,  consTask.END_DATE endDate, ");
        sql.append(
                " consTask.START_DATE startDate,  consTask.DESCRIPTION description FROM CONSTRUCTION_TASK consTask ");
        sql.append(" left join SYS_USER captain on captain.SYS_USER_ID = consTask.SUPERVISOR_ID ");
        sql.append(" left join SYS_USER direct on direct.SYS_USER_ID = consTask.DIRECTOR_ID ");
        sql.append(" left join SYS_USER perform on perform.SYS_USER_ID = consTask.PERFORMER_ID ");
        sql.append(
                " LEFT JOIN (select distinct cnt.code, cntWork.CONSTRUCTION_ID from CNT_CONSTR_WORK_ITEM_TASK cntWork inner JOIN CNT_CONTRACT cnt ON cnt.CNT_CONTRACT_ID =cntWork.CNT_CONTRACT_ID AND cnt.CONTRACT_TYPE               = 0 and cnt.status!=0) cntWork ON cntWork.CONSTRUCTION_ID= consTask.CONSTRUCTION_ID ");
        sql.append(
                " LEFT JOIN CONSTRUCTION cons ON cons.CONSTRUCTION_ID = consTask.CONSTRUCTION_ID LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID LEFT JOIN cat_province province ON province.cat_province_id = cs.cat_province_id ");
        sql.append(
                " WHERE consTask.DETAIL_MONTH_PLAN_ID = :id AND consTask.type                   = 1 and consTask.level_id = 3 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("cntContract", new StringType());
        query.addScalar("workItemName", new StringType());
        query.addScalar("sourceType", new StringType());
        query.addScalar("deployType", new StringType());
        query.addScalar("supervisorName", new StringType());
        query.addScalar("directorName", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("startDate", new DateType());
        query.addScalar("endDate", new DateType());
        query.setParameter("id", detailMonthPlanId);
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
        return query.list();
    }

    public List<DmpnOrderDTO> getPl4ExcelList(Long detailMonthPlanId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                "SELECT goods_code goodsCode, goods_name goodsName, unit_name unitName, quantity quantity FROM dmpn_order where DETAIL_MONTH_PLAN_ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("goodsCode", new StringType());
        query.addScalar("goodsName", new StringType());
        query.addScalar("unitName", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.setParameter("id", detailMonthPlanId);
        query.setResultTransformer(Transformers.aliasToBean(DmpnOrderDTO.class));
        return query.list();
    }

    public List<ConstructionTaskDetailDTO> getPl6ForExportExcel(Long detailMonthPlanId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                "SELECT consTask.task_name taskName,  per.full_name performerName,  TO_CHAR(consTask.start_date,'dd/MM/yyyy') startDateStr, ");
        sql.append("  TO_CHAR(consTask.end_date,'dd/MM/yyyy') endDateStr,  consTask.DESCRIPTION description ");
        sql.append(
                " FROM construction_task consTask left join sys_user per on per.SYS_USER_ID = consTask.PERFORMER_ID ");
        sql.append(" where consTask.DETAIL_MONTH_PLAN_ID = :id and consTask.type = 6 and consTask.level_id = 4");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("taskName", new StringType());
        query.addScalar("performerName", new StringType());
        query.addScalar("startDateStr", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("endDateStr", new StringType());
        query.setParameter("id", detailMonthPlanId);
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
        return query.list();
    }

    public List<DetailMonthPlaningDTO> exportDetailMonthPlan(DetailMonthPlanSimpleDTO obj, List<String> groupIdList) {
        StringBuilder sql = new StringBuilder("SELECT DMP.DETAIL_MONTH_PLAN_ID detailMonthPlanId," + " DMP.YEAR year ,"
                + " DMP.sys_group_id sysGroupId ," + " DMP.MONTH month ," + " DMP.CREATED_DATE createdDate ,"
                + " DMP.Created_user_id createdUserId, " + " DMP.Created_group_id createdGroupId ," + " DMP.CODE code ,"
                + " DMP.NAME name ," + " DMP.Sign_state signState ," + " DMP.STATUS status , "
                + " DMP.DESCRIPTION description ," + " SYSG.NAME sysName, " + " SYSG.Code sysGroupCode "
                + " FROM DETAIL_MONTH_PLAN DMP LEFT JOIN ctct_cat_owner.sys_group SYSG ON DMP.sys_group_id=SYSG.sys_group_id"
                + " WHERE 1=1 ");
        /**Hoangnh start 11072019 -- Type=1 ngoai OS**/
        sql.append("AND DMP.TYPE =1 ");
        /**Hoangnh end 11072019 -- Type=1 ngoai OS**/
        
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(DMP.CODE) LIKE upper(:keySearch) OR  upper(DMP.YEAR) LIKE upper(:keySearch) OR upper(DMP.NAME) LIKE upper(:keySearch) escape '&')");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(" AND DMP.STATUS = :status");

        }
        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            sql.append(" AND DMP.Sign_state  in :signStateList");
        }
        if (obj.getMonthList() != null && !obj.getMonthList().isEmpty()) {
            sql.append(" AND DMP.MONTH  in :monthList");
        }
        if (obj.getYearList() != null && !obj.getYearList().isEmpty()) {
            sql.append(" AND DMP.YEAR  in :yearList");
        }

        if (obj.getSysGroupId() != null) {
            sql.append(" AND SYSG.SYS_GROUP_ID  = :sysGroupId");
        }
        
        if(groupIdList!=null && groupIdList.size()>0) {
        	sql.append(" AND SYSG.SYS_GROUP_ID  in (:groupIdList)");
        }
        
        sql.append(" ORDER BY DMP.DETAIL_MONTH_PLAN_ID DESC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }

        if (StringUtils.isNotEmpty(obj.getStatus()) && !"2".equals(obj.getStatus())) {
            query.setParameter("status", obj.getStatus());
            queryCount.setParameter("status", obj.getStatus());
        }

        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            query.setParameterList("signStateList", obj.getSignStateList());
            queryCount.setParameterList("signStateList", obj.getSignStateList());
        }
        if (obj.getMonthList() != null && !obj.getMonthList().isEmpty()) {
            query.setParameterList("monthList", obj.getMonthList());
            queryCount.setParameterList("monthList", obj.getMonthList());
        }
        if (obj.getYearList() != null && !obj.getYearList().isEmpty()) {
            query.setParameterList("yearList", obj.getYearList());
            queryCount.setParameterList("yearList", obj.getYearList());
        }
        if (obj.getSysGroupId() != null) {
            query.setParameter("sysGroupId", obj.getSysGroupId());
            queryCount.setParameter("sysGroupId", obj.getSysGroupId());
        }

        if(groupIdList!=null && groupIdList.size()>0) {
        	query.setParameterList("groupIdList", groupIdList);
            queryCount.setParameterList("groupIdList", groupIdList);
        }
        
        query.addScalar("detailMonthPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("month", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("sysName", new StringType());
        query.addScalar("sysGroupCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlaningDTO.class));
        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        query.setMaxResults(obj.getPageSize().intValue());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public List<ConstructionTaskDetailDTO> getPl235ForExportExcel(Long detailMonthPlanId, Long type) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                " SELECT province.code catProvinceCode,  cs.code catStationCode,  cons.Code constructionCode,  cntWork.code cntContract, ");
        sql.append(
                " consTask.task_name workItemName,  consTask.quantity quantity, consTask.vat vat, consTask.source_type sourceType,  consTask.deploy_type deployType, ");
        sql.append(
                " captain.full_Name supervisorName,  direct.full_name directorName,  perform.Full_name performerName,  consTask.END_DATE endDate, ");
        sql.append(
                " consTask.START_DATE startDate,  consTask.DESCRIPTION description,catType.CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId, catType.name catConstructionTypeName,consTask.CONSTRUCTION_ID constructionId "
                        + "FROM CONSTRUCTION_TASK consTask ");
        sql.append(" left join SYS_USER captain on captain.SYS_USER_ID = consTask.SUPERVISOR_ID ");
        sql.append(" left join SYS_USER direct on direct.SYS_USER_ID = consTask.DIRECTOR_ID ");
        sql.append(" left join SYS_USER perform on perform.SYS_USER_ID = consTask.PERFORMER_ID ");
        sql.append(
                " LEFT JOIN (select distinct cnt.code, cntWork.CONSTRUCTION_ID from CNT_CONSTR_WORK_ITEM_TASK cntWork inner JOIN CNT_CONTRACT cnt ON cnt.CNT_CONTRACT_ID =cntWork.CNT_CONTRACT_ID AND cnt.CONTRACT_TYPE               = 0 and cnt.status!=0) cntWork ON cntWork.CONSTRUCTION_ID= consTask.CONSTRUCTION_ID ");
        sql.append(
                " LEFT JOIN CONSTRUCTION cons ON cons.CONSTRUCTION_ID = consTask.CONSTRUCTION_ID LEFT JOIN CAT_STATION cs ");
        sql.append(
                " ON cons.CAT_STATION_ID =cs.CAT_STATION_ID LEFT JOIN cat_province province ON province.cat_province_id = cs.cat_province_id ");
        sql.append(
                " LEFT JOIN CAT_CONSTRUCTION_TYPE catType on catType.CAT_CONSTRUCTION_TYPE_ID = cons.CAT_CONSTRUCTION_TYPE_ID ");
        sql.append(" WHERE consTask.DETAIL_MONTH_PLAN_ID = :id AND consTask.type                   = :type ");
        if (type != 5)
            sql.append(" and consTask.level_id = 4 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("cntContract", new StringType());
        query.addScalar("workItemName", new StringType());
        query.addScalar("sourceType", new StringType());
        query.addScalar("deployType", new StringType());
        query.addScalar("supervisorName", new StringType());
        query.addScalar("directorName", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("vat", new DoubleType());
        query.addScalar("startDate", new DateType());
        query.addScalar("endDate", new DateType());
        query.addScalar("catConstructionTypeName", new StringType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("constructionId", new LongType());
        query.setParameter("id", detailMonthPlanId);
        query.setParameter("type", type);
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
        return query.list();
    }

    public List<WorkItemDetailDTO> getWorkItemDetailByConstructionId(Long constructionId) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                "SELECT   wi.CODE code,wi.NAME name ,catWorkType.name catWorkItemTypeName, wi.QUANTITY/1000000 quantity"
                        + " FROM WORK_ITEM wi left join CAT_WORK_ITEM_TYPE catWorkType on catWorkType.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID "
                        + "WHERE  wi.CONSTRUCTION_ID = :constructionId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("constructionId", constructionId);
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("catWorkItemTypeName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
        return query.list();
    }

    public List<CatCommonDTO> getListWorkItemTypeByName(String name) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder("Select catWorkType.name name from CAT_WORK_ITEM_TYPE catWorkType ");
        sql.append(
                " LEFT JOIN CAT_CONSTRUCTION_TYPE consType on consType.CAT_CONSTRUCTION_TYPE_ID = catWorkType.CAT_CONSTRUCTION_TYPE_ID where consType.name =:name");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("name", name);
        query.addScalar("name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(CatCommonDTO.class));
        return query.list();
    }

    // chinhpxn20180723_start
    public int createSendSmsEmail(ConstructionTaskDetailDTO request, KttsUserSession user) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
        String sql = new String(" SELECT SEND_SMS_EMAIL_SEQ.nextval FROM DUAL ");
        SQLQuery query = getSession().createSQLQuery(sql);
        int sendSmsEmailId = ((BigDecimal) query.uniqueResult()).intValue();
        StringBuilder sqlSendSmsEmail = new StringBuilder("INSERT INTO SEND_SMS_EMAIL (" + "   SEND_SMS_EMAIL_ID "
                + " , TYPE" + " , RECEIVE_PHONE_NUMBER " + " , RECEIVE_EMAIL" + " , CREATED_DATE "
                + " , CREATED_USER_ID" + " , CREATED_GROUP_ID" + " , SUBJECT" + " , CONTENT,status,WORK_ITEM_ID " + " ) VALUES ( "
                + " :sendSmsEmailId" + " , 1 " + " , :phoneNumber " + " , :email" + " , :createdDate"
                + " , :createUserId" + " , :createGroupId" + " , :subject " + " , :content,0, :workItemId  " + ")");

        String nameSubject = "";
        String sqlSubject = new String(" SELECT ap.NAME from APP_PARAM ap where ap.par_type = 'SUBJECT_SMS'");
        SQLQuery querySubject = getSession().createSQLQuery(sqlSubject);
        querySubject.addScalar("name", new StringType());
        List<String> ListSubject = querySubject.list();
        if (!ListSubject.isEmpty()) {
            nameSubject = ListSubject.get(0);
        }

        String nameCatTask = "";
        String sqlGetNameCatTask = new String(
                " SELECT ap.NAME name from APP_PARAM ap where ap.par_type = 'CONTENT_SMS_RECEIVE'");
        SQLQuery queryGetNameCatTask = getSession().createSQLQuery(sqlGetNameCatTask);
        queryGetNameCatTask.addScalar("name", new StringType());
        List<String> nameListCatTask = queryGetNameCatTask.list();
        if (!nameListCatTask.isEmpty()) {
            nameCatTask = nameListCatTask.get(0);
        }

        StringBuilder strContent = new StringBuilder(nameCatTask);
        int i = strContent.indexOf("X");
        String name = "";
        if (request.getType().equals("1")) {
            name = request.getTaskCount() + " hạng mục";
        } else if (request.getType().equals("2")) {
            name = request.getTaskCount() + " việc làm HSHC";
        } else if (request.getType().equals("3")) {
            name = request.getTaskCount() + " việc làm lên doanh thu";
        } else if (request.getType().equals("6")) {
            name = request.getTaskCount() + " công việc khác";
        }

        strContent.replace(i, i + 1, name);

        String email = "";
        String phoneNumber = "";
        SQLQuery querySms = getSession().createSQLQuery(sqlSendSmsEmail.toString());
        String sqlUser = new String(
                "SELECT EMAIL email, PHONE_NUMBER mobile FROM SYS_USER WHERE SYS_USER_ID = :sysUserId ");
        SQLQuery queryGetSysUser = getSession().createSQLQuery(sqlUser);
        queryGetSysUser.addScalar("email", new StringType());
        queryGetSysUser.addScalar("mobile", new StringType());
        queryGetSysUser.setParameter("sysUserId", request.getPerformerId());
        queryGetSysUser.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
        SysUserDTO userDTO = (SysUserDTO) queryGetSysUser.uniqueResult();

        email = userDTO.getEmail();
        phoneNumber = userDTO.getMobile();

        querySms.setParameter("phoneNumber", phoneNumber);
        querySms.setParameter("email", email);
        querySms.setParameter("createUserId", user.getVpsUserInfo().getSysUserId());
        querySms.setParameter("createGroupId", user.getVpsUserInfo().getSysGroupId());
        querySms.setParameter("sendSmsEmailId", sendSmsEmailId);
        querySms.setParameter("createdDate", new Date());
        querySms.setParameter("content", strContent.toString());
        querySms.setParameter("subject", nameSubject);
//      hoanm1_20181022_start
      if(request.getWorkItemId() !=null){
      	querySms.setParameter("workItemId", request.getWorkItemId());
      }else{
      	querySms.setParameter("workItemId", 0L);
      }
//      hoanm1_20181022_end
        return querySms.executeUpdate();
    }
    // chinhpxn20180723_end
    
    public void getUserForMap(Map<String, SysUserDTO> mapByCode, Map<String, SysUserDTO> mapByEmail) {
    	try{
	        StringBuilder sql = new StringBuilder(
	                "SELECT SYS_USER_ID userId, FULL_NAME fullName, login_name loginName, (REPLACE(EMAIL,'@viettel.com.vn','')) email " 
	                		+ " FROM SYS_USER "
	                        + " WHERE status = 1 ");
	        SQLQuery query = getSession().createSQLQuery(sql.toString());
	        query.addScalar("userId", new LongType());
	        query.addScalar("fullName", new StringType());
	        query.addScalar("loginName", new StringType());
	        query.addScalar("email", new StringType());
	        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
	        List<SysUserDTO> listUser = query.list();
	        for(SysUserDTO obj : listUser){
	        	mapByCode.put(obj.getLoginName().toUpperCase(), obj);
	        	if(obj.getEmail() != null && !obj.getEmail().isEmpty())
	        		mapByEmail.put(obj.getEmail().toUpperCase(), obj);
	        }
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }
    //tanqn start 20181108
    public void removeRow(Long constructionTaskId) {
        StringBuilder sql = new StringBuilder("delete from construction_task where 1=1 AND construction_task_id=:constructionTaskId or parent_id= :constructionTaskId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if(constructionTaskId != null){
        	query.setParameter("constructionTaskId", constructionTaskId);
        }
        query.executeUpdate();

    }
    //tanqn end 20181108
    
    //hoangnh 201218 start
    public WorkItemDTO getWiType(){
    	StringBuilder sql = new StringBuilder("SELECT CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId FROM CAT_WORK_ITEM_TYPE WHERE CODE='BTS_KHONGTHUONGXUYEN' ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("catWorkItemGroupId", new LongType()); //HuyPQ-20190523-add
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	return (WorkItemDTO) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<CNTContractDTO> getKeyValueTask(List<String> listConstrExcel){
    	StringBuilder sql = new StringBuilder("select  b.code||'+'||c.code code, a.CNT_CONSTR_WORK_ITEM_TASK_id constructionId "
    			+ "from CNT_CONSTR_WORK_ITEM_TASK a,construction b,cnt_contract c "
    			+ "where a.construction_id=b.construction_id and a.cnt_contract_id=c.cnt_contract_id and c.contract_type in (0,9) "
    			+ "and a.status !=0 and c.status !=0 and b.status !=0 ");
    	if(listConstrExcel!=null && listConstrExcel.size()>0) {
    		sql.append(" and UPPER(b.code) in (:listConstrExcel) ");
    	}
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("code", new StringType());
    	if(listConstrExcel!=null && listConstrExcel.size()>0) {
    		query.setParameterList("listConstrExcel", listConstrExcel);
    	}
    	query.setResultTransformer(Transformers.aliasToBean(CNTContractDTO.class));
    	return query.list();
    }
    
    //tatph - start - 15112019
    @SuppressWarnings("unchecked")
   	public List<ConstructionDTO> getKeyProjectEstimates(List<String > list) {
    	StringBuilder sql = new StringBuilder("select CONSTRUCTION_CODE constructionCode , PROJECT_ESTIMATES_ID projectEstimatesId , PROJECT_CODE projectCode from ctct_ims_owner.PROJECT_ESTIMATES  ");
    	if(list != null && !list.isEmpty()) {
    		sql.append("where upper(PROJECT_CODE) in :list ");
    	}
    
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("projectEstimatesId", new LongType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("projectCode", new StringType());
    	if(list != null && !list.isEmpty()) {
    		query.setParameterList("list", list);
    	}
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));
    	return query.list();
       }
    //tatph - end - 15112019
	public WorkItemDTO getWiById(String code){
    	StringBuilder sql = new StringBuilder("SELECT WORK_ITEM_ID workItemId,"
    			+ "CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId,"
    			+ "CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId," //HuyPQ-20190523-add
    			+ "NAME name," 	//Huypq-20200114-add
    			+ "CODE code FROM WORK_ITEM WHERE 1=1 ");
    	if(code != null){
    		sql.append("AND CODE =:code ");
    	}
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("code", code);
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("code", new StringType());
    	query.addScalar("catWorkItemGroupId", new LongType()); //HuyPQ-20190523-add
    	query.addScalar("name", new StringType());  //Huypq-20200114-add
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	return (WorkItemDTO) query.uniqueResult();
    }
	
	public void updateRegistry(Long detailMonthPlanId) {
        StringBuilder sql = new StringBuilder(
                "UPDATE DETAIL_MONTH_PLAN set SIGN_STATE = '3'  where DETAIL_MONTH_PLAN_ID = :detailMonthPlanId");
        StringBuilder sql2 = new StringBuilder(
                "UPDATE REVOKE_CASH_MONTH_PLAN set SIGN_STATE = '3'  where DETAIL_MONTH_PLAN_ID = :detailMonthPlanId");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery query2 = getSession().createSQLQuery(sql2.toString());
        
        query.setParameter("detailMonthPlanId", detailMonthPlanId);
        query2.setParameter("detailMonthPlanId", detailMonthPlanId);
        
        query.executeUpdate();
        query2.executeUpdate();
    }
    //hoangnh 201218 end
	
	//HuyPQ-20190527-start
	public DetailMonthPlanSimpleDTO getDetailMonthPlanIdByTime(Long month, Long year, Long sysGroupId) {
		String sql = new String("SELECT dtmp.Detail_month_plan_id detailMonthPlanId FROM detail_month_plan dtmp "
                + "where dtmp.Month = :month and dtmp.Year = :year and sign_state = 3 and status = 1 and SYS_GROUP_ID = :sysGroupId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("detailMonthPlanId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlanSimpleDTO.class));
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("sysGroupId", sysGroupId);
		@SuppressWarnings("unchecked")
        List<DetailMonthPlanSimpleDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;

	}
	
	//Huy-end
	
	 //Huypq-20190627-start
    @SuppressWarnings("unchecked")
	public List<DetailMonthPlanDTO> checkTaskConstruction(Long id){
    	StringBuilder sql = new StringBuilder("SELECT a.DETAIL_MONTH_PLAN_ID detailMonthPlanId " + 
    			"  FROM DETAIL_MONTH_PLAN a, " + 
    			"  CONSTRUCTION_TASK b " + 
    			"  WHERE a.DETAIL_MONTH_PLAN_id=b.DETAIL_MONTH_PLAN_id " + 
    			"  AND a.sign_state            =3 " + 
    			"  AND a.status                =1 " + 
//    			"  AND b.LEVEL_ID              =4 " + 
//    			"  AND b.status                >1 " + 
    			"  AND b.DETAIL_MONTH_PLAN_ID  = :id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("detailMonthPlanId", new LongType());
    	query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlanDTO.class));
    	query.setParameter("id", id);
    	return query.list();
    }
    //Huy-end
    
    public List<ConstructionDTO> getConstructionById(Long id) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.name name ");
		stringBuilder.append(",T1.code code ");
		stringBuilder.append(",T1.CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId ");
		stringBuilder.append("FROM construction T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS != 0 ");
		stringBuilder.append("and T1.CONSTRUCTION_ID = :constructionId");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("catConstructionTypeId", new LongType());
		query.setResultTransformer(Transformers
				.aliasToBean(ConstructionDTO.class));
		query.setParameter("constructionId", id);
		return query.list();
	}
    
    //tatph -start - 20112019
    public Map<String, SysUserCOMSDTO> getSysUserLoginNameExcel(List<String> list){
    	try{
    	 StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID sysUserId, FULL_NAME fullName,upper(LOGIN_NAME) loginName FROM SYS_USER where TYPE_USER is null ");
    	 if(list != null && !list.isEmpty()) {
    		 sql.append(" and LOGIN_NAME in :list ");
    	 }
    	
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(SysUserCOMSDTO.class));
			query.addScalar("sysUserId", new LongType());
		    query.addScalar("fullName", new StringType());
		    query.addScalar("loginName", new StringType());
		    if(list != null && !list.isEmpty()) {
		    	  query.setParameterList("list",  list);
		    }
		  
			List<SysUserCOMSDTO> lstUser = query.list();
			Map<String, SysUserCOMSDTO> sysUserMap = new HashMap<String, SysUserCOMSDTO>();
			for (SysUserCOMSDTO obj : lstUser) {
				sysUserMap.put(obj.getLoginName().toUpperCase().trim(), obj);
			}
			return sysUserMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public Map<String, SysUserCOMSDTO> getSysUserEmailExcel(List<String> list){
    	try{
    	 StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID sysUserId, FULL_NAME fullName,upper(REPLACE(EMAIL,'@viettel.com.vn',''))loginName FROM SYS_USER where TYPE_USER is null ");
    	 if(list != null && !list.isEmpty()) {
    		 sql.append(" and upper(REPLACE(EMAIL,'@viettel.com.vn','')) in :list ");
    	 }
    	
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(SysUserCOMSDTO.class));
			query.addScalar("sysUserId", new LongType());
		    query.addScalar("fullName", new StringType());
		    query.addScalar("loginName", new StringType());
		    if(list != null && !list.isEmpty()) {
		    	 query.setParameterList("list", list);
		    }
			List<SysUserCOMSDTO> lstUser = query.list();
			Map<String, SysUserCOMSDTO> sysUserMap = new HashMap<String, SysUserCOMSDTO>();
			for (SysUserCOMSDTO obj : lstUser) {
				sysUserMap.put(obj.getLoginName().trim().toUpperCase(), obj);
			}
			return sysUserMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    //Huypq-20200221-start
    public List<SysUserCOMSDTO> getListSysUserEmailExcel(List<String> list){
    	 StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID sysUserId, FULL_NAME fullName,upper(REPLACE(EMAIL,'@viettel.com.vn','')) email, upper(LOGIN_NAME) loginName FROM SYS_USER where TYPE_USER is null and status!=0 ");
    	 if(list != null && !list.isEmpty()) {
    		 sql.append(" and (upper(REPLACE(EMAIL,'@viettel.com.vn','')) in (:list) OR upper(LOGIN_NAME) in (:list)) ");
    	 }
    	
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(SysUserCOMSDTO.class));
			query.addScalar("sysUserId", new LongType());
		    query.addScalar("fullName", new StringType());
		    query.addScalar("loginName", new StringType());
		    query.addScalar("email", new StringType());
		    if(list != null && !list.isEmpty()) {
		    	 query.setParameterList("list", list);
		    }
			@SuppressWarnings("unchecked")
			List<SysUserCOMSDTO> lstUser = query.list();
//			Map<String, SysUserCOMSDTO> sysUserMap = new HashMap<String, SysUserCOMSDTO>();
//			for (SysUserCOMSDTO obj : lstUser) {
//				sysUserMap.put(obj.getLoginName().trim().toUpperCase(), obj);
//			}
			return lstUser;
    }
    //huy-end
    
    public List<WorkItemDTO> getWorkItemId(String constrCode ,String contractOutCode){
    	try{
    	 StringBuilder sql = new StringBuilder("SELECT CONSTRUCTION_CODE constructionCode, PROJECT_NAME projectName , WORK_ITEM_ID workItemId from ctct_ims_owner.CONSTR_WI_TASK_PROJECT_HTCT ");
    	 	if(!"".equals(constrCode) && !"".equals(contractOutCode)) {
    	 		sql.append(" where CONSTRUCTION_CODE = :constructionCode  and PROJECT_NAME = :projectName ");
    	 	}
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(WorkItemDTO.class));
			query.addScalar("workItemId", new LongType());
		    query.addScalar("projectName", new StringType());
		    query.addScalar("constructionCode", new StringType());
		    if(!"".equals(constrCode) && !"".equals(contractOutCode)) {
		    	query.setParameter("constructionCode", constrCode);
		    	query.setParameter("projectName", contractOutCode);
		    }
		    
			List<WorkItemDTO> lstUser = query.list();
			return lstUser;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public void removeConstructionTaskByDMPId(Long detailMonthPlanId) {
        StringBuilder sql = new StringBuilder(
                "UPDATE CONSTRUCTION_TASK set status = 0  where DETAIL_MONTH_PLAN_ID = :detailMonthPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("detailMonthPlanId", detailMonthPlanId);
        query.executeUpdate();

    }
    public List<RevokeCashMonthPlanDTO> doSearchManageValue(RevokeCashMonthPlanDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder("SELECT T1.REVOKE_CASH_MONTH_PLAN_ID revokeCashMonthPlanId," )
        		.append(" T1.CNT_CONTRACT_CODE cntContractCode ,")
        		.append(" T1.BILL_CODE billCode ,")
        		.append(" T1.CREATED_BILL_DATE createdBillDate ,")
        		.append(" nvl(T1.BILL_VALUE,0) billValue ,")
        		.append(" T1.AREA_CODE areaCode ,")
        		.append(" T1.PROVINCE_CODE provinceCode ,")
        		.append(" T1.PERFORMER_ID performerId ,")
        		.append(" SU.FULL_NAME  performerName,")
        		.append(" T1.START_DATE startDate ,")
        		.append(" T1.END_DATE endDate ,")
        		.append(" T1.DESCRIPTION description ,")
        		.append(" T1.CREATED_USER_ID createdUserId ,")
        		.append(" T1.CREATED_DATE createdDate ,")
        		.append(" T1.UPDATED_USER_ID updatedUserId ,")
        		.append(" T1.UPDATED_DATE updatedDate ,")
        		.append(" T1.SIGN_STATE signState ,")
        		.append(" T1.DETAIL_MONTH_PLAN_ID detailMonthPlanId ,")
        		.append(" T1.STATUS status ,")
        		.append(" T1.CONSTRUCTION_ID constructionId ,")
        		.append(" T1.CONSTRUCTION_CODE constructionCode ,")
        		.append(" T1.CAT_STATION_ID catStationId ,")
        		.append(" T1.CAT_STATION_CODE catStationCode, ")
        		.append(" T1.REASON_REJECT reasonReject, ")
        		.append(" T1.SYS_GROUP_ID sysGroupId ")
        		.append(" FROM REVOKE_CASH_MONTH_PLAN T1 ")
        		.append(" LEFT JOIN SYS_USER SU ON TO_CHAR(T1.PERFORMER_ID) = SU.SYS_USER_ID")
        		.append(" where 1 = 1 and T1.STATUS != 0 AND T1.SIGN_STATE = 3");
        		
        if(groupIdList.size() > 0) {
        	sql.append("  and T1.PROVINCE_CODE in (select code from CAT_PROVINCE where CAT_PROVINCE.CAT_PROVINCE_ID in (:groupIdList )) ");
        }
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(T1.CNT_CONTRACT_CODE) LIKE upper(:keySearch) OR  upper(T1.BILL_CODE) LIKE upper(:keySearch) OR upper(T1.CONSTRUCTION_CODE) LIKE upper(:keySearch) escape '&')");
        }
//        if (StringUtils.isNotEmpty(obj.getStatus())) {
//            sql.append(" AND DMP.STATUS = :status");
//
//        }
        sql.append(" ORDER BY T1.REVOKE_CASH_MONTH_PLAN_ID DESC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        
        if (groupIdList.size() > 0) {
            query.setParameterList("groupIdList", groupIdList);
            queryCount.setParameterList("groupIdList", groupIdList);
        }
        query.addScalar("revokeCashMonthPlanId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("billCode", new StringType());
        query.addScalar("createdBillDate", new DateType());
        query.addScalar("billValue", new DoubleType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("performerId", new LongType());
        query.addScalar("performerName", new StringType());
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
        
        query.addScalar("reasonReject", new StringType());
        query.addScalar("sysGroupId", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(RevokeCashMonthPlanDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    //tatph -end - 20112019
    
    public List<WorkItemDTO> getWiByIdNew(String code){
    	StringBuilder sql = new StringBuilder("SELECT WORK_ITEM_ID workItemId,"
    			+ "CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId,"
    			+ "CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId," //HuyPQ-20190523-add
    			+ "NAME name," 	//Huypq-20200114-add
    			+ "CODE code FROM WORK_ITEM WHERE 1=1 ");
    	if(code != null){
    		sql.append("AND CODE =:code ");
    	}
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("code", code);
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("code", new StringType());
    	query.addScalar("catWorkItemGroupId", new LongType()); //HuyPQ-20190523-add
    	query.addScalar("name", new StringType());  //Huypq-20200114-add
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	return query.list();
    }
    
    //Huypq-20200121-start
    public List<WorkItemDTO> getWorkItemByName(String consCode, String name){
    	StringBuilder sql = new StringBuilder(" select name name from work_item where UPPER(name)=:name and status != 0 and construction_id = (select construction_id from construction where UPPER(code)=:consCode and status != 0) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("name", new StringType());
    	query.setParameter("name", name);
    	query.setParameter("consCode", consCode);
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	return query.list();
    }
    //Huy-end
    
    //Huypq-20200513-start
    public List<ConstructionTaskDetailDTO> getStationByLstCode(List<String> lstCode){
    	StringBuilder sql = new StringBuilder(" SELECT cs.CAT_STATION_ID catStationId, " + 
    			"  cs.CODE catStationCode, " +
    			"  cp.code catProvinceCode " + 
    			"FROM CAT_STATION cs " + 
    			"LEFT JOIN CAT_PROVINCE cp " + 
    			"ON cp.CAT_PROVINCE_ID = cs.CAT_PROVINCE_ID "
    			+ "where upper(cs.code) in (:lstCode) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catStationId", new LongType());
    	query.addScalar("catStationCode", new StringType());
    	query.addScalar("catProvinceCode", new StringType());
    	query.setParameterList("lstCode", lstCode);
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDetailDTO.class));
    	return query.list();
    }
    
    public List<SysUserDTO> getUserByLstCode(List<String> lstCode){
    	StringBuilder sql = new StringBuilder(" SELECT SYS_USER_ID sysUserId, " + 
    			"  EMPLOYEE_CODE employeeCode, " + 
    			"  FULL_NAME fullName, " + 
    			"  REPLACE(EMAIL,'@viettel.com.vn','') email " + 
    			"  FROM SYS_USER where upper(EMPLOYEE_CODE) in (:lstCode) or upper(REPLACE(EMAIL,'@viettel.com.vn','')) in (:lstCode) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("sysUserId", new LongType());
    	query.addScalar("employeeCode", new StringType());
    	query.addScalar("fullName", new StringType());
    	query.addScalar("email", new StringType());
    	query.setParameterList("lstCode", lstCode);
    	query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
    	return query.list();
    }
    
    public void deleteRentGround(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE "
    			+ "from CONSTRUCTION_TASK "
    			+ "where DETAIL_MONTH_PLAN_ID=:id and type= 4");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public List<ConstructionTaskDTO> checkDupStationByMonthPlanId(Long monthPlanId, Long sysGroupId, List<String> lstStation){
    	StringBuilder sql = new StringBuilder("SELECT ct.STATION_CODE stationCode " + 
    			" FROM CONSTRUCTION_TASK ct " + 
    			" left join DETAIL_MONTH_PLAN dmp " + 
    			" on ct.DETAIL_MONTH_PLAN_ID = dmp.DETAIL_MONTH_PLAN_ID " + 
    			" WHERE dmp.STATUS!=0 " + 
    			" and dmp.SIGN_STATE =3 " + 
    			" and ct.type              =4 " + 
    			" and ct.SYS_GROUP_ID=:sysGroupId " + 
    			" AND ct.DETAIL_MONTH_PLAN_ID= :monthPlanId " +
    			" AND upper(ct.STATION_CODE) in (:lstStation) "
    			);
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("stationCode", new StringType());
    	query.setParameter("sysGroupId", sysGroupId);
    	query.setParameter("monthPlanId", monthPlanId);
    	query.setParameterList("lstStation", lstStation);
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	return query.list();
    }
    //Huy-end
    
    //Huypq-06052020-start
    public List<DepartmentDTO> getSysUserByLstUser(List<String> lstUser) {
        StringBuilder sql = new StringBuilder(
                "SELECT SYS_USER_ID departmentId," 
                		+ " FULL_NAME name, "
                		+ " EMPLOYEE_CODE employeeCode, "
                		+ " REPLACE(EMAIL,'@viettel.com.vn','') email "
                		+ " FROM SYS_USER "
                        + " WHERE TYPE_USER is null and (UPPER(LOGIN_NAME) in (:lstUser) OR UPPER(REPLACE(EMAIL,'@viettel.com.vn','')) in (:lstUser)) ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameterList("lstUser", lstUser);
        query.addScalar("departmentId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(DepartmentDTO.class));
        List<DepartmentDTO> list = query.list();
        return list;
    }
    
    public List<ConstructionDetailDTO> getCntContractByLstContractCode(List<String> lstContractCode){
    	StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT cc.code cntContractCode, "
    			+ " cons.code constructionCode "
                + " from CNT_CONTRACT cc"
                + " left join CNT_CONSTR_WORK_ITEM_TASK cwit on cc.cnt_contract_id = cwit.cnt_contract_id"
                + " left join CONSTRUCTION cons on cons.construction_id = cwit.construction_id"
                + " where cc.status!=0"
                + " and cwit.status!=0 AND cons.STATUS != 0 "
                + " and upper(cc.code) in (:lstContractCode) ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        
        query.setParameterList("lstContractCode", lstContractCode);
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ConstructionDetailDTO.class));
        List<ConstructionDetailDTO> list = query.list();
        return list;
    }
    
    public List<CatStationDTO> getCheckCompleteByListStationCode(List<String> lstCode){
    	StringBuilder sql = new StringBuilder("select code code from cat_station where upper(code) in (:lstCode) and COMPLETE_STATUS=1 ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("code", new StringType());
    	query.setParameterList("lstCode", lstCode);
    	query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
    	return query.list();
    }
    //Huy-end
    
    //Huypq-29062020-start
    public List<CntContractDTO> getCntContractIdByLstContractCode(List<String> lstContractCode){
    	StringBuilder sql = new StringBuilder(
                "select CNT_CONTRACT_ID cntContractId, CODE code from CNT_CONTRACT where STATUS!=0 and contract_type=9 and upper(code) in (:lstContractCode) ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        
        query.setParameterList("lstContractCode", lstContractCode);
        query.addScalar("cntContractId", new LongType());
        query.addScalar("code", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));
        List<CntContractDTO> list = query.list();
        return list;
    }
    
    public boolean checkMonthYearSysTTXD(Long month, Long year, Long sysGroupId, Long detailMonthId) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DETAIL_MONTH_PLAN_ID) FROM DETAIL_MONTH_PLAN where 1=1 and type=2 and status = 1 and month=:month and year=:year and sys_group_id = :sysGroupId");
        if (detailMonthId != null) {
            sql.append(" AND DETAIL_MONTH_PLAN_ID != :detailMonthId ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("sysGroupId", sysGroupId);
        if (detailMonthId != null) {
            query.setParameter("detailMonthId", detailMonthId);
        }
        return ((BigDecimal) query.uniqueResult()).intValue() > 0;
    }
    
    public void deleteConstructionTaskByType(Long id, String type) {
    	StringBuilder sql = new StringBuilder("DELETE "
    			+ "from CONSTRUCTION_TASK "
    			+ "where DETAIL_MONTH_PLAN_ID=:id and type=:type");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.setParameter("type", type);
    	query.executeUpdate();
    }
    
    public void deleteDetailMonthQuantityByDetailMonthPlanId(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE "
    			+ "from DETAIL_MONTH_QUANTITY "
    			+ "where DETAIL_MONTH_PLAN_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    //huy-end
}
