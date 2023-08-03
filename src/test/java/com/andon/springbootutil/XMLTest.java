package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Andon
 * 2023/7/21
 */
@Slf4j
public class XMLTest {

    @Test
    public void test01() throws DocumentException {
        String response = getResponse();
//        log.info("response:{}", response);
        JSONObject responseJson = JSONObject.parseObject(response);
        String data = responseJson.getString("data");
        log.info("data:{}", data);

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        Element rootElement = document.getRootElement();
        log.info("rootElement.getName:{}", rootElement.getName());
    }

    @Test
    public void test02() {
        String response = getResponse();
        JSONObject responseJson = JSONObject.parseObject(response);
        String data = responseJson.getString("data");
//        log.info("data:{}", data);
        org.jsoup.nodes.Document document = Jsoup.parse(data);
        Elements unitDataElements = document.getElementsByTag("ns2:UnitData");
        for (int i = 0; i < unitDataElements.size(); i++) {
            org.jsoup.nodes.Element unitDataElement = unitDataElements.get(i);
            if (unitDataElement.getElementsByTag("ns2:UnitEnglishName").get(0).text().equals("UNIT_LIST")) {
                String unitValue = unitDataElement.getElementsByTag("ns2:unitValue").get(0).text();
                org.jsoup.nodes.Document unitValueDocument = Jsoup.parse(unitValue);
                Elements unitListElements = unitValueDocument.getElementsByTag("DataCol");
                StringBuilder unitListSB = new StringBuilder();
                for (org.jsoup.nodes.Element dataColElement : unitListElements) {
                    unitListSB.append("\n").append("\t").append(dataColElement.getElementsByTag("ResIndex").get(0).text()).append(":").append(dataColElement.getElementsByTag("Value").get(0).text());
                }
                log.info("num:{} {}:{}:{}", i + 1, unitDataElement.getElementsByTag("ns2:UnitEnglishName").get(0).text(), unitDataElement.getElementsByTag("ns2:UnitDisplayName").get(0).text(), unitListSB);
            } else if (unitDataElement.getElementsByTag("ns2:UnitEnglishName").get(0).text().equals("RESULT")) {
                String unitValue = unitDataElement.getElementsByTag("ns2:unitValue").get(0).text();
                org.jsoup.nodes.Document unitValueDocument = Jsoup.parse(unitValue);
                Elements dataRowElements = unitValueDocument.getElementsByTag("DataRow");
                StringBuilder dataRowSB = new StringBuilder();
                for (int i1 = 0; i1 < dataRowElements.size(); i1++) {
                    org.jsoup.nodes.Element dataRowElement = dataRowElements.get(i1);
                    Elements dataColElements = dataRowElement.getElementsByTag("DataCol");
                    StringBuilder dataColSB = new StringBuilder();
                    dataRowSB.append("\n");
                    for (org.jsoup.nodes.Element dataColElement : dataColElements) {
                        dataColSB.append("\n").append("\t").append(dataColElement.getElementsByTag("ResIndex").get(0).text()).append(":").append(dataColElement.getElementsByTag("Value").get(0).text());
                    }
                    dataRowSB.append(i1 + 1).append("-").append(dataColSB);
                }
                log.info("num:{} {}:{}:{}", i + 1, unitDataElement.getElementsByTag("ns2:UnitEnglishName").get(0).text(), unitDataElement.getElementsByTag("ns2:UnitDisplayName").get(0).text(), dataRowSB);
            } else {
                log.info("num:{} {}:{}:{}", i + 1, unitDataElement.getElementsByTag("ns2:UnitEnglishName").get(0).text(), unitDataElement.getElementsByTag("ns2:UnitDisplayName").get(0).text(), unitDataElement.getElementsByTag("ns2:UnitValue").get(0).text());
            }
        }
        log.info("{}", ".");
    }

    private String getResponse() {
        return "{\"msg\":\"调用成功！\",\"data\":\"\n" +
                "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\n" +
                "<Envelope xmlns=\\\"http://www.powerrich.com.cn/Envelope\\\"> <Header>\n" +
                "\t<EnvelpeId>EnvelpeId</EnvelpeId>\n" +
                "\t<Router>\n" +
                "\t\t<ExchangeSponsor>\n" +
                "\t\t\t<Node>\n" +
                "\t\t\t\t<NodeName>NodeName</NodeName>\n" +
                "\t\t\t\t<NodeCode>NodeCode</NodeCode>\n" +
                "\t\t\t\t<NodeType>FrontNodeType</NodeType>\n" +
                "\t\t\t</Node>\n" +
                "\t\t\t<RequestType>APPREQUEST_FETCHRESDATA</RequestType>\n" +
                "\t\t\t<OrgId>OrgId</OrgId>\n" +
                "\t\t\t<AppId>AppId</AppId>\n" +
                "\t\t\t<ServiceEndpoint>ServiceEndpoint_IP</ServiceEndpoint>\n" +
                "\t\t\t<Port>8080</Port>\n" +
                "\t\t\t<MqHost>MqHost</MqHost>\n" +
                "\t\t\t<MqPort>MqPort</MqPort>\n" +
                "\t\t</ExchangeSponsor>\n" +
                "\t\t<Partners>\n" +
                "\t\t\t<Target>\n" +
                "\t\t\t\t<Node>\n" +
                "\t\t\t\t\t<NodeName>NodeName</NodeName>\n" +
                "\t\t\t\t\t<NodeCode>NodeCode</NodeCode>\n" +
                "\t\t\t\t\t<NodeType>NodeType</NodeType>\n" +
                "\t\t\t\t</Node>\n" +
                "\t\t\t\t<ServiceEndpoint>ServiceEndpoint_IP</ServiceEndpoint>\n" +
                "\t\t\t\t<Port>Port</Port>\n" +
                "\t\t\t\t<MqHost>MqHost</MqHost>\n" +
                "\t\t\t\t<MqPort>MqPort</MqPort>\n" +
                "\t\t\t\t<NextPartner>\n" +
                "\t\t\t\t\t<Node>\n" +
                "\t\t\t\t\t\t<NodeName>nextPartnerNodeName</NodeName>\n" +
                "\t\t\t\t\t\t<NodeCode>nextPartnerNodeCode</NodeCode>\n" +
                "\t\t\t\t\t\t<NodeType>nextPartnerNodeType</NodeType>\n" +
                "\t\t\t\t\t</Node>\n" +
                "\t\t\t\t\t<ServiceEndpoint>203.91.47.170</ServiceEndpoint>\n" +
                "\t\t\t\t\t<Port>8080</Port>\n" +
                "\t\t\t\t\t<MqHost>203.91.47.170</MqHost>\n" +
                "\t\t\t\t\t<MqPort>7676</MqPort>\n" +
                "\t\t\t\t\t<IsPassed>false</IsPassed>\n" +
                "\t\t\t\t</NextPartner>\n" +
                "\t\t\t\t<IsPassed>true</IsPassed>\n" +
                "\t\t\t\t<IsSupportFrontPToP>true</IsSupportFrontPToP>\n" +
                "\t\t\t\t<ReponseType>FORWARDING_MESSAGE</ReponseType>\n" +
                "\t\t\t</Target>\n" +
                "\t\t</Partners>\n" +
                "\t</Router>\n" +
                "\t</Header>\n" +
                "\n" +
                "\t<Body xmlns:ns1=\\\"http://www.powerrich.com.cn/Envelope\\\" ns1:BodyType=\\\"ResResult\\\">\n" +
                "\t\t<ResResult xmlns=\\\"\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:type=\\\"ResResult\\\">\n" +
                "\t\t\t<ResultInfo>\n" +
                "\t\t\t\t<BizDataInfo>\n" +
                "\t\t\t\t\t<IsExist>true</IsExist>\n" +
                "\t\t\t\t</BizDataInfo>\n" +
                "\t\t\t\t<Attachment>\n" +
                "\t\t\t\t\t<IsExist>false</IsExist>\n" +
                "\t\t\t\t</Attachment>\n" +
                "\t\t\t</ResultInfo>\n" +
                "\t\t\t<BizBody>\n" +
                "\t\t\t\t<ns2:BusinessData xmlns:ns2=\\\"http://www.egs.org.cn/businessdata\\\" xsi:type=\\\"ns2:BusinessData\\\">\n" +
                "\t\t\t\t\t<ns2:DataStructure>\n" +
                "\t\t\t\t\t\t<ns2:Identifier>3ebbfa49afa04e6fbd0faa7b199732f0</ns2:Identifier>\n" +
                "\t\t\t\t\t\t<ns2:DisplayName>个人缴费记录明细表接口</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t<ns2:ExplanatoryComment>法人社保缴费信息_信用示范区项目</ns2:ExplanatoryComment>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732646</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>参保人员身份证号</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>ID_NO</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732647</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询结束年</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>TO_YEAR</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732648</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>系统代码</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SYS_CODE</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732649</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询开始月</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>FROM_MONTH</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732650</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>交互码</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>PROCESS_NO</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732651</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>访问密码</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>PASSWORD</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732652</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询用户</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>QUERY_BY</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732653</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询开始年</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>FROM_YEAR</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732654</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询结束月</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>TO_MONTH</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732655</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>参保人员姓名</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>NAME</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732656</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>养老保险连续缴费不中断月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>YANGL_CTMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732657</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>医疗保险累计缴费月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>YUL_TMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732658</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>医疗保险连续缴费不中断月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>YUL_CTMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732659</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>工伤保险连续缴费不中断月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>GONGS_CTMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732660</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>失业保险累计缴费月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SHIY_TMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732661</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>失业保险连续缴费不中断月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SHIY_CTMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732662</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>生育保险累计缴费月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SHENGY_TMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732663</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>反馈结果集</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>RESULT</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>大文本类型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732664</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>错误代码</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>ERROR_CODE</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732665</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>生育保险连续缴费不中断月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SHENGY_CTMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732666</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>明细中单位列表</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>UNIT_LIST</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>大文本类型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732667</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>参保人员社保电脑号</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>S_NO</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732668</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>性别</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SEX</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732669</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>在深首次缴费日期</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>FIRST_DATE</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732670</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>养老保险累计缴费月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>YANGL_TMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732671</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>工伤保险累计缴费月数</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>GONGS_TMS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732672</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>错误信息</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>ERROR_MESSAGE</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732673</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>查询险种</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>CXXZ</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732674</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>展示方式</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>ZSFS</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t\t<ns2:DataUnit>\n" +
                "\t\t\t\t\t\t\t<ns2:IDName>1732675</ns2:IDName>\n" +
                "\t\t\t\t\t\t\t<ns2:DisplayName>授权记录</ns2:DisplayName>\n" +
                "\t\t\t\t\t\t\t<ns2:EnglishName>SQJL</ns2:EnglishName>\n" +
                "\t\t\t\t\t\t\t<ns2:Datatype>字符型</ns2:Datatype>\n" +
                "\t\t\t\t\t\t\t<ns2:isPK>false</ns2:isPK>\n" +
                "\t\t\t\t\t\t</ns2:DataUnit>\n" +
                "\t\t\t\t\t</ns2:DataStructure>\n" +
                "\t\t\t\t\t<ns2:DataSet>\n" +
                "\t\t\t\t\t\t<ns2:RecordData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732646</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>参保人员身份证号</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>ID_NO</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\t430723199704011810</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732647</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询结束年</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>TO_YEAR</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732648</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>系统代码</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SYS_CODE</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732649</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询开始月</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>FROM_MONTH</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732650</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>交互码</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>PROCESS_NO</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732651</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>访问密码</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>PASSWORD</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732652</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询用户</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>QUERY_BY</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732653</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询开始年</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>FROM_YEAR</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732654</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询结束月</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>TO_MONTH</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732655</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>参保人员姓名</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>NAME</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">彭敏\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732656</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>养老保险连续缴费不中断月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>YANGL_CTMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732657</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>医疗保险累计缴费月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>YUL_TMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732658</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>医疗保险连续缴费不中断月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>YUL_CTMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732659</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>工伤保险连续缴费不中断月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>GONGS_CTMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732660</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>失业保险累计缴费月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SHIY_TMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732661</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>失业保险连续缴费不中断月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SHIY_CTMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732662</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>生育保险累计缴费月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SHENGY_TMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732663</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>反馈结果集</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>RESULT</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\t&lt;?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?&gt;\n" +
                "\t\t\t\t\t\t\t\t\t&lt;DataSet&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;*13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;06&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;*13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;*58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;*260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;*14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;*1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;*7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;*1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;*16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;*13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;*780&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;07&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;780&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;780&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;09&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;780&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;10&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;11&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;12&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;58.5&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2022&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;01&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;02&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;03&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;04&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;14.56&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;05&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;18.2&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YJBZ&lt;/ResIndex&gt;&lt;Value&gt;&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;MONTH&lt;/ResIndex&gt;&lt;Value&gt;06&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;65&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_OWN&lt;/ResIndex&gt;&lt;Value&gt;260&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;GONGS_UNIT&lt;/ResIndex&gt;&lt;Value&gt;18.2&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;1820.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_OWN&lt;/ResIndex&gt;&lt;Value&gt;7.08&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YANGL_OWN&lt;/ResIndex&gt;&lt;Value&gt;1040.0&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHIY_UNIT&lt;/ResIndex&gt;&lt;Value&gt;16.52&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YEAR&lt;/ResIndex&gt;&lt;Value&gt;2023&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;SHENGY_BASE_WAGE&lt;/ResIndex&gt;&lt;Value&gt;13000&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;QZFS_FLAG&lt;/ResIndex&gt;&lt;Value&gt;1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;YUL_UNIT&lt;/ResIndex&gt;&lt;Value&gt;806&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;/DataSet&gt;\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732664</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>错误代码</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>ERROR_CODE</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">000\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732665</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>生育保险连续缴费不中断月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SHENGY_CTMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732666</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>明细中单位列表</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>UNIT_LIST</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\t&lt;?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?&gt;\n" +
                "\t\t\t\t\t\t\t\t\t&lt;DataSet&gt;&lt;DataRow&gt;&lt;DataCol&gt;&lt;ResIndex&gt;UNIT_ORGNO&lt;/ResIndex&gt;&lt;Value&gt;MA5GUYNN1&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;UNIT_NAME&lt;/ResIndex&gt;&lt;Value&gt;深圳安恒信息安全技术有限公司&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;UNIT_TYSHXYDM&lt;/ResIndex&gt;&lt;Value&gt;91440300MA5GUYNN1A&lt;/Value&gt;&lt;/DataCol&gt;&lt;DataCol&gt;&lt;ResIndex&gt;UNIT_NO&lt;/ResIndex&gt;&lt;Value&gt;30558556&lt;/Value&gt;&lt;/DataCol&gt;&lt;/DataRow&gt;&lt;/DataSet&gt;\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732667</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>参保人员社保电脑号</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>S_NO</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\t810771600</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732668</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>性别</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SEX</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">男\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732669</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>在深首次缴费日期</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>FIRST_DATE</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\t202206</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732670</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>养老保险累计缴费月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>YANGL_TMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732671</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>工伤保险累计缴费月数</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>GONGS_TMS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">13\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732672</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>错误信息</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>ERROR_MESSAGE</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732673</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>查询险种</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>CXXZ</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732674</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>展示方式</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>ZSFS</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t<ns2:UnitData>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitIDName>1732675</ns2:UnitIDName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitDisplayName>授权记录</ns2:UnitDisplayName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitEnglishName>SQJL</ns2:UnitEnglishName>\n" +
                "\t\t\t\t\t\t\t\t<ns2:UnitValue xmlns:java=\\\"http://java.sun.com\\\" xsi:type=\\\"java:java.lang.String\\\">\n" +
                "\t\t\t\t\t\t\t\t\tnull</ns2:UnitValue>\n" +
                "\t\t\t\t\t\t\t</ns2:UnitData>\n" +
                "\t\t\t\t\t\t</ns2:RecordData>\n" +
                "\t\t\t\t\t</ns2:DataSet>\n" +
                "\t\t\t\t</ns2:BusinessData>\n" +
                "\t\t\t</BizBody>\n" +
                "\t\t</ResResult>\n" +
                "\t</Body>\n" +
                "</Envelope>\",\"success\":true,\"totalTime\":\"745ms\"}";
    }
}
