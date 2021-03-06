/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fish1208.test;

import com.fish1208.FabricServiceApplication;
import com.fish1208.sdk.FabricConfig;
import com.fish1208.sdk.FabricManager;
import com.fish1208.sdk.util.FabricHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FabricServiceApplication.class)
public abstract class BaseTest {

    @Autowired
    private FabricConfig config;

    public FabricManager getFabricManager() throws Exception {

        FabricHelper fabricHelper = FabricHelper.obtain();
        FabricManager fabricManager;
//        fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg1(),config.getChannel(),config.getChaincode(),config.getOrderers(),config.getPeers1(),config.getCa1(),config.getLeague().getName());
        fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg2(),config.getChannel(),config.getChaincodes().get(0),config.getOrderers(),config.getPeers2(),config.getCa2(),config.getLeague().getName());

        return fabricManager;
    }
}
