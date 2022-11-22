package com.viettel.coms.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/**
 * @author HoangNH38
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YearPlanSimpleOSDTO extends YearPlanOSDTO{
	private static final long serialVersionUID = 1L;
    private List<String> signStateList;
    private List<YearPlanDetailOSDTO> detailList;

    public List<YearPlanDetailOSDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<YearPlanDetailOSDTO> detailList) {
        this.detailList = detailList;
    }

    public List<String> getSignStateList() {
        return signStateList;
    }

    public void setSignStateList(List<String> signStateList) {
        this.signStateList = signStateList;
    }
}
