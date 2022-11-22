package com.viettel.coms.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricDetailBO;
import com.viettel.coms.bo.SendEmailBO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javassist.convert.Transformer;

@Repository("electricDetailDAO")
public class ElectricDetailDAO extends BaseFWDAOImpl<ElectricDetailBO, Long>{

	public ElectricDetailDAO() {
        this.model = new ElectricDetailBO();
    }

    public ElectricDetailDAO(Session session) {
        this.session = session;
    }
    
    @Autowired
	private SendEmailDAO sendEmailDAO;
    
    public Long updateDeviceStation(Long deviceId, String state) {
    	StringBuilder sql = new StringBuilder(" UPDATE DEVICE_STATION_ELECTRICAL set STATE=:state WHERE DEVICE_ID=:deviceId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("state", state);
    	query.setParameter("deviceId", deviceId);
    	return (long)query.executeUpdate();
    }

	public List<SysUserDTO> sendMailCD(Long deviceId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT DISTINCT SU.SYS_USER_ID userId, SU.PHONE_NUMBER phone, SU.EMAIL email, cs.CODE name ");
		sql.append(" FROM CTCT_VPS_OWNER.SYS_USER SU  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.USER_ROLE UR ON SU.SYS_USER_ID = UR.SYS_USER_ID  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.SYS_ROLE SR ON UR.SYS_ROLE_ID = SR.SYS_ROLE_ID  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.ROLE_PERMISSION RP ON SR.SYS_ROLE_ID = RP.SYS_ROLE_ID  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.PERMISSION P ON P.PERMISSION_ID = RP.PERMISSION_ID  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.AD_RESOURCE AR ON AR.AD_RESOURCE_ID = P.AD_RESOURCE_ID  ");
		sql.append(" INNER JOIN CTCT_VPS_OWNER.OPERATION O ON O.OPERATION_ID = P.OPERATION_ID  ");
		sql.append(" LEFT join CTCT_VPS_OWNER.USER_ROLE_DATA urd on UR.USER_ROLE_ID = urd.USER_ROLE_ID  ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON cs.CAT_PROVINCE_ID = sg.PROVINCE_ID ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.DEVICE_STATION_ELECTRICAL dse ON cs.CAT_STATION_ID = dse.STATION_ID ");
		sql.append(" WHERE AR.CODE = 'DEVICE_ELECTRICT_CNKT' AND O.CODE = 'APPROVED' AND dse.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("userId", new LongType());
		query.addScalar("email", new StringType());
		query.addScalar("phone", new StringType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		query.setParameter("deviceId", deviceId);
		return query.list();
	}
}
