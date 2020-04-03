<!DOCTYPE HTML>
<html>
<head>
    <title>成功提示</title>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-success">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4>
                    成功!
                </h4> <strong >${msg!""}</strong>  <a href="${url}" class="alert-link">3s后自动跳转</a>
            </div>
        </div>
    </div>
</div>
</body>
<script >
    setTimeout(function () {
        location.href="${url}";
    },3000)
</script>
</html>