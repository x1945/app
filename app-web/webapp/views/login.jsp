<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Login</title>
</head>
<body>
	<div class="" style="background: #f8f8f8">
		<!-- Breadcrumbs v5 -->
		<div class="container padding-top-10">

			<ul class="breadcrumb-v5">
				<li><a href="/index"><i class="fa fa-home"></i></a></li>
				<li class="active">Login</li>
			</ul>
		</div>
		<!-- End Breadcrumbs v5 -->
		<!--=== Content Part ===-->
		<div class="container content">
			<div class="row">
				<div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
					<form id="loginForm" name="loginForm" class="reg-page"
						method="POST" action="/j_spring_security_check">
						<div class="reg-header">
							<h2>Login to your account</h2>
						</div>

						<div class="input-group margin-bottom-20">
							<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="email" name="j_username" placeholder="Email"
								class="form-control" required />
						</div>
						<div class="input-group margin-bottom-20">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input type="password" name="j_password" placeholder="Password"
								class="form-control" required />
						</div>

						<div class="input-group">
							<label class="font-light"><input type="checkbox"
								name="remember-me" /> Keep me logged in</label>
						</div>
						<div class="col-lg-12 text-center">
							<button class="btn-u text-center btn-u-sea" type="submit">Login</button>
						</div>

						<hr>
						<c:if test="${param.failure != null}">
							<span class="text-error"><spring:message code="loginFailure" /></span>
						</c:if>
						<h4>Forget your Password ?</h4>
						<p>
							no worries, <a data-toggle="modal" href="#myModal">click here</a>
							to reset your password.
						</p>
					</form>
				</div>
			</div>
			<!--/row-->
		</div>
		<!--/container-->
		<!--=== End Content Part ===-->
	</div>

	<!-- Modal -->
	<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog"
		tabindex="-1" id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Forgot Password ?</h4>
				</div>
				<div class="modal-body">
					<p>Enter your e-mail address below to reset your password.</p>
					<input type="text" name="email" placeholder="Email"
						autocomplete="off" class="form-control placeholder-no-fix">

				</div>
				<div class="modal-footer">
					<button data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
					<button class="btn btn-success" type="button">Submit</button>
				</div>
			</div>
		</div>
	</div>
	<!-- modal -->
</body>
</html>