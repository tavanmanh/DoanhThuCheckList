package com.viettel.coms.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ConstructionBO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@EnableTransactionManagement
@Transactional
@Repository("sysGroupDAO")
public class SysGroupDAO extends BaseFWDAOImpl<BaseFWModelImpl, Long>{
	
	public SysGroupDAO() {
        this.model = new ConstructionBO();
    }
	
    public SysGroupDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
	public SysGroupDTO getSysGroupLv2ById(Session session, Long id) {
    	StringBuilder stringBuilder = new StringBuilder("SELECT " + 
    			"    SYS_GROUP_ID sysGroupId, " + 
    			"    CODE code, " + 
    			"    NAME name " + 
    			"FROM " + 
    			"    sys_group " + 
    			"WHERE " + 
    			"    SYS_GROUP_ID = ( " + 
    			"        SELECT " + 
    			"            REGEXP_SUBSTR(PATH,'[^/]+',1,2) " + 
    			"        FROM " + 
    			"            sys_group " + 
    			"        WHERE " + 
    			"            SYS_GROUP_ID =:sysGroupId )");
    	
    	SQLQuery query = session.createSQLQuery(stringBuilder.toString());
    	
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
    	
		query.setParameter("sysGroupId", id);
		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
    	
		return (SysGroupDTO) query.uniqueResult();
	}
    
    @SuppressWarnings("unchecked")
   	public List<SysGroupDTO> getSysGroupLv3PHT(SysGroupDTO obj) {
       	StringBuilder sql = new StringBuilder("select PROVINCE_CODE catProvinceCode, CODE code, NAME name, SYS_GROUP_ID sysGroupId from SYS_GROUP where 1=1 ");
       	if(StringUtils.isNotBlank(obj.getGroupLevel())) {
       		sql.append(" AND GROUP_LEVEL=:groupLv ");
       	}
       	if(StringUtils.isNotBlank(obj.getCode())) {
       		sql.append(" AND code like :code ");
       	}
       	SQLQuery query = getSession().createSQLQuery(sql.toString());
       	
   		query.addScalar("sysGroupId", new LongType());
   		query.addScalar("code", new StringType());
   		query.addScalar("name", new StringType());
   		query.addScalar("catProvinceCode", new StringType());
       	
   		if(StringUtils.isNotBlank(obj.getGroupLevel())) {
       		query.setParameter("groupLv", obj.getGroupLevel());
       	}
       	if(StringUtils.isNotBlank(obj.getCode())) {
       		sql.append(" AND code like :code ");
       		query.setParameter("code", "%"+ obj.getCode() +"%");
       	}
   		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
       	
   		return query.list();
   	}

    public SysGroupDTO getListCdLv2BTS(Long cntContractId) {
		String sql = "SELECT sg.SYS_GROUP_ID sysGroupId ,sg.NAME name , sg.CODE code  FROM CTCT_CAT_OWNER.SYS_GROUP sg WHERE sg.PARENT_ID =  " +
				" (SELECT cnt.SYS_GROUP_ID  FROM CNT_CONTRACT cnt " +
				" JOIN CTCT_CAT_OWNER.SYS_GROUP sg2 ON cnt.SYS_GROUP_ID = sg2.SYS_GROUP_ID  " +
				" WHERE cnt.CNT_CONTRACT_ID = :cntContractId AND sg2.CODE in ('CNKT.HNI','CNCT.HNI','CNKT.HCM','CNCT.HCM')) AND sg.CODE LIKE '%_P.HT' " +
				" UNION " +
				" SELECT sg.SYS_GROUP_ID sysGroupId ,sg.NAME name , sg.CODE code  FROM CTCT_CAT_OWNER.SYS_GROUP sg WHERE sg.PARENT_ID =  " +
				" (SELECT cnt.SYS_GROUP_ID  FROM CNT_CONTRACT cnt " +
				" JOIN CTCT_CAT_OWNER.SYS_GROUP sg2 ON cnt.SYS_GROUP_ID = sg2.SYS_GROUP_ID  " +
				" WHERE cnt.CNT_CONTRACT_ID = :cntContractId AND sg2.CODE not in ('CNKT.HNI','CNCT.HNI','CNKT.HCM','CNCT.HCM')) AND sg.CODE LIKE '%_P.XD'";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.setParameter("cntContractId", cntContractId);

		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
		List<SysGroupDTO> sysGroupDTO= query.list();
		if(CollectionUtils.isEmpty(sysGroupDTO)){
			return null;
		}
		return sysGroupDTO.get(0);
	}

	public SysGroupDTO getListCdLv2BTSVHKT(Long cntContractId) {
		String sql = "SELECT sg.SYS_GROUP_ID sysGroupId ,sg.NAME name , sg.CODE code FROM  SYS_GROUP sg WHERE sg.PARENT_ID =  " +
				"  (SELECT " +
				"  cnt.SYS_GROUP_ID " +
				"  FROM " +
				"  CNT_CONTRACT cnt " +
				"  JOIN CTCT_CAT_OWNER.SYS_GROUP sg2 ON " +
				"  cnt.SYS_GROUP_ID = sg2.SYS_GROUP_ID " +
				"  WHERE " +
				"  cnt.CNT_CONTRACT_ID = :cntContractId) " +
				"  AND  (sg.CODE like '%_P.KT' or code like '%_P.VHKT') ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.setParameter("cntContractId", cntContractId);

		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
		List<SysGroupDTO> sysGroupDTO= query.list();
		if(CollectionUtils.isEmpty(sysGroupDTO)){
			return null;
		}
		return sysGroupDTO.get(0);
	}
}
