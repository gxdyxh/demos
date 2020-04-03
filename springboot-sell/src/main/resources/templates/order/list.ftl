<@override name="content">
<div class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>地址</th>
                    <th>金额</th>
                    <th>状态</th>
                    <th>支付</th>
                    <th>时间</th>
                    <th colspan="2">操作</th>
                </tr>
                </thead>
                <tbody>

                    <#list list as order>
                    <tr>
                        <td >${order.getOrderId()} </td>
                        <td >${order.getBuyerName()} </td>
                        <td >${order.getBuyerPhone()} </td>
                        <td >${order.getBuyerAddress()} </td>
                        <td >${order.getOrderAmount()} </td>
                        <td >${order.getOrderStatusEnum().message} </td>
                        <td >${order.getPayStatusEnum().message} </td>
                        <td >${order.getCreateTime()} </td>
                        <td ><a href="${path}/seller/order/detail?orderId=${order.getOrderId()}">详情</a></td>
                        <td ><#if order.getOrderStatus() == 0><a href="${path}/seller/order/cancel?orderId=${order.getOrderId()}">取消</a></#if> </td>
                    </tr>
                    </#list>

                </tbody>
            </table>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-md-12 column">
            ${page.show(0,1,2,3,4,5,6,7)}
        </div>
    </div>

</div>
</@override>
<@extends name="/common/layout.ftl"/>