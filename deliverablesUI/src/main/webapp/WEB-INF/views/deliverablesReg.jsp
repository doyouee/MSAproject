<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<link rel="stylesheet" href="css/fileAdd.css">
<style>
    .modal-td-scroll{
        max-height: 30vh;
        overflow-x: hidden;
        overflow-y: scroll;
    }
</style>

<!-- 산출물 목록 프로그램 트리 영역 + 파일 설명 -->
<section class="card">
    <header class="card-header">
        <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-folder-check"></i></span>산출물 등록</h2>
        <div class="select_content">
            <form class="project_select">
                <select id="projectSelect"  class="form-control" onchange="changeProjectId(this.options[this.selectedIndex].value)">
                    <option name="프로젝트를 선택하세요">프로젝트를 선택하세요</option>
                </select>
            </form>
        </div>
    </header>
    <div class="ct-content">
        <c:import url="/WEB-INF/views/deliverablesRegBody.jsp">
            <c:param name="pageType" value="deliverablesReg"></c:param>
        </c:import>
    </div>
        <div class="ct-footer" style="float: right; margin-right: 3vw;">
            <a id="btn_blacks" class="btn mr-2 mb-2" data-toggle="modal" onclick="inspectionReq()" style="float: right; margin-right: 3vw;">검수요청</a>
        </div>
</section>

<!-- 산출물 등록 -->
<div id="modal_file_register" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">산출물 등록</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                class="icon-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <form class="file_type">
                        <div>
                            <input type="radio" name="type" value="type_file" checked>
                            <label for="type_file">파일</label></br>
                        </div>
                        <div>
                            <input type="radio" name="type" value="type_text">
                            <label for="type_text">텍스트</label></br>
                        </div>
                    </form>
                </div>
                <div class="form-group" style="margin-bottom: 5px">
                    <label>제목</label>
                    <input id="titleValiInput" class="input_title" type="text" oninput="inputCheck(this)" required>
                </div>
                <span id="titleValidation" class="icon-x" style="display: none; color: red; margin-left: 45px; font-size: 12px"> 제목을 입력하세요.</span>
                <span id="specialValidation" class="icon-x" style="display: none; color: red; margin-left: 45px; font-size: 12px"> 특수문자는 입력하실수 없습니다</span>
                <div class="form-group" style="display: flex; margin-top: 5px">
                    <label>파일</label>
                    <form action="multi-file" method="post" enctype="multipart/form-data" style="width: 100%;">
                        <div id="fileVali" class="upload_name" style="height: 100px; overflow-y: scroll;" onchange="setValidation(this)"></div>
                        <label class="search_button" for="file">찾기</label>
                        <input type="file" id="file" class="uploadFileBtn" multiple />
                    </form>
                </div>
                <span id="fileByteValidation" class="icon-x" style="display: none; color: red; margin-left: 5px;margin-top: 5px;font-size: 12px"> 총 100MB 미만의 파일만 등록 가능합니다.</span>
                <span id="fileValidation" class="icon-x" style="display: none; color: red; margin-left: 45px; font-size: 12px"> 파일을 추가하세요.</span>
                <div class="form-group">
                    <label>내용</label>
                    <textarea id="contentValiInput" class="input_content" oninput="inputCheck(this)" ></textarea>
                </div>
                <span id="contentValidation" class="icon-x" style="display: none; color: red; margin-left: 5px; font-size: 12px"> 내용을 입력하세요.</span>
                <span id="contentByteValidation" class="icon-x" style="display: none; color: red; margin-left: 5px; font-size: 12px">글자수가 초과되었습니다 (8000byte 이하) </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button id="regis_modal_btn" value="" onclick="onFileUpload(this.value)" type="button" class="btn btn-primary" <%--data-dismiss="modal"--%> data-toggle="modal">등록</button>
            </div>
        </div>
    </div>
</div>

<!-- 검수 요청 팝업 -->
<div id="modal_inspection" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content" style="max-height: 600px;">
            <div class="modal-header">
                <h2 class="modal-title">검수 요청</h2>
                <div style="display: flex;">
                    <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i class="icon-close"></i></button>
                </div>
            </div>
            <div class="modal-body-ct">
                <div class="modal-body">
                    <div class="modal-header" style="margin:0px; align-items: end;">
                        <div style="margin-bottom: 0px;">
                            <p id="explain1" style="display: none">* 필수 파일이 누락되어 있습니다. 파일을 확인해주세요.</p>
                            <p id="explain2" style="display: none">* 준필수 파일이 누락되어 있습니다. 준필수 파일 없이 검수 요청 시, 누락 사유 작성 후 진행해주세요.</p>
                            </div>
                        <div style="display: flex;">
                            <button style="justify-content: space-between" onclick="tmpSave('tmp')" type="button" class="btn btn-primary" >임시저장</button>
                        </div>
                    </div>
                    <div class="ct-content modal-td-scroll">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr class="ins_table_tr">
                                    <th>번호</th>
                                    <th>파일 종류</th>
                                    <th>파일명</th>
                                    <th style="text-align: -webkit-center;">사유</th>
                                    </tr>
                                </thead>
                                <tbody class="inspection_tbody"></tbody>
                            </table>
                        </div>
                        <ul class="pagination" id="pagination" ></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <span id="requestBefore">
                    <button id="inspectionButton" onclick="InspectionButtonClick()" id="inspectionButton" class="btn btn-primary" >검수요청</button>
                </span>
            </div>
        </div>
    </div>
</div>

<!-- 삭제 전_alert -->
<div id="delete_alert" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-dialog-alert">
        <div class="modal-content">
            <div class="modal-body">
                <i class="iconb-warn"></i>
                <p class="delete-text"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <a id="delete_final_btn" type="button" class="btn btn-primary" data-dismiss="modal"
                data-toggle="modal">삭제</a>
            </div>
        </div>
    </div>
</div>
<span id="spanStrWidth" style="visibility:hidden; position:absolute; top:-10000; font-size:14pt;"></span>

<!-- js -->
<script type="text/javascript">
    var saveType = "inspection";
    var nowProjectId ="";
    var nowDeliverablesID;
    $(document).ready(function (e) {
    	$('#projectSelect').on('mouseover', function(){
            optionResize();
         });
        $("#warnModalButton").click(function (e) {
            completeInspection(e);
        });

        $("#completeModalButton").click(function (e) {
            if(saveType == 'inspection'){
                 location.replace("deliverablesReg");
            }
        });
    });

    $('.left_content').click(function(){
        if(nowProjectId === undefined) {
            $('#failModalContent').text('프로젝트를 선택해주세요.');
            $("#failModal").modal();
        }
    })

    function textLength(text){
        $("#spanStrWidth").text(text);
        return $("#spanStrWidth").innerWidth();
    }

    function optionResize(){
    	var selectSize = $('#projectSelect').outerWidth(true)+($('#projectSelect').outerWidth(true)/4);
    	var projectJson = ${projectJson};
    	var option = "<option name='프로젝트를 선택하세요'>프로젝트를 선택하세요</option>";
    	$('#projectSelect').empty();
    	
        var ellipsisSize = textLength("...");
    	projectJson.forEach((projectInfo) => {
    		if(projectInfo.inspectionStatus == '0' || projectInfo.inspectionStatus == '3'){
	    		var originName = projectInfo.projectName;
		        var textSize = textLength(originName);
		        var resizeProjectName = originName;
		        if(selectSize <= textSize){
		            var newText = originName;
		            while(selectSize <= (textSize+ellipsisSize)){
		                newText = newText.substring(0, newText.length-1);
		                textSize= textLength(newText)
		            }
		            resizeProjectName = newText + "...";
		        }
	    		option += "<option value='" + projectInfo.projectId + "' name='" + originName + "'>" + resizeProjectName + "</option>";
    		}
        });
        $('#projectSelect').append(option);
        if(nowProjectId != "" ){
            $('#projectSelect').val(nowProjectId);
        }
    }

    function inputCheck(e){
        if($(e).val() != ""){
            if($(e).attr("class") == "input_title"){
                const regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g;
                if(regExp.test($(e).val())){
                    $('#titleValiInput').css('border', '1px solid red');
                    $('#specialValidation').css('display', 'block');
                }else{
                    $('#titleValiInput').css('border', '1px solid black');
                    $('#titleValidation').css('display', 'none');
                    $('#specialValidation').css('display', 'none');
                }
            }else if($(e).attr("class") == "input_content"){
                if(getByte($(e).val()) > 55054){
                    $("#contentByteValidation").css("display", "unset")
                }else{
                    $("#contentByteValidation").css("display", "none")
                }
                $('#contentValiInput').css('border', '1px solid black');
                $('#contentValidation').css('display', 'none');
            }
        }
    }

    function changeProjectId(id) {
        $('#right_content').css("display", "none");
        nowProjectId = id;
        $(".left_content").css("display", "flex");
        $("#div_hr").css("display", "block");
        $(".ct-header").css("display", "block");
        $('.project_select > select').css('border', '1px solid black');
        $('#tree_hr').css('pointer-events', 'auto');

        $('.line').css("display", "block");
        $('.roots_open').attr('class','button level0 switch roots_open');
        $('.center_open').attr('class','button level0 switch center_open');
        $('.bottom_open').attr('class','button level0 switch bottom_open');
        $(".ico_open").attr('class','button ico_open');
    }

    $('input[name="type"]').change(function(){
        $("#titleValiInput").css("border", "1px solid black");
        $("#titleValidation").hide();
        $("#contentValiInput").css("border", "1px solid black");
        $("#contentValidation").hide();
        $("#fileVali").css("border", "1px solid black");
        $("#fileValidation").hide();
        $("#fileByteValidation").hide();
        $("#contentByteValidation").hide();
    });

    $('#modal_file_register').on('show.bs.modal', function(e) {
        $("#titleValiInput").css("border", "1px solid black");
        $("#titleValidation").hide();
        $("#contentValiInput").css("border", "1px solid black");
        $("#contentValidation").hide();
        $("#fileVali").css("border", "1px solid black");
        $("#fileValidation").hide();
        $("#fileByteValidation").hide();
        $("#contentByteValidation").hide();
        $("#specialValidation").hide();
        $(".input_title").val("");
        $(".input_content").val("");
        $(".upload_name").empty();
        $("#file").val("");
        fileList2 = [];

    });

    // 파일 & 텍스트 등록
    function onFileUpload(id) {
        nowDeliverablesID = id;
        var contentType = $("input[type='radio']:checked").val();
        var deliverablesTitle = $('.input_title').val();
        var textContents = $('.input_content').val();

        if (contentType === 'type_text' && (deliverablesTitle == "" || textContents == "")) {
            if (deliverablesTitle == "") {
                $("#titleValiInput").css("border", "1px solid red");
                $("#titleValidation").show();
            }
            if (textContents == "" ) {
                $("#contentValiInput").css("border", "1px solid red");
                $("#contentValidation").show();
            }
        } else if(contentType === 'type_file' && (deliverablesTitle == "" || fileList2.length==0)) {
            if (deliverablesTitle == "") {
                $("#titleValiInput").css("border", "1px solid red");
                $("#titleValidation").show();
            }
            if (fileList2.length==0) {
                $("#fileVali").css("border", "1px solid red");
                $("#fileValidation").show();
            }
        } else {
            if (contentType === 'type_text' ) {
                if( $("#contentByteValidation").css("display") == "none" && $("#specialValidation").css('display') == "none"){
                    $('.layerPopup')[0].style.display='block';
                    $.ajax({
                        url: "/deliverablesRegPostText",
                        type: "POST",
                        data: {
                            "projectId": nowProjectId,
                            "deliverablesId": nowDeliverablesID,
                            "deliverablesTitle": deliverablesTitle,
                            "textContents": textContents
                        },
                        dataType: "JSON",
                        timeout: 180000,
                        success: function (response) {
                            $("#modal_file_register").modal('hide');
                            if (response.responseCode == "SUCCESS"){
                                var sendData2 = "projectId=" + nowProjectId + "&deliverablesId=" + nowDeliverablesID;
                                getFileList(sendData2);
                            }
                            $("#contentByteValidation").css("display", "none")
                        }
                    });
                }

            } else {
                var formData = new FormData();
                var tempFileList = [];
                var uploadFileName;
                var totalSize = 0;

                fileList2.forEach((file) => {
                    // 파일 데이터 저장
                    uploadFileName = file.name;
                    totalSize +=  file.size;
                    formData.append("fileList", file);

                });
                // 객체
                var dataVo = {
                    registrationId: null,
                    projectId: nowProjectId,
                    deliverablesId: nowDeliverablesID,
                    contentType: "0",
                    deliverablesTitle: deliverablesTitle,
                    fileName: null,
                    filePath: null,
                    registrationUserEmail: null,
                    registrationUserName: null
                };

                formData.append("registrationInfo", new Blob([JSON.stringify(dataVo)], { type: "application/json" }));
                if( totalSize <= 104857600  && $("#specialValidation").css('display') == "none"){
                    $('.layerPopup')[0].style.display='block';
                    $.ajax({
                        url: "/deliverablesRegPostFile",
                        type: "POST",
                        data: formData,
                        enctype: "multipart/form-data",
                        contentType: false,
                        processData: false,
                        timeout: 180000,
                        success: function (response) {
                            $("#modal_file_register").modal('hide');
                            if (response.responseCode == "ERROR") {
                                $('#failModalContent').text(response.responseMessage);
            	                $("#failModal").modal('show');
                            } else if (response.responseCode == "SUCCESS") {
                                var sendData = "projectId=" + nowProjectId + "&deliverablesId=" + nowDeliverablesID;
                                getFileList(sendData);
                                $(".input_title").val("");
                                $(".input_content").val("");
                                $(".upload_name").empty();
                                $('form').each(function() {
                                    this.reset();
                                });
                                $('#projectSelect').val(nowProjectId);
                            }
                        }
                    });
                }
            }
        }
    };


    /* 파일 찾기 뒤 제목 자동 입력 */
    var fileList2 = [];


    $("#file").on('change', function (e) {
        var fileList = [];  // 추가된 파일 리스트
        var fileNames = [];  // 추가된 파일 리스트의 이름 리스트
        var uploadFiles = Array.prototype.slice.call(e.target.files); // 파일선택창에서 선택한 파일들
        fileList2 = [];
        uploadFiles.forEach((uploadFile) => {
            fileList2.push(uploadFile);
        });
        for (var i = 0; i < e.currentTarget.files.length; i++) {
            var name = e.currentTarget.files[i].name;
            fileList.push(name);
            fileNames.push(name.substring(0, name.indexOf('.')));
        }
        $(".upload_name").empty();

        if(fileNames.length == 1){
            $(".input_title").val(fileNames[0]);
        }else{
            $(".input_title").val(fileNames[0] + " 외 " + (fileNames.length-1));
        }
        var totalSize = 0;
        fileList2.forEach((uploadFile) => {
            $("#fileValidation").css("display","none");
            $("#fileVali").css("border", "1px solid black");
            $("#titleValidation").css("display","none");
            $("#titleValiInput").css("border", "1px solid black");
            totalSize += uploadFile.size;
            $(".upload_name").append("<p>" + uploadFile.name + "</p>");

        });
        if( totalSize <= 104857600 ){
            $("#fileByteValidation").css("display","none")
        }else{
            $("#fileByteValidation").css("display","unset")
        }
    });

    // 파일 삭제
    function deleteFile(regisId) {
        var sendData = "registrationId=" + regisId;
        $('.layerPopup')[0].style.display='block';
         $.ajax({
            type: "DELETE",
            url: "/deliverablesReg",
            data: sendData,
            datatype: "JSON",
            success: function (response) {
                if(response.responseCode == "SUCCESS") {
                    var sendData2 = "projectId=" + nowProjectId + "&deliverablesId=" + nowDeliverablesID;
                    $('.layerPopup')[0].style.display='block';
                    $.ajax({
                        type: "GET",
                        url: "/fileList",
                        data: sendData2,
                        datatype: "JSON",
                        success: function (response) {
                            if (response.responseCode == "SUCCESS") {
                                var fileInfo = response.responseMessage;
                                rightContent(fileInfo);
                            }
                        }
                    });
                }
            }
        });
    }

    // 누락된 필수, 준필수 파일 리스트 가져오기
    function inspectionReq() {
        if (nowProjectId == null || nowProjectId == '프로젝트를 선택하세요') {
            $('#failModalContent').text('프로젝트를 선택해주세요.');
            $("#failModal").modal();
        } else {
             var sendData = "projectId=" + nowProjectId;
            $('.layerPopup')[0].style.display='block';
            $.ajax({
                type: "GET",
                url: "/inspectList",
                data: sendData,
                datatype: "JSON",
                success: function (response) {
                    if (response.responseCode == "SUCCESS") {
                        var fileInfo = response.responseMessage;
                        inspectionList(fileInfo);
                    }
                }
            });
        }
    }

    var arr0 = [];
    var arr1 = [];
    var arrAll;
    let deliverablesIdArr = [];
    let deliverablesNameArr = [];
    let emptyIds = "";

    // 누락된 필수, 준필수 파일 -> 리스트에 출력하기
    function inspectionList(fileInfo) {
        $('.inspection_tbody').empty();
        arr0 = [];
        arr1 = [];
        arrAll = [];
        deliverablesIdArr = [];
        deliverablesNameArr = [];
        var html = '';
        $("#explain1").hide();
        $("#explain2").hide();

        fileInfo.forEach(function(item, index) {
            if(item.requiredItems == '0') {
                arr0.push(item);
            } else if(item.requiredItems == '1') {
                arr1.push(item);
            }
        })
        arrAll = arr0.concat(arr1);
        if (arrAll.length > 0) {
            $('#modal_inspection').modal();
            arrAll.forEach(function(item, index) {
                html += '        <tr class="ins_table_tr">';
                html += '            <td>' + (index+1) + '</td>';
                if(item.requiredItems === '0') {
                    $("#explain1").show();
                    html += '            <td>필수 파일</td>';
                } else if(item.requiredItems == '1') {
                    $("#explain2").show();
                    html += '            <td>준필수 파일</td>';
                }
                html += '            <td>' + item.deliverablesName + '</td>';
                if(item.requiredItems === '1') {
                    if(item.notRegistrationReason == null){
                        emptyIds += item.deliverablesId + ",";
                        html += '            <td>';
                        html += '                <input id="reasonText'+(index+1)+'" class="fileExplain" type="text" placeholder="사유를 입력하세요." oninput="checkByte(this)" />';
                        html += '                <span style="color:red; font-size:12px;display:none" id="reasonText'+(index+1)+'Validation"  name="reasonTextValidation" class="icon-x">&nbsp;글자수가 초과되었습니다 (2000byte 이하)</span>';
                        html += '            </td>';
                    } else {
                        html += '<td><input id="reasonText'+(index+1)+'" class="fileExplain" type="text" value="'+ item.notRegistrationReason +'" oninput="checkByte(this)" >';
                        html += '                <span style="color:red; font-size:12px;display:none" id="reasonText'+(index+1)+'Validation" name="reasonTextValidation" class="icon-x">&nbsp;글자수가 초과되었습니다 (2000byte 이하)</span>';
                        html += '            </td>';
                    }
                } else {
                    html += '            <td></td>';
                }
                html += '        </tr>';
                deliverablesIdArr.push(item.deliverablesId);
                deliverablesNameArr.push(item.deliverablesName);
            })

            $(".inspection_tbody").append(html);
        } else {
            $("#modal_inspection").modal('hide');
            $('#warnModalContent').text('검수 요청을 보내시겠습니까?');
            $("#warnModal").modal('show');
        }

    }


    function tmpSave(data) {
    	if(deliverablesNameArr.length != 0){
    		let dataList = [];
    		let sizeConfirm = "true";
            for (var i = arr0.length+1; i <= arrAll.length; i++) {
                const reasonText = $('#reasonText'+i).val();
                if (emptyIds.indexOf(deliverablesIdArr[i-1]+",") < 0 || (reasonText!=null && reasonText!="")) {
                    let data = {
                        "projectId": nowProjectId,
                        "deliverablesId": deliverablesIdArr[i-1],
                        "contentType": "1",
                        "registrationUserEmail": "${userInfo.userEmail}",
                        "registrationUserName": "${userInfo.userName}",
                        "notRegistrationReason": reasonText,
                        "deliverablesTitle": deliverablesNameArr[i-1]
                    };
                    dataList.push(data);
                }
                if($('#reasonText'+i+"Validation").css('display') != "none"){
                    sizeConfirm ="false"
                }
            }
            if(sizeConfirm == "true"){
                $('.layerPopup')[0].style.display='block';
                $.ajax({
                    contentType:'application/json',
                    type: "POST",
                    url: "/deliverablesReason",
                    data: JSON.stringify(dataList),
                    datatype: "json",
                    success: function (response) {
                        if (response.responseCode == "SUCCESS") {
                            $("#modal_inspection").modal('hide')
                            emptyIds = "";
                            if (data == 'tmp') {
                                saveType = 'tmp';
                                $('#completeModalContent').html('저장되었습니다.<br> 검수 요청을 진행하시기 바랍니다.');
                                $("#completeModal").modal();
                            }
                        }
                    }
                });
            }
    	}
    }
    function zIndexChange(){
        $("#modal_inspection").css("z-index",5000)
    }

    function InspectionButtonClick() {
        tmpSave(null);
        let sizeConfirm = "true";
        for (var i = arr0.length+1; i <= arrAll.length; i++) {
            if($('#reasonText'+i+"Validation").css('display') != "none"){
                sizeConfirm = "false"
            }
        }
    	if(sizeConfirm == "false") {
    	    $("#modal_inspection").css("z-index",1)
            $('#failModalContent').text('글자 수가 초과된 사유가 있습니다. (2000byte 이하)');
            $('#failModalButton').attr("onclick", "zIndexChange()")
            $("#failModal").modal('show');
        }else{
            $("#modal_inspection").modal('hide');
            var reasonNull = false;
	        for (var i = arr0.length+1; i <= arrAll.length; i++) {
	            const reasonText = $('#reasonText' + i).val();
	            if (reasonText === '' || reasonText === null) {
                    reasonNull = true;
                    break;
	            }
	        }
	        if(reasonNull && arr0.length != 0){
                $('#failModalContent').text('등록되지 않은 파일 및 사유가 존재합니다.');
                $("#failModal").modal('show');
	        }
            else if (reasonNull) {
                $('#failModalContent').text('입력되지 않은 사유가 존재합니다.');
                $("#failModal").modal('show');
            } else if (arr0.length != 0) {
                $('#failModalContent').text('미등록 된 필수 파일이 존재합니다.');
                $("#failModal").modal('show');
            } else{
                $('#warnModalContent').text('검수 요청을 보내시겠습니까?');
                $("#warnModal").modal('show');
            }
    	}
    }


    function checkByte(e) {
        if(getByte($(e).val()) > 1372){
            $("#"+$(e).attr("id")+"Validation").css("display", "unset")
        }else{
            $("#"+$(e).attr("id")+"Validation").css("display", "none")
        }
    }
    function completeInspection(e){
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url : "/inspection",
            type: 'PUT',
            dataType: "json",
            data : {
                "projectId": nowProjectId,
                "projectName": $("#projectSelect option:selected").attr('name'),
                "inspectionStatus": "1",
                "inspectionUserEmail": "${userInfo.userEmail}",
                "inspectionUserName": "${userInfo.userName}",
                "teamId" : "${userInfo.teamId}"
            },
            success: function (response) {
                if (response.responseCode == "SUCCESS") {
                    $('#completeModalContent').html('검수 요청이 완료되었습니다.');
                    saveType = 'inspection';
                    $("#completeModal").modal();
                }
            }
        });
    }

    function confirm(func, regisId) {
        switch (func) {
            case 'delete':
            $('.delete-text').text('삭제하시겠습니까?');
            $('#delete_final_btn').attr('value', regisId);
            $('#delete_final_btn').attr('onclick', 'deleteFile($(this).attr(\'value\'))');
            break;
        }
    }


</script>