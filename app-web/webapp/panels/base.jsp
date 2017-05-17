<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="random" value="<%=Math.random()%>" />

<!-- Favicon -->
<link rel="shortcut icon" href="favicon.ico">
<!-- icon -->
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<!-- MDL  -->
<link rel="stylesheet" href="<c:url value='/assets/mdl/material.min.css'/>">
<script src="<c:url value='/assets//mdl/material.min.js'/>"></script>
<!-- MDL THEME -->
<link rel="stylesheet" href="<c:url value='/assets/css/material.blue_grey-indigo.min.css'/>">
<!-- CSS Customization -->
<link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
<link rel="stylesheet" href="<c:url value='/assets/css/custom.css'/>?r=${random}">
<!-- JS Global Compulsory -->
<script src="<c:url value='/assets/js/jquery-3.2.1.min.js'/>"></script>
<!-- init add by fantasy 2016/03/20 -->
<script src="<c:url value='/assets/init.js'/>?r=${random}"></script>