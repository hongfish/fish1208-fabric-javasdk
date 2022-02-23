package com.fish1208.config;

import com.alibaba.fastjson.JSON;
import com.fish1208.sdk.FabricConfig;
import com.fish1208.sdk.entity.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "fabric-config")
public class FabricManagerConfig {
    private League league;
    private Channel channel;
    private List<Orderer> orderers = new ArrayList<>();
    private Chaincode chaincode;
    private Org org1;
    private List<Peer> peers1 = new ArrayList<>();
    private CA ca1;
    private Org org2;
    private List<Peer> peers2 = new ArrayList<>();
    private CA ca2;

    @Bean
    public FabricConfig config() {
        
        log.debug("league = {}", JSON.toJSONString(league));
        log.debug("channel = {}", JSON.toJSONString(channel));
        log.debug("orderers = {}", JSON.toJSONString(orderers));
        log.debug("chaincode = {}", JSON.toJSONString(chaincode));
        log.debug("peers1 = {}", JSON.toJSONString(peers1));
        log.debug("ca1 = {}", JSON.toJSONString(ca1));
        log.debug("org2 = {}", JSON.toJSONString(org2));
        log.debug("peers2 = {}", JSON.toJSONString(peers2));
        log.debug("ca2 = {}", JSON.toJSONString(ca2));
        
        FabricConfig config = new FabricConfig();
        config.setLeague(league);
        config.setChannel(channel);
        config.setOrderers(orderers);
        config.setChaincode(chaincode);
        config.setOrg1(org1);
        config.setPeers1(peers1);
        config.setCa1(ca1);
        config.setOrg2(org2);
        config.setPeers2(peers2);
        config.setCa2(ca2);
        return config;
    }

}
