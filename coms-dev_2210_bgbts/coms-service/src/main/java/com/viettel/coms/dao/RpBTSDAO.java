package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.ReportBTSByDADTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportEffectiveDTO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.RpKHBTSDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("rpBTSDAO")
public class RpBTSDAO extends BaseFWDAOImpl<WorkItemBO, Long> {

	public RpBTSDAO() {
		this.model = new WorkItemBO();
	}

	public RpBTSDAO(Session session) {
		this.session = session;
	}

	public List<RpBTSDTO> doSearchBTS(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder(" select "
				+ " rp.sys_group_name ChiNhanh,rp.cat_province_code provinceCode,rp.cat_station_house_code,rp.cnt_contract_code,"
				+ " 0 XDDaCoMb,0 XDCanGPXD,0 XDDaCoGPXD,0 XDChuaCo,"

				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1) is not null then 1 else 0 end XDDuDKNhanBGMB,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and (ass.Received_obstruct_date is not null or ass.Received_goods_date is not null or ass.Received_date is not null))"
				+ " is not null then 1 else 0 end XDDaNhanBGMB,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and (ass.Received_obstruct_date is null or ass.Received_goods_date is null or ass.Received_date is null))"
				+ " is not null then 1 else 0 end XDDuDKChuaDiNhan,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Out_of_date_received >0)"
				+ " is not null then 1 else 0 end quahan_BGMB_XD,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Starting_date is not null )"
				+ " is not null then 1 else 0 end XDDaVaoTK, "
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and (ass.Received_obstruct_date is not null or ass.Received_goods_date is not null or ass.Received_date is not null)"
				+ " and ass.Starting_date is null )is not null then 1 else 0 end XDNhanChuaTK,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Out_of_date_start_date is not null) is not null then 1 else 0 end quahan_khoicong_XD,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Construction_status=2) is not null then 1 else 0 end XDDangTKXDDoDang,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Construction_status=2) is not null then 1 else 0 end dangtrienkhai_XD,"

				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=3 and ass.Received_date is not null) is not null then 1 else 0 end CDNhanBGDiemDauNoi,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=3 and ass.Received_obstruct_date is not null) is not null then 1 else 0 end CDVuong,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=3 and ass.Starting_date is not null) is not null then 1 else 0 end CDDangTK,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=3 and ass.Starting_date is null) is not null then 1 else 0 end CDChuaTK,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=3 and ass.Construction_status=3) is not null then 1 else 0 end CDTCXongDien,"

				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=1 and ass.Construction_status =3) is not null"
				+ " and (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi,(select CONSTRUCTION_ID,work_item_id, max(decode(status,3,3,0))status from stock_trans group by CONSTRUCTION_ID,work_item_id) st"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id and ass.construction_id=st.construction_id and wi.work_item_id=st.work_item_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and st.status !=3) is not null then 1 else 0 end LDDuDKChuaCap,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi,(select CONSTRUCTION_ID,work_item_id, max(decode(status,3,3,0))status from stock_trans group by CONSTRUCTION_ID,work_item_id) st"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id and ass.construction_id=st.construction_id and wi.work_item_id=st.work_item_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and ass.Construction_status =3 and st.status =3) is not null then 1 else 0 end LDCapChuaLap,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and ass.Received_obstruct_date is not null) is not null then 1 else 0 end LDVuongLD,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and ass.Construction_status=2) is not null then 1 else 0 end LDDangLap,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and ass.Construction_status=3) is not null then 1 else 0 end LDTCXongLapDung,"

				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=2 and ass.Construction_status =3) is not null"
				+ " and (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi,(select CONSTRUCTION_ID,work_item_id, max(decode(status,3,3,0))status from stock_trans group by CONSTRUCTION_ID,work_item_id) st"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id and ass.construction_id=st.construction_id and wi.work_item_id=st.work_item_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=4 and st.status !=3) is not null then 1 else 0 end BTSDuDKChuaCapBTS,"

				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi,(select CONSTRUCTION_ID,work_item_id, max(decode(status,3,3,0))status from stock_trans group by CONSTRUCTION_ID,work_item_id) st"
				+ " where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id and ass.construction_id=st.construction_id and wi.work_item_id=st.work_item_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=4 and ass.Construction_status =3 and st.status =3) is not null then 1 else 0 end BTSCapChuaLap,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=4 and ass.Construction_status=2) is not null then 1 else 0 end BTSDangLap,"
				+ " case when (select max(ass.Assign_Handover_id) from Assign_Handover ass,work_item wi where ass.cat_station_house_id=rp.cat_station_house_id and ass.cnt_contract_id=rp.cnt_contract_id"
				+ " and ass.construction_id=wi.construction_id and wi.CAT_WORK_ITEM_GROUP_ID=4 and ass.Construction_status=3) is not null then 1 else 0 end BTSTCXongBTS,"

				+ " case when Complete_date is not null then 1 else 0 end TramXongDB,"
				+ " case when Construction_state >=1 then 1 else 0 end TCQuahan"

				+ " from RP_STATION_COMPLETE rp where 1=1 ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(CAT_STATION_HOUSE_CODE) LIKE upper(:keySearch) escape '&') ");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" and rp.SYS_GROUP_ID = :sysGroupId ");
		}
		if (obj.getCatProvinceId() != null) {
			sql.append(" and rp.CAT_PROVINCE_ID = :catProvinceId ");
		}
		if (StringUtils.isNotEmpty(obj.getContractCode())) {
			sql.append(" and rp.CNT_CONTRACT_CODE = :contractCode ");
		}
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("cat_station_house_code", new StringType());
		query.addScalar("cnt_contract_code", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("ChiNhanh", new StringType());
		query.addScalar("XDDaCoMb", new LongType());
		query.addScalar("XDCanGPXD", new LongType());
		query.addScalar("XDDaCoGPXD", new LongType());
		query.addScalar("XDChuaCo", new LongType());
		query.addScalar("XDDuDKNhanBGMB", new LongType());
		query.addScalar("XDDaNhanBGMB", new LongType());
		query.addScalar("XDDuDKChuaDiNhan", new LongType());
		query.addScalar("quahan_BGMB_XD", new LongType());
		query.addScalar("XDDaVaoTK", new LongType());
		query.addScalar("XDNhanChuaTK", new LongType());
		query.addScalar("quahan_khoicong_XD", new LongType());
		query.addScalar("XDDangTKXDDoDang", new LongType());
		query.addScalar("dangtrienkhai_XD", new LongType());

		query.addScalar("CDNhanBGDiemDauNoi", new LongType());
		query.addScalar("CDVuong", new LongType());
		query.addScalar("CDDangTK", new LongType());
		query.addScalar("CDChuaTK", new LongType());
		query.addScalar("CDTCXongDien", new LongType());

		query.addScalar("LDDuDKChuaCap", new LongType());
		query.addScalar("LDCapChuaLap", new LongType());
		query.addScalar("LDVuongLD", new LongType());
		query.addScalar("LDDangLap", new LongType());
		query.addScalar("LDTCXongLapDung", new LongType());

		query.addScalar("BTSDuDKChuaCapBTS", new LongType());
		query.addScalar("BTSCapChuaLap", new LongType());
		query.addScalar("BTSDangLap", new LongType());
		query.addScalar("BTSTCXongBTS", new LongType());

		query.addScalar("TramXongDB", new LongType());
		query.addScalar("TCQuahan", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getCatProvinceId() != null) {
			query.setParameter("catProvinceId", obj.getCatProvinceId());
			queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
		}
		if (StringUtils.isNotEmpty(obj.getContractCode())) {
			query.setParameter("contractCode", obj.getContractCode());
			queryCount.setParameter("contractCode", obj.getContractCode());
		}
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}
	// public List<RpBTSDTO> doSearchBTS(RpBTSDTO obj){
	// StringBuilder sql = new StringBuilder("with tbl as " +
	// "(select '1' type, " +
	// "'Tổng' ChiNhanh, " +
	// "'Tổng' provinceCode, " +
	// "count(stationCode) XDTongTram, " +
	// "sum(XDDaCoMb)XDDaCoMb, " +
	// "sum(XDCanGPXD)XDCanGPXD, " +
	// "sum(XDDaCoGPXD)XDDaCoGPXD, " +
	// "sum(XDCanGPXD)- sum(XDDaCoGPXD) XDChuaCo, " +
	// "sum(XDDuDKNhanBGMB) XDDuDKNhanBGMB, " +
	// "sum(XDDaNhanBGMB) XDDaNhanBGMB, " +
	// "sum(XDDuDKNhanBGMB)- sum(XDDaNhanBGMB) XDDuDKChuaDiNhan, " +
	// "sum(XDDaVaoTK) XDDaVaoTK, " +
	// "sum(XDNhanChuaTK) XDNhanChuaTK, " +
	// "sum(XDDangTKXDDoDang)XDDangTKXDDoDang, " +
	// "sum(XDTCQuaHan)XDTCQuaHan, " +
	// "sum(CDNhanBGDiemDauNoi) CDNhanBGDiemDauNoi, " +
	// "sum(CDVuong)CDVuong, " +
	// "sum(CDDangTK) CDDangTK, " +
	// "sum(CDChuaTK) CDChuaTK, " +
	// "sum(CDTCXongDien) CDTCXongDien, " +
	// "sum(LDDuDKChuaCap) LDDuDKChuaCap, " +
	// "sum(LDCapChuaLap) LDCapChuaLap, " +
	// "sum(LDVuongLD) LDVuongLD, " +
	// "sum(LDDangLap) LDDangLap, " +
	// "sum(LDTCXongLapDung) LDTCXongLapDung, " +
	// "sum(BTSDuDKChuaCapBTS)BTSDuDKChuaCapBTS, " +
	// "sum(BTSCapChuaLap)BTSCapChuaLap, " +
	// "sum(BTSDangLap) BTSDangLap, " +
	// "sum(BTSTCXongBTS) BTSTCXongBTS, " +
	// "sum(TramXongDB)TramXongDB " +
	// "from RP_BTS where 1=1 ");
	// // if (null != obj.getContractCodeLst() &&
	// obj.getContractCodeLst().size() > 0) {
	// sql.append(" and contractCode in (:contractCodeLst) ");
	// // }
	// if(null!=obj.getStationCodeLst() && obj.getStationCodeLst().size()>0) {
	// sql.append(" and stationCode in(:stationCodeLst) ");
	// }
	// sql.append(" union all " +
	// "select '2' type, " +
	// "sysGroupName ChiNhanh, " +
	// "sysGroupName provinceCode, " +
	// "count(stationCode) XDTongTram, " +
	// "sum(XDDaCoMb)XDDaCoMb, " +
	// "sum(XDCanGPXD)XDCanGPXD, " +
	// "sum(XDDaCoGPXD)XDDaCoGPXD, " +
	// "sum(XDCanGPXD)- sum(XDDaCoGPXD) XDChuaCo, " +
	// "sum(XDDuDKNhanBGMB) XDDuDKNhanBGMB, " +
	// "sum(XDDaNhanBGMB) XDDaNhanBGMB, " +
	// "sum(XDDuDKNhanBGMB)- sum(XDDaNhanBGMB) XDDuDKChuaDiNhan, " +
	// "sum(XDDaVaoTK) XDDaVaoTK, " +
	// "sum(XDNhanChuaTK) XDNhanChuaTK, " +
	// "sum(XDDangTKXDDoDang)XDDangTKXDDoDang, " +
	// "sum(XDTCQuaHan)XDTCQuaHan, " +
	// "sum(CDNhanBGDiemDauNoi) CDNhanBGDiemDauNoi, " +
	// "sum(CDVuong)CDVuong, " +
	// "sum(CDDangTK) CDDangTK, " +
	// "sum(CDChuaTK) CDChuaTK, " +
	// "sum(CDTCXongDien) CDTCXongDien, " +
	// "sum(LDDuDKChuaCap) LDDuDKChuaCap, " +
	// "sum(LDCapChuaLap) LDCapChuaLap, " +
	// "sum(LDVuongLD) LDVuongLD, " +
	// "sum(LDDangLap) LDDangLap, " +
	// "sum(LDTCXongLapDung) LDTCXongLapDung, " +
	// "sum(BTSDuDKChuaCapBTS)BTSDuDKChuaCapBTS, " +
	// "sum(BTSCapChuaLap)BTSCapChuaLap, " +
	// "sum(BTSDangLap) BTSDangLap, " +
	// "sum(BTSTCXongBTS) BTSTCXongBTS, " +
	// "sum(TramXongDB)TramXongDB " +
	// "from RP_BTS where 1=1 ");
	// // if (null != obj.getContractCodeLst() &&
	// obj.getContractCodeLst().size() > 0) {
	// sql.append(" and contractCode in (:contractCodeLst) ");
	// // }
	// if(null!=obj.getStationCodeLst() && obj.getStationCodeLst().size()>0) {
	// sql.append(" and stationCode in(:stationCodeLst) ");
	// }
	// sql.append(" group by sysGroupName " +
	// "union all " +
	// "select '3' type, " +
	// "sysGroupName ChiNhanh, " +
	// "provinceCode , " +
	// "count(stationCode) XDTongTram, " +
	// "sum(XDDaCoMb)XDDaCoMb, " +
	// "sum(XDCanGPXD)XDCanGPXD, " +
	// "sum(XDDaCoGPXD)XDDaCoGPXD, " +
	// "sum(XDCanGPXD)- sum(XDDaCoGPXD) XDChuaCo, " +
	// "sum(XDDuDKNhanBGMB) XDDuDKNhanBGMB, " +
	// "sum(XDDaNhanBGMB) XDDaNhanBGMB, " +
	// "sum(XDDuDKNhanBGMB)- sum(XDDaNhanBGMB) XDDuDKChuaDiNhan, " +
	// "sum(XDDaVaoTK) XDDaVaoTK, " +
	// "sum(XDNhanChuaTK) XDNhanChuaTK, " +
	// "sum(XDDangTKXDDoDang)XDDangTKXDDoDang, " +
	// "sum(XDTCQuaHan)XDTCQuaHan, " +
	// "sum(CDNhanBGDiemDauNoi) CDNhanBGDiemDauNoi, " +
	// "sum(CDVuong)CDVuong, " +
	// "sum(CDDangTK) CDDangTK, " +
	// "sum(CDChuaTK) CDChuaTK, " +
	// "sum(CDTCXongDien) CDTCXongDien, " +
	// "sum(LDDuDKChuaCap) LDDuDKChuaCap, " +
	// "sum(LDCapChuaLap) LDCapChuaLap, " +
	// "sum(LDVuongLD) LDVuongLD, " +
	// "sum(LDDangLap) LDDangLap, " +
	// "sum(LDTCXongLapDung) LDTCXongLapDung, " +
	// "sum(BTSDuDKChuaCapBTS)BTSDuDKChuaCapBTS, " +
	// "sum(BTSCapChuaLap)BTSCapChuaLap, " +
	// "sum(BTSDangLap) BTSDangLap, " +
	// "sum(BTSTCXongBTS) BTSTCXongBTS, " +
	// "sum(TramXongDB)TramXongDB " +
	// "from RP_BTS where 1=1 " );
	// // if (null != obj.getContractCodeLst() &&
	// obj.getContractCodeLst().size() > 0) {
	// sql.append(" and contractCode in (:contractCodeLst) ");
	// // }
	// if(null!=obj.getStationCodeLst() && obj.getStationCodeLst().size()>0) {
	// sql.append(" and stationCode in(:stationCodeLst) ");
	// }
	// sql.append(" group by sysGroupName,provinceCode) " +
	// "select * from tbl order by type,ChiNhanh,provinceCode");
	// StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
	// sqlCount.append(sql.toString());
	// sqlCount.append(")");
	// SQLQuery query = getSession().createSQLQuery(sql.toString());
	// SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
	// query.addScalar("provinceCode", new StringType());
	// query.addScalar("ChiNhanh", new StringType());
	// query.addScalar("XDTongTram", new LongType());
	// query.addScalar("XDDaCoMb", new LongType());
	// query.addScalar("TramXongDB", new LongType());
	// query.addScalar("BTSTCXongBTS", new LongType());
	// query.addScalar("BTSDangLap", new LongType());
	// query.addScalar("BTSCapChuaLap", new LongType());
	// query.addScalar("BTSDuDKChuaCapBTS", new LongType());
	// query.addScalar("LDTCXongLapDung", new LongType());
	// query.addScalar("LDVuongLD", new LongType());
	// query.addScalar("LDDangLap", new LongType());
	// query.addScalar("LDCapChuaLap", new LongType());
	// query.addScalar("LDDuDKChuaCap", new LongType());
	// query.addScalar("CDTCXongDien", new LongType());
	// query.addScalar("CDChuaTK", new LongType());
	// query.addScalar("CDDangTK", new LongType());
	// query.addScalar("CDVuong", new LongType());
	// query.addScalar("CDNhanBGDiemDauNoi", new LongType());
	// query.addScalar("XDTCQuaHan", new LongType());
	// query.addScalar("XDDangTKXDDoDang", new LongType());
	// query.addScalar("XDNhanChuaTK", new LongType());
	// query.addScalar("XDDaVaoTK", new LongType());
	// query.addScalar("XDDuDKChuaDiNhan", new LongType());
	// query.addScalar("XDDaNhanBGMB", new LongType());
	// query.addScalar("XDDuDKNhanBGMB", new LongType());
	// query.addScalar("XDChuaCo", new LongType());
	// query.addScalar("XDDaCoGPXD", new LongType());
	// query.addScalar("XDCanGPXD", new LongType());
	//
	// query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));
	// // if (null != obj.getContractCodeLst() &&
	// obj.getContractCodeLst().size() > 0) {
	// query.setParameterList("contractCodeLst", obj.getContractCodeLst());
	// queryCount.setParameterList("contractCodeLst", obj.getContractCodeLst());
	// // }
	// if (null != obj.getStationCodeLst() && obj.getStationCodeLst().size() >
	// 0) {
	// query.setParameterList("stationCodeLst", obj.getStationCodeLst());
	// queryCount.setParameterList("stationCodeLst", obj.getStationCodeLst());
	// }
	// if (obj.getPageSize() != null) {
	// query.setFirstResult((obj.getPage().intValue() - 1) *
	// obj.getPageSize().intValue());
	// query.setMaxResults(obj.getPageSize().intValue());
	// }
	// obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
	//
	// return query.list();
	//
	// }

	// Huypq-20191126-start
	public List<RpBTSDTO> getDataChart(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "sst.CONSTRUCTION_ID constructionId, " + "cons.sys_group_id sysGroupId " + "FROM SYN_STOCK_TRANS sst "
				+ "left join CONSTRUCTION cons " + "on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "where sst.CONSTRUCTION_ID is not null " + "and cons.sys_group_id is not null "
				+ "and cons.sys_group_id != -1 " + "and cons.status < 3 " + "group by sst.CONSTRUCTION_ID, "
				+ "cons.sys_group_id " + ") " + "select count(*) countConstruction, " + "sysGroupId, "
				+ "(select code from sys_group  " + "  where sys_group.SYS_GROUP_ID = ( "
				+ "    select SUBSTR(sg2.PATH, 9, 6) from CTCT_CAT_OWNER.SYS_GROUP sg2  "
				+ "      where sg2.SYS_GROUP_ID = tbl.sysGroupId " + "    ) " + "    and (sys_group.code LIKE '%XC%' "
				+ "          OR sys_group.code LIKE '%TTKT%') " + "        AND sys_group.GROUP_LEVEL =2 "
				+ ") sysGroupCode " + "from tbl tbl " + "WHERE TRUNC(sysdate) - TRUNC(transDate)>=45 ");
		if (obj.getSysGroupId() != null) {
			sql.append("and sysGroupId = :id ");
		}
		sql.append("group by sysGroupId");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("countConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("id", obj.getSysGroupId());
			queryCount.setParameter("id", obj.getSysGroupId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<RpBTSDTO> getListTtkt() {
		StringBuilder sql = new StringBuilder("select code sysGroupCode,  " + "SYS_GROUP_ID sysGroupId,  "
				+ "NAME sysGroupName  " + "from CTCT_CAT_OWNER.SYS_GROUP  " + "where (sys_group.code LIKE '%CNCT%') "
				+ "        AND sys_group.GROUP_LEVEL =2" + " ORDER BY code asc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		return query.list();
	}

	public List<RpBTSDTO> doSearchKpi(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT "
				+ "            MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "            sst.CONSTRUCTION_ID constructionId, " + "            cons.sys_group_id sysGroupId, "
				+ "            cons.code constructionCode, "
				+ "            sum(nvl(tds.UNIT_PRICE,0) * nvl(tds.AMOUNT,0)) sumValue " + "        FROM "
				+ "            SYN_STOCK_TRANS sst  " + "        left join " + "            CONSTRUCTION cons  "
				+ "                on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "        left join SYN_STOCK_TRANS_DETAIL_SERIAL tds "
				+ "        on tds.SYN_STOCK_TRANS_ID = sst.SYN_STOCK_TRANS_ID " + "        left join CONSTRUCTION cons "
				+ "        on sst.construction_id = cons.construction_id" + "        where "
				+ "            sst.CONSTRUCTION_ID is not null  " + "            and cons.sys_group_id is not null  "
				+ "            and cons.sys_group_id != -1  " + "            and cons.status < 3  "
				+ "        group by " + "            sst.CONSTRUCTION_ID, "
				+ "            cons.sys_group_id, cons.code )  " + "             " + "            select "
				+ "            constructionId constructionId, " + "            sysGroupId sysGroupId, "
				+ "			 constructionCode constructionCode," + "            ROUND(sumValue) valueConstruction, "
				+ "            (select " + "                code  " + "            from "
				+ "                sys_group     " + "            where "
				+ "                sys_group.SYS_GROUP_ID = ( " + "                    select "
				+ "                        SUBSTR(sg2.PATH, " + "                        9, "
				+ "                        6)  " + "                    from "
				+ "                        CTCT_CAT_OWNER.SYS_GROUP sg2         " + "                    where "
				+ "                        sg2.SYS_GROUP_ID = tbl.sysGroupId      " + "                )      "
				+ "                and ( " + "                    sys_group.code LIKE '%XC%'            "
				+ "                    OR sys_group.code LIKE '%TTKT%' " + "                )          "
				+ "                AND sys_group.GROUP_LEVEL =2  " + "            ) sysGroupCode  " + "        from "
				+ "            tbl tbl " + "        WHERE " + "            TRUNC(sysdate) - TRUNC(transDate)>=45");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND sysGroupId = :sysId ");
		}

		if (obj.getConstructionId() != null) {
			sql.append(" AND constructionId = : consId ");
		}

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("constructionId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("valueConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("constructionCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysId", obj.getSysGroupId());
			queryCount.setParameter("sysId", obj.getSysGroupId());
		}

		if (obj.getConstructionId() != null) {
			query.setParameter("consId", obj.getConstructionId());
			queryCount.setParameter("consId", obj.getConstructionId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	// Huy-end

	// tatph-start-6/2/2019
	public List<RpBTSDTO> doSearchKpi60days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT "
				+ "            MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "            sst.CONSTRUCTION_ID constructionId, " + "            cons.sys_group_id sysGroupId, "
				+ "            cons.code constructionCode, "
				+ "            sum(nvl(tds.UNIT_PRICE,0) * nvl(tds.AMOUNT,0)) sumValue " + "        	 FROM "
				+ "            SYN_STOCK_TRANS sst  " + "        	 left join " + "            CONSTRUCTION cons  "
				+ "            on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "        	 left join SYN_STOCK_TRANS_DETAIL_SERIAL tds "
				+ "        	 on tds.SYN_STOCK_TRANS_ID = sst.SYN_STOCK_TRANS_ID "
				+ "        	 left join CONSTRUCTION cons " + "        	 on sst.construction_id = cons.construction_id"
				+ "        	 where " + "            sst.CONSTRUCTION_ID is not null  "
				+ "            and cons.sys_group_id is not null  " + "            and cons.sys_group_id != -1  "
				+ "            and cons.status < 5  " + "        	 group by " + "            sst.CONSTRUCTION_ID, "
				+ "            cons.sys_group_id, cons.code )  " + "             " + "            select "
				+ "            constructionId constructionId, " + "            sysGroupId sysGroupId, "
				+ "			 constructionCode constructionCode," + "            ROUND(sumValue) valueConstruction, "
				+ "            (select " + "                code  " + "            from "
				+ "                sys_group     " + "            where "
				+ "                sys_group.SYS_GROUP_ID = ( " + "                    select "
				+ "                        SUBSTR(sg2.PATH, " + "                        9, "
				+ "                        6)  " + "                    from "
				+ "                        CTCT_CAT_OWNER.SYS_GROUP sg2         " + "                    where "
				+ "                        sg2.SYS_GROUP_ID = tbl.sysGroupId      " + "                )      "
				+ "                and ( " + "                    sys_group.code LIKE '%XC%'            "
				+ "                    OR sys_group.code LIKE '%TTKT%' " + "                )          "
				+ "                AND sys_group.GROUP_LEVEL =2  " + "            ) sysGroupCode  " + "        	 from "
				+ "            tbl tbl " + "       	 WHERE " + "            TRUNC(sysdate) - TRUNC(transDate)>=60");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND sysGroupId = :sysId ");
		}

		if (obj.getConstructionId() != null) {
			sql.append(" AND constructionId = : consId ");
		}

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("constructionId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("valueConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("constructionCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysId", obj.getSysGroupId());
			queryCount.setParameter("sysId", obj.getSysGroupId());
		}

		if (obj.getConstructionId() != null) {
			query.setParameter("consId", obj.getConstructionId());
			queryCount.setParameter("consId", obj.getConstructionId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<RpBTSDTO> getDataChart60days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "sst.CONSTRUCTION_ID constructionId, " + "cons.sys_group_id sysGroupId " + "FROM SYN_STOCK_TRANS sst "
				+ "left join CONSTRUCTION cons " + "on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "where sst.CONSTRUCTION_ID is not null " + "and cons.sys_group_id is not null "
				+ "and cons.sys_group_id != -1 " + "and cons.status < 5 " + "group by sst.CONSTRUCTION_ID, "
				+ "cons.sys_group_id " + ") " + "select count(*) countConstruction, " + "sysGroupId, "
				+ "(select code from sys_group  " + "  where sys_group.SYS_GROUP_ID = ( "
				+ "    select SUBSTR(sg2.PATH, 9, 6) from CTCT_CAT_OWNER.SYS_GROUP sg2  "
				+ "      where sg2.SYS_GROUP_ID = tbl.sysGroupId " + "    ) " + "    and (sys_group.code LIKE '%XC%' "
				+ "          OR sys_group.code LIKE '%TTKT%') " + "        AND sys_group.GROUP_LEVEL =2 "
				+ ") sysGroupCode " + "from tbl tbl " + "WHERE TRUNC(sysdate) - TRUNC(transDate)>=60 ");
		if (obj.getSysGroupId() != null) {
			sql.append("and sysGroupId = :id ");
		}
		sql.append("group by sysGroupId");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("countConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("id", obj.getSysGroupId());
			queryCount.setParameter("id", obj.getSysGroupId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	// <---------------------------------------------------------------------------------------------------------->
	public List<RpBTSDTO> doSearchKpi135days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT "
				+ "            MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "            sst.CONSTRUCTION_ID constructionId, " + "            cons.sys_group_id sysGroupId, "
				+ "            cons.code constructionCode, "
				+ "            sum(nvl(tds.UNIT_PRICE,0) * nvl(tds.AMOUNT,0)) sumValue " + "        	 FROM "
				+ "            SYN_STOCK_TRANS sst  " + "        	 left join " + "            CONSTRUCTION cons  "
				+ "            on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "        	 left join SYN_STOCK_TRANS_DETAIL_SERIAL tds "
				+ "        	 on tds.SYN_STOCK_TRANS_ID = sst.SYN_STOCK_TRANS_ID "
				+ "        	 left join CONSTRUCTION cons " + "        	 on sst.construction_id = cons.construction_id"
				+ "       	 where " + "            sst.CONSTRUCTION_ID is not null  "
				+ "            and cons.sys_group_id is not null  " + "            and cons.sys_group_id != -1  "
				+ "            and cons.status < 8  " + "        	 group by " + "            sst.CONSTRUCTION_ID, "
				+ "            cons.sys_group_id, cons.code )  " + "             " + "            select "
				+ "            constructionId constructionId, " + "            sysGroupId sysGroupId, "
				+ "			 constructionCode constructionCode," + "            ROUND(sumValue) valueConstruction, "
				+ "            (select " + "                code  " + "            from "
				+ "                sys_group     " + "            where "
				+ "                sys_group.SYS_GROUP_ID = ( " + "                    select "
				+ "                        SUBSTR(sg2.PATH, " + "                        9, "
				+ "                        6)  " + "                    from "
				+ "                        CTCT_CAT_OWNER.SYS_GROUP sg2         " + "                    where "
				+ "                        sg2.SYS_GROUP_ID = tbl.sysGroupId      " + "                )      "
				+ "                and ( " + "                    sys_group.code LIKE '%XC%'            "
				+ "                    OR sys_group.code LIKE '%TTKT%' " + "                )          "
				+ "                AND sys_group.GROUP_LEVEL =2  " + "            ) sysGroupCode  " + "        	 from "
				+ "            tbl tbl " + "        	 WHERE "
				+ "            TRUNC(sysdate) - TRUNC(transDate)>=135");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND sysGroupId = :sysId ");
		}

		if (obj.getConstructionId() != null) {
			sql.append(" AND constructionId = : consId ");
		}

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("constructionId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("valueConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("constructionCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysId", obj.getSysGroupId());
			queryCount.setParameter("sysId", obj.getSysGroupId());
		}

		if (obj.getConstructionId() != null) {
			query.setParameter("consId", obj.getConstructionId());
			queryCount.setParameter("consId", obj.getConstructionId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<RpBTSDTO> getDataChart135days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT MAX(sst.REAL_IE_TRANS_DATE) transDate, "
				+ "sst.CONSTRUCTION_ID constructionId, " + "cons.sys_group_id sysGroupId " + "FROM SYN_STOCK_TRANS sst "
				+ "left join CONSTRUCTION cons " + "on sst.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "where sst.CONSTRUCTION_ID is not null " + "and cons.sys_group_id is not null "
				+ "and cons.sys_group_id != -1 " + "and cons.status < 8 " + "group by sst.CONSTRUCTION_ID, "
				+ "cons.sys_group_id " + ") " + "select count(*) countConstruction, " + "sysGroupId, "
				+ "(select code from sys_group  " + "  where sys_group.SYS_GROUP_ID = ( "
				+ "    select SUBSTR(sg2.PATH, 9, 6) from CTCT_CAT_OWNER.SYS_GROUP sg2  "
				+ "      where sg2.SYS_GROUP_ID = tbl.sysGroupId " + "    ) " + "    and (sys_group.code LIKE '%XC%' "
				+ "          OR sys_group.code LIKE '%TTKT%') " + "        AND sys_group.GROUP_LEVEL =2 "
				+ ") sysGroupCode " + "from tbl tbl " + "WHERE TRUNC(sysdate) - TRUNC(transDate)>=135 ");
		if (obj.getSysGroupId() != null) {
			sql.append("and sysGroupId = :id ");
		}
		sql.append("group by sysGroupId");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("countConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("id", obj.getSysGroupId());
			queryCount.setParameter("id", obj.getSysGroupId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<ManageVttbDTO> doSearchCongNoTonVTAC_CT(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("with t1 as(select  " + "t2.code constructionCode, "
				+ "t1.project_code projectCode, " + "t1.real_ie_trans_date realIeTransDate, "
				+ "t1.contract_code contractCode, " + "t3.name sysGroupName, " + "t1.code pxkCode, "
				+ "t9.CAT_PROVINCE_ID catProvinceId, " + "t4.goods_code vttbCode, " + "t4.goods_name vttbName, "
				+ "t4.goods_unit_name dvt, " + "nvl(t4.amount_real,0) soLuongDDK , "
				+ "nvl(t5.UNIT_PRICE * t4.amount_real,0) ttDDK , "
				+ "nvl(t4.REAL_RECIEVE_AMOUNT * t5.unit_price,0) ttNTK, "
				+ "nvl(t4.REAL_RECIEVE_AMOUNT,0) soLuongNTK , " + "nvl(t6.SO_LUONG,0) soLuongQT , "
				+ "nvl(t6.THANH_TIEN,0) ttQT , "
				+ "(nvl(t4.amount_real,0) + nvl(t4.REAL_RECIEVE_AMOUNT,0) - nvl(t6.SO_LUONG,0)) soLuongTCK , "
				+ "(nvl(t5.UNIT_PRICE * t4.amount_real,0) + nvl(t4.REAL_RECIEVE_AMOUNT * t5.unit_price,0) - nvl(t6.THANH_TIEN,0)) ttTCK "
				+ "from SYN_STOCK_TRANS t1 "
				+ "left join CONSTRUCTION t2 on t1.CONSTRUCTION_ID = t2.CONSTRUCTION_ID    "
				+ "left join SYS_GROUP t3 on t2.SYS_GROUP_ID = t3.SYS_GROUP_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL t4 on t1.SYN_STOCK_TRANS_ID = t4.SYN_STOCK_TRANS_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL_SERIAL t5 on t1.SYN_STOCK_TRANS_ID = t5.SYN_STOCK_TRANS_ID "
				+ "left join MANAGE_HCQT t6 on t1.CONSTRUCTION_CODE = t6.CONSTRUCTION_CODE and t6.vttb_code = t4.GOODS_CODE "
				+ " left join CAT_STATION t8 on t2.cat_station_id = t8.CAT_STATION_ID  "
				+ " left join CAT_PRovince t9 on t8.CAT_PROVINCE_ID = t9.CAT_PROVINCE_ID  )," + "t2 as(select  "
				+ "sum(t1.soLuongDDK) soLuongDDK, " + "sum(t1.ttDDK) ttDDK, " + "sum(t1.soLuongNTK) soLuongNTK, "
				+ "sum(t1.ttNTK) ttNTK, " + "sum(t1.soLuongQT) soLuongQT, " + "sum(t1.ttQT) ttQT, "
				+ "sum(t1.soLuongTCK) soLuongTCK, " + "sum(t1.ttTCK) ttTCK, " + "t1.constructionCode constructionCode, "
				+ "t1.realIeTransDate realIeTransDate, " + "t1.projectCode projectCode, "
				+ "t1.contractCode contractCode, " + "t1.sysGroupName sysGroupName, "
				+ "t1.catProvinceId catProvinceId, " + "t1.pxkCode pxkCode, " + "t1.vttbCode vttbCode, "
				+ "t1.vttbName vttbName, " + "t1.dvt dvt " + "from t1 " + "group by t1.constructionCode , "
				+ "t1.projectCode , " + "t1.contractCode , "
				+ "t1.pxkCode ,dvt,vttbName,vttbCode,sysGroupName,realIeTransDate,catProvinceId ) " + "select  "
				+ "t2.soLuongDDK soLuongDDK , " + "t2.ttDDK ttDDK , " + "t2.soLuongNTK soLuongNTK , "
				+ "t2.ttNTK ttNTK , " + "t2.soLuongQT soLuongQT , " + "t2.ttQT ttQT , " + "t2.soLuongTCK soLuongTCK , "
				+ "t2.ttTCK ttTCK , " + "t2.constructionCode constructionCode, " + "t2.projectCode projectCode, "
				+ "t2.contractCode contractCode, " + "t2.pxkCode pxkCode, " + "t2.dvt dvt, " + "t2.vttbName vttbName, "
				+ "t2.vttbCode vttbCode, " + "t2.sysGroupName sysGroupName, " + "t2.realIeTransDate realIeTransDate "
				+ "from t2  " + "where 1 = 1 " + "and t2.constructionCode is not null "
				+ "and t2.contractCode is not null ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(t2.constructionCode) LIKE upper(:keySearch) " + " escape '&') ");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			sql.append(
					" AND  t2.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  t2.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append(" AND t2.catProvinceId in (:provinceIds) ");
		}
		sql.append(
				" order by t2.constructionCode , " + "t2.projectCode , " + "t2.contractCode , " + "t2.pxkCode desc ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("soLuongDDK", new LongType());
		query.addScalar("ttDDK", new LongType());
		query.addScalar("soLuongNTK", new LongType());
		query.addScalar("ttNTK", new LongType());
		query.addScalar("soLuongQT", new LongType());
		query.addScalar("ttQT", new LongType());
		query.addScalar("soLuongTCK", new LongType());
		query.addScalar("ttTCK", new LongType());

		query.addScalar("constructionCode", new StringType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("pxkCode", new StringType());
		query.addScalar("dvt", new StringType());
		query.addScalar("vttbName", new StringType());
		query.addScalar("vttbCode", new StringType());
		query.addScalar("sysGroupName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}

	public List<ManageVttbDTO> doSearchCongNoTonVTAC_TH(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("with t1 as(select  " + "t2.code constructionCode, "
				+ "t2.status constructionStatus, " + "t1.project_code projectCode, "
				+ "t1.real_ie_trans_date realIeTransDate, " + "t1.contract_code contractCode, "
				+ "t7.contract_type contractType, " + "t7.CONTRACT_TYPE_O contractTypeO, "
				+ "t7.CONTRACT_TYPE_OS_NAME contractTypeOsName, " + "t9.CAT_PROVINCE_ID catProvinceId, "
				+ "t3.name sysGroupName, " + "nvl(t4.amount_real,0) soLuongDDK , "
				+ "nvl(t5.UNIT_PRICE * t4.amount_real,0) ttDDK , "
				+ "nvl(t4.REAL_RECIEVE_AMOUNT * t5.unit_price,0) ttNTK, "
				+ "nvl(t4.REAL_RECIEVE_AMOUNT,0) soLuongNTK , " + "nvl(t6.SO_LUONG,0) soLuongQT , "
				+ "nvl(t6.THANH_TIEN,0) ttQT , "
				+ "(nvl(t4.amount_real,0) + nvl(t4.REAL_RECIEVE_AMOUNT,0) - nvl(t6.SO_LUONG,0)) soLuongTCK , "
				+ "(nvl(t5.UNIT_PRICE * t4.amount_real,0) + nvl(t4.REAL_RECIEVE_AMOUNT * t5.unit_price,0) - nvl(t6.THANH_TIEN,0)) ttTCK "
				+ "from SYN_STOCK_TRANS t1 " + "left join CONSTRUCTION t2 on t1.CONSTRUCTION_ID = t2.CONSTRUCTION_ID   "
				+ "left join SYS_GROUP t3 on t2.SYS_GROUP_ID = t3.SYS_GROUP_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL t4 on t1.SYN_STOCK_TRANS_ID = t4.SYN_STOCK_TRANS_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL_SERIAL t5 on t1.SYN_STOCK_TRANS_ID = t5.SYN_STOCK_TRANS_ID "
				+ "left join MANAGE_HCQT t6 on t1.CONSTRUCTION_CODE = t6.CONSTRUCTION_CODE and t6.vttb_code = t4.GOODS_CODE "
				+ "left join CNT_CONTRACT t7 on t1.CONTRACT_CODE = t7.CODE " +

				" left join CAT_STATION t8 on t2.cat_station_id = t8.CAT_STATION_ID  "
				+ " left join CAT_PRovince t9 on t8.CAT_PROVINCE_ID = t9.CAT_PROVINCE_ID  )," +

				"t2 as(select  " + "sum(t1.soLuongDDK) soLuongDDK, " + "sum(t1.ttDDK) ttDDK, "
				+ "sum(t1.soLuongNTK) soLuongNTK, " + "sum(t1.ttNTK) ttNTK, " + "sum(t1.soLuongQT) soLuongQT, "
				+ "sum(t1.ttQT) ttQT, " + "sum(t1.soLuongTCK) soLuongTCK, " + "sum(t1.ttTCK) ttTCK, "
				+ "t1.constructionCode constructionCode, " + "t1.projectCode projectCode, "
				+ "t1.contractCode contractCode, " + "t1.sysGroupName sysGroupName, " + "t1.contractType contractType, "
				+ "t1.contractTypeO contractTypeO, " + "t1.contractTypeOsName contractTypeOsName, "
				+ "t1.realIeTransDate realIeTransDate, " + "t1.catProvinceId catProvinceId, " + "t1.constructionStatus "
				+ "from t1 " + "group by t1.constructionCode , " + "t1.projectCode , " + "t1.contractCode , "
				+ "sysGroupName, " + "contractType, " + "constructionStatus, " + "contractTypeO, " + "catProvinceId, "
				+ "contractTypeOsName," + "realIeTransDate) " + "select  " + "t2.soLuongDDK soLuongDDK , "
				+ "t2.ttDDK ttDDK , " + "t2.soLuongNTK soLuongNTK , " + "t2.ttNTK ttNTK , "
				+ "t2.soLuongQT soLuongQT , " + "t2.ttQT ttQT , " + "t2.soLuongTCK soLuongTCK , " + "t2.ttTCK ttTCK , "
				+ "t2.constructionCode constructionCode, " + "t2.projectCode projectCode, "
				+ "t2.contractCode contractCode, " + "t2.sysGroupName sysGroupName, " + "t2.contractType contractType, "
				+ "t2.constructionStatus constructionStatus, " + "t2.contractTypeO contractTypeO, "
				+ "t2.realIeTransDate realIeTransDate, " + "t2.contractTypeOsName contractTypeOsName " + "from t2  "
				+ "where 1 = 1 " + "and t2.constructionCode is not null " + "and t2.contractCode is not null ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(t2.constructionCode) LIKE upper(:keySearch) escape '&') ");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			sql.append(
					" AND  t2.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  t2.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append(" AND t2.catProvinceId in (:provinceIds) ");
		}
		sql.append(" order by t2.constructionCode , " + "t2.projectCode , " + "t2.contractCode desc ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("soLuongDDK", new LongType());
		query.addScalar("ttDDK", new LongType());
		query.addScalar("soLuongNTK", new LongType());
		query.addScalar("ttNTK", new LongType());
		query.addScalar("soLuongQT", new LongType());
		query.addScalar("ttQT", new LongType());
		query.addScalar("soLuongTCK", new LongType());
		query.addScalar("ttTCK", new LongType());

		query.addScalar("constructionCode", new StringType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("contractType", new LongType());
		query.addScalar("contractTypeO", new LongType());
		query.addScalar("contractTypeOsName", new StringType());
		query.addScalar("constructionStatus", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}

	public List<ManageVttbDTO> doSearchTongHopVTTB_TH(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("with t1 as(select " + "t3.code constructionCode, "
				+ "t1.real_ie_trans_date realIeTransDate, " + "t1.contract_code contractCode, "
				+ "t2.contract_type contractType, " + "t2.contract_type_o contractTypeO, "
				+ "t2.contract_type_os_name contractTypeOsName, " + "t4.code stationCode , "
				+ "t8.cat_province_id catProvinceId , " + "t3.status constructionStatus, "
				+ "nvl((t5.REAL_RECIEVE_AMOUNT * t6.unit_price),0) giaTriXacNhan, "
				+ "nvl((t5.AMOUNT_REAL * t6.unit_price),0) giaTriPxk, " + "t7.name sysGroupName "
				+ "from SYN_STOCK_TRANS t1 " + "left join CNT_CONTRACT t2 on t1.contract_code = t2.code "
				+ "left join CONSTRUCTION t3 on t1.CONSTRUCTION_ID = t3.CONSTRUCTION_ID "
				+ "left join CAT_STATION t4 on t3.CAT_STATION_ID = t4.CAT_STATION_ID "
				+ "left join CAT_PROVINCE t8 on t4.CAT_PROVINCE_ID = t8.CAT_PROVINCE_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL t5 on t1.SYN_STOCK_TRANS_ID = t5.SYN_STOCK_TRANS_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL_SERIAL t6 on t1.SYN_STOCK_TRANS_ID = t6.SYN_STOCK_TRANS_ID "
				+ "left join SYS_GROUP t7 on t3.sys_group_id = t7.sys_group_id) " + "select  "
				+ "sum(t1.giaTriXacNhan) giaTriXacNhan, " + "sum(t1.giaTriPxk) giaTriPxk, "
				+ "t1.constructionCode constructionCode, " + "t1.catProvinceId catProvinceId, "
				+ "t1.realIeTransDate realIeTransDate, " + "t1.contractCode contractCode, "
				+ "t1.contractType contractType, " + "t1.stationCode stationCode , "
				+ "t1.constructionStatus constructionStatus , " + "t1.sysGroupName sysGroupName, "
				+ "t1.contractTypeO contractTypeO, " + "t1.contractTypeOsName contractTypeOsName " + "from t1  "
				+ "where 1 = 1 " + "and t1.constructionCode is not null " + "and t1.contractCode is not null ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(t1.constructionCode) LIKE upper(:keySearch) escape '&') ");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			sql.append(
					" AND  t1.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  t1.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append(" AND t1.catProvinceId in (:provinceIds) ");
		}
		sql.append("group by " + "t1.constructionCode , " + "t1.contractCode , " + "t1.contractType , "
				+ "t1.stationCode  , " + "t1.constructionStatus , " + "t1.sysGroupName, " + "t1.catProvinceId, "
				+ "t1.contractTypeO , " + "t1.contractTypeOsName , " + "t1.realIeTransDate  " + "order by  "
				+ "constructionCode asc");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("giaTriPxk", new LongType());
		query.addScalar("giaTriXacNhan", new LongType());

		query.addScalar("constructionCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("contractType", new LongType());
		query.addScalar("contractTypeO", new LongType());
		query.addScalar("contractTypeOsName", new StringType());
		query.addScalar("constructionStatus", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("sysGroupName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}

	public List<ManageVttbDTO> doSearchTongHopVTTB_CT(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("with t1 as(select " + "t3.code constructionCode, "
				+ "t1.contract_code contractCode, " + "t1.real_ie_trans_date realIeTransDate, "
				+ "t2.contract_type contractType, " + "t2.contract_type_o contractTypeO, "
				+ "t2.contract_type_os_name contractTypeOsName, " + "t4.code stationCode , "
				+ "t8.cat_province_id catProvinceId , " + "t3.status constructionStatus, "
				+ "nvl(t5.REAL_RECIEVE_AMOUNT,0) giaTriXacNhan, " + "nvl(t5.AMOUNT_REAL,0) giaTriPxk, "
				+ "t7.name sysGroupName, " + "t1.code pxkCode , "
				+ "to_char(t1.REAL_IE_TRANS_DATE,'dd/MM/yyyy') ngayXN, " + "t1.REAL_IE_USER_NAME nguoiXN, "
				+ "t5.goods_code vttbCode, " + "t5.goods_name vttbName, "
				+ "to_char(t1.created_date,'dd/MM/yyyy') pxkDateS " + "from SYN_STOCK_TRANS t1 "
				+ "left join CNT_CONTRACT t2 on t1.contract_code = t2.code "
				+ "left join CONSTRUCTION t3 on t1.CONSTRUCTION_ID = t3.CONSTRUCTION_ID "
				+ "left join CAT_STATION t4 on t3.CAT_STATION_ID = t4.CAT_STATION_ID "
				+ "left join CAT_PROVINCE t8 on t4.CAT_PROVINCE_ID = t8.CAT_PROVINCE_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL t5 on t1.SYN_STOCK_TRANS_ID = t5.SYN_STOCK_TRANS_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL_SERIAL t6 on t1.SYN_STOCK_TRANS_ID = t6.SYN_STOCK_TRANS_ID "
				+ "left join SYS_GROUP t7 on t3.sys_group_id = t7.sys_group_id) "
				+ "select t1.constructionCode constructionCode, " + "t1.contractCode contractCode, "
				+ "t1.catProvinceId catProvinceId, " + "t1.realIeTransDate realIeTransDate, "
				+ "t1.contractType contractType, " + "t1.contractTypeO contractTypeO, "
				+ "t1.contractTypeOsName contractTypeOsName, " + "t1.stationCode stationCode , "
				+ "t1.constructionStatus constructionStatus, " + "sum(t1.giaTriXacNhan) giaTriXacNhan, "
				+ "sum(t1.giaTriPxk) giaTriPxk, " + "(sum(t1.giaTriPxk) - sum(t1.giaTriXacNhan)) giaTriChenhLech, "
				+ "t1.sysGroupName sysGroupName, " + "t1.pxkCode pxkCode , " + "t1.ngayXN ngayXN, "
				+ "t1.nguoiXN nguoiXN, " + "t1.vttbCode vttbCode, " + "t1.vttbName vttbName, "
				+ "t1.pxkDateS pxkDateS  " + "from t1 " + "where 1 = 1 " + "and t1.constructionCode is not null "
				+ "and t1.contractCode is not null ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(t1.constructionCode) LIKE upper(:keySearch) escape '&') ");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			sql.append(
					" AND  t1.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  t1.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append(" AND t1.catProvinceId in (:provinceIds) ");
		}
		sql.append("group by  " + "t1.constructionCode , " + "t1.contractCode , " + "t1.contractType , "
				+ "t1.catProvinceId , " + "t1.contractTypeO , " + "t1.contractTypeOsName , " + "t1.stationCode  , "
				+ "t1.constructionStatus , " + "t1.sysGroupName , " + "t1.pxkCode  , " + "t1.ngayXN , "
				+ "t1.nguoiXN , " + "t1.vttbCode , " + "t1.vttbName , " + "t1.realIeTransDate , " + "t1.pxkDateS  "
				+ "order by constructionCode asc");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("giaTriPxk", new LongType());
		query.addScalar("giaTriXacNhan", new LongType());
		query.addScalar("giaTriChenhLech", new LongType());

		query.addScalar("constructionCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("contractType", new LongType());
		query.addScalar("contractTypeO", new LongType());
		query.addScalar("contractTypeOsName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionStatus", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("pxkCode", new StringType());
		query.addScalar("ngayXN", new StringType());
		query.addScalar("pxkDateS", new StringType());
		query.addScalar("nguoiXN", new StringType());
		query.addScalar("vttbCode", new StringType());
		query.addScalar("vttbName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}

	public List<ManageVttbDTO> doSearchTongHopPXK(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("with t1 as(select  " + "t2.code constructionCode, " + "t1.code pxkCode, "
				+ "t1.real_ie_trans_date realIeTransDate, " + "to_char(t1.created_date,'dd/MM/yyyy') pxkDateS , "
				+ "t1.stock_code stockCode, " + "t3.name sysGroupName , " + "t4.code stationCode , "
				+ "t5.code provinceCode, " + "t5.cat_province_id catProvinceId, " + "t1.contract_code contractCode, "
				+ "t6.CODE catStationHouseCode, " + "t7.goods_code vttbCode, " + "t7.goods_name vttbName, "
				+ "t8.serial serial, " + "t7.goods_unit_name dvt, " + "t1.status status, "
				+ "nvl(t8.unit_price,0) donGia , " + "nvl(t7.amount_order,0) soLuongPxk, "
				+ "nvl(t7.amount_real,0) soLuongSuDung, "
				+ "(nvl(t7.amount_real,0) * nvl(t8.unit_price,0)) giaTriSuDung " + "from SYN_STOCK_TRANS t1  "
				+ "left join CONSTRUCTION t2 on t1.CONSTRUCTION_ID = t2.CONSTRUCTION_ID "
				+ "left join sys_group t3 on t2.SYS_GROUP_ID = t3.SYS_GROUP_ID "
				+ "left join cat_station t4 on t2.CAT_STATION_ID = t4.CAT_STATION_ID "
				+ "left join CAT_PROVINCE t5 on t4.cat_province_id = t5.CAT_PROVINCE_ID "
				+ "left join CTCT_CAT_OWNER.CAT_STATION_HOUSE t6 on t4.CAT_STATION_HOUSE_ID = t6.CAT_STATION_TYPE_HOUSE_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL t7 on t1.SYN_STOCK_TRANS_ID = t7.SYN_STOCK_TRANS_ID "
				+ "left join SYN_STOCK_TRANS_DETAIL_SERIAL t8 on t1.SYN_STOCK_TRANS_ID = t8.SYN_STOCK_TRANS_ID "
				+ "where t1.type = 2 and t1.CONFIRM = 1)" + "select  " + "t1.donGia donGia, "
				+ "sum(t1.soLuongPxk) soLuongPxk , " + "sum(t1.soLuongSuDung) soLuongSuDung, "
				+ "sum(t1.giaTriSuDung) giaTriSuDung , " + "t1.constructionCode constructionCode, "
				+ "t1.pxkCode pxkCode, " + "t1.catProvinceId catProvinceId, " + "t1.pxkDateS pxkDateS, "
				+ "t1.stockCode stockCode, " + "t1.sysGroupName sysGroupName , " + "t1.stationCode stationCode , "
				+ "t1.provinceCode provinceCode, " + "t1.contractCode contractCode, "
				+ "t1.catStationHouseCode catStationHouseCode, " + "t1.vttbCode vttbCode, " + "t1.vttbName vttbName, "
				+ "t1.serial serial, " + "t1.dvt dvt, " + "t1.realIeTransDate realIeTransDate, " + "t1.status status "
				+ "from t1 " + "where 1 = 1 " + "and t1.constructionCode is not null "
				+ "and t1.contractCode is not null ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(t1.constructionCode) LIKE upper(:keySearch) escape '&') ");
		}
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			sql.append(
					" AND  t1.realIeTransDate >= TO_DATE(:startDate,'dd/MM/yyyy')  AND  t1.realIeTransDate <= TO_DATE(:endDate,'dd/MM/yyyy') ");
		}
		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append("  AND t1.catProvinceId in (:provinceIds)");
		}
		sql.append("GROUP BY  " + "t1.constructionCode , " + "t1.pxkCode , " + "t1.pxkDateS , " + "t1.stockCode , "
				+ "t1.sysGroupName  , " + "t1.stationCode  , " + "t1.provinceCode , " + "t1.contractCode , "
				+ "t1.catStationHouseCode , " + "t1.vttbCode , " + "t1.vttbName , " + "t1.serial , " + "t1.dvt , "
				+ "t1.status,  " + "t1.donGia,  " + "t1.catProvinceId,  " + "t1.realIeTransDate  "
				+ "order by pxkCode asc");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("donGia", new LongType());
		query.addScalar("soLuongPxk", new LongType());
		query.addScalar("soLuongSuDung", new LongType());
		query.addScalar("giaTriSuDung", new LongType());

		query.addScalar("constructionCode", new StringType());
		query.addScalar("pxkCode", new StringType());
		query.addScalar("pxkDateS", new StringType());
		query.addScalar("stockCode", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("catStationHouseCode", new StringType());
		query.addScalar("vttbCode", new StringType());
		query.addScalar("vttbName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("dvt", new StringType());
		query.addScalar("status", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));
		if (StringUtils.isNotEmpty(obj.getStartDate()) && StringUtils.isNotEmpty(obj.getEndDate())) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();

	}

	public List<RpBTSDTO> getDataChartPxk7days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder().append(" with tbl as (select Max(t1.REAL_IE_TRANS_DATE) transDate, "
				+ "              trunc(sysdate) - trunc(Max(t1.REAL_IE_TRANS_DATE)) ngayQuaHan, "
				+ "              t1.stock_code stockCode, " + "              t1.code pxkCode, "
				+ "              t1.contract_code contractCode, "
				+ "              t1.construction_code constructionCode, " + "              t2.name sysGroupName, "
				+ "              t1.sys_group_id sysGroupId, " + "              t3.email email, "
				+ "              t3.phone_number phoneNumber , " + "              t1.status status , "
				+ "              t1.real_ie_user_name transMan " + "              from SYN_STOCK_TRANS t1 "
				+ "              left join sys_group t2 on t1.sys_group_id = t2.sys_group_id "
				+ "              left join sys_user t3 on t1.real_ie_user_id = t3.sys_user_id "
				+ "			   left join CONSTRUCTION cons   " + "              on t1.CONSTRUCTION_CODE = cons.CODE   "
				+ "              where t1.CONSTRUCTION_CODE is not null   "
				+ "              and cons.sys_group_id is not null   " + "              and cons.sys_group_id != -1   "
				+ "              and cons.status < 5   " + "              and  t1.type = 2   "
				+ "              group by  " + "              t1.stock_code , " + "              t1.code , "
				+ "              t1.contract_code , " + "              t1.construction_code , "
				+ "              t2.name , " + "              t1.sys_group_id, " + "              t3.email , "
				+ "              t3.phone_number  , " + "              t1.status  , "
				+ "              t1.real_ie_user_name " + "				)    "
				+ "				select count(*) countConstruction,    " + "				sysGroupId,    "
				+ "				(select code from sys_group     "
				+ "				  where sys_group.SYS_GROUP_ID = (    "
				+ "				    select SUBSTR(sg2.PATH, 9, 6) from CTCT_CAT_OWNER.SYS_GROUP sg2     "
				+ "				      where sg2.SYS_GROUP_ID = tbl.sysGroupId    " + "				    )    "
				+ "				    and (sys_group.code LIKE '%CNCT%')    "
				+ "				        AND sys_group.GROUP_LEVEL =2    " + "				) sysGroupCode    "
				+ "				from tbl tbl    " + "				WHERE TRUNC(sysdate) - TRUNC(transDate)>=7 ");
		if (obj.getSysGroupId() != null) {
			sql.append(" and sysGroupId = :id ");
		}
		sql.append(" group by sysGroupId");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("countConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("id", obj.getSysGroupId());
			queryCount.setParameter("id", obj.getSysGroupId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<ManageVttbDTO> doSearchPxk7days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder(
				"with t1 as(select Max(to_char(t1.REAL_IE_TRANS_DATE,'dd/MM/yyyy')) transDate, "
						+ "              trunc(sysdate) - trunc(Max(t1.REAL_IE_TRANS_DATE)) ngayQuaHan, "
						+ "              t1.stock_code stockCode, " + "              t1.code pxkCode, "
						+ "              t1.contract_code contractCode, "
						+ "              t1.construction_code constructionCode, "
						+ "              t2.name sysGroupName, " + "              t1.sys_group_id sysGroupId, "
						+ "              t3.email email, " + "              t3.phone_number phoneNumber , "
						+ "              t1.status status , " + "              t1.real_ie_user_name transMan "
						+ "              from SYN_STOCK_TRANS t1 "
						+ "              left join sys_group t2 on t1.sys_group_id = t2.sys_group_id "
						+ "              left join sys_user t3 on t1.real_ie_user_id = t3.sys_user_id "
						+ "			   left join CONSTRUCTION cons   "
						+ "              on t1.CONSTRUCTION_CODE = cons.CODE   "
						+ "              where t1.CONSTRUCTION_CODE is not null   "
						+ "              and cons.sys_group_id is not null   "
						+ "              and cons.sys_group_id != -1   " + "              and cons.status < 5   "
						+ "              and t1.type = 2   " + "              group by  "
						+ "              t1.stock_code , " + "              t1.code , "
						+ "              t1.contract_code , " + "              t1.construction_code , "
						+ "              t2.name , " + "              t1.sys_group_id, " + "              t3.email , "
						+ "              t3.phone_number  , " + "              t1.status  , "
						+ "              t1.real_ie_user_name ) " + "              select "
						+ "              transDate , " + "              ngayQuaHan , " + "              stockCode , "
						+ "              pxkCode , " + "              contractCode , "
						+ "              constructionCode, " + "              sysGroupName, "
						+ "              sysGroupId, " + "              email, " + "              phoneNumber, "
						+ "              status, " + "              transMan " + "              from t1 "
						+ "              where ngayQuaHan >= 7");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("transDate", new StringType());
		query.addScalar("ngayQuaHan", new StringType());
		query.addScalar("stockCode", new StringType());
		query.addScalar("pxkCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("transMan", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));

		return query.list();
	}

	public List<RpBTSDTO> getDataChartVttb45days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder().append(" with tbl as (select Max(t1.REAL_IE_TRANS_DATE) transDate,"
				+ "              trunc(sysdate) - trunc(Max(t1.REAL_IE_TRANS_DATE)) ngayQuaHan,"
				+ "              t1.stock_code stockCode," + "              t1.code pxkCode,"
				+ "              t1.contract_code contractCode,"
				+ "              t1.construction_code constructionCode," + "              t2.name sysGroupName,"
				+ "              t1.sys_group_id sysGroupId," + "              t3.email email,"
				+ "              t3.phone_number phoneNumber ," + "              t1.status status ,"
				+ "              t1.real_ie_user_name transMan," + "              t4.goods_code vttbCode,"
				+ "              t4.goods_name vttbName" + "              from SYN_STOCK_TRANS t1"
				+ "              left join sys_group t2 on t1.sys_group_id = t2.sys_group_id"
				+ "              left join sys_user t3 on t1.real_ie_user_id = t3.sys_user_id"
				+ "              left join SYN_STOCK_TRANS_DETAIL t4 on t1.syn_stock_trans_id = t4.syn_stock_trans_id "
				+ "			   left join CONSTRUCTION cons   " + "              on t1.CONSTRUCTION_CODE = cons.CODE   "
				+ "              where t1.CONSTRUCTION_CODE is not null   "
				+ "              and cons.sys_group_id is not null   " + "              and cons.sys_group_id != -1   "
				+ "              and cons.status < 3   " + "              and  t1.type = 2   "
				+ "              group by " + "              t1.stock_code ," + "              t1.code ,"
				+ "              t1.contract_code ," + "              t1.construction_code ,"
				+ "              t2.name ," + "              t1.sys_group_id," + "              t3.email ,"
				+ "              t3.phone_number  ," + "              t1.status  ,"
				+ "              t1.real_ie_user_name," + "              t4.goods_code," + "              t4.goods_name"
				+ "				)   " + "				select count(*) countConstruction,   "
				+ "				sysGroupId,   " + "				(select code from sys_group    "
				+ "				  where sys_group.SYS_GROUP_ID = (   "
				+ "				    select SUBSTR(sg2.PATH, 9, 6) from CTCT_CAT_OWNER.SYS_GROUP sg2    "
				+ "				      where sg2.SYS_GROUP_ID = tbl.sysGroupId   " + "				    )   "
				+ "				    and (sys_group.code LIKE '%CNCT%')   "
				+ "				        AND sys_group.GROUP_LEVEL =2   " + "				) sysGroupCode   "
				+ "				from tbl tbl   " + "				WHERE TRUNC(sysdate) - TRUNC(transDate)>=45 ");
		if (obj.getSysGroupId() != null) {
			sql.append("and sysGroupId = :id ");
		}
		sql.append("group by sysGroupId");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("countConstruction", new LongType());
		query.addScalar("sysGroupCode", new StringType());

		if (obj.getSysGroupId() != null) {
			query.setParameter("id", obj.getSysGroupId());
			queryCount.setParameter("id", obj.getSysGroupId());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpBTSDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<ManageVttbDTO> doSearchVttb45days(RpBTSDTO obj) {
		StringBuilder sql = new StringBuilder(
				"with t1 as(select Max(to_char(t1.REAL_IE_TRANS_DATE,'dd/MM/yyyy')) transDate, "
						+ "              trunc(sysdate) - trunc(Max(t1.REAL_IE_TRANS_DATE)) ngayQuaHan, "
						+ "              t1.stock_code stockCode, " + "              t1.code pxkCode, "
						+ "              t1.contract_code contractCode, "
						+ "              t1.construction_code constructionCode, "
						+ "              t2.name sysGroupName, " + "              t1.sys_group_id sysGroupId, "
						+ "              t3.email email, " + "              t3.phone_number phoneNumber , "
						+ "              t1.status status , " + "              t1.real_ie_user_name transMan, "
						+ "              t4.goods_code vttbCode, " + "              t4.goods_name vttbName "
						+ "              from SYN_STOCK_TRANS t1 "
						+ "              left join sys_group t2 on t1.sys_group_id = t2.sys_group_id "
						+ "              left join sys_user t3 on t1.real_ie_user_id = t3.sys_user_id "
						+ "              left join SYN_STOCK_TRANS_DETAIL t4 on t1.syn_stock_trans_id = t4.syn_stock_trans_id "
						+ "			   left join CONSTRUCTION cons   "
						+ "              on t1.CONSTRUCTION_CODE = cons.CODE   "
						+ "              where t1.CONSTRUCTION_CODE is not null   "
						+ "              and cons.sys_group_id is not null   "
						+ "              and cons.sys_group_id != -1   " + "              and cons.status < 5   "
						+ "              and t1.type = 2   " + "              group by  "
						+ "              t1.stock_code , " + "              t1.code , "
						+ "              t1.contract_code , " + "              t1.construction_code , "
						+ "              t2.name , " + "              t1.sys_group_id, " + "              t3.email , "
						+ "              t3.phone_number  , " + "              t1.status  , "
						+ "              t1.real_ie_user_name, " + "              t4.goods_code , "
						+ "              t4.goods_name ) " + "              select " + "              transDate , "
						+ "              ngayQuaHan , " + "              stockCode , " + "              pxkCode , "
						+ "              contractCode , " + "              constructionCode, "
						+ "              sysGroupName, " + "              sysGroupId, " + "              email, "
						+ "              phoneNumber, " + "              status, " + "              transMan , "
						+ "              vttbCode, " + "              vttbName " + "              from t1 "
						+ "              where ngayQuaHan >= 45");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("transDate", new StringType());
		query.addScalar("ngayQuaHan", new StringType());
		query.addScalar("stockCode", new StringType());
		query.addScalar("pxkCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("transMan", new StringType());
		query.addScalar("vttbCode", new StringType());
		query.addScalar("vttbName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ManageVttbDTO.class));

		return query.list();
	}

	// tatph-end-6/2/2019

	// Huypq-09072020-start
	// Tổng hợp thanh toán
	public List<ReportDTO> doSearchRpGeneralPaymentCtv(ReportDTO obj, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder("SELECT d.CAT_PROVINCE_ID, d.code provinceCode, "
				+ "  c.employee_code employeeCode, " + "  c.full_name fullName, " + "  c.phone_number phoneNumber, "
				+ "  b.employee_code employeeCodeCTV, " + "  b.full_name fullNameCTV, " + "  b.tax_code cmtnd, "
				+ "  b.account_number vtpay, " +
				// " SUM(e.CONTRACT_PRICE) giaTruocThue, " +
				// " SUM(e.CONTRACT_ROSE) hoaHong, " +
				"  round((SUM(e.CONTRACT_PRICE)/1.1),2) giaTruocThue, "
				+ "  round((SUM(e.CONTRACT_PRICE)/1.1 * MAX(nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT')))),2) hoaHong, "
				+ "  (round((SUM(e.CONTRACT_PRICE)/1.1 * MAX(nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT')))),2)*0.1) thueTNCN, "
				+ "  (round((SUM(e.CONTRACT_PRICE)/1.1 * MAX(nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT')))),2)*0.9) thucNhan "
				+ "  ,a.STATUS status " + "FROM TANGENT_CUSTOMER a " + "INNER JOIN sys_user b "
				+ "ON a.CREATED_USER=b.sys_user_id " + "AND b.type_user in (1,2) " + "INNER JOIN sys_user c "
				+ "ON b.PARENT_USER_ID=c.sys_user_id " + "INNER JOIN cat_province d "
				+ "ON a.province_id=d.CAT_PROVINCE_ID " + "INNER JOIN RESULT_SOLUTION e "
				+ "ON a.TANGENT_CUSTOMER_id                         =e.TANGENT_CUSTOMER_id "
				+ "AND e.CONTRACT_code                             IS NOT NULL "
				+ " inner join CNT_CONTRACT cnt on e.CONTRACT_id=cnt.CNT_CONTRACT_id and cnt.CHECK_WO_KC_XDDD =1 "
				+ " INNER JOIN CTCT_COMS_OWNER.WO wo ON e.CONTRACT_CODE = wo.CONTRACT_CODE AND wo.CAT_WORK_ITEM_TYPE_ID=2563 AND wo.STATE = 'OK' "
				+ " WHERE 1=1 " + "AND a.STATUS in (8) ");
		if (StringUtils.isNotBlank(obj.getThang())) {
			// sql.append(" AND TO_CHAR(a.APPOVED_DATE,'MM/yyyy') =:thang ");
			sql.append(" AND (CASE WHEN NVL(e.UPDATED_DATE,e.created_date) > wo.UPDATE_TTHT_APPROVE_WO THEN TO_CHAR(NVL(e.UPDATED_DATE,e.created_date),'MM/yyyy') ELSE TO_CHAR(wo.UPDATE_TTHT_APPROVE_WO,'MM/yyyy') END) =:thang ");
		}

		if (obj.getDateFrom() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) >= :dateFrom ");
			sql.append(" AND nvl(e.UPDATED_DATE,e.created_date) >= :dateFrom ");
		}

		if (obj.getDateTo() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) <= :dateTo ");
			sql.append(" AND TRUNC(nvl(e.UPDATED_DATE,e.created_date)) <= :dateTo ");
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(b.employee_code) LIKE upper(:keySearch) escape '&' "
					+ "OR upper(b.full_name) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.tax_code) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.account_number) LIKE upper(:keySearch)  escape '&' ) ");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND d.code =:provinceCode ");
		}

		sql.append(" AND d.CAT_PROVINCE_ID in (:groupIdList) ");

		sql.append(" GROUP BY d.CAT_PROVINCE_ID, d.code, " + "  c.employee_code, " + "  c.full_name, "
				+ "  c.phone_number, " + "  b.employee_code , " + "  b.full_name , " + "  b.tax_code, "
				+ "  b.account_number,a.STATUS " + "  ORDER BY d.code ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		StringBuilder sqlSumGiaTruocThue = new StringBuilder("SELECT round(NVL(sum(giaTruocThue), 0),0) FROM (");
		sqlSumGiaTruocThue.append(sql.toString());
		sqlSumGiaTruocThue.append(")");

		StringBuilder sqlSumHoaHong = new StringBuilder("SELECT round(NVL(sum(hoaHong), 0),0) FROM (");
		sqlSumHoaHong.append(sql.toString());
		sqlSumHoaHong.append(")");

		StringBuilder sqlSumThueTNCN = new StringBuilder("SELECT round(NVL(sum(thueTNCN), 0),0) FROM (");
		sqlSumThueTNCN.append(sql.toString());
		sqlSumThueTNCN.append(")");

		StringBuilder sqlSumThucNhan = new StringBuilder("SELECT round(NVL(sum(thucNhan), 0),0) FROM (");
		sqlSumThucNhan.append(sql.toString());
		sqlSumThucNhan.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		SQLQuery querySumGiaTruocThue = getSession().createSQLQuery(sqlSumGiaTruocThue.toString());
		SQLQuery querySumHoaHong = getSession().createSQLQuery(sqlSumHoaHong.toString());
		SQLQuery querySumThueTNCN = getSession().createSQLQuery(sqlSumThueTNCN.toString());
		SQLQuery querySumThucNhan = getSession().createSQLQuery(sqlSumThucNhan.toString());

		// query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("employeeCodeCTV", new StringType());
		query.addScalar("fullNameCTV", new StringType());
		query.addScalar("cmtnd", new StringType());
		query.addScalar("vtpay", new StringType());
		query.addScalar("giaTruocThue", new DoubleType());
		query.addScalar("hoaHong", new DoubleType());
		query.addScalar("thueTNCN", new DoubleType());
		query.addScalar("thucNhan", new DoubleType());
		query.addScalar("status", new StringType());

		if (StringUtils.isNotBlank(obj.getThang())) {
			query.setParameter("thang", obj.getThang());
			queryCount.setParameter("thang", obj.getThang());
			querySumGiaTruocThue.setParameter("thang", obj.getThang());
			querySumHoaHong.setParameter("thang", obj.getThang());
			querySumThueTNCN.setParameter("thang", obj.getThang());
			querySumThucNhan.setParameter("thang", obj.getThang());
		}

		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
			querySumGiaTruocThue.setParameter("dateFrom", obj.getDateFrom());
			querySumHoaHong.setParameter("dateFrom", obj.getDateFrom());
			querySumThueTNCN.setParameter("dateFrom", obj.getDateFrom());
			querySumThucNhan.setParameter("dateFrom", obj.getDateFrom());
		}

		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
			querySumGiaTruocThue.setParameter("dateTo", obj.getDateTo());
			querySumHoaHong.setParameter("dateTo", obj.getDateTo());
			querySumThueTNCN.setParameter("dateTo", obj.getDateTo());
			querySumThucNhan.setParameter("dateTo", obj.getDateTo());
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumGiaTruocThue.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumHoaHong.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumThueTNCN.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumThucNhan.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
			querySumGiaTruocThue.setParameter("provinceCode", obj.getProvinceCode());
			querySumHoaHong.setParameter("provinceCode", obj.getProvinceCode());
			querySumThueTNCN.setParameter("provinceCode", obj.getProvinceCode());
			querySumThucNhan.setParameter("provinceCode", obj.getProvinceCode());
		}

		query.setParameterList("groupIdList", groupIdList);
		queryCount.setParameterList("groupIdList", groupIdList);
		querySumGiaTruocThue.setParameterList("groupIdList", groupIdList);
		querySumHoaHong.setParameterList("groupIdList", groupIdList);
		querySumThueTNCN.setParameterList("groupIdList", groupIdList);
		querySumThucNhan.setParameterList("groupIdList", groupIdList);

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<ReportDTO> ls = query.list();
		if (ls.size() > 0) {
			BigDecimal totalGiaTruocThue = (BigDecimal) querySumGiaTruocThue.uniqueResult();
			ls.get(0).setSumGiaTruocThue(totalGiaTruocThue.doubleValue());

			BigDecimal totalHoaHong = (BigDecimal) querySumHoaHong.uniqueResult();
			ls.get(0).setSumHoaHong(totalHoaHong.doubleValue());

			BigDecimal totalThueTNCN = (BigDecimal) querySumThueTNCN.uniqueResult();
			ls.get(0).setSumThueTNCN(totalThueTNCN.doubleValue());

			BigDecimal totalThucNhan = (BigDecimal) querySumThucNhan.uniqueResult();
			ls.get(0).setSoTienChuyenChu(totalThucNhan.toString());
			ls.get(0).setSumThucNhan(totalThucNhan.doubleValue());
		}
		return ls;
	}

	// Chi tiết HĐ
	public List<ReportDTO> doSearchRpDetailContractCtv(ReportDTO obj, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder("SELECT d.CAT_PROVINCE_ID, d.code provinceCode, "
				+ "  c.employee_code employeeCode, " + "  c.full_name fullName, " + "  c.phone_number phoneNumber, "
				+ "  b.employee_code employeeCodeCTV, " + "  b.full_name fullNameCTV, " + "  b.tax_code cmtnd, "
				+ "  b.account_number vtpay, " + "  e.CONTRACT_code contractCode, " +
				// " e.CONTRACT_PRICE giaTruocThue, " +
				// " e.CONTRACT_ROSE hoaHong, " +
				"  round((e.CONTRACT_PRICE/1.1),2) giaTruocThue, "
				+ "  round((e.CONTRACT_PRICE/1.1 * nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT'))),2) hoaHong, "
				+ "  round((e.CONTRACT_PRICE/1.1 * nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT'))),2)*0.1 thueTNCN, "
				+ "  round((e.CONTRACT_PRICE/1.1 * nvl(cnt.ROSE_CONTRACT/100,(select code from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE ='ROSE_CONTRACT'))),2)*0.9 thucNhan, "
				+ "  TO_CHAR(add_months(e.SIGN_DATE,1),'MM/yyyy') thang "
				+ "  ,a.status status, a.DISTRICT_NAME districtName " + "FROM TANGENT_CUSTOMER a "
				+ "INNER JOIN sys_user b " + "ON a.CREATED_USER=b.sys_user_id " + "AND b.type_user in (1,2) "
				+ "INNER JOIN sys_user c " + "ON b.PARENT_USER_ID=c.sys_user_id " + "INNER JOIN cat_province d "
				+ "ON a.province_id=d.CAT_PROVINCE_ID " + "INNER JOIN RESULT_SOLUTION e "
				+ "ON a.TANGENT_CUSTOMER_id                         =e.TANGENT_CUSTOMER_id "
				+ "AND e.CONTRACT_code                             IS NOT NULL "
				+ " inner join CNT_CONTRACT cnt on e.CONTRACT_id=cnt.CNT_CONTRACT_id and cnt.CHECK_WO_KC_XDDD =1 "
				+ " INNER JOIN CTCT_COMS_OWNER.WO wo ON e.CONTRACT_CODE = wo.CONTRACT_CODE AND wo.CAT_WORK_ITEM_TYPE_ID=2563 AND wo.STATE = 'OK' "
				+ "WHERE 1=1 " + "AND a.STATUS in (8) ");

		// if(StringUtils.isNotBlank(obj.getThang())) {
		// sql.append(" AND TO_CHAR(a.APPOVED_DATE,'MM/yyyy') =:thang ");
		// }
		//
		// if(obj.getDateFrom()!=null) {
		// sql.append(" AND TRUNC(a.APPOVED_DATE) >= :dateFrom ");
		// }
		//
		// if(obj.getDateTo()!=null) {
		// sql.append(" AND TRUNC(a.APPOVED_DATE) <= :dateTo ");
		// }

		if (StringUtils.isNotBlank(obj.getThang())) {
			// sql.append(" AND TO_CHAR(a.APPOVED_DATE,'MM/yyyy') =:thang ");
			sql.append(" AND (CASE WHEN NVL(e.UPDATED_DATE,e.created_date) > wo.UPDATE_TTHT_APPROVE_WO THEN TO_CHAR(NVL(e.UPDATED_DATE,e.created_date),'MM/yyyy') ELSE TO_CHAR(wo.UPDATE_TTHT_APPROVE_WO,'MM/yyyy') END) =:thang ");
		}

		if (obj.getDateFrom() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) >= :dateFrom ");
			sql.append(" AND nvl(e.UPDATED_DATE,e.created_date) >= :dateFrom ");
		}

		if (obj.getDateTo() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) <= :dateTo ");
			sql.append(" AND TRUNC(nvl(e.UPDATED_DATE,e.created_date)) <= :dateTo ");
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(b.employee_code) LIKE upper(:keySearch) escape '&' "
					+ "OR upper(b.full_name) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.tax_code) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.account_number) LIKE upper(:keySearch)  escape '&' ) ");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND d.code =:provinceCode ");
		}

		sql.append(" AND d.CAT_PROVINCE_ID in (:groupIdList) ");

		sql.append("  ORDER BY d.code ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		StringBuilder sqlSumGiaTruocThue = new StringBuilder("SELECT round(NVL(sum(giaTruocThue), 0), 0) FROM (");
		sqlSumGiaTruocThue.append(sql.toString());
		sqlSumGiaTruocThue.append(")");

		StringBuilder sqlSumHoaHong = new StringBuilder("SELECT round(NVL(sum(hoaHong), 0), 0) FROM (");
		sqlSumHoaHong.append(sql.toString());
		sqlSumHoaHong.append(")");

		StringBuilder sqlSumThueTNCN = new StringBuilder("SELECT round(NVL(sum(thueTNCN), 0), 0) FROM (");
		sqlSumThueTNCN.append(sql.toString());
		sqlSumThueTNCN.append(")");

		StringBuilder sqlSumThucNhan = new StringBuilder("SELECT round(NVL(sum(thucNhan), 0), 0) FROM (");
		sqlSumThucNhan.append(sql.toString());
		sqlSumThucNhan.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		SQLQuery querySumGiaTruocThue = getSession().createSQLQuery(sqlSumGiaTruocThue.toString());
		SQLQuery querySumHoaHong = getSession().createSQLQuery(sqlSumHoaHong.toString());
		SQLQuery querySumThueTNCN = getSession().createSQLQuery(sqlSumThueTNCN.toString());
		SQLQuery querySumThucNhan = getSession().createSQLQuery(sqlSumThucNhan.toString());

		// query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("employeeCodeCTV", new StringType());
		query.addScalar("fullNameCTV", new StringType());
		query.addScalar("cmtnd", new StringType());
		query.addScalar("vtpay", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("giaTruocThue", new DoubleType());
		query.addScalar("hoaHong", new DoubleType());
		query.addScalar("thueTNCN", new DoubleType());
		query.addScalar("thucNhan", new DoubleType());
		query.addScalar("thang", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("districtName", new StringType());

		if (StringUtils.isNotBlank(obj.getThang())) {
			query.setParameter("thang", obj.getThang());
			queryCount.setParameter("thang", obj.getThang());
			querySumGiaTruocThue.setParameter("thang", obj.getThang());
			querySumHoaHong.setParameter("thang", obj.getThang());
			querySumThueTNCN.setParameter("thang", obj.getThang());
			querySumThucNhan.setParameter("thang", obj.getThang());
		}

		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
			querySumGiaTruocThue.setParameter("dateFrom", obj.getDateFrom());
			querySumHoaHong.setParameter("dateFrom", obj.getDateFrom());
			querySumThueTNCN.setParameter("dateFrom", obj.getDateFrom());
			querySumThucNhan.setParameter("dateFrom", obj.getDateFrom());
		}

		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
			querySumGiaTruocThue.setParameter("dateTo", obj.getDateTo());
			querySumHoaHong.setParameter("dateTo", obj.getDateTo());
			querySumThueTNCN.setParameter("dateTo", obj.getDateTo());
			querySumThucNhan.setParameter("dateTo", obj.getDateTo());
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumGiaTruocThue.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumHoaHong.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumThueTNCN.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumThucNhan.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
			querySumGiaTruocThue.setParameter("provinceCode", obj.getProvinceCode());
			querySumHoaHong.setParameter("provinceCode", obj.getProvinceCode());
			querySumThueTNCN.setParameter("provinceCode", obj.getProvinceCode());
			querySumThucNhan.setParameter("provinceCode", obj.getProvinceCode());
		}

		query.setParameterList("groupIdList", groupIdList);
		queryCount.setParameterList("groupIdList", groupIdList);
		querySumGiaTruocThue.setParameterList("groupIdList", groupIdList);
		querySumHoaHong.setParameterList("groupIdList", groupIdList);
		querySumThueTNCN.setParameterList("groupIdList", groupIdList);
		querySumThucNhan.setParameterList("groupIdList", groupIdList);

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<ReportDTO> ls = query.list();
		if (ls.size() > 0) {
			BigDecimal totalGiaTruocThue = (BigDecimal) querySumGiaTruocThue.uniqueResult();
			ls.get(0).setSumGiaTruocThue(totalGiaTruocThue.doubleValue());

			BigDecimal totalHoaHong = (BigDecimal) querySumHoaHong.uniqueResult();
			ls.get(0).setSumHoaHong(totalHoaHong.doubleValue());

			BigDecimal totalThueTNCN = (BigDecimal) querySumThueTNCN.uniqueResult();
			ls.get(0).setSumThueTNCN(totalThueTNCN.doubleValue());

			BigDecimal totalThucNhan = (BigDecimal) querySumThucNhan.uniqueResult();
			ls.get(0).setSoTienChuyenChu(totalThucNhan.toString());
			ls.get(0).setSumThucNhan(totalThucNhan.doubleValue());
		}
		return ls;
	}

	// Chuyển tiền CTV
	public List<ReportDTO> doSearchRpTranfersCtv(ReportDTO obj, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder("SELECT d.CAT_PROVINCE_ID,d.code provinceCode, "
				+ "  b.employee_code employeeCodeCTV, " + "  b.full_name fullNameCTV, " + "  b.account_number vtpay, "
				+ "  SUM(e.CONTRACT_ROSE*0.9) soTien, " + "  'Thanh toán hoa hồng môi giới tháng ' "
				+ "  ||MAX(TO_CHAR(nvl(e.UPDATED_DATE,e.created_date),'MM/yyyy') ) tinNhan "
				+ "FROM TANGENT_CUSTOMER a " + "INNER JOIN sys_user b " + "ON a.CREATED_USER=b.sys_user_id "
				+ "AND b.type_user in (1,2) " + "INNER JOIN cat_province d " + "ON a.province_id=d.CAT_PROVINCE_ID "
				+ "INNER JOIN RESULT_SOLUTION e "
				+ "ON a.TANGENT_CUSTOMER_id                         =e.TANGENT_CUSTOMER_id "
				+ "AND e.CONTRACT_code                             IS NOT NULL "
				+ " inner join CNT_CONTRACT cnt on e.CONTRACT_id=cnt.CNT_CONTRACT_id and cnt.CHECK_WO_KC_XDDD =1 "
				+ " INNER JOIN CTCT_COMS_OWNER.WO wo ON e.CONTRACT_CODE = wo.CONTRACT_CODE AND wo.CAT_WORK_ITEM_TYPE_ID=2563 AND wo.STATE = 'OK' "
				+ " WHERE 1=1 AND a.STATUS in (8) ");

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(b.employee_code) LIKE upper(:keySearch) escape '&' "
					+ "OR upper(b.full_name) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.tax_code) LIKE upper(:keySearch)  escape '&' "
					+ "OR upper(b.account_number) LIKE upper(:keySearch)  escape '&' ) ");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND d.code =:provinceCode ");
		}

		sql.append(" AND d.CAT_PROVINCE_ID in (:groupIdList) ");

		// if(StringUtils.isNotBlank(obj.getThang())) {
		// sql.append(" AND TO_CHAR(a.APPOVED_DATE,'MM/yyyy') =:thang ");
		// }
		//
		// if(obj.getDateFrom()!=null) {
		// sql.append(" AND TRUNC(a.APPOVED_DATE) >= :dateFrom ");
		// }
		//
		// if(obj.getDateTo()!=null) {
		// sql.append(" AND TRUNC(a.APPOVED_DATE) <= :dateTo ");
		// }

		if (StringUtils.isNotBlank(obj.getThang())) {
			// sql.append(" AND TO_CHAR(a.APPOVED_DATE,'MM/yyyy') =:thang ");
			sql.append(" AND (CASE WHEN NVL(e.UPDATED_DATE,e.created_date) > wo.UPDATE_TTHT_APPROVE_WO THEN TO_CHAR(NVL(e.UPDATED_DATE,e.created_date),'MM/yyyy') ELSE TO_CHAR(wo.UPDATE_TTHT_APPROVE_WO,'MM/yyyy') END) =:thang ");
		}

		if (obj.getDateFrom() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) >= :dateFrom ");
			sql.append(" AND nvl(e.UPDATED_DATE,e.created_date) >= :dateFrom ");
		}

		if (obj.getDateTo() != null) {
			// sql.append(" AND TRUNC(a.APPOVED_DATE) <= :dateTo ");
			sql.append(" AND TRUNC(nvl(e.UPDATED_DATE,e.created_date)) <= :dateTo ");
		}

		sql.append("  GROUP BY d.CAT_PROVINCE_ID,d.code , " + "  b.employee_code , " + "  b.full_name , "
				+ "  b.account_number " + "ORDER BY d.code ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		StringBuilder sqlSumGiaTruocThue = new StringBuilder("SELECT round(NVL(sum(soTien), 0), 0) FROM (");
		sqlSumGiaTruocThue.append(sql.toString());
		sqlSumGiaTruocThue.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		SQLQuery querySumGiaTruocThue = getSession().createSQLQuery(sqlSumGiaTruocThue.toString());

		query.addScalar("provinceCode", new StringType());
		query.addScalar("employeeCodeCTV", new StringType());
		query.addScalar("fullNameCTV", new StringType());
		query.addScalar("vtpay", new StringType());
		query.addScalar("soTien", new DoubleType());
		query.addScalar("tinNhan", new StringType());

		if (StringUtils.isNotBlank(obj.getThang())) {
			query.setParameter("thang", obj.getThang());
			queryCount.setParameter("thang", obj.getThang());
			querySumGiaTruocThue.setParameter("thang", obj.getThang());
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			querySumGiaTruocThue.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
			querySumGiaTruocThue.setParameter("provinceCode", obj.getProvinceCode());
		}

		query.setParameterList("groupIdList", groupIdList);
		queryCount.setParameterList("groupIdList", groupIdList);
		querySumGiaTruocThue.setParameterList("groupIdList", groupIdList);

		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
			querySumGiaTruocThue.setParameter("dateFrom", obj.getDateFrom());
		}

		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
			querySumGiaTruocThue.setParameter("dateTo", obj.getDateTo());
		}

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<ReportDTO> ls = query.list();
		if (ls.size() > 0) {
			BigDecimal totalGiaTruocThue = (BigDecimal) querySumGiaTruocThue.uniqueResult();
			ls.get(0).setSoTienChuyenChu(totalGiaTruocThue.toString());
			ls.get(0).setSumGiaTruocThue(totalGiaTruocThue.doubleValue());
		}
		return ls;
	}
	// Huy-end

	// Huypq-20072020-start
	public List<ReportEffectiveDTO> doSearchEffective(ReportEffectiveDTO obj) {
		SQLQuery query = null;
		SQLQuery queryCount = null;
		if (StringUtils.isNotBlank(obj.getType()) && obj.getType().equals("1")) {
			StringBuilder sql = new StringBuilder("SELECT a.Das_type dasType, " + "  a.Cdbr_type cdbrType, "
					+ "  a.House_name houseName, " + "  a.Total_area totalArea, "
					+ "  a.Total_apartments totalApartment, " + "  a.Cost_Das costDas, "
					+ "  a.Cost_engine_room_CDBR costEngineRoomCDBR, " + "  a.Cost_CDBR costCDBR, "
					+ "  a.Ratio_rate ratioRate, " + "  a.Engine_room_Das engineRoomDas, "
					+ "  a.Feeder_anten_Das feederAntenDas, " + "  a.Cost_other_Das costOtherDas, "
					+ "  a.Axis_Cdbr axisCdbr, " + "  a.Apartments_all_Cdbr apartmentsAllCdbr, "
					+ "  a.Apartments_Cdbr apartmentsCdbr, " + "  a.Cost_other_Cdbr costOtherCdbr, "
					+ "  a.Engine_room_cdbr engineRoomCdbr, " + "  a.Engine_room_cable_cdbr engineRoomCableCdbr, "
					+ "  a.effective effective " + "FROM Effective_Calculate_DAS_CDBR a WHERE 1=1 ");

			if (StringUtils.isNotBlank(obj.getKeySearch())) {
				sql.append(" AND upper(a.House_name) like upper(:keySearch) escape '&' ");
			}

			sql.append(" ORDER BY a.Effective_Calculate_DAS_CDBR_ID DESC ");
			StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

			query = getSession().createSQLQuery(sql.toString());
			queryCount = getSession().createSQLQuery(sqlCount.toString());

			query.addScalar("dasType", new StringType());
			query.addScalar("cdbrType", new StringType());
			query.addScalar("costDas", new DoubleType());
			query.addScalar("costEngineRoomCDBR", new DoubleType());
			query.addScalar("costCDBR", new DoubleType());
			query.addScalar("ratioRate", new DoubleType());
			query.addScalar("houseName", new StringType());
			query.addScalar("totalArea", new DoubleType());
			query.addScalar("totalApartment", new DoubleType());
			query.addScalar("engineRoomDas", new StringType());
			query.addScalar("feederAntenDas", new StringType());
			query.addScalar("costOtherDas", new StringType());
			query.addScalar("axisCdbr", new StringType());
			query.addScalar("apartmentsAllCdbr", new StringType());
			query.addScalar("apartmentsCdbr", new StringType());
			query.addScalar("costOtherCdbr", new StringType());
			query.addScalar("engineRoomCdbr", new StringType());
			query.addScalar("engineRoomCableCdbr", new StringType());
			query.addScalar("effective", new StringType());

			if (StringUtils.isNotBlank(obj.getKeySearch())) {
				query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
				queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
		} else {
			StringBuilder sql = new StringBuilder("SELECT CAT_PROVINCE_CODE catProvinceCode, "
					+ "  CAT_PROVINCE_NAME catProvinceName, " + "  LATITUDE latitude, " + "  LONGITUDE longitude, "
					+ "  HIGHT_BTS hightBts, " + "  COLUMN_TYPE columnType, " + "  TOPOGRAPHIC topographic, "
					+ "  LOCATION location, " + "  ADDRESS address, " + "  TYPE_STATION typeStation, "
					+ "  MNO_VIETTEL mNOViettel, " + "  MNO_VINA mNOVina, " + "  MNO_MOBILE mNOMobi, "
					+ "  OURCE_DEPLOYMENT ourceDeployment, " + "  LECT_DEPRECIATION_PERIOD lectDepreciationPeriod, "
					+ "  SILK_ENTER_PRICE silkEnterPrice, " + "  PRICE price, " + "  COST_MB costMB, "
					+ "  COLUMN_FOUNDATION_ITEMS columnFoundationItems, "
					+ "  COST_COLUMN_FOUNDATION_ITEMS costColumnFoundationItems, "
					+ "  HOUSE_FOUNDATION_ITEMS houseFoundationItems, "
					+ "  COST_HOUSE_FOUNDATION_ITEMS costHouseFoundationItems, "
					+ "  COLUMN_BODY_CATEGORY columnBodyCategory, "
					+ "  COST_COLUMN_BODY_CATEGORY costColumnBodyCategory, " + "  MACHINE_ROOM_ITEMS machineRoomItems, "
					+ "  COST_MACHINE_ROOM_ITEMS costMachineRoomItems, " + "  GROUNDING_ITEMS groundingItems, "
					+ "  COST_GROUNDING_ITEMS costGroundingItems, " + "  ELECTRIC_TOWING_ITEMS electricTowingItems, "
					+ "  COST_ELECTRIC_TOWING_ITEMS costElectricTowingItems, "
					+ "  COLUMN_MOUNTING_ITEM columnMountingItem, "
					+ "  COST_COLUMN_MOUNTING_ITEM costColumnMountingItem, "
					+ "  INSTALLATION_HOUSES installationHouses, "
					+ "  COST_INSTALLATION_HOUSES costInstallationHouses, " + "  ELECTRICAL_ITEMS electricalItems, "
					+ "  COST_ELECTRICAL_ITEMS costElectricalItems, "
					+ "  MOTORIZED_TRANSPORT_ITEMS motorizedTransportItems, "
					+ "  COST_MOTORIZED_TRANSPORT_ITEMS costMotorizedTransportItems, "
					+ "  ITEM_MANUAL_SHIPPING itemManualShipping, "
					+ "  COST_ITEM_MANUAL_SHIPPING costItemManualShipping, " + "  ITEM_SHIPPING itemShipping, "
					+ "  COST_ITEM_SHIPPING costItemShipping, " + "  COST_ITEMS_OTHER_EXPENSES costItemsOtherExpenses, "
					+ "  OWER_CABINET_COOLING_SYSTEM owerCabinetCoolingSystem, " + "  RECTIFIER_3000 rectifier3000, "
					+ "  BATTERY_LITHIUM batteryLithium, " + "  OIL_GENERATOR oilGenerator, " + "  ATS ats, "
					+ "  SUPERVISION_CONTROL supervisionControl, " + "  OTHER_AUXILIARY_SYSTEM otherAuxiliarySystem, "
					+ "  PUBLIC_INSTALLATION_POWER publicInstallationPower, " + "  EFFECTIVE effective "
					+ "FROM Effective_Calculate_BTS WHERE 1=1 ");

			if (StringUtils.isNotBlank(obj.getKeySearch())) {
				sql.append(" AND (upper(LOCATION) like upper(:keySearch) escape '&' "
						+ " OR upper(ADDRESS) like upper(:keySearch) escape '&' " + " )");
			}

			if (StringUtils.isNotBlank(obj.getCatProvinceCode())) {
				sql.append(" AND CAT_PROVINCE_CODE = :catProvinceCode ");
			}

			sql.append(" ORDER BY Effective_Calculate_BTS_ID DESC ");
			StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

			query = getSession().createSQLQuery(sql.toString());
			queryCount = getSession().createSQLQuery(sqlCount.toString());

			query.addScalar("catProvinceCode", new StringType());
			query.addScalar("catProvinceName", new StringType());
			query.addScalar("latitude", new DoubleType());
			query.addScalar("longitude", new DoubleType());
			query.addScalar("hightBts", new DoubleType());
			query.addScalar("columnType", new StringType());
			query.addScalar("topographic", new StringType());
			query.addScalar("location", new StringType());
			query.addScalar("address", new StringType());
			query.addScalar("typeStation", new StringType());
			query.addScalar("mNOViettel", new StringType());
			query.addScalar("mNOVina", new StringType());
			query.addScalar("mNOMobi", new StringType());
			query.addScalar("ourceDeployment", new StringType());
			query.addScalar("lectDepreciationPeriod", new StringType());
			query.addScalar("silkEnterPrice", new StringType());
			query.addScalar("price", new DoubleType());
			query.addScalar("costMB", new DoubleType());
			query.addScalar("columnFoundationItems", new StringType());
			query.addScalar("costColumnFoundationItems", new DoubleType());
			query.addScalar("houseFoundationItems", new StringType());
			query.addScalar("costHouseFoundationItems", new DoubleType());
			query.addScalar("columnBodyCategory", new StringType());
			query.addScalar("costColumnBodyCategory", new DoubleType());
			query.addScalar("machineRoomItems", new StringType());
			query.addScalar("costMachineRoomItems", new DoubleType());
			query.addScalar("groundingItems", new StringType());
			query.addScalar("costGroundingItems", new DoubleType());
			query.addScalar("electricTowingItems", new StringType());
			query.addScalar("costElectricTowingItems", new DoubleType());
			query.addScalar("columnMountingItem", new StringType());
			query.addScalar("costColumnMountingItem", new DoubleType());
			query.addScalar("installationHouses", new StringType());
			query.addScalar("costInstallationHouses", new DoubleType());
			query.addScalar("electricalItems", new StringType());
			query.addScalar("costElectricalItems", new DoubleType());
			query.addScalar("motorizedTransportItems", new StringType());
			query.addScalar("costMotorizedTransportItems", new DoubleType());
			query.addScalar("itemManualShipping", new StringType());
			query.addScalar("costItemManualShipping", new DoubleType());
			query.addScalar("itemShipping", new StringType());
			query.addScalar("costItemShipping", new DoubleType());
			query.addScalar("costItemsOtherExpenses", new DoubleType());
			query.addScalar("owerCabinetCoolingSystem", new StringType());
			query.addScalar("rectifier3000", new StringType());
			query.addScalar("batteryLithium", new StringType());
			query.addScalar("oilGenerator", new StringType());
			query.addScalar("ats", new StringType());
			query.addScalar("supervisionControl", new StringType());
			query.addScalar("otherAuxiliarySystem", new StringType());
			query.addScalar("publicInstallationPower", new StringType());
			query.addScalar("effective", new StringType());

			if (StringUtils.isNotBlank(obj.getCatProvinceCode())) {
				query.setParameter("catProvinceCode", obj.getCatProvinceCode());
				queryCount.setParameter("catProvinceCode", obj.getCatProvinceCode());
			}

			if (StringUtils.isNotBlank(obj.getKeySearch())) {
				query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
				queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
		}

		query.setResultTransformer(Transformers.aliasToBean(ReportEffectiveDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	// Huy-end

	// huypq-21062021-start
	public List<ReportDTO> doSearchReportMassSearchConstr(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT " + "MONTH month, " + "YEAR year, "
				+ "PROVINCECODE provinceCode, " + "PERFORMDBHT performDBHT, " + "PERFORMHSHC performHSHC, "
				+ "PERFORMKC performKC, " + "PERFORMPS performPS, " + "PERFORMTMB performTMB, " + "PLANDBHT planDBHT, "
				+ "PLANHSHC planHSHC, " + "PLANKC planKC, " + "PLANPS planPS, " + "PLANTMB planTMB, " + "PROCESS_DATE, "
				+ "RATIODBHT ratioDBHT, " + "RATIOHSHC ratioHSHC, " + "RATIOKC ratioKC, " + "RATIOPS ratioPS, "
				+ "RATIOTMB ratioTMB, " + "sum(PERFORMDBHT) over() sumPerformDBHT, "
				+ "sum(PERFORMHSHC) over() sumPerformHSHC, " + "sum(PERFORMKC) over() sumPerformKC, "
				+ "sum(PERFORMPS) over() sumPerformPS, " + "sum(PERFORMTMB) over() sumPerformTMB, "
				+ "sum(PLANDBHT) over() sumPlanDBHT, " + "sum(PLANHSHC) over() sumPlanHSHC, "
				+ "sum(PLANKC) over() sumPlanKC, " + "sum(PLANPS) over() sumPlanPS, "
				+ "sum(PLANTMB) over() sumPlanTMB, "
				+ "round(decode(sum(PLANDBHT) over(),0,0,100*(sum(PERFORMDBHT) over()/sum(PLANDBHT) over())),2) sumRatioDBHT, "
				+ "round(decode(sum(PLANHSHC) over(),0,0,100*(sum(PERFORMHSHC) over()/sum(PLANHSHC) over())),2) sumRatioHSHC, "
				+ "round(decode(sum(PLANKC) over(),0,0,100*(sum(PERFORMKC) over()/sum(PLANKC) over())),2) sumRatioKC, "
				+ "round(decode(sum(PLANPS) over(),0,0,100*(sum(PERFORMPS) over()/sum(PLANPS) over())),2) sumRatioPS, "
				+ "round(decode(sum(PLANTMB) over(),0,0,100*(sum(PERFORMTMB) over()/sum(PLANTMB) over())),2) sumRatioTMB "
				+ "FROM RP_BTS_HTCT where 1=1 ");
		if (StringUtils.isNotBlank(obj.getThang())) {
			sql.append(" AND MONTH ||'/'|| YEAR= :thang ");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND PROVINCECODE = :provinceCode ");
		}

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("provinceCode", new StringType());
		query.addScalar("planTMB", new DoubleType());
		query.addScalar("performTMB", new DoubleType());
		query.addScalar("ratioTMB", new DoubleType());
		query.addScalar("planKC", new DoubleType());
		query.addScalar("performKC", new DoubleType());
		query.addScalar("ratioKC", new DoubleType());
		query.addScalar("planDBHT", new DoubleType());
		query.addScalar("performDBHT", new DoubleType());
		query.addScalar("ratioDBHT", new DoubleType());
		query.addScalar("planPS", new DoubleType());
		query.addScalar("performPS", new DoubleType());
		query.addScalar("ratioPS", new DoubleType());
		query.addScalar("planHSHC", new DoubleType());
		query.addScalar("performHSHC", new DoubleType());
		query.addScalar("ratioHSHC", new DoubleType());
		query.addScalar("month", new StringType());
		query.addScalar("year", new StringType());
		query.addScalar("sumPlanTMB", new DoubleType());
		query.addScalar("sumPerformTMB", new DoubleType());
		query.addScalar("sumRatioTMB", new DoubleType());
		query.addScalar("sumPlanKC", new DoubleType());
		query.addScalar("sumPerformKC", new DoubleType());
		query.addScalar("sumRatioKC", new DoubleType());
		query.addScalar("sumPlanDBHT", new DoubleType());
		query.addScalar("sumPerformDBHT", new DoubleType());
		query.addScalar("sumRatioDBHT", new DoubleType());
		query.addScalar("sumPlanPS", new DoubleType());
		query.addScalar("sumPerformPS", new DoubleType());
		query.addScalar("sumRatioPS", new DoubleType());
		query.addScalar("sumPlanHSHC", new DoubleType());
		query.addScalar("sumPerformHSHC", new DoubleType());
		query.addScalar("sumRatioHSHC", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (StringUtils.isNotBlank(obj.getThang())) {
			query.setParameter("thang", obj.getThang());
			queryCount.setParameter("thang", obj.getThang());
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<ReportDTO> doSearchReportResultDeployBts(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("select distinct  \r\n" + "				 catP.code provinceCode,  \r\n"
				+ "				 (select max(CAT_PARTNER_STATION_CODE) from CAT_PARTNER_STATION staPartner where staPartner.CAT_STATION_HOUSE_CODE=catS.code) tramVTN,  \r\n"
				+ "				 catS.code tramVCC,  \r\n"
				+ "				 nvl(c.contract_code,d.contract_code) contractCode,  \r\n"
				+ "				 a.ngaythue_MB ngayThueMb,  \r\n"
				+ "				 b.hoanthanh_TKDT ngayHoanThanhTkdt,  \r\n"
				+ "				 b.hoanthanh_thamdinh ngayHoanThanhThamDinh,  \r\n"
				+ "				 b.ra_quyetdinh_TKDT ngayRaQuyetDinhTkdt,  \r\n"
				+ "				 c.ngay_khoicong ngayKhoiCong,  \r\n"
				+ "				 d.ngay_dongbo ngayDongBo,  \r\n"
				+ "				 e.ngay_phatsong ngayPhatSong,  \r\n"
				+ "				 g.update_cd_approve_wo ngayHshcGuiTtht,  \r\n"
				+ "				 TRUNC (LAST_DAY (ngay_dongbo +30)) ngayKpiHshc,  \r\n"
				+ "				 case when nvl(g.update_cd_approve_wo,sysdate) <= TRUNC (LAST_DAY (ngay_dongbo +30)) then 'Đúng hạn'  \r\n"
				+ "				 when nvl(g.update_cd_approve_wo,sysdate) > TRUNC (LAST_DAY (ngay_dongbo +30)) then 'Quá hạn' end quaHanKpi  \r\n"
				+ "				   \r\n" + "				 from   \r\n"
				+ "                 cat_station catS,cat_province catP,\r\n"
				+ "				 (select sys.province_code,cat.code stationCode, a.APPROVE_DATE_REPORT_WO ngaythue_MB from wo a,WO_MAPPING_STATION b,  \r\n"
				+ "				 cat_station cat,sys_group sys  \r\n"
				+ "				 where a.id=b.wo_id and a.wo_type_id=243 and a.state='OK' and a.status =1 and b.status=1 and b.cat_station_id=cat.cat_station_id and a.CD_LEVEL_2=sys.sys_group_id) a,  \r\n"
				+ "				 (select a.station_code stationCode,  \r\n"
				+ "				 (select b.COMPLETED_DATE from WO_CHECKLIST b where a.id=b.wo_id and b.CHECKLIST_NAME='Thiết kế dự toán')hoanthanh_TKDT,  \r\n"
				+ "				 (select b.COMPLETED_DATE from WO_CHECKLIST b where a.id=b.wo_id and b.CHECKLIST_NAME='Thẩm định dự toán')hoanthanh_thamdinh,  \r\n"
				+ "				 (select b.COMPLETED_DATE from WO_CHECKLIST b where a.id=b.wo_id and b.CHECKLIST_NAME='Duyệt tư vấn thiết kế dự toán')ra_quyetdinh_TKDT,  \r\n"
				+ "				 nvl(a.contract_code,a.project_code)contract_code,a.contract_id,a.construction_id  \r\n"
				+ "				 from wo a where a.wo_type_id=261 and a.status =1) b,  \r\n" + "				   \r\n"
				+ "				 (select sys.province_code,a.station_code stationCode,APPROVE_DATE_REPORT_WO ngay_khoicong, nvl(a.contract_code,a.project_code) contract_code from wo_tr a, wo b,  \r\n"
				+ "				 sys_group sys  \r\n"
				+ "				 where a.tr_type_id in (31350,444522) and a.id=b.tr_id and b.CAT_WORK_ITEM_TYPE_id=2083 and b.status=1 and b.state in('CD_OK','OK') and b.CD_LEVEL_2=sys.sys_group_id)c,  \r\n"
				+ "				   \r\n"
				+ "				 (select distinct sys.province_code,a.station_code stationCode,c.COMPLETE_DATE ngay_dongbo, nvl(a.contract_code,a.project_code) contract_code from wo_tr a, wo b,construction c,  \r\n"
				+ "				 sys_group sys where a.tr_type_id in (31350,444522) and a.id=b.tr_id and b.construction_id=c.construction_id and b.state in('CD_OK','OK') and b.status=1  \r\n"
				+ "				 and b.CD_LEVEL_2=sys.sys_group_id) d,  \r\n" + "				   \r\n"
				+ "				 (select sys.province_code,a.station_code stationCode,b.APPROVE_DATE_REPORT_WO ngay_phatsong from wo_tr a, wo b, sys_group sys where a.tr_type_id in (31350,444522) and a.id=b.tr_id and b.wo_type_id=241 and b.state in('OK') and b.status=1  \r\n"
				+ "				 and b.CD_LEVEL_2=sys.sys_group_id) e,  \r\n" + "				   \r\n"
				+ "				 (select a.station_code stationCode, update_cd_approve_wo from wo a,construction b where a.wo_type_id=3 and a.status=1  \r\n"
				+ "				 and a.construction_id=b.construction_id and b.CHECK_HTCT=1 and a.state in('CD_OK','OK'))g  \r\n"
				+ "				   \r\n" + "				 where \r\n"
				+ "                 catS.cat_province_id=catP.cat_province_id\r\n"
				+ "                 and catS.code=a.stationCode(+)\r\n"
				+ "                 and catS.code=b.stationCode(+)  \r\n"
				+ "				 and catS.code=c.stationCode(+)  \r\n"
				+ "				 and catS.code=d.stationCode(+)  \r\n"
				+ "				 and catS.code=e.stationCode(+)  \r\n"
				+ "				 and catS.code=g.stationCode(+) ");

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND catP.code= :provinceCode ");
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(
					" AND (UPPER(catS.code) like UPPER(:keySearch) OR UPPER(b.contract_code) like UPPER(:keySearch)) ");
		}

		if (StringUtils.isNotBlank(obj.getDateType())) {
			if (obj.getDateType().equals("1")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND a.ngaythue_MB >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(a.ngaythue_MB) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("2")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND b.hoanthanh_TKDT >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(b.hoanthanh_TKDT) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("3")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND b.hoanthanh_thamdinh >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(b.hoanthanh_thamdinh) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("4")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND b.ra_quyetdinh_TKDT >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(b.ra_quyetdinh_TKDT) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("5")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND c.ngay_khoicong >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(c.ngay_khoicong) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("6")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND d.ngay_dongbo >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(d.ngay_dongbo) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("7")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND e.ngay_phatsong >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(e.ngay_phatsong) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("8")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND g.update_cd_approve_wo >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(g.update_cd_approve_wo) <=:dateTo ");
				}
			} else if (obj.getDateType().equals("9")) {
				if (obj.getDateFrom() != null) {
					sql.append(" AND LAST_DAY(ngay_dongbo +30) >=:dateFrom ");
				}

				if (obj.getDateTo() != null) {
					sql.append(" AND TRUNC(LAST_DAY(ngay_dongbo +30)) <=:dateTo ");
				}
			}
		}

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("provinceCode", new StringType());
		query.addScalar("tramVTN", new StringType());
		query.addScalar("tramVCC", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("ngayThueMb", new DateType());
		query.addScalar("ngayHoanThanhTkdt", new DateType());
		query.addScalar("ngayHoanThanhThamDinh", new DateType());
		query.addScalar("ngayRaQuyetDinhTkdt", new DateType());
		query.addScalar("ngayKhoiCong", new DateType());
		query.addScalar("ngayDongBo", new DateType());
		query.addScalar("ngayPhatSong", new DateType());
		query.addScalar("ngayHshcGuiTtht", new DateType());
		query.addScalar("ngayKpiHshc", new DateType());
		query.addScalar("quaHanKpi", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		if (StringUtils.isNotBlank(obj.getDateType())) {
			if (obj.getDateType().equals("1")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("2")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("3")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("4")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("5")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("6")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("7")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("8")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			} else if (obj.getDateType().equals("9")) {
				if (obj.getDateFrom() != null) {
					query.setParameter("dateFrom", obj.getDateFrom());
					queryCount.setParameter("dateFrom", obj.getDateFrom());
				}

				if (obj.getDateTo() != null) {
					query.setParameter("dateTo", obj.getDateTo());
					queryCount.setParameter("dateTo", obj.getDateTo());
				}
			}
		}

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	// Huy-end

	// Huypq-08072021-start
	public List<ReportDTO> doSearchReportAcceptHSHC(ReportDTO obj) {
		StringBuilder sql = new StringBuilder(" SELECT " + "    ( " + "        SELECT " + "            name "
				+ "        FROM " + "            sys_group s " + "        WHERE "
				+ "            s.sys_group_id = sys.parent_id " + "            AND s.status = 1 "
				+ "    ) sysGroupName, " + "    wo_code woCode, " + "    wo_name woName, "
				+ "    to_char(UPDATE_CD_APPROVE_WO,'dd/MM/yyyy hh:mi:ss') updateCdApproveWo, "
				+ "    to_char(UPDATE_TTHT_APPROVE_WO,'dd/MM/yyyy hh:mi:ss') updateTthtApproveWo, "
				+ "    to_char(UPDATE_TC_BRANCH_APPROVE_WO,'dd/MM/yyyy hh:mi:ss') updateTcBranchApproveWo, "
				+ "    to_char(UPDATE_TC_TCT_APPROVE_WO,'dd/MM/yyyy hh:mi:ss') updateTcTctApproveWo, "
				+ "    to_char(APPROVE_DATE_REPORT_WO,'dd/MM/yyyy hh:mi:ss') approveDateReportWo, "
				+ "    CONTRACT_CODE contractCode, " + "    CONSTRUCTION_CODE constructionCode, "
				+ "    STATION_CODE stationCode, " + "    ( " + "        SELECT " + "            name "
				+ "        FROM " + "            app_param p " + "        WHERE "
				+ "            par_type = 'AP_WORK_SRC' " + "            AND status = 1 "
				+ "            AND p.code = a.AP_WORK_SRC " + "    ) apWorkSrc, " + "    ( " + "        SELECT "
				+ "            name " + "        FROM " + "            app_param p " + "        WHERE "
				+ "            par_type = 'AP_CONSTRUCTION_TYPE' " + "            AND status = 1 "
				+ "            AND p.code = a.AP_CONSTRUCTION_TYPE " + "    ) apConstructionType, " + "    CASE "
				+ "            WHEN a.state = 'CD_OK'          THEN 'Điều phối duyệt OK' "
				+ "            WHEN a.state = 'WAIT_TC_BRANCH' THEN 'Chờ tài chính trụ duyệt' "
				+ "            WHEN a.state = 'WAIT_TC_TCT'    THEN 'Chờ tài chính TCT duyệt' "
				+ "            WHEN a.state = 'OK'             THEN 'Hoàn thành' " + "        END " + "    woState, "
				+ "    MONEY_VALUE / 1000000 moneyValue " + "FROM " + "    wo a "
				+ "    LEFT JOIN sys_group sys ON a.CD_LEVEL_2 = sys.sys_group_id " + "WHERE " + "    WO_TYPE_ID = 3 "
				+ "    AND state IN ( " + "        'CD_OK', " + "        'OK', " + "        'WAIT_TC_BRANCH', "
				+ "        'WAIT_TC_TCT' " + "    ) ");

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (UPPER(a.CONTRACT_CODE) LIKE UPPER(:keySearch)  "
					+ "        OR UPPER(a.CONSTRUCTION_CODE) LIKE UPPER(:keySearch) "
					+ "        OR UPPER(a.STATION_CODE) LIKE UPPER(:keySearch) "
					+ " 		 OR UPPER(wo_code) LIKE UPPER(:keySearch) escape '&') ");
		}

		if (obj.getDateFrom() != null) {
			if (obj.getDateType().equals("1")) {
				sql.append(" AND a.UPDATE_CD_APPROVE_WO >=:dateFrom ");
			} else if (obj.getDateType().equals("2")) {
				sql.append(" AND a.UPDATE_TTHT_APPROVE_WO >=:dateFrom ");
			} else if (obj.getDateType().equals("3")) {
				sql.append(" AND a.UPDATE_TC_BRANCH_APPROVE_WO >=:dateFrom ");
			} else if (obj.getDateType().equals("4")) {
				sql.append(" AND a.UPDATE_TC_TCT_APPROVE_WO >=:dateFrom ");
			} else if (obj.getDateType().equals("5")) {
				sql.append(" AND a.APPROVE_DATE_REPORT_WO >=:dateFrom ");
			}
		}

		if (obj.getDateTo() != null) {
			if (obj.getDateType().equals("1")) {
				sql.append(" AND TRUNC(a.UPDATE_CD_APPROVE_WO) <=:dateTo ");
			} else if (obj.getDateType().equals("2")) {
				sql.append(" AND TRUNC(a.UPDATE_TTHT_APPROVE_WO) <=:dateTo ");
			} else if (obj.getDateType().equals("3")) {
				sql.append(" AND TRUNC(a.UPDATE_TC_BRANCH_APPROVE_WO) <=:dateTo ");
			} else if (obj.getDateType().equals("4")) {
				sql.append(" AND TRUNC(a.UPDATE_TC_TCT_APPROVE_WO) <=:dateTo ");
			} else if (obj.getDateType().equals("5")) {
				sql.append(" AND TRUNC(a.APPROVE_DATE_REPORT_WO) <=:dateTo ");
			}
		}

		if (obj.getSysGroupId() != null) {
			sql.append(" AND ( " + "        SELECT " + "            sys_group_id " + "        FROM "
					+ "            sys_group s " + "        WHERE " + "            s.sys_group_id = sys.parent_id "
					+ "            AND s.status = 1 " + "    ) =:sysGroupId ");
		}

		if (obj.getLstWoState() != null && !obj.getLstWoState().isEmpty()) {
			sql.append(" AND a.state IN (:lstWoState) ");
		}

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("sysGroupName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("updateCdApproveWo", new StringType());
		query.addScalar("updateTthtApproveWo", new StringType());
		query.addScalar("updateTcBranchApproveWo", new StringType());
		query.addScalar("updateTcTctApproveWo", new StringType());
		query.addScalar("approveDateReportWo", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("apWorkSrc", new StringType());
		query.addScalar("apConstructionType", new StringType());
		query.addScalar("woState", new StringType());
		query.addScalar("moneyValue", new DoubleType());

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
		}

		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
		}

		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}

		if (obj.getLstWoState() != null && !obj.getLstWoState().isEmpty()) {
			query.setParameterList("lstWoState", obj.getLstWoState());
			queryCount.setParameterList("lstWoState", obj.getLstWoState());
		}

		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	// Huy-end
	// taotq start 27092021
	public List<RpKHBTSDTO> doSearchReportKHBTS(RpKHBTSDTO obj) {
		StringBuilder sql = new StringBuilder("");
		if (obj.getTypeBc().equals("1")) {
			sql = new StringBuilder("select  " + " null stationVccCode, null stationVtNetCode, "
					+ " 				  th.month||'/'||th.year monthYear, th.provinceCode,   "
					+ "                 th.PlanTMBT planTMB, th.planTKDT planTKDT, th.planTTKDT planTTKDT, th.planKC, th.planDBHT, th.planPS,   "
					+ "				  th.planDT, th.planHSHCTTHT, th.planHSHCDTHT,    "
					+ "				  nvl(th.performTMB,0) performTMB , nvl(th.performTKDT,0) performTKDT, nvl(th.performTTKDT,0) performTTKDT, nvl(th.performKC,0) performKC, nvl(th.performDBHT,0) performDBHT,   "
					+ "				  nvl(th.performPS,0) performPS, nvl(th.performDT,0) performDT, nvl(th.performHSHCTTHT,0) performHSHCTTHT, nvl(th.performHSHCDTHT,0) performHSHCDTHT,   "
					+ "				  nvl(th.overdueKPIRentMB,0) overdueKPIRentMB, nvl(th.overdueKPIKC,0) overdueKPIKC,   "
					+ "				  nvl(th.overdueKPILDT,0) overdueKPILDT, nvl(th.overdueKPIHSHCTTHT,0) overdueKPIHSHCTTHT, nvl(th.overdueKPIHSHCDTHT,0) overdueKPIHSHCDTHT,   "
					+ "				  nvl(th.overdueTKDT,0) overdueTKDT, nvl(th.rentMB,0)/1000 rentMB, nvl(th.startUp,0)/1000 startUp, nvl(th.draftingRevenue,0)/1000 draftingRevenue, nvl(th.hshcTTHT,0)/1000 hshcTTHT,   "
					+ "				  nvl(th.overdueKPIDBHT,0) overdueKPIDBHT, nvl(th.rentDBHT,0)/1000 rentDBHT,   "
					+ "				   "
					+ "		Case when nvl(th.PlanTMBT,0)!=0 then round(100*(th.PerformTMB/th.PlanTMBT),2) else 0 end as ratioTMB,  "
					+ "        Case when nvl(th.planTKDT,0)!=0 then round(100*(th.PerformTKDT/th.planTKDT),2) else 0 end as ratioTKDT,  "
					+ "        Case when nvl(th.planTKDT,0)!=0 then round(100*(th.PerformTTKDT/th.planTKDT),2) else 0 end as ratioTTKDT,  "
					+ "        Case when nvl(th.planKC,0)!=0 then round(100*(th.PerformKC/th.planKC),2) else 0 end as ratioKC,  "
					+ "        Case when nvl(th.planDBHT,0)!=0 then round(100*(th.PerformDBHT/th.planDBHT),2) else 0 end as ratioDBHT,  "
					+ "        Case when nvl(th.planPS,0)!=0 then round(100*(th.PerformPS/th.planPS),2) else 0 end as ratioPS,  "
					+ "        Case when nvl(th.planDT,0)!=0 then  round(100*(th.PerformDT/th.planDT),2) else 0 end as ratioDT,  "
					+ "        Case when nvl(th.planHSHCTTHT,0)!=0 then  round(100*(th.PerformHSHCTTHT/th.planHSHCTTHT),2) else 0 end as ratioHSHCTTHT,  "
					+ "        Case when nvl(th.PlanHSHCDTHT,0)!=0 then round(100*(th.PerformHSHCDTHT/th.PlanHSHCDTHT),2) else 0 end as ratioHSHCDTHT "
					+ " from " + " ( "
					+ "     select Province_Code as provinceCode, YEAR ,MONTH, SUM(PERFORM_TMB) as performTMB , Sum(PERFORM_TKDT) as performTKDT, "
					+ "     Sum(PERFORM_TTKDT) as performTTKDT,sum(PERFORM_KC) as performKC, sum(PERFORM_DBHT) as performDBHT, "
					+ "     sum(PERFORM_PS) as performPS,  sum(PERFORM_DT) as performDT,  sum(PERFORM_HSHC_TTHT) as performHSHCTTHT,  "
					+ "     sum(PERFORM_HSHC_DTHT) as performHSHCDTHT, Sum(Case When OVER_KPI_TMBT >0 then 1 else 0 end) as overdueKPIRentMB,Sum(Case When OVER_KPI_KC >0 then 1 else 0 end) as overdueKPIKC, "
					+ "     Sum(Case When OVER_KPI_DT >0 then 1 else 0 end) as overdueKPILDT, Sum(Case When OVER_KPI_HSHC_TTHT >0 then 1 else 0 end) as overdueKPIHSHCTTHT,Sum(Case When OVER_KPI_HSHC_DTHT >0 then 1 else 0 end) as overdueKPIHSHCDTHT, "
					+ "     Sum(Case When OVER_KPI_TKDT >0 then 1 else 0 end) as overdueTKDT, sum(FINE_TMB) as rentMB, sum(FINE_KC) as startUp, Sum(FINE_DT) as draftingRevenue,sum(FINE_HSHC_TTHT) as hshcTTHT, "
					+ "	  "
					+ "	 sum(PLAN_TMB) PlanTMBT, sum(PLAN_TKDT) planTKDT, sum(PLAN_TKDT) planTTKDT, sum(PLAN_KC) as planKC, sum(PLAN_DBHT) planDBHT, sum(PLAN_PS) AS planPS, sum(PLAN_DT) AS planDT, sum(PLAN_HSHC_TTHT) as planHSHCTTHT,sum(PLAN_HSHC_DTHT) as PlanHSHCDTHT, "
					+ "	 Sum(Case When Over_KPI_DBHT >0 then 1 else 0 end) as overdueKPIDBHT,sum(FINE_DBHT) rentDBHT  "
					+ "     from RP_KH_BTS_HTCT  " + "     where 1=1");
			if (obj.getMonthYearD() != null && !obj.getMonthYearD().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (StringUtils.isNotBlank(obj.getProvinceCodeD())) {
				sql.append(" and province_code = :provinceCode ");
			}

			sql.append(" group by Province_Code, YEAR, MONTH )th ");
		} else {
			sql = new StringBuilder("select  " + "STATIONVCC_CODE stationVccCode,STATION_VTNET_CODE stationVtNetCode, "
					+ "th.month||'/'||th.year monthYear, th.provinceCode,   "
					+ "				 th.PlanTMBT planTMB, th.planTKDT planTKDT, th.planTTKDT planTTKDT, th.planKC, th.planDBHT, th.planPS,   "
					+ "				  th.planDT, th.planHSHCTTHT, th.planHSHCDTHT,    "
					+ "				  nvl(th.performTMB,0) performTMB , nvl(th.performTKDT,0) performTKDT, nvl(th.performTTKDT,0) performTTKDT, nvl(th.performKC,0) performKC, nvl(th.performDBHT,0) performDBHT,   "
					+ "				  nvl(th.performPS,0) performPS, nvl(th.performDT,0) performDT, nvl(th.performHSHCTTHT,0) performHSHCTTHT, nvl(th.performHSHCDTHT,0) performHSHCDTHT,   "
					+ "				  nvl(th.overdueKPIRentMB,0) overdueKPIRentMB, nvl(th.overdueKPIKC,0) overdueKPIKC,   "
					+ "				  nvl(th.overdueKPILDT,0) overdueKPILDT, nvl(th.overdueKPIHSHCTTHT,0) overdueKPIHSHCTTHT, nvl(th.overdueKPIHSHCDTHT,0) overdueKPIHSHCDTHT,   "
					+ "				  nvl(th.overdueTKDT,0) overdueTKDT, nvl(th.rentMB,0)/1000 rentMB, nvl(th.startUp,0)/1000 startUp, nvl(th.draftingRevenue,0)/1000 draftingRevenue, nvl(th.hshcTTHT,0)/1000 hshcTTHT,   "
					+ "				  nvl(th.overdueKPIDBHT,0) overdueKPIDBHT, nvl(th.rentDBHT,0)/1000 rentDBHT,   "
					+ "				   "
					+ "		Case when nvl(th.PlanTMBT,0)!=0 then round(100*(th.PerformTMB/th.PlanTMBT),2) else 0 end as ratioTMB,  "
					+ "        Case when nvl(th.planTKDT,0)!=0 then round(100*(th.PerformTKDT/th.planTKDT),2) else 0 end as ratioTKDT,  "
					+ "        Case when nvl(th.planTKDT,0)!=0 then round(100*(th.PerformTTKDT/th.planTKDT),2) else 0 end as ratioTTKDT,  "
					+ "        Case when nvl(th.planKC,0)!=0 then round(100*(th.PerformKC/th.planKC),2) else 0 end as ratioKC,  "
					+ "        Case when nvl(th.planDBHT,0)!=0 then round(100*(th.PerformDBHT/th.planDBHT),2) else 0 end as ratioDBHT,  "
					+ "        Case when nvl(th.planPS,0)!=0 then round(100*(th.PerformPS/th.planPS),2) else 0 end as ratioPS,  "
					+ "        Case when nvl(th.planDT,0)!=0 then  round(100*(th.PerformDT/th.planDT),2) else 0 end as ratioDT,  "
					+ "        Case when nvl(th.planHSHCTTHT,0)!=0 then  round(100*(th.PerformHSHCTTHT/th.planHSHCTTHT),2) else 0 end as ratioHSHCTTHT,  "
					+ "        Case when nvl(th.PlanHSHCDTHT,0)!=0 then round(100*(th.PerformHSHCDTHT/th.PlanHSHCDTHT),2) else 0 end as ratioHSHCDTHT "
					+ " from " + " ( "
					+ "     select STATIONVCC_CODE,STATION_VTNET_CODE, Province_Code as provinceCode, YEAR ,MONTH, SUM(PERFORM_TMB) as performTMB , Sum(PERFORM_TKDT) as performTKDT, "
					+ "     Sum(PERFORM_TTKDT) as performTTKDT,sum(PERFORM_KC) as performKC, sum(PERFORM_DBHT) as performDBHT, "
					+ "     sum(PERFORM_PS) as performPS,  sum(PERFORM_DT) as performDT,  sum(PERFORM_HSHC_TTHT) as performHSHCTTHT,  "
					+ "     sum(PERFORM_HSHC_DTHT) as performHSHCDTHT, Sum(Case When OVER_KPI_TMBT >0 then 1 else 0 end) as overdueKPIRentMB,Sum(Case When OVER_KPI_KC >0 then 1 else 0 end) as overdueKPIKC, "
					+ "     Sum(Case When OVER_KPI_DT >0 then 1 else 0 end) as overdueKPILDT, Sum(Case When OVER_KPI_HSHC_TTHT >0 then 1 else 0 end) as overdueKPIHSHCTTHT,Sum(Case When OVER_KPI_HSHC_DTHT >0 then 1 else 0 end) as overdueKPIHSHCDTHT, "
					+ "     Sum(Case When OVER_KPI_TKDT >0 then 1 else 0 end) as overdueTKDT, sum(FINE_TMB) as rentMB, sum(FINE_KC) as startUp, Sum(FINE_DT) as draftingRevenue,sum(FINE_HSHC_TTHT) as hshcTTHT, "
					+ "	  "
					+ "	 sum(PLAN_TMB) PlanTMBT, sum(PLAN_TKDT) planTKDT, sum(PLAN_TKDT) planTTKDT, sum(PLAN_KC) as planKC, sum(PLAN_DBHT) planDBHT, sum(PLAN_PS) AS planPS, sum(PLAN_DT) AS planDT, sum(PLAN_HSHC_TTHT) as planHSHCTTHT,sum(PLAN_HSHC_DTHT) as PlanHSHCDTHT, "
					+ "	 Sum(Case When Over_KPI_DBHT >0 then 1 else 0 end) as overdueKPIDBHT,sum(FINE_DBHT) rentDBHT  "
					+ "     from RP_KH_BTS_HTCT  " + "     where 1=1 ");
			if (obj.getMonthYearD() != null && !obj.getMonthYearD().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (StringUtils.isNotBlank(obj.getProvinceCodeD())) {
				sql.append(" and province_code = :provinceCode ");
			}
			if (StringUtils.isNotBlank(obj.getKeySearch())) {
				sql.append(
						" and (UPPER(STATIONVCC_CODE) like UPPER(:keySearch) OR UPPER(STATION_VTNET_CODE) like UPPER(:keySearch) escape '&')");
			}

			sql.append(" group by Province_Code, YEAR, MONTH, STATIONVCC_CODE, STATION_VTNET_CODE)th ");
		}

		StringBuilder sqlSum = new StringBuilder("SELECT count(*) totalRecord, ");
		sqlSum.append(
				" sum(planTMB) sumPlanTMB, sum(planTKDT) sumPlanTKDT, sum(planTTKDT) sumPlanTTKDT, sum(planKC) sumPlanKC, sum(planDBHT) sumPlanDBHT, sum(planPS) sumPlanPS, ");
		sqlSum.append(
				" sum(planDT) sumPlanDT, sum(planHSHCTTHT) sumPlanHSHCTTHT, sum(planHSHCDTHT) sumPlanHSHCDTHT,  ");
		sqlSum.append(" sum(ratioTMB) sumRatioTMB, ");
		sqlSum.append(
				" sum(performTMB) sumPerformTMB , sum(performTKDT) sumPerformTKDT, sum(performTTKDT) sumPerformTTKDT, sum(performKC) sumPerformKC, sum(performDBHT) sumPerformDBHT, ");
		sqlSum.append(
				" sum(performPS) sumPerformPS, sum(performDT) sumPerformDT, sum(performHSHCTTHT) sumPerformHSHCTTHT, sum(performHSHCDTHT) sumPerformHSHCDTHT, ");
		sqlSum.append(" sum(overdueKPIRentMB) sumOverdueKPIRentMB, sum(overdueKPIKC) sumOverdueKPIKC, ");
		sqlSum.append(
				" sum(overdueKPILDT) sumOverdueKPILDT, sum(overdueKPIHSHCTTHT) sumOverdueKPIHSHCTTHT, sum(overdueKPIHSHCDTHT) sumOverdueKPIHSHCDTHT, ");
		sqlSum.append(
				" sum(overdueTKDT) sumOverdueTKDT, sum(rentMB) sumRentMB, sum(startUp) sumStartUp, sum(draftingRevenue) sumDraftingRevenue, sum(hshcTTHT) sumHshcTTHT, ");
		sqlSum.append(" sum(ratioTKDT) sumRatioTKDT, ");
		sqlSum.append(" sum(ratioTTKDT) sumRatioTTKDT, ");
		sqlSum.append(" sum(ratioKC) sumRatioKC, ");
		sqlSum.append(" sum(ratioDBHT) sumRatioDBHT, ");
		sqlSum.append(" sum(ratioPS) sumRatioPS, ");
		sqlSum.append(" sum(ratioDT) sumRatioDT, ");
		sqlSum.append(" sum(ratioHSHCTTHT) sumRatioHSHCTTHT,  ");
		sqlSum.append(" sum(rentDBHT) sumRentDBHT, ");
		sqlSum.append(" sum(overdueKPIDBHT) sumOverdueKPIDBHT, ");
		sqlSum.append(" sum(ratioHSHCDTHT) sumRatioHSHCDTHT  FROM (");
		sqlSum.append(sql.toString());
		sqlSum.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlSum.toString());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("monthYear", new StringType());
		query.addScalar("planTMB", new DoubleType());
		query.addScalar("performTMB", new DoubleType());
		query.addScalar("ratioTMB", new DoubleType());
		query.addScalar("planTKDT", new DoubleType());
		query.addScalar("performTKDT", new DoubleType());
		query.addScalar("ratioTKDT", new DoubleType());
		query.addScalar("planTTKDT", new DoubleType());
		query.addScalar("performTTKDT", new DoubleType());
		query.addScalar("ratioTTKDT", new DoubleType());
		query.addScalar("planKC", new DoubleType());
		query.addScalar("performKC", new DoubleType());
		query.addScalar("ratioKC", new DoubleType());
		query.addScalar("planDBHT", new DoubleType());
		query.addScalar("performDBHT", new DoubleType());
		query.addScalar("ratioDBHT", new DoubleType());
		query.addScalar("planPS", new DoubleType());
		query.addScalar("performPS", new DoubleType());
		query.addScalar("ratioPS", new DoubleType());
		query.addScalar("planDT", new DoubleType());
		query.addScalar("performDT", new DoubleType());
		query.addScalar("ratioDT", new DoubleType());
		query.addScalar("planHSHCTTHT", new DoubleType());
		query.addScalar("performHSHCTTHT", new DoubleType());
		query.addScalar("ratioHSHCTTHT", new DoubleType());
		query.addScalar("planHSHCDTHT", new DoubleType());
		query.addScalar("performHSHCDTHT", new DoubleType());
		query.addScalar("ratioHSHCDTHT", new DoubleType());
		query.addScalar("overdueKPIRentMB", new DoubleType());
		query.addScalar("overdueKPIKC", new DoubleType());
		query.addScalar("overdueKPILDT", new DoubleType());
		query.addScalar("overdueKPIHSHCTTHT", new DoubleType());
		query.addScalar("overdueKPIHSHCDTHT", new DoubleType());
		query.addScalar("overdueTKDT", new DoubleType());
		query.addScalar("rentMB", new DoubleType());
		query.addScalar("startUp", new DoubleType());
		query.addScalar("draftingRevenue", new DoubleType());
		query.addScalar("hshcTTHT", new DoubleType());
		query.addScalar("rentDBHT", new DoubleType());
		query.addScalar("overdueKPIDBHT", new DoubleType());
		query.addScalar("stationVccCode", new StringType());
		query.addScalar("stationVtNetCode", new StringType());
		// query sum
		querySum.addScalar("sumPlanTMB", new DoubleType());
		querySum.addScalar("sumPerformTMB", new DoubleType());
		querySum.addScalar("sumRatioTMB", new DoubleType());
		querySum.addScalar("sumPlanTKDT", new DoubleType());
		querySum.addScalar("sumPerformTKDT", new DoubleType());
		querySum.addScalar("sumRatioTKDT", new DoubleType());
		querySum.addScalar("sumPlanTTKDT", new DoubleType());
		querySum.addScalar("sumPerformTTKDT", new DoubleType());
		querySum.addScalar("sumRatioTTKDT", new DoubleType());
		querySum.addScalar("sumPlanKC", new DoubleType());
		querySum.addScalar("sumPerformKC", new DoubleType());
		querySum.addScalar("sumRatioKC", new DoubleType());
		querySum.addScalar("sumPlanDBHT", new DoubleType());
		querySum.addScalar("sumPerformDBHT", new DoubleType());
		querySum.addScalar("sumRatioDBHT", new DoubleType());
		querySum.addScalar("sumPlanPS", new DoubleType());
		querySum.addScalar("sumPerformPS", new DoubleType());
		querySum.addScalar("sumRatioPS", new DoubleType());
		querySum.addScalar("sumPlanDT", new DoubleType());
		querySum.addScalar("sumPerformDT", new DoubleType());
		querySum.addScalar("sumRatioDT", new DoubleType());
		querySum.addScalar("sumPlanHSHCTTHT", new DoubleType());
		querySum.addScalar("sumPerformHSHCTTHT", new DoubleType());
		querySum.addScalar("sumRatioHSHCTTHT", new DoubleType());
		querySum.addScalar("sumPlanHSHCDTHT", new DoubleType());
		querySum.addScalar("sumPerformHSHCDTHT", new DoubleType());
		querySum.addScalar("sumRatioHSHCDTHT", new DoubleType());
		querySum.addScalar("sumOverdueKPIRentMB", new DoubleType());
		querySum.addScalar("sumOverdueKPIKC", new DoubleType());
		querySum.addScalar("sumOverdueKPILDT", new DoubleType());
		querySum.addScalar("sumOverdueKPIHSHCTTHT", new DoubleType());
		querySum.addScalar("sumOverdueKPIHSHCDTHT", new DoubleType());
		querySum.addScalar("sumOverdueTKDT", new DoubleType());
		querySum.addScalar("sumRentMB", new DoubleType());
		querySum.addScalar("sumStartUp", new DoubleType());
		querySum.addScalar("sumDraftingRevenue", new DoubleType());
		querySum.addScalar("sumHshcTTHT", new DoubleType());
		querySum.addScalar("totalRecord", new IntegerType());
		querySum.addScalar("sumRentDBHT", new DoubleType());
		querySum.addScalar("sumOverdueKPIDBHT", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
		// querySum.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
		if (obj.getMonthYearD() != null && !obj.getMonthYearD().equals("")) {
			query.setParameter("monthYear", obj.getMonthYearD());
			querySum.setParameter("monthYear", obj.getMonthYearD());
		}

		if (StringUtils.isNotBlank(obj.getProvinceCodeD())) {
			query.setParameter("provinceCode", obj.getProvinceCodeD());
			querySum.setParameter("provinceCode", obj.getProvinceCodeD());
		}

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			querySum.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<RpKHBTSDTO> lstDto = query.list();
		int total = 0;

		if (lstDto.size() > 0) {
			List<Object[]> rs = querySum.list();
			for (Object[] objects : rs) {
				lstDto.get(0).setSumPlanTMB((Double) objects[0]);
				lstDto.get(0).setSumPerformTMB((Double) objects[1]);
				lstDto.get(0).setSumRatioTMB(
						(double) Math.round((((Double) objects[1] * 100) / (Double) objects[0]) * 100) / 100);

				lstDto.get(0).setSumPlanTKDT((Double) objects[3]);
				lstDto.get(0).setSumPerformTKDT((Double) objects[4]);
				// lstDto.get(0).setSumRatioTKDT((Double) objects[5]);
				lstDto.get(0).setSumRatioTKDT(
						(double) Math.round((((Double) objects[4] * 100) / (Double) objects[3]) * 100) / 100);
				lstDto.get(0).setSumPlanTTKDT((Double) objects[6]);
				lstDto.get(0).setSumPerformTTKDT((Double) objects[7]);
				// lstDto.get(0).setSumRatioTTKDT((Double) objects[8]);
				lstDto.get(0).setSumRatioTTKDT(
						(double) Math.round((((Double) objects[7] * 100) / (Double) objects[6]) * 100) / 100);
				lstDto.get(0).setSumPlanKC((Double) objects[9]);
				lstDto.get(0).setSumPerformKC((Double) objects[10]);
				// lstDto.get(0).setSumRatioKC((Double) objects[11]);
				lstDto.get(0).setSumRatioKC(
						(double) Math.round((((Double) objects[10] * 100) / (Double) objects[9]) * 100) / 100);
				lstDto.get(0).setSumPlanDBHT((Double) objects[12]);
				lstDto.get(0).setSumPerformDBHT((Double) objects[13]);
				// lstDto.get(0).setSumRatioDBHT((Double) objects[14]);
				lstDto.get(0).setSumRatioDBHT(
						(double) Math.round((((Double) objects[13] * 100) / (Double) objects[12]) * 100) / 100);
				lstDto.get(0).setSumPlanPS((Double) objects[15]);
				lstDto.get(0).setSumPerformPS((Double) objects[16]);
				// lstDto.get(0).setSumRatioPS((Double) objects[17]);
				lstDto.get(0).setSumRatioPS(
						(double) Math.round((((Double) objects[16] * 100) / (Double) objects[15]) * 100) / 100);
				lstDto.get(0).setSumPlanDT((Double) objects[18]);
				lstDto.get(0).setSumPerformDT((Double) objects[19]);
				// lstDto.get(0).setSumRatioDT((Double) objects[20]);
				lstDto.get(0).setSumRatioDT(
						(double) Math.round((((Double) objects[19] * 100) / (Double) objects[18]) * 100) / 100);
				lstDto.get(0).setSumPlanHSHCTTHT((Double) objects[21]);
				lstDto.get(0).setSumPerformHSHCTTHT((Double) objects[22]);
				// lstDto.get(0).setSumRatioHSHCTTHT((Double) objects[23]);
				lstDto.get(0).setSumRatioHSHCTTHT(
						(double) Math.round((((Double) objects[22] * 100) / (Double) objects[21]) * 100) / 100);
				lstDto.get(0).setSumPlanHSHCDTHT((Double) objects[24]);
				lstDto.get(0).setSumPerformHSHCDTHT((Double) objects[25]);
				// lstDto.get(0).setSumRatioHSHCDTHT((Double) objects[26]);
				lstDto.get(0).setSumRatioHSHCDTHT(
						(double) Math.round((((Double) objects[25] * 100) / (Double) objects[24]) * 100) / 100);
				lstDto.get(0).setSumOverdueKPIRentMB((Double) objects[27]);
				lstDto.get(0).setSumOverdueKPIKC((Double) objects[28]);
				lstDto.get(0).setSumOverdueKPILDT((Double) objects[29]);
				lstDto.get(0).setSumOverdueKPIHSHCTTHT((Double) objects[30]);
				lstDto.get(0).setSumOverdueKPIHSHCDTHT((Double) objects[31]);
				lstDto.get(0).setSumOverdueTKDT((Double) objects[32]);
				lstDto.get(0).setSumRentMB((Double) objects[33]);
				lstDto.get(0).setSumStartUp((Double) objects[34]);
				lstDto.get(0).setSumDraftingRevenue((Double) objects[35]);
				lstDto.get(0).setSumHshcTTHT((Double) objects[36]);
				total = (Integer) objects[37];
				lstDto.get(0).setSumRentDBHT((Double) objects[38]);
				lstDto.get(0).setSumOverdueKPIDBHT((Double) objects[39]);

			}
		}
		obj.setTotalRecord(total);
		return lstDto;
	}

	public List<RpKHBTSDTO> doSearchStation(RpKHBTSDTO obj) {
		List<RpKHBTSDTO> lstDto = new ArrayList<>();
		if (obj.getTypes() == 1) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId, "
							+ "province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_TMB planTMB, PERFORM_TMB performTMB, OVER_KPI_TMBT numberDateLimitKPI, (FINE_TMB/1000) numberFines ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_TMB,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planTMB", new DoubleType());
			query.addScalar("performTMB", new DoubleType());
			query.addScalar("numberDateLimitKPI", new IntegerType());
			query.addScalar("numberFines", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 2) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId, "
							+ "province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_TKDT planTKDT, PERFORM_TKDT performTKDT, OVER_KPI_TKDT numberDateLimitKPI ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_TKDT,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planTKDT", new DoubleType());
			query.addScalar("performTKDT", new DoubleType());
			query.addScalar("numberDateLimitKPI", new IntegerType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 3) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_TKDT planTKDT, PERFORM_TTKDT performTTKDT ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_TKDT,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planTKDT", new DoubleType());
			query.addScalar("performTTKDT", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 4) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_KC planKC, PERFORM_KC performKC, OVER_KPI_KC numberDateLimitKPI, (FINE_KC/1000) numberFines ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_KC,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planKC", new DoubleType());
			query.addScalar("performKC", new DoubleType());
			query.addScalar("numberDateLimitKPI", new IntegerType());
			query.addScalar("numberFines", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 5) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_DBHT planDBHT, PERFORM_DBHT performDBHT, Over_KPI_DBHT numberDateLimitKPI, (FINE_DBHT/1000) numberFines  ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_DBHT,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planDBHT", new DoubleType());
			query.addScalar("performDBHT", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 6) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_PS planPS, PERFORM_PS performPS ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_PS,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planPS", new DoubleType());
			query.addScalar("performPS", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 7) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_DT planDT, PERFORM_DT performDT, OVER_KPI_DT numberDateLimitKPI, (FINE_DT/1000) numberFines ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_DT,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planDT", new DoubleType());
			query.addScalar("performDT", new DoubleType());
			query.addScalar("numberDateLimitKPI", new IntegerType());
			query.addScalar("numberFines", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}
		if (obj.getTypes() == 8) {
			StringBuilder sql = new StringBuilder(
					"select wo_type_id woTypeId, tr_type_id trTypeId, Cat_Work_Item_Type_Id catWorkItemTypeId,"
							+ " province_code provinceCode, STATIONVCC_CODE vccCode,STATION_VTNET_CODE vtnetCode, PLAN_HSHC_TTHT planHSHCTTHT, PERFORM_HSHC_TTHT performHSHCTTHT, OVER_KPI_HSHC_TTHT numberDateLimitKPI, (FINE_HSHC_TTHT/1000) numberFines ");
			sql.append(" FROM RP_KH_BTS_HTCT ");
			sql.append(" where (nvl(PLAN_HSHC_TTHT,0)!=0 ) ");
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				sql.append(" and (MONTH||'/'||YEAR) = :monthYear ");
			}
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" and province_code = :provinceCode");
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				sql.append(" and (upper(STATIONVCC_CODE) like upper(:vccCode) escape '&') ");
			}
			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("woTypeId", new LongType());
			query.addScalar("trTypeId", new LongType());
			query.addScalar("catWorkItemTypeId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("vccCode", new StringType());
			query.addScalar("vtnetCode", new StringType());
			query.addScalar("planHSHCTTHT", new DoubleType());
			query.addScalar("performHSHCTTHT", new DoubleType());
			query.addScalar("numberDateLimitKPI", new IntegerType());
			query.addScalar("numberFines", new DoubleType());
			query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));
			if (obj.getMonthYear() != null && !obj.getMonthYear().equals("")) {
				query.setParameter("monthYear", obj.getMonthYear());
				queryCount.setParameter("monthYear", obj.getMonthYear());
			}

			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				queryCount.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getVccCode() != null && !obj.getVccCode().equals("")) {
				query.setParameter("vccCode", "%" + obj.getVccCode() + "%");
				queryCount.setParameter("vccCode", "%" + obj.getVccCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
			lstDto = query.list();
		}

		return lstDto;
	}

	public List<RpKHBTSDTO> doSearchWO(RpKHBTSDTO obj) {
		List<RpKHBTSDTO> lstDto = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
				"SELECT wo.Id woId, tr.Tr_Code trCode, wo.Wo_Code woCode, wo.Wo_Name woName, wo.created_date createDate,APPROVE_DATE_REPORT_WO approveDate, "
						+ " woStt.Name status,nvl(wo.Station_Code,cat.Code) stationCode, wo.Construction_Code constructionCode, item.Name itemName, "
						+ "wo.Contract_Code contractCode, wo.Finish_Date finishDate,wo.CD_Level_2_name cd2Name, wo.ft_name ftName, wo.money_value cost ");
		sql.append("FROM WO wo ");
		sql.append("LEFT JOIN  wo_tr tr ON tr.id=wo.tr_id ");
		sql.append("LEFT JOIN  APP_PARAM woStt  ON woStt.Code=wo.State AND PAR_TYPE = 'WO_XL_STATE' ");
		sql.append("LEFT JOIN cat_work_item_type item ON  wo.cat_work_item_type_id=item.cat_work_item_type_id ");
		sql.append("LEFT JOIN WO_MAPPING_STATION mapstation ON wo.id=mapstation.wo_id ");
		sql.append("LEFT JOIN cat_station cat ON cat.cat_station_id=mapstation.cat_station_id ");

		sql.append("WHERE  tr.tr_type_id= Case when :trTypeId = 0 then tr.tr_type_id else :trTypeId end ");
		sql.append("and wo.wo_type_id= Case when :woTypeId =0 then wo.wo_type_id else :woTypeId end ");
		sql.append(
				"and nvl(wo.Cat_Work_Item_Type_Id,0)= Case when :catWorkItemTypeId=0 then nvl(wo.Cat_Work_Item_Type_Id,0) else :catWorkItemTypeId end ");
		sql.append("and (wo.station_code = :vccCode OR cat.Code= :vccCode) and  wo.status=1 ");
		// sql.append("and (wo.Wo_order <= 5 or (:trTypeId !=31350 and :woTypeId !=1))
		// ");
		if (obj.getType().equals("5")) {
			sql.append("and wo.Wo_order <= 5 ");
		}
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("itemName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("ftName", new StringType());
		query.addScalar("cost", new DoubleType());
		query.addScalar("trCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("cd2Name", new StringType());
		query.addScalar("createDate", new DateType());
		query.addScalar("approveDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(RpKHBTSDTO.class));

		query.setParameter("trTypeId", obj.getTrTypeId());
		queryCount.setParameter("trTypeId", obj.getTrTypeId());

		query.setParameter("catWorkItemTypeId", obj.getCatWorkItemTypeId());
		queryCount.setParameter("catWorkItemTypeId", obj.getCatWorkItemTypeId());

		query.setParameter("woTypeId", obj.getWoTypeId());
		queryCount.setParameter("woTypeId", obj.getWoTypeId());

		query.setParameter("vccCode", obj.getVccCode());
		queryCount.setParameter("vccCode", obj.getVccCode());

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		// obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		lstDto = query.list();
		return lstDto;
	}

	public List<ReportBTSByDADTO> doSearchReportBTSByDA(ReportBTSByDADTO obj) {
		if (obj.getTypeBc().equals("1")) {
			StringBuilder sql = new StringBuilder("SELECT ");
			sql.append(
					" T1.PROVINCE_CODE provinceCode,count(*) count, sum(T1.TMB) tMB,sum(T1.TKDT) tKDT,sum(T1.THAM_TKDT) thamTKDT, ");
			sql.append(
					" (count(*)- sum(T1.TKDT) )tonChuaCoTKDTTham,sum(T1.VUONG) vuong,sum(T1.KC) daKC,sum(T1.DBHT) dBHT,sum(T1.PS) daPS,sum(T1.HSHC_TTHT) hSHCVeTTHT,sum(T1.HSHC_DTHT) hSHCVeDTHT,sum(T1.HSHC_NOT_TTHT) countTonHSHCChuaVeTTHT, ");
			sql.append(" sum(HSHC_NOT_TTHT_30) n1TonHSHCChuaVeTTHT, ");
			sql.append(" sum(T1.HSHC_NOT_TTHT_60) n2TonHSHCChuaVeTTHT,sum(T1.HSHC_NOT_TTHT_90) n3TonHSHCChuaVeTTHT, ");
			sql.append(
					" sum(T1.HSHC_NOT_TTHT_120) n4TonHSHCChuaVeTTHT,sum(T1.HSHC_NOT_DTHT) countTonHSHCChuaVeDHTH,sum(T1.HSHC_NOT_DTHT_30) n1TonHSHCChuaVeDHTH,sum(T1.HSHC_NOT_DTHT_60) n2TonHSHCChuaVeDHTH, ");
			sql.append(" sum(T1.HSHC_NOT_DTHT_90) n3TonHSHCChuaVeDHTH,sum(T1.HSHC_NOT_DTHT_120) n4TonHSHCChuaVeDHTH ");
			sql.append(" from RP_BTS_DA_HTCT T1 ");
			sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON T1.STATIONVCC_CODE = cs.CODE ");
			sql.append(" LEFT JOIN CONSTRUCTION c ON cs.CAT_STATION_ID = c.CAT_STATION_ID ");
			sql.append(" LEFT JOIN PROJECT_ESTIMATES pe ON c.CONTRACT_CODE = pe.CONSTRUCTION_CODE ");
			sql.append(" LEFT JOIN CTCT_IMS_OWNER.CONSTRUCTION_PROJECT cp ON pe.PROJECT_CODE = cp.PROJECT_CODE ");
			sql.append(" where TO_CHAR(T1.INSERT_TIME,'MM/yyyy') =:thang ");
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" AND T1.PROVINCE_CODE= :provinceCode ");
			}
			if(obj.getProjectCode() != null) {
				sql.append(" AND cp.PROJECT_CODE= :code ");
			}
			sql.append(" group by T1.PROVINCE_CODE ");
			sql.append(" order by T1.PROVINCE_CODE ");

			StringBuilder sqlSum = new StringBuilder("SELECT count(*) totalRecord, ");
			sqlSum.append(
					" sum(count) sumCount, sum(tMB) sumTMB, sum(tKDT) sumTKDT, sum(thamTKDT) sumThamTKDT, sum(tonChuaCoTKDTTham) sumTonChuaCoTKDTTham, sum(vuong) sumVuong, ");
			sqlSum.append(" sum(daKC) sumDaKC, sum(DBHT) sumDBHT, sum(daPS) sumDaPS,  ");
			sqlSum.append(
					" sum(hSHCVeTTHT) sumHSHCVeTTHT , sum(hSHCVeDTHT) sumHSHCVeDTHT, sum(countTonHSHCChuaVeTTHT) sumCountTonHSHCChuaVeTTHT, sum(n1TonHSHCChuaVeTTHT) sumN1TonHSHCChuaVeTTHT, ");
			sqlSum.append(
					" sum(n2TonHSHCChuaVeTTHT) sumN2TonHSHCChuaVeTTHT, sum(n3TonHSHCChuaVeTTHT) sumN3TonHSHCChuaVeTTHT, sum(n4TonHSHCChuaVeTTHT) sumN4TonHSHCChuaVeTTHT, ");
			sqlSum.append(
					" sum(countTonHSHCChuaVeDHTH) sumCountTonHSHCChuaVeDHTH, sum(n1TonHSHCChuaVeDHTH) sumN1TonHSHCChuaVeDHTH, sum(n2TonHSHCChuaVeDHTH) sumN2TonHSHCChuaVeDHTH , ");
			sqlSum.append(
					" sum(n3TonHSHCChuaVeDHTH) sumN3TonHSHCChuaVeDHTH, sum(n4TonHSHCChuaVeDHTH) sumN4TonHSHCChuaVeDHTH  FROM (");
			sqlSum.append(sql.toString());
			sqlSum.append(")");

			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery querySum = getSession().createSQLQuery(sqlSum.toString());

			query.addScalar("provinceCode", new StringType());
			query.addScalar("count", new DoubleType());
			query.addScalar("tMB", new DoubleType());
			query.addScalar("tKDT", new DoubleType());
			query.addScalar("thamTKDT", new DoubleType());
			query.addScalar("tonChuaCoTKDTTham", new DoubleType());
			query.addScalar("vuong", new DoubleType());
			query.addScalar("daKC", new DoubleType());
			query.addScalar("dBHT", new DoubleType());
			query.addScalar("daPS", new DoubleType());
			query.addScalar("hSHCVeTTHT", new DoubleType());
			query.addScalar("hSHCVeDTHT", new DoubleType());
			query.addScalar("countTonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n1TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n2TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n3TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n4TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("countTonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n1TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n2TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n3TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n4TonHSHCChuaVeDHTH", new DoubleType());

			querySum.addScalar("totalRecord", new DoubleType());
			querySum.addScalar("sumCount", new DoubleType());
			querySum.addScalar("sumTMB", new DoubleType());
			querySum.addScalar("sumTKDT", new DoubleType());
			querySum.addScalar("sumThamTKDT", new DoubleType());
			querySum.addScalar("sumTonChuaCoTKDTTham", new DoubleType());
			querySum.addScalar("sumVuong", new DoubleType());
			querySum.addScalar("sumDaKC", new DoubleType());
			querySum.addScalar("sumDBHT", new DoubleType());
			querySum.addScalar("sumDaPS", new DoubleType());
			querySum.addScalar("sumHSHCVeTTHT", new DoubleType());
			querySum.addScalar("sumHSHCVeDTHT", new DoubleType());
			querySum.addScalar("sumCountTonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN1TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN2TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN3TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN4TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumCountTonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN1TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN2TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN3TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN4TonHSHCChuaVeDHTH", new DoubleType());

			query.setResultTransformer(Transformers.aliasToBean(ReportBTSByDADTO.class));

			query.setParameter("thang", obj.getMonthYearD());
			querySum.setParameter("thang", obj.getMonthYearD());
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				querySum.setParameter("provinceCode", obj.getProvinceCode());
			}
			if(obj.getProjectCode() != null) {
				query.setParameter("code", obj.getProjectCode());
				querySum.setParameter("code", obj.getProjectCode());
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			List<ReportBTSByDADTO> lst = query.list();
			double total = 0d;

			if (lst.size() > 0) {
				List<Object[]> rs = querySum.list();
				for (Object[] objects : rs) {
					total = (Double) objects[0];
					lst.get(0).setSumCount((Double) objects[1]);
					lst.get(0).setSumTMB((Double) objects[2]);
					lst.get(0).setSumTKDT((Double) objects[3]);
					lst.get(0).setSumThamTKDT((Double) objects[4]);
					lst.get(0).setSumTonChuaCoTKDTTham((Double) objects[5]);
					lst.get(0).setSumVuong((Double) objects[6]);
					lst.get(0).setSumDaKC((Double) objects[7]);
					lst.get(0).setSumDBHT((Double) objects[8]);
					lst.get(0).setSumDaPS((Double) objects[9]);
					lst.get(0).setSumHSHCVeTTHT((Double) objects[10]);
					lst.get(0).setSumHSHCVeDTHT((Double) objects[11]);
					lst.get(0).setSumCountTonHSHCChuaVeTTHT((Double) objects[12]);
					lst.get(0).setSumN1TonHSHCChuaVeTTHT((Double) objects[13]);
					lst.get(0).setSumN2TonHSHCChuaVeTTHT((Double) objects[14]);
					lst.get(0).setSumN3TonHSHCChuaVeTTHT((Double) objects[15]);
					lst.get(0).setSumN4TonHSHCChuaVeTTHT((Double) objects[16]);
					lst.get(0).setSumCountTonHSHCChuaVeDHTH((Double) objects[17]);
					lst.get(0).setSumN1TonHSHCChuaVeDHTH((Double) objects[18]);
					lst.get(0).setSumN2TonHSHCChuaVeDHTH((Double) objects[19]);
					lst.get(0).setSumN3TonHSHCChuaVeDHTH((Double) objects[20]);
					lst.get(0).setSumN4TonHSHCChuaVeDHTH((Double) objects[21]);
				}
			}
			obj.setTotalRecord((int) total);
			return lst;
		} else {
			StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
			sql.append(
					" htct.PROVINCE_CODE provinceCode ,htct.STATIONVCC_CODE stationVccCode,htct.STATION_VTNET_CODE stationVtNetCode,1 count, htct.TMB,htct.TKDT tKDT,htct.THAM_TKDT thamTKDT, ");
			sql.append(
					" (1- htct.TKDT )tonChuaCoTKDTTham,htct.VUONG,htct.KC daKC,htct.DBHT dBHT,htct.PS daPS,htct.HSHC_TTHT hSHCVeTTHT,htct.HSHC_DTHT hSHCVeDTHT,htct.HSHC_NOT_TTHT countTonHSHCChuaVeTTHT,htct.HSHC_NOT_TTHT_30 n1TonHSHCChuaVeTTHT, ");
			sql.append(" htct.HSHC_NOT_TTHT_60 n2TonHSHCChuaVeTTHT,htct.HSHC_NOT_TTHT_90 n3TonHSHCChuaVeTTHT, ");
			sql.append(
					" htct.HSHC_NOT_TTHT_120 n4TonHSHCChuaVeTTHT,htct.HSHC_NOT_DTHT countTonHSHCChuaVeDHTH,htct.HSHC_NOT_DTHT_30 n1TonHSHCChuaVeDHTH, ");
			sql.append(
					" htct.HSHC_NOT_DTHT_60 n2TonHSHCChuaVeDHTH,htct.HSHC_NOT_DTHT_90 n3TonHSHCChuaVeDHTH,htct.HSHC_NOT_DTHT_120 n4TonHSHCChuaVeDHTH  ");
			// Huypq-04122021-start
			sql.append(", pe.PROJECT_CODE projectCode ");
			// Huy-end
			sql.append(" from RP_BTS_DA_HTCT htct ");
			// Huypq-04122021-start
			sql.append(" left join CAT_STATION cs on htct.STATIONVCC_CODE = cs.CODE and cs.STATUS!=0 ");
			sql.append(" left join construction cons on cs.CAT_STATION_ID = cons.CAT_STATION_ID and cons.STATUS!=0 ");
			sql.append(" LEFT JOIN PROJECT_ESTIMATES pe on cons.CONSTRUCTION_ID = pe.CONSTRUCTION_ID ");
			// Huy-end
			sql.append(" where TO_CHAR(htct.INSERT_TIME,'MM/yyyy') =:thang ");
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				sql.append(" AND htct.PROVINCE_CODE= :provinceCode ");
			}
			if (obj.getKeySearch() != null && !obj.getKeySearch().equals("")) {
				sql.append(
						" and (UPPER(htct.STATIONVCC_CODE) like UPPER(:keySearch) or UPPER(htct.STATION_VTNET_CODE) like UPPER (:keySearch) )");
			}
			if (StringUtils.isNotBlank(obj.getProjectCode())) {
				sql.append(
						" and UPPER(pe.PROJECT_CODE) like UPPER(:projectCode) escape '&' ");
			}
			sql.append(" order by htct.PROVINCE_CODE, htct.STATIONVCC_CODE ");

			StringBuilder sqlSum = new StringBuilder("SELECT count(*) totalRecord, ");
			sqlSum.append(
					" sum(count) sumCount, sum(tMB) sumTMB, sum(tKDT) sumTKDT, sum(thamTKDT) sumThamTKDT, sum(tonChuaCoTKDTTham) sumTonChuaCoTKDTTham, sum(vuong) sumVuong, ");
			sqlSum.append(" sum(daKC) sumDaKC, sum(DBHT) sumDBHT, sum(daPS) sumDaPS,  ");
			sqlSum.append(
					" sum(hSHCVeTTHT) sumHSHCVeTTHT , sum(hSHCVeDTHT) sumHSHCVeDTHT, sum(countTonHSHCChuaVeTTHT) sumCountTonHSHCChuaVeTTHT, sum(n1TonHSHCChuaVeTTHT) sumN1TonHSHCChuaVeTTHT, ");
			sqlSum.append(
					" sum(n2TonHSHCChuaVeTTHT) sumN2TonHSHCChuaVeTTHT, sum(n3TonHSHCChuaVeTTHT) sumN3TonHSHCChuaVeTTHT, sum(n4TonHSHCChuaVeTTHT) sumN4TonHSHCChuaVeTTHT, ");
			sqlSum.append(
					" sum(countTonHSHCChuaVeDHTH) sumCountTonHSHCChuaVeDHTH, sum(n1TonHSHCChuaVeDHTH) sumN1TonHSHCChuaVeDHTH, sum(n2TonHSHCChuaVeDHTH) sumN2TonHSHCChuaVeDHTH , ");
			sqlSum.append(
					" sum(n3TonHSHCChuaVeDHTH) sumN3TonHSHCChuaVeDHTH, sum(n4TonHSHCChuaVeDHTH) sumN4TonHSHCChuaVeDHTH  FROM (");
			sqlSum.append(sql.toString());
			sqlSum.append(")");

			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery querySum = getSession().createSQLQuery(sqlSum.toString());

			query.addScalar("provinceCode", new StringType());
			query.addScalar("stationVccCode", new StringType());
			query.addScalar("stationVtNetCode", new StringType());
			query.addScalar("count", new DoubleType());
			query.addScalar("tMB", new DoubleType());
			query.addScalar("tKDT", new DoubleType());
			query.addScalar("thamTKDT", new DoubleType());
			query.addScalar("tonChuaCoTKDTTham", new DoubleType());
			query.addScalar("vuong", new DoubleType());
			query.addScalar("daKC", new DoubleType());
			query.addScalar("dBHT", new DoubleType());
			query.addScalar("daPS", new DoubleType());
			query.addScalar("hSHCVeTTHT", new DoubleType());
			query.addScalar("hSHCVeDTHT", new DoubleType());
			query.addScalar("countTonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n1TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n2TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n3TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("n4TonHSHCChuaVeTTHT", new DoubleType());
			query.addScalar("countTonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n1TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n2TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n3TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("n4TonHSHCChuaVeDHTH", new DoubleType());
			query.addScalar("projectCode", new StringType());

			querySum.addScalar("totalRecord", new DoubleType());
			querySum.addScalar("sumCount", new DoubleType());
			querySum.addScalar("sumTMB", new DoubleType());
			querySum.addScalar("sumTKDT", new DoubleType());
			querySum.addScalar("sumThamTKDT", new DoubleType());
			querySum.addScalar("sumTonChuaCoTKDTTham", new DoubleType());
			querySum.addScalar("sumVuong", new DoubleType());
			querySum.addScalar("sumDaKC", new DoubleType());
			querySum.addScalar("sumDBHT", new DoubleType());
			querySum.addScalar("sumDaPS", new DoubleType());
			querySum.addScalar("sumHSHCVeTTHT", new DoubleType());
			querySum.addScalar("sumHSHCVeDTHT", new DoubleType());
			querySum.addScalar("sumCountTonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN1TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN2TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN3TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumN4TonHSHCChuaVeTTHT", new DoubleType());
			querySum.addScalar("sumCountTonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN1TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN2TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN3TonHSHCChuaVeDHTH", new DoubleType());
			querySum.addScalar("sumN4TonHSHCChuaVeDHTH", new DoubleType());

			query.setResultTransformer(Transformers.aliasToBean(ReportBTSByDADTO.class));

			query.setParameter("thang", obj.getMonthYearD());
			querySum.setParameter("thang", obj.getMonthYearD());
			if (obj.getProvinceCode() != null && !obj.getProvinceCode().equals("")) {
				query.setParameter("provinceCode", obj.getProvinceCode());
				querySum.setParameter("provinceCode", obj.getProvinceCode());
			}
			if (obj.getKeySearch() != null && !obj.getKeySearch().equals("")) {
				query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
				querySum.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
			if (StringUtils.isNotBlank(obj.getProjectCode())) {
				query.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
				querySum.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
			}
			if (obj.getPage() != null && obj.getPageSize() != null) {
				query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				query.setMaxResults(obj.getPageSize().intValue());
			}
			List<ReportBTSByDADTO> lst = query.list();
			double total = 0d;

			if (lst.size() > 0) {
				List<Object[]> rs = querySum.list();
				for (Object[] objects : rs) {
					total = (Double) objects[0];
					lst.get(0).setSumCount((Double) objects[1]);
					lst.get(0).setSumTMB((Double) objects[2]);
					lst.get(0).setSumTKDT((Double) objects[3]);
					lst.get(0).setSumThamTKDT((Double) objects[4]);
					lst.get(0).setSumTonChuaCoTKDTTham((Double) objects[5]);
					lst.get(0).setSumVuong((Double) objects[6]);
					lst.get(0).setSumDaKC((Double) objects[7]);
					lst.get(0).setSumDBHT((Double) objects[8]);
					lst.get(0).setSumDaPS((Double) objects[9]);
					lst.get(0).setSumHSHCVeTTHT((Double) objects[10]);
					lst.get(0).setSumHSHCVeDTHT((Double) objects[11]);
					lst.get(0).setSumCountTonHSHCChuaVeTTHT((Double) objects[12]);
					lst.get(0).setSumN1TonHSHCChuaVeTTHT((Double) objects[13]);
					lst.get(0).setSumN2TonHSHCChuaVeTTHT((Double) objects[14]);
					lst.get(0).setSumN3TonHSHCChuaVeTTHT((Double) objects[15]);
					lst.get(0).setSumN4TonHSHCChuaVeTTHT((Double) objects[16]);
					lst.get(0).setSumCountTonHSHCChuaVeDHTH((Double) objects[17]);
					lst.get(0).setSumN1TonHSHCChuaVeDHTH((Double) objects[18]);
					lst.get(0).setSumN2TonHSHCChuaVeDHTH((Double) objects[19]);
					lst.get(0).setSumN3TonHSHCChuaVeDHTH((Double) objects[20]);
					lst.get(0).setSumN4TonHSHCChuaVeDHTH((Double) objects[21]);
				}
			}
			obj.setTotalRecord((int) total);
			return lst;
		}
	}
	// taotq end 28092021
	
	//Huypq-12012022-start
	public List<ReportBTSByDADTO> doSearchReportResultPerform(ReportBTSByDADTO obj) {
		StringBuilder sql = new StringBuilder("");
		if (obj.getTypeBc().equals("1")) {
			sql = new StringBuilder(" SELECT " + 
					"	 sum(SUM(MACRO)) over() sumMacroNumber," +
					"	 sum(SUM(RRU)) over() sumRruNumber," +
					"	 sum(SUM(SMC)) over() sumSmcNumber," +
					"	 sum(SUM(THUE_MAT_BANG)) over() sumThueMb," +
					"	 sum(SUM(DA_CHOT_GIA)) over() sumDaChotGia," +
					"	 sum(SUM(TK_DT)) over() sumTKDT," +
					"	 sum(SUM(THAM_TK_DT)) over() sumThamTKDT," +
					"	 sum(SUM(DUYET_TK_DT)) over() sumDuyetTKDT," +
					"	 sum(SUM(TON_CHUA_TKDT)) over() sumTonChuaCoTKDTTham," +
					"	 sum(SUM(DU_DIEUKIEN_TRIENKHAI)) over() sumDuDieuKienTk," +
					"	 sum(SUM(VUONG_KC)) over() sumVuong," +
					"	 sum(SUM(DA_TRIEN_KHAI)) over() sumDaKC," +
					"	 sum(SUM(XONG_XD)) over() sumXongDBHT," +
					"	 sum(SUM(DONG_BO_HA_TANG)) over() sumDBHT," +
					"	 sum(SUM(DA_PHAT_SONG)) over() sumDaPS," +
					"	 sum(SUM(DANG_THI_CONG)) over() sumDangTc," +
					"	 sum(SUM(CHUA_TRIEN_KHAI)) over() sumChuaTk," +
					"	 sum(SUM(HSHC_TTHT)) over() sumHSHCVeTTHT," +
					"	 sum(SUM(HSHC_ĐTHT)) over() sumHSHCVeDTHT," +
					"	 sum(SUM(HSHC_TC)) over() sumHSHCVeTc," +
					"	 sum(SUM(TON_HSHC_TTHT_TONG)) over() sumCountTonHSHCChuaVeTTHT," +
					"	 sum(SUM(TON_HSHC_TTHT_N1)) over() sumN1TonHSHCChuaVeTTHT," +
					"	 sum(SUM(TON_HSHC_TTHT_N2)) over() sumN2TonHSHCChuaVeTTHT," +
					"	 sum(SUM(TON_HSHC_TTHT_N3)) over() sumN3TonHSHCChuaVeTTHT," +
					"	 sum(SUM(TON_HSHC_TTHT_N4)) over() sumN4TonHSHCChuaVeTTHT," +
					"	 sum(SUM(TON_HSHC_ĐTHT_TONG)) over() sumCountTonHSHCChuaVeDHTH," +
					"	 sum(SUM(TON_HSHC_ĐTHT_N1)) over() sumN1TonHSHCChuaVeDHTH," +
					"	 sum(SUM(TON_HSHC_ĐTHT_N2)) over() sumN2TonHSHCChuaVeDHTH," +
					"	 sum(SUM(TON_HSHC_ĐTHT_N3)) over() sumN3TonHSHCChuaVeDHTH," +
					"	 sum(SUM(TON_HSHC_ĐTHT_N4)) over() sumN4TonHSHCChuaVeDHTH," +
					"	 sum(SUM(TON_HSHC_TC_TONG)) over() sumCountTonHSHCChuaVeTC," +
					"	 sum(SUM(TON_HSHC_TC_N1)) over() sumN1TonHSHCChuaVeTC," +
					"	 sum(SUM(TON_HSHC_TC_N2)) over() sumN2TonHSHCChuaVeTC," +
					"	 sum(SUM(TON_HSHC_TC_N3)) over() sumN3TonHSHCChuaVeTC," +
					"	 sum(SUM(TON_HSHC_TC_N4)) over() sumN4TonHSHCChuaVeTC," +
					"    PROVINCE_CODE provinceCode, " + 
					"    COUNT(*) yearPlan, " + 
					"    SUM(MACRO) macroNumber, " + 
					"    SUM(RRU) rruNumber, " + 
					"    SUM(SMC) smcNumber, " + 
					"    SUM(THUE_MAT_BANG) thueMb, " + 
					"    round(100 * SUM(THUE_MAT_BANG) / COUNT(*),2) tyLeHt, " + 
					"    SUM(DA_CHOT_GIA) daChotGia, " + 
					"    SUM(TK_DT) tKDT, " + 
					"    SUM(THAM_TK_DT) thamTKDT, " + 
					"    SUM(DUYET_TK_DT) duyetTKDT, " + 
					"    SUM(TON_CHUA_TKDT) tonChuaCoTKDTTham, " + 
					"    SUM(DU_DIEUKIEN_TRIENKHAI) duDieuKienTk, " + 
					"    SUM(VUONG_KC) vuong, " + 
					"    SUM(DA_TRIEN_KHAI) daKC, " + 
					"    SUM(XONG_XD) xongDBHT, " + 
					"    SUM(DONG_BO_HA_TANG) dBHT, " + 
					"    SUM(DA_PHAT_SONG) daPS, " + 
					"    SUM(DANG_THI_CONG) dangTc, " + 
					"    SUM(CHUA_TRIEN_KHAI) chuaTk, " + 
					"    SUM(HSHC_TTHT) hSHCVeTTHT, " + 
					"    SUM(HSHC_ĐTHT) hSHCVeDTHT, " + 
					"    SUM(HSHC_TC) hSHCVeTc, " + 
					"    SUM(TON_HSHC_TTHT_TONG) countTonHSHCChuaVeTTHT, " + 
					"    SUM(TON_HSHC_TTHT_N1) n1TonHSHCChuaVeTTHT, " + 
					"    SUM(TON_HSHC_TTHT_N2) n2TonHSHCChuaVeTTHT, " + 
					"    SUM(TON_HSHC_TTHT_N3) n3TonHSHCChuaVeTTHT, " + 
					"    SUM(TON_HSHC_TTHT_N4) n4TonHSHCChuaVeTTHT, " + 
					"    SUM(TON_HSHC_ĐTHT_TONG) countTonHSHCChuaVeDHTH, " + 
					"    SUM(TON_HSHC_ĐTHT_N1) n1TonHSHCChuaVeDHTH, " + 
					"    SUM(TON_HSHC_ĐTHT_N2) n2TonHSHCChuaVeDHTH, " + 
					"    SUM(TON_HSHC_ĐTHT_N3) n3TonHSHCChuaVeDHTH, " + 
					"    SUM(TON_HSHC_ĐTHT_N4) n4TonHSHCChuaVeDHTH, " + 
					"    SUM(TON_HSHC_TC_TONG) countTonHSHCChuaVeTC, " + 
					"    SUM(TON_HSHC_TC_N1) n1TonHSHCChuaVeTC, " + 
					"    SUM(TON_HSHC_TC_N2) n2TonHSHCChuaVeTC, " + 
					"    SUM(TON_HSHC_TC_N3) n3TonHSHCChuaVeTC, " + 
					"    SUM(TON_HSHC_TC_N4) n4TonHSHCChuaVeTC " + 
					" FROM " + 
					"    CTCT_COMS_OWNER.RP_KH_YEAR_BTS_HTCT " + 
					" WHERE 1=1 ");
			if(StringUtils.isNotBlank(obj.getMonthYearD())) {
				sql.append(" AND year = :year ");
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				sql.append(" AND PROVINCE_CODE = :provinceCode ");
			}
			sql.append(" GROUP BY " + 
					"    PROVINCE_CODE ");
			sql.append(" ORDER BY PROVINCE_CODE ASC ");
		} else {
			sql = new StringBuilder("SELECT " + 
					"	 sum(MACRO) over() sumMacroNumber," +
					"	 sum(RRU) over() sumRruNumber," +
					"	 sum(SMC) over() sumSmcNumber," +
					"	 sum(THUE_MAT_BANG) over() sumThueMb," +
					"	 sum(DA_CHOT_GIA) over() sumDaChotGia," +
					"	 sum(TK_DT) over() sumTKDT," +
					"	 sum(THAM_TK_DT) over() sumThamTKDT," +
					"	 sum(DUYET_TK_DT) over() sumDuyetTKDT," +
					"	 sum(TON_CHUA_TKDT) over() sumTonChuaCoTKDTTham," +
					"	 sum(DU_DIEUKIEN_TRIENKHAI) over() sumDuDieuKienTk," +
					"	 sum(VUONG_KC) over() sumVuong," +
					"	 sum(DA_TRIEN_KHAI) over() sumDaKC," +
					"	 sum(XONG_XD) over() sumXongDBHT," +
					"	 sum(DONG_BO_HA_TANG) over() sumDBHT," +
					"	 sum(DA_PHAT_SONG) over() sumDaPS," +
					"	 sum(DANG_THI_CONG) over() sumDangTc," +
					"	 sum(CHUA_TRIEN_KHAI) over() sumChuaTk," +
					"	 sum(HSHC_TTHT) over() sumHSHCVeTTHT," +
					"	 sum(HSHC_ĐTHT) over() sumHSHCVeDTHT," +
					"	 sum(HSHC_TC) over() sumHSHCVeTc," +
					"	 sum(TON_HSHC_TTHT_TONG) over() sumCountTonHSHCChuaVeTTHT," +
					"	 sum(TON_HSHC_TTHT_N1) over() sumN1TonHSHCChuaVeTTHT," +
					"	 sum(TON_HSHC_TTHT_N2) over() sumN2TonHSHCChuaVeTTHT," +
					"	 sum(TON_HSHC_TTHT_N3) over() sumN3TonHSHCChuaVeTTHT," +
					"	 sum(TON_HSHC_TTHT_N4) over() sumN4TonHSHCChuaVeTTHT," +
					"	 sum(TON_HSHC_ĐTHT_TONG) over() sumCountTonHSHCChuaVeDHTH," +
					"	 sum(TON_HSHC_ĐTHT_N1) over() sumN1TonHSHCChuaVeDHTH," +
					"	 sum(TON_HSHC_ĐTHT_N2) over() sumN2TonHSHCChuaVeDHTH," +
					"	 sum(TON_HSHC_ĐTHT_N3) over() sumN3TonHSHCChuaVeDHTH," +
					"	 sum(TON_HSHC_ĐTHT_N4) over() sumN4TonHSHCChuaVeDHTH," +
					"	 sum(TON_HSHC_TC_TONG) over() sumCountTonHSHCChuaVeTC," +
					"	 sum(TON_HSHC_TC_N1) over() sumN1TonHSHCChuaVeTC," +
					"	 sum(TON_HSHC_TC_N2) over() sumN2TonHSHCChuaVeTC," +
					"	 sum(TON_HSHC_TC_N3) over() sumN3TonHSHCChuaVeTC," +
					"	 sum(TON_HSHC_TC_N4) over() sumN4TonHSHCChuaVeTC," +
					"    YEAR, " + 
					"    PROVINCE_CODE provinceCode, " + 
					"    STATION_CODE_VCC stationVccCode, " + 
					"    STATION_CODE_VTNET stationVtNetCode, " + 
					"    1 yearPlan, " + 
					"    MACRO macroNumber, " + 
					"    RRU rruNumber, " + 
					"    SMC smcNumber, " + 
					"    THUE_MAT_BANG thueMb, " + 
					"    100 * THUE_MAT_BANG tyLeHt, " + 
					"    DA_CHOT_GIA daChotGia, " + 
					"    TK_DT tKDT, " + 
					"    THAM_TK_DT thamTKDT, " + 
					"    DUYET_TK_DT duyetTKDT, " + 
					"    TON_CHUA_TKDT tonChuaCoTKDTTham, " + 
					"    DU_DIEUKIEN_TRIENKHAI duDieuKienTk, " + 
					"    VUONG_KC vuong, " + 
					"    DA_TRIEN_KHAI daKC, " + 
					"    XONG_XD xongDBHT, " + 
					"    DONG_BO_HA_TANG dBHT, " + 
					"    DA_PHAT_SONG daPS, " + 
					"    DANG_THI_CONG dangTc, " + 
					"    CHUA_TRIEN_KHAI chuaTk, " + 
					"    HSHC_TTHT hSHCVeTTHT, " + 
					"    HSHC_ĐTHT hSHCVeDTHT, " + 
					"    HSHC_TC hSHCVeTc, " + 
					"    TON_HSHC_TTHT_TONG countTonHSHCChuaVeTTHT, " + 
					"    TON_HSHC_TTHT_N1 n1TonHSHCChuaVeTTHT, " + 
					"    TON_HSHC_TTHT_N2 n2TonHSHCChuaVeTTHT, " + 
					"    TON_HSHC_TTHT_N3 n3TonHSHCChuaVeTTHT, " + 
					"    TON_HSHC_TTHT_N4 n4TonHSHCChuaVeTTHT, " + 
					"    TON_HSHC_ĐTHT_TONG countTonHSHCChuaVeDHTH, " + 
					"    TON_HSHC_ĐTHT_N1 n1TonHSHCChuaVeDHTH, " + 
					"    TON_HSHC_ĐTHT_N2 n2TonHSHCChuaVeDHTH, " + 
					"    TON_HSHC_ĐTHT_N3 n3TonHSHCChuaVeDHTH, " + 
					"    TON_HSHC_ĐTHT_N4 n4TonHSHCChuaVeDHTH, " + 
					"    TON_HSHC_TC_TONG countTonHSHCChuaVeTC, " + 
					"    TON_HSHC_TC_N1 n1TonHSHCChuaVeTC, " + 
					"    TON_HSHC_TC_N2 n2TonHSHCChuaVeTC, " + 
					"    TON_HSHC_TC_N3 n3TonHSHCChuaVeTC, " + 
					"    TON_HSHC_TC_N4 n4TonHSHCChuaVeTC " + 
					"	 FROM RP_KH_YEAR_BTS_HTCT where 1=1 ");
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				sql.append(" AND PROVINCE_CODE = :provinceCode ");
			}
			if(StringUtils.isNotBlank(obj.getMonthYearD())) {
				sql.append(" AND year = :year ");
			}
			if(StringUtils.isNotBlank(obj.getKeySearch())) {
				sql.append(" AND (UPPER(STATION_CODE_VCC) like UPPER(:keySearch) OR UPPER(STATION_CODE_VTNET) like UPPER(:keySearch) escape '&') ");
			}
			sql.append(" ORDER BY PROVINCE_CODE ASC ");
		}
		
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("provinceCode", new StringType());
		if (obj.getTypeBc().equals("2")) {
			query.addScalar("stationVccCode", new StringType());
			query.addScalar("stationVtNetCode", new StringType());
		}
		query.addScalar("yearPlan", new StringType());
		query.addScalar("macroNumber", new LongType());
		query.addScalar("rruNumber", new LongType());
		query.addScalar("smcNumber", new LongType());
		query.addScalar("thueMb", new LongType());
		query.addScalar("tyLeHt", new LongType());
		query.addScalar("daChotGia", new LongType());
		query.addScalar("tKDT", new DoubleType());
		query.addScalar("thamTKDT", new DoubleType());
		query.addScalar("duyetTKDT", new LongType());
		query.addScalar("tonChuaCoTKDTTham", new DoubleType());
		query.addScalar("duDieuKienTk", new LongType());
		query.addScalar("vuong", new DoubleType());
		query.addScalar("daKC", new DoubleType());
		query.addScalar("xongDBHT", new LongType());
		query.addScalar("dBHT", new DoubleType());
		query.addScalar("daPS", new DoubleType());
		query.addScalar("dangTc", new LongType());
		query.addScalar("chuaTk", new LongType());
		query.addScalar("hSHCVeTTHT", new DoubleType());
		query.addScalar("hSHCVeDTHT", new DoubleType());
		query.addScalar("hSHCVeTc", new LongType());
		query.addScalar("countTonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("n1TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("n2TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("n3TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("n4TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("countTonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("n1TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("n2TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("n3TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("n4TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("countTonHSHCChuaVeTC", new LongType());
		query.addScalar("n1TonHSHCChuaVeTC", new LongType());
		query.addScalar("n2TonHSHCChuaVeTC", new LongType());
		query.addScalar("n3TonHSHCChuaVeTC", new LongType());
		query.addScalar("n4TonHSHCChuaVeTC", new LongType());
		
		query.addScalar("sumMacroNumber", new LongType());
		query.addScalar("sumRruNumber", new LongType());
		query.addScalar("sumSmcNumber", new LongType());
		query.addScalar("sumThueMb", new LongType());
		query.addScalar("sumDaChotGia", new LongType());
		query.addScalar("sumTKDT", new DoubleType());
		query.addScalar("sumThamTKDT", new DoubleType());
		query.addScalar("sumDuyetTKDT", new LongType());
		query.addScalar("sumTonChuaCoTKDTTham", new DoubleType());
		query.addScalar("sumDuDieuKienTk", new LongType());
		query.addScalar("sumVuong", new DoubleType());
		query.addScalar("sumDaKC", new DoubleType());
		query.addScalar("sumXongDBHT", new LongType());
		query.addScalar("sumDBHT", new DoubleType());
		query.addScalar("sumDaPS", new DoubleType());
		query.addScalar("sumDangTc", new LongType());
		query.addScalar("sumChuaTk", new LongType());
		query.addScalar("sumHSHCVeTTHT", new DoubleType());
		query.addScalar("sumHSHCVeDTHT", new DoubleType());
		query.addScalar("sumHSHCVeTc", new LongType());
		query.addScalar("sumCountTonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("sumN1TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("sumN2TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("sumN3TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("sumN4TonHSHCChuaVeTTHT", new DoubleType());
		query.addScalar("sumCountTonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("sumN1TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("sumN2TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("sumN3TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("sumN4TonHSHCChuaVeDHTH", new DoubleType());
		query.addScalar("sumCountTonHSHCChuaVeTC", new LongType());
		query.addScalar("sumN1TonHSHCChuaVeTC", new LongType());
		query.addScalar("sumN2TonHSHCChuaVeTC", new LongType());
		query.addScalar("sumN3TonHSHCChuaVeTC", new LongType());
		query.addScalar("sumN4TonHSHCChuaVeTC", new LongType());
		
		if (StringUtils.isNotBlank(obj.getMonthYearD())) {
			query.setParameter("year", obj.getMonthYearD());
			queryCount.setParameter("year", obj.getMonthYearD());
		}
		if(StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}
		if (obj.getTypeBc().equals("2")) {
			if(StringUtils.isNotBlank(obj.getKeySearch())) {
				query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
				queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ReportBTSByDADTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		List<ReportBTSByDADTO> lst = query.list();
		
		if (lst.size() > 0) {
			for (ReportBTSByDADTO objects : lst) {
				lst.get(0).setSumMacroNumber(objects.getSumMacroNumber());
				lst.get(0).setSumRruNumber(objects.getSumRruNumber());
				lst.get(0).setSumSmcNumber(objects.getSumSmcNumber());
				lst.get(0).setSumThueMb(objects.getSumThueMb());
				lst.get(0).setSumDaChotGia(objects.getSumDaChotGia());
				lst.get(0).setSumTKDT(objects.getSumTKDT());
				lst.get(0).setSumThamTKDT(objects.getSumThamTKDT());
				lst.get(0).setSumDuyetTKDT(objects.getSumDuyetTKDT());
				lst.get(0).setSumTonChuaCoTKDTTham(objects.getSumTonChuaCoTKDTTham());
				lst.get(0).setSumDuDieuKienTk(objects.getSumDuDieuKienTk());
				lst.get(0).setSumVuong(objects.getSumVuong());
				lst.get(0).setSumDaKC(objects.getSumDaKC());
				lst.get(0).setSumXongDBHT(objects.getSumXongDBHT());
				lst.get(0).setSumDBHT(objects.getSumDBHT());
				lst.get(0).setSumDaPS(objects.getSumDaPS());
				lst.get(0).setSumDangTc(objects.getSumDangTc());
				lst.get(0).setSumChuaTk(objects.getSumChuaTk());
				lst.get(0).setSumHSHCVeTTHT(objects.getSumHSHCVeTTHT());
				lst.get(0).setSumHSHCVeDTHT(objects.getSumHSHCVeDTHT());
				lst.get(0).setSumHSHCVeTc(objects.getSumHSHCVeTc());
				lst.get(0).setSumCountTonHSHCChuaVeTTHT(objects.getSumCountTonHSHCChuaVeTTHT());
				lst.get(0).setSumN1TonHSHCChuaVeTTHT(objects.getSumN1TonHSHCChuaVeTTHT());
				lst.get(0).setSumN2TonHSHCChuaVeTTHT(objects.getSumN2TonHSHCChuaVeTTHT());
				lst.get(0).setSumN3TonHSHCChuaVeTTHT(objects.getSumN3TonHSHCChuaVeTTHT());
				lst.get(0).setSumN4TonHSHCChuaVeTTHT(objects.getSumN4TonHSHCChuaVeTTHT());
				lst.get(0).setSumCountTonHSHCChuaVeDHTH(objects.getSumCountTonHSHCChuaVeDHTH());
				lst.get(0).setSumN1TonHSHCChuaVeDHTH(objects.getSumN1TonHSHCChuaVeDHTH());
				lst.get(0).setSumN2TonHSHCChuaVeDHTH(objects.getSumN2TonHSHCChuaVeDHTH());
				lst.get(0).setSumN3TonHSHCChuaVeDHTH(objects.getSumN3TonHSHCChuaVeDHTH());
				lst.get(0).setSumN4TonHSHCChuaVeDHTH(objects.getSumN4TonHSHCChuaVeDHTH());
				lst.get(0).setSumCountTonHSHCChuaVeTC(objects.getSumCountTonHSHCChuaVeTC());
				lst.get(0).setSumN1TonHSHCChuaVeTC(objects.getSumN1TonHSHCChuaVeTC());
				lst.get(0).setSumN2TonHSHCChuaVeTC(objects.getSumN2TonHSHCChuaVeTC());
				lst.get(0).setSumN3TonHSHCChuaVeTC(objects.getSumN3TonHSHCChuaVeTC());
				lst.get(0).setSumN4TonHSHCChuaVeTC(objects.getSumN4TonHSHCChuaVeTC());
			}
		}
		
		return lst;
	}
	//Huy-end

	public List<ReportBTSByDADTO> getProjectForAutocomplete(ReportBTSByDADTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT PROJECT_NAME name, PROJECT_CODE code ");
		sql.append(" from CTCT_IMS_OWNER.CONSTRUCTION_PROJECT ");
		sql.append(" WHERE 1=1 ");
		if(obj.getName() != null && !obj.getName().trim().equals("")) {
			sql.append(" AND PROJECT_NAME like :name ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(ReportBTSByDADTO.class));
		if(obj.getName() != null && !obj.getName().trim().equals("")) {
			query.setParameter("name", "%"+ obj.getName() +"%");
		}
		sql.append(" AND ROWNUM < 30 ");
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		return query.list();
	}
}
