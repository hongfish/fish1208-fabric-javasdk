package com.fish1208.test;

import com.alibaba.fastjson.JSONObject;
import com.fish1208.sdk.FabricManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class PersonccTest extends BaseTest{

	private FabricManager fabricManager;

	@Before
	public void manage() throws Exception{
        fabricManager = getFabricManager();
	}

	//安装链码
	@Test
	public void install() throws Exception{
		JSONObject jsonObject = fabricManager.install();
		log.info("install: " + jsonObject);
	}

	//实例化链码
	@Test
	public void instantiate() throws Exception{
		String[] args = new String[]{""};
		JSONObject jsonObject = fabricManager.instantiate(args);
		log.info("instantiate: " + jsonObject);
	}

	//升级链码
	@Test
	public void upgrade() throws Exception{
		String[] args = new String[]{""};
		JSONObject jsonObject = fabricManager.upgrade(args);
		log.info("upgrade: " + jsonObject);
	}

	@Test
	public void query() throws Exception{
		//peer chaincode query -C fish1208channel -n personcc -c '{"Args":["queryPerson","1111"]}'
		String fcn = "queryPerson";
		String[] args = new String[]{"009"};
		JSONObject jsonObject = fabricManager.query(fcn, args);
		System.out.println("queryPerson: " + jsonObject);
	}

	@Test
	public void save() throws Exception{
//		peer chaincode invoke -o orderer.fish1208.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/fish1208.com/orderers/orderer.fish1208.com/msp/tlscacerts/tlsca.fish1208.com-cert.pem -C fish1208channel -n personcc -c '{"Args":["savePerson","1111", "xiaoxiao", "girl", "24"]}'
		String fcn = "savePerson";
		String[] args = new String[]{"009", "小小", "女", "24"};
		JSONObject jsonObject = fabricManager.invoke(fcn, args);
		System.out.println("savePerson: " + jsonObject);
	}

	@Test
	public void invokeOtherChaincode() throws Exception{
		//跨链码调用
//		peer chaincode invoke -C zmeduchannel -n personcc -c '{"Args":["invokeOtherChaincode","accountcc", "zmeduchannel", "queryBalance", "001"]}' -o orderer.example.com:7050
		String fcn = "invokeOtherChaincode";
		//链码名、通道名、方法名、参数
		String[] args = new String[]{"accountcc", "zmeduchannel", "queryBalance", "001"};
		JSONObject jsonObject = fabricManager.invoke(fcn, args);
		System.out.println("invokeOtherChaincode: " + jsonObject);
	}

	@Test
	public void getBlockchainInfo() throws Exception{
		JSONObject jsonObject = fabricManager.getBlockchainInfo();
		log.info("getBlockchainInfo: " + jsonObject);
	}

	@Test
	public void queryBlockByNumber() throws Exception{
		int blockNumber = 0;
		JSONObject jsonObject = fabricManager.queryBlockByNumber(blockNumber);
		log.info("queryBlockByNumber: " + jsonObject);
	}

	@Test
	public void queryBlockByTransactionID() throws Exception{
		String txID = "1c609b83da44f865fd2d65b60ced17847e772cc144ecdd1a4a9756cd236dcfc4";
		JSONObject jsonObject = fabricManager.queryBlockByTransactionID(txID);
		log.info("queryBlockByTransactionID: " + jsonObject);
		if(200 == jsonObject.getInteger("code")){
			JSONObject dataJson = jsonObject.getJSONObject("data");
			String dataHash = dataJson.getString("dataHash");
			String currentBlockHash = dataJson.getString("calculatedBlockHash");
			String previousBlockHash = dataJson.getString("previousHashID");
			Integer blockNumber = dataJson.getInteger("blockNumber");
			log.info("数据Hash:{},上个区块Hash:{},当前区块Hash:{},当前区块高度:{}",dataHash,previousBlockHash,currentBlockHash,blockNumber);
		}
	}

}
