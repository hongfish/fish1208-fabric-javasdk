package com.fish1208.sdk;

//import com.fish1208.sdk.util.FabricUtils;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import static java.lang.String.format;

import com.fish1208.sdk.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
@Data
public class FabricConfig {

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

/*
	//crypto-config/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/server.crt
	public static String getPeerServerCrtPath(String path, String orgName, String peerName){
		return path + format("/crypto-config/peerOrganizations/%s/peers/%s/tls/server.crt", orgName, peerName);
	}

	//crypto-config/peerOrganizations/org1.example.com/users/User1@org1.example.com/tls/client.crt
	public static String getPeerClientCertPath(String path, String orgName){
		return path + format("/crypto-config/peerOrganizations/%s/users/User1@%s/tls/client.crt", orgName, orgName);
	}

	//crypto-config/peerOrganizations/org1.example.com/users/User1@org1.example.com/tls/client.key
	public static String getPeerClientKeyPath(String path, String orgName){
		return path + format("/crypto-config/peerOrganizations/%s/users/User1@%s/tls/client.key", orgName, orgName);
	}

	//crypto-config/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.crt
	public static String getOrdererServerCrtPath(String path, String ordererDomainName, String ordererName){
		return path + format("/crypto-config/ordererOrganizations/%s/orderers/%s/tls/server.crt", ordererDomainName, ordererName);
	}

	//crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.crt
	public static String getOrdererClientCertPath(String path, String ordererDomainName){
		return path + format("/crypto-config/ordererOrganizations/%s/users/Admin@%s/tls/client.crt", ordererDomainName, ordererDomainName);
	}

	//crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.key
	public static String getOrdererClientKeyPath(String path, String ordererDomainName){
		return path + format("/crypto-config/ordererOrganizations/%s/users/Admin@%s/tls/client.key", ordererDomainName, ordererDomainName);
	}

	//crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/7037081bdda4284795f633b696992617442b3e99708c60e091022476b71104a9_sk
	public static String getCA_SK(String path, String orgName){
		try{
			return new String(Files.readAllBytes(FabricUtils.findFileSk(Paths.get(path, "/crypto-config/peerOrganizations/",
					orgName, format("/users/Admin@%s/msp/keystore", orgName)).toFile()).toPath()));
		} catch (Exception e){
			throw new RuntimeException(format("FabricConfig getCA_SK is Fail, path = %s, orgName = %s!", path, orgName));
		}
	}

	//crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem
	public static String getCA_CERT(String path, String orgName){
		try{
			return new String(Files.readAllBytes(Paths.get(path, "/crypto-config/peerOrganizations/", orgName,
					format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", orgName, orgName)).toFile().toPath()));
		} catch (Exception e){
			throw new RuntimeException(format("FabricConfig getCA_CERT is Fail, path = %s, orgName = %s!", path, orgName));
		}
	}

	public static String getPolicy(String path){
		try{
			return Paths.get(path,"/policy.yaml").toString();
		} catch (Exception e){
			throw new RuntimeException(format("FabricConfig getPolicy is Fail, path = %s!", path));
		}
	}
*/

}
