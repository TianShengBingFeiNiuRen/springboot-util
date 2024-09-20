package com.andon.springbootutil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andon
 * 2021/12/9
 */
@Slf4j
@Api(tags = "temp")
@RestController
@RequestMapping(value = "/temp")
public class TempTestController {

    @ApiOperation("在线任务")
    @GetMapping(value = "/onlineTask")
    public void test() {
        // apiId：968dab09b25f48c3a6b02cc8e2a4876d
        // contractId：kjzqauedclcbxjtjaagliwkdqfzfnfjx
        // datasetName：data_loan_jh_t
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {

                }
            }.start();
        }
    }

    private static void call(String apiId, String contractId, String datasetName) {
        String startQueryUrl = String.format("%s%s", "https://10.190.160.232/_ailand/openapi/flow/startQuery/", apiId);
        String startQueryParams = "{\"contractId\":\"" + contractId + "\",\"datasetParam\":[{\"datasetName\":\"" + datasetName + "\",\"columns\":[{\"name\":\"data\",\"value\":\"Ogz3ZnSlakj2hZCH92yZwm8/nbUYQY7Dk1fTV57rg1TNS5zsiNo4ALLZvyOEjvpa7nJbe5tjMi+sk/UHPMXbPcp72ccGTkc8yA3h6vWZ9Q93SgCISO2qry008Z+9mjeWFBABIh/JBy/JfXIGSlMFEXXEI6Pqlufi1ElcUh0Gy3qSHS3LMTE2pgUTovT7B9y4mmGF6eDXuoFvk9jinPXpYLpDCqLJzg4om8Aoz/cbYI8SzioZvGSeDXJYtKLr+/iP2au2fQBnw1o4uFdB4SPZ3RmezaXqx3bWWi65kwYKn++codYKr9HQSAiQbUUWl42f605WWhhgu3XVSy5hURl+mxoBeUSwh4tCb6ojUukOoKXTGE/+Ntle9ucUwcY5dResP1KBK0x+PVxi5SPQaUfK9L3eTmcXTxsf+yJF1pn//zQUyhN6iNDhIWTp3l7YoeFcF8VhpmYyb/xdXZQTWmScxOkG2H/eRUBIx1Xb6JISztOEOisCXQFz5IhNBmPPSEnn7l0cngNT5FilEEpYrcSpSTfzZ890K+s0YqwSKYHy7Qdxd/5aEDYe01IZvdfkOZjZ4wmdy5gc+5GNYLmvDZaHRLXI33QVYo2K85sy+XLLBZpbfcksswMhu7ZZaDmrxahGHqGQMQlThE/93u0ZcU/dajqFDpQdsN58MYNNHVTSPRM=\",\"type\":\"STRING\"}]}]}";
    }
}
