package com.viettel.coms.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.DetailTangentCustomerBO;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.dto.DetailTangentCustomerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("detailTangentCustomerDAO")
public class DetailTangentCustomerDAO extends BaseFWDAOImpl<DetailTangentCustomerBO, Long> {

	public DetailTangentCustomerDAO() {
		this.model = new DetailTangentCustomerBO();
	}

	public DetailTangentCustomerDAO(Session session) {
		this.session = session;
	}

	public DetailTangentCustomerDTO getDetailsTangentCustomer(TangentCustomerDTO obj) {
		DetailTangentCustomerDTO detailTangent= new DetailTangentCustomerDTO();
		if (null == obj.getPartnerType()) {
			return detailTangent;
		} else {
			StringBuilder sql = new StringBuilder(
					"SELECT T1.DETAIL_TANGENT_CUSTOMER_ID detailTangentCustomerId,T1.TANGENT_CUSTOMER_ID tangentCustomerId,T1.PARTNER_TYPE partnerType, " +
					"T1.SEX_B2C sexB2C,T1.EMAIL_B2C emailB2C, T1.PHONE_B2C phoneB2C," +
					"T1.BIRTH_YEAR_B2C birthYearB2C," +
					"T1.GEOGRAPHICAL_AREA geographicalArea,T1.FIELD_WORK fieldWork, T1.CUSTOMERS customers, T1.POSITION position" );
			if(obj.getPartnerType()==2){
				sql.append(",T1.UNIT_NAME unitName,T1.TAX_CODE taxCode,T1.BUSINESS business,T1.DETAIL_BUSINESS_OTHER detailBusinessOther,T1.EMPLOYEE employee,"  +
					" T1.FOUNDING founding,T1.EMAIL_B2B emailB2B, T1.REPRESENTATIVE representative, T1.POSITION_REPRESENTATIVE positionRepresentative,T1.SEX_REPRESENTATIVE sexRepresentative," +
					" T1.BIRTH_YEAR_REPRESENTATIVE birthYearRepresentative, T1.PHONE_REPRESENTATIVE phoneRepresentative, T1.EMAIL_REPRESENTATIVE emailRepresentative, T1.ADDRESS_REPRESENTATIVE addressRepresentative," + 
					" T1.DIRECT_CONTACT directContact, T1.POSITION_DIRECT_CONTACT positionDirectContact, T1.SEX_DIRECT_CONTACT sexDirectContact, T1.BIRTH_YEAR_DIRECT_CONTACT birthYearDirectContact," +
					" T1.PHONE_DIRECT_CONTACT phoneDirectContact, T1.EMAIL_DIRECT_CONTACT emailDirectContact, T1.ADDRESS_DIRECT_CONTACT addressDirectContact");
			}	
			sql.append(" FROM DETAIL_TANGENT_CUSTOMER T1 WHERE 1 = 1 ");
			
			if(obj.getTangentCustomerId() != null) {
				sql.append(" AND T1.TANGENT_CUSTOMER_ID = :tangentCustomerId ");
			}
			
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("detailTangentCustomerId", new LongType());
			query.addScalar("tangentCustomerId", new LongType());
			query.addScalar("sexB2C", new LongType());
			query.addScalar("phoneB2C", new StringType());
			query.addScalar("emailB2C", new StringType());
			query.addScalar("partnerType", new LongType());
			query.addScalar("birthYearB2C", new DateType());
			query.addScalar("geographicalArea", new StringType());
			query.addScalar("fieldWork", new StringType());
			query.addScalar("customers", new StringType());
			query.addScalar("position", new StringType());
			if(obj.getPartnerType()==2){
				query.addScalar("unitName", new StringType());
				query.addScalar("taxCode", new StringType());
				query.addScalar("business", new StringType());
				query.addScalar("detailBusinessOther", new StringType());
				
				query.addScalar("employee", new StringType());
				query.addScalar("founding", new DateType());
				query.addScalar("emailB2B", new StringType());
				query.addScalar("representative", new StringType());
				query.addScalar("positionRepresentative", new StringType());
				query.addScalar("sexRepresentative", new LongType());
				query.addScalar("birthYearRepresentative", new DateType());

				query.addScalar("phoneRepresentative", new StringType());
				query.addScalar("emailRepresentative", new StringType());
				query.addScalar("addressRepresentative", new StringType());
				
				query.addScalar("directContact", new StringType());
				query.addScalar("positionDirectContact", new StringType());
				query.addScalar("sexDirectContact", new LongType());
				query.addScalar("birthYearDirectContact", new DateType());
				
				
				query.addScalar("phoneDirectContact", new StringType());
				query.addScalar("emailDirectContact", new StringType());
				query.addScalar("addressDirectContact", new StringType());
				
			}
			
			query.setResultTransformer(Transformers.aliasToBean(DetailTangentCustomerDTO.class));
			if(obj.getTangentCustomerId() != null) {
				query.setParameter("tangentCustomerId", obj.getTangentCustomerId());
			}
			return (DetailTangentCustomerDTO) query.uniqueResult();
			
		}

	}

}
