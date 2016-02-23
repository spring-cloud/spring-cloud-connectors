package org.springframework.cloud.localconfig;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.S3ServiceInfo;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p/>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p/>
 * Author: Arthur Halet
 * Date: 23/02/2016
 */
public class LocalConfigConnectorS3ServiceTest extends AbstractLocalConfigConnectorWithUrisTest {

    @Test
    public void serviceCreation() {
        List<ServiceInfo> services = connector.getServiceInfos();
        ServiceInfo service = getServiceInfo(services, "riak");
        assertNotNull(service);
        assertTrue(service instanceof S3ServiceInfo);
        assertUriParameters((S3ServiceInfo) service);
    }

}