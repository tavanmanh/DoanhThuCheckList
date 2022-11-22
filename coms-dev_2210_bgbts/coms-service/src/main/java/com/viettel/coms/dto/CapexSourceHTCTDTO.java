package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CapexSourceHTCTBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@XmlRootElement(name = "CAPEX_SOURCE_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CapexSourceHTCTDTO extends ComsBaseFWDTO<CapexSourceHTCTBO>{

	private static final long serialVersionUID = 1L;
	private Long capexSourceId;
	private String contentCapex;
	private String unit;
	private Long costCapex;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createdDate;
	private Long createdUserId;
	private Long updateUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updatedDate ;

	@JsonProperty("capexSourceId")
	public Long getCapexSourceId() {
		return capexSourceId;
	}

	public void setCapexSourceId(Long capexSourceId) {
		this.capexSourceId = capexSourceId;
	}

	@JsonProperty("contentCapex")
	public String getContentCapex() {
		return contentCapex;
	}

	public void setContentCapex(String contentCapex) {
		this.contentCapex = contentCapex;
	}

	@JsonProperty("unit")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@JsonProperty("costCapex")
	public Long getCostCapex() {
		return costCapex != null ? costCapex : 0;
	}

	public void setCostCapex(Long costCapex) {
		this.costCapex = costCapex;
	}

	@JsonProperty("createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@JsonProperty("createdUserId")
	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	@JsonProperty("updateUserId")
	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@JsonProperty("updatedDate")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String catchName() {
		return getCapexSourceId().toString();
	}

	@Override
	public Long getFWModelId() {
		return capexSourceId;
	}

	@Override
	public CapexSourceHTCTBO toModel() {
		CapexSourceHTCTBO capexSourceHTCTBO = new CapexSourceHTCTBO();
		capexSourceHTCTBO.setCapexSourceId(this.capexSourceId);
		capexSourceHTCTBO.setContentCapex(this.contentCapex);
		capexSourceHTCTBO.setUnit(this.unit);
		capexSourceHTCTBO.setCostCapex(this.costCapex);
		capexSourceHTCTBO.setCreatedDate(this.createdDate);
		capexSourceHTCTBO.setCreatedUserId(this.createdUserId);
		capexSourceHTCTBO.setUpdateUserId(this.updateUserId);
		capexSourceHTCTBO.setUpdatedDate(this.updatedDate);
		return capexSourceHTCTBO;
	}

}
