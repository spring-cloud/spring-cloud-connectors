package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryCloudConfigTestHelper {
	
	public static void assertConfigProperties(MongoDbFactory connector, String writeConcern, Integer connectionsPerHost, Integer maxWaitTime) {
		if (connectionsPerHost == null) {
			connectionsPerHost = 100; // default
		}
		if (maxWaitTime == null) {
			maxWaitTime = 120000; // default
		}
		assertNotNull(connector);

		assertEquals(ReflectionTestUtils.getField(connector,  "writeConcern"), writeConcern == null ? null : WriteConcern.valueOf(writeConcern));
		
		MongoClient mongoClient = (MongoClient) ReflectionTestUtils.getField(connector, "mongo");
		assertEquals(connectionsPerHost.intValue(), mongoClient.getMongoClientOptions().getConnectionsPerHost());
		assertEquals(maxWaitTime.intValue(), mongoClient.getMongoClientOptions().getMaxWaitTime());
	}

}
