<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="random" value="<%=Math.random()%>" />

<html>
<head>
<title>Register</title>
</head>
<body>
	<div class="" style="background: #f8f8f8">
		<!-- Breadcrumbs v5 -->
		<div class="container padding-top-10">

			<ul class="breadcrumb-v5">
				<li><a href="index.html"><i class="fa fa-home"></i></a></li>
				<li class="active">Register</li>
			</ul>
		</div>
		<!-- End Breadcrumbs v5 -->
		<!--=== Content Part ===-->
		<div class="container content">
			<div class="row">
				<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
					<form id="registerForm" class="reg-page">
						<div class="reg-header">
							<h2>Register a new account</h2>
							<p>
								Already Signed Up? Click <a href="/login" class="color-green">Sign In</a> to login your account.
							</p>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label>First Name <span class="color-red">*</span></label> <input type="text" name="firstname" class="form-control margin-bottom-20" required />

							</div>
							<div class="col-sm-6">
								<label>Last Name</label> <input type="text" name="lastname" class="form-control margin-bottom-20">
							</div>
						</div>

						<label>Email Address <span class="color-red">*</span></label> <input type="email" name="email" class="form-control margin-bottom-20" required />

						<div class="row">
							<div class="col-sm-6">
								<label>Password <span class="color-red">*</span></label> <input type="password" name="password" class="form-control margin-bottom-20" required />
							</div>
							<div class="col-sm-6">
								<label>Confirm Password <span class="color-red">*</span></label> <input type="password" name="confirmPassword" class="form-control margin-bottom-20" required />
							</div>
							<div class="col-lg-12">
								<label>Address</label> <input type="text" name="address" class="form-control margin-bottom-20" placeholder="Address" />
							</div>
							<div class=" row col-lg-12">
								<div class="col-sm-6">
									<label>City</label> <input type="text" name="city" class="form-control margin-bottom-20" />
								</div>
								<div class="col-sm-2">
									<label>State</label> <input type="text" name="state" class="form-control margin-bottom-20" />
								</div>
								<div class="col-sm-4">
									<label>Zip</label> <input type="text" name="zip" class="form-control margin-bottom-20" />
								</div>
							</div>
							<div class="col-lg-12">
								<label class="font-light"> <input type="checkbox" name="agree" required> By signing you are agreeing to our <a href="">Privacy Policy</a> and <a href="">Terms of Service</a>
								</label>
							</div>
						</div>

						<div class="row">
							<hr>
							<div class="col-lg-12 text-center">
								<!-- 								<button class="btn-u btn-u-sea" type="submit">Register</button> -->
								<button id="Register" class="btn-u btn-u-sea" type="button">Register</button>
								<button id="testAjax" class="btn-u btn-u-sea" type="button">testAjax</button>
							</div>
						</div>

					</form>
				</div>
			</div>
		</div>
		<!--/container-->
		<!--=== End Content Part ===-->

	</div>

	<script src="/views/register.js?r=${random}"></script>
</body>
</html>