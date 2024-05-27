<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div id="file_add_content">
    <div class="left_content">
        <div class="file_list_explain">
            <div id="file_infos">
                <div class="file_info">
                    <div class="redcircle"></div>
                    <span>: &nbsp;필수</span>
                    <p class="info_box">[필수]<br />필수적으로 등록해야하는 폴더<br />누락 시, 검수 요청 불가</p>
                </div>
                <div class="file_info">
                    <div class="bluecircle"></div>
                    <span>: &nbsp;준필수</span>
                    <p class="info_box">[준필수]<br />누락되어도 검수요청 가능하지만,<br />검수요청 시, 누락 사유 기입 필요
                    </p>&nbsp;&nbsp;
                </div>
                <div class="file_info">
                    <div class="blackcircle"></div>
                    <span>: &nbsp;선택</span>
                    <p class="info_box">[선택]<br />누락되어도 검수요청 가능
                    </p>
                </div>
            </div>
        </div>
        <div id="tree_hr">
            <div>
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>
    </div>

    <div id="div_hr">
    </div>

    <div id="right_content">
        <div class="right_content_header">
            <div>
                <h3>등록된 파일</h3>
                <p class="right_fileTitle"></p>
            </div>
            <c:if test="${param.pageType== 'deliverablesReg'}">
                <div class="right_content_button">
                    <a href="#modal_file_register" id="btn_black" class="btn mr-2 mb-2" data-toggle="modal">등록</a>
                </div>
            </c:if>
        </div>
        <div id="file_detail_list">
	        <div class="ct-content">
	            <div class="table-responsive">
	                <table class="table">
	                    <thead class="filelist_thead">
	                        <tr class="table_tr">
	                            <th>순서</th>
	                            <th>구분</th>
	                            <th>제목</th>
	                            <th>파일명</th>
	                            <th>등록자</th>
	                            <th>등록일자</th>
	                            <th>관리</th>
	                        </tr>
	                    </thead>
	                    <tbody class="filelist_tbody">
	                    </tbody>
	                </table>
	            </div>
	        </div>
	    </div>
	</div>
</div>

<!-- 산출물 텍스트 보기 -->
<div id="modalTextViewModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">산출물 텍스트</h2>
            </div>
            <div class="modal-body modal-body-ct">
                <table class="table">
                    <colgroup>
                        <col style="width:30%;">
                        <col style="width:70%">
                    </colgroup>
                    <tbody>
	                    <tr>
	                        <td class="alert-text" style="vertical-align: middle;">제목</td>
	                        <td id="textTitleValue"></td>
	                    </tr>
	                    <tr>
	                        <td class="alert-text" style="vertical-align: middle;">내용</td>
	                        <td><textarea id="textContentValue" style="height:200px; width:100%" readonly></textarea></td>
	                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<!-- File 미리보기 Form -->
<form id="fileViewForm">
	<input type="hidden" id="fileViewFormFilePath" name="fileViewFormFilePath">
</form>

<script type="text/javascript">
	var userAuthorityId = '${userInfo.authorityId}';
	var nowProjectId;
	var nowDeliverablesID;
	var urlParams;
	
	$(document).ready(function () {
		// z-tree 설정
		var setting = {
		    view: {
		        dblClickExpand: false,
		        showLine: true,
		        selectedMulti: false
		    },
		    data: {
		        key: {
		            isParent: "isParent",
		            children: "children",
		            name: "deliverablesName",
		            title: "null",
		            grade: "requiredItems",
		            type: "fileGubun",
		            url: "url",
		            icon: "icon",
	                open: "open"
		        },
		        simpleData: {
		            enable: true,
		            idKey: "deliverablesId",
		            pIdKey: "deliverablesTopId",
		            rootPId: ""
		        }
		    },
		    callback: {
		        beforeClick: function (treeId, treeNode) {
		        var zTree = $.fn.zTree.getZTreeObj("tree");
		            if (treeNode.isParent) {
		                zTree.expandNode(treeNode);
		                return false;
		            }
		        }
		    }
		};
	    $.fn.zTree.init($("#tree"), setting, ${ zNodes });

        if(userAuthorityId == 4) {
            $("#btn_black").css("visibility", "hidden");
        }
        var visibleHiddenArray = ${ visibleHiddenArray };
        for (var i = 0; i < visibleHiddenArray.length; i++) {
        	$('span[visibleId='+visibleHiddenArray[i]).css('visibility', 'hidden');
        }
        $('.layerPopup')[0].style.display='none';

		// 산출물 등록 모달 내 라디오 박스 이벤트
	    $("input[name='type']").change(function () {
	        if ($("input[name='type']:checked").val() == 'type_text') {
	            $('.form-group:nth-of-type(3)').css('display', 'none');
	            $('.form-group:nth-of-type(4)').css('display', 'block');
	        } else if ($("input[name='type']:checked").val() == 'type_file') {
	            $('.form-group:nth-of-type(4)').css('display', 'none');
	            $('.form-group:nth-of-type(3)').css('display', 'flex');
	        }
	    })
	});
	
	// 좌측 리스트에서 파일 클릭 시 -> 오른쪽 파일 리스트 불러오기
	$(document).on('click', 'span[id*=span]', function (e) {
		var sendData;
		var dirArray = ${ dirArray };
        urlParams = new URL(location.href).pathname;
        var name = new URL(location.href).searchParams.get('inspectionProjectId');
        var lastChildArray = ${lastChildArray};

        if(lastChildArray.includes( e.target.innerText)){
            if (e.button === 0 && dirArray.includes(e.target.innerText)) {
                nowDeliverablesID = e.currentTarget.getAttribute('deliverablesid');
                $('#regis_modal_btn').attr('value', nowDeliverablesID);
                $('#right_content').css("display", "block");
                $('.right_fileTitle').text(" - " + e.target.innerText);
                if (urlParams == '/inspectionDetail'){
                    sendData = "projectId=" + name + "&deliverablesId=" + nowDeliverablesID;
                } else {
                    sendData = "projectId=" + nowProjectId + "&deliverablesId=" + nowDeliverablesID;
                }
            }
        }else{
            $('#right_content').css("display", "none");
        }
        getFileList(sendData);
        $('.layerPopup')[0].style.display='block';
	})
	
	function getFileList(sendData) {
        $('.layerPopup')[0].style.display='block';
	    $.ajax({
	        type: "GET",
	        url: "/fileList",
	        data: sendData,
	        datatype: "JSON",
	        success: function (response) {
	            if (response.responseCode == 'SUCCESS'){
	                var fileInfo = response.responseMessage;
	                rightContent(fileInfo);
	            }
	        }
	    });
	}
	
	// 불러온 파일 리스트 -> 오른쪽에 띄우기
	function rightContent(fileInfo) {
	    $('.filelist_tbody').empty();
	    var html = '';
	    fileInfo.forEach(function(item, index) {
	        html += '        <tr class="table_tr">';
	        html += '            <td>' + (index+1) + '</td>';
	        if(item.contentType == '0') {
	            html += '            <td>파일</td>';
	        } else if(item.contentType == '1') {
	            html += '            <td>텍스트</td>';
	        }
	        html += '            <td>' + item.deliverablesTitle + '</td>';
	        if(item.fileName != null) {
	            html += '            <td>' + item.fileName + '</td>';
	        } else {
	            html += '            <td></td>';
	        }
	        html += '            <td>' + item.registrationUserName + '</td>';
	        html += '            <td>' + item.registrationDate + '</td>';
	        html += '            <td>';
	        html += '                <button class="icon-srch" value="'+ item.filePath + '/' + item.fileName +
	        	'" onclick=\"readTextFile(this.value,\'' + item.contentType + '\',\'' + item.deliverablesTitle + '\',\'' + encodeURI(item.textContents) + '\')\"></button>';
	        if (userAuthorityId != 4 && urlParams != '/inspectionDetail') {
                html += '                <a href="#delete_alert" class="icon-delete" value="' + item.registrationId + 
                '" onclick="confirm(\'delete\', $(this).attr(\'value\'))" data-toggle="modal" disabled></a>';
            }
	        html += '            </td>';
	        html += '        </tr>';
	    });
	
	    $(".filelist_tbody").append(html);
	}
	
	//저장된 파일 열기
	function readTextFile(file, contentType, deliverablesTitle, textContents) {
		if(contentType == '0') { // 파일 형식
			if(file.indexOf('.pdf') == -1 && file.indexOf('.gif') == -1 && file.indexOf(".jpeg") == -1 && file.indexOf(".png") == -1){
		        $("#failModalContent").html('지원하지 않는 파일 형식입니다.');
		        $("#failModal").modal("show");
		    }else{
		        window.open('', 'newWindow');
		        $("#fileViewFormFilePath").val(file);
		        $("#fileViewForm")[0].target = 'newWindow';
		        $("#fileViewForm")[0].action="/fileView";
		        $("#fileViewForm")[0].submit();
		    }
		}else{ // 텍스트 형식
			$("#textTitleValue").html(deliverablesTitle);
			$("#textContentValue").html((decodeURI(textContents)).replace(/<br>/gi, "\n"));
			$("#modalTextViewModal").modal("show");
		}
	}

</script>