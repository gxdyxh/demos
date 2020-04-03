<@override name="content">
<div  class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <form role="form" action="${path}/seller/category/save" method="post">
                <input type="hidden" value="${(cate.getCategoryId())!""}" name="categoryId">
                <div class="form-group">
                    <label >名称</label><input type="text" class="form-control" name="categoryName" value="${(cate.getCategoryName())!""}" />
                </div>
                <div class="form-group">
                    <label >类别</label><input type="number" class="form-control" name="categoryType" value="${(cate.getCategoryType())!""}" />
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </div>
    </div>

</div>

</@override>
<@extends name="/common/layout.ftl"/>