<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="random" value="<%=Math.random()%>" />
<c:set var="page_url" value="${requestScope['javax.servlet.forward.servlet_path']}" />

<html>
<head>
<title>Page List</title>
</head>
<body>
	<table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
		<thead>
			<tr>
				<th class="mdl-data-table__cell--non-numeric">ID</th>
				<th class="mdl-data-table__cell--non-numeric">姓名</th>
				<th class="mdl-data-table__cell--non-numeric">電子郵件</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="row">
				<tr>
					<td class="mdl-data-table__cell--non-numeric">${row.id}</td>
					<td class="mdl-data-table__cell--non-numeric">${row.name}</td>
					<td class="mdl-data-table__cell--non-numeric">${row.email}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<script src="<c:url value='/views/page.js'/>?r=${random}"></script>
</body>
</html>