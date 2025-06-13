package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.service.ConsumerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/portal/api/oss")
@Tag(name = "用户管理",description = "用户登录 注册 验证码 更改密码 用户信息")
public class ConsumerController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired private ConsumerService consumerService;

    @PostMapping("/register")
    public ResponseResult<Map<String,String>> register(@RequestParam String username,
                                                       @RequestParam String password,
                                                       @RequestParam String telephone,
                                                       @RequestParam String authCode){
        consumerService.register(username,password,telephone,authCode);
        String token = consumerService.login(telephone,password);
        Map<String,String> result = new HashMap<>();
        result.put("token",token);
        result.put("tokenHead",tokenHead);
        return ResponseResult.success(result);
    }

    @PostMapping("/login")
    public ResponseResult<Map<String,String>> login(
            @RequestParam String telephone,
            @RequestParam String password
    ){
        String token = consumerService.login(telephone,password);
        Map<String,String> result = new HashMap<>();
        result.put("token",token);
        result.put("tokenHead",tokenHead);
        return ResponseResult.success(result);
    }

    @GetMapping("/getAuthCode")
    @ApiOperation("获取验证码 ")
    public ResponseResult<String> getAuthCode(@RequestParam String telephone){
        consumerService.sendAuthCode(telephone);
        return ResponseResult.success("验证码发送成功");
    }


    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public ResponseResult<UmsMember> info(){
        UmsMember member = consumerService.getCurrentMember();
        if (member==null) {
            return ResponseResult.unauthorized("未登录");
        }
        member.setSalt(null);
        member.setPassword(null);
        return ResponseResult.success(member);
    }


    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public ResponseResult<String> updatePassword(@RequestParam String telephone,
                                       @RequestParam String password,
                                       @RequestParam String authCode) {
        consumerService.updatePassword(telephone,password,authCode);
        return ResponseResult.success("修改成功");
    }

    @GetMapping("/refreshToken")
    public ResponseResult<Map<String,String>> refreshToken(@RequestParam String token){
        String newToken = consumerService.refreshToken(token);
        if (newToken==null){
            return ResponseResult.unauthorized("未登录");
        }
        Map<String,String> result = new HashMap<>();
        result.put("token",newToken);
        result.put("tokenHead",tokenHead);
        return ResponseResult.success(result);
    }
}
