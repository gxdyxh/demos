<@override name="content">
<div  class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <form role="form" action="${path}/seller/product/save" method="post">
                <input type="hidden" value="${(product.getProductId())!""}" name="productId">
                <div class="form-group">
                    <label >名称</label><input type="text" class="form-control" name="productName" value="${(product.getProductName())!""}" />
                </div>
                <div class="form-group">
                    <label >价格</label><input type="text" class="form-control" name="productPrice" value="${(product.getProductPrice())!""}" />
                </div>
                <div class="form-group">
                    <label >库存</label><input type="number" class="form-control" name="productStock" value="${(product.getProductStock())!""}" />
                </div>
                <div class="form-group">
                    <label >描述</label><input type="text" class="form-control" name="productDesc" value="${(product.getProductDesc())!""}" />
                </div>


                <div class="form-group">
                    <label >图片</label><input type="text" class="form-control" name="productIcon" value="${(product.getProductIcon())!""}" />
                    <img src="${(product.getProductIcon())!""}">
                </div>

                <div class="form-group">
                    <label >类目</label>
                    <select  class="form-control" name="categoryType">

                        <#list  categoryList as cate>
                            <option value="${cate.getCategoryId()}" <#if (product.getCategoryType())?? && cate.getCategoryId()==(product.getCategoryType())>selected</#if> > ${cate.getCategoryName()}</option>
                        </#list>

                    </select>
                </div>


                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </div>
    </div>

</div>

</@override>
<@extends name="/common/layout.ftl"/>