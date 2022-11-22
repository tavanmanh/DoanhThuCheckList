package com.viettel.coms.business;
import java.util.List;
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;

public interface EffectiveCalculateDasCapexBusiness {
	List<EffectiveCalculateDasCapexDTO> getAssumptionsCapex(EffectiveCalculateDasCapexDTO obj);
	List<EffectiveCalculateDasCapexDTO> importDasCapex(String fileInput) throws Exception;
}
