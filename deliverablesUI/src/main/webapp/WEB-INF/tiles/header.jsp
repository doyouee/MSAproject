<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.hd-logout {
    height: 140px;
    padding: 10px 0px;
    border-radius: 0.4rem;
    border: 1px solid #F0F1F6;;
    box-shadow: 0 4px 12px 0 rgba(21,24,38,.16);
}

.hd-logout div {
    display: block;
    text-align: left;
    text-indent: 15px;
    font-family: Malgun Gothic;
}

.hd-logout .name {
    font-weight: 700;
    color: black;
}

.hd-logout .team {
    font-weight: 700;
    color:rgba(21, 24, 38, 0.5);
}

.hd-logout .link {
    line-height:35px;
    height:35px;
    margin:5px 0px;
}

.hd-logout .link:hover {
    background: #f0f1f6;
}

.hd-logout .btn {
    width: 180px;
}

</style>
<div class="hd-brand">
	<h1 class="logo text-hide">
		<a href="/project"><img src="img/logo.svg" alt="INZENT">INZENT</a>
	</h1>
	<button type="button" class="menu-toggler d-none d-lg-block">
		<i class="icon-hamburger-back"></i>
	</button>
	<button type="button" class="menu-toggler d-lg-none">
		<i class="icon-hamburger-back"></i>
	</button>
</div>
<ul class="breadcrumb">
</ul>
<div class="hd-side"  style="cursor: pointer"  onclick="getProfile()">
	<a class="icon-profile"></a>
	<span id="popup-logout" class="text-dark small">${userInfo.userName}<a class="icon-down"></a></span>
</div>
<div id="hd-logout" class="hd-logout" style="display:none">
	<div class="name">${userInfo.userName}</div>
	<div class="team">${userInfo.codeName}</div>
	<div class="link" onclick="detailModal('${userInfo.userName}','${userInfo.userEmail}','${userInfo.codeName}','${userInfo.authorityName}', 'myInfo')">내 정보</div>
	<a href="/logout" class="btn"><span class="hide">로그아웃</span></a>
</div>


<script>
	function getProfile() {
		if ($("#hd-logout").css("display") == "none") {
			$("#hd-logout").show();
		} else {
			$("#hd-logout").hide();
		}
	}

	$(document).ready(function () {
		$("#pwModalBtn").click(function (e) {
			$('#detailModal').modal('hide');
			$("#pwModalBeforePwValidation").css("display", "none");
			$("#pwModalNewPwValidation").css("display", "none");
			$("#pwModalNewPwConfirmValidation").css("display", "none");
			$("#pwModalForm")[0].reset();
			$('#pwModal').modal('show');
		})
		$("#pwChangeBtn").click(function (e) {
			$('.layerPopup')[0].style.display='block';
			updatePwFunction();
		})
	})

	//상세 조회
	function detailModal(userName, userEmail, codeName, authorityName, infoCheck) {
		$("#detailModalPwEdit").css("display", "none");
		$("#searchUserName").html(userName);
		$("#searchUserEmail").html(userEmail);
		$("#searchTeamName").html(codeName);
		$("#searchAuthorityId").html(authorityName);

		if (infoCheck) {
			$("#detailModalHeader").html("내 정보 상세보기");
			$("#detailModalPwEdit").css("display", "contents");
			$('#detailModal').modal('show');
		} else {
			$("#detailModalHeader").html("상세 조회");
			$('#detailModal').modal('show');
		}

	}

	function updatePwFunction() {
		$('.layerPopup')[0].style.display='none';
		if (pwCheck()) {
			$.ajax({
				type: "PUT",
				url: "/user",
				data: {
					"userEmail": $("#searchUserEmail").html(),
					"userPassword": $('input[name=pwModalBeforePw]').val(),
					"newPassword": $('input[name=pwModalNewPw]').val()
				},
				success: function (data) {
					$('.layerPopup')[0].style.display='none';
					if (data.responseCode == "SUCCESS") {
						$("#pwModalForm")[0].reset();
						$("#completeModalContent").html('비밀번호가 변경되었습니다.');
						$('#pwModal').modal('hide');
						$('#completeModal').modal('show');
						userList(1);
					} else if (data.responseCode == "ERROR"){
						$("#pwModalBeforePwValidation").html("&nbsp;" + data.responseMessage);
						$("#pwModalBeforePwValidation").css("display", "unset");
					}
				}
			})
		}

	}
	//pwCheck
	function pwCheck() {
		var reg = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$/;
		let col = ["pwModalBeforePw", "pwModalNewPw", "pwModalNewPwConfirm"]
		var state = true

		col.forEach(function (name) {
			if ($("#" + name).val() == '') {
				$("#" + name + "Validation").html("&nbsp;값이 존재하지 않습니다.")
				$("#" + name + "Validation").css("display", "unset")
				state = false
			} else {
				$("#" + name + "Validation").css("display", "none")
				//비밀번호 형식 검사 & 비밀번호 일치 검사
				if (name == "pwModalNewPw" || name == "pwModalNewPwConfirm") {
					//비밀번호 형식 검사
					if (!reg.test($("#" + name).val())) {
						state = false
						$("#" + name + "Validation").html("&nbsp;영문/숫자 포함 8자 이상으로 입력해 주세요.")
						$("#" + name + "Validation").css("display", "unset")
					} else {
						//pwModalNewPw, pwModalNewPwConfirm 입력된 값이 동일해야 한다.
						if ($('input[name=pwModalNewPw]').val() != $('input[name=pwModalNewPwConfirm]').val()) {
							state = false
							$("#pwModalNewPwValidation").html("&nbsp;비밀번호가 일치하지 않습니다.")
							$("#pwModalNewPwValidation").css("display", "unset")
							$("#pwModalNewPwConfirmValidation").html("&nbsp;비밀번호가 일치하지 않습니다.")
							$("#pwModalNewPwConfirmValidation").css("display", "unset")
						}
					}
				}
			}
		});
		$('.layerPopup')[0].style.display='none';
		return state
	}


</script>