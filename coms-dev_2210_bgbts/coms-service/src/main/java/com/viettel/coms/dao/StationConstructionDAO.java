package com.viettel.coms.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SharedSessionContract;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.StationConstructionDTO;
import com.viettel.coms.dto.StationConstructionOverviewDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
@EnableTransactionManagement
@Transactional
@Repository("stationConstructionDAO")
public class StationConstructionDAO extends BaseFWDAOImpl<BaseFWModelImpl, Long>{
	public StationConstructionDAO() {
        this.model = new BaseFWModelImpl() {
			
			@Override
			public BaseFWDTOImpl toDTO() {
				// TODO Auto-generated method stub
				return null;
			}
		};
    }
	
	public StationConstructionDAO(Session session) {
		this.session = session;
	}
	
	public List<StationConstructionOverviewDTO> doSearch(StationConstructionDTO obj) {
		List<StationConstructionOverviewDTO> rsOverview = Lists.newArrayList();
		
		StringBuilder sql = new StringBuilder(" select ");
		sql.append(" rp.area area,rp.provinceCode provinceCode,rp.stationVtNetCode stationVtNetCode,rp.stationVccCode stationVccCode,"
				+ " rp.otherCode otherCode,rp.projectCode projectCode,rp.projectName projectName,"
				+ " rp.stationType stationType,rp.longitude longitude,rp.latitude latitude,"
				+ " rp.mongCo mongCo,rp.phongMay phongMay,rp.tiepDia tiepDia,"
				+ " rp.maiDat maiDat,rp.address address,rp.doCaoCot doCaoCot,rp.loaiCot loaiCot,"
				+ " rp.dienAC dienAC,rp.cotTrongMoi cotTrongMoi,rp.vanChuyenBo vanChuyenBo,"
				+ " rp.thueNguon thueNguon,rp.giaCoDacBiet giaCoDacBiet,rp.donviThietKe donviThietKe,"
				+ " rp.nguoiThietKe nguoiThietKe,rp.coDuToan coDuToan,rp.Tham Tham,"
				+ " rp.bangiaoTTHT bangiaoTTHT,rp.ngayKC ngayKC,rp.ngayThueMB ngayThueMB,"
				+ " rp.ngayDB ngayDB,rp.ngayPS ngayPS,rp.Vuong Vuong,"
				+ " rp.nguyenNhanVuong nguyenNhanVuong,rp.kyXHDLan1 kyXHDLan1,rp.ngayDTTram ngayDTTram,"
				+ " rp.DTTram DTHaTang,rp.DTNguon DTNguon,rp.CPThueMB CPThueMB,"
				+ " rp.ngayTTHTPheDuyet ngayTTHTPheDuyet,rp.ngayNhanHSHC ngayNhanHSHC,rp.ngayTTDTHTDuyetWO ngayTTDTHTDuyetWO, rp.giatriQT giatriQT, "
				+ " rp.ngayDTNguon ngayDTNguon , rp.dungXHD dungXHD, rp.coTK coTK "
				+ " from RP_STATION_CONSTRUCTION rp where 1=1 ");
		
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(rp.stationVtNetCode) LIKE upper(:keySearch) escape '&'"
					+ "     OR upper(rp.stationVccCode) LIKE upper(:keySearch) escape '&' ) "
					+ "     OR upper(rp.provinceCode) LIKE upper(:keySearch) escape '&' ) ");
		}
		
		if (StringUtils.isNotEmpty(obj.getProjectCode())) {
			sql.append(" AND (upper(rp.projectCode) LIKE upper(:projectCode) escape '&'"
					+ "     OR upper(rp.projectName) LIKE upper(:projectCode) escape '&' ) ");
		}
		
		if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
			sql.append(" and rp.provinceCode  LIKE  :provinceCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("area", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("stationVtNetCode", new StringType());
		query.addScalar("stationVccCode", new StringType());
		
		query.addScalar("otherCode", new StringType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("projectName", new StringType());
		query.addScalar("stationType", new StringType());
		
		query.addScalar("longitude", new DoubleType());
		query.addScalar("latitude", new DoubleType());
		
		query.addScalar("mongCo", new StringType());
		query.addScalar("phongMay", new StringType());
		query.addScalar("tiepDia", new StringType());
		query.addScalar("maiDat", new StringType());
		
		query.addScalar("address", new StringType());
		query.addScalar("doCaoCot", new StringType());
		query.addScalar("loaiCot", new StringType());
		query.addScalar("dienAC", new StringType());
		query.addScalar("cotTrongMoi", new StringType());
		
		query.addScalar("vanChuyenBo", new StringType());
		query.addScalar("thueNguon", new StringType());
		query.addScalar("giaCoDacBiet", new StringType());
		query.addScalar("donviThietKe", new StringType());
		
		query.addScalar("nguoiThietKe", new StringType());
		query.addScalar("coDuToan", new StringType());
		query.addScalar("Tham", new StringType());
		query.addScalar("bangiaoTTHT", new StringType());
		
		query.addScalar("ngayThueMB", new StringType());
		query.addScalar("ngayKC", new StringType());
		query.addScalar("ngayDB", new StringType());
		query.addScalar("ngayPS", new StringType());

		query.addScalar("Vuong", new StringType());
		query.addScalar("nguyenNhanVuong", new StringType());
		query.addScalar("kyXHDLan1", new StringType());
		query.addScalar("ngayDTTram", new DateType());
		query.addScalar("DTHaTang", new BigDecimalType());
		query.addScalar("DTNguon", new BigDecimalType());
		query.addScalar("CPThueMB", new BigDecimalType());
		query.addScalar("ngayTTHTPheDuyet", new StringType());
		query.addScalar("ngayNhanHSHC", new StringType());
		query.addScalar("ngayTTDTHTDuyetWO", new StringType());
		query.addScalar("giatriQT", new BigDecimalType());
		query.addScalar("ngayDTNguon", new StringType());
		query.addScalar("dungXHD", new StringType());
		query.addScalar("coTK", new StringType());
		
		
		query.setResultTransformer(Transformers.aliasToBean(StationConstructionDTO.class));
		
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		
		if (StringUtils.isNotEmpty(obj.getProjectCode())) {
			query.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
			
		}
		
		if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
			query.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
		}
		
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		StringBuilder sqlCountValue = new StringBuilder("");
		if(obj.getTypeBc().equals("1")==true) {
			sqlCountValue.append("SELECT ");
			sqlCountValue.append(" sum(case when stationVtNetCode is null then 0 else 1 end)  sumStationVtNet, ");
			sqlCountValue.append(" sum(case when stationVccCode is null then 0 else 1 end) sumStationVcc, ");
			sqlCountValue.append(" sum(case when projectCode is null then 0 else 1 end) sumProject "
					+ "FROM ( ");
			sqlCountValue.append(sql.toString());
			sqlCountValue.append(" and rp.projectName is not null)");
			
			
			SQLQuery queryCountValue = getSession().createSQLQuery(sqlCountValue.toString());
			queryCountValue.addScalar("sumStationVtNet", new LongType());
			queryCountValue.addScalar("sumStationVcc", new LongType());
			queryCountValue.addScalar("sumProject", new LongType());
			
			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
				queryCountValue.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
			
			if (StringUtils.isNotEmpty(obj.getProjectCode())) {
				queryCountValue.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
			}
			
			if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
				queryCountValue.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
			}
			
			List<StationConstructionDTO> lstDto = query.list();
	        
	        if(lstDto.size() > 0) {
	        	List<Object[]> rs = queryCountValue.list();   	
	            for (Object[] objects : rs) {
	            	lstDto.get(0).setSumStationVtNet((Long) objects[0]);
	            	lstDto.get(0).setSumStationVcc((Long) objects[1]);
	            	lstDto.get(0).setSumProject((Long) objects[2]);
	            }
	        }
	        StationConstructionOverviewDTO add = new StationConstructionOverviewDTO();
	        add.setListStation(lstDto);
	        rsOverview.add(add);
		}
		
		StringBuilder sqlCountOverview = new StringBuilder("");
		if(obj.getTypeBc().equals("2")==true) {
			sqlCountOverview.append("SELECT ");
			sqlCountOverview.append(" projectCode projectCode, projectName projectName,sum(case when projectCode is null then 0 else 1 end) sumTaoMa, ");
			
			sqlCountOverview.append(" sum(case when ngayThueMB is null then 0 else 1 end)  sumThueMB,   ");
			sqlCountOverview.append(" sum(case when ngayKC is null then 0 else 1 end)  sumKC,   ");
			sqlCountOverview.append(" sum(case when ngayDB is null then 0 else 1 end)  sumDB,   ");
			sqlCountOverview.append(" sum(case when ngayPS is null then 0 else 1 end)  sumPS,   ");
			sqlCountOverview.append(" sum(case when ngayDTTram is null then 0 else 1 end) sumDT   ");
			sqlCountOverview.append("FROM ( ");
			sqlCountOverview.append(sql.toString());
			sqlCountOverview.append(" )  ");
			sqlCountOverview.append(" GROUP BY projectCode,projectName ORDER BY COUNT(projectCode) ASC  ");
			SQLQuery queryCountOverview  = getSession().createSQLQuery(sqlCountOverview.toString());
			
			queryCountOverview.addScalar("projectCode", new StringType());
			queryCountOverview.addScalar("projectName", new StringType());
			queryCountOverview.addScalar("sumTaoMa", new LongType());
			queryCountOverview.addScalar("sumThueMB", new LongType());
			queryCountOverview.addScalar("sumKC", new LongType());
			queryCountOverview.addScalar("sumDB", new LongType());
			queryCountOverview.addScalar("sumPS", new LongType());
			queryCountOverview.addScalar("sumDT", new LongType());
			queryCountOverview.setResultTransformer(Transformers.aliasToBean(StationConstructionOverviewDTO.class));
			
			if (StringUtils.isNotEmpty(obj.getProjectCode())) {
				queryCountOverview.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
			}
			
			if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
				queryCountOverview.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
			}
			
			if (obj.getPageSize() != null) {
				queryCountOverview.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
				queryCountOverview.setMaxResults(obj.getPageSize().intValue());
			}
            
            sqlCountValue.append("SELECT ");
			sqlCountValue.append(" sum(sumTaoMa)  allTaoMa, ");
			sqlCountValue.append(" sum(sumThueMB) allThueMB, ");
			sqlCountValue.append(" sum(sumKC)  allKC, ");
			sqlCountValue.append(" sum(sumDB)  allDB, ");
			sqlCountValue.append(" sum(sumPS) allPS, ");
			sqlCountValue.append(" sum(sumDT)  allDT ");
			sqlCountValue.append("FROM ( ");
			sqlCountValue.append(sqlCountOverview.toString());
			sqlCountValue.append(" )");

			
			SQLQuery queryCountValue = getSession().createSQLQuery(sqlCountValue.toString());
			queryCountValue.addScalar("allTaoMa", new LongType());
			queryCountValue.addScalar("allThueMB", new LongType());
			queryCountValue.addScalar("allKC", new LongType());
			queryCountValue.addScalar("allDB", new LongType());
			queryCountValue.addScalar("allPS", new LongType());
			queryCountValue.addScalar("allDT", new LongType());
			
			if (StringUtils.isNotEmpty(obj.getKeySearch())) {
				queryCountValue.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			}
			
			if (StringUtils.isNotEmpty(obj.getProjectCode())) {
				queryCountValue.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
			}
			
			if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
				queryCountValue.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
			}
			
			rsOverview = queryCountOverview.list();
            

	        if(rsOverview.size() > 0) {
	        	List<Object[]> rs = queryCountValue.list();   	
	            for (Object[] objects : rs) {
	            	rsOverview.get(0).setAllTaoMa((Long) objects[0]);
	            	rsOverview.get(0).setAllThueMB((Long) objects[1]);
	            	rsOverview.get(0).setAllKC((Long) objects[2]);
	            	rsOverview.get(0).setAllDB((Long) objects[3]);
	            	rsOverview.get(0).setAllPS((Long) objects[4]);
	            	rsOverview.get(0).setAllDT((Long) objects[5]);
	            }
	        }

		}
		
		StringBuilder sqlCountRecord = new StringBuilder("SELECT  COUNT(*) FROM ( ");
		if(obj.getTypeBc().equals("1")==true) {
			sqlCountRecord.append(sql.toString());
		}else sqlCountRecord.append(sqlCountOverview.toString());
		sqlCountRecord.append(" )");
		
		SQLQuery queryCountRecord  = getSession().createSQLQuery(sqlCountRecord.toString());
		
		if (StringUtils.isNotEmpty(obj.getProjectCode())) {
			queryCountRecord.setParameter("projectCode", "%" + obj.getProjectCode() + "%");
		}
		
		if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
			queryCountRecord.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
		}
		
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			queryCountRecord.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		
        obj.setTotalRecord(((BigDecimal) queryCountRecord.uniqueResult()).intValue());
        return rsOverview;
	}


}
