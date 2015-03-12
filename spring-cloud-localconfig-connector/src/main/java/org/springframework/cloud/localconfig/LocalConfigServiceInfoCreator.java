package org.springframework.cloud.localconfig;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfoCreator;

public abstract class LocalConfigServiceInfoCreator<SI extends ServiceInfo> extends UriBasedServiceInfoCreator<SI> {

    protected LocalConfigServiceInfoCreator(String... uriSchemes) {
        super(uriSchemes);
    }
}
