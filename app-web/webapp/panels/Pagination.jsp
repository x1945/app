<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pagination_url" value="${requestScope['javax.servlet.forward.servlet_path']}" />

<div class="text-center">
	<ul class="pagination pagination-v2">
		<c:choose>
			<c:when test="${page.isFirstPage}">
				<li class="disabled"><a href="javascript:void(0);"><i class="fa fa-angle-left"></i></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${pagination_url}?pageSize=${page.pageSize}&pageNum=${page.prePage}"><i class="fa fa-angle-left"></i></a></li>
			</c:otherwise>
		</c:choose>
		<c:forEach items="${page.navigatepageNums}" var="val">
			<c:choose>
				<c:when test="${page.pageNum == val}">
					<li class="active"><a href="javascript:void(0);">${val}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${pagination_url}?pageSize=${page.pageSize}&pageNum=${val}">${val}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${page.isLastPage}">
				<li class="disabled"><a href="javascript:void(0);"><i class="fa fa-angle-right"></i></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${pagination_url}?pageSize=${page.pageSize}&pageNum=${page.nextPage}"><i class="fa fa-angle-right"></i></a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
