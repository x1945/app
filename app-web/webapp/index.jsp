<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="random" value="<%=Math.random()%>" />
<jsp:forward page="/index?r=${random}" />
