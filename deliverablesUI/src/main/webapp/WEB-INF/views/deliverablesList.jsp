<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<link rel="stylesheet" href="css/fileList.css">

<!-- **********************************************
      산출물 목록 프로그램 트리 영역 + 파일 설명
*********************************************** -->
<section class="card">
    <header class="card-header">
        <h2 class="card-title">
            <span class="i-rounded bg-danger"><i class="icon-folder-check"></i></span>산출물 목록 관리
        </h2>
    </header>
    <div style="height: 75vh; padding-left: 35px; padding-top: 5px">
        <div class="file_list_header">
            <div class="file_list_explain">
                <div class="file_info" style="padding-right: 8px;">
                    <div class="redcircle"></div>
                    <span>: &nbsp; 필수</span>
                    <p class="info_box">
                        [필수]<br />필수적으로 등록해야하는 폴더<br />누락 시, 검수 요청 불가
                    </p>
                </div>
                <div class="file_info" style="padding-right: 8px;">
                    <div class="bluecircle"></div>
                    <span>: &nbsp; 준필수</span>
                    <p class="info_box">
                        [준필수]<br />누락되어도 검수요청 가능하지만,<br />검수요청 시, 누락 사유 기입 필요
                    </p>
                </div>
                <div class="file_info" style="padding-right: 8px;">
                    <div class="blackcircle"></div>
                    <span>: &nbsp; 선택</span>
                    <p class="info_box">
                        [선택]<br />누락되어도 검수요청 가능
                    </p>
                </div>
            </div>
        </div>
        <div id="tree_div">
            <div id="rootNodeDiv">
                <img src="./img/folderIcon.png" />
                <p id="rootNode">산출물 목록</p>
            </div>
            <ul id="tree" class="ztree"></ul>
        </div>
    </div>
</section>


<!-- 디렉터리 등록 -->
<div id="modal_dirAdmin" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="admin_title" class="modal-title">하위폴더 추가</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close">
                    <i class="icon-close"></i>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="control-label">폴더명</label>
                    <div style="position: relative">
                        <input type="text" id='dir_form' class="form-control" required>
                        <span id="addDirValidation" class="icon-x" style="display: none; color: red; font-size: 12px"> 폴더명을 입력하세요.</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <a type="button" id="admin_button" class="btn btn-primary" data-toggle="modal">추가</a>
            </div>
        </div>
    </div>
</div>

<!-- 파일 등록 -->
<div id="modal_fileAdmin" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="admin_fileTitle" class="modal-title">파일 수정</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close">
                    <i class="icon-close"></i>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group"  style="margin-bottom: 15px">
                    <label class="control-label">필수 여부</label>
                    <form class="file_type">
                        <div>
                            <input id="radioOne" type="radio" name="type" value="0"> <label for="type_text">필수</label></br>
                        </div>
                        <div>
                            <input id="radioTwo" type="radio" name="type" value="1"> <label for="type_text">준필수</label></br>
                        </div>
                        <div>
                            <input id="radioThree" type="radio" name="type" value="2"> <label for="type_text">선택</label></br>
                        </div>
                    </form>
                    <span id="settingFolderValidationCheck" class="icon-x" style="display: none; color: red; font-size: 12px"> 필수여부를 체크하세요.</span>
                </div>
                <div class="form-group">
                    <label class="control-label">설명</label>
                    <div style="position: relative">
                        <input type="text" class="form-control" id="settingFolderDescription">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <a href="#modal_complete" onclick="complete('수정')" type="button" id="admin_fileButton" class="btn btn-primary" data-toggle="modal">변경</a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var listInfo = ${ zNodes };
    var userList = ${ userList };
    var dirArray = ${ dirArray };

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
                open: "open",
                pId: "deliverablesTopId"
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

    // z-tree 적용
    $(document).ready(function (e) {
        $.fn.zTree.init($("#tree"), setting, ${ zNodes });
        var visibleHiddenArray = ${ visibleHiddenArray };
        for (var i = 0; i < visibleHiddenArray.length; i++) {
        	$('span[visibleId='+visibleHiddenArray[i]).css('visibility', 'hidden');
        }

        $('.layerPopup')[0].style.display='none';
    });

    // z-tree leaf 노드 클릭 시, 기능 버튼 모달 출력
    $(document).on('mousedown', '#dir_buttons .modal_addDir', function (e) {
        $(".checkBlank").removeClass('alert alert-danger');
        $(".checkBlank").html('');
        $("#dir_form").val("");
        var parentTag = $("#"+e.target.id).parents("div")[0].id;
        var modifyPrevGrade = $("#"+parentTag).prev()[0].getAttribute('grade');
        var modifyPrevTitle = $("#"+parentTag).prev()[0].getAttribute('filename');
        if (modifyPrevGrade == '0' || modifyPrevGrade == '1' || modifyPrevGrade == '2') {
            $('.modal-footer > button:first-child').css("visibility", "visible");
            $('#failModalCancelButton').show();
            $('#failModalContent').html('이미 등록 설정 된 폴더입니다.<br>하위 폴더 추가 시 설정이 초기화 됩니다.<br>그래도 하위 폴더를 추가 하시겠습니까?');
            $("#failModal").modal();
            $("#failModalButton").click(function () {
                $("#modal_dirAdmin").modal('show');
                admin('addDirectory', deliverablesid, modifyPrevTitle, '', '');
            });
        } else {
            $("#modal_dirAdmin").modal('show');
            admin('addDirectory', deliverablesid, modifyPrevTitle, '', '');
        }
    })

    $(document).on('mousedown', '#dir_buttons .modal_modifyDir', function (e) {
        $(".checkBlank").removeClass('alert alert-danger');
        $(".checkBlank").html('');
        $("#dir_form").val("");
        var parentTag = $("#"+e.target.id).parents("div")[0].id;
        var modifyPrevTitle = $("#"+parentTag).prev()[0].getAttribute('filename');
        var modifyPrevGrade = $("#"+parentTag).prev()[0].getAttribute('grade');
        var pId = $("#"+parentTag).prev()[0].getAttribute('deliverablesTopId');
        admin('modifyDirectory', deliverablesid, modifyPrevTitle, modifyPrevGrade, pId);
    })

    $(document).on('mousedown', '#dir_buttons .modal_settingFolder', function (e) {
        $(".checkBlank").removeClass('alert alert-danger');
        $(".checkBlank").html('');
        $("input:radio[name='type']").prop('checked', false);
        $("#settingFolderName").val("");
        $("#settingFolderDescription").val("");
        var parentTag = $("#"+e.target.id).parents("div")[0].id;
        var modifyPrevTitle = $("#"+parentTag).prev()[0].getAttribute('filename');
        var deliverablesId = $("#"+parentTag).prev()[0].getAttribute('deliverablesid');
        getTopId(deliverablesId, modifyPrevTitle);
    })

    $(document).on('mousedown', '#title_buttons #btns_black_title', function (e) { admin('addDirectory', deliverablesid, '', '', '') })

    var deliverablesid;
    var dirButton = `<div id="dir_buttons" class="openFileButtons">
	                    <a href="" id="btns_black_dir" class="modal_addDir" data-toggle="modal" >하위폴더 추가</a>
	                    <a href="#modal_dirAdmin" id="btns_black_dir" class="modal_modifyDir" data-toggle="modal">폴더이름 변경</a>
	                    <a href="#modal_fileAdmin" id="btns_black_dir" class="modal_settingFolder" data-toggle="modal">등록폴더 설정</a>
	                    <a id="btns_black_dir" class="modal_alert" onclick="complete('fileDirectory', deliverablesid)">해당폴더 삭제</a>
	                    </div>`;

    var titleButton = `<div id="title_buttons" class="openFileButtons">
	                    <a href="#modal_dirAdmin" id="btns_black_title" class="modal_addDir" data-toggle="modal">하위폴더 추가</a>
	                    </div>`;


    function getTopId(deliverablesId, modifyPrevTitle) {
        $.ajax({
            type: "GET",
            url: "/deliverablesSearch",
            data : { "deliverablesId": deliverablesId },
            datatype: "JSON",
            success: function (response) {
                if(response.responseCode == "ERROR") {
                    $('#warnModalContent').text('최하위 폴더만 등록 설정이 가능합니다.');
                    $("#warnModal").modal();
                } else if (response.responseCode == 'SUCCESS'){
                    admin('settingFolder', deliverablesid, modifyPrevTitle, '', '');
                }
            }
        });
    }

    // 디렉터리 팝업 기능 (우클릭)
    var pastDirText;
    var pastFileText;
    var pastTitleText;
    if(userList.authorityId == 0 || userList.authorityId == 1 || userList.authorityId == 2) {
        $(document).on('mousedown', 'a[id*=tree_]', function (e) {
            var treeName = e.currentTarget.getAttribute('id');
            deliverablesid = e.currentTarget.getAttribute('deliverablesid');

            // 우클릭으로 디렉터리를 눌렀을 때
            if (e.button === 2 && dirArray.includes(e.target.innerText) && e.target.getAttribute("type") == '0') {
                pastDirText = e.target.innerText;
                document.oncontextmenu = function (e) {
                    return false;
                }
                $('#dir_buttons').remove();
                $('#title_buttons').remove();
                $('a[id=' + treeName + ']').after(dirButton);
                $('#dir_buttons').attr('deliverablesid', deliverablesid);
            }

            // 좌클릭으로 디렉터리를 눌렀을 때
            else if (e.button === 0 && dirArray.includes(e.target.innerText) && e.target.getAttribute("type") == '0') {
                $('#dir_buttons').remove();
                $('#title_buttons').remove();
            }

            // 우클릭으로 파일을 눌렀을 때
            else if (e.button === 2 && !dirArray.includes(e.target.innerText) && e.target.getAttribute("type") == '1') {
                pastFileText = e.target.innerText;
                document.oncontextmenu = function (e) {
                    return false;
                }
                $('#dir_buttons').remove();
                $('#title_buttons').remove();
                $(this).after(fileButton);
                $('p[deliverablesid*='+deliverablesid+']').css('visibility', 'hidden');
            }

            // 좌클릭으로 파일을 눌렀을 때
            else if (e.button === 0 && !dirArray.includes(e.target.innerText) && e.target.getAttribute("type") == '1') {
                pastFileText = e.target.innerText;
                $('#dir_buttons').remove();
                $('#title_buttons').remove();
            }
        })
    }

    $(document).on('mousedown', '#rootNodeDiv', function (e) {
        // 우클릭으로 타이틀을 눌렀을 때
        if (e.button === 2) {
            pastTitleText = e.target.innerText;
            document.oncontextmenu = function (e) {
                return false;
            }
            $('#dir_buttons').remove();
            $('#title_buttons').remove();
            $('#rootNodeDiv').after(titleButton);
        }

        // 좌클릭으로 디렉터리를 눌렀을 때
        else if (e.button === 0) {
            $('#dir_buttons').remove();
            $('#title_buttons').remove();
        }
    })

    // 기능 버튼 영역 외 영역 클릭 시 기능 버튼 팝업 닫기
    $('html').click(function (e) {
        if (pastDirText !== e.target.innerText) {
            $('div').remove('#dir_buttons');
        }
        if (pastTitleText !== e.target.innerText) {
            $('div').remove('#title_buttons');
        }
    })

    // 파일 위에 마우스 올렸을 때, 파일 설명창 표시
    $(document).on('mouseenter', 'a[class*=level]', function (e) {
        var selectId = this.getAttribute('deliverablesid');
        for (let i=0; i < listInfo.length; i++) {
            if(listInfo[i].deliverablesId == selectId) {
                $('p[deliverablesid*='+deliverablesid+']').css('visibility', 'visible');
                if(listInfo[i].description == null) {
                    $('p[deliverablesid*='+selectId+']').remove();
                }
                $('p[deliverablesid*='+selectId+']').text(listInfo[i].description);
            }
        }
    });

    var prevTitle ="";
    var prevChecked = "";
    $('#modal_fileAdmin').on('shown.bs.modal', function (e) {
        prevTitle = $('#settingFolderName').val();
        prevChecked = $("input[name='type']:checked").val();
    });

    var nowDeliverablesName = "";
    // 완성 모달 내 TEXT 변경
    function admin(e, deliverablesId, modifyPrevTitle, requiredItems, deliverablesTopId) {
        var deliverId = deliverablesId;
        var grade = requiredItems;
        var pId = deliverablesTopId;
        if (e === 'settingFolder') {
            $("#settingFolderValidationName").hide();
            $("#settingFolderValidationCheck").hide();
            $('#admin_fileTitle').text('등록폴더 설정');
            $("#admin_fileButton").attr('onclick', "complete('settingFolder', '"+ deliverId+ "' ,'', '', '','')");
            for(var i=0; i<listInfo.length; i++) {
                if(listInfo[i].deliverablesId == deliverId) {
                    $('#settingFolderDescription').val(listInfo[i].description);
                    if(listInfo[i].requiredItems == "0") {
                        $("#radioOne").prop("checked",true);
                    } else if(listInfo[i].requiredItems == "1") {
                        $("#radioTwo").prop("checked",true);
                    } else if(listInfo[i].requiredItems == "2") {
                        $("#radioThree").prop("checked",true);
                    }
                }
            }
            $('#admin_fileButton').text('설정');
            nowDeliverablesName = modifyPrevTitle;
        } else if (e === 'addDirectory') {
            var deliverId = deliverablesId;
            var prevTitle = modifyPrevTitle;
            $("#addDirValidation").hide();
            $('#admin_title').text('하위폴더 추가');
            $("#admin_button").attr('onclick', "complete('addDirectory', '"+deliverId+ "' , '" +prevTitle+ "', '', '', '')");
            $('#admin_button').text('추가');
        } else if (e === 'modifyDirectory') {
            var description = '';
            for(var i=0; i<listInfo.length; i++) {
                if(listInfo[i].deliverablesId == deliverId) {
                    description = listInfo[i].description;
                }
            }
            $("#addDirValidation").hide();
            $('#admin_title').text('폴더이름 변경');
            $("#admin_button").attr('onclick', "complete('modifyDirectory', '"+ deliverId+ "' ,'', '"+grade+"', '"+pId+"', '"+description+"')");
            $('#admin_button').text('변경');
            $('#dir_form').val(modifyPrevTitle);
        }

    }

    function settingFolderComplete(deliverablesId, deliverablesName, requiredItems, description, rejectProjectId){
    	var sendData = "deliverablesId=" + deliverablesId + "&deliverablesName=" + deliverablesName + "&requiredItems=" + requiredItems;
        if(description != '') {
            sendData += "&description=" + description;
        }
        $(".complete-text").last().css('display', 'none');
        $('#modal_fileAdmin').modal('hide');
        $.ajax({
            type: "PUT",
            url: "/deliverablesList",
            data: sendData,
            datatype: "JSON",
            success: function (response) {
                if (response.responseCode == "ERROR") {
                    $(".checkBlank").addClass('alert alert-danger');
                    $(".checkBlank").html(response.responseMessage);
                } else if (response.responseCode == 'SUCCESS'){
                	if(rejectProjectId != ""){
                        $.ajax({
                            url : "/inspection",
                            type: 'PUT',
                            dataType: "json",
                            data : {
                            	"projectId": rejectProjectId,
                                "inspectionStatus": "3",
                                "inspectionUserEmail": "${userInfo.userEmail}",
                                "inspectionUserName": "${userInfo.userName}",
                                "inspectionRejectionReason": "필수 또는 준필수 폴더가 추가되어 반려 처리되었습니다."
                            }
                        });
                    }
                    $("input[name='type']").removeAttr('checked');
                    $('#settingFolderDescription').val('');
                    top.document.location.reload();
                }
            }
        });
    }

    function complete(e, deliverablesId, modifyPrevTitle, grade, deliverablesTopId, descript) {
        switch (e) {
            case 'settingFolder':
                var deliverablesName = nowDeliverablesName;
                var requiredItems = $("input[name='type']:checked").val();
                var description = $('#settingFolderDescription').val();

                if(requiredItems == undefined) {
                    $("#settingFolderValidationCheck").show();
                } else {
                    $('.layerPopup')[0].style.display='block';
                    $.ajax({
                        url: "/getRejectionStatus",
                        type: 'GET',
                        data : {
                        	"deliverablesId": deliverablesId,
                        	"prevChecked": prevChecked,
                        	"requiredItems": requiredItems
                        },
                        success: function (data) {
                            var rejectProjectId = data.responseMessage.rejectProjectId;
                            if(rejectProjectId != ""){
                                $('.alert-text').text("필수나 준필수 여부 선택 시 이전의 검수 요청은 반려됩니다.");
                                $("#warnModalButton").attr('onclick', "settingFolderComplete( '"+ deliverablesId+ "' , '"+deliverablesName+"', '"+requiredItems+"', '"+description+"', '"+rejectProjectId+"')");
                                $('#warnModal').modal('show');
                            }else{
                                settingFolderComplete(deliverablesId, deliverablesName, requiredItems, description, rejectProjectId);
                            }
                        }
                    });
                }
                break;

            case 'addDirectory':
                var deliverablesName = $('#dir_form').val();
                var prevTitle = modifyPrevTitle;
                if(deliverablesName == '') {
                    $("#addDirValidation").show();
                } else {
                    var sendData = "deliverablesId=" + deliverablesId + "&deliverablesName=" + deliverablesName + "&prevTitle=" + prevTitle;
                    $(".complete-text").last().css('display', 'none');
                    $.ajax({
                        type: "POST",
                        url: "/deliverablesList",
                        data: sendData,
                        datatype: "JSON",
                        success: function (response) {
                            if(response.responseCode == "ERROR") {
                                $('.alert-text').text(response.responseMessage);
                                $('.modal-footer > button:first-child').css("visibility", "hidden");
                                $('#warnModal').modal();
                            } else if (response.responseCode == 'SUCCESS'){
                                top.document.location.reload();
                            }
                        }
                    });
                }
                break;

            case 'modifyDirectory':
                var deliverablesName = $('#dir_form').val()
                var requiredItems = grade;
                var description = ""; // 고쳐
                var sendData = "deliverablesId=" + deliverablesId + "&deliverablesName=" + deliverablesName + "&requiredItems=" + requiredItems + "&deliverablesTopId=" + deliverablesTopId;
                if(descript != '') {
                    sendData += "&description=" + descript;
                }
                $(".complete-text").last().css('display', 'none');
                $.ajax({
                    type: "PUT",
                    url: "/deliverablesList",
                    data: sendData,
                    datatype: "JSON",
                    success: function (response) {
                        if(response.responseCode == "ERROR") {
                            $('.alert-text').text(response.responseMessage);
                            $('.modal-footer > button:first-child').css("visibility", "hidden");
                            $('#warnModal').modal();
                        } else if (response.responseCode == 'SUCCESS'){
                            location.reload();
                        }
                    }
                });
                break;

            case 'fileDirectory':
                var sendData = "deliverablesId=" + deliverablesId;
                $(".complete-text").last().css('display', 'none');

                $.ajax({
                    type: "DELETE",
                    url: "/deliverablesList",
                    data: sendData,
                    datatype: "JSON",
                    success: function (response) {
                        if(response.responseCode == "ERROR") {
                            $('.alert-text').text(response.responseMessage);
                            $('.modal-footer > button:first-child').css("visibility", "hidden");
                            $('#warnModal').modal();
                        } else if (response.responseCode == 'SUCCESS') {
                            location.reload();
                        }
                    }
                });
                break;

        }
    }
</script>