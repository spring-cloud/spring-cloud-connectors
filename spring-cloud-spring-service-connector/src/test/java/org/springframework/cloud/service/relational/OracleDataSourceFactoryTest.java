package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.OracleServiceInfo;

public class OracleDataSourceFactoryTest extends AbstractDataSourceFactoryTest<OracleServiceInfo> {
	public OracleServiceInfo getTestServiceInfo(String id) {
		return new OracleServiceInfo(id, "oracle://username:pass@host:port/db");
	}
}
