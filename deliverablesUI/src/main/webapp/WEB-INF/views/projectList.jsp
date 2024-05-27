<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="projectListType" value="${projectListType}" />
<div class="ct-header">
    <button type="button" class="btn-filter d-xl-none" data-toggle="collapse" data-target="#collapse-filter"
                    aria-expanded="true">검색 필터<i class="icon-down"></i></button>
    <div id="collapse-filter" class="collapse collapse-filter show">
        <div class="filter no-gutters">
            <div class="col" style="max-width: 30%;">
                <label class="form-control-label">
                    <b class="control-label">프로젝트 코드</b>
                    <input type="text" class="form-control" id="projectCodeInput" placeholder="프로젝트 코드를 입력해주세요.">
                </label>
            </div>
            <div class="col" style="max-width: 30%;">
                <label class="form-control-label">
                    <b class="control-label">프로젝트명</b>
                    <input type="text" class="form-control" id="projectNameInput" placeholder="프로젝트명을 입력해주세요.">
                </label>
            </div>
            <div class="col" style="max-width: 30%;">
                <label class="form-control-label">
                    <b class="control-label">부서명</b>
                    <input type="text" class="form-control" id="teamNameInput" placeholder="부서명을 입력해주세요">
                </label>
            </div>
            <div class="col-auto" style="min-width: 10%;">
                <button type="button" class="btn btn-m"  id="searchBtn" onclick="search()"><span class="hide"><i class="icon-srch"></i>조회</span></button>
            </div>
        </div>
    </div>
</div>

<div class="ct-content">
    <div class="table-responsive">
        <table class="table" style="table-layout: fixed;">
            <colgroup>
                <col span="1" style="width: 3%">
                <col span="1" style="width: 10%">
                <col span="1" style="width: 20%">
                <col span="2" style="width: 11%">
                <col span="3" style="width: 7%">
                <col span="1" style="width: 8%">
            </colgroup>
            <thead>
	            <tr style="text-align: center;">
	                <th  style="width:3%">번호</th>
	                <th style="width: 5%">프로젝트 코드</th>
	                <th style="width: 32%">프로젝트 명</th>
	                <th style="width: 13%">고객사 명</th>
	                <th style="width: 8%">부서명</th>
	                <th style="width: 5%">라이선스 수량</th>
	                <th style="width: 5%">프로젝트 상태</th>
	                <th  style="width: 5%">검수 상태</th>
	                <th style="width: 5%">관리</th>
	            </tr>
            </thead>
            <tbody id = "projectTable">
                <c:choose>
                    <c:when test="${projectList==null}">
                        <tr><td></td><td></td><td>검수 요청 상태의 프로젝트가 없습니다.</td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${fn:length(projectList) == 0 }">
                                <tr><td></td><td></td><td></td><td> 등록된 프로젝트가 없습니다.</td><td></td><td></td><td></td><td></td></tr>
                            </c:when>
                        <c:otherwise>
                            <c:forEach var="project" items="${projectList}" varStatus="index">
                                    <tr>
                                        <td>${index.count}</td>
                                        <td>${project.projectCode}</td>
                                        <td>${project.projectName}</td>
                                        <td>${project.customerName}</td>
                                        <td>${project.teamName}</td>
                                        <td>(${project.hubContractCount} / ${project.agentContractCount})</td>
                                        <td>${project.projectStatusName}</td>
                                        <td>${project.inspectionStatusName}</td>
                                        <td style="display: flex; align-items: center; justify-content: center; border-left: none; border-top: none">
                                            <c:choose>
                                                <c:when test="${projectListType == 'project'}">
                                                    <button class="btn btn-icon" style="margin: 1px;" onclick="projectDetail('${project.projectId}','1')"><i class="icon-srch"></i></button>
                                                    <c:if test="${authorityId != 4}">
                                                        <button href="#projectAddMod" class="btn btn-icon" data-toggle="modal" style="margin: 1px;" name="projectMod" onclick="setModData(this)" value="${project.projectId}" data-value="${project}"><i class="icon-edit"></i></button>
                                                        <button href="#warnModal" class="btn btn-icon" data-toggle="modal"  onclick="deleteProject('${project.projectId}');"   style="margin: 1px;"><i class="icon-delete"></i></button>
                                                    </c:if>
                                                    <c:choose>
                                                        <c:when test="${project.inspectionStatus eq '2'}">
                                                            <button class="btn btn-icon" data-toggle="modal" style="margin: 1px;" onclick="deliverablesDownload('${project.projectId}', '${project.projectName}')"><img src="download icon.png" width="18"></button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button class="btn btn-icon" style="margin: 1px; filter: brightness(93%)" disabled><img src="download icon.png" width="18"></button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <button onclick="sndData('${project.projectId}')" type="button" class="btn btn-primary" style="max-height: 28px; display: flex; align-items: center; justify-content: center;">검수</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    <div style="margin-top:5px">
        <a style="margin: 0.5%;font-weight: bold;" id="allSizePrint">총  ${projectAllListSize} 건</a>
    </div>
    <ul class="pagination" id="pagination" ></ul>
</div>

<form id="inspectionForm" method="get">
    <input type="hidden" id="inspectionProjectId" name="inspectionProjectId"/>
    <input type="hidden" id= "inspectionSearchVal" name="inspectionSearchVal" />
</form>

<script>
    $(document).ready(function () {
        var size = ${projectAllListSize};
        var originPage = ${pageNum};
        $("#activePage").val(originPage);
        setPagination(size, originPage);
        setEndSize(originPage,size);
        $('.layerPopup')[0].style.display='none';
    })
	function movePage(move,activePage,nextPage,setPage,prePage){
	    setSection(move,activePage,prePage,nextPage,setPage)
	    if(move == 0 ){
	        projectList(activePage);
            $("#activePage").val(activePage);
	    }else if(move == 1){
	        projectList(nextPage);
            $("#activePage").val(nextPage);
	    }else if(move == -1){
	        projectList(prePage);
            $("#activePage").val(prePage);
	    }
	}
	function enterkey() {
    	if (window.event.keyCode == 13) {
        	projectList(1);
        }
    }
    document.addEventListener("keyup", function(event) {
      if(event.keyCode == '13'){
        if($("#updateProjectUser").hasClass("show")){
            $("#userSearchBtn").click();
        }else{
            $("#searchBtn").click();
        }
      }
    });

	function search() {
		projectList(1);
	}
    function sndData(projectIdSet){
        $("#inspectionProjectId").val(projectIdSet);
        $("#inspectionForm")[0].action="/inspectionDetail";
        $("#inspectionForm")[0].submit();
    }
	function projectList(active){
		$('.layerPopup')[0].style.display='block';
	    var type = "${projectListType}";
	    var url = "";
	    if(type == 'project'){
	        url = "/projectList";
	    }else{
	        url = "/inspectionList";
	    }
	
		$.ajax({
			url : url,
			type : 'GET',
			data : {
				"projectCode" : $("#projectCodeInput").val().trim(),
				"projectName" : $("#projectNameInput").val().trim(),
				"teamName" : $("#teamNameInput").val().trim(),
				"pageNum" : active
			},
			success : function(data) {
				if(data.responseCode == "SUCCESS" ){
					var responseMessage = data.responseMessage;
                    $("#inspectionSearchVal").val(responseMessage.search);
	                if (responseMessage.projectAllListSize == 0) {
	                    setEmpty();
	                    setPagination(responseMessage.projectAllListSize, active);
	                } else {
	                    setTbody(responseMessage.projectList, active, responseMessage.projectListType, responseMessage.projectAllListSize);
	                    setSection(0,1,1,2,responseMessage.projectAllListSize);
	                    setPagination(responseMessage.projectAllListSize, active);
	                }
				}
			}
		});
	}
	
	function setEmpty (){
	    var projectTable ="";
	    $("#projectTable").empty();
	    $('#allSizePrint').text("총 0 건 ")
	    projectTable +="<tr><td></td><td></td><td></td><td>검색 결과가 없습니다</td><td></td><td></td><td></td><td></td><td></td></tr>"
	    $("#projectTable").append(projectTable);
	}
	
	function setTbody (data, activePage, projectListType, size){
	    var projectTable ="";
	    $("#projectTable").empty();

        setEndSize(activePage, size)
	    data.forEach (function (el, index) {
	        var idx = (activePage - 1) * 15 + index+1
	        projectTable +="<tr><td >"+idx+"</td>"
	        projectTable +="<td  >"+el.projectCode+"</td>"
	        projectTable +="<td >"+el.projectName+"</td>"
	        projectTable +="<td >"+el.customerName+"</td>"
	        projectTable +="<td >"+el.teamName+"</td>"
	        projectTable +="<td >("+el.hubContractCount+" / "+el.agentContractCount+")</td>"
	        projectTable +="<td  >"+el.projectStatusName+"</td>"
	        projectTable +="<td  >"+el.inspectionStatusName+"</td>"
	        projectTable +="<td  style='display: flex; align-items: center; justify-content: center; border-left: none; border-top: none'>"
	        if(projectListType == "project"){
		       	projectTable +="<button class='btn btn-icon' style='margin: 1px;' onclick=\"projectDetail('"+el.projectId+"' , '"+activePage+"')\"><i class='icon-srch'></i></button>"
                if ('${authorityId}' != '4') {
                    projectTable +="<button href='#projectAddMod' class='btn btn-icon' data-toggle='modal' style='margin: 1px;' name='projectMod' onclick='setModData(this)' value='"+
                    				el.projectId +"' data-value='"+JSON.stringify(el).split('\"').join("").split(':').join("=")+"'><i class='icon-edit'></i></button>"
                    projectTable +="<button href='#warnModal' class='btn btn-icon' data-toggle='modal' onclick=\"deleteProject('"+el.projectId+"')\"  style='margin: 1px;'><i class='icon-delete'></i></button>"
                }
                if(el.inspectionStatus == '2'){
                	projectTable +="<button class='btn btn-icon' data-toggle='modal' style='margin: 1px;' onclick=\"deliverablesDownload('"+el.projectId+"', '"+el.projectName+"')\"><img src='download icon.png' width='18'></button>"
                }else{
                	projectTable +="<button class='btn btn-icon' style='margin: 1px; filter: brightness(93%);' disabled> <img src='download icon.png' width='18'></button>";
                }
	        }else{
                projectTable +="<button onclick=\"sndData('"+el.projectId+"')\" type='button' class='btn btn-primary' style='max-height: 28px; display: flex; align-items: center; justify-content: center;'>검수</button>"
	        }
	       	projectTable +="</td></tr>"
	    });
	    $("#projectTable").append(projectTable);
	}
</script>