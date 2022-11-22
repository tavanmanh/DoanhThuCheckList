package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.viettel.cat.utils.ValidateUtils;
import com.viettel.coms.bo.RpQuantityBO;
import com.viettel.coms.dto.RpConstructionDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.dto.couponExportDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("rpQuantityDAO")
public class RpQuantityDAO extends BaseFWDAOImpl<RpQuantityBO, Long>  {

	@Autowired
    private QuantityConstructionDAO quantityConstructionDAO;
	
	public RpQuantityDAO() {
        this.model = new RpQuantityBO();
    }

    public RpQuantityDAO(Session session) {
        this.session = session;
    }
	
	public List<WorkItemDetailDTO> doSearchQuantity(WorkItemDetailDTO obj, List<String> groupIdList) {
//       	StringBuilder sql = new StringBuilder(
////				hungnx 20180703 start
//				"select * from (" + quantityConstructionDAO.buildQuerySumByMonth(obj) + " SELECT DISTINCT "
//						+ " ctd.CREATED_DATE dateComplete," + " a.COMPLETE_DATE completeDate,"
//						+ " a.PERFORMER_ID performerId," + " sysu.FULL_NAME performerName,"
//						+ " (SELECT name FROM sys_group SYS WHERE sys.SYS_GROUP_ID=b.SYS_GROUP_ID) constructorName,b.SYS_GROUP_ID sysGroupIdSMS,"
//						+ " cat.CODE catstationCode, " + "b.CODE constructionCode, " + " a.NAME name, "
//						+ " (SELECT sum(cc.quantity) FROM CONSTRUCTION_TASK_DAILY cc where to_char(cc.created_date, 'dd/MM/yyyy') = ctd.CREATED_DATE and cc.work_item_id = ctd.work_item_id and cc.confirm = 1)/1000000 quantity,"
//						+ " (select 1 from dual) quantityByDate," + "a.STATUS status, " + " '' cntContractCode, "
//						+ " catPro.CODE catProvinceCode,  "
//						+ " a.WORK_ITEM_ID workItemId, " + "b.CONSTRUCTION_ID constructionId, "
//						+ "b.status statusConstruction, " + "b.approve_complete_value  approveCompleteValue , "
//						+ "catPro.CAT_PROVINCE_ID catProvinceId, " + "b.price price, "
//						+ " b.OBSTRUCTED_STATE obstructedState , "
//						+ "b.approve_complete_date  approveCompleteDate  "
//						+ " FROM  " + "WORK_ITEM a " + " LEFT JOIN SYS_USER sysu on sysu.SYS_USER_ID = a.PERFORMER_ID "
//						+ " JOIN CONSTRUCTION b  on A.CONSTRUCTION_ID = B.CONSTRUCTION_ID"
//						+ " JOIN tblTaskDaily ctd on CTD.WORK_ITEM_ID = A.WORK_ITEM_ID "
//						+ "LEFT JOIN CAT_STATION cat ON b.CAT_STATION_ID =cat.CAT_STATION_ID "
//						+ " left join cat_province catPro on catPro.CAT_PROVINCE_ID = cat.CAT_PROVINCE_ID " + " UNION"
//						+ " SELECT DISTINCT CASE "
//						+ " WHEN a.STATUS= 3 " + "THEN TO_CHAR(a.complete_date,'dd/MM/yyyy') " + "WHEN a.STATUS=2 "
//						+ "THEN TO_CHAR(b.complete_date,'dd/MM/yyyy') " + "END dateComplete, "
//						+ " a.COMPLETE_DATE completeDate," + " a.PERFORMER_ID performerId,"
//						+ " sysu.FULL_NAME performerName,"
//						+ "(SELECT name FROM sys_group SYS WHERE sys.SYS_GROUP_ID=b.SYS_GROUP_ID) constructorName,b.SYS_GROUP_ID sysGroupIdSMS,"
//						+ "cat.CODE catstationCode, " + "b.CODE constructionCode, " + " a.NAME name, "
//						+ "a.QUANTITY/1000000 quantity, " + " (select 0 from dual) quantityByDate,"
//						+ "a.STATUS status, " + " '' cntContractCode, " 
//						+ "catPro.CODE catProvinceCode, "
//						+ " a.WORK_ITEM_ID workItemId, " + "b.CONSTRUCTION_ID constructionId, "
//						+ "b.status statusConstruction, " + "b.approve_complete_value  approveCompleteValue , "
//						+ "catPro.CAT_PROVINCE_ID catProvinceId, "
//						+ "b.price price, "
//						+ " b.OBSTRUCTED_STATE obstructedState , "
//						+ "b.approve_complete_date  approveCompleteDate  "
//
//						+ " FROM  " + "WORK_ITEM a " + " LEFT JOIN SYS_USER sysu on sysu.SYS_USER_ID = a.PERFORMER_ID "
//						+ " JOIN CONSTRUCTION b  on A.CONSTRUCTION_ID = B.CONSTRUCTION_ID "
//						+ "LEFT JOIN CAT_STATION cat ON b.CAT_STATION_ID =cat.CAT_STATION_ID "
//						+ " left join cat_province catPro on catPro.CAT_PROVINCE_ID = cat.CAT_PROVINCE_ID "
//						+ "WHERE 1=1 " + " AND (a.STATUS          = 3 " + "OR (a.STATUS           =2 "
//						+ "AND b.STATUS           =4 " + "AND b.OBSTRUCTED_STATE =2) " + " )"
//						+ " and not exists (SELECT CTD.WORK_ITEM_ID FROM CONSTRUCTION_TASK_DAILY ctd where CTD.WORK_ITEM_ID = A.WORK_ITEM_ID)");
//		if (obj.getDateFrom() != null) {
//			sql.append(
//					"AND trunc( CASE WHEN a.STATUS= 3 THEN a.complete_date  WHEN a.STATUS=2 THEN b.complete_date END ) >= :monthYearFrom ");
//		}
//		if (obj.getDateTo() != null) {
//			sql.append(
//					"AND trunc( CASE WHEN a.STATUS= 3 THEN a.complete_date  WHEN a.STATUS=2 THEN b.complete_date END ) <= :monthYearTo ");
//		}
//        sql.append(" ) a" + " where 1=1");
		StringBuilder sql = new StringBuilder("select to_char(starting_date,'dd/MM/yyyy') dateComplete,complete_date completeDate,performer_Id performerId,performer_Name performerName,sysGroupName constructorName, "
				+ " catstationCode, constructionCode,workItemName name,quantity,status,statusConstruction,cntContractCode,catProvinceCode,workItemId,catProvinceId,constructionId, "
				+ " approveCompleteValue,approveCompleteDate,price,quantityByDate,obstructedState,SYS_GROUP_ID sysGroupIdSMS,partnerName constructorName1 from rp_quantity where 1=1 ");
		if (obj.getDateFrom() != null) {
			sql.append(" and starting_date >= :monthYearFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and starting_date <= :monthYearTo ");
		}
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(" AND (upper(cntContractCode) LIKE upper(:keySearch) OR  upper(constructionCode) LIKE upper(:keySearch) OR upper(catstationCode) LIKE upper(:keySearch) escape '&')");
        }
        if (obj.getSysGroupId() != null) {
            sql.append(" AND SYS_GROUP_ID  = :sysGroupId");
        }
        if (groupIdList != null && !groupIdList.isEmpty()) {
            sql.append(" and catProvinceId in :groupIdList ");
        }
        if (obj.getCatProvinceId() != null) {
            sql.append(" AND catProvinceId = :catProvinceId ");
        }
        sql.append(" ORDER BY starting_date DESC,sysGroupName");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        StringBuilder countDateComplete = new StringBuilder("SELECT DISTINCT dateComplete FROM (");
        countDateComplete.append(sql);
        countDateComplete.append(")");
        StringBuilder countCatstationCode = new StringBuilder("SELECT DISTINCT catstationCode FROM (");
        countCatstationCode.append(sql);
        countCatstationCode.append(")");
        SQLQuery queryStation = getSession().createSQLQuery(countCatstationCode.toString());
        StringBuilder countConstrCode = new StringBuilder("SELECT DISTINCT constructionCode FROM (");
        countConstrCode.append(sql);
        countConstrCode.append(")");
        SQLQuery queryConstr = getSession().createSQLQuery(countConstrCode.toString());
        StringBuilder sqlTotalQuantity = new StringBuilder("SELECT NVL(sum(quantity), 0) FROM (");
        sqlTotalQuantity.append(sql);
        sqlTotalQuantity.append(")");
        SQLQuery queryQuantity = getSession().createSQLQuery(sqlTotalQuantity.toString());
        SQLQuery queryDC = getSession().createSQLQuery(countDateComplete.toString());
//		hungnx 20180713 end
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (groupIdList != null && !groupIdList.isEmpty()) {
            query.setParameterList("groupIdList", groupIdList);
            queryCount.setParameterList("groupIdList", groupIdList);
            queryDC.setParameterList("groupIdList", groupIdList);
            queryStation.setParameterList("groupIdList", groupIdList);
            queryConstr.setParameterList("groupIdList", groupIdList);
            queryQuantity.setParameterList("groupIdList", groupIdList);
        }
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryDC.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryStation.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryConstr.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryQuantity.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }

        if (obj.getMonthList() != null && !obj.getMonthList().isEmpty()) {
            query.setParameterList("monthList", obj.getMonthList());
            queryCount.setParameterList("monthList", obj.getMonthList());
        }
        if (obj.getMonthYear() != null && !obj.getMonthYear().isEmpty()) {
            query.setParameter("monthYear", obj.getMonthYear());
            queryCount.setParameter("monthYear", obj.getMonthYear());
        }

        if (obj.getSysGroupId() != null) {
            query.setParameter("sysGroupId", obj.getSysGroupId());
            queryCount.setParameter("sysGroupId", obj.getSysGroupId());
            queryDC.setParameter("sysGroupId", obj.getSysGroupId());
            queryStation.setParameter("sysGroupId", obj.getSysGroupId());
            queryConstr.setParameter("sysGroupId", obj.getSysGroupId());
            queryQuantity.setParameter("sysGroupId", obj.getSysGroupId());
        }
//		hungnx 20170703 start
        if (obj.getDateFrom() != null) {
            query.setParameter("monthYearFrom", obj.getDateFrom());
            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
            queryConstr.setParameter("monthYearFrom", obj.getDateFrom());
            queryDC.setParameter("monthYearFrom", obj.getDateFrom());
            queryQuantity.setParameter("monthYearFrom", obj.getDateFrom());
            queryStation.setParameter("monthYearFrom", obj.getDateFrom());
        }
        if (obj.getDateTo() != null) {
            query.setParameter("monthYearTo", obj.getDateTo());
            queryCount.setParameter("monthYearTo", obj.getDateTo());
            queryConstr.setParameter("monthYearTo", obj.getDateTo());
            queryDC.setParameter("monthYearTo", obj.getDateTo());
            queryQuantity.setParameter("monthYearTo", obj.getDateTo());
            queryStation.setParameter("monthYearTo", obj.getDateTo());
        }
//		hungnx 20170703 end

        // tuannt_15/08/2018_start
        if (obj.getCatProvinceId() != null) {
            query.setParameter("catProvinceId", obj.getCatProvinceId());
            queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
            queryConstr.setParameter("catProvinceId", obj.getCatProvinceId());
            queryDC.setParameter("catProvinceId", obj.getCatProvinceId());
            queryQuantity.setParameter("catProvinceId", obj.getCatProvinceId());
            queryStation.setParameter("catProvinceId", obj.getCatProvinceId());
        }
        // tuannt_15/08/2018_start

        query.addScalar("dateComplete", new StringType());
        query.addScalar("completeDate", new DateType());
        query.addScalar("performerId", new LongType());
        query.addScalar("performerName", new StringType());
        query.addScalar("constructorName", new StringType());
        query.addScalar("catstationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("status", new StringType());
        query.addScalar("statusConstruction", new StringType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("workItemId", new LongType());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("constructionId", new LongType());

        query.addScalar("approveCompleteValue", new DoubleType());
        query.addScalar("approveCompleteDate", new DateType());
        query.addScalar("price", new DoubleType());
        query.addScalar("quantityByDate", new StringType());
        query.addScalar("obstructedState", new StringType());

        queryDC.addScalar("dateComplete", new StringType());
        queryStation.addScalar("catstationCode", new StringType());
        queryConstr.addScalar("constructionCode", new StringType());
//		hoanm1_20180724_start
        query.addScalar("sysGroupIdSMS", new LongType());
        query.addScalar("constructorName1", new StringType());
//		hoanm1_20180724_end
        query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

//		hungnx 20180628 start
        List<WorkItemDetailDTO> lst = query.list();

        if (lst.size() > 0) {
            int countDC = queryDC.list().size();
            int countStation = queryStation.list().size();
            int countConstr = queryConstr.list().size();
            lst.get(0).setCountDateComplete(countDC);
            lst.get(0).setCountCatstationCode(countStation);
            lst.get(0).setCountConstructionCode(countConstr);
            lst.get(0).setCountWorkItemName(((BigDecimal) queryCount.uniqueResult()).intValue());
            BigDecimal totalQuantity = (BigDecimal) queryQuantity.uniqueResult();
            lst.get(0).setTotalQuantity(totalQuantity.doubleValue());
        }
//		hungnx 20180713 end
        return lst;
    }
//	hungtd_20181217_start
	public List<RpConstructionDTO> doSearch(RpConstructionDTO obj) {
		StringBuilder sql = new StringBuilder("select T.ASSIGN_HANDOVER_ID asSignHadoverId,"
				+ " T.SYS_GROUP_NAME sysgroupname,"
				+ " T.CAT_PROVINCE_CODE Catprovincecode,"
				+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
				+ " T.CNT_CONTRACT_CODE cntContractCodeBGMB,"
				+ " to_char(T.COMPANY_ASSIGN_DATE,'dd/MM/yyyy') Companyassigndate,"
				+ " trunc(SYSDATE) - trunc(T.COMPANY_ASSIGN_DATE) outOfdate, "
				+ " case when Out_of_date_received is not null then 1 else null end outofdatereceivedBGMB,"
				+ " T.DESCRIPTION description,"
				//Huypq-20190604-start
				+ " T.CONSTRUCTION_CODE constructionCode,"
				+ " cct.name constructionTypeName "
//				hoanm1_20190806_start
				+ " ,(select REPLACE(EMAIL,'@viettel.com.vn','') from sys_user sys where sys.sys_user_id=t.Update_user_id) nguoiGiamSat "
//				hoanm1_20190806_end
				//Huy-end
				+ " from ASSIGN_HANDOVER T "
				//Huypq-20190604-start
				+ " left join CONSTRUCTION cons on T.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ " left join CTCT_CAT_OWNER.CAT_CONSTRUCTION_TYPE cct on cons.CAT_CONSTRUCTION_TYPE_ID = cct.CAT_CONSTRUCTION_TYPE_ID "
				//Huypq-end
				+ " WHERE 1=1 and Received_status = 1 and T.status !=0 ");
		
		/*hungtd_20180311_strat*/
		if (obj.getDateFrom() != null) {
			sql.append(" and trunc(T.COMPANY_ASSIGN_DATE) >= :monthYearFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and trunc(T.COMPANY_ASSIGN_DATE) <= :monthYearTo ");
		}
		/*hungtd_20180311_end*/
		if (obj.getSysGroupId() != null) {
			sql.append(" AND T.SYS_GROUP_ID  = :sysGroupId ");
		}
		if (obj.getCatProvinceId() != null) {
			sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
		}
		if (obj.getStationCode() != null) {
			sql.append(" AND (upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) OR upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) escape '&')");
		}
		if (obj.getCntContractCode() != null) {
			sql.append(" AND T.CNT_CONTRACT_CODE =:cntContractCode ");
		}
		if (obj.getReceivedStatus() != null) {
			sql.append(" AND (case when Out_of_date_received is not null then 1 else 0 end) =:receivedStatus ");
		}
		
		
		sql.append(" ORDER BY T.COMPANY_ASSIGN_DATE DESC,SYS_GROUP_NAME,CAT_PROVINCE_CODE ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		if (obj.getDateFrom() != null) {
            query.setParameter("monthYearFrom", obj.getDateFrom());
            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
            
        }
        if (obj.getDateTo() != null) {
            query.setParameter("monthYearTo", obj.getDateTo());
            queryCount.setParameter("monthYearTo", obj.getDateTo());
            
        }
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getReceivedStatus() != null) {
			query.setParameter("receivedStatus", obj.getReceivedStatus());
			queryCount.setParameter("receivedStatus", obj.getReceivedStatus());
		}
		if (obj.getCatProvinceId() != null) {
			query.setParameter("catProvinceId",obj.getCatProvinceId());
			queryCount.setParameter("catProvinceId",obj.getCatProvinceId());
		}

		if (obj.getStationCode() != null) {
			query.setParameter("stationCode", obj.getStationCode());
			queryCount.setParameter("stationCode", obj.getStationCode());
		}
		if (obj.getCntContractCode() != null) {
			query.setParameter("cntContractCode",obj.getCntContractCode());
			queryCount.setParameter("cntContractCode", obj.getCntContractCode());
		}
		
        query.addScalar("asSignHadoverId", new LongType());
		query.addScalar("sysgroupname", new StringType());
		query.addScalar("Catprovincecode", new StringType());
		query.addScalar("Catstattionhousecode", new StringType());
		query.addScalar("cntContractCodeBGMB", new StringType());
		query.addScalar("Companyassigndate", new StringType());
		query.addScalar("outOfdate", new LongType());
		query.addScalar("outofdatereceivedBGMB", new StringType());
		query.addScalar("description", new StringType());
		//Huypq-20190604-start
		query.addScalar("constructionCode", new StringType());
		query.addScalar("constructionTypeName", new StringType());
		query.addScalar("nguoiGiamSat", new StringType());
		//Huy-end
		query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List ls = query.list();
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}
	//NHAN_BGMB
	public List<RpConstructionDTO> doSearchNHAN(RpConstructionDTO obj) {
		StringBuilder sql = new StringBuilder("select T.ASSIGN_HANDOVER_ID asSignHadoverId,"
				+ " T.SYS_GROUP_NAME sysgroupname,"
				+ " T.CAT_PROVINCE_CODE Catprovincecode,"
				+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
				+ " T.CNT_CONTRACT_CODE cntContractCodeBGMB,"
				+ " TO_CHAR(greatest(nvl(Received_obstruct_date,sysdate-365),nvl(Received_goods_date,sysdate-365),nvl(Received_date,sysdate-365)),'dd/MM/yyyy') Companyassigndate,"
				+ " case when Station_type=2 and nvl(t.Have_work_item_name,1) not like '%Cột%' then T.COLUMN_HEIGHT end HeightTM,"
				+ " case when Station_type=2 and nvl(t.Have_work_item_name,1) not like '%Cột%' then T.NUMBER_CO end numberCoTM,"
				+ " case when Station_type=2 then T.HOUSE_TYPE_NAME end houseTypeNameTM,"
				+ " case when Station_type=1 and nvl(t.Have_work_item_name,1) not like '%Cột%' then T.COLUMN_HEIGHT end HeightDD,"
				+ " case when Station_type=1 and nvl(t.Have_work_item_name,1) not like '%Cột%' then T.NUMBER_CO end numberCoDD,"
				+ " case when Station_type=1 then T.HOUSE_TYPE_NAME end houseTypeNameDD,"
				+ " T.GROUNDING_TYPE_NAME groundingTypeName,"
				+ " T.Have_work_item_name description, "
				//Huypq-20190604-start
				+ " T.CONSTRUCTION_CODE constructionCode,"
				+ " cct.name constructionTypeName "
//				hoanm1_20190827_start
				+ " ,case when t.Have_work_item_name='Cột,Tiếp địa,Nhà,AC' then 1 end houseColumnReady,"
				+ " t.HAVE_START_POINT haveStartPoint,"
				+ " t.LENGTH_METER lengthMeter,"
				+ " TYPE_METER typeMetter,"
				+ " NUM_COLUMNS_AVAIBLE numColumnsAvaible,"
				+ " NUM_NEW_COLUMN numNewColumn,"
				+ " TYPE_COLUMN typeColumns ,"
				+ " case when t.Have_work_item_name like '%AC%' then 1 end ACReady,"
				+ " case when HAVE_START_POINT is null then 1 end stationReceive "
//				hoanm1_20190827_end
				+ " from ASSIGN_HANDOVER T "
				//Huypq-20190604-start
				+ " left join CONSTRUCTION cons on T.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ " left join CTCT_CAT_OWNER.CAT_CONSTRUCTION_TYPE cct on cons.CAT_CONSTRUCTION_TYPE_ID = cct.CAT_CONSTRUCTION_TYPE_ID "
				//Huypq-end
				+ " WHERE 1=1 AND T.RECEIVED_STATUS!=1 and T.status !=0 ");
		
		if (obj.getDateFrom() != null) {
			sql.append(" and trunc(greatest(nvl(Received_obstruct_date,sysdate-365),nvl(Received_goods_date,sysdate-365),nvl(Received_date,sysdate-365))) >= :monthYearFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and trunc(greatest(nvl(Received_obstruct_date,sysdate-365),nvl(Received_goods_date,sysdate-365),nvl(Received_date,sysdate-365))) <= :monthYearTo ");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND T.SYS_GROUP_ID  = :sysGroupId ");
		}
		if (obj.getCatProvinceId() != null) {
			sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
		}
		if (obj.getStationCode() != null) {
			sql.append(" AND (upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) OR upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) escape '&')");
		}
		if (obj.getCntContractCode() != null) {
			sql.append(" AND T.CNT_CONTRACT_CODE =:cntContractCode ");
		}
		
		sql.append(" ORDER BY greatest(nvl(Received_obstruct_date,sysdate-365),nvl(Received_goods_date,sysdate-365),nvl(Received_date,sysdate-365)) DESC,SYS_GROUP_NAME,CAT_PROVINCE_CODE ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		if (obj.getDateFrom() != null) {
            query.setParameter("monthYearFrom", obj.getDateFrom());
            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
            
        }
        if (obj.getDateTo() != null) {
            query.setParameter("monthYearTo", obj.getDateTo());
            queryCount.setParameter("monthYearTo", obj.getDateTo());
            
        }
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getCatProvinceId() != null) {
			query.setParameter("catProvinceId",obj.getCatProvinceId());
			queryCount.setParameter("catProvinceId",obj.getCatProvinceId());
		}

		if (obj.getStationCode() != null) {
			query.setParameter("stationCode", obj.getStationCode());
			queryCount.setParameter("stationCode", obj.getStationCode());
		}
		if (obj.getCntContractCode() != null) {
			query.setParameter("cntContractCode",obj.getCntContractCode());
			queryCount.setParameter("cntContractCode", obj.getCntContractCode());
		}
		
		query.addScalar("sysgroupname", new StringType());
		query.addScalar("Catprovincecode", new StringType());
		query.addScalar("Catstattionhousecode", new StringType());
		query.addScalar("cntContractCodeBGMB", new StringType());
		query.addScalar("Companyassigndate", new StringType());
		query.addScalar("HeightTM", new LongType());
		query.addScalar("numberCoTM", new LongType());
		query.addScalar("houseTypeNameTM", new StringType());
		query.addScalar("HeightDD", new LongType());
		query.addScalar("numberCoDD", new LongType());
		query.addScalar("houseTypeNameDD", new StringType());
		query.addScalar("groundingTypeName", new StringType());
		query.addScalar("description", new StringType());	
		//Huypq-20190604-start
		query.addScalar("constructionCode", new StringType());
		query.addScalar("constructionTypeName", new StringType());
		//Huy-end
//		hoanm1_20190827_start
		query.addScalar("houseColumnReady", new StringType());
		query.addScalar("haveStartPoint", new StringType());
		query.addScalar("lengthMeter", new StringType());
		query.addScalar("typeMetter", new StringType());
		query.addScalar("numColumnsAvaible", new StringType());
		query.addScalar("numNewColumn", new StringType());
		query.addScalar("typeColumns", new StringType());
		query.addScalar("ACReady", new StringType());
		query.addScalar("stationReceive", new StringType());
//		hoanm1_20190827_end
		query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List ls = query.list();
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}
	//KC
		public List<RpConstructionDTO> doSearchKC(RpConstructionDTO obj) {
			StringBuilder sql = new StringBuilder("select T.ASSIGN_HANDOVER_ID asSignHadoverId,"
					+ " T.SYS_GROUP_NAME sysgroupname,"
					+ " T.CAT_PROVINCE_CODE Catprovincecode,"
					+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
					+ " T.CNT_CONTRACT_CODE cntContractCodeBGMB,"
					+ " to_char(T.Received_date,'dd/MM/yyyy') Companyassigndate,"
					+ " trunc(SYSDATE) - trunc(T.Received_date) outOfdate, "
					+ " case when Out_of_date_start_date is not null then 1 end outofdatereceivedBGMB,"
					+ " T.DESCRIPTION description,"
					//Huypq-20190604-start
					+ " T.CONSTRUCTION_CODE constructionCode,"
					+ " cct.name constructionTypeName "
//					hoanm1_20190806_start
					+ " ,(select REPLACE(EMAIL,'@viettel.com.vn','') from sys_user sys where sys.sys_user_id=t.Update_user_id) nguoiGiamSat,"
					+ " case when t.Cat_partner_id is null then sys_group_name end thiCongTrucTiep, "
					+ " case when t.Cat_partner_id is not null then Partner_name end thiCongDoiTac "
//					hoanm1_20190806_end
					//Huy-end
					+ " from ASSIGN_HANDOVER T "
					//Huypq-20190604-start
					+ " left join CONSTRUCTION cons on T.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
					+ " left join CTCT_CAT_OWNER.CAT_CONSTRUCTION_TYPE cct on cons.CAT_CONSTRUCTION_TYPE_ID = cct.CAT_CONSTRUCTION_TYPE_ID "
					//Huypq-end
					+ " WHERE 1=1 AND T.STARTING_DATE is null AND T.RECEIVED_DATE is not null and T.status !=0 ");
			if (obj.getDateFrom() != null) {
				sql.append(" and Received_date >= :monthYearFrom ");
			}
			if (obj.getDateTo() != null) {
				sql.append(" and Received_date <= :monthYearTo ");
			}
			if (obj.getSysGroupId() != null) {
				sql.append(" AND T.SYS_GROUP_ID  = :sysGroupId ");
			}
			if (obj.getCatProvinceId() != null) {
				sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
			}
			if (obj.getStationCode() != null) {
				sql.append(" AND (upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) OR upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) escape '&')");
			}
			if (obj.getCntContractCode() != null) {
				sql.append(" AND T.CNT_CONTRACT_CODE =:cntContractCode ");
			}
			sql.append(" ORDER BY Received_date DESC,SYS_GROUP_NAME,CAT_PROVINCE_CODE ");
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			if (obj.getDateFrom() != null) {
	            query.setParameter("monthYearFrom", obj.getDateFrom());
	            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
	            
	        }
	        if (obj.getDateTo() != null) {
	            query.setParameter("monthYearTo", obj.getDateTo());
	            queryCount.setParameter("monthYearTo", obj.getDateTo());
	            
	        }
			if (obj.getSysGroupId() != null) {
				query.setParameter("sysGroupId", obj.getSysGroupId());
				queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			}
			if (obj.getCatProvinceId() != null) {
				query.setParameter("catProvinceId",obj.getCatProvinceId());
				queryCount.setParameter("catProvinceId",obj.getCatProvinceId());
			}

			if (obj.getStationCode() != null) {
				query.setParameter("stationCode", obj.getStationCode());
				queryCount.setParameter("stationCode", obj.getStationCode());
			}
			if (obj.getCntContractCode() != null) {
				query.setParameter("cntContractCode",obj.getCntContractCode());
				queryCount.setParameter("cntContractCode", obj.getCntContractCode());
			}
			query.addScalar("sysgroupname", new StringType());
			query.addScalar("Catprovincecode", new StringType());
			query.addScalar("Catstattionhousecode", new StringType());
			query.addScalar("cntContractCodeBGMB", new StringType());
			query.addScalar("Companyassigndate", new StringType());
			query.addScalar("outOfdate", new LongType());
			query.addScalar("outofdatereceivedBGMB", new StringType());
			query.addScalar("description", new StringType());
			//Huypq-20190604-start
			query.addScalar("constructionCode", new StringType());
			query.addScalar("constructionTypeName", new StringType());
			//Huy-end
			query.addScalar("nguoiGiamSat", new StringType());
			query.addScalar("thiCongTrucTiep", new StringType());
			query.addScalar("thiCongDoiTac", new StringType());
			query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			List ls = query.list();
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			return ls;
		}
		//TONTHICONG
		public List<RpConstructionDTO> doSearchTONTC(RpConstructionDTO obj) {
//			hoanm1_20190828_start
//			StringBuilder sql = new StringBuilder("select T.RP_STATION_COMPLETE_ID spStationCompleteId,"
//					+ " T.SYS_GROUP_NAME sysGroupCode,"
//					+ " T.CAT_PROVINCE_CODE Catprovincecode,"
//					+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
//					+ " T.Cnt_contract_code cntContractCodeBGMB,"
//					+ " to_char(T.STARTING_DATE,'dd/MM/yyyy') companyassigndate,"
//					+ " trunc(SYSDATE) -trunc(T.STARTING_DATE) outOfdate,"
//					+ " case when Station_type =1 and (trunc(SYSDATE) -trunc(T.STARTING_DATE)-45) >0 then 1 "
//					+ " when Station_type =2 and (trunc(SYSDATE) -trunc(T.STARTING_DATE)-30) >0 then 1 end outofdatereceivedBGMB, "
//					+ " WORK_ITEM_OUTSTANDING workItemOutStanding,"
//					+ " T.DESCRIPTION description,"
//					+ " null constructionCode,"
//					+ " null constructionTypeName "
//					+ " from RP_STATION_COMPLETE T "
//					+ " WHERE 1=1 AND T.Handover_date_build is not null AND Complete_date is null");
		StringBuilder sql = new StringBuilder(" select T.SYS_GROUP_NAME sysGroupCode, T.CAT_PROVINCE_CODE Catprovincecode,T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
					+ " T.Cnt_contract_code cntContractCodeBGMB,to_char(T.STARTING_DATE,'dd/MM/yyyy') companyassigndate,max(trunc(SYSDATE) -trunc(T.STARTING_DATE)) outOfdate,"
					+ " max( case when Station_type =1 and (trunc(SYSDATE) -trunc(T.STARTING_DATE)-45) >0 then 1 when Station_type =2 and (trunc(SYSDATE) -trunc(T.STARTING_DATE)-30) >0 then 1 end) outofdatereceivedBGMB, "
					+ " WORK_ITEM_OUTSTANDING workItemOutStanding, (listagg( (select (listagg(to_char(name), ',') within group (order by name)) from work_item wi where wi.construction_id=t.construction_id "
					+ " and wi.status =3 group by wi.construction_id),'') within group (order by (select (listagg(name, ',') within group (order by name)) from work_item wi "
					+ " where wi.construction_id=t.construction_id and wi.status =3 group by wi.construction_id)))workItemComplete,"
					+ " t.construction_code constructionCode,cstType.name constructionTypeName, "
					+ " case when t.complete_build is null then 1 end xd_dodang, max((select REPLACE(EMAIL,'@viettel.com.vn','') from (select sys.email from work_item wi,sys_user sys where wi.PERFORMER_ID=sys.sys_user_id  "
					+ " and t.construction_id=wi.construction_id and cat_work_item_group_id=1) where rownum =1))emai_XD_dodang, "
					+ " case when t.Complete_electric is null then 1 end ac_dodang, "
					+ " max ((select REPLACE(EMAIL,'@viettel.com.vn','') from (select sys.email from work_item wi,sys_user sys where wi.PERFORMER_ID=sys.sys_user_id "
					+ " and t.construction_id=wi.construction_id and cat_work_item_group_id=3) where rownum =1))emai_AC_dodang,"
					+ " case when t.Complete_erect is null then 1 end ld_dodang,"
					+ " max ((select REPLACE(EMAIL,'@viettel.com.vn','') from (select sys.email from work_item wi,sys_user sys where wi.PERFORMER_ID=sys.sys_user_id "
					+ " and t.construction_id=wi.construction_id and cat_work_item_group_id=2) where rownum =1))emai_LD_dodang,"
					+ " case when t.Complete_device is null then 1 end thietbi_dodang,"
					+ " max((select REPLACE(EMAIL,'@viettel.com.vn','') from (select sys.email from work_item wi,sys_user sys where wi.PERFORMER_ID=sys.sys_user_id "
					+ " and t.construction_id=wi.construction_id and cat_work_item_group_id=4) where rownum =1))emai_thietbi_dodang,"
					+ " case when t.complete_build is not null and t.Complete_erect is null then 1 end xong_XD_LD,"
					+ " case when t.Complete_erect is not null and t.Complete_device is null then 1 end xong_LD_TB,''description "
					+ " from RP_STATION_COMPLETE T left join construction cst on t.construction_id=cst.construction_id "
					+ " left join CAT_CONSTRUCTION_TYPE cstType on cst.CAT_CONSTRUCTION_TYPE_id=cstType.CAT_CONSTRUCTION_TYPE_id WHERE 1=1 AND T.Handover_date_build is not null AND t.Complete_date is null");
			
			if (obj.getSysGroupId() != null) {
				sql.append(" AND T.SYS_GROUP_ID  = :sysGroupId ");
			}
			if (obj.getCatProvinceId() != null) {
				sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
			}
			if (obj.getStationCode() != null) {
				sql.append(" AND (upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) OR upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) escape '&')");
			}
			if (obj.getCntContractCode() != null) {
				sql.append(" AND T.CNT_CONTRACT_CODE =:cntContractCode ");
			}
			if (obj.getReceivedStatus() != null) {
//				sql.append(" AND (case when Construction_state =1 then 1 else 0 end) =:receivedStatus ");
				sql.append(" AND (CASE WHEN station_type = 1 AND(trunc(SYSDATE) - trunc(t.starting_date) - 45) > 0 THEN 1 "
						+ " WHEN station_type = 2 AND(trunc(SYSDATE) - trunc(t.starting_date) - 30) > 0 THEN 1 else 0 END) =:receivedStatus ");
			}
			sql.append(" group by  T.SYS_GROUP_NAME ,T.CAT_PROVINCE_CODE ,T.CAT_STATION_HOUSE_CODE ,T.Cnt_contract_code ,T.STARTING_DATE,"
					+ " WORK_ITEM_OUTSTANDING,t.construction_code,t.complete_build,t.Complete_electric,t.Complete_erect,t.Complete_device,cstType.name "
					+ " ORDER BY SYS_GROUP_NAME,CAT_PROVINCE_CODE,CAT_STATION_HOUSE_CODE,Cnt_contract_code ");
//			hoanm1_20190828_end
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			if (obj.getSysGroupId() != null) {
				query.setParameter("sysGroupId", obj.getSysGroupId());
				queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			}
			if (obj.getCatProvinceId() != null) {
				query.setParameter("catProvinceId",obj.getCatProvinceId());
				queryCount.setParameter("catProvinceId",obj.getCatProvinceId());
			}

			if (obj.getStationCode() != null) {
				query.setParameter("stationCode", obj.getStationCode());
				queryCount.setParameter("stationCode", obj.getStationCode());
			}
			if (obj.getCntContractCode() != null) {
				query.setParameter("cntContractCode",obj.getCntContractCode());
				queryCount.setParameter("cntContractCode", obj.getCntContractCode());
			}
			if (obj.getReceivedStatus() != null) {
				query.setParameter("receivedStatus", obj.getReceivedStatus());
				queryCount.setParameter("receivedStatus", obj.getReceivedStatus());
			}

			query.addScalar("sysGroupCode", new StringType());
			query.addScalar("Catprovincecode", new StringType());
			query.addScalar("Catstattionhousecode", new StringType());
			query.addScalar("cntContractCodeBGMB", new StringType());
			query.addScalar("companyassigndate", new StringType());
			query.addScalar("outOfdate", new LongType());
			query.addScalar("outofdatereceivedBGMB", new StringType());
			query.addScalar("workItemOutStanding", new StringType());
			query.addScalar("description", new StringType());
			//Huypq-20190604-start
			query.addScalar("constructionCode", new StringType());
			query.addScalar("constructionTypeName", new StringType());
			//Huy-end
//			hoanm1_20190828_start
			query.addScalar("workItemComplete", new StringType());
			query.addScalar("xd_dodang", new StringType());
			query.addScalar("emai_XD_dodang", new StringType());
			query.addScalar("ac_dodang", new StringType());
			query.addScalar("emai_AC_dodang", new StringType());
			query.addScalar("ld_dodang", new StringType());
			query.addScalar("emai_LD_dodang", new StringType());
			query.addScalar("thietbi_dodang", new StringType());
			query.addScalar("emai_thietbi_dodang", new StringType());
			query.addScalar("xong_XD_LD", new StringType());
			query.addScalar("xong_LD_TB", new StringType());
//			hoanm1_20190828_end
			query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			List ls = query.list();
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			return ls;
		}
//		HSHC
		public List<RpConstructionDTO> doSearchHSHC(RpConstructionDTO obj) {
//			hoanm1_20190813_start
//			StringBuilder sql = new StringBuilder("select T.RP_STATION_COMPLETE_ID spStationCompleteId,"
//					+ " T.SYS_GROUP_NAME sysGroupCode,"
//					+ " T.CAT_PROVINCE_CODE Catprovincecode,"
//					+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
//					+ " T.CNT_CONTRACT_CODE cntContractCodeBGMB,"
//					+ " to_char(T.COMPLETE_DATE,'dd/MM/yyyy') companyassigndate,"
//					+ " to_char(T.Approved_complete_date,'dd/MM/yyyy') approvedCompleteDate,"
//					+ " completion_record_state completionRecordState, Total_value totalQuantity,"
//					+ " T.DESCRIPTION description"
//					+ " from RP_STATION_COMPLETE T WHERE 1=1 AND Complete_completion_record is null ");
			StringBuilder sql = new StringBuilder("select T.construction_code constructionCode,cstType.name constructionTypeName,catStation.code catstationCode,"
			+ " T.SYS_GROUP_NAME sysGroupCode,"
			+ " T.CAT_PROVINCE_CODE Catprovincecode,"
			+ " T.CAT_STATION_HOUSE_CODE Catstattionhousecode,"
			+ " T.CNT_CONTRACT_CODE cntContractCodeBGMB,"
			+ " to_char(T.COMPLETE_DATE,'dd/MM/yyyy') companyassigndate,"
			+ " to_char(T.Approved_complete_date,'dd/MM/yyyy') approvedCompleteDate,"
			+ " 1 completionRecordState, Total_value totalQuantity,"
			+ " T.DESCRIPTION description"
			+ " from RP_STATION_COMPLETE T left join construction cst on t.construction_id=cst.construction_id "
			+ " left join CAT_CONSTRUCTION_TYPE cstType on cst.CAT_CONSTRUCTION_TYPE_id=cstType.CAT_CONSTRUCTION_TYPE_id "
			+ " left join cat_station catStation on cst.cat_station_id=catStation.cat_station_id WHERE 1=1 AND Complete_completion_record is null and Total_value >0 ");
//			hoanm1_20190813_end
			if (obj.getDateFrom() != null) {
				sql.append(" and trunc(T.Complete_date ) >= :monthYearFrom ");
			}
			if (obj.getDateTo() != null) {
				sql.append(" and trunc(T.Complete_date ) <= :monthYearTo ");
			}
			if (obj.getSysGroupId() != null) {
				sql.append(" AND T.SYS_GROUP_ID  = :sysGroupId ");
			}
			if (obj.getCatProvinceId() != null) {
				sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
			}
			if (obj.getStationCode() != null) {
				sql.append(" AND (upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) OR upper(T.CAT_STATION_HOUSE_CODE) LIKE upper(:stationCode) escape '&')");
			}
			if (obj.getCntContractCode() != null) {
				sql.append(" AND T.CNT_CONTRACT_CODE =:cntContractCode ");
			}
			if (obj.getReceivedStatus() != null) {
				sql.append(" AND nvl(completion_record_state,0) =:receivedStatus ");
			}
			
			if(obj.getSysGroupType().equals("1")) {
				sql.append(" AND T.SYS_GROUP_ID in (select sys_group_id from  sys_group where code like '%XC%' and name like '%Chi nhánh%') ");
			} else if(obj.getSysGroupType().equals("2")) {
				sql.append(" AND T.SYS_GROUP_ID in (select sys_group_id from  sys_group where code like '%TTKT%' and name like '%Chi nhánh Kỹ thuật%') ");
			}
//			hoanm1_20190813_start
			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
		            sql.append("AND (" +
		                    "upper(T.CONSTRUCTION_CODE) LIKE upper(:keySearch) " +
		                    "OR upper(catStation.code) LIKE upper(:keySearch) escape '&') ");
		     }
//			hoanm1_20190813_end
			 
			sql.append(" ORDER BY T.COMPLETE_DATE DESC,T.SYS_GROUP_NAME,T.CAT_PROVINCE_CODE ");
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			if (obj.getDateFrom() != null) {
	            query.setParameter("monthYearFrom", obj.getDateFrom());
	            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
	            
	        }
	        if (obj.getDateTo() != null) {
	            query.setParameter("monthYearTo", obj.getDateTo());
	            queryCount.setParameter("monthYearTo", obj.getDateTo());
	            
	        }
			if (obj.getSysGroupId() != null) {
				query.setParameter("sysGroupId", obj.getSysGroupId());
				queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			}
			if (obj.getCatProvinceId() != null) {
				query.setParameter("catProvinceId",obj.getCatProvinceId());
				queryCount.setParameter("catProvinceId",obj.getCatProvinceId());
			}

			if (obj.getStationCode() != null) {
				query.setParameter("stationCode", obj.getStationCode());
				queryCount.setParameter("stationCode", obj.getStationCode());
			}
			if (obj.getCntContractCode() != null) {
				query.setParameter("cntContractCode",obj.getCntContractCode());
				queryCount.setParameter("cntContractCode", obj.getCntContractCode());
			}
			if (obj.getReceivedStatus() != null) {
				query.setParameter("receivedStatus", obj.getReceivedStatus());
				queryCount.setParameter("receivedStatus", obj.getReceivedStatus());
			}
//			hoanm1_20190813_start
			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
	            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
	            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
	        }
//			hoanm1_20190813_end
			query.addScalar("sysGroupCode", new StringType());
			query.addScalar("Catprovincecode", new StringType());
			query.addScalar("Catstattionhousecode", new StringType());
			query.addScalar("cntContractCodeBGMB", new StringType());
			query.addScalar("companyassigndate", new StringType());
			query.addScalar("approvedCompleteDate", new StringType());
			query.addScalar("completionRecordState", new LongType());
			query.addScalar("totalQuantity", new DoubleType());
			query.addScalar("description", new StringType());
//			hoanm1_20190813_start
			query.addScalar("constructionCode", new StringType());
			query.addScalar("constructionTypeName", new StringType());
			query.addScalar("catstationCode", new StringType());
//			hoanm1_20190813_end
			query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			
			List ls = query.list();
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			return ls;
		}
//	hungtd_20181217_end
		
//		hungtd_20192101_start
		public List<couponExportDTO> doSearchCoupon(couponExportDTO obj, List<String> provinceIdList) {
			StringBuilder sql = new StringBuilder(" SELECT distinct s.STOCK_CODE stockCode, "
					 + " s.CODE code,"
					 + " s.ORDER_CODE orderCode,"
					 + " s.REAL_IE_TRANS_DATE realIeTransDate,"
					 + " s.CONSTRUCTION_CODE constructionCode,"
					 + " nvl(s.CONFIRM,0) comfirm,"
					 + " sg.NAME name,"
					 + " sg.SYS_GROUP_ID sysGroupId,"
					 + " su.LOGIN_NAME loginName,"
					 + " su.EMPLOYEE_CODE employeeCode,"
					 + " su.FULL_NAME fullName,"
					 + " su.EMAIL email,"
					 + " '' codeParam,"
					 + " (sysdate  - cast(s.REAL_IE_TRANS_DATE as date)) realDate,"
					 + " su.PHONE_NUMBER phoneNumber,"
					 + " a.CODE catStationCode, case when nvl(s.CONFIRM,0)=0 then 'Chờ phê duyệt' when nvl(s.CONFIRM,0)=1 then 'Đã phê duyệt' when nvl(s.CONFIRM,0)=2 then 'Đã từ chối' end comfirmExcel "
					 + " ,s.contract_code cntContractCode "
					 + " FROM SYN_STOCK_TRANS s,SYS_GROUP sg, "
					 + " SYS_USER su,CONSTRUCTION c,CAT_STATION a"
					 + "  ,syn_stock_trans_Detail std"
					 + " WHERE s.type= 2 AND s.CONSTRUCTION_ID=c.CONSTRUCTION_ID(+) AND s.SYS_GROUP_ID= sg.SYS_GROUP_ID(+) AND s.LAST_SHIPPER_ID= su.SYS_USER_ID(+) AND"
					 + " c.CAT_STATION_ID=a.CAT_STATION_ID(+) "
					 + " and  std.SYN_STOCK_TRANS_ID = s.SYN_STOCK_TRANS_ID"
					 + " and s.REQ_TYPE in(2,3) and s.DEST_TYPE=3 and s.stock_code not like '%CTCT_%'");
			if (obj.getDateFrom() != null) {
				sql.append(" and REAL_IE_TRANS_DATE >= :monthYearFrom ");
			}
			if (obj.getDateTo() != null) {
				sql.append(" and REAL_IE_TRANS_DATE <= :monthYearTo ");
			}
			if (obj.getEmployeeCode() != null) {
				sql.append(" AND su.EMPLOYEE_CODE =:employeeCode ");
			}
			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
				sql.append(" AND (upper(s.CONSTRUCTION_CODE) LIKE upper(:keySearch) or upper(A.CODE) LIKE upper(:keySearch) or upper(s.contract_code) LIKE upper(:keySearch) escape '&') ");
			}
			if (obj.getName() != null) {
				sql.append(" AND (upper(sg.NAME) LIKE upper(:name) escape '&')");
			}
			if (obj.getEmail() != null) {
				sql.append(" AND (upper(su.EMAIL) LIKE upper(:email) escape '&')");
			}
			if (obj.getSysGroupId() != null) {
				sql.append(" AND s.SYS_GROUP_ID =:sysGroupId ");
			}
//			hoanm1_20190214_start
			if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
		        sql.append(" AND nvl(S.CONFIRM,0) in :lstConfirm ");
		    }
//			hoanm1_20190214_end
			
			//Huypq-20191003-start
			if(provinceIdList!=null && provinceIdList.size()>0) {
				sql.append(" AND a.CAT_PROVINCE_ID in (:provinceIdList) ");
			}
			//Huy-end
			
			sql.append(" ORDER BY s.REAL_IE_TRANS_DATE DESC");
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			query.addScalar("stockCode", new StringType());
			query.addScalar("code", new StringType());
			query.addScalar("codeParam", new StringType());
			query.addScalar("comfirm", new StringType());
			query.addScalar("realDate", new StringType());
			query.addScalar("catStationCode", new StringType());
			query.addScalar("cntContractCode", new StringType());
			query.addScalar("realIeTransDate", new DateType());
			query.addScalar("constructionCode", new StringType());
			query.addScalar("name", new StringType());
			query.addScalar("email", new StringType());
			query.addScalar("fullName", new StringType());
			query.addScalar("sysGroupId", new LongType());
			query.addScalar("phoneNumber", new StringType());
			query.addScalar("employeeCode", new StringType());
			query.addScalar("comfirmExcel", new StringType());
			query.addScalar("orderCode", new StringType());
			
			
			if (obj.getSysGroupId() != null) {
				query.setParameter("sysGroupId", obj.getSysGroupId());
				queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			}
			if (obj.getEmail() != null) {
				query.setParameter("email", obj.getEmail());
				queryCount.setParameter("email", obj.getEmail());
			}
			if (obj.getDateFrom() != null) {
	            query.setParameter("monthYearFrom", obj.getDateFrom());
	            queryCount.setParameter("monthYearFrom", obj.getDateFrom());
	            
	        }
	        if (obj.getDateTo() != null) {
	            query.setParameter("monthYearTo", obj.getDateTo());
	            queryCount.setParameter("monthYearTo", obj.getDateTo());
	            
	        }

			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
	            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
	            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
	        }
			if (obj.getEmployeeCode() != null) {
				query.setParameter("employeeCode", obj.getEmployeeCode());
				queryCount.setParameter("employeeCode", obj.getEmployeeCode());
			}
			if (StringUtils.isNotEmpty(obj.getName())) {
	            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
	            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
	        }
//			hoanm1_20190214_start
			if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
	            query.setParameterList("lstConfirm", obj.getListConfirm());
	            queryCount.setParameterList("lstConfirm", obj.getListConfirm());
	        }
//			hoanm1_20190214_end
			
			//Huypq-20191003-start
			if(provinceIdList!=null && provinceIdList.size()>0) {
				query.setParameterList("provinceIdList", provinceIdList);
	            queryCount.setParameterList("provinceIdList", provinceIdList);
			}
			//Huy-end
			
			query.setResultTransformer(Transformers.aliasToBean(couponExportDTO.class));
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			
			List ls = query.list();
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			return ls;
		}
		public List<couponExportDTO> doSearchPopup(couponExportDTO obj) {
			StringBuilder sql = new StringBuilder("SELECT H.SYS_USER_ID sysUserId,"
					+ " H.LOGIN_NAME loginName,"
					+ " H.EMPLOYEE_CODE employeeCode,"
					+ " H.EMAIL email,"
					+ " H.PHONE_NUMBER phoneNumber,"
					+ " H.FULL_NAME fullName"
					+ " FROM SYS_USER H"
					+ " WHERE 1=1 AND H.STATUS=1");
			
			if (StringUtils.isNotEmpty(obj.getEmail())) {
				sql.append(" AND (upper(H.EMPLOYEE_CODE) LIKE upper(:email) escape '&')");
				sql.append(" or (upper(H.EMAIL) LIKE upper(:email) escape '&')");
				sql.append(" or (upper(H.FULL_NAME) LIKE upper(:email) escape '&')");
			}
			if (StringUtils.isNotEmpty(obj.getEmployeeCode())) {
				sql.append(" AND (upper(H.EMPLOYEE_CODE) LIKE upper(:employeeCode) escape '&')");
				sql.append(" or (upper(H.FULL_NAME) LIKE upper(:employeeCode) escape '&')");
			}
			
				
			
			
			sql.append(" ORDER BY H.SYS_USER_ID ASC");
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			query.addScalar("employeeCode", new StringType());
			query.addScalar("loginName", new StringType());
			query.addScalar("email", new StringType());
			query.addScalar("fullName", new StringType());
			query.addScalar("phoneNumber", new StringType());
			query.addScalar("sysUserId", new LongType());
//			query.addScalar("status", new StringType());
			
			if (StringUtils.isNotEmpty(obj.getEmployeeCode())) {
	            query.setParameter("employeeCode", "%" + ValidateUtils.validateKeySearch(obj.getEmployeeCode()) + "%");
	            queryCount.setParameter("employeeCode", "%" + ValidateUtils.validateKeySearch(obj.getEmployeeCode()) + "%");
	        }
			
			if (StringUtils.isNotEmpty(obj.getFullName())) {
	            query.setParameter("fullName", "%" + ValidateUtils.validateKeySearch(obj.getFullName()) + "%");
	            queryCount.setParameter("fullName", "%" + ValidateUtils.validateKeySearch(obj.getFullName()) + "%");
	        }
			if (StringUtils.isNotEmpty(obj.getEmail())) {
	            query.setParameter("email", "%" + ValidateUtils.validateKeySearch(obj.getEmail()) + "%");
	            queryCount.setParameter("email", "%" + ValidateUtils.validateKeySearch(obj.getEmail()) + "%");
	        }
			if (obj.getSysUserId() != null) {
				query.setParameter("sysUserId", obj.getSysUserId());
				queryCount.setParameter("sysUserId", obj.getSysUserId());
			}
			if (obj.getLoginName() != null) {
				query.setParameter("loginName", obj.getLoginName());
				queryCount.setParameter("loginName", obj.getLoginName());
			}
			
			query.setResultTransformer(Transformers.aliasToBean(couponExportDTO.class));
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			
			List ls = query.list();
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			return ls;
		}
//		hungtd_20192101_end
		
		//HuyPq-20190724-start
		public List<WorkItemDetailDTO> getSysGroupIdByTTKT(){
			StringBuilder sql = new StringBuilder("SELECT sys_group_id sysGroupId " + 
					"FROM sys_group " + 
					"WHERE (code LIKE '%XC%' " + 
					"AND name LIKE '%Chi nhánh%') " + 
					"OR (code LIKE '%TTKT%' " + 
					"AND name LIKE '%Chi nhánh Kỹ thuật%')");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("sysGroupId", new LongType());
			query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
			
			return query.list();
		}
		
		public WorkItemDetailDTO getGroupLv2ByGroupUser(Long id){
			StringBuilder sql = new StringBuilder("SELECT sg.SYS_GROUP_ID sysGroupId, " + 
					"	sg.code sysGroupCode, " + 
					"	sg.name sysGroupName " + 
					"	FROM CTCT_CAT_OWNER.SYS_GROUP sg " + 
					"	LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg1 " + 
					"	ON sg.name            = sg1.GROUP_NAME_LEVEL2 " + 
					"	WHERE sg1.SYS_GROUP_ID=:id");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("sysGroupId", new LongType());
			query.addScalar("sysGroupCode", new StringType());
			query.addScalar("sysGroupName", new StringType());
			
			query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
			
			query.setParameter("id", id);
			
			@SuppressWarnings("unchecked")
	        List<WorkItemDetailDTO> lst = query.list();
	        if (lst.size() > 0) {
	            return lst.get(0);
	        }
	        return null;
		}
		//Huy-end
			//hienvd: START 25/7/2019
	public List<couponExportDTO> doSearchSysPXK(couponExportDTO obj) {
		StringBuilder sql = new StringBuilder(" SELECT distinct s.STOCK_CODE stockCode, "
				+ " s.CODE code,"
				+ " s.SYN_STOCK_TRANS_ID synStockTransId,"
				+ " s.SYN_TRANS_TYPE synTransType,"
				+ " s.ORDER_CODE orderCode,"
				+ " s.STOCK_NAME stockName,"
				+ " s.SHIPPER_NAME shipperName,"
				+ " s.DESCRIPTION description,"
				+ " s.SIGN_STATE signState,"
				+ " s.STATUS status,"
				+ " s.CREATED_BY_NAME createdByName,"
				+ " s.CREATED_DATE createdDate,"
				+ " s.CREATED_DEPT_NAME createdDeptName,"
				+ " s.CANCEL_BY_NAME cancelByName,"
				+ " s.CANCEL_REASON_NAME cancelReasonName,"
				+ " s.CANCEL_DATE cancelDate,"
				+ " s.CANCEL_DESCRIPTION cancelDescription,"
				+ " s.REAL_IE_TRANS_DATE realIeTransDate,"
				+ " s.CONSTRUCTION_CODE constructionCode,"
				+ " s.CONSTRUCTION_ID constructionId,"
				+ " nvl(s.CONFIRM,0) comfirm,"
				+ " sg.NAME name,"
				+ " sg.SYS_GROUP_ID sysGroupId,"
				+ " su.LOGIN_NAME loginName,"
				+ " su.EMPLOYEE_CODE employeeCode,"
				+ " su.FULL_NAME fullName,"
				+ " su.EMAIL email,"
				+ " '' codeParam,"
				+ " (sysdate  - cast(s.REAL_IE_TRANS_DATE as date)) realDate,"
				+ " su.PHONE_NUMBER phoneNumber,"
				+ " a.CODE catStationCode, case when nvl(s.CONFIRM,0)=0 then 'Chờ phê duyệt' when nvl(s.CONFIRM,0)=1 then 'Đã phê duyệt' when nvl(s.CONFIRM,0)=2 then 'Đã từ chối' end comfirmExcel "
				+ " ,s.contract_code cntContractCode "
				+ " ,trunc(nvl(s.UPDATED_DATE,sysdate))- to_date(to_char(s.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') daysOverdue"
				+ " FROM SYN_STOCK_TRANS s,SYS_GROUP sg, "
				+ " SYS_USER su,CONSTRUCTION c,CAT_STATION a"
				+ "  ,syn_stock_trans_Detail std"
				+ " WHERE s.type= 2 AND s.CONSTRUCTION_ID=c.CONSTRUCTION_ID(+) AND s.SYS_GROUP_ID= sg.SYS_GROUP_ID(+) AND s.LAST_SHIPPER_ID= su.SYS_USER_ID(+) AND"
				+ " c.CAT_STATION_ID=a.CAT_STATION_ID(+) "
				+ " and  std.SYN_STOCK_TRANS_ID = s.SYN_STOCK_TRANS_ID"
				+ " and s.REQ_TYPE=3 and s.DEST_TYPE=3 and s.stock_code not like '%CTCT_%' "
				+ " and trunc(nvl(s.UPDATED_DATE,sysdate))- to_date(to_char(s.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >7");

		if (obj.getDateFrom() != null) {
			sql.append(" and REAL_IE_TRANS_DATE >= :monthYearFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and REAL_IE_TRANS_DATE <= :monthYearTo ");
		}
		if (obj.getEmployeeCode() != null) {
			sql.append(" AND su.EMPLOYEE_CODE =:employeeCode ");
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(s.CONSTRUCTION_CODE) LIKE upper(:keySearch) or upper(A.CODE) LIKE upper(:keySearch) or upper(s.contract_code) LIKE upper(:keySearch) escape '&') ");
		}
		if (obj.getName() != null) {
			sql.append(" AND (upper(sg.NAME) LIKE upper(:name) escape '&')");
		}
		if (obj.getEmail() != null) {
			sql.append(" AND (upper(su.EMAIL) LIKE upper(:email) escape '&')");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND s.SYS_GROUP_ID =:sysGroupId ");
		}
		if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
			sql.append(" AND nvl(S.CONFIRM,0) in :lstConfirm ");
		}

		sql.append(" ORDER BY s.SYN_STOCK_TRANS_ID DESC");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("stockCode", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("codeParam", new StringType());
		query.addScalar("comfirm", new StringType());
		query.addScalar("realDate", new StringType());
		query.addScalar("catStationCode", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("realIeTransDate", new DateType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("comfirmExcel", new StringType());
		query.addScalar("daysOverdue", new LongType());
		query.addScalar("orderCode" , new StringType());
		query.addScalar("stockName" , new StringType());
		query.addScalar("shipperName" , new StringType());
		query.addScalar("description" , new StringType());
		query.addScalar("signState" , new StringType());
		query.addScalar("status" , new StringType());
		query.addScalar("createdByName" , new StringType());
		query.addScalar("createdDate" , new DateType());
		query.addScalar("createdDeptName" , new StringType());
		query.addScalar("cancelByName", new StringType());
		query.addScalar("cancelDate", new DateType());
		query.addScalar("cancelReasonName", new StringType());
		query.addScalar("cancelDescription", new StringType());
		query.addScalar("synStockTransId", new LongType());
		query.addScalar("synTransType", new LongType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getEmail() != null) {
			query.setParameter("email", obj.getEmail());
			queryCount.setParameter("email", obj.getEmail());
		}
		if (obj.getDateFrom() != null) {
			query.setParameter("monthYearFrom", obj.getDateFrom());
			queryCount.setParameter("monthYearFrom", obj.getDateFrom());
		}
		if (obj.getDateTo() != null) {
			query.setParameter("monthYearTo", obj.getDateTo());
			queryCount.setParameter("monthYearTo", obj.getDateTo());

		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getEmployeeCode() != null) {
			query.setParameter("employeeCode", obj.getEmployeeCode());
			queryCount.setParameter("employeeCode", obj.getEmployeeCode());
		}
		if (StringUtils.isNotEmpty(obj.getName())) {
			query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
			queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
		}
		if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
			query.setParameterList("lstConfirm", obj.getListConfirm());
			queryCount.setParameterList("lstConfirm", obj.getListConfirm());
		}

		query.setResultTransformer(Transformers.aliasToBean(couponExportDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		List ls = query.list();
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}

	public List<couponExportDTO> doSearchSysPXK60(couponExportDTO obj) {
		StringBuilder sql = new StringBuilder(" select * from (SELECT"
				+ " a.STOCK_CODE stockCode, "
				+ " a.order_code orderCode, "
				+ " a.CODE code, "
				+ " a.REAL_IE_TRANS_DATE realIeTransDate,"
				+ " a.construction_code constructionCode,"
				+ " a.contract_code cntContractCode,"
				+ " sg.name sysGroupName, "
				+ " u.full_name fullName,"
				+ " u.email email,"
				+ " a.SYN_STOCK_TRANS_ID synStockTransId,"
				+ " a.SYN_TRANS_TYPE synTransType,"
				+ " a.STOCK_NAME stockName,"
				+ " a.SHIPPER_NAME shipperName,"
				+ " a.DESCRIPTION description,"
				+ " a.SIGN_STATE signState,"
				+ " a.STATUS status,"
				+ " a.CREATED_BY_NAME createdByName,"
				+ " a.CREATED_DATE createdDate,"
				+ " a.CREATED_DEPT_NAME createdDeptName,"
				+ " a.CANCEL_BY_NAME cancelByName,"
				+ " a.CANCEL_REASON_NAME cancelReasonName,"
				+ " a.CANCEL_DATE cancelDate,"
				+ " a.CANCEL_DESCRIPTION cancelDescription,"
				+ " trunc(nvl(rp.DATECOMPLETE,sysdate))-(real_ie_trans_date+60) daysOverdue,"
				+ " RANK() OVER(PARTITION BY construction_code ORDER BY real_ie_trans_date DESC)time_construction,"
				+ " a.SYS_GROUP_ID,u.EMPLOYEE_CODE,a.CONFIRM "
				+ " from SYN_STOCK_TRANS a"
				+ " left join sys_user u on a.last_shipper_id=u.sys_user_id "
				+ " left join sys_group sg on a.sys_group_id=sg.sys_group_id "
				+ " left join RP_HSHC rp on a.construction_id=rp.CONSTRUCTIONID "
				+ " where a.type=2 and a.REQ_TYPE=3 and a.DEST_TYPE=3 and a.stock_code not like '%CTCT_%' and nvl(rp.DATECOMPLETE,sysdate) > a.real_ie_trans_date + 60"
				+ " and nvl(rp.completestate,1)=1) a where time_construction=1  ");

		if (obj.getDateFrom() != null) {
			sql.append(" and a.realIeTransDate >= :monthYearFrom");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and a.realIeTransDate <= :monthYearTo");
		}
		if (obj.getEmployeeCode() != null) {
			sql.append(" AND a.EMPLOYEE_CODE =:employeeCode");
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(a.constructionCode) LIKE upper(:keySearch) or upper(a.cntContractCode) LIKE upper(:keySearch) escape '&') ");
		}
		if (obj.getName() != null) {
			sql.append(" AND (upper(sg.sysGroupName) LIKE upper(:name) escape '&')");
		}
		if (obj.getEmail() != null) {
			sql.append(" AND (upper(u.email) LIKE upper(:email) escape '&')");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.SYS_GROUP_ID =:sysGroupId ");
		}
		if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
			sql.append(" AND nvl(a.CONFIRM,0) in :lstConfirm ");
		}

		sql.append(" ORDER BY a.realIeTransDate DESC");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("stockCode", new StringType());
		query.addScalar("orderCode" , new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("realIeTransDate", new DateType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("sysGroupName" , new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("daysOverdue", new LongType());
		query.addScalar("synStockTransId", new LongType());
		query.addScalar("synTransType", new LongType());
		query.addScalar("stockName" , new StringType());
		query.addScalar("shipperName" , new StringType());
		query.addScalar("description" , new StringType());
		query.addScalar("signState" , new StringType());
		query.addScalar("status" , new StringType());
		query.addScalar("createdByName" , new StringType());
		query.addScalar("createdDate" , new DateType());
		query.addScalar("createdDeptName" , new StringType());
		query.addScalar("cancelByName", new StringType());
		query.addScalar("cancelDate", new DateType());
		query.addScalar("cancelReasonName", new StringType());
		query.addScalar("cancelDescription", new StringType());


		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getEmail() != null) {
			query.setParameter("email", obj.getEmail());
			queryCount.setParameter("email", obj.getEmail());
		}
		if (obj.getDateFrom() != null) {
			query.setParameter("monthYearFrom", obj.getDateFrom());
			queryCount.setParameter("monthYearFrom", obj.getDateFrom());

		}
		if (obj.getDateTo() != null) {
			query.setParameter("monthYearTo", obj.getDateTo());
			queryCount.setParameter("monthYearTo", obj.getDateTo());

		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getEmployeeCode() != null) {
			query.setParameter("employeeCode", obj.getEmployeeCode());
			queryCount.setParameter("employeeCode", obj.getEmployeeCode());
		}
		if (StringUtils.isNotEmpty(obj.getName())) {
			query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
			queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
		}
		if (obj.getListConfirm() != null && obj.getListConfirm().size() > 0) {
			query.setParameterList("lstConfirm", obj.getListConfirm());
			queryCount.setParameterList("lstConfirm", obj.getListConfirm());
		}

		query.setResultTransformer(Transformers.aliasToBean(couponExportDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List ls = query.list();
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}

	//hienvd: END
	
	//Huypq-20191004-start
	@SuppressWarnings("unchecked")
	public List<RpConstructionDTO> doSearchEvaluateKpiHshc(RpConstructionDTO obj){
		StringBuilder sql = new StringBuilder(" select  " + 
				"T.SYS_GROUP_NAME sysGroupCode, " + 
				"T.CAT_PROVINCE_CODE catProvinceCode, " + 
				"T.CAT_STATION_HOUSE_CODE Catstattionhousecode, " + 
				"catStation.code catstationCode, " + 
				"T.construction_code constructionCode, " + 
				"cstType.name constructionTypeName, " + 
				"T.CNT_CONTRACT_CODE cntContractCode, " + 
				"to_char(T.COMPLETE_DATE,'dd/MM/yyyy') Companyassigndate, " + 
				"Total_value totalQuantity, " + 
				"to_char(T.Approved_complete_date,'MM/yyyy') approvedCompleteDate, " + 
				"T.DESCRIPTION description, " + 
				"case when                                                 " + 
				"to_char(T.COMPLETE_DATE,'yyyy')=to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
				"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM') - to_char(T.COMPLETE_DATE,'MM') >1 then 2                                                  " + 
				"when  to_char(T.COMPLETE_DATE,'yyyy')=to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
				"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM') - to_char(T.COMPLETE_DATE,'MM') <=1 then 1 " + 
				"when to_char(T.COMPLETE_DATE,'yyyy')<to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
				"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM')= '01' and to_char(T.COMPLETE_DATE,'MM') - 12 =0 then 1 " + 
				"when to_char(T.COMPLETE_DATE,'yyyy')<to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
				"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM')= '01' and to_char(T.COMPLETE_DATE,'MM') - 12 !=0 then 2 " + 
				"else 2 end kpiState "+
				"from RP_STATION_COMPLETE T  " + 
				"left join construction cst on t.construction_id=cst.construction_id  " + 
				"left join CAT_CONSTRUCTION_TYPE cstType on cst.CAT_CONSTRUCTION_TYPE_id=cstType.CAT_CONSTRUCTION_TYPE_id  " + 
				"left join cat_station catStation on cst.cat_station_id=catStation.cat_station_id  " + 
				"WHERE 1=1  " + 
				"and T.COMPLETE_DATE is not null  " + 
				"and Total_value >0 ");
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND upper(catStation.code) like upper(:keySearch) escape '&' "
					+ " OR upper(T.construction_code) like upper(:keySearch) escape '&' ");
		}
		
		if(StringUtils.isNotBlank(obj.getCntContractCode())) {
			sql.append(" AND T.CNT_CONTRACT_CODE = :contractCode ");
		}
		
		if(obj.getSysGroupId()!=null) {
			sql.append(" AND T.SYS_GROUP_ID=:sysGroupId ");
		}
		
		if(obj.getCatProvinceId()!=null) {
			sql.append(" AND T.CAT_PROVINCE_ID=:catProvinceId ");
		}
		
		if(StringUtils.isNotBlank(obj.getCatstattionhousecode())) {
			sql.append(" AND T.CAT_STATION_HOUSE_CODE=:catStattionHouseCode ");
		}
		
		if(StringUtils.isNotBlank(obj.getKpiState())) {
			sql.append(" AND (case when " + 
					"to_char(T.COMPLETE_DATE,'yyyy')=to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
					"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM') - to_char(T.COMPLETE_DATE,'MM') >1 then 2                                                  " + 
					"when  to_char(T.COMPLETE_DATE,'yyyy')=to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
					"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM') - to_char(T.COMPLETE_DATE,'MM') <=1 then 1 " + 
					"when to_char(T.COMPLETE_DATE,'yyyy')<to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
					"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM')= '01' and to_char(T.COMPLETE_DATE,'MM') - 12 =0 then 1 " + 
					"when to_char(T.COMPLETE_DATE,'yyyy')<to_char(nvl(Approved_complete_date,trunc(sysdate)),'yyyy') and  " + 
					"to_char(nvl(Approved_complete_date,trunc(sysdate)),'MM')= '01' and to_char(T.COMPLETE_DATE,'MM') - 12 !=0 then 2 " + 
					"else 2 end) =:kpiState ");
		}
		
		if(obj.getDateFrom()!=null) {
			sql.append(" and T.COMPLETE_DATE >=:dateFrom ");
		}
		
		if(obj.getDateTo()!=null) {
			sql.append(" and T.COMPLETE_DATE <=:dateTo ");
		}
		
		sql.append(" ORDER BY T.RP_STATION_COMPLETE_ID ASC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("Catstattionhousecode", new StringType());
		query.addScalar("catstationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("constructionTypeName", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("Companyassigndate", new StringType());
		query.addScalar("totalQuantity", new DoubleType());
		query.addScalar("approvedCompleteDate", new StringType());
		query.addScalar("description", new StringType());
		query.addScalar("kpiState", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(RpConstructionDTO.class));
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		
		if(StringUtils.isNotBlank(obj.getCntContractCode())) {
			query.setParameter("contractCode", obj.getCntContractCode());
			queryCount.setParameter("contractCode", obj.getCntContractCode());
		}
		
		if(obj.getSysGroupId()!=null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		
		if(obj.getCatProvinceId()!=null) {
			query.setParameter("catProvinceId", obj.getCatProvinceId());
			queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
		}
		
		if(StringUtils.isNotBlank(obj.getCatstattionhousecode())) {
			query.setParameter("catStattionHouseCode", obj.getCatstattionhousecode());
			queryCount.setParameter("catStattionHouseCode", obj.getCatstattionhousecode());
		}
		
		if(StringUtils.isNotBlank(obj.getKpiState())) {
			query.setParameter("kpiState", obj.getKpiState());
			queryCount.setParameter("kpiState", obj.getKpiState());
		}
		
		if(obj.getDateFrom()!=null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
		}
		
		if(obj.getDateTo()!=null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
		}
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	//huy-end
}
