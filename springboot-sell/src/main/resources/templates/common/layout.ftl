<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>首页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link th:href="@{/lib/bootstrap-3.3.7/css/bootstrap.min.css}" href="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <#assign path="${springMacroRequestContext.getContextPath()}">
    <link href="${path}/css/style.css" rel="stylesheet">
</head>
<body>

<div id="wrapper" class="toggled">

    <#include "/common/nav.ftl" />

    <div id="page-connect-wrapper">
        <@block name="content">
            base_body_content
        </@block>
    </div>

</div>

<#--弹窗-->
<div class="modal fade" id="orderModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">
                    订单提醒
                </h4>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button onclick="javascript:document.getElementById('notice').pause();" type="button" class="btn btn-default" data-dismiss="modal">关闭</button> <button type="button" class="btn btn-primary">查看</button>
            </div>
        </div>

    </div>

</div>
<#--弹窗-->

<#--音乐-->
<audio id="notice" loop="loop">
    <source src="${path}/mp3/song.mp3" type="audio/mpeg">
</audio>
<#--音乐-->

<script th:src="@{/lib/jquery-3.3.0/js/jquery.min.js}" src="https://cdn.bootcss.com/jquery/3.3.0/jquery.min.js"></script>
<script type="text/javascript" th:src="@{/lib/bootstrap-3.3.7/js/bootstrap.min.js}" src="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- 存放具体业务页面需要引入的javascript文件 -->
<@block name="bodyAssets">

</@block>
<script>
var websocket = null;
if('WebSocket' in window){
    websocket = new WebSocket('ws://${webRootUrl}${path}/webSocket');
    websocket.onopen = function (ev) {
        console.log("建立链接")
    }
    websocket.onclose=function (ev) {
        console.log("连接关闭")
    }
    websocket.onmessage=function (ev) {
        console.log("收到消息"+ev.data);

        $('#orderModal .modal-body').text(ev.data);
        $('#orderModal').modal("show");
        document.getElementById('notice').play();

    }
    websocket.onerror=function (ev) {
        console.log("连接发生错误");
    }
    window.onbeforeunload=function (ev) {
        websocket.close();
    }

}else{
    alert("该浏览器不支持websocket!");
}

</script>

</body>
</html>