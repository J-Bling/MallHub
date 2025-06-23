package com.mall.admin.controller.orders;

import com.mall.admin.domain.orders.OssCallbackResult;
import com.mall.admin.domain.orders.OssPolicyResult;
import com.mall.admin.service.orders.OssService;
import com.mall.common.api.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Oss对象存储管理Controller
 */
@Controller
@Api(tags = "OssController")
@Tag(name = "OssController", description = "Oss对象存储管理")
@RequestMapping("/aliyun/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    @ApiOperation(value = "Oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "Oss上传成功回调")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return ResponseResult.success(ossCallbackResult);
    }

}
