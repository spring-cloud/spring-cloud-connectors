package org.springframework.cloud.cloudfoundry;

/**
 * An extension point that allows service data to be processed after it is read from {@literal VCAP_SERVICES}.
 */
public interface ServiceDataPostProcessor {
	/**
	 * Process raw service data as read from {@literal VCAP_SERVICES}.
	 *
	 * This method will be called after the {@literal VCAP_SERVICES} environment variable has been read from
	 * the environment and transformed from JSON text into a {@link CloudFoundryRawServiceData} data structure.
	 *
	 * If the {@literal VCAP_SERVICES} environment variable for an application contains the following:
	 *
	 * <pre>
	 * {@code
	 * "VCAP_SERVICES": {
	 *   "mysql": [
	 *     {
	 *       "label": "mysql",
	 *       "name": "mysql-db",
	 *       "plan": "100mb",
	 *       "tags": [ "mysql", "relational" ],
	 *       "credentials": {
	 *         "jdbcUrl": "jdbc:mysql://mysql-broker:3306/db?user=username\u0026password=password",
	 *         "uri": "mysql://username:password@mysql-broker:3306/db?reconnect=true",
	 *       }
	 *     }
	 *   ],
	 *   "rabbitmq": [
	 *     {
	 *       "label": "rabbitmq",
	 *       "name": "rabbit-queue",
	 *       "plan": "standard",
	 *       "tags": [ "rabbitmq", "messaging" ],
	 *       "credentials": {
	 *         "http_api_uri": "https://username:password@rabbitmq-broker:12345/api",
	 *         "uri": "amqp://username:password@rabbitmq-broker/vhost",
	 *       }
	 *     }
	 *   ]
	 * }
	 * }
	 * </pre>
	 *
	 * Then the {@link CloudFoundryRawServiceData} data structure would contain the equivalent of this:
	 *
	 * <pre>
	 * {@code
	 * {
	 *   "mysql": [
	 *     {
	 *       "label": "mysql",
	 *       "name": "mysql-db",
	 *       "plan": "100mb",
	 *       "tags": [ "mysql", "relational" ],
	 *       "credentials": {
	 *         "jdbcUrl": "jdbc:mysql://mysql-broker:3306/db?user=username\u0026password=password",
	 *         "uri": "mysql://username:password@mysql-broker:3306/db?reconnect=true",
	 *       }
	 *     }
	 *   ]
	 *   "rabbitmq": [
	 *     {
	 *       "label": "rabbitmq",
	 *       "name": "rabbit-queue",
	 *       "plan": "standard",
	 *       "tags": [ "rabbitmq", "messaging" ],
	 *       "credentials": {
	 *         "http_api_uri": "https://username:password@rabbitmq-broker:12345/api",
	 *         "uri": "amqp://username:password@rabbitmq-broker/vhost",
	 *       }
	 *     }
	 *   ]
	 * }
	 * }
	 * </pre>
	 *
	 * @param serviceData the service data parsed from {@literal VCAP_SERVICES}
	 * @return the provided {@literal serviceData} with modifications
	 */
	CloudFoundryRawServiceData process(CloudFoundryRawServiceData serviceData);
}
