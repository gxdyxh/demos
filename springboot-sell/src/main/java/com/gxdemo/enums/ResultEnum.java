package com.gxdemo.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(0,"成功"),
    PARAM_ERROR(1,"参数不正确"),
    PRODUCT_NOT_EXIST(10,"商品不存在"),
    PRODUCT_STOCK_ERROR(11,"商品库存不足"),
    PRODUCT_STATUS_ERROR(12,"商品状态错误"),
    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),
    ORDER_NOT_EXIST(20,"订单不存在"),
    ORDERDETAIL_NOT_EXIST(21,"订单详情不存在"),
    ORDER_CANCEL_ERROR(22,"订单状态无法取消"),
    ORDER_CANCEL_SUCCESS(27,"订单取消成功"),
    ORDER_FINISH_SUCCESS(26,"订单完结成功"),
    ORDER_UPDATE_FAIL(23,"订单状态更新失败"),
    ORDER_PAY_STATUS_ERROR(24,"订单支付状态不正确"),
    CART_EMPTY(25,"购物车不能为空"),
    WX_MP_ERROR(31,"微信公众账号方面错误"),
    WX_NOTIFY_MONEY_ERROR(41,"微信支付异步通知金额校验失败"),
    LOGIN_FILED_EMPTY(51,"登录信息不完整"),
    LOGIN_USER_NOT_FIND(52,"登录用户不存在"),
    LOGIN_PASSWORD_ERROR(53,"登录用户密码错误"),
    LOGOUT_SUCCESS(52,"登出成功"),
    ;

    private Integer code;
    private  String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
