package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.MysqlServiceInfo;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlDataSourceCreator extends DataSourceCreator<MysqlServiceInfo> {
	public static final String[] DRIVERS = new String[]{"org.mariadb.jdbc.Driver", "com.mysql.jdbc.Driver"};
	/**
	 * Validation query obtained from the MySQL reference manual:
	 * https://dev.mysql.com/doc/refman/5.1/en/connector-j-usagenotes-j2ee.html
	 */
	public static final String VALIDATION_QUERY = "/* ping */ SELECT 1";

	public MysqlDataSourceCreator() {
	    super("spring-cloud.mysql.driver", DRIVERS, VALIDATION_QUERY);
	}

}
