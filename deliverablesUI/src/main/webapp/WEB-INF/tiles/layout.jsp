<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html lang="UTF-8">
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width,initial-scale=1.0,shrink-to-fit=no">
<title>INZENT</title>
<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.min.css">
<link rel="shortcut icon" href="img/inzent_logo.png">
<link rel="stylesheet" href="css/common.css">
<script src="vendor/jquery/jquery-3.3.1.min.js"></script>
<script src="vendor/jquery/jquery.form.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="./css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./js/jquery.ztree.core.js"></script>
<script src="js/common.js"></script>
</head>
<style>
    .btn-icon {
        min-width: 1.5rem;
        height: 1.5rem;
    }
    .table td{
        overflow: hidden;
        text-align : center;
        height: 2.2rem;
        border-left: 1px solid #f0f1f6;
        border-right: 1px solid #f0f1f6;
        border-top: 1px solid #f0f1f6;
    }
    .table th{
        text-align: center;
    }
    body.modal-open[style] {

        padding-right: 0px !important;

    }
    .tdLeft{
        text-align : left !important;
    }
    .table tr span{
        float: left;
        padding: 5px;
    }
    #ct{
        overflow-y: scroll;
        display: flow;
    }
    .layerPopup {
	    display: none;
	    position: fixed;
	    top: 0;
	    left: 0;
	    width: 100%;
	    height: 100%;
	    background: rgba(0,0,0,0.8);
	    opacity : 0.5;
	    z-index: 1000;
	    justify-content: center;
	    align-items: center;
	}
	.spinner-border {
	    position: absolute;
	    top: 50%;
	    left: 50%;
	}
	div {
		position: relative;
	}
</style>

<body>
    <div class="layerPopup" style="z-index:2000;display:block">
        <div class="spinner-border text-light" role="status"></div>
    </div>
	<div id="wrap">
		<header id="hd">
			<tiles:insertAttribute name="header" />
		</header>
	    <nav id="sidebar" class="panel">
	    	<tiles:insertAttribute name="menu" />
	    </nav>
	    <div id="ct">
	    	<tiles:insertAttribute name="content"/>
            <input type="hidden" id="activePage" name="activePage" value="1"/>
	    </div>
	</div>
	<tiles:insertAttribute name="commonModal"/>
	<div class="layerPopup" style="z-index:2000;">
	    <div class="spinner-border text-light" role="status"></div>
	</div>
</body>

<!-- 산출물 다운로드 Form -->
<form id="deliverablesDownloadForm" method="get">
    <input type="hidden" id="downloadProjectId" name="projectId"/>
    <input type="hidden" id="downloadProjectName" name="projectName"/>
</form>
<script>

	var startPage = 1;
	var endPage = 5;
    var setPage =0;
    var prePage =0;
    var nextPage =0;

	function errorCheck(){
		var responseCode = '${responseCode}';
		if(responseCode == "HTTP_ERROR"){
		    $('.layerPopup')[0].style.display='none';
            $('#warnModalContent').text("ERROR MESSAGE : ${responseMessage}");
            $('#warnModal').modal('show');
            $('#warnModalButton').attr("onclick","location.href='/login'");
        }else if(responseCode == "EXCEPTION_ERROR"){
            $('.layerPopup')[0].style.display='none';
        	location = "/error";
        }
	}
	window.onload= function(){
		errorCheck();
	}

    $(document).ready(function (){
        $("#downloadModalButton").click(function (e) {
            zipFileDownload();
        });

        $('#deliverablesDownloadForm').submit(function() {
            $(this).ajaxSubmit({
                beforeSubmit: function () {
                    $('.layerPopup')[0].style.display='block';
                },
                timeout: 180000,
                success: function(res){
                    $('.layerPopup')[0].style.display='none';
                },
                error: function(res){
                    $('.layerPopup')[0].style.display='none';
                    location = "/error";
                }
            });
        });
    })

    $.ajaxSetup({
          timeout: 5000
    });

    $(document).ajaxError(function() {
        $('.layerPopup')[0].style.display='none';
        location = "/error";
     });

    $(document).ajaxSuccess(function(event, xhr, options, data) {
        $('.layerPopup')[0].style.display='none';
        if(xhr.getResponseHeader('content-length') == '7366'){ // login 페이지로 redirect할 경우
			location = "/login";
       	} else if(data.responseCode == "HTTP_ERROR"){
            $(".modal").hide();
            $('#warnModalContent').text("ERROR MESSAGE : " + data.responseMessage);
            $('#warnModal').modal('show');
            $('#warnModalButton').attr("onclick","location.href='/login'");
        }else if(data.responseCode == "EXCEPTION_ERROR"){
        	location = "/error";
        }
    });

	function setPagination(size, activePage){
        activePage =  parseInt(activePage);
	    var page ="";

	    if(parseInt(size%15) == 0){
	        setPage = parseInt(size/15);
	    }else{
	        setPage = parseInt(size/15)+1;
	    }
        if(activePage == 1 ){
            startPage = 1
            if(setPage < 5){
                endPage = setPage
            }else{
                endPage =5
            }
        }else{
            if (setPage <= 5) {
                endPage = setPage
            }else if( startPage > 5){
                endPage = startPage + 5
                if(endPage > setPage){
                    endPage = setPage
                }
            }
        }
	    if(activePage == 1 ){
	        prePage = activePage;
	    }else{
	        prePage = activePage -1;
	    }
	
	    if (activePage == setPage ){
	        nextPage = setPage;
	    }else{
	        nextPage = activePage +1;
	    }
	    $("#pagination").empty();
	     page += "  <li class='page-item'><a class='page-link page-link-prev'  onclick='movePage(-1,"+activePage+","+nextPage+","+setPage+","+prePage+")' href='#'><i class='icon-left'></i></a></li>"
	     if ( size !=0 ){
             for(let i = startPage; i<=endPage; i++){
                if(i == activePage){
                    page += " <li class='page-item'><a class='page-link active' id='active'  onclick='movePage(-1,"+i+","+nextPage+","+setPage+","+prePage+")' href='#'>"+i+"</a></li>"
                }else{
                    page += " <li class='page-item'><a class='page-link'  onclick='movePage(0,"+i+","+nextPage+","+setPage+","+prePage+")' href='#'>"+i+"</a></li>"
                }
             }
	     }
	     page += "<li class='page-item'><a class='page-link page-link-next' onclick='movePage(1,"+activePage+","+nextPage+","+setPage+","+prePage+")'  href='#'><span class='icon-right'></span></a></li>"
	    $("#pagination").append(page);
	
	}
	
	function setSection(move,activePage,prePage,nextPage, setPage){
	    if (move == -1){
	        if( activePage != 1 && (prePage%5) == 0){
	            endPage = prePage;
	            startPage = endPage-4;
	            if(endPage > setPage){
	                endPage = setPage;
	            }
	        }
	    }else if (move == 1){
	        if( activePage != 1 && (nextPage%5) == 1){
	            startPage = nextPage;
	            endPage = startPage+4;
	            if(endPage > setPage){
	                endPage = setPage;
	            }
	        }
	    }
	}

	function setEndSize(activePage, size){
        var endSize = activePage * 15
        if(endSize > size){
            endSize = size
        }
        if(activePage != 1){
            $('#allSizePrint').text("총 "+size+" 건 / [ "+((activePage - 1) * 15 + 1 )+" 부터 "+endSize +" ]")
        }else{
            $('#allSizePrint').text("총 "+size+" 건 / [ "+activePage+" 부터 "+endSize +" ]")
        }
	}


	function deliverablesDownload(projectId, projectName) {
		$("#downloadProjectId").val(projectId);
		$("#downloadProjectName").val(projectName);
		$("#downloadModalContent").html("산출물 다운로드를 하시겠습니까?");
		$("#downloadModal").modal("show");
	}

	function zipFileDownload() {
		$("#deliverablesDownloadForm")[0].action = "/zipFileDownload";
		$("#deliverablesDownloadForm").submit();
	}

    function getByte(str) {
        var byte = 0;
        for (var i=0; i<str.length; ++i) {
            (str.charCodeAt(i) > 127) ? byte += 2 : byte++ ;
        }
        return byte;
    }
</script>
</html>
