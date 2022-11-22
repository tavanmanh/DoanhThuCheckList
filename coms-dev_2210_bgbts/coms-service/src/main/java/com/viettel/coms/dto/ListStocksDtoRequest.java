package com.viettel.coms.dto;

import java.util.List;

public class ListStocksDtoRequest {
    private List<String> listType;
    private String levelStock;
    private Long sysGroupId;
    private String name;

    public List<String> getListType() {
        return listType;
    }

    public void setListType(List<String> listType) {
        this.listType = listType;
    }

    public String getLevelStock() {
        return levelStock;
    }

    public void setLevelStock(String levelStock) {
        this.levelStock = levelStock;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
