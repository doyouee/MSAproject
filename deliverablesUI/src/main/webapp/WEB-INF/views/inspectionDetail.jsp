<%@ page import="java.util.Map" %>
<%@ page import="org.springframework.web.servlet.ModelAndView" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<head>
    <link rel="stylesheet" href="css/fileAdd.css">
</head>
<body>

<div>
    <section class="card">
        <header class="card-header">
            <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-folder-check"></i></span>산출물 검수</h2>
            <div>
                <div>
                    <p class="icon-tag" style="font-size: 16px; color: black; padding: 15px;"> 프로젝트 명 : ${projectJson[0].projectName}</p>
                </div>
            </div>
            <div class="btn-container" style="margin: 70px;">
                <button id="fileDownButton" type="button" class="btn " style="margin-right: 10px" onclick="deliverablesDownload('${projectJson[0].projectId}', '${projectJson[0].projectName}')">산출물 다운로드</button>
                <button id="inspectionRejectButton" type="button" class="btn btn-primary" style="margin-right: 10px">반려</button>
                <button id="inspectionOkButton" type="button" class="btn btn-primary">검수완료</button>
            </div>
            <div style="display: flex">
                <c:if test="${projectJson[0].inspectionRejectionReason!=null}">
                    <br>&emsp;&emsp;&emsp;<p class="icon-approve" style="align-items: center; color: #ed3137; font-size: 13px;"> 이전 반려 사유  <strong>:</strong> ${projectJson[0].inspectionRejectionReason}  </p>
                </c:if>
            </div>
        </header>
        <div class="ct-header">
            <c:import url="/WEB-INF/views/deliverablesRegBody.jsp">
                <c:param name="pageType" value="inspection"></c:param>
            </c:import>
        </div>
    </section>
    <div class="ct-content">
    <hr style="height: 0.3px; width:100%; color: #efefef">
        <div class="underTable">
            <header class="card-header">
                <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-folder-check"></i></span>준필수 파일 사유 리스트</h2>
            </header>
            <div class="card-body" >
                <div class="ct-content">
                    <div class="ct-content">
                        <div class="table-responsive">
                            <table class="table" style="border-right: 1px solid #f0f1f6; border-left: 1px solid #f0f1f6;">
                                <colgroup>
                                    <col span="1" style="width: 10%">
                                    <col span="2" style="width: 15%">
                                    <col span="1" style="width: 35%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th scope="col">번호</th>
                                    <th scope="col">파일 종류</th>
                                    <th scope="col">파일 명</th>
                                    <th scope="col">사유</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:if test="${fn:length(deliverablesReasonList) == 0}">
                                    <tr><td></td><td></td><td>등록된 사유가 없습니다.</td><td></td></tr>
                                </c:if>
                                <c:forEach var="d" items="${deliverablesReasonList}" varStatus="index">
                                    <tr>
                                        <td style="border-right: 1px solid #f0f1f6;">${index.count}</td>
                                        <td style="border-right: 1px solid #f0f1f6;">준필수 파일</td>
                                        <td style="border-right: 1px solid #f0f1f6;">${d.deliverablesName}</td>
                                        <td style="border-right: 1px solid #f0f1f6;">${d.notRegistrationReason}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 검수 반려 사유 (팝업) -->
<div id="inspectionRejectReason" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-dialog-alert">
        <div class="modal-content" style="width: 350px;">
            <div class="modal-header">
                <h2 class="modal-title">반려 사유</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i class="icon-close"></i></button>
            </div>
            <div class="modal-body modal-body-ct">
                <div class="ct-content">
                    <div class="table-responsive">
                        <textarea id="rejectReason" style="width: 300px;height: 190px;border: 1px solid gray;border-radius: 5px;margin: 10px 0 0 0;" placeholder=" 반려 사유를 입력하세요."></textarea>
                    </div>
                </div>
                <span id="validation" class="icon-x" style="display: none; color: red"> 반려 사유를 입력하세요.</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button id="rejectButton" type="button" class="btn btn-primary">반려</button>
            </div>
        </div>
    </div>
</div>
<form id="inspectionSearch" method="get">
    <input type="hidden" id= "inspectionSearchVal" name="inspectionSearchVal" />
</form>

<script>


    $(document).ready(function (e) {
        $('#tree_hr').css('pointer-events', 'auto');

        $("#completeModalButton").click(function (e) {
            if('${search}' != ''){
                var search = JSON.parse('${search}')[0]
                $("#inspectionSearchVal").val(JSON.stringify(search));
            }
            $("#inspectionSearch")[0].action = "/inspection";
            $("#inspectionSearch").submit();
        });

        $("#warnModalButton").click(function (e) {
            updateInspection('check');
        });

        $(function (){
            $('#inspectionOkButton').click(function (){
                $('#warnModalContent').text('검수를 완료하시겠습니까?');
                $("#warnModal").modal();
            });
        })

        $(function (){
            $('#inspectionRejectButton').click(function (){
                $("#rejectReason").css("border", "1px solid black");
                $("#validation").hide();
                $("#inspectionRejectReason").modal();
            });
        })


        $("#rejectButton").click(function (e) {
            if($("#rejectReason").val() == ''){
                $("#rejectReason").css("border", "1px solid red");
                $("#validation").show();
            } else{
                updateInspection('reject');
            }
        });
        $('.layerPopup')[0].style.display='none';
    });


    function updateInspection(e){
        $('.layerPopup')[0].style.display='block';
        var projectId = "${projectId}";
        var inspectionUserEmail = "${userInfo.userEmail}";
        var inspectionUserName = "${userInfo.userName}";
        var inspectionRejectionReason = $("#rejectReason").val();
        var inspectionStatus = "";
        var content = "";
        switch (e) {
            case 'reject':
                inspectionStatus = "3";
                content = "검수가 반려되었습니다.";
            break;
            case 'check':
                inspectionStatus = "2";
                content = "검수가 완료되었습니다.";
            break;
        }
        $.ajax({
            url : "/inspection",
            type: 'PUT',
            dataType: "json",
            data : {
                "projectId": projectId,
                "inspectionStatus": inspectionStatus,
                "inspectionUserEmail": inspectionUserEmail,
                "inspectionUserName": inspectionUserName,
                "inspectionRejectionReason": inspectionRejectionReason
            },
            success: function(response){
                if (response.responseCode == 'SUCCESS') {
                    $("#inspectionRejectReason").modal('hide');
                    $('#completeModalContent').text(content);
                    $("#completeModal").modal();
                }
            }
        });
    }

</script>