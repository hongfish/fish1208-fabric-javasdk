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
@RequestMapping("/person")
public class PersonController {

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
                    fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg1(),config.getChannel(),config.getChaincodes().get(1),config.getOrderers(),config.getPeers1(),config.getCa1(),config.getLeague().getName());
                } else {
                    fabricManager = fabricHelper.createFabricManager(config.getLeague(),config.getOrg2(),config.getChannel(),config.getChaincodes().get(1),config.getOrderers(),config.getPeers2(),config.getCa2(),config.getLeague().getName());
                }
                fabricManagerMap.put(number, fabricManager);
            }
        }
        return fabricManager;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result<?> query(@RequestParam String symbol) throws Exception {
		String fcn = "queryPerson";
		String[] args = new String[]{symbol};
		JSONObject jsonObject = getFabricManager().query(fcn, args);
        return Result.data(jsonObject);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<?> save(@RequestBody Map<String,Object> param) throws Exception {
        String fcn = "savePerson";
        String symbol = (String)param.get("symbol");
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String age = (String) param.get("age");
        String[] args = new String[]{symbol, name, sex, age};
		JSONObject jsonObject = getFabricManager().invoke(fcn, args);
        return Result.data(jsonObject);
    }
}
