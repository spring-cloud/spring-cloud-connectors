package org.springframework.cloud.service.document;

import java.util.concurrent.TimeUnit;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

/**
 * Simplified access to creating reactive MongoDB service objects.
 *
 * @author Mark Paluch
 */
public class ReactiveMongoDatabaseFactoryCreator extends AbstractServiceConnectorCreator<ReactiveMongoDatabaseFactory, MongoServiceInfo> {
	@Override
	public ReactiveMongoDatabaseFactory create(MongoServiceInfo serviceInfo, ServiceConnectorConfig config) {
		try {
			ConnectionString connectionString = new ConnectionString(serviceInfo.getUri());
			MongoClientSettings settings = getMongoClientSettings(connectionString, (MongoDbFactoryConfig) config);

			SimpleReactiveMongoDatabaseFactory mongoDbFactory = createMongoDbFactory(settings, connectionString);

			return configure(mongoDbFactory, (MongoDbFactoryConfig) config);
		} catch (MongoException e) {
			throw new ServiceConnectorCreationException(e);
		}
	}

	private static SimpleReactiveMongoDatabaseFactory createMongoDbFactory(MongoClientSettings settings, ConnectionString connectionString) {
		return new SimpleReactiveMongoDatabaseFactory(MongoClients.create(settings), connectionString.getDatabase());
	}

	private static MongoClientSettings getMongoClientSettings(ConnectionString connectionString,
			MongoDbFactoryConfig config) {

		ConnectionPoolSettings poolSettings = getConnectionPoolSettings(connectionString, config);

		MongoClientSettings.Builder builder = MongoClientSettings.builder()
				.clusterSettings(ClusterSettings.builder()
						.applyConnectionString(connectionString).build())
				.connectionPoolSettings(poolSettings)
				.serverSettings(ServerSettings.builder()
						.applyConnectionString(connectionString).build())
				.sslSettings(SslSettings.builder().applyConnectionString(connectionString)
						.build())
				.socketSettings(SocketSettings.builder()
						.applyConnectionString(connectionString).build());
		
		if (connectionString.getCredential() != null) {
            builder.credential(connectionString.getCredential());
        }
        if (connectionString.getReadPreference() != null) {
            builder.readPreference(connectionString.getReadPreference());
        }
        if (connectionString.getReadConcern() != null) {
            builder.readConcern(connectionString.getReadConcern());
        }
        if (connectionString.getWriteConcern() != null) {
            builder.writeConcern(connectionString.getWriteConcern());
        }
        if (connectionString.getApplicationName() != null) {
            builder.applicationName(connectionString.getApplicationName());
        }
        
		if (config != null && config.getWriteConcern() != null) {
			builder.writeConcern(new WriteConcern(config.getWriteConcern()));
		}

		return builder.build();
	}

	private static ConnectionPoolSettings getConnectionPoolSettings(ConnectionString connectionString, MongoDbFactoryConfig config) {
		
		ConnectionPoolSettings.Builder poolSettingBuilder = ConnectionPoolSettings
				.builder().applyConnectionString(connectionString);

		if (config != null) {
			if (config.getMaxWaitTime() != null) {
				poolSettingBuilder.maxWaitTime(config.getMaxWaitTime(),
						TimeUnit.MILLISECONDS);
			}
		}

		return poolSettingBuilder.build();
	}

	public SimpleReactiveMongoDatabaseFactory configure(SimpleReactiveMongoDatabaseFactory mongoDbFactory, MongoDbFactoryConfig config) {
		if (config != null && config.getWriteConcern() != null) {
			WriteConcern writeConcern = WriteConcern.valueOf(config.getWriteConcern());
			if (writeConcern != null) {
				mongoDbFactory.setWriteConcern(writeConcern);
			}
		}
		return mongoDbFactory;
	}
}
