<@override name="content">
<div class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>名字</th>
                    <th>类别</th>
                    <th>创建时间</th>
                    <th>修改时间</th>
                    <th >操作</th>
                </tr>
                </thead>
                <tbody>

                    <#list list as cate>
                    <tr>
                        <td >${cate.getCategoryId()} </td>
                        <td >${cate.getCategoryName()} </td>
                        <td >${cate.getCategoryType()} </td>
                        <td >${cate.getCreateTime()} </td>
                        <td >${cate.getUpdateTime()} </td>
                        <td ><a href="${path}/seller/category/index?categoryId=${cate.getCategoryId()}">修改</a></td>

                    </tr>
                    </#list>

                </tbody>
            </table>
        </div>
    </div>

</div>
</@override>
<@extends name="/common/layout.ftl"/>