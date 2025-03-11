package com.hcskia.Eordermanager.controller;

import com.auth0.jwt.interfaces.Claim;
import com.hcskia.Eordermanager.pojo.User;
import com.hcskia.Eordermanager.repository.UserRepository;
import com.hcskia.Eordermanager.service.TokenUtli;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/test",consumes = "application/json")
    public User postTest(@RequestBody User user){
        return user;
    }
    //User user = userRepository.findByUserId(loginUser.getUserId());
    @PostMapping(value = "/login")//,consumes = "application/json"
    public List<Map> UserLogin(@RequestBody User loginUser) throws JoseException {
        List<Map> finStructure = new ArrayList<>();
        if ((Objects.equals(loginUser.getUserId(), ""))||(Objects.equals(loginUser.getPw(), ""))){
            return null;
        }
        User user = loginUser;
        if (!userRepository.existsByUserId(user.getUserId())){
            userRepository.save(user);
        } else {
            userRepository.deleteByUserId(user.getUserId());
            userRepository.save(user);
        }
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("userid", user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("message","登录成功！");
        map.put("token",TokenUtli.GenerateToken(claimMap));
        finStructure.add(initQueryMap("0",map));
        return finStructure;
    }

    @PostMapping(value = "/register")//,consumes = "application/json"
    public List<Map> UserRegister(@RequestBody User registerUser) throws JoseException {
        List<Map> finStructure = new ArrayList<>();
        System.out.println(registerUser.getUserId());
        System.out.println(registerUser.getPw());
        if ((Objects.equals(registerUser.getUserId(), ""))||(Objects.equals(registerUser.getPw(), ""))){
            return null;
        }
        User user = registerUser;
        if (userRepository.existsByUserId(user.getUserId())){
            Map<String, Object> map = new HashMap<>();
            map.put("message","错误！存在相同用户！");
            finStructure.add(initQueryMap("-1",map));
            return finStructure;
        } else {
            userRepository.save(user);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message","注册成功！");
        finStructure.add(initQueryMap("0",map));
        return finStructure;
    }

//    @GetMapping(value = "/profile")//,consumes = "application/json"
//    public User UserProfile(@RequestHeader(value = "Authorization") String token) throws Exception {
//        Map<String, Claim> claimMap = TokenUtli.VerifyJWTToken(token);
//        String userID = null;
//        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
//            if (Objects.equals(entry.getKey(), "userid")){
//                userID = String.valueOf(entry.getValue());
//                break;
//            }
//        }
//        assert userID != null;//因为token生成后用户名不可能为null
//        User user = userRepository.findByUserId(userID.replace("\"",""));//去除引号
//        return user;
//    }

    public static Map initQueryMap(String code,Map data){
        Map<String, Object> map = new HashMap<>();
        map.put("CODE",code);
        map.put("DATA",data);
        return map;
    }
}
