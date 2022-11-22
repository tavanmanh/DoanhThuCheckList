package com.viettel.erp.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.dto.ComsBaseFWDTO;
import com.viettel.erp.bo.ConstrConstructionsBO;

@XmlRootElement(name = "CONSTRUCTIONBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TCTCTSettlementDebtDTO extends ComsBaseFWDTO<ConstrConstructionsBO>{

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConstrConstructionsBO toModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
