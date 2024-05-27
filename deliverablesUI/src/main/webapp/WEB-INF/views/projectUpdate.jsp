<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<style>
    .projectUserModal{
        height:70%;
        width: 100% !important;
    }
    .user-box{
        width: 100%;
        height: 100%;
    }
    .user-div{
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        padding-bottom: .75rem;
        position: relative;

    }
    .managerName{
        width: 330px;
        align-self: center;
        text-align:left;
        display:none;
    }
    .modal-content{
        width: 70%;
    }
</style>


<!-- 프로젝트 추가 수정 Modal Dialog -->
<div id="projectAddMod" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title" id="projectAddModTitle"></h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close">
                    <i class="icon-close"></i>
                </button>
            </div>
            <div class="modal-body modal-body-ct">
                <table class="table">
                    <colgroup>
                        <col style="width: 30%;">
                        <col style="width: 70%">
                    </colgroup>
                    <tbody>
                    <input type="hidden" class="form-control" id="inspectionStatus" >
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">프로젝트 명</td>
                        <td class='tdLeft' name="addProject"><input type="text" class="form-control" id="projectName" placeholder="프로젝트명을 입력하세요." maxlength="166" style="vertical-align: middle;" oninput="inputCheck(this)">
                        <span style="color:red; font-size:12px;display:none" id="projectNameValidation" class="icon-x">&nbsp;프로젝트명을 입력해주세요</span>
                        <span style="color:red; font-size:12px;display:none" id="projectNameDuplication" class="icon-x">&nbsp;중복된 프로젝트명 입니다. 다시 입력해주세요</span>
                        <span style="color:red; font-size:12px;display:none" id="projectNameLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                        </td>
                        <td class='tdLeft' name="modProject" style="vertical-align: middle; display:none" id="projectNameMod" name="projectName" ></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">고객사 명</td>
                         <td class='tdLeft' name="addProject"><input type="text" class="form-control" id="customerName" placeholder="고객사명을 입력하세요." style="vertical-align: middle;" maxlength="166"  oninput="inputCheck(this)">
                            <span style="color:red; font-size:12px;display:none" id="customerNameValidation" class="icon-x">&nbsp;고객사명을 입력해주세요</span>
                            <span style="color:red; font-size:12px;display:none" id="customerNameLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                        </td>
                        <td class='tdLeft' name="modProject" style="vertical-align: middle; display:none" id="customerNameMod" name="customerName" ></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">프로젝트 코드</td>
                        <td class='tdLeft' name="addProject" ><input type="text" class="form-control" id="projectCode" placeholder="프로젝트 코드를 입력하세요." style="vertical-align: middle;" maxlength="16" oninput="inputCheck(this)">
                            <span style="color:red; font-size:12px;display:none" id="projectCodeValidation" class="icon-x">&nbsp;프로젝트 코드를 입력해주세요</span>
                            <span style="color:red; font-size:12px;display:none" id="projectCodeDuplication" class="icon-x">&nbsp;중복된 프로젝트 코드 입니다. 다시 입력해주세요</span>
                            <span style="color:red; font-size:12px;display:none" id="projectCodeLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                        </td>
                        <td  class='tdLeft' name="modProject" style="vertical-align: middle; display:none" id="projectCodeMod" name="projectCode" ></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">담당자</td>
                        <td class='tdLeft'>
                            <div style="float: inline-start;">
                                <div class="form-group" style="float: left;display: flex;">
                                    <a class="managerName" id="managerName" style="display:none" data-value="null" >담당자명</a>
                                    <a href="#updateProjectUser" onclick="checkManager()" data-toggle="modal" class="btn btn-primary" style="display:flex; align-items:center; vertical-align: middle;float:left; max-height: 30px; max-width: 104px;"><span class="hide" id="managerBtn">담당자 추가</span></a>
                                </div>
                                <div>
                                    <span style="color:red; font-size:12px;display:none;align-self:center" id="managerNameValidation" class="icon-x">&nbsp;담당자를 추가해주세요</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">부서명</td>
                        <td class='tdLeft'>
                            <select id="teamId" name="teamId" class="form-control">
                                <c:forEach var="team" items="${commonTeam}" varStatus="index"  >
                                    <option value=${team.codeId}>${team.codeName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr >
                        <td class="alert-text" style="vertical-align: middle;">라이선스 수량</td>
                        <td class='tdLeft' style="vertical-align: middle;">
                            <div class="form-group" style="display: flex; align-items: center;">
                                <label>HUB : </label> &nbsp;
                                <input type='number' min='1' max='999' oninput="inputCheck(this)"
                                    class="form-control" id="hubContractCount" placeholder="HUB수량 입력" name="hubContractCount"
                                    style="width: 5px; height: 25px; font-size: 10px;"  >&nbsp;
                                <label>Agent : </label>&nbsp;
                                <input type='number' min='1' max='999' oninput="inputCheck(this)"
                                    class="form-control" id="agentContractCount" name="agentContractCount"  placeholder="Agent수량 입력"
                                    style="width: 5px; height: 25px; font-size: 10px;">
                            </div>
                            <span style="color:red; font-size:12px;display:none" id="hubContractCountValidation" class="icon-x">&nbsp;HUB 수량을 입력해주세요</span>
                            <span style="color:red; font-size:12px;display:none" id="agentContractCountValidation" class="icon-x">&nbsp;Agent 수량을 입력해주세요</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle;">프로젝트 상태</td>
                        <td class='tdLeft' >
                            <select id="projectStatus" name="projectStatus" onchange="statusValidation()" class="form-control">
                                <c:forEach var="status" items="${commonStatus}" varStatus="index">
                                    <option value=${status.codeId}>${status.codeName}</option>
                                </c:forEach>
                            </select>
                            <span style="color:red; font-size:12px;display:none" id="projectStatusValidation" class="icon-x">&nbsp;검수완료 상태가 아닌 프로젝트는 해당 상태를 선택하실수 없습니다.</span>
                        </td>
                    </tr>

					</tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button type="button" id="detailModBtn" style="display:none" onclick="updateDetailProject()" class="btn btn-primary">수정</button>
                <button type="button" id="modBtn" style="display:none" onclick="updateProject()" class="btn btn-primary">수정</button>
                <button type="button" id="addBtn" style="display:none" onclick="insertProject()" class="btn btn-primary">등록</button>
            </div>
        </div>
    </div>
</div>

<!-- 담당자 추가 Modal Dialog -->
<div id="updateProjectUser" class="modal fade" tabindex="-1" role="dialog" >
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content projectUserModal"  >
            <div class="modal-header">
                <h2 class="modal-title" id="managerAddTitle">담당자 추가</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i class="icon-close"></i></button>
            </div>
            <div class="modal-body modal-body-ct">
                <div class="ct-header">
                    <button type="button" class="btn-filter collapsed d-xl-none" data-toggle="collapse" data-target="#collapse-filter-service">검색 필터<i class="icon-down"></i></button>
                    <div id="collapse-filter-service" class="collapse collapse-filter">
                        <div class="filter no-gutters">
                            <div class="col">
                                <label class="form-control-label">
                                    <b class="control-label">사용자명</b>
                                    <input type="text" class="form-control"  id="userSearch" placeholder="사용자명을 입력하세요.">
                                </label>
                            </div>
                            <div class="col" style="max-width: 50%;">
                                <label class="form-control-label">
                                    <b class="control-label">부서명</b>
                                    <input type="text" class="form-control" id="teamName" placeholder="부서명을 입력해주세요">
                                </label>
                            </div>
                            <div class="col-auto">
                                <button type="button" class="btn" id="userSearchBtn" onclick="userSearch()"><i class="icon-srch"></i>조회</button>
                            </div>
                        </div>
                    </div>
                    <div class="user-div">
                        <div class="user-box" id="user-box">
                        </div>
                    </div>
                </div>
				<div class="ct-content">
					<div class="table-responsive">
						<table class="table"
							style="border-left: 1px solid #f0f1f6; border-right: 1px solid #f0f1f6;">
							<colgroup>
		                        <col style="width: 5%;">
		                        <col style="width: 55%">
		                        <col style="width: 20%">
		                        <col style="width: 20%">
		                    </colgroup>
							<thead class="thead-fixed">
								<tr>
									<th></th>
									<th>이메일</th>
									<th>사용자명</th>
									<th>부서명</th>
								</tr>
							</thead>
                            <tbody id="userSelect">
                                <c:forEach var="user" items="${userAllList}" varStatus="index">
                                    <tr>
                                        <td style="border-right:0px">
	                                        <label id="checkUser" class="custom-control custom-checkbox">
	                                            <input type="checkbox" class="custom-control-input" value="${user.userEmail}" id="${user.userId}" name="${user.userName}" onclick="checkedChange(this)"/>
	                                            <span class="custom-control-label"></span>
	                                        </label>
                                        </td>
                                        <td>${user.userEmail}</td>
                                        <td>${user.userName}</td>
                                        <td>${user.codeName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
						</table>
                    </div>
				</div>
			</div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button type="button" id="managerSubmitBtn" class="btn btn-primary" data-dismiss="modal" onclick="setManager()">수정</button>
            </div>
        </div>
    </div>
</div>

<script>
    // 사용자 추가
    function checkManager (){
    	$('input[type="checkbox"]').prop("checked", false);
        $("#user-box").empty();
        $("#userSearch").val('');
        let setData = null ;
        if('${authorityId}' == '2' || '${authorityId}' == '3') {
            $("#teamName").val('${codeName}')
        }else{
            $("#teamName").val('');
        }
        if($("#managerName").attr("data-value") !== undefined){
            setData =JSON.parse($("#managerName").attr("data-value"));
        }
        if( setData !== null ){
            setData.forEach(function(user) {
            	var userValue = {
                    userEmail : user.userEmail,
                    userName : user.userName,
                    userId : user.userEmail.replaceAll('@', '').replaceAll('\.', '')
                };
            	$('#'+user.userId).prop("checked", true);
                $("#user-box").append("<a class='btn btn-m' style='margin-right:10px;margin-top:10px'"
                		+ "onclick=\"deleteUser('"+  user.userId +"Del')\""
                		+ "id='"+ user.userId+"Del' "
                		+ "name='"+ user.userId
                		+ "' value="+JSON.stringify(userValue)+">"
                		+ "<span class='hide'>" + user.userName+"</span>"
                		+ "&nbsp;<i class='icon-close' style='margin-right:0px' ></i></a>");
            })
        }
        userSearch();
    }
    function statusValidation(){
        $("#projectStatusValidation").css("display","none")
        if( $("#projectStatus").val() == "C20240415003952443" || $("#projectStatus").val() == "C20240214100559281"){
            if( $("#inspectionStatus").val() != 2){
                $("#projectStatusValidation").css("display","unset")
            }else{
                $("#projectStatusValidation").css("display","none")
            }
        }
    }
    function checkedChange(e){
        var id = $(e).attr("id");
        var userValue = {
            userEmail : $(e).val(),
            userName :$(e).attr("name"),
            userId : $(e).val().replaceAll('@', '').replaceAll('\.', '')
        };
        if($(e).is(':checked')){
            $('a[name='+$(e).attr("id")+']').val(userValue)
            $("#user-box").append("<a class='btn btn-m' style='margin-right:10px;margin-top:10px'  onclick=\"deleteUser('"+
            		$(e).attr("id")+"Del')\" id='"+$(e).attr("id")+"Del' name='"+$(e).attr("id")+"' value="+JSON.stringify(userValue)+"><span class='hide'>"+
            		$(e).attr("name")+"</span>&nbsp;<i class='icon-close' style='margin-right:0px' ></i></a>")
        }else{
            deleteUser(id)
        }
	}

	function inputCheck(e){
        if($(e).val() != ""){
            if($(e).attr("maxlength") !== undefined ){
                if($(e).attr("maxlength")<$(e).val().length){
                    $('#'+$(e).attr("id") +'Length').css('display', 'block');
                }else{
                    $('#'+$(e).attr("id") +'Length').css('display', 'none');
                }
            }
            if( $(e).attr("id") == "hubContractCount" || $(e).attr("id") == "agentContractCount"){
                $(e).val( $(e).val().replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1'));
                if($(e).val() > 999){
                    $(e).val(999)
                }
            }
            $('#'+$(e).attr("id") +'Validation').css('display', 'none');
        }
    }

	function deleteUser(id){
	    $('#'+id.replace("Del","")).prop("checked", false);
	    $('a[name='+id.replace("Del","")+']').remove()
	}

    function setManager(){
	    let setName = "";
	    const children = $("#user-box").children('a');
	    var allValue = new Array();
        for(i = 0; i < children.length; i++) {
            const data = JSON.parse($(children[i]).attr("value"))
            allValue.push(data)
            setName +=  data.userName + ","
        };
        $("#managerName").text(setName.slice(0, -1))
        $("#managerName").attr("data-value",JSON.stringify(allValue))
        if(allValue.length != 0){
            $("#managerNameValidation").css("display", "none")
            $("#managerName").css("display","unset")
        }else{
            $("#managerName").css("display","none")
        }
	}

	function userSearch(){
        var userTr ="";
        $("#userSelect").empty();
        $('.layerPopup')[0].style.display='block';
        
        var setName = "";
        const children = $("#user-box").children('a');
        for(i = 0; i < children.length; i++) {
            const data = JSON.parse($(children[i]).attr("value"))
            setName +=  data.userEmail + ","
        };
	    $.ajax({
            url: "/projectUserSearch",
            type : 'GET',
            data :{
                  "userName" : $("#userSearch").val().trim(),
                  "codeName": $("#teamName").val().trim()
              },
            success :function( data ) {
                if(data.responseCode == "SUCCESS" ){
                    if(data.responseMessage == 0){
                        userTr += "<tr><td></td><td>검색결과가 없습니다.</td><td></td><td></td></tr>"
                        $("#userSelect").append(userTr)
                    }else{
                    	var user = data.responseMessage;
                        user.forEach (function (el, index) {
                            userTr += "<tr><td><label id='checkUser' class='custom-control custom-checkbox'>"
                            userTr += "<input type='checkbox' class='custom-control-input'  value='" + el.userEmail + "' id='" + el.userId + "' name='" + el.userName + "'";
                            if(setName.indexOf(el.userEmail+",") != -1){
                            	userTr += "checked='true'";
                            }
                            userTr += "onclick='checkedChange(this)'><span class='custom-control-label'></span></label></td>"
                            userTr += "<td>"+el.userEmail+"</td><td>"+el.userName+"</td><td>"+el.codeName+"</td></tr>"
                        });
                        $("#userSelect").append(userTr);
                    }
                }
            }
        });
	}
</script>

