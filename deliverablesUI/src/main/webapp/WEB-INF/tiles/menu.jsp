<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="panel-content">
    <ul id="gnb">
		<li>
			<c:set var="currentTopMenuId" value='<%=session.getAttribute("currentTopMenuId") %>' />
			<c:forEach var="topMenu" items="${topMenuList}">
        		<c:set var="topMenuMappingId" value="${fn:replace(topMenu.mappingUrl, '/', '')}" />
        		<a href="#${topMenuMappingId}" class="menu-collapse-link <c:if test="${currentTopMenuId != topMenu.menuId}">collapsed</c:if>" aria-expanded="<c:choose><c:when test="${currentTopMenuId == topMenu.menuId}">true</c:when><c:otherwise>false</c:otherwise></c:choose>" data-toggle="collapse">
        		<i class="${topMenu.icon} text-dark"></i>${topMenu.menuName}<i class="icon-down text-dark"></i></a>
         		<ul id="${topMenuMappingId}" class="menu-collapse <c:choose><c:when test="${currentTopMenuId == topMenu.menuId}">show</c:when><c:otherwise>collapse</c:otherwise></c:choose>" data-parent="#gnb">
	          		<c:forEach var="subMenuMap" items="${subMenuList}">
	         			<c:if test="${topMenu.menuName == subMenuMap.key}">
	         				<c:forEach var="subMenuListMap" items="${subMenuMap.value}">
	         					<c:set var="subMenuMappingId" value="${fn:replace(subMenuListMap.mappingUrl, '/', '')}" />
	         					<li class="menu-item">
			                        <a class="menu-link" onclick="changeMenu('${subMenuMappingId}')">${subMenuListMap.menuName}</a>
			                    </li>
	         				</c:forEach>
	         			</c:if>
		          	</c:forEach>
	         	</ul>
        	</c:forEach>
        </li>
    </ul>
</div>

<form id='changeMenuForm'>
</form>

<script>
$(document).ready(function () {
	$('#changeMenuForm').submit(function() {
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

function changeMenu(subMenuMappingId){
	$("#changeMenuForm")[0].action = subMenuMappingId;
	$('#changeMenuForm').submit();
}
</script>