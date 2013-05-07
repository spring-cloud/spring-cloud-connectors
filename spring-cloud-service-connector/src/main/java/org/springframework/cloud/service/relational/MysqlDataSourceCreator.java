package org.springframework.cloud.service.relational;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MysqlDataSourceCreator extends DataSourceCreator<MysqlServiceInfo> {
	
	/**
	 * Validation query obtained from the MySQL reference manual:
	 * http://dev.mysql.com/doc/refman/5.1/en/connector-j-usagenotes-j2ee.html
	 */
	private static final String VALIDATION_QUERY = "/* ping */ SELECT 1";

	@Override
	public String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public String getValidationQuery() {
		return VALIDATION_QUERY;
	}

}
