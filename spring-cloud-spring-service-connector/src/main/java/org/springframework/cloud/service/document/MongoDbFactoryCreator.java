package org.springframework.cloud.service.document;

import java.net.UnknownHostException;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

/**
 * Simplified access to creating MongoDB service objects.
 *
 * @author Ramnivas Laddad
 * @author Thomas Risberg
 * @author Chris Schaefer
 * @author Mark Paluch
 */
public class MongoDbFactoryCreator extends AbstractServiceConnectorCreator<MongoDbFactory, MongoServiceInfo> {
	@Override
	public MongoDbFactory create(MongoServiceInfo serviceInfo, ServiceConnectorConfig config) {
		try {
			MongoClientOptions.Builder mongoOptionsToUse = getMongoOptions((MongoDbFactoryConfig) config);

			SimpleMongoDbFactory mongoDbFactory = createMongoDbFactory(serviceInfo, mongoOptionsToUse);

			return configure(mongoDbFactory, (MongoDbFactoryConfig) config);
		} catch (UnknownHostException e) {
			throw new ServiceConnectorCreationException(e);
		} catch (MongoException e) {
			throw new ServiceConnectorCreationException(e);
		}
	}

	private SimpleMongoDbFactory createMongoDbFactory(MongoServiceInfo serviceInfo, MongoClientOptions.Builder mongoOptionsToUse) throws UnknownHostException {
		MongoClientURI mongoClientURI = new MongoClientURI(serviceInfo.getUri(), mongoOptionsToUse);
		MongoClient mongo = new MongoClient(mongoClientURI);
		return new SimpleMongoDbFactory(mongo, mongoClientURI.getDatabase());
	}

	private MongoClientOptions.Builder getMongoOptions(MongoDbFactoryConfig config) {
		MongoClientOptions.Builder builder = MongoClientOptions.builder();

		if (config != null) {
			if (config.getConnectionsPerHost() != null) {
				builder.connectionsPerHost(config.getConnectionsPerHost());
			}
			if (config.getMaxWaitTime() != null) {
				builder.maxWaitTime(config.getMaxWaitTime());
			}
			if (config.getWriteConcern() != null) {
				builder.writeConcern(new WriteConcern(config.getWriteConcern()));
			}
		}

		return builder;
	}

	public SimpleMongoDbFactory configure(SimpleMongoDbFactory mongoDbFactory, MongoDbFactoryConfig config) {
		if (config != null && config.getWriteConcern() != null) {
			WriteConcern writeConcern = WriteConcern.valueOf(config.getWriteConcern());
			if (writeConcern != null) {
				mongoDbFactory.setWriteConcern(writeConcern);
			}
		}
		return mongoDbFactory;
	}
}
