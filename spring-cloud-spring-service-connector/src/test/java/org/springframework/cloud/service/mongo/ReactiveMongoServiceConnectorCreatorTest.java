package org.springframework.cloud.service.mongo;

import java.util.List;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.Test;

import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.document.ReactiveMongoDatabaseFactoryCreator;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;

/**
 * Test cases for reactive Mongo service connector creators.
 *
 * @author Mark Paluch
 */
public class ReactiveMongoServiceConnectorCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final String TEST_HOST_1 = "10.20.30.41";
	private static final String TEST_HOST_2 = "10.20.30.42";
	private static final int TEST_PORT = 1234;
	private static final int TEST_PORT_DEFAULT = 27017;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";
	private static final String TEST_DB = "mydb";
	private static final String MONGODB_SCHEME = "mongodb";
	private static final String[] TEST_HOSTS = new String[] { TEST_HOST, TEST_HOST_1, TEST_HOST_2 };

	private ReactiveMongoDatabaseFactoryCreator testCreator = new ReactiveMongoDatabaseFactoryCreator();

	@Test
	public void cloudMongoCreationNoConfig() throws Exception {
		MongoServiceInfo serviceInfo = new MongoServiceInfo("id", TEST_HOST, TEST_PORT, TEST_USERNAME, TEST_PASSWORD, TEST_DB);

		ReactiveMongoDatabaseFactory mongoDbFactory = testCreator.create(serviceInfo, null);
		assertNotNull(mongoDbFactory);

		MongoClient mongoClient = getMongoClientField(mongoDbFactory);

		MongoCredential credentials = mongoClient.getSettings().getCredential();

		List<ServerAddress> addresses = extractServerAddresses(mongoClient);
		assertEquals(1, addresses.size());

		ServerAddress address = addresses.get(0);

		assertEquals(serviceInfo.getHost(), address.getHost());
		assertEquals(serviceInfo.getPort(), address.getPort());
		assertEquals(serviceInfo.getUserName(), credentials.getUserName());
		assertNotNull(credentials.getPassword());

		// Don't do connector.getDatabase().getName() as that will try to initiate the connection
		assertEquals(serviceInfo.getDatabase(), ReflectionTestUtils.getField(mongoDbFactory, "databaseName"));
	}

	@Test
	public void cloudMongoCreationWithMultipleHostsByUri() throws Exception {
		String uri = String.format("%s://%s:%s@%s:%s/%s", MONGODB_SCHEME, TEST_USERNAME, TEST_PASSWORD,
				StringUtils.arrayToDelimitedString(TEST_HOSTS, ","), TEST_PORT, TEST_DB);

		MongoServiceInfo serviceInfo = new MongoServiceInfo("id", uri);

		ReactiveMongoDatabaseFactory mongoDbFactory = testCreator.create(serviceInfo, null);
		assertNotNull(mongoDbFactory);

		MongoClient mongoClient = getMongoClientField(mongoDbFactory);

		List<ServerAddress> addresses = extractServerAddresses(mongoClient);
		assertEquals(3, addresses.size());

		MongoCredential credentials = mongoClient.getSettings().getCredential();
		assertEquals(TEST_USERNAME, credentials.getUserName());
		assertNotNull(credentials.getPassword());

		// Don't do connector.getDatabase().getName() as that will try to initiate the connection
		assertEquals(TEST_DB, ReflectionTestUtils.getField(mongoDbFactory, "databaseName"));

		ServerAddress address1 = addresses.get(0);
		assertEquals(TEST_HOST, address1.getHost());
		assertEquals(TEST_PORT_DEFAULT, address1.getPort());

		ServerAddress address2 = addresses.get(1);
		assertEquals(TEST_HOST_1, address2.getHost());
		assertEquals(TEST_PORT_DEFAULT, address2.getPort());

		ServerAddress address3 = addresses.get(2);
		assertEquals(TEST_HOST_2, address3.getHost());
		assertEquals(TEST_PORT, address3.getPort());
	}

	@SuppressWarnings("unchecked")
	private List<ServerAddress> extractServerAddresses(MongoClient client) {
		return client.getSettings().getClusterSettings().getHosts();
	}

	private MongoClient getMongoClientField(ReactiveMongoDatabaseFactory mongoDbFactory) {
		MongoClient mongo = (MongoClient) ReflectionTestUtils.getField(mongoDbFactory, "mongo");
		assertNotNull(mongo);
		return mongo;
	}

}
