package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * Web3Util
 */
@Slf4j
public class Web3Util {

    public static String httpService = "http://localhost:8545";
    public static Web3j web3;

    private static synchronized void init() {
        if (web3 == null) {
            web3 = Web3j.build(new HttpService(httpService));
        }
    }

    /**
     * 获取web3j连接
     */
    public static Web3j getWeb3Client() {
        if (web3 == null) {
            init();
        }
        return web3;
    }

    public static String getWeb3ClientVersion() {
        String web3ClientVersion = null;
        try {
            web3 = getWeb3Client();
            web3ClientVersion = web3.web3ClientVersion().send().getWeb3ClientVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return web3ClientVersion;
    }

    /**
     * 查询账户余额
     */
    public static BigInteger getBalance(String address) {
        BigInteger balance = BigInteger.valueOf(0);
        try {
            web3 = getWeb3Client();
            balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (Exception e) {
            log.error("getBalance failure!! error={}", e.getMessage());
        }
        return balance;
    }

    /**
     * 查询交易笔数
     */
    public static BigInteger getTransactionCount(String address) {
        BigInteger transactionCount = BigInteger.valueOf(0);
        try {
            web3 = getWeb3Client();
            transactionCount = web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        } catch (Exception e) {
            log.error("getTransactionCount failure!! error={}", e.getMessage());
        }
        return transactionCount;
    }

    /**
     * 查询交易详情(输入数据)
     */
    public static String getInput(String address) {
        String input = null;
        try {
            web3 = getWeb3Client();
            Transaction result = web3.ethGetTransactionByHash(address).send().getResult();
            input = result.getInput();
        } catch (Exception e) {
            log.error("getInput failure!! error={}", e.getMessage());
        }
        return input;
    }

    /**
     * 转换成符合 decimal 的数值
     */
    public static String toDecimal(int decimal, BigInteger integer) {
        if (integer.equals(BigInteger.valueOf(0))) {
            return String.valueOf(integer);
        }
        StringBuilder sbf = new StringBuilder("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        return new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
    }
}
