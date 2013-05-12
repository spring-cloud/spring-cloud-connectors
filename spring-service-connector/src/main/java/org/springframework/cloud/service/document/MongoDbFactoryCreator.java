package org.springframework.cloud.service.document;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.net.UnknownHostException;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.ReflectionUtils;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
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
			UserCredentials credentials = new UserCredentials(serviceInfo.getUserName(), serviceInfo.getPassword());
			Mongo mongo = null;
			MongoOptions mongoOptionsToUse = getMongoOptions((MongoDbFactoryConfig) config);
			if (mongoOptionsToUse != null) {
				ServerAddress serverAddress = new ServerAddress(serviceInfo.getHost(), serviceInfo.getPort());
				mongo = new Mongo(serverAddress, mongoOptionsToUse);
			} else {
				mongo = new Mongo(serviceInfo.getHost(), serviceInfo.getPort());
			}
			SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, serviceInfo.getDatabase(), credentials);
			return configure(mongoDbFactory, (MongoDbFactoryConfig) config);
		} catch (UnknownHostException e) {
			throw new ServiceConnectorCreationException(e);
		} catch (MongoException e) {
			throw new ServiceConnectorCreationException(e);
		}
	}

	private MongoOptions getMongoOptions(MongoDbFactoryConfig config) {
		if (config == null) {
			return null;
		}
		MongoOptions mongoOptions = null;
		BeanWrapper source = new BeanWrapperImpl(config);
		for (PropertyDescriptor pd : source.getPropertyDescriptors()) {
			String property = pd.getName();
			if (!"class".equals(property) && source.isReadableProperty(property) &&
						source.getPropertyValue(property) != null) {
				Field field = ReflectionUtils.findField(MongoOptions.class, property);
				if (field != null) {
					if (mongoOptions == null) {
						mongoOptions = new MongoOptions();
					}
					ReflectionUtils.setField(field, mongoOptions, source.getPropertyValue(property));
				}
			}
		}
		return mongoOptions;
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
