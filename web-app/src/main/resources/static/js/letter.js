$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
	    CONTEXT_PATH + "/letter/send",
	    {"toName":toName,"content":content},
	    function(data) {
	        data = $.parseJSON(data);
	        if(data.code == 0) {
	            // 提示框输出
	            $("#hintBody").text("发送成功!");
	        } else {
	            $("#hintBody").text(data.msg);
	        }

            // 显示提示框，两秒后隐藏
	        $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
	    }
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}