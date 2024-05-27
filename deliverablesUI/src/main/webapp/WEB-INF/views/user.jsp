<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<section class="card">
    <header class="card-header">
        <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-set"></i></span>사용자 관리</h2>
        <div class=" p-4 btn-container">
            <a href="#" id="insertButton" name="insertButton" data-toggle="modal" class="btn btn-primary"
               onclick="insertModal()"><span class="hide">등록</span></a>
        </div>
    </header>
    <div class="ct-header">
        <button type="button" class="btn-filter d-xl-none" data-toggle="collapse" data-target="#collapse-filter"
                aria-expanded="true">검색 필터<i class="icon-down"></i></button>
        <div id="collapse-filter" class="collapse-filter collapse show" style="">
            <div class="filter no-gutters">
                <div class="col" style="max-width: 30%;">
                    <label class="form-control-label">
                        <b class="control-label">사용자명</b>
                        <input id="userName" name="userName" type="text" class="form-control"
                               placeholder="사용자명을 입력해주세요.">
                    </label>
                </div>
                <div class="col" style="max-width: 30%;">
                    <label class="form-control-label">
                        <b class="control-label">사용자 이메일</b>
                        <input id="userEmail" name="userEmail" type="text" class="form-control"
                               placeholder="사용자 이메일을 입력해주세요.">
                    </label>
                </div>
                <div class="col" style="max-width: 30%;">
                    <label class="form-control-label">
                        <b class="control-label">부서명</b>
                        <input id="codeName" name="codeName" type="text" class="form-control"
                               placeholder="부서명을 입력해주세요.">
                    </label>
                </div>
                <div class="col-auto" style="min-width: 10%;">
                    <button id="keywordSearchbtn" name="keywordSearchbtn" onclick="search()" class="btn btn-m"><i
                            class="icon-srch"></i>조회
                    </button>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="deleteUserEmail">
    <input type="hidden" id="deleteAuthorityId">
    <input type="hidden" id="preAuthorityId">
    <div class="ct-content">
        <div class="table-responsive">
            <table class="table">
                <colgroup>
                    <col span="1" style="width: 3%">
                    <col span="1" style="width: 10%">
                    <col span="1" style="width: 20%">
                    <col span="3" style="width: 10%">
                </colgroup>
                <thead>
                <tr>
                    <th style="width: 3%">번호</th>
                    <th style="width: 10%">사용자명</th>
                    <th style="width: 20%" class="jusify-content-center">이메일</th>
                    <th style="width: 3%">부서명</th>
                    <th style="width: 3%">권한그룹</th>
                    <th style="width: 4%">관리</th>
                </tr>
                </thead>
                <tbody id="changeBody">
	                <c:forEach var="user" items="${userList}">
	                    <c:set var="i" value="${i+1}"/>
	                    <tr>
	                        <td>${i}</td>
	                        <td>${user.userName}</td>
	                        <td>${user.userEmail}</td>
	                        <td>${user.codeName}</td>
	                        <td>${user.authorityName}</td>
	                        <td style="display: flex; align-items: center; justify-content: center; border-left: none; border-top: none">
	                            <button data-toggle="modal" style="margin: 1px;" class="btn btn-icon" onclick="detailModal('${user.userName}','${user.userEmail}', '${user.codeName}','${user.authorityName}')">
	                                <i class="icon-srch text-dark"></i>
	                            </button>
	                            <c:choose>
		                            <c:when test ="${user.authorityId < loginAuthority }">
		                                <button data-toggle="modal" style="margin: 1px;" class="btn btn-icon" style='filter: brightness(93%);' disabled>
		                                    <i class="icon-edit text-dark"></i>
		                                </button>
		                                <button data-toggle="modal" style="margin: 1px;" class="btn btn-icon" id="delete" style='filter: brightness(93%);' disabled>
		                                    <i class="icon-delete"></i>
		                                </button>
		                            </c:when>
		                            <c:otherwise>
		                                <button data-toggle="modal" style="margin: 1px;" class="btn btn-icon" onclick="updateModal('${user.userEmail}')">
		                                    <i class="icon-edit text-dark"></i>
		                                </button>
		                                <button data-toggle="modal" style="margin: 1px;" class="btn btn-icon" id="delete" onclick="deleteModal('${user.userEmail}','${user.authorityId}')">
		                                    <i class="icon-delete"></i>
		                                </button>
		                            </c:otherwise>
	                            </c:choose>
	                        </td>
	                    </tr>
	                </c:forEach>
                </tbody>
            </table>
        </div>
        <div style="margin-top:5px">
            <a style="margin: 0.5%;font-weight: bold;" id="allSizePrint">총  ${userAllListSize} 건</a>
        </div>
        <ul class="pagination" id="pagination"></ul>
    </div>
</section>

<%--사용자 등록 & 수정 모달--%>
<form id="userModalForm">
    <div id="userModal" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-modal="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
            <div class="modal-content" style="width:50%;">
                <div class="modal-header">
                    <h2 class="modal-title" id="userModalHeader">사용자 등록</h2>
                    <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                            class="icon-close"></i></button>
                </div>
                <div class="table-responsive d-flex jusify-content-center"
                     style="padding-top: 2%; padding-bottom: 2%; padding-left:4%; padding-right:4%;">
                    <table style="text-align: center; padding: 2px;" class="table">
                        <colgroup class="jusify-content-center">
                            <col class="col" style="width: 30%">
                            <col class="col" style="width: 70%">
                        </colgroup>
                        <tbody>
                        <tr>
                            <td class="alert-text" style="text-align: center">이름</td>
                            <td style="text-align:left" id="userModalUserNameTd">
                                <p id="userModalEditUserName" style="display: none"></p>
                                <input id="userModalUserName" name="userModalUserName" type="text"  oninput="inputCheck(this)"  maxlength="16" class="form-control"
                                       style="display: none" placeholder="이름을 입력하세요.">
                                <span class="icon-x" style="color:red; font-size:12px; display:none"
                                      id="userModalUserNameValidation">&nbsp;값이 존재하지 않습니다.</span>
                                <span style="color:red; font-size:12px;display:none" id="userModalUserNameLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="alert-text" style="text-align: center">이메일</td>
                            <td style="text-align:left" id="userModalUserEmailTd">
                                <p id="userModalEditUserEmail" style="display: none"></p>
                                <input id="userModalUserEmail" name="userModalUserEmail" oninput="inputCheck(this)" type="email" maxlength="33"
                                       class="form-control" placeholder="이메일을 입력하세요.">
                                <span class="icon-x" style="color:red; font-size:12px; display:none"
                                      id="userModalUserEmailValidation">&nbsp;값이 존재하지 않습니다.</span>
                                <span style="color:red; font-size:12px;display:none" id="userModalUserEmailLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="alert-text" style="text-align: center">부서명</td>
                            <td style="text-align:left">
                                <select id="userModalSelectedTeamName" name="userModalSelectedTeamName"
                                        class="form-control">
                                    <option value="">부서명을 선택해주세요.</option>
                                    <c:forEach items="${teamListMap}" var="map2">
                                        <option value= ${map2.codeId}>${map2.codeName}</option>
                                    </c:forEach>
                                </select>
                                <span class="icon-x" style="color:red; font-size:12px; display:none"
                                      id="userModalSelectedTeamNameValidation">&nbsp;값이 존재하지 않습니다.</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="alert-text" style="text-align: center">권한</td>
                            <td style="text-align:left">
                                <div class="form-group">
                                    <select id="userModalSelectedAuthority" name="userModalSelectedAuthority"
                                            class="form-control">
                                        <option value="">권한을 선택해주세요.</option>
                                        <c:forEach items="${authorityListMap}" var="map">
                                            <option value= ${map.authorityId}>${map.authorityName}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="icon-x" style="color:red; font-size:12px; display:none"
                                          id="userModalSelectedAuthorityValidation">&nbsp;값이 존재하지 않습니다.</span>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-primary" id="userModalButton">등록</button>
                </div>
            </div>
        </div>
    </div>
</form>
<form id="logoutForm"></form>
<script>
    $(document).ready(function () {
        var size = ${userAllListSize};
        var originPage = ${pageNum};
        $("#activePage").val(originPage);
        setPagination(size, originPage);
        setEndSize(originPage,size);
        $('.layerPopup')[0].style.display='none';
        $("#userModalButton").click(function (e) {
            var clickButton = $("#userModalButton").html();
            if (clickButton == "등록") {
                insertFunction();
            } else {
                var myAuthority =${myAuthority};
                var userEmail = $("#userModalEditUserEmail").html();
                if(myAuthority.userEmail != userEmail ){
                    $("#completeModalButton").attr("onclick","")
                     updateUser();
                }else{
                    $("#warnModalContent").text("내 정보 수정 시 로그아웃 됩니다.수정하시겠습니까?")
                    $("#warnModalButton").attr("onclick", "updateUser()");
                    $("#completeModalButton").attr("onclick","logout()")
                    $("#userModal").modal("hide");
                    $('#warnModal').modal("show");
                }
            }
        })
    })

    function logout(){
        $("#logoutForm")[0].action = "/logout";
        $("#logoutForm").submit();
    }

    function movePage(move, activePage, nextPage, setPage, prePage) {
        setSection(move, activePage, prePage, nextPage, setPage);
        if (move == 0) {
            userList(activePage);
            $("#activePage").val(activePage);
        } else if (move == 1) {
            userList(nextPage);
            $("#activePage").val(nextPage);
        } else if (move == -1) {
            userList(prePage);
            $("#activePage").val(prePage);
        }
    }
    document.addEventListener("keyup", function(event) {
        if(event.keyCode == '13'){
            $("#keywordSearchbtn").click();
        }
    });
    function search() {
        userList(1);
    }
    function userList(active) {
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/userList",
            type: 'GET',
            data: {
                "userName": $("#userName").val().trim(),
                "userEmail": $("#userEmail").val().trim(),
                "codeName": $("#codeName").val().trim(),
                "pageNum": active
            },
            success: function (data) {
                if(data.responseCode == "SUCCESS" ) {
                    var responseMessage = data.responseMessage;
                    if (responseMessage.userAllListSize == 0) {
                        setEmpty();
                        setPagination(responseMessage.userAllListSize, active);
                        $('.layerPopup')[0].style.display = 'none';
                    } else {
                        setTbody(responseMessage.userList, active, responseMessage.userAllListSize);
                        setSection(0, 1, 1, 2, responseMessage.userAllListSize);
                        setPagination(responseMessage.userAllListSize, active);
                        $('.layerPopup')[0].style.display = 'none';
                    }
                }
            }
        });
    }

    function setEmpty() {
        var userTable = "";
        $("#changeBody").empty();
        $('#allSizePrint').text("총 0 건 ")
        userTable += "<tr><td></td><td></td><td>검색 결과가 없습니다</td><td></td><td></td><td></td></tr>";
        $("#changeBody").append(userTable);
    }

    function setTbody(data, activePage,size) {
        var bodyAll = $("#changeBody");
        var loginAuthority =  ${loginAuthority};
        bodyAll.empty();
        setEndSize(activePage, size)
        var newBody = "";
        data.forEach(function (el, index) {
            var idx = (activePage - 1) * 15 + index + 1;
            //reload
            newBody +=
                "<tr>" +
                "<td>" + idx + "</td>" +
                "<td>" + el.userName + "</td>" +
                "<td>" + el.userEmail + "</td>" +
                "<td>" + el.codeName + "</td>" +
                "<td>" + el.authorityName + "</td>" +
                "<td style=\"display: flex; align-items: center; justify-content: center; border-left: none; border-top: none\">" +
                "<button data-toggle=\"modal\" style=\"margin: 1px;\" class=\"btn btn-icon\" onclick=\"detailModal(\'" + el.userName + "\',\'" + el.userEmail + "\', \'" + el.codeName + "\',\'" + el.authorityName + "\')\"><i class=\"icon-srch text-dark\"></i></button>";
            if(loginAuthority > el.authorityId){
                newBody +=
                    "<button data-toggle=\"modal\" style=\"margin: 1px;\" class=\"btn btn-icon\" style='filter: brightness(93%);' disabled><i class=\"icon-edit text-dark\"></i></button>" +
                    "<button data-toggle=\"modal\" style=\"margin: 1px;\" class=\"btn btn-icon\" id=\"delete\" style='filter: brightness(93%);' disabled><i class=\"icon-delete\"></i></button>";
            }else{
                newBody +=
                    "<button data-toggle=\"modal\" style=\"margin: 1px;\" class=\"btn btn-icon\" onclick=\"updateModal(\'" + el.userEmail + "\')\"><i class=\"icon-edit text-dark\"></i></button>" +
                    "<button data-toggle=\"modal\" style=\"margin: 1px;\" class=\"btn btn-icon\" id=\"delete\" onclick=\"deleteModal(\'" + el.userEmail + "\' ,\'" + el.authorityId + "\')\"><i class=\"icon-delete\"></i></button>";
            }
            newBody += "</td></tr>";
        })
        bodyAll.append(newBody);
    }

    // 등록 모달
    function insertFunction() {
        if (nullCheck('insert')) {
            $('.layerPopup')[0].style.display='block';
            $.ajax({
                contentType: 'application/json; charset=utf-8',
                type: "POST",
                url: "/user",
                data: JSON.stringify({
                    "userName": $('input[name=userModalUserName]').val(),
                    "userEmail": $('input[name=userModalUserEmail]').val(),
                    "teamId": $('select[name=userModalSelectedTeamName]').val(),
                    "authorityId": $('select[name=userModalSelectedAuthority]').val()
                }),
                success: function (data) {
                    if (data.responseCode == "SUCCESS") {
                        $("#userModalUserEmailValidation").css("display", "none");
                        $("#completeModalContent").html('등록되었습니다.');
                        $('#userModal').modal('hide');
                        $('#completeModal').modal('show');
                        $("#userModalForm")[0].reset();
                        userList(1);
                    } else if (data.responseCode == "ERROR"){
                        $("#userModalUserEmailValidation").html("&nbsp;" + data.responseMessage);
                        $("#userModalUserEmailValidation").css("display", "unset");
                    }
                }
            })
        }
    }

    //nullCheck and emailCheck
    function nullCheck(type) {
        let emailRegex = new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}');
        let col = [];
        if (type == "insert") {
            col = ["userModalUserName", "userModalUserEmail", "userModalSelectedTeamName", "userModalSelectedAuthority"];
        } else {
            col = ["userModalSelectedTeamName", "userModalSelectedAuthority"];
        }
        var state = true;
        col.forEach(function (name) {
            if ($("#" + name).val() == '' || typeof $("#" + name).val() == "undefined" || $("#" + name).val() == null) {
                if (name == "userModalUserEmail") {
                    $("#userModalUserEmailValidation").html("&nbsp;값이 존재하지 않습니다.");
                    state = false;
                }
                $("#" + name + "Validation").css("display", "unset");
                state = false;
            } else {
                $("#" + name + "Validation").css("display", "none");
                if (name == "userModalUserEmail" && $("#userModalUserEmail").val() != "" && !emailRegex.test($("#userModalUserEmail").val())) {
                    $("#userModalUserEmailValidation").html("&nbsp;이메일 형식이 올바르지 않습니다.");
                    $("#userModalUserEmailValidation").css("display", "unset");
                    state = false;
                }
            }
        });
        return state;
    }

    //수정 모달
    function updateModal(userEmail) {
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            contentType: 'application/json; charset=utf-8',
            type: "GET",
            url: "/getUserInfo",
            data: {
                "userEmail": userEmail,
                "type": "email"
            },
            success: function (data) {
                if (data.responseCode == "SUCCESS") {
                    $("#userModalUserNameValidation").css("display", "none");
                    $("#userModalUserEmailValidation").css("display", "none");
                    $("#userModalSelectedTeamNameValidation").css("display", "none");
                    $("#userModalSelectedAuthorityValidation").css("display", "none");
                    $("#userModalHeader").html("사용자 수정");
                    $("#userModalButton").html("수정");
                    $("#userModalEditUserName").html(data.responseMessage.userName);
                    $("#userModalEditUserEmail").html(data.responseMessage.userEmail);
                    $("#preAuthorityId").val(data.responseMessage.authorityId);
                    $("#userModalUserName").css("display", "none");
                    $("userModalUserNameValidation").css("display", "none");
                    $("#userModalEditUserName").css("display", "unset");
                    $("#userModalUserEmail").css("display", "none");
                    $("userModalUserEmailValidation").css("display", "none");
                    $("#userModalEditUserEmail").css("display", "unset");
                    $("#userModalSelectedTeamName").val(data.responseMessage.teamId);
                    $("#userModalSelectedAuthority").val(data.responseMessage.authorityId);
                    $('#userModal').modal('show');
                }
            }
        })

    }

    function updateUser() {
        if (nullCheck('update')) {
            $.ajax({
                type: "PUT",
                url: "/user",
                data: {
                    "userEmail": $("#userModalEditUserEmail").html(),
                    "teamId": $('select[name=userModalSelectedTeamName]').val(),
                    "authorityId": $('select[name=userModalSelectedAuthority]').val(),
                    "preAuthorityId": $('#preAuthorityId').val()
                },
                success: function (data) {
                    if (data.responseCode == "SUCCESS") {
                        $("#userModalUserEmailValidation").css("display", "none");
                        $("#completeModalContent").html('수정되었습니다.');
                        $('#userModal').modal('hide');
                        $('#completeModal').modal('show');
                        $("#userModalForm")[0].reset();
                        userList($("#activePage").val());
                    } else if (data.responseCode == "ERROR"){
                        $("#userModalUserEmailValidation").html("&nbsp;" + data.responseMessage);
                        $("#userModalUserEmailValidation").css("display", "unset");
                    }
                }
            })
        }
    }

    function insertModal() {
        let selectValue = document.querySelector("select[name=userModalSelectedTeamName]").options;
        $("#userModalForm")[0].reset();
        $("#userModalSelectedTeamNameValidation").css("display", "none");
        $("#userModalSelectedAuthorityValidation").css("display", "none");
        $("#userModalSelectedTeamName").val('');
        $("#userModalSelectedAuthority").val('');
        $("#userModalHeader").html("사용자 등록");
        $("#userModalEditUserName").css("display", "none");
        $("#userModalUserNameValidation").css("display", "none");
        $("#userModalUserName").css("display", "unset");
        $("#userModalEditUserEmail").css("display", "none");
        $("#userModalUserEmailValidation").css("display", "none");
        $("#userModalUserEmail").css("display", "unset");
        $("#userModalButton").html("등록");
        $('#userModal').modal('show');
        for (let i=0; i<selectValue.length; i++) {
            if (selectValue[i].value == '${teamId}') selectValue[i].selected = true;
        }
    }

    function deleteModal(userEmail, authorityId) {
        $('#warnModalContent').text("삭제하시겠습니까?");
        $('#deleteUserEmail').val(userEmail);
        $('#deleteAuthorityId').val(authorityId);
        $("#warnModalButton").attr("onclick","deleteFunction()");
        $('#warnModal').modal('show');
    }

    function deleteFunction() {
        $('.layerPopup')[0].style.display='block';
        var userEmail = $('#deleteUserEmail').val();
        var authorityId = $('#deleteAuthorityId').val();
        $.ajax({
            type: "DELETE",
            url: "/user",
            data: {
                "userEmail": userEmail,
                "authorityId": authorityId
            },
            success: function (data) {
                if (data.responseCode == "SUCCESS") {
                    $("#userModalUserEmailValidation").css("display", "none");
                    $("#completeModalContent").html('삭제되었습니다.');
                    $('#userModal').modal('hide');
                    $('#completeModal').modal('show');
                    $("#userModalForm")[0].reset();
                    userList(1);
                }
            }
        })
    }

     function inputCheck(e){
        if($(e).val() != ""){
            if($(e).attr("maxlength") !== undefined ){
                if($(e).attr("maxlength")< $(e).val().length){
                    $('#'+$(e).attr("id") +'Length').css('display', 'block');
                }else{
                    $('#'+$(e).attr("id") +'Length').css('display', 'none');
                }
            }
            $('#'+$(e).attr("id")+'Validation').css('display', 'none');
        }
    }
</script>
