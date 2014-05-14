package org.springframework.cloud.service.document;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * Simplified access to creating MongoDB service objects.
 *
 * @author Ramnivas Laddad
 * @author Thomas Risberg
 *
 */
public class MongoDbFactoryCreator extends AbstractServiceConnectorCreator<MongoDbFactory, MongoServiceInfo> {
	@Override
	public MongoDbFactory create(MongoServiceInfo serviceInfo, ServiceConnectorConfig config) {
		try {
			MongoClientOptions mongoOptionsToUse = getMongoOptions((MongoDbFactoryConfig) config);
			ServerAddress serverAddress = null;
			if (serviceInfo.getPort() == -1) {
			    serverAddress = new ServerAddress(serviceInfo.getHost());
			} else {
			    serverAddress = new ServerAddress(serviceInfo.getHost(), serviceInfo.getPort());
			}
			MongoClient mongo = new MongoClient(serverAddress, mongoOptionsToUse);
            UserCredentials credentials = new UserCredentials(serviceInfo.getUserName(), serviceInfo.getPassword());
			SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, serviceInfo.getDatabase(), credentials);
			return configure(mongoDbFactory, (MongoDbFactoryConfig) config);
		} catch (UnknownHostException e) {
			throw new ServiceConnectorCreationException(e);
		} catch (MongoException e) {
			throw new ServiceConnectorCreationException(e);
		}
	}

	private MongoClientOptions getMongoOptions(MongoDbFactoryConfig config) {
		MongoClientOptions.Builder builder = null;
		
		Method builderMethod = ClassUtils.getMethodIfAvailable(MongoClientOptions.class, "builder");
		if (builderMethod != null) {
		    builder = (Builder) ReflectionUtils.invokeMethod(builderMethod, null);
		} else {
		    Constructor<Builder> builderConstructor = ClassUtils.getConstructorIfAvailable(MongoClientOptions.Builder.class);
		    try {
                builder = builderConstructor.newInstance(new Object[0]);
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
		}

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
		    
		return builder.build();
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
