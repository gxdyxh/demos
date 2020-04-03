<@override name="content">

<div class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-8 column">
            <table class="table table-condensed table-bordered">
                <thead>
                <tr>
                    <th>
                        编号
                    </th>
                    <th>
                        订单号
                    </th>
                    <th>
                        订单金额
                    </th>
                    <th>
                        订单时间
                    </th>
                    <th>
                        订单状态
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td >${orderDTO.getOrderId()}</td>
                    <td >${orderDTO.getOrderNo()}</td>
                    <td >${orderDTO.getOrderAmount()}</td>
                    <td >${orderDTO.getCreateTime()} </td>
                    <td >${orderDTO.getPayStatusEnum().message} </td>
                </tr>

                </tbody>
            </table>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>商品ID</th>
                    <th>商品名称</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>总额</th>

                </tr>
                </thead>
                <tbody>
                <#list orderDTO.getOrderDetailList() as order>
                <tr>
                    <td>${order.getProductId()} </td>
                    <td>${order.getProductName()} </td>
                    <td>${order.getProductPrice()} </td>
                    <td>${order.getProductQuantity()} </td>
                    <td>${order.getProductQuantity() * order.getProductPrice()} </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
    <#if orderDTO.getOrderStatus() == 0>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <a  href="${path}/seller/order/finish?orderId=${orderDTO.getOrderId()}" class="btn btn-default btn-primary">完结订单</a>
            <a  href="${path}/seller/order/cancel?orderId=${orderDTO.getOrderId()}" class="btn btn-default btn-danger">取消订单</a>
        </div>
    </div>
    </#if>

</div>

</@override>
<@extends name="/common/layout.ftl"/>