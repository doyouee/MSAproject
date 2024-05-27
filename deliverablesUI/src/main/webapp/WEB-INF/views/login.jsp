<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="UTF-8">
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width,initial-scale=1.0,shrink-to-fit=no">
<title>INZENT</title>
<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.min.css">
<link rel="shortcut icon" href="img/inzent_logo.png">
<link rel="stylesheet" href="css/common.css">
<script src="vendor/jquery/jquery-3.3.1.min.js"></script>
<script src="vendor/jquery/jquery.form.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="js/common.js"></script>
</head>
<style>
.layerPopup {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.8);
    opacity : 0.5;
    z-index: 1000;
    justify-content: center;
    align-items: center;
}
.spinner-border {
    position: absolute;
    top: 50%;
    left: 50%;
}
div {
	position: relative;
}
</style>

<body>
<div id="wrap" class="login">
	<header id="hd">
		<h1 class="logo text-hide"><a href=""><img src="img/logo_wh.svg" alt="INZENT">INZENT</a></h1>
	</header>
	<div id="ct">
		<form class="form-login" id="form_login" method="post">
			<div class="login-title">산출물 관리 시스템</div><br>
			<div class="form-group">
				<i class="icon-user"></i>
				<input type="text" class="form-control form-control-lg" placeholder="이메일" id="userEmail" name="userEmail">
			</div>
			<div class="form-group">
				<i class="icon-lock"></i>
				<input type="password" class="form-control form-control-lg" placeholder="비밀번호" id="userPassword" name="userPassword" autocomplete="on">
			</div>
			<div id="loginValidation"></div>
			<button type="submit" class="btn btn-block btn-danger btn-lg" id="btn_login">로그인</button>
			<div class="login-menu">
			    <a href="#modal_passwordReset" class="btn-login" data-toggle="modal" id="passwordResetBtn">비밀번호 초기화</a>
			</div>
		</form>
		<p class="copy">&copy; 2024 IZENT. All rights reserved</p>
	</div>
</div>

<div id="modal_passwordReset" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h2 class="modal-title">비밀번호 초기화</h2>
			</div>
			<div class="modal-body">
			    <div class="form-group">
					<label class="control-label">이메일</label>
					<div style="position: relative">
                        <input type="text" class="form-control form-input" id="passwordResetEmail" placeholder="이메일을 입력해 주세요.">
                    </div>
				</div>
				<span style="color:red; font-size:12px" id="emailValidation"></span>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn" data-dismiss="modal">취소</button>
				<button type="button" class="btn btn-primary" onclick="passwordReset()">확인</button>
			</div>
		</div>
	</div>
</div>

<!-- Complete Modal -->
<div id="completeModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-alert">
		<div class="modal-content">
			<div class="modal-body">
				<i class="iconb-compt"></i>
				<p class="alert-text" id="completeModalContent"></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>

<!-- Fail Modal -->
<div id="failModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-alert">
		<div class="modal-content">
			<div class="modal-body">
				<i class="iconb-danger"></i>
				<p class="alert-text" id="failModalContent"></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>

<div class="layerPopup" style="z-index:2000;">
    <div class="spinner-border text-light" role="status"></div>
</div>

<form id="projectForm" action="project">
</form>

<script>

$(document).ajaxError(function() {
    $('.layerPopup')[0].style.display='none';
    location = "/error";
 });
 
$(document).ajaxSuccess(function(event, xhr, options, data) {
    if(xhr.getResponseHeader('content-length') == '7366'){ // login 페이지로 redirect할 경우
        $('.layerPopup')[0].style.display='none';
		location = "/login";
   	} else if(data.responseCode == "HTTP_ERROR"){
   	    $('.layerPopup')[0].style.display='none';
        $(".modal").hide();
        $('#warnModalContent').text("ERROR MESSAGE : " + data.responseMessage);
        $('#warnModal').modal('show');
        $('#warnModalButton').attr("onclick","location.href='/login'");
    }else if(data.responseCode == "EXCEPTION_ERROR"){
        $('.layerPopup')[0].style.display='none';
    	location = "/error";
    }
});

$(document).ready(function () {
	$("#btn_login").click(function (e) {
		if($("#userEmail").val() == ''){
			$("#loginValidation").addClass('alert alert-danger');
			$("#loginValidation").html('이메일을 입력하세요');
			return false;
		}else if($("#userPassword").val() == ''){
			$("#loginValidation").addClass('alert alert-danger');
			$("#loginValidation").html('비밀번호를 입력하세요');
			return false;
		}else{
			$('.layerPopup')[0].style.display='block';
			$.ajax({
				url: 'loginProcess',
				type: 'post',
				data: $('#form_login').serialize(),
				success: function (data) {
					if (data.responseCode == 'SUCCESS') {
						$('#projectForm').submit();
					}else if(data.responseCode == "ERROR" ){
						$("#loginValidation").addClass('alert alert-danger');
						$("#loginValidation").html(data.responseMessage);
						$('.layerPopup')[0].style.display='none';
					}
				}
			});
			return false;
		}
	});
	
	$("#passwordResetBtn").click(function (e) {
		$('#passwordResetEmail').val('');
	});
	
	$('#projectForm').submit(function() {
	    $(this).ajaxSubmit({
			beforeSubmit: function () {
				$('.layerPopup')[0].style.display='block';
			},
		    error: function(res){
		    	$('.layerPopup')[0].style.display='none';
		    	location = "/error";
		    }
	    });
	});
});

function passwordReset(){
	const passwordResetEmail = $('#passwordResetEmail').val();
	if(passwordResetEmail == ''){
		$("#emailValidation").addClass('icon-x');
		$("#emailValidation").html('&nbsp;이메일을 입력하세요');
	}else{
		$('.layerPopup')[0].style.display='block';
		$.ajax({
			url: '/userPasswordReset',
			type: 'post',
			data: {'userEmail': passwordResetEmail},
			success: function (data) {
			    $('.layerPopup')[0].style.display='none';
				if (data.responseCode == 'SUCCESS') {
					$("#completeModalContent").html('메일이 발송되었습니다.');
					$('#modal_passwordReset').modal('hide');
					$('#completeModal').modal('show');
				}else if(data.responseCode == "ERROR" ){
					$("#emailValidation").addClass('icon-x');
					$("#emailValidation").html('&nbsp;'+data.responseMessage);
				}
			}
		});
	}
}
</script>
</body>
</html>