<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="result">
    <section class="card">
        <header class="card-header">
            <h2 class="card-title"><span class="i-rounded bg-danger"><i class="icon-set"></i></span>그룹관리
            </h2>
            <div class=" p-4 btn-container">
                <a href="#groupAddMod"  name="groupAdd" data-toggle="modal" class="btn btn-primary"><span
                        class="hide">등록</span></a>
            </div>
        </header>
        <div class="ct-header" >
            <button type="button" class="btn-filter d-xl-none" data-toggle="collapse"
                    data-target="#collapse-filter-group" aria-expanded="true">검색 필터<i class="icon-down"></i>
            </button>
            <div id="collapse-filter-group" class="collapse-filter collapse show" style="">
                <div class="filter no-gutters">
                    <div class="col">
                        <label class="form-control-label">
                            <b class="control-label">공통코드 그룹명</b>
                            <input type="text" id="searchGroup" class="form-control" placeholder="공통코드 그룹 명을 입력해주세요.">
                        </label>
                    </div>
                    <div class="col-auto">
                        <button type="button" id="searchBtn" class="btn " onclick="search()"><i class="icon-srch"></i>조회</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="ct-content">
            <div class="table-responsive">
                <table class="table">
                    <colgroup>
                        <col span="1" style="width: 3%">
                        <col span="1" style="width: 10%">
                        <col span="1" style="width: 20%">
                        <col span="1" style="width: 8%">
                        <col span="1" style="width: 10%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th style="width: 5%;">번호</th>
                        <th>공통코드 그룹ID</th>
                        <th>공통코드 그룹명</th>
                        <th>서브시스템</th>
                        <th style="width: 10%;">관리</th>
                    </tr>
                    </thead>
                    <tbody id="groupTbody">
                    <c:choose>
                        <c:when test="${fn:length(commonCodeGroup) == 0 }">
                            <tr><td></td><td></td><td>등록된 프로젝트가 없습니다.</td><td></td><td></td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="group" items="${commonCodeGroup}"  varStatus="index" >
                               <tr>
                                   <td>${index.count}</td>
                                   <td>${group.codeGroupId}</td>
                                   <td>${group.codeGroupName}</td>
                                   <td>${group.subSystem}</td>
                                   <td style="display: flex; align-items: center; justify-content: center; border-left: none; border-top: none">
                                       <a href="#groupDetail"  id="${group}" style="margin: 1px;" onclick="setDetailModal(this);" data-toggle="modal" class="btn btn-icon">
                                       		<i class="icon-srch text-dark"></i></a>
                                       <a href="#groupAddMod" id="${group}" style="margin: 1px;" onclick="setModModal(this);" value="${group.codeGroupName}" name="groupMod" data-toggle="modal" class="btn  btn-icon">
                                       		<i class="icon-edit text-dark"></i></a>
                                       <a href="#warnModal" name="groupDel" style="margin: 1px;" onclick="deleteGroup(this);" id="${group.codeGroupId}" data-toggle="modal" class="btn  btn-icon">
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
            <ul class="pagination" id="pagination"></ul>
        </div>
    </section>
</div>

<form id="groupForm">
    <!-- 그룹 등록 / 수정 MODAL -->
    <div id="groupAddMod" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
            <div class="modal-content commonCode form" style="width:50%;">
                <div class="modal-header">
                    <h2 class="modal-title" id="groupTitle"></h2>
                    <button type="button"  class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                            class="icon-close"></i></button>
                </div>
                <div class="modal-body modal-body-ct">
                    <table class="table">
                        <colgroup>
                            <col style="width: 20%">
                            <col style="width: 70%">
                        </colgroup>
                        <tbody>
                        <tr>
                            <td class="alert-text" style="vertical-align: middle">그룹ID</td>
                            <td class="tdLeft"  id="addGroup" style="display:none">
                                <div class="form-group">
                                    <input type="text" id="codeGroupId" name="codeGroupId" oninput="inputCheck(this)" maxlength="33" class="form-control form-input">
                                    <div style="position: relative">
                                        <div style="position: relative">
                                            <span style="color: #212529; font-size:12px" id="idRegExpValidation">&nbsp;영어 한자리, 숫자 조합으로된 ID를 입력해주세요</span>
                                        </div>
                                        <div style="position: relative">
                                            <span style="color:red; font-size:12px;display:none" id="codeGroupIdValidation" class="icon-x">&nbsp;그룹 아이디를 입력해주세요</span>
                                            <span style="color:red; font-size:12px;display:none" id="groupIdDuplication" class="icon-x">&nbsp;중복된 그룹 아이디 입니다. 다시 입력해주세요</span>
                                            <span style="color:red; font-size:12px;display:none" id="codeGroupIdLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td class="tdLeft" class="tdContents" style="vertical-align: middle; display:none" id="modGroup"  name="codeGroupId" ></td>
                        </tr>
                        <tr>
                            <td class="alert-text" style="vertical-align: middle">그룹명</td>
                            <td class="tdLeft">
                                <div class="form-group">
                                        <input type="text" id ="codeGroupName" name="codeGroupName" maxlength="166" oninput="inputCheck(this)" class="form-control form-input">
                                        <span style="color:red; font-size:12px;display:none" id="codeGroupNameValidation" class="icon-x">&nbsp;그룹명을 입력해주세요</span>
                                        <span  style="color:red; font-size:12px;display:none" id="groupNameDuplication" class="icon-x">&nbsp;중복된 그룹명 입니다. 다시 입력해주세요</span>
                                        <span style="color:red; font-size:12px;display:none" id="codeGroupNameLength" class="icon-x">&nbsp;글자수가 초과 되었습니다.</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="alert-text"  style="vertical-align: middle">서브시스템</td>
                            <td class="tdLeft" >
                                <select id="subSystem" name="subSystem" class="form-control">
                                    <option value="ALL">ALL</option>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">취소</button>
                    <button type="button" id="groupAddBtn" style="display:none" class="btn btn-primary" >등록</button>
                    <button type="button" id="groupModBtn" style="display:none" class="btn btn-primary">수정</button>
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<!-- 그룹 상세 조회 MODAL -->
<div id="groupDetail" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content commonCode form" style="width:50%;">
            <div class="modal-header">
                <h2 class="modal-title">그룹 상세 조회</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                        class="icon-close"></i></button>
            </div>
            <div class="modal-body modal-body-ct">
                <table class="table">
                    <colgroup>
                        <col style="width: 20%">
                        <col style="width: 70%">
                    </colgroup>
                    <tbody>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">그룹ID</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeGroupIdDetail"></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">그룹명</td>
                        <td class="tdLeft" style="vertical-align: middle" id="codeGroupNameDetail"></td>
                    </tr>
                    <tr>
                        <td class="alert-text" style="vertical-align: middle">서브시스템</td>
                        <td class="tdLeft" style="vertical-align: middle" id="subSystemDetail"></td>
                    </tr>

                    </tbody>
                </table>
            </div>
            <div class="modal-footer"></div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#groupAddMod').on('hidden.bs.modal', function () {
            $("#addGroup").css("display","none");
            if( $("#groupAddBtn").css("display") !="none"){
                $("#groupAddBtn").attr("style","display:none");
            }
            if( $("#groupModBtn").css("display") !="none"){
                $("#groupModBtn").attr("style","display:none");
            }
            $("#modGroup").css("display","none");
            $("#idRegExpValidation").attr("class","")
            $("#idRegExpValidation").css("color","#212529")
            $('#groupIdDuplication').css("display","none")
            $('#groupNameDuplication').css("display","none")
            $('#codeGroupNameValidation').css("display","none")
            $('#codeGroupIdValidation').css("display","none")
            $("#groupForm")[0].reset();

        });
    })
    function search(){
        commonCodeGroupList(1)
    }
    function movePage(move,activePage,nextPage,setPage,prePage){
        setSection(move,activePage,prePage,nextPage,setPage)
        if(move == 0 ){
            commonCodeGroupList(activePage);
            $("#activePage").val(activePage);
        }else if(move == 1){
            commonCodeGroupList(nextPage);
            $("#activePage").val(nextPage);
        }else if(move == -1){
            commonCodeGroupList(prePage);
            $("#activePage").val(prePage);
        }
    }
    function commonCodeGroupList(active){
    	$('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCodeGroupList",
            type : 'GET',
            data : {
              "codeGroupName" : $("#searchGroup").val().trim(),
              "pageNum": active
            },
            success :function( data ) {
                var responseMessage = data.responseMessage;
                if(data.responseCode == "SUCCESS"){
                    if(responseMessage.size == 0){
                        setEmpty()
                        setPagination(responseMessage.size,active)
                    }else{
                        setTbody(responseMessage.commonCodeGroup,active, responseMessage.size);
                        setSection(0,1,1,2,responseMessage.size)
                        setPagination( responseMessage.size, active);
                    }
                }
            }
        });
    }

    function idRegExpValidation(text){
        const regExp = /^[a-zA-Z]{1}[0-9]{2,}$/g ;
        if(regExp.test(text)){
            return true;
        }else{
            return false;
        }
    }

   //공통코드 그룹
   //공통코드 그룹 상세조회
    function setDetailModal(e) {
        var data = e.id.replace("{","").replace("}","").trim().split(", ");
        data.forEach(function (el, index){
          $("#"+el.split("=")[0]+"Detail").text(el.split("=")[1]);
        })
    }
    //공통코드 그룹 삭제
    function deleteGroup(e){
        $("button[name=confirmBtn]").attr('onClick',"codeConfirmDel('"+e.id+"')")
        $("#warnModalContent").text("삭제하시겠습니까?")
    }
    function codeConfirmDel(id){
        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCodeGroup",
            type : 'DELETE',
            data : "codeGroupId="+id+"&pageNum=1",
            success :function( data ) {
                var responseMessage = data.responseMessage;
                if(data.responseCode == "SUCCESS"){
                     setTbody(responseMessage.commonCodeGroup,1, responseMessage.size);
                     setPagination( responseMessage.size, 1);
                     $('#completeModalContent').text('삭제되었습니다.')
                     $('#completeModal').modal('show')
                }else if(data.responseCode == "ERROR"){
                     $('#warnModalContent').text(responseMessage)
                     $('#warnModal').modal('show')
                }
            }
        });
    }
    //공통코드 그룹 등록
    $("a[name=groupAdd]").on('click',function () {
       $("#groupTitle").text("공통 코드 그룹 등록");
       $("#groupAddBtn").attr("style","display:unset");
       $("#addGroup").css("display","");
    })
     // 공통코드 그룹 등록
    $("#groupAddBtn").on('click',function (){
       if($("#codeGroupId").val() != '' && $("#codeGroupName").val() != ''){

           $('#codeGroupIdValidation').css("display","none")
           $('#codeGroupNameValidation').css("display","none")
           $("#idRegExpValidation").attr("class","")
           $("#idRegExpValidation").css("color","#212529")

           if(idRegExpValidation($("#codeGroupId").val())){
               $('.layerPopup')[0].style.display='block';
               $.ajax({
                  url: "/commonCodeGroup",
                  type : 'POST',
                  data : $("#groupForm").serialize(),
                  success :function( data ) {
                    var responseMessage = data.responseMessage;
                    if(data.responseCode == "SUCCESS"){
                        $('#groupAddMod').modal('hide');
                        setTbody(responseMessage.commonCodeGroup,1,responseMessage.size);
                        setPagination( responseMessage.size, 1);
                        $('#completeModalContent').text('등록되었습니다.')
                        $('#completeModal').modal('show')

                    }else if(data.responseCode == "ERROR" ){
                        if(data.responseMessage == "ALL"){
                            $('#groupIdDuplication').css("display","unset")
                            $('#groupNameDuplication').css("display","unset")
                        }else if(data.responseMessage == "NAME"){
                            $('#groupNameDuplication').css("display","unset")
                        }else if(data.responseMessage == "ID"){
                            $('#groupIdDuplication').css("display","unset")
                        }
                    }

                  }
               });
           }else{
                $("#idRegExpValidation").attr("class","icon-x")
                $("#idRegExpValidation").css("color","red")
           }
       }else{
            if($("#codeGroupId").val() == '' && $("#codeGroupName").val() == ''){
                $("#idRegExpValidation").css("display","")
                $('#codeGroupIdValidation').css("display","unset")
                $('#codeGroupNameValidation').css("display","unset")
            }else if ($("#codeGroupId").val() == '' && $("#codeGroupName").val() != ''){
                 $('#codeGroupIdValidation').css("display","unset")
                 $('#codeGroupNameValidation').css("display","none")
            }else if ($("#codeGroupName").val() == '' && $("#codeGroupId").val() != ''){
                 if(!idRegExpValidation($("#codeGroupId").val())){
                    $("#idRegExpValidation").attr("class","icon-x")
                    $("#idRegExpValidation").css("color","red")
                 }
                 $('#codeGroupNameValidation').css("display","unset")
                 $('#codeGroupIdValidation').css("display","none")
            }
       }
    });

    // 공통 코드 그룹 수정
    function setModModal(e) {

        $('.layerPopup')[0].style.display='block';
        $.ajax({
            url: "/commonCodeGroupOne",
            type : 'GET',
            data : "codeGroupName="+$(e).attr("value"),
            success :function( data ) {
                if(data.responseCode == "SUCCESS"){
                    var responseMessage = data.responseMessage[0]
                    for(var key in responseMessage) {
                        if( key =="codeGroupId"){
                          $("td[name="+key+"]").text(responseMessage[key]);
                          $("#"+key).val(responseMessage[key]);

                        }else{
                          $("#"+key).val(responseMessage[key]);
                        }
                    }
                    $("#groupTitle").text("공통 코드 그룹 수정");
                    $("#groupModBtn").attr("style","display:unset");
                    $("#modGroup").css("display","");
                    $("#addGroup").css("display","none");
                }
            }
        });
    }
     $("#groupModBtn").on('click',function (){
        if($("#codeGroupId").val() != '' && $("#codeGroupName").val() != ''){
            $('.layerPopup')[0].style.display='block';
            $.ajax({
                url: "/commonCodeGroup",
                type : 'PUT',
                data : $("#groupForm").serialize().replace("groupId","codeGroupId"),
                success :function( data ) {
                    var responseMessage = data.responseMessage;
                    if(data.responseCode == "SUCCESS"){
                        $('#groupAddMod').modal('hide');
                        commonCodeGroupList($("#activePage").val())
                        $('#completeModalContent').text('수정되었습니다.')
                        $('#completeModal').modal('show')

                    }else if (data.responseCode == "ERROR"){
                       $('#groupNameDuplication').css("display","unset")

                    }
                }
            });
        }else{
            if($("#codeGroupId").val() == '' && $("#codeGroupName").val() == ''){
                $("#idRegExpValidation").css("display","")
                $('#codeGroupIdValidation').css("display","unset")
                $('#codeGroupNameValidation').css("display","unset")
            }else if ($("#codeGroupId").val() == '' && $("#codeGroupName").val() != ''){
                 $('#codeGroupIdValidation').css("display","unset")
                 $('#codeGroupNameValidation').css("display","none")
            }else if ($("#codeGroupName").val() == '' && $("#codeGroupId").val() != ''){
                 $('#codeGroupNameValidation').css("display","unset")
                 $('#codeGroupIdValidation').css("display","none")
            }
        }
     });
    function setTbody (data, activePage, size){
        var groupTr ="";
        setEndSize(activePage, size)

        $("#groupTbody").empty();
        data.forEach (function (el, index) {
            var idx = (activePage-1)*15 + index+1;
            var one = "{codeGroupId="+el.codeGroupId+", codeGroupName="+el.codeGroupName+", subSystem="+el.subSystem+"}";
            groupTr +="<tr><td>"+idx+"</td>";
            groupTr +="<td>"+el.codeGroupId+"</td>";
            groupTr +="<td>"+el.codeGroupName+"</td>";
            groupTr +="<td>"+el.subSystem+"</td>";
            groupTr +="<td style='display: flex; align-items: center; justify-content: center; border-left: none; border-top: none'>";
            groupTr +="<a href='#groupDetail' id='"+one+"' style='margin: 1px;'  onclick='setDetailModal(this);' name='groupDetail' data-toggle='modal' class='btn btn-icon'><i class='icon-srch text-dark'></i></a>";
            groupTr +="<a href='#groupAddMod' id='"+one+"' style='margin: 1px;' name='groupMod'  value='"+el.codeGroupName+"' onclick='setModModal(this)' data-toggle='modal' class='btn btn-icon'><i class='icon-edit text-dark'></i></a>";
            groupTr +="<a href='#warnModal' id='"+el.codeGroupId+"' style='margin: 1px;'  data-toggle='modal'   onclick='deleteGroup(this)' name='groupDel' class='btn btn-icon'><i class='icon-delete'></i></a></td></tr>";
        });
        $("#groupTbody").append(groupTr);

    }
    function setEmpty (){
        var groupTr ="";
        $("#groupTbody").empty();
        $('#allSizePrint').text("총 0 건 ")

        groupTr +="<tr><td></td>"
        groupTr +="<td></td>"
        groupTr +="<td>검색 결과가 없습니다</td>"
        groupTr +="<td></td>"
        groupTr +="<td></td></tr>"

        $("#groupTbody").append(groupTr);

    }

</script>
</section>