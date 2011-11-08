/*
 * Copyright 2011 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.splunk.sdk.tests.com.splunk;

import com.splunk.*;
import com.splunk.sdk.Program;
import com.splunk.Service;

import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.Assert;

import org.junit.*;

public class DistributedPeerTest extends TestCase {
    Program program = new Program();

    public DistributedPeerTest() {}

    Service connect() throws IOException {
        return new Service(
            program.host, program.port, program.scheme)
                .login(program.username, program.password);
    }

    @Before public void setUp() {
        this.program.init(); // Pick up .splunkrc settings
    }

    @Test public void testDistributedPeer() throws Exception {
        Service service = connect();

        EntityCollection<DistributedPeer> dps = service.getDistributedPeers();

        if (dps.values().size() == 0) {
            System.out.println("WARNING: distributedPeer not configured");
            Assert.assertFalse(true);
        }

        for (DistributedPeer dp: dps.values()) {
            System.out.println(dp.getTitle());
            System.out.println(dp.getBuild());
            System.out.println(dp.getBundleVersions());
            System.out.println(dp.getGuid());
            System.out.println(dp.getLicenseSignature());
            System.out.println(dp.getPassword());
            System.out.println(dp.getPeerName());
            System.out.println(dp.getPeerType());
            System.out.println(dp.getReplicationStatus());
            System.out.println(dp.getStatus());
            System.out.println(dp.getUsername());
            System.out.println(dp.getVersion());
            System.out.println(dp.isDisabled());
            System.out.println(dp.isHttps());


        }
    }
}