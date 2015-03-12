package org.springframework.cloud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.cloud.CloudTestUtil.getTestCloudConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.service.relational.MysqlDataSourceCreator;

/**
 * Test cases for service properties
 *
 * @author Ramnivas Laddad
 * @author Chris Schaefer
 */
public class CloudTest extends StubCloudConnectorTest {
	private List<ServiceConnectorCreator<?, ? extends ServiceInfo>> serviceCreators;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		serviceCreators = new ArrayList<ServiceConnectorCreator<?, ? extends ServiceInfo>>();
		serviceCreators.add(new MysqlDataSourceCreator());
	}
	
	@Test
	public void serviceConnectorCreation() {
		String serviceId = "mysql-db";
		CloudConnector stubCloudConnector = getTestCloudConnector(createMysqlService(serviceId));
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);
		
		DataSource serviceConnector = testCloud.getServiceConnector(serviceId, DataSource.class, null);
		assertNotNull(serviceConnector);
	}

	@Test
	public void serviceInfosForConnectorType() {
		String serviceId = "mysql-db";
		CloudConnector stubCloudConnector = getTestCloudConnector(createMysqlService(serviceId));
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);

		List<ServiceInfo> serviceInfos = testCloud.getServiceInfos(DataSource.class);
		assertEquals(1, serviceInfos.size());
	}
	
	@Test
	public void servicePropsTwoServicesOfTheSameLabel() {
		MysqlServiceInfo mysqlServiceInfo1 = createMysqlService("my-mysql1");
		MysqlServiceInfo mysqlServiceInfo2 = createMysqlService("my-mysql2");	
		CloudConnector stubCloudConnector = getTestCloudConnector(mysqlServiceInfo1, mysqlServiceInfo2);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);
		
		Properties cloudProperties = testCloud.getCloudProperties();
		assertRelationalProps("cloud.services.my-mysql1", mysqlServiceInfo1, cloudProperties);
		assertRelationalProps("cloud.services.my-mysql2", mysqlServiceInfo2, cloudProperties);
		assertNull(cloudProperties.get("cloud.services.mysql.connection.host"));
	}

	@Test
	public void servicePropsOneServiceOfTheSameLabel() {
		MysqlServiceInfo mysqlServiceInfo = createMysqlService("my-mysql");
		MongoServiceInfo mongoServiceInfo = createMongoService("my-mongo");
		CloudConnector stubCloudConnector = getTestCloudConnector(mysqlServiceInfo, mongoServiceInfo);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);
		
		Properties cloudProperties = testCloud.getCloudProperties();
		assertRelationalProps("cloud.services.my-mysql", mysqlServiceInfo, cloudProperties);
		assertMongoProps("cloud.services.my-mongo", mongoServiceInfo, cloudProperties);
		assertRelationalProps("cloud.services.mysql", mysqlServiceInfo, cloudProperties);
		assertMongoProps("cloud.services.mongo", mongoServiceInfo, cloudProperties);
	}
	
	@Test
	public void servicePropsRedis() {
		String serviceId = "my-redis";
		RedisServiceInfo redisServiceInfo = createRedisService(serviceId);
		CloudConnector stubCloudConnector = getTestCloudConnector(redisServiceInfo);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);

		Properties cloudProperties = testCloud.getCloudProperties();
		
		assertBasicProps("cloud.services.my-redis", redisServiceInfo, cloudProperties);
		assertBasicProps("cloud.services.redis", redisServiceInfo, cloudProperties);
	}

	@Test
	public void servicePropsRabbit() {
		String serviceId = "my-rabbit";
		AmqpServiceInfo rabbitServiceInfo = createRabbitService(serviceId);
		CloudConnector stubCloudConnector = getTestCloudConnector(rabbitServiceInfo);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);

		Properties cloudProperties = testCloud.getCloudProperties();
		assertRabbitProps("cloud.services.my-rabbit", rabbitServiceInfo, cloudProperties);
		assertRabbitProps("cloud.services.rabbitmq", rabbitServiceInfo, cloudProperties);
	}

	@Test
	public void servicePropsMongoMultipleHostsUriString() {
		String serviceId = "my-mongo-multiple-hosts-uri";
		MongoServiceInfo mongoServiceInfo = createMongoServiceWithMultipleHostsByUri(serviceId);
		CloudConnector stubCloudConnector = getTestCloudConnector(mongoServiceInfo);
		Cloud testCloud = new Cloud(stubCloudConnector, serviceCreators);

		Properties cloudProperties = testCloud.getCloudProperties();
		assertMongoPropsWithMultipleHostsByUri("cloud.services.my-mongo-multiple-hosts-uri", mongoServiceInfo, cloudProperties);
		assertMongoPropsWithMultipleHostsByUri("cloud.services.mongo", mongoServiceInfo, cloudProperties);
	}

	private void assertMongoPropsWithMultipleHostsByUri(String leadKey, MongoServiceInfo serviceInfo, Properties cloudProperties) {
		assertEquals(serviceInfo.getId(), cloudProperties.get(leadKey + ".id"));
		assertEquals(serviceInfo.getUri(), cloudProperties.get(leadKey + ".connection.uri"));
		assertEquals(-1, cloudProperties.get(leadKey + ".connection.port"));

		assertNull(cloudProperties.get(leadKey + ".connection.host"));
		assertNull(cloudProperties.get(leadKey + ".connection.username"));
		assertNull(cloudProperties.get(leadKey + ".connection.password"));
	}
	
	private void assertBasicProps(String leadKey, UriBasedServiceInfo serviceInfo, Properties cloudProperties) {
		assertEquals(serviceInfo.getId(), cloudProperties.get(leadKey + ".id"));

		assertEquals(serviceInfo.getUri(), cloudProperties.get(leadKey + ".connection.uri"));

		assertEquals(serviceInfo.getHost(), cloudProperties.get(leadKey + ".connection.host"));
		assertEquals(serviceInfo.getPort(), cloudProperties.get(leadKey + ".connection.port"));
		assertEquals(serviceInfo.getUserName(), cloudProperties.get(leadKey + ".connection.username"));
		assertEquals(serviceInfo.getPassword(), cloudProperties.get(leadKey + ".connection.password"));
	}
	
	private void assertRelationalProps(String leadKey, RelationalServiceInfo serviceInfo, Properties cloudProperties) {
		assertBasicProps(leadKey, serviceInfo, cloudProperties);		
		assertEquals(serviceInfo.getJdbcUrl(), cloudProperties.get(leadKey + ".connection.jdbcurl"));		
	}

	private void assertMongoProps(String leadKey, MongoServiceInfo serviceInfo, Properties cloudProperties) {
		assertBasicProps(leadKey, serviceInfo, cloudProperties);
	}

	private void assertRabbitProps(String leadKey, AmqpServiceInfo serviceInfo, Properties cloudProperties) {
		assertBasicProps(leadKey, serviceInfo, cloudProperties);
	}
}
