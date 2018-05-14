<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
#ajaxloading {
	position: fixed;
	top: 0;
	width: 100%; height : 100%;
	text-align: center;
	display: none;
	z-index: 100000;
	height: 100%;
}
</style>

<div id="ajaxloading">
	<img src="<c:url value='/assets/img/ajax-loader.gif' />" />
</div>