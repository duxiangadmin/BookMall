//声明变量
var path="/goods";
$(function(){
	$(".errorclass").each(function(){//变量所有元素
			showError($(this));//将当前遍历的元素传入
		}			
	);
	
//	鼠标悬浮事件
	$("#submitBtn").hover(
		function(){//修改图片路径
			$("#submitBtn").attr("src","/goods/images/regist2.jpg");
		},function(){
			$("#submitBtn").attr("src","/goods/images/regist1.jpg");
		}
	);
	
//	输入框得到焦点隐藏对应的lable元素
	$(".inputclass").focus(
		function(){
			var lableId = $(this).attr("id")+"Error";
			$("#"+lableId).text("");//将当前lable的内容清空
			showError($("#"+lableId));//将lable元素传过去
		}
	);	
	
//	输入框失去焦点校验
	$(".inputClass").blur(function() {
		var id = $(this).attr("id");//获取当前输入框的id
		var funName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1) + "()";//得到对应的校验函数名
		eval(funName);//执行函数调用		
	});
	
//	表单提交验证
	$("#registForm").submit(function(){
		var bool = true;
//		if(!validateLoginname()){bool=false;}		
//		if(!validateLoginpass()){bool=false;}
//		if(!validateReloginpass()){bool=false;}
//		if(!validateEmail()){bool=false;}
//		if(!validateVerifyCode()){bool=false;}
//		
//		循环遍历所有方法获取返回结果
		$(".inputclass").each(function (){
			var id = $(this).attr("id");
			var funName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1) + "()";//得到对应的校验函数名
			if(!eval(funName)){
				bool=false;
			}
		});
		return bool;
	});
});

//账号校验
function validateLoginname() {
	var id = "loginname";
	var value = $("#" + id).val();//获取输入框内容
	/*
	 * 1. 非空校验
	 */
	if(!value) {
		/*
		 * 获取对应的label
		 * 添加错误信息
		 * 显示label
		 */
		$("#" + id + "Error").text("用户名不能为空！");
		showError($("#" + id + "Error"));
		return false;
	}
	/*
	 * 2. 长度校验
	 */
	if(value.length < 3 || value.length > 20) {
		/*
		 * 获取对应的label
		 * 添加错误信息
		 * 显示label
		 */
		$("#" + id + "Error").text("用户名长度必须在3 ~ 20之间！");
		showError($("#" + id + "Error"));
		return false;
	}
//	账号是否注册验证
	$.ajax({
		url:path+"/UserServlet",
		data:{method:"ajaxValiDateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#" + id + "Error").text("账户已存在！");
				showError($("#" + id + "Error"));
				return false
			}
		}
	});
	return true;
	
}
//密码校验
function validateLoginpass(){
//	非空验证
	var id ="loginpass";
	var value = $("#"+id).val();
	if(!value){
		$("#"+id+"Error").text("密码不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
//	长度验证
	if(value.length<6 || value.length >20){
		$("#"+id+"Error").text("密码长度必须在6~20位之间");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
	
}
//确认密码校验
function validateReloginpass(){
	//	非空验证
	var id ="reloginpass";
	var value = $("#"+id).val();
	if(!value){
		$("#"+id+"Error").text("确认密码不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
//	与密码是否一致
	var loginpass = $("#loginpass").val();
//	获取两个密码框中的值
	if(loginpass != value){
		$("#"+id+"Error").text("密码不一致");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
}
//邮箱校验
function validateEmail(){
//	非空校验
	var id ="email";
	var value=$("#"+id).val();
	if(!value){
		$("#"+id+"Error").text("邮箱不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
//	邮箱格式验证
	if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)){
		$("#" + id + "Error").text("错误的Email格式！");
		showError($("#" + id + "Error"));
		false;
	}
//	邮箱是否注册校验
	$.ajax({
		url:path+"/UserServlet",
		data:{method:"ajaxValiDateEmail",email:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#" + id + "Error").text("邮箱已存在！");
				showError($("#" + id + "Error"));
				return false
			}
		}
	});
	return true;
	
}
//验证吗校验
function validateVerifyCode(){
	var id="verifyCode";
	var value = $("#"+id).val();
	//	非空校验
	if(!value) {
		/*
		 * 获取对应的label
		 * 添加错误信息
		 * 显示label
		 */
		$("#" + id + "Error").text("验证码不能为空！");
		showError($("#" + id + "Error"));
		return false;
	}
//	长度校验
	if(value.length!=4){
		$("#" + id + "Error").text("错误的验证码！！");
		showError($("#" + id + "Error"));
		return false;
	}
//	内容校验
	$.ajax({
		url:path+"/UserServlet",
		data:{method:"ajaxValiDateVerifyCode",verifyCode:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#" + id + "Error").text("验证码错误！");
				showError($("#" + id + "Error"));
				return false
			}
		}
	});
	
	return true;
	
}

//显示错误信息
function showError(e){
	var text = e.text();//获取内容
	if(!text){//没有内容
		e.css("display","none");//隐藏
	}else{
		e.css("display","");//显示
	}
}
//换一张验证码
function _hyz(){
	$("#imgVerifyCode").attr("src", "/goods/VerifyCodeServlet?a=" + new Date().getTime());
}