package com.viettel.coms.dao;

import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.bo.TangentCustomerNoticeBO;
import com.viettel.coms.bo.UtilAttachDocumentBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.coms.dto.TangentCustomerNoticeDTO;
import com.viettel.coms.dto.TangentCustomerRequest;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.utils.DataUtil;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.utils.ValidateUtils;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
@Transactional
@Repository("tangentCustomerNoticeDAO")
public class TangentCustomerNoticeDAO extends BaseFWDAOImpl<TangentCustomerNoticeBO, Long> {

	public TangentCustomerNoticeDAO() {
		this.model = new TangentCustomerNoticeBO();
	}

	public TangentCustomerNoticeDAO(Session session) {
		this.session = session;
	}

	public List<TangentCustomerNoticeDTO> findByTangentCustomerId (Long id) {
		String sql = "SELECT ID id, TANGENT_CUSTOMER_STATUS tangentCustomerStatus, CALL_AT callAt, AUDIO_URL audioUrl, "
			+ "CUSTOMER_PHONE customerPhone, CREATED_DATE createdDate, STATE state, "
			+ "to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateString "
			+ "FROM TANGENT_CUSTOMER_NOTICE WHERE TANGENT_CUSTOMER_ID = :id and CONVERSATION_ID IS NOT NULL  ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("id", new LongType());
		query.addScalar("callAt", new LongType());
		query.addScalar("tangentCustomerStatus", new LongType());
		query.addScalar("audioUrl", new StringType());
		query.addScalar("customerPhone", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdDateString", new StringType());
		query.addScalar("state", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerNoticeDTO.class));
		query.setParameter("id", id);
		return query.list();
	}

}
