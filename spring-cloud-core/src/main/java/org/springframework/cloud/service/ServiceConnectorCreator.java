package org.springframework.cloud.service;

/**
 *
 * @author Ramnivas Laddad
 *
 * @param <SC>
 *            service connector type
 * @param <SI>
 *            service info type
 */
public interface ServiceConnectorCreator<SC, SI extends ServiceInfo> {
    /**
     * Create service for the given service info and configured with the given
     * configuration
     *
     * @param serviceInfo
     *            the {@link ServiceInfo} object containing the information necessary to connect to the service
     * @param serviceConnectorConfig
     *            configuration information to be applied to the connection
     * @return service connector
     */
    SC create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig);

    Class<SC> getServiceConnectorType();

    Class<?> getServiceInfoType();
}
