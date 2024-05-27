<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<header class="card-header">
	<h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-ul"></i></span>상세조회</h2>
	<div class="btn-container">
		<c:if test="${projectInfo.inspectionStatus eq '2'}">
			<a onclick="deliverablesDownload('${projectInfo.projectId}', '${projectInfo.projectName}')" data-toggle="modal" class="btn btn-m">산출물 다운로드</a>
		</c:if>
	</div>
</header>
<div class="ct-content">
	<div class="table-responsive">
		<table class="table">
			<thead>
				<tr>
					<th colspan="4" style="text-align: center">프로젝트 정보</th>
				</tr>
			</thead>
			<tbody id="projectTbody">
				<tr>
					<td style="width: 200px" colspan="1">프로젝트 명</td>
					<td class="tdLeft" colspan="3"  name="projectName">${projectInfo.projectName}</td>
				</tr>
				<tr>
					<td colspan="1">고객사 명</td>
					<td class="tdLeft" colspan="3" name="customerName" >${projectInfo.customerName}</td>
				</tr>
				<tr>
					<td colspan="1">프로젝트 코드</td>
					<td class="tdLeft" colspan="3" name="projectCode" >${projectInfo.projectCode}</td>
				</tr>
				<tr>
					<td colspan="1">부서명</td>
					<td class="tdLeft" colspan="3" name="teamName">${projectInfo.teamName}</td>
				</tr>
				<tr>
					<td colspan="1">담당자</td>
					<c:if test="${projectUser == '' }">
					    <td class="tdLeft" colspan="3" name="projectUser" >-</td>
                    </c:if>
                    <c:if test="${projectUser != ''}">
                        <td class="tdLeft" colspan="3" name="projectUser">${projectUser}</td>
                    </c:if>
				</tr>
				<tr>
					<td colspan="1">라이선스 수량<br>(HUB/Agent)
					</td>
					<td class="tdLeft" colspan="3" name="license">(${projectInfo.hubContractCount} / ${projectInfo.agentContractCount})</td>
				</tr>
				<tr>
					<td colspan="1">프로젝트 상태</td>
					<td class="tdLeft" colspan="3" name="projectStatusName">${projectInfo.projectStatusName}</td>
				</tr>
				<tr>
					<td colspan="1">검수 상태</td>
					<td class="tdLeft" colspan="3" name="inspectionStatusName">${projectInfo.inspectionStatusName}</td>
				</tr>
				<tr>
					<c:if test="${projectInfo.inspectionStatus eq '3'}">
						<td colspan="1">반려 사유</td>
						<td class="tdLeft" colspan="3"name="inspectionRejectionReason">${projectInfo.inspectionRejectionReason}</td>
					</c:if>
				</tr>
				<tr>
					<td>검수일</td>
					<td class="tdLeft" style="width: 300px" name="inspectionDate">
					<c:if test="${projectInfo.inspectionDate == null }">-</c:if>
					<c:if test="${projectInfo.inspectionDate != null }">${projectInfo.inspectionDate}</c:if> </td>
				</tr>
				<tr>
                    <td style="width: 200px">검수자</td>
                    <td class="tdLeft" style="width: 300px" name="inspectionUserName">
                        <c:if test="${projectInfo.inspectionUserName == null }">-</c:if>
                        <c:if test="${projectInfo.inspectionUserName != null }">${projectInfo.inspectionUserName}</c:if>
                    </td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="ct-footer" style="display: flex; flex-wrap: wrap; align-items: center; justify-content: flex-end; padding: 0.7rem 0; margin: 1.5rem;">
		<button type="button" class="btn" id="goProjectList" >목록보기</button>
		&nbsp;&nbsp;
        <c:if test="${authorityId != 4}">
            <button type="button" href="" data-toggle="modal" id='setDetailModData' class="btn btn-primary">수정</button>
            &nbsp;&nbsp;
            <button href="" class="btn btn-primary" data-toggle="modal"  onclick="deleteProject('${projectInfo.projectId}');">삭제</button>
        </c:if>
	</div>
</div>


<c:import url="/WEB-INF/views/projectUpdate.jsp">
    <c:param name="userAllList" value="${userAllList}"/>
    <c:param name="commonStatus" value="${commonStatus}"/>
    <c:param name="commonTeam" value="${commonTeam}"/>
</c:import>

<form id="projectList" method="get">
    <input type="hidden" id= "searchVal" name="searchVal" />
    <input type="hidden" id= "pageNum" name="pageNum" value="${pageNum}"/>
</form>

<input type="hidden" id="projectId" name="projectId" value="${projectId}"/>
<script>
	$(document).ready(function () {
	    $('.layerPopup')[0].style.display='none';

        var url = new URL(window.location.href);
        var urlParams = url.searchParams;
        $("#projectId").val(urlParams.get('projectId'))

		$('#projectList').submit(function() {
            $(this).ajaxSubmit({
                beforeSubmit: function () {
                    $('.layerPopup')[0].style.display='block';
                },
                success: function(res){
                    $('.layerPopup')[0].style.display='none';
                },
                error: function(res){
                    $('.layerPopup')[0].style.display='none';
                    location = "/error";
                }
            });
        });
	});

    document.addEventListener("keyup", function(event) {
      if(event.keyCode == '13'){
        $("#userSearchBtn").click();
      }
    });


    $("#goProjectList").click(function (){
        if('${search}' != ''){
		    var search = '${search}'
            $("#searchVal").val(search);
        }
        $("#projectList")[0].action = "/project";
        $('.layerPopup')[0].style.display='block';
        $("#projectList").submit();
	})
    function nullCheck(){
        let col = [ "hubContractCount","agentContractCount"]
        var state = "true";
        col.forEach(function(name) {
            if($("#"+name).val()  == '' || typeof $("#"+name).val()  == "undefined" || $("#"+name).val() == null){
               $("#"+name+"Validation").css("display", "unset")
               state = "false";
            }else{
                $("#"+name+"Validation").css("display", "none")
            }
        });
        if ( $("#managerName").attr("data-value") == "null" || $("#managerName").attr("data-value") == "[]"){
            $("#managerNameValidation").css("display", "unset")
            state = "false";
        }
        return  state
	}
    // 프로젝트 수정
    $("#setDetailModData").click(function (){
        $('.layerPopup')[0].style.display='block';
        $('#detailModBtn').css("display",'unset');
        $('#projectAddModTitle').text("프로젝트 수정")
        $('#managerAddTitle').text("담당자 수정")
        $('#managerBtn').text("담당자 수정")
        $('#managerSubmitBtn').text("수정")
        $.ajax({
            url: "/oneProject",
            type : 'GET',
            data : "projectId="+$("#projectId").val(),
            success :function( data ) {
             var responseMessage = data.responseMessage[0];
             if(data.responseCode == "SUCCESS"){
                for(var key in responseMessage) {
                    if( key == "manager"){
                		if(responseMessage[key] != null ){
                			let managerData =responseMessage[key].split("|")
                			var managerVal = new Array();
                			var managerText = "";
                			managerData.forEach(function (manager){
                				var managerUserEmail = manager.split("/")[1];
                				let json = 	{
                					userName :manager.split("/")[0],
                					userEmail : managerUserEmail,
                					userId : managerUserEmail.replaceAll('@', '').replaceAll('\.', '')
                				};
                				managerVal.push(json)
                				managerText += manager.split("/")[0]+",";
                			})
                			$("#managerName").css("display","unset");
                			$("#managerName").text(managerText.substring(0, managerText.length - 1));
                			$("#managerName").attr("data-value",JSON.stringify(managerVal));
                		}else{
                		    $("#managerName").css("display","none");
                            $("#managerName").attr("data-value",null);
                		}
                    }else{
                      $("#"+key).val(responseMessage[key]);
                      $("#"+key+"Mod").text(responseMessage[key]);
                      if( key =="projectStatusName" ){
                         $("#projectStatus").attr("text",responseMessage[key] );
                      }
                      if( key =="teamName"){
                         $("#teamId").attr("text",responseMessage[key] );
                      }
                    }
                }
             }else if(data.responseCode == "ERROR" ){
                 $('#warnModalContent').text(data.responseMessage)
                 $('#warnModal').modal('show')
             }
            }
        });
        $('td[name=addProject]').css('display','none');
        $('td[name=modProject]').css('display','');
        $("#projectAddMod").modal('show');
        if( $("#managerName").attr("data-value") != "null" ){
             $("#managerName").css("display","unset")
        }
    })


	function updateDetailProject() {
        if(nullCheck() == "true" && $("#projectStatusValidation").css("display") == "none"){
            let data = JSON.parse($("#managerName").attr("data-value"))
            let updateData = {
                projectId : $("#projectId").val(),
                customerName : $("#customerName").val(),
                hubContractCount : $("#hubContractCount").val(),
                agentContractCount : $("#agentContractCount").val(),
                projectStatus : $("#projectStatus").val(),
                teamId : $("#teamId").val(),
                manager : data
            };
            $('.layerPopup')[0].style.display='block';
            $.ajax({
              url: "/project",
              type : 'PUT',
              data : updateData,
              success :function( data ) {
                if(data.responseCode == "SUCCESS"){
                	var responseMessage = data.responseMessage;
                    $("#projectAddMod").modal('hide')
                    $('#completeModalContent').text('수정되었습니다.')


                    const newData = responseMessage.oneProject[0];
                    $("#projectTbody").empty();
                    var projectTr = "";

                    projectTr += "<tr><td  style='width: 200px' colspan='1'>프로젝트 명</td><td class='tdLeft' colspan='3' name='projectName'>"+newData.projectName+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>고객사 명</td><td class='tdLeft' colspan='3' name='customerName'>"+newData.customerName+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>프로젝트 코드</td><td class='tdLeft' colspan='3' name='projectCode'>"+newData.projectCode+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>부서명</td><td class='tdLeft' colspan='3' name='teamName'>"+newData.teamName+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>담당자</td><td class='tdLeft' colspan='3' name='projectUser'>"+responseMessage.projectUserName+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>라이선스 수량<br>(HUB/Agent)</td><td class='tdLeft' colspan='3' name='license'>("+newData.hubContractCount+"/"+newData.hubContractCount+")</td></tr>"
                    projectTr += "<tr><td colspan='1'>프로젝트 상태</td><td class='tdLeft' colspan='3' name='projectStatusName'>"+newData.projectStatusName+"</td></tr>"
                    projectTr += "<tr><td colspan='1'>검수 상태</td><td class='tdLeft' colspan='3' name='inspectionStatusName'>"+newData.inspectionStatusName+"</td></tr>"
                    if(newData.inspectionDate != null ){
                        projectTr += "<tr><td>검수일</td><td class='tdLeft'  style='width: 300px' name='inspectionDate'>"+newData.inspectionDate+"</td></tr>"
                    }else{
                        projectTr += "<tr><td>검수일</td><td class='tdLeft' style='width: 300px' name='inspectionDate'>-</td></tr>"
                    }
                    if( newData.inspectionUserName != null ){
                        projectTr += "<tr><td style='width: 200px'>검수자</td><td  class='tdLeft' style='width: 300px' name='inspectionUserName'>"+newData.inspectionUserName+"</td></tr>"
                    }else{
                        projectTr += "<tr><td style='width: 200px'>검수자</td><td class='tdLeft' style='width: 300px' name='inspectionUserName'>-</td></tr>"
                    }
                    $("#projectTbody").append(projectTr);
                    $("td[name=hubContractCount]").val(newData.hubContractCount)
                    $("td[name=agentContractCount]").val(newData.agentContractCount)
                    $("td[name=license]").text("("+newData.hubContractCount +"/"+ newData.agentContractCount+")")
                    $("td[name=projectStatus]").val(newData.projectStatus)
                    $("td[name=teamId]").val(newData.teamId)
                    $("td[name=managerName]").val(responseMessage.projectUserData)
                    $('#completeModal').modal('show')

                }else if(data.responseCode == "ERROR"){
                    $('#warnModalContent').text(data.responseMessage)
                    $('#warnModal').modal('show')
                }
              }
            });
            $('#detailModBtn').css("display",'none');
        }
    }
     //프로젝트 삭제
    function deleteProject(e){
        $("#warnModal").modal('show');
        $("button[name=confirmBtn]").attr('onClick',"projectConfirmDel('"+ e+"')")
        $("#warnModalContent").text("삭제하시겠습니까?")
    }

    function projectConfirmDel(e){
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/project",
            type : 'DELETE',
            data : "projectId="+e,
            success :function( data ) {
             if(data.responseCode == "SUCCESS"){
                 $('#completeModalContent').text('삭제되었습니다.')
                 $('#completeModal').modal('show')
                 $('#goProjectList').trigger("click")
             }else if(data.responseCode == "ERROR"){
                 $('#warnModalContent').text(data.responseMessage)
                 $('#warnModal').modal('show')
             }
            }
        });
    }
</script>
</html>
