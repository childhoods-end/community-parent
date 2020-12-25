$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	// 获取标题和内容
	// 通过id选择器获取
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求(POST)
	$.post(
	    // 访问路径
	    CONTEXT_PATH + "/discuss/add",
	    // 传入的参数
	    {"title":title,"content":content},
	    // 回调函数，处理返回结果
	    function(data) {
	        // 将服务器返回的json转换成js对象
	        data = $.parseJSON(data);
	        // 在提示框中显示返回消息
	        // 用id获取提示框
	        $("#hintBody").text(data.msg);
	        // 显示提示框
            $("#hintModal").modal("show");
            // 2秒后,自动隐藏提示框
            setTimeout(function(){
                $("#hintModal").modal("hide");
                // 判断成功后，刷新页面
                if(data.code == 0) {
                    window.location.reload();
                }
            }, 2000);
	    }
	);
}