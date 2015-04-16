package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryCloudConfigTestHelper {
	
	public static void assertConfigProperties(MongoDbFactory connector, Integer writeConcernW, Integer connectionsPerHost, Integer maxWaitTime) {
		if (connectionsPerHost == null) {
			connectionsPerHost = 100; // default
		}
		if (maxWaitTime == null) {
			maxWaitTime = 120000; // default
		}
		assertNotNull(connector);

		WriteConcern writeConcern = (WriteConcern) ReflectionTestUtils.getField(connector, "writeConcern");
		if (writeConcernW != null) {
			assertNotNull(writeConcern);
			assertEquals(writeConcernW.intValue(), writeConcern.getW());
		}
		
		MongoClient mongoClient = (MongoClient) ReflectionTestUtils.getField(connector, "mongo");
		assertEquals(connectionsPerHost.intValue(), mongoClient.getMongoClientOptions().getConnectionsPerHost());
		assertEquals(maxWaitTime.intValue(), mongoClient.getMongoClientOptions().getMaxWaitTime());
	}

}
