<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<section class="card">
    <header class="card-header">
        <h2 class="card-title">
            <span class="i-rounded bg-danger"><i class="icon-ul"></i></span>조회
        </h2>
        <div class="btn-container">
            <c:if test="${authorityId != 4}">
                <button type="button" href="#projectAddMod" name="projectAdd" data-toggle="modal" class="btn btn-primary">등록</button>
            </c:if>
        </div>
    </header>
    <c:import url="/WEB-INF/views/projectList.jsp"/>
</section>

<c:import url="/WEB-INF/views/projectUpdate.jsp">
    <c:param name="userAllList" value="${userAllList}"/>
    <c:param name="commonStatus" value="${commonStatus}"/>
    <c:param name="commonTeam" value="${commonTeam}"/>
</c:import>

<!-- 프로젝트 Form -->
<form id="projectForm" method="get">
    <input type="hidden" id="projectId" name="projectId"/>
    <input type="hidden" id= "searchVal" name="searchVal" />
    <input type="hidden" id= "pageNum" name="pageNum" />
</form>

<script>
	$(document).ready(function () {
        if('${search}' != ''){
            var search = JSON.parse('${search}')
            $.each(search,function(key,value) {
                $("#"+key+"Input").val(value);
            });

        }
		$("#downloadModalButton").click(function (e) {
			zipFileDownload();
		});
		
		$('#projectForm').submit(function() {
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


	function projectDetail(projectIdValue,activePage) {
		$("#projectId").val(projectIdValue);
		var data = { "projectCode" : $("#projectCodeInput").val().trim(),
		             "projectName" : $("#projectNameInput").val().trim(),
		             "teamName" : $("#teamNameInput").val().trim() }
        $("#searchVal").val(JSON.stringify(data));
        $("#pageNum").val(activePage);
		$("#projectForm")[0].action = "/projectDetail";
		$("#projectForm").submit();
	}

	function nullCheck(){
        let col = ["projectName", "customerName", "projectCode","hubContractCount","agentContractCount"]
        var state = "true";
        col.forEach(function(name) {
            if($("#"+name).val()  == '' ||  $("#"+name).val()  === undefined || $("#"+name).val() == null){
                $("#hubContractCountValidation").html("&nbsp;HUB 수량을 입력해주세요")
                $("#agentContractCountValidation").html("&nbsp;Agent 수량을 입력해주세요");
               $("#"+name+"Validation").css("display", "unset")
               state = "false";
            }else{
                $("#"+name+"Validation").css("display", "none")
            }
        });
        if ( $("#managerName").attr("data-value") === undefined || $("#managerName").attr("data-value") == "null" || $("#managerName").attr("data-value") == "[]" ){
            $("#managerNameValidation").css("display", "unset")
            state = "false";
        }
        if($("#hubContractCount").val().length > 3){
            $("#hubContractCountValidation").html("&nbsp;HUB 수량은 1~999의 값만 입력해주세요.");
            $("#hubContractCountValidation").css("display", "unset");
            state = "false";
        }
        if($("#agentContractCount").val().length > 3){
            $("#agentContractCountValidation").html("&nbsp;Agent 수량은 1~999의 값만 입력해주세요.");
            $("#agentContractCountValidation").css("display", "unset");
            state = "false";
        }
        return state;
	}

    // 프로젝트 추가
    let selectValue = document.querySelector("select[name=teamId]").options;
    $("button[name=projectAdd]").on('click',function () {
         if('${authorityId}' == '3'){
            setDefaultManager('${myUserName}' + '/' + '${myUserEmail}');
        }
        $('#modBtn').css("display","none")
	    $('#addBtn').css("display","unset")
	    $('td[name=addProject]').css('display','');
	    $('td[name=modProject]').css('display','none');
        $('#projectAddModTitle').text("프로젝트 등록")
        $('#managerAddTitle').text("담당자 추가")
        $('#managerBtn').text("담당자 추가")
        $('#managerSubmitBtn').text("추가")
        for (let i=0; i<selectValue.length; i++) {
            if (selectValue[i].value == '${teamId}') selectValue[i].selected = true;
        }
    })
    
	function insertProject() {
        if(nullCheck() == "true"){
            let data = JSON.parse($("#managerName").attr("data-value"))
            let insertData = {
                projectName : $("#projectName").val(),
                customerName : $("#customerName").val(),
                projectCode : $("#projectCode").val(),
                hubContractCount : $("#hubContractCount").val(),
                agentContractCount : $("#agentContractCount").val(),
                projectStatus : $("#projectStatus").val(),
                teamId : $("#teamId").val(),
                manager : data
            }

            $('.layerPopup')[0].style.display='block';
            $.ajax({
              url: "/project",
              type : 'POST',
              data : insertData,
              success :function( data ) {
                if(data.responseCode == "SUCCESS" ){
                	var responseMessage = data.responseMessage;
                    $("#projectAddMod").modal('hide')
                    $('#completeModalContent').text('등록되었습니다.')
                    $('#completeModal').modal('show')
                    projectList(1)
                }else if(data.responseCode == "ERROR" ){
                   nullCheck()
                   if(data.responseMessage.type == "code"){
                      $("#projectNameDuplication").css("display", "none")
                      $("#projectCodeDuplication").css("display", "unset")
                   }else if(data.responseMessage.type == "name"){
                      $("#projectCodeDuplication").css("display", "none")
                      $("#projectNameDuplication").text(data.responseMessage.message)
                      $("#projectNameDuplication").css("display", "unset")
                   }
                }
              }
            });
        }
	}
    
    // 프로젝트 수정
    function setModData(e){
        $('.layerPopup')[0].style.display='block';
        $('#managerAddTitle').text("담당자 수정")
        $('#managerBtn').text("담당자 수정")
        $('#managerSubmitBtn').text("수정")
        $.ajax({
            url: "/oneProject",
            type : 'GET',
            data : "projectId="+$(e).attr("value"),
            success :function( data ) {
             if(data.responseCode == "SUCCESS"){
                var responseMessage = data.responseMessage[0]
                for(var key in responseMessage) {
                	if( key == "manager"){
                		if(responseMessage[key] != null ){
                            setDefaultManager(responseMessage[key]);
                		}else{
                		    $("#managerName").css("display","none");
                            $("#managerName").attr("data-value",null);
                		}
                	}else{
                		$("#"+key +"Mod").text(responseMessage[key]);
                		$("#"+key +"Mod").val(responseMessage[key]);
                		$("#"+key).val(responseMessage[key]);
                		$("#"+key+"Mod").css("display","");
                    }
                }
                $('#modBtn').css("display","unset")
                $('#addBtn').css("display","none")
                $('#projectAddModTitle').text("프로젝트 수정")
                $('td[name=addProject]').css('display','none');
                $('td[name=modProject]').css('display','');

             }else if(data.responseCode == "ERROR" ){
                 $('#warnModalContent').text(data.responseMessage)
                 $('#warnModal').modal('show')
             }
            }
        });

        if( $("#managerName").attr("data-value") != "null" ){
             $("#managerName").css("display","unset")
        }
    }


    function setDefaultManager(responseMessageKey){
        let managerData = responseMessageKey.split("|")
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
    }
    
	function updateProject() {
	    var page = '${pageNum}';
        if(nullCheck() == "true" && $("#projectStatusValidation").css("display") == "none" ){
            $('.layerPopup')[0].style.display='block';
            let data = JSON.parse($("#managerName").attr("data-value"))
            let updateData = {
                projectId : $("#projectId").val(),
                customerName : $("#customerName").val(),
                hubContractCount : $("#hubContractCount").val(),
                agentContractCount : $("#agentContractCount").val(),
                projectStatus : $("#projectStatus").val(),
                teamId : $("#teamId").val(),
                manager : data,
                pageNum : page
            };
            $.ajax({
              url: "/project",
              type : 'PUT',
              data : updateData,
              success :function( data ) {
                if(data.responseCode == "SUCCESS" ){
                	var responseMessage = data.responseMessage;
                    $("#projectAddMod").modal('hide')
                    $('#completeModalContent').text('수정되었습니다.')
                    $('#completeModal').modal('show')
                    projectList($("#activePage").val())
                }else if(data.responseCode == "ERROR" ){
                   nullCheck()
                }
              }
            });
        }
	}

    //프로젝트 삭제
    function deleteProject(e){
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
             if(data.responseCode == "SUCCESS" ){
            	 var responseMessage = data.responseMessage;
                 $('#completeModalContent').text('삭제되었습니다.')
                 $('#completeModal').modal('show')
                 projectList(1)
             }else if(data.responseCode == "ERROR" ){
                 $('#warnModalContent').text(data.responseMessage)
                 $('#warnModal').modal('show')
             }
            }
        });
    }

	$('#projectAddMod').on('hidden.bs.modal', function () {
       let col = ["projectName", "customerName", "projectCode","hubContractCount","agentContractCount"]
       col.forEach(function(name) {
          $("#"+name+"Validation").css("display", "none")
          $("#"+name+"Duplication").css("display", "none")
       });
        $("#projectName").val('');
        $("#customerName").val('');
        $("#projectCode").val('');
        $("#hubContractCount").val('');
        $("#agentContractCount").val('');
        $("#managerName").attr("data-value" ,'[]')
        $("#managerName").text('')
        $("#managerName").css('display', 'none')
        $('#addBtn').css("display","none")
        $("#managerNameValidation").css("display", "none")
        $("#projectStatusValidation").css("display","none")

    });
</script>
