<@override name="content">
<div  class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>名称</th>
                    <th>图片</th>
                    <th>单价</th>
                    <th>库存</th>
                    <th>描述</th>
                    <th>类目</th>
                    <th>创建时间</th>
                    <th colspan="2">操作</th>
                </tr>
                </thead>
                <tbody>
                <#list list as product>
                <tr>
                    <td >${product.getProductId()} </td>
                    <td >${product.getProductName()} </td>
                    <td >
                        <img src="${product.getProductIcon()}" style="width: 120px;height: 120px;">
                    </td>
                    <td >${product.getProductPrice()} </td>
                    <td >${product.getProductStock()} </td>
                    <td >${product.getProductDesc()!""}</td>
                    <td >${product.getCategoryType()} </td>
                    <td >${product.getCreateTime()} </td>
                    <td ><a href="${path}/seller/product/index?productId=${product.getProductId()}">修改</a></td>
                    <td >
                        <#if product.getProductStatus()== 0>
                        <a  href="${path}/seller/product/off_sale?productId=${product.getProductId()}">下架</a>
                        </#if>
                        <#if product.getProductStatus()== 1>
                        <a href="${path}/seller/product/on_sale?productId=${product.getProductId()}">上架</a>
                        </#if>
                    </td>
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