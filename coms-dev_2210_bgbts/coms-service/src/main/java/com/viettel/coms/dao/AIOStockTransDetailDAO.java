/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import com.viettel.coms.bo.StockTransDetailVsmartBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * @author HOANM1
 * @version 1.0
 * @since 2019-03-10
 */
@Repository("aioStockTransDetailDAO")
public class AIOStockTransDetailDAO extends BaseFWDAOImpl<StockTransDetailVsmartBO, Long> {

    public AIOStockTransDetailDAO() {
        this.model = new StockTransDetailVsmartBO();
    }

    public AIOStockTransDetailDAO(Session session) {
        this.session = session;
    }
}
