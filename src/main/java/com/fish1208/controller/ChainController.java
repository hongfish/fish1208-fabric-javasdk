package com.fish1208.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish1208.common.response.Result;
import com.fish1208.sdk.FabricConfig;
import com.fish1208.sdk.FabricManager;
import com.fish1208.sdk.util.FabricHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/mycc")
public class ChainController {

    private static Map<Integer, FabricManager> fabricManagerMap = new HashMap<>();

    @Autowired
    private FabricConfig config;

    private FabricManager getFabricManager() throws Exception {

        FabricHelper fabricHelper = FabricHelper.obtain();
        FabricManager fabricManager;
        //产生随机数1 - 2
        Random random = new Random();
        int number = random.nextInt(2) + 1;
        fabricManager = fabricManagerMap.get(number);
        if(null == fabricManager){
            synchronized (fabricManagerMap) {
                if (number % 2 != 0){
                    fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg1(),config.getChannel(),config.getChaincodes().get(0),config.getOrderers(),config.getPeers1(),config.getCa1(),config.getLeague().getName());
                } else {
                    fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg2(),config.getChannel(),config.getChaincodes().get(0),config.getOrderers(),config.getPeers2(),config.getCa2(),config.getLeague().getName());
                }
                fabricManagerMap.put(number, fabricManager);
            }
        }
        return fabricManager;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result<?> query(@RequestParam String from) throws Exception {
		String fcn = "query";
		String[] args = new String[]{from};
		JSONObject jsonObject = getFabricManager().query(fcn, args);
        return Result.data(jsonObject);
    }

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    public Result<?> invoke(@RequestBody Map<String,Object> param) throws Exception {

        String fcn = "invoke";
        String from = (String)param.get("from");
        String to = (String) param.get("to");
        String amount = (String) param.get("amount");
        String[] args = new String[]{from, to, amount};
		JSONObject jsonObject = getFabricManager().invoke(fcn, args);
        return Result.data(jsonObject);
    }
}
