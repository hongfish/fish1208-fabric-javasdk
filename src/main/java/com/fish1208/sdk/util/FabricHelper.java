package com.fish1208.sdk.util;

import cn.hutool.core.util.StrUtil;
import com.fish1208.sdk.FabricManager;
import com.fish1208.sdk.OrgManager;
import com.fish1208.sdk.entity.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.core.io.Resource;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class FabricHelper {

    private static Logger log = LogManager.getLogger(FabricHelper.class);

    private static FabricHelper instance;

    private static Map<String, Object> caMap = new HashMap<>();

    public static FabricHelper obtain() {
        if (null == instance) {
            synchronized (FabricHelper.class) {
                if (null == instance) {
                    instance = new FabricHelper();
                }
            }
        }
        return instance;
    }

    private FabricHelper() {
    }

    public FabricManager createFabricManager(League league, Org org, Channel channel, Chaincode chaincode, List<Orderer> orderers, List<Peer> peers, CA ca, String cacheName) throws Exception {
        String sk = IOUtils.toString(ca.getSk().getInputStream(), "utf-8");
        String cert = IOUtils.toString(ca.getCertificate().getInputStream(), "utf-8");
        log.debug("sk = {}", sk);
        log.debug("cert = {}", cert);
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(cacheName, org.getMspId(), org.isTls())
                .setUser(league.getName(), org.getMspId(), peers.get(0).getName(), ca.getName(), sk, cert)
                .setChannel(channel.getName())
                .setChainCode(null == chaincode ? "" : chaincode.getName(),
                        null == chaincode ? "" : chaincode.getPath(),
                        null == chaincode ? "" : chaincode.getSource(),
                        null == chaincode ? "" : chaincode.getPolicy(),
                        null == chaincode ? "" : chaincode.getVersion(),
                        null == chaincode ? 0 : chaincode.getProposalWaitTime());

        for (Orderer orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation(), getFilePath(orderer.getName(), orderer.getServerCrtPath()), getFilePath(orderer.getName(), orderer.getClientCertPath()), getFilePath(orderer.getName(), orderer.getClientKeyPath()));
        }

        for (Peer peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getLocation(), peer.getEventHubLocation(), getFilePath(peer.getName(), peer.getServerCrtPath()), getFilePath(peer.getName(), peer.getClientCertPath()), getFilePath(peer.getName(), peer.getClientKeyPath()));
        }

        if (null != chaincode && chaincode.isChaincodeEventListener() && StrUtil.isNotEmpty(chaincode.getCallbackLocation())
                && StrUtil.isNotEmpty(chaincode.getEvents())) {
            orgManager.setChaincodeEventListener(chaincode.getEvents(), (handle, jsonObject, eventName, chaincodeId, txId) -> {
                log.debug(String.format("handle = %s", handle));
                log.debug(String.format("eventName = %s", eventName));
                log.debug(String.format("chaincodeId = %s", chaincodeId));
                log.debug(String.format("txId = %s", txId));
                log.debug(String.format("code = %s", String.valueOf(jsonObject.getInteger("code"))));
                log.debug(String.format("data = %s", jsonObject.getJSONObject("data").toJSONString()));
            });
        }

        orgManager.add();
        return orgManager.use(cacheName, ca.getName());
    }

    public CA createCA(String name, String location, boolean openCATLS, String username, String password) throws Exception{

        if(caMap.get(name) == null) {
            HFClient client = HFClient.createNewInstance();
            CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
            client.setCryptoSuite(cs);

            Properties prop = new Properties();
            prop.put("verify", false);

            location = location.trim();
            if(!"http".equals(location) && !"https".equals(location)) {
                location =  "http://" + location;
            }
            location = openCATLS ? location.replaceFirst("^http://", "https://") : location;

            HFCAClient caClient = HFCAClient.createNewInstance(location, prop);

            caClient.setCryptoSuite(cs);
            // enrollment保存了证书和私钥
            Enrollment enrollment = caClient.enroll(username, password);

            CA ca = new CA();
            ca.setName(name);
            //ca.setCertificate(enrollment.getCert());
            //获取私钥
            StringBuilder sb = new StringBuilder(300);
            sb.append("-----BEGIN PRIVATE KEY-----\n");
            String priKey = DatatypeConverter.printBase64Binary(enrollment.getKey().getEncoded());
            // 每64个字符输出一个换行
            int LEN = priKey.length();
            for (int ix = 0; ix < LEN; ++ix) {
                sb.append(priKey.charAt(ix));

                if ((ix + 1) % 64 == 0) {
                    sb.append('\n');
                }
            }
            sb.append("\n-----END PRIVATE KEY-----\n");
            //ca.setSk(sb.toString());

            caMap.put(name, ca);
        }

        return (CA)caMap.get(name);
    }

    /**
     * 将证书、私钥放到临时文件，返回文件路径
     * @param name
     * @param resource
     * @return
     * @throws Exception
     */
    private String getFilePath(String name, Resource resource) throws Exception{

        File tempFile = File.createTempFile(StrUtil.format("{}-{}", name, resource.getFilename().split("\\.")[0]), "." + resource.getFilename().split("\\.")[1]);
        InputStream in = resource.getInputStream();
        try{
            FileUtils.copyInputStreamToFile(in, tempFile);
        }finally {
            in.close();
        }
        return tempFile.getPath();
    }
}
