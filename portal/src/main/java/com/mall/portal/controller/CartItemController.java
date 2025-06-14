package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.OmsCartItem;
import com.mall.portal.domain.model.CartProduct;
import com.mall.portal.domain.model.PromotionCartItem;
import com.mall.portal.service.CartItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/api/cart")
public class CartItemController {
    @Autowired private CartItemService cartItemService;


    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ResponseResult<String> add(@RequestBody OmsCartItem cartItem){
        cartItemService.add(cartItem);
        return ResponseResult.success("添加成功");
    }


    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("分页获取当前用户购物车商品列表")
    public ResponseResult<List<OmsCartItem>> list(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0){
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize =10;
        }
        return ResponseResult.success(cartItemService.list(pageNum,pageSize));
    }


    @PostMapping("/promotionList")
    @ApiOperation("获取当前会员的购物车列表,包括促销信息")
    public ResponseResult<List<PromotionCartItem>> promotionList(@RequestBody List<Long> cartIds){
        if (cartIds==null || cartIds.isEmpty()){
            return null;
        }
        return ResponseResult.success(cartItemService.promotionList(cartIds));
    }


    @PutMapping("/updateCartQuantity/{cartId}/{quantity}")
    @ApiOperation("修改指定购物车的商品数量")
    public ResponseResult<String> updateCartQuantity(@PathVariable("cartId") long cartId,@PathVariable("quantity") int quantity){
        if (quantity <0 || cartId <0){
            return null;
        }
        cartItemService.updateQuantity(cartId,quantity);
        return ResponseResult.success("修改成功");
    }

    @PostMapping("/delete")
    @ApiOperation("批量删除购物车商品")
    public ResponseResult<String> delete(@RequestBody List<Long> cartIds){
        if (cartIds==null || cartIds.isEmpty()){
            return null;
        }
        cartItemService.delete(cartIds);
        return ResponseResult.success("删除成功");
    }


    @GetMapping("/cartProduct/{productId}")
    @ApiOperation("获取购物车中用于选择商品规格的商品信息")
    public ResponseResult<CartProduct> getCartProduct(@PathVariable("productId") long productId){
        return ResponseResult.success(cartItemService.getCartProduct(productId));
    }


    @PutMapping("/updateAttribute")
    @ApiOperation("修改购物车商品规格")
    public ResponseResult<String> updateAttribute(@RequestBody OmsCartItem cartItem){
        cartItemService.updateAttribute(cartItem);
        return ResponseResult.success("修改成功");
    }


    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public ResponseResult<String> clean(){
        cartItemService.clear();
        return ResponseResult.success("清空完毕");
    }
}
