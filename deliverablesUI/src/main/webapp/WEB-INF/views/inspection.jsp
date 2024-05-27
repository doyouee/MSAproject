<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<body>
    <section class="card">
        <header class="card-header">
            <h2 class="card-title">
                <span class="i-rounded bg-danger"><i class="icon-ul"></i></span>검수
            </h2>
        </header>
            <c:import url="/WEB-INF/views/projectList.jsp"></c:import>
    </section>
</body>

<script>
	$(document).ready(function () {
        if('${search}' != ''){
            var search = JSON.parse('${search}')
            $.each(search,function(key,value) {
                $("#"+key+"Input").val(value);
            });

        }
		$('#inspectionForm').submit(function() {
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
		$('.layerPopup')[0].style.display='none';
	});


</script>

