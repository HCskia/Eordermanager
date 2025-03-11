package com.hcskia.Eordermanager.controller;

import com.auth0.jwt.interfaces.Claim;
import com.hcskia.Eordermanager.pojo.OrderList;
import com.hcskia.Eordermanager.pojo.User;
import com.hcskia.Eordermanager.repository.OrderRepository;
import com.hcskia.Eordermanager.repository.UserRepository;
import com.hcskia.Eordermanager.service.TokenUtli;
import com.hcskia.Eordermanager.service.XlsxReduce;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/ordermanager")
public class OrderManagerControler {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/deleteorder")//,consumes = "application/json"
    public List<Map> DeleteOrder(@RequestHeader(value = "Authorization") String token,@RequestParam(value = "orderids") List<String> orderIds) throws Exception {
        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
        String userID = "";
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "userid")){
                userID = String.valueOf(entry.getValue());
                break;
            }
        }
        userID = userID.replace("\"","").replace("\\","");


        for(String orderid:orderIds){
            if (Objects.equals(orderRepository.findByOrderId(orderid).getuserId(), userID)){orderRepository.deleteByOrderId(orderid);}
        }

        List<Map> finStructure = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("message","操作成功");
        finStructure.add(initQueryMap("0",map));
        return finStructure;
    }

    @PostMapping(value = "/getorderdata")//,consumes = "application/json"
    public List<Map> GetOrderData(@RequestHeader(value = "Authorization") String token,@RequestParam(value = "columns") Integer columns) throws Exception {
        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
        String userID = "";
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "userid")){
                userID = String.valueOf(entry.getValue());
                break;
            }
        }
        userID = userID.replace("\"","").replace("\\","");

        List<Integer> resultCount = orderRepository.getOrderListCountByOrderId(userID);
        List<OrderList> resultList;

        if(columns == 0) {resultList = orderRepository.findOrderListByOrderId(userID,resultCount.get(0));}
        else {resultList = orderRepository.findOrderListByOrderId(userID,columns);}

        List<Map> finStructure = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("length",resultCount.get(0));
        map.put("data",resultList);
        finStructure.add(initQueryMap("0",map));
        return finStructure;
    }

    @PostMapping(value = "/refreshorderdata")//,consumes = "application/json"
    public List<Map> RefreshOrderData(@RequestHeader(value = "Authorization") String token) throws Exception {
        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
        String userID = "";
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "userid")){
                userID = String.valueOf(entry.getValue());
                break;
            }
        }
        userID = userID.replace("\"","").replace("\\","");

        String[] pathFileNameLists = getFiles("G:/uploads/EorderManagement/source/Eordermanager/src/main/upload/");
        ArrayList<String> userFileLists = new ArrayList<>();
        for(int i=0;i<pathFileNameLists.length;i++){
            if (pathFileNameLists[i].contains(userID)){
                userFileLists.add(pathFileNameLists[i]);
            }
        }

        List<Map<String,List<List<String>>>> XlsxJson = XlsxReduce.XlsxToJson(userFileLists,"G:/uploads/EorderManagement/source/Eordermanager/src/main/upload/");

        List<List<String>> pddJson = new ArrayList<>();
        List<List<String>> qnJson = new ArrayList<>();
        if (XlsxJson != null){
            for(Map<String,List<List<String>>> item:XlsxJson){
                pddJson = item.get("pdd");
                qnJson = item.get("qn");
            }
        }else{
            return null;
        }

        int loopCount = 0;
        int updateCount = 0;
        for(List<String> item:pddJson){
            if (loopCount == 0) {
                System.out.println(item.toString());
                loopCount++;
                continue;
            }

            OrderList tOrderList = new OrderList();

            if (orderRepository.existsByOrderId(item.get(0))){
                loopCount++;
                continue;
            }

            tOrderList.setOrderId(item.get(0));
            tOrderList.setPlatform("pdd");
            tOrderList.setBuyer(item.get(1));
            tOrderList.setOrderDate((new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss")).parse(item.get(25)));
            tOrderList.setOrderInfo(item.get(13));
            tOrderList.setOrderPrice(Double.parseDouble(item.get(22).replace("￥","")));
            tOrderList.setOrderMount(Integer.parseInt(item.get(20)));
            tOrderList.setProductID(item.get(15));
            tOrderList.setBuyerAddress(item.get(3) + "/" + item.get(4));
            tOrderList.setuserId(userID);

            orderRepository.save(tOrderList);

            updateCount++;
            loopCount++;
        }
        loopCount = 0;
        for(List<String> item:qnJson){
            if (loopCount == 0) {
                System.out.println(item.toString());
                loopCount++;
                continue;
            }

            OrderList tOrderList = new OrderList();

            if (orderRepository.existsByOrderId(item.get(1))){
                loopCount++;
                continue;
            }

            tOrderList.setOrderId(item.get(1));
            tOrderList.setPlatform("qn");
            tOrderList.setBuyer(item.get(2));
            tOrderList.setOrderDate((new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss")).parse(item.get(9)));
            tOrderList.setOrderInfo(item.get(14));
            tOrderList.setOrderPrice(Double.parseDouble(item.get(11)));
            tOrderList.setOrderMount(Integer.parseInt(item.get(21)));
            tOrderList.setProductID(item.get(15));
            tOrderList.setBuyerAddress(item.get(5) + "/" + item.get(6)+ "/" + item.get(7)+ "/" + item.get(8));
            tOrderList.setuserId(userID);

            orderRepository.save(tOrderList);

            updateCount++;
            loopCount++;
        }

        List<Map> finStructure = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("updateCount",updateCount);
        map.put("message","更新成功！成功更新"+updateCount+"条数据！");
        finStructure.add(initQueryMap("0",map));
        return finStructure;
    }


    @PostMapping(value = "/checkfiles")//,consumes = "application/json"
    public List<Map> OrderImport(@RequestHeader(value = "Authorization") String token) throws Exception {
        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
        String userID = "";
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "userid")){
                userID = String.valueOf(entry.getValue());
                break;
            }
        }
        userID = userID.replace("\"","").replace("\\","");

        String[] pathFileNameLists = getFiles("G:/uploads/EorderManagement/source/Eordermanager/src/main/upload/");
        ArrayList<String> userFileLists = new ArrayList<>();
        for(int i=0;i<pathFileNameLists.length;i++){
            if (pathFileNameLists[i].contains(userID)){
                userFileLists.add(pathFileNameLists[i]);
            }
        }

        List<Map> finStructure = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("data",userFileLists);
        finStructure.add(initQueryMap("0",map));

        return finStructure;
    }

    @PostMapping(value = "/uploadxls")//,consumes = "application/json"
    public List<Map> uploadXlsFIle(@RequestParam(value = "file") MultipartFile uploadfile,@RequestHeader(value = "Authorization") String token) throws Exception {
        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
        String userID = "";
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "userid")){
                userID = String.valueOf(entry.getValue());
                break;
            }
        }
        userID = userID.replace("\"","").replace("\\","");

        List<Map> finStructure = new ArrayList<>();
        String filePath = "G:/uploads/EorderManagement/source/Eordermanager/src/main/upload/"
                + userID +"-"+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +".xlsx";
        File dest = new File(filePath);
        Map<String, Object> map = new HashMap<>();
        try{
            uploadfile.transferTo(dest);
            map.put("message","上传成功！");
            finStructure.add(initQueryMap("0",map));
            return finStructure;
        }catch (IOException e){
            map.put("message","上传失败！\n原因："+e.getMessage());
            finStructure.add(initQueryMap("-1",map));
            return finStructure;
        }
    }

    public static Map initQueryMap(String code,Map data){
        Map<String, Object> map = new HashMap<>();
        map.put("CODE",code);
        map.put("DATA",data);
        return map;
    }

    public static String[] getFiles(String filePath){
        File file = new File(filePath);
        return file.list();
    }
}
