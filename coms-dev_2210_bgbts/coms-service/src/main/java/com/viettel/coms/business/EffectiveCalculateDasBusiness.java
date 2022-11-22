package com.viettel.coms.business;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import java.util.List;

public interface EffectiveCalculateDasBusiness {
	
	List<EffectiveCalculateDasDTO> getAssumptions(EffectiveCalculateDasDTO obj);
	List<EffectiveCalculateDasDTO> importDas(String fileInput) throws Exception;
	
}
