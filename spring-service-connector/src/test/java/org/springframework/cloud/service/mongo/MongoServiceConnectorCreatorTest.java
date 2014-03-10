package org.springframework.cloud.service.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.document.MongoDbFactoryCreator;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoServiceConnectorCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final int TEST_PORT = 1234;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";
	private static final String TEST_DB = "mydb";

	
	private MongoDbFactoryCreator testCreator = new MongoDbFactoryCreator();

	@Test
	public void cloudMongoCreationNoConfig() throws Exception {
		MongoServiceInfo serviceInfo = createServiceInfo();

		MongoDbFactory mongoDbFactory = testCreator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, mongoDbFactory);
	}

	public MongoServiceInfo createServiceInfo() {
		return new MongoServiceInfo("id", TEST_HOST, TEST_PORT, TEST_DB, TEST_USERNAME, TEST_PASSWORD);
	}

	private void assertConnectorProperties(MongoServiceInfo serviceInfo, MongoDbFactory connector) {
		assertNotNull(connector);
		
		Mongo mongo = (Mongo) ReflectionTestUtils.getField(connector, "mongo");
		UserCredentials credentials = (UserCredentials) ReflectionTestUtils.getField(connector, "credentials");
		assertNotNull(mongo);
		ServerAddress address = mongo.getAddress();
		
		assertEquals(serviceInfo.getHost(), address.getHost());
		assertEquals(serviceInfo.getPort(), address.getPort());
		assertEquals(serviceInfo.getUserName(), ReflectionTestUtils.getField(credentials, "username"));
		assertEquals(serviceInfo.getPassword(), ReflectionTestUtils.getField(credentials, "password"));
		
		// Don't do connector.getDatabase().getName() as that will try to initiate the connection
		assertEquals(serviceInfo.getDatabase(), ReflectionTestUtils.getField(connector, "databaseName"));
	}
}
