<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="result">
    <section class="card">
        <header class="card-header">
            <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-set"></i></span>코드관리
            </h2>
            <div class=" p-4 btn-container">
                <a href="#codeAddMod" name="codeAdd" data-toggle="modal" class="btn btn-primary"><span
                        class="hide">등록</span></a>
            </div>
        </header>
        <div class="ct-header" >
            <button type="button" class="btn-filter d-xl-none" data-toggle="collapse"
                    data-target="#collapse-filter-code" aria-expanded="true">검색 필터<i class="icon-down"></i>
            </button>
            <div id="collapse-filter-code" class="collapse-filter collapse show" style="">
                <div class="filter no-gutters">
                    <div class="col">
                        <label class="form-control-label">
                            <b class="control-label">공통코드명</b>
                            <input type="text" class="form-control" id="searchCode" placeholder="공통코드명을 입력해주세요.">
                        </label>
                    </div>
                    <div class="col-auto">
                        <button type="button" class="btn" id="searchBtn" onclick="search()"><i class="icon-srch"></i>조회</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="ct-content">
            <div class="table-responsive">
                <table class="table">
                    <colgroup>
                        <col span="1" style="width: 3%">
                        <col span="1" style="width: 20%">
                        <col span="1" style="width: 7%">
                        <col span="2" style="width: 10%">
                        <col span="2" style="width: 7%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th style="width: 5%;">번호</th>
                        <th>공통코드명</th>
                        <th>공통코드 그룹ID</th>
                        <th>공통코드 그룹명</th>
                        <th>상위 공통코드명</th>
                        <th>서브시스템</th>
                        <th style="width: 10%;">관리</th>
                    </tr>
                    </thead>
                    <tbody id="codeTbody">
                    <c:choose>
                        <c:when test="${fn:length(commonCode) == 0 }">
                            <tr><td></td><td></td><td></td><td>등록된 프로젝트가 없습니다.</td><td> </td><td> </td><td> </td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="code" items="${commonCode}" varStatus="index"  >
                                <tr>
                                    <td>${index.count}</td>
                                    <td >${code.codeName}</td>
                                    <td>${code.codeGroupId}</td>
                                    <td>${code.codeGroupName}</td>
                                    <c:if test="${code.codeTopName == null}">
                                        <td>-</td>
                                    </c:if>
                                    <c:if test="${code.codeTopName != null }">
                                         <td>${code.codeTopName}</td>
                                    </c:if>
                                    <td >${code.subSystem}</td>
                                    <td style="display: flex; align-items: center; justify-content: center; border-left: none; border-top: none">
                                        <a href="#codeDetail" name="codeDetail" style="margin: 1px;" onclick="setDetailModal(this);" id="${code}" data-toggle="modal" class="btn btn-icon">
                                        	<i class="icon-srch text-dark"></i></a>
                                        <a href="#codeAddMod" name="codeMod" style="margin: 1px;" onclick="setModModal(this);" id="${code}" value="${code.codeId}" data-toggle="modal" class="btn btn-icon">
                                        	<i class="icon-edit text-dark"></i></a>
                                        <a href="#warnModal" id="${code.codeId}" style="margin: 1px;" data-toggle="modal" onclick='deleteCode(this)'  name="codeDel" class="btn btn-icon">
                                        	<i class="icon-delete"></i></a>
                                    </td>
                                </tr>
                              </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <div style="margin-top:5px">
                <a style="margin: 0.5%;font-weight: bold;" id="allSizePrint">총  ${size} 건</a>
            </div>
            <ul class="pagination" id="pagination" ></ul>
        </div>
    </section>
</div>

<!-- 공통코드 상세조회 MODAL -->
<div id="codeDetail" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content commonCode form" style="width:50%;">
            <div class="modal-header">
                <h2 class="modal-title">공통 코드 상세조회</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                        class="icon-close"></i>
                </button>
            </div>
            <div class="modal-body modal-body-ct">
                <table class="table">
                    <colgroup>
                        <col style="width: 20%">
                        <col style="width: 70%">
                    </colgroup>
                    <tbody>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">공통코드명</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeNameDetail"></td>

                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">그룹 ID</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeGroupIdDetail"></td>

                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">그룹명</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeGroupNameDetail"></td>
                    </tr>

                    <tr>
                        <td class="alert-text" style="vertical-align: middle">상위공통코드명</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeTopNameDetail"></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">서브시스템</td>
                        <td class="tdLeft" style="vertical-align: middle" id="subSystemDetail"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<!-- 공통코드 등록 / 수정 MODAL -->
<form id="codeForm">
    <input type="hidden"  id="codeId" name="codeId">
    <div id="codeAddMod" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
            <div class="modal-content commonCode form" style="width:50%;">
                <div class="modal-header">
                    <h2 class="modal-title" id="codeTitle"></h2>
                    <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                            class="icon-close"></i>
                    </button>
                </div>
                <div class="modal-body modal-body-ct">
                    <table class="table">
                        <colgroup>
                            <col style="width: 20%">
                            <col style="width: 70%">
                        </colgroup>
                        <tbody>
                        <tr>
                            <td class="alert-text" style="vertical-align: middle">공통코드명</td>
                            <td class="tdLeft" >
                                <div class="form-group">
                                    <input type="text" id="codeName" name="codeName" oninput="inputCheck(this)" maxlength="166"  class="form-control form-input">
                                    <span style="color:red; font-size:12px;display:none" id="codeNameValidation" class="icon-x">&nbsp;공통코드명을 입력해주세요</span>
                                    <span style="color:red; font-size:12px;display:none" id="codeNameLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                                </div>
                            </td>
                        </tr>
                         <tr>
                            <td class="alert-text" style="vertical-align: middle">그룹명</td>
                            <td class="tdLeft" >
                                <select id="codeGroupId" name="codeGroupId" class="form-control">
                                    <c:forEach var="group" items="${commonCodeGroup}" varStatus="index"  >
                                        <option value=${group.codeGroupId}>${group.codeGroupName}</option>
                                    </c:forEach>
                                </select>
                                <span style="color:red; font-size:12px;display:none" id="groupNameDuplication" class="icon-x">&nbsp;중복된 그룹명 입니다. 다시 입력해주세요</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="alert-text" style="vertical-align: middle">상위공통코드명</td>
                            <td class="tdLeft" >
                                <select id="codeTopId" name="codeTopId" class="form-control">
                                    <option value="-">-</option>
                                    <c:forEach var="top" items="${commonTop}" varStatus="index" >
                                        <option name="topOption" value=${top.codeId}>${top.codeName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">취소</button>
                    <button type="button" id="codeAddBtn" style="display:none" class="btn btn-primary" >등록</button>
                    <button type="button" id="codeModBtn" style="display:none" class="btn btn-primary" >수정</button>
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
    $(document).ready(function () {
        //공통코드 폼 리셋
        $('#codeAddMod').on('hidden.bs.modal', function () {
            if( $("#codeAddBtn").css("display") !="none"){
                $("#codeAddBtn").attr("style","display:none");
            }
            if( $("#codeModBtn").css("display") != "none"){
                $("#codeModBtn").attr("style","display:none");
            }
            $('#codeNameValidation').css("display","none")
            $('#groupNameDuplication').css("display","none")
            $("#codeForm")[0].reset();
        });
    })

    function search(){
        commonCodeList(1)
    }
    function movePage(move,activePage,nextPage,setPage,prePage){
        setSection(move,activePage,prePage,nextPage,setPage)
        if(move == 0 ){
            commonCodeList(activePage);
            $("#activePage").val(activePage)
        }else if(move == 1){
            commonCodeList(nextPage);
            $("#activePage").val(nextPage)
        }else if(move == -1){
            commonCodeList(prePage);
            $("#activePage").val(prePage)
        }
    }

    function commonCodeList(active){
    	$('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCodeList",
            type : 'GET',
            data :  {
                "codeName" : $("#searchCode").val().trim(),
                "pageNum": active
            },
            success :function( data ) {
                var responseMessage = data.responseMessage;
                if(data.responseCode == "SUCCESS" ){
                    if(responseMessage.size == 0){
                        setEmpty()
                        setPagination(responseMessage.size,active)
                    }else{
                        setTbody(responseMessage.commonCode,active,responseMessage.size);
                        setSection(0,1,1,2,responseMessage.size)
                        setPagination(responseMessage.size,active)
                    }
                }

            },
        });
    }

    //공통코드 상세조회
    function setDetailModal(e) {
       var data = e.id.replace("{","").replace("}","").trim().split(", ");
       data.forEach(function (el, index){
        if(el.split("=")[1] == "null"){
          $("#"+el.split("=")[0]+"Detail").text("-");
        }else{
          $("#"+el.split("=")[0]+"Detail").text(el.split("=")[1]);
        }
       })
    }

    //공통코드 삭제
    function deleteCode(e){
        $("button[name=confirmBtn]").attr('onClick',"codeConfirmDel('"+e.id+"')")
        $("#warnModalContent").text("삭제하시겠습니까?")
    }
    function codeConfirmDel(id){
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCode",
            type : 'DELETE',
            data : "codeId="+id+"&pageNum=1",
            success :function( data ) {
                var responseMessage = data.responseMessage;
                if(data.responseCode == "SUCCESS"){
                    setTbody(responseMessage.commonCode,1,responseMessage.size);
                    setPagination(responseMessage.size,1)
                    $('#completeModalContent').text('삭제되었습니다.')
                    $('#completeModal').modal('show')
                }
            }
        });
    }

    // 공통코드 추가
    $("a[name=codeAdd]").on('click',function () {
       $("#codeTitle").text("공통 코드 등록");
       $("#codeAddBtn").attr("style","display:unset");
    })

    $("#codeAddBtn").on('click',function (){
       if($("#codeName").val() != ''){
    	   $('.layerPopup')[0].style.display='block';
           $.ajax({
              url: "/commonCode",
              type : 'POST',
              data : $("#codeForm").serialize(),
              success :function( data ) {
                var responseMessage = data.responseMessage;
                if(data.responseCode == "SUCCESS"){
                    $('#codeAddMod').modal('hide')
                    setTbody(responseMessage.commonCode,1,responseMessage.size);
                    setPagination(responseMessage.size,1)
                    $('#completeModalContent').text('등록되었습니다.')
                    $('#completeModal').modal('show')
                }else if(data.responseCode == "ERROR" ){
                    $('#groupNameDuplication').css("display","unset")

                }
              }
           });
       }else{
            $('#codeNameValidation').css("display","unset")
       }
    });

    //공통코드 수정
    function setModModal(e) {
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCodeOne",
            type : 'GET',
            data : "codeId="+$(e).attr("value"),
            success :function( data ) {
                if(data.responseCode == "SUCCESS"){
                    var responseMessage = data.responseMessage[0]
                    for(var key in responseMessage) {
                        if(responseMessage[key] == null){
                          $("#"+key).val("-");
                        }else{
                          $("#"+key).val(responseMessage[key]);
                        }
                    }
                    $("#codeTitle").text("공통 코드 수정");
                    $("#codeModBtn").attr("style","display:unset");
                    var codeId =  $("#codeId").val();

                }

            }
        });

    }

    $("#codeModBtn").on('click',function (){
        if($("#codeName").val() != ''){
            $('.layerPopup')[0].style.display='block';
            $.ajax({
                url: "/commonCode",
                type : 'PUT',
                data : $("#codeForm").serialize(),
                success :function( data ) {
                  if(data.responseCode == "SUCCESS"){
                      var responseMessage = data.responseMessage;
                      $('#codeAddMod').modal('hide')
                      commonCodeList($("#activePage").val())
                      $('#completeModalContent').text('수정되었습니다.')
                      $('#completeModal').modal('show')

                  }else if(data.responseCode == "ERROR" ){
                      $('#groupNameDuplication').css("display","unset")
                  }
                }
             });
        }else{
             $('#codeNameValidation').css("display","unset")
        }
    });



    function setTbody (data, activePage, size){
        var codeTr ="";
        setEndSize(activePage, size)

        $("#codeTbody").empty();
        data.forEach (function (el, index) {
            var idx = (activePage-1)*15 + index+1
            var one = "{codeId="+el.codeId+", codeName="+el.codeName+", codeGroupId="+el.codeGroupId+", codeTopId="+el.codeTopId+", codeGroupName="+el.codeGroupName+", subSystem="+el.subSystem+", codeTopName="+el.codeTopName+"}"
            codeTr +="<tr><td>"+idx+"</td>"
            codeTr +="<td>"+el.codeName+"</td>"
            codeTr +="<td>"+el.codeGroupId+"</td>"
            codeTr +="<td>"+el.codeGroupName+"</td>"
            if(el.codeTopName == undefined){
                codeTr +="<td>-</td>"
            }else{
                codeTr +="<td>"+el.codeTopName+"</td>"
            }
            codeTr +="<td>"+el.subSystem+"</td>"
            codeTr +="<td style='display: flex; align-items: center; justify-content: center; border-left: none; border-top: none'>"
            codeTr +="<a href='#codeDetail' style='margin: 1px;' id='"+one+"' onclick='setDetailModal(this);' name='codeDetail' data-toggle='modal' class='btn btn-icon'><i class='icon-srch text-dark'></i></a>"
            codeTr +="<a href='#codeAddMod' style='margin: 1px;' id='"+one+"' name='codeMod' onclick='setModModal(this)'  value='"+el.codeId+"'  data-toggle='modal' class='btn btn-icon'><i class='icon-edit text-dark'></i></a>"
            codeTr +="<a href='#warnModal' style='margin: 1px;'  id='"+el.codeId+"' data-toggle='modal' onclick='deleteCode(this)' name='codeDel' class='btn btn-icon'><i class='icon-delete'></i></a></td></tr>"
        });
        $("#codeTbody").append(codeTr);
    }
    function setEmpty (){
        var codeTr ="";
        $('#allSizePrint').text("총 0 건 ")
        $("#codeTbody").empty();
        codeTr +="<tr><td></td>"
        codeTr +="<td></td>"
        codeTr +="<td></td>"
        codeTr +="<td>검색 결과가 없습니다</td>"
        codeTr +="<td></td>"
        codeTr +="<td></td>"
        codeTr +="<td></td></tr>"
        $("#codeTbody").append(codeTr);
    }

</script>
