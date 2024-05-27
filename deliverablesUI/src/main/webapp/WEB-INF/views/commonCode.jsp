<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .form{
        width : 70%
    }
    .ct-header{
        padding: 5px
    }
    .modal-content commonCode{
        width : 70%;
    }
    .pagination{
        padding:0; !important
    }

</style>

<section class="card">
<nav>
    <div class="nav nav-tabs" id="nav-tab" role="tablist">
        <a class="${mode eq 'commonCodeManager' ? 'nav-item nav-link active' : 'nav-item nav-link'}"  aria-selected="${mode eq 'commonCodeManager' ? 'true':'false'}" id="nav-code-tab" data-toggle="tab" href="/commonCode" role="tab"
           aria-controls="nav-code" >코드관리</a>
        <a class="${mode eq 'commonGroupManager' ? 'nav-item nav-link active' : 'nav-item nav-link'}"  aria-selected="${mode eq 'commonGroupManager' ? 'true':'false'}" id="nav-group-tab" data-toggle="tab" href="/commonCodeGroup" role="tab"
           aria-controls="nav-group" >그룹관리</a>
    </div>
</nav>

<div class="tab-content" id="nav-tabContent">
    <div class="tab-pane fade show  active" id="${mode eq 'commonCodeManager' ? 'nav-code' : 'nav-group'}"
    aria-labelledby="${mode eq 'commonCodeManager' ? 'nav-code' : 'nav-group'}" role="tabpanel" >
        <c:import url="/WEB-INF/views/${mode}.jsp"/>
    </div>
</div>

<form id="commonTabForm"></form>

</section>

<script>
	$(document).ready(function () {
        var size = ${size};
        $("#activePage").val("1");
        setPagination( size,  $("#activePage").val() );
        setEndSize( $("#activePage").val(), size );
        $('.layerPopup')[0].style.display='none';
		$('#commonTabForm').submit(function() {
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

    $("#nav-group-tab").on('click', function(){
        $("#commonTabForm")[0].action = "/commonCodeGroup";
        $("#commonTabForm").submit();
    });

    $("#nav-code-tab").on('click', function(){
        $("#commonTabForm")[0].action = "/commonCode";
        $("#commonTabForm").submit();
    });

    document.addEventListener("keyup", function(event) {
      if(event.keyCode == '13'){
        $("#searchBtn").click();
      }
    });

     function inputCheck(e){
        if($(e).val() != ""){
            if($(e).attr("maxlength") !== undefined ){
                if($(e).attr("maxlength")< $(e).val().length){
                    $('#'+$(e).attr("id") +'Length').css('display', 'block');
                }else{
                    $('#'+$(e).attr("id") +'Length').css('display', 'none');
                }
            }
            $('#'+$(e).attr("id")+'Validation').css('display', 'none');
        }
    }

</script>