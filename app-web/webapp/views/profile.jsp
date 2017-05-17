<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="random" value="<%=Math.random()%>" />

<html>
<head>
<title>Profile</title>
</head>
<body>
	<link rel="stylesheet" href="assets/css/pages/profile.css">
	<link rel="stylesheet" href="assets/plugins/sky-forms-pro/skyforms/css/sky-forms.css">
	<link rel="stylesheet" href="assets/plugins/sky-forms-pro/skyforms/custom/custom-sky-forms.css"> 

	<div class="" style="background: #f8f8f8">
		<!-- Breadcrumbs v5 -->
		<div class="container padding-top-10">
			<ul class="breadcrumb-v5">
				<li><a href="index.html"><i class="fa fa-home"></i></a></li>
				<li class="active">Profile</li>
			</ul>
		</div>
		<!-- End Breadcrumbs v5 -->
		<!--=== Content Part ===-->
		<div class="container content profile">
			<div class="row">
				<!-- Profile Content -->
				<div class="col-md-9 col-md-offset-2 col-sm-8 col-sm-offset-2">
					<div class="profile-body margin-bottom-20">
						<div class="tab-v1">
							<ul class="nav nav-justified nav-tabs">
								<li class="active"><a data-toggle="tab" href="#profile">Edit
										Profile</a></li>
								<li><a data-toggle="tab" href="#passwordTab">Change
										Password</a></li>
								<li><a data-toggle="tab" href="#payment">Payment
										Options</a></li>
							</ul>
							<div class="tab-content">
								<div id="profile" class="profile-edit tab-pane fade in active">
									<form class="sky-form" id="sky-form1" action="#">
										<dl class="dl-horizontal">
											<dt>
												<strong>Your name </strong>
											</dt>										
											<dd>
												<div class="row">
													<section class="col col-10" id="sectionName">
														${member.firstname}&nbsp;&nbsp;${member.lastname}
													</section>
													<section class="col col-5" id="sectionFirstname" style="display:none">
														<label class="input">
															<input type="text" name="firstname" id="firstname" class="form-control margin-bottom-20" value="${member.firstname}" readonly required /> 
														</label>
													</section>
													<section class="col col-5" id="sectionlastname" style="display:none">
														<label class="input">
															<input type="text" name="lastname" id="lastname" class="form-control margin-bottom-20" value="${member.lastname}" readonly required /> 
														</label>
													</section>
													<section class="col col-2">
														<span>
															<a class="pull-right" href="#" id="changeName"> <i class="fa fa-pencil"></i></a>
														</span>
													</section>
												</div>
											</dd>
											<hr>
											<dt>
												<strong>Email</strong>
											</dt>
											<dd>
												<div class="row">
													<section class="col col-10" id="sectionTextEmail">
														${member.email} 
													</section>
													<section class="col col-10" id="sectionEmail" style="display:none">
														<label class="input">													
															<input type="email" name="email" id="email" class="form-control margin-bottom-20" value="${member.email}" readonly required /> 
														</label>
													</section>
													<section class="col col-2">
														<span>
															<a class="pull-right" href="#" id="changeEmail"> <i class="fa fa-pencil"></i></a>
														</span>
													</section>
												</div>
	
											</dd>
											<hr>
											<dt>
												<strong>Address </strong>
											</dt>
											<dd>										
												<div class="row">
													<section class="col col-10" id="sectionTextAddress">
														${member.address} 
													</section>
													<section class="col col-10" id="sectionAddress" style="display:none">
														<label class="input">													
															<input type="text" name="address" id="address" class="form-control margin-bottom-20" value="${member.address}" readonly required /> 
														</label>
													</section>
													<section class="col col-2">
														<span>
															<a class="pull-right" href="#" id="changeAddress"> <i class="fa fa-pencil"></i></a>
														</span>
													</section>
												</div>
											</dd>
											<hr>
										</dl>
										<div class="row text-right">
											<button type="button" id="btnCancelTab1" class="btn-u btn-u-default">Cancel</button>
											<button type="button" id="btnSaveChangeTab1" class="btn-u btn-u-sea">Save Changes</button>
											<!-- <input type="hidden" value="${member.org_uuid}" id="org_uuid" name="org_uuid"/>
											<input type="hidden" value="${member.member_uuid}" id="member_uuid" name="member_uuid"/>
											 -->
										</div>
									</form>
								</div>

								<div id="passwordTab" class="profile-edit tab-pane fade">
									<h2 class="heading-md">Manage your Security Settings</h2>
									<p>Change your password.</p>
									<br>
									<form class="sky-form" id="sky-form2" action="#">
										<dl class="dl-horizontal">
											<dt>Email address</dt>
											<dd>
												<section>
													<label class="input"> <i
														class="icon-append fa fa-envelope"></i> <input
														type="email" placeholder="Email address" name="email" id="email" required>

													</label>
												</section>
											</dd>
											<dt>Enter your password</dt>
											<dd>
												<section>
													<label class="input"> <i
														class="icon-append fa fa-lock"></i> <input type="password"
														id="password" name="password" placeholder="Password" required >

													</label>
												</section>
											</dd>
											<dt>Confirm Password</dt>
											<dd>
												<section>
													<label class="input"> <i
														class="icon-append fa fa-lock"></i> <input type="password" id="confirmPassword"
														name="confirmPassword" placeholder="Confirm password" required >

													</label>
												</section>
											</dd>
										</dl>
										<div class="row text-right">
											<button type="button" id="btnCancelTab2" class="btn-u btn-u-default">Cancel</button>
											<button type="button" id="btnSaveChangeTab2" class="btn-u btn-u-sea">Save Changes</button>
											<!-- <input type="hidden" value="${member.org_uuid}" id="org_uuid" name="org_uuid"/>
											<input type="hidden" value="${member.member_uuid}" id="member_uuid" name="member_uuid"/>
 -->
										</div>
									</form>
								</div>

								<div id="payment" class="profile-edit tab-pane fade">
									<h2 class="heading-md">Manage your Payment Settings</h2>
									<p>Below are the payment options for your account.</p>
									<br>
									<form class="sky-form" id="sky-form3" action="#">
										<!--Checkout-Form-->
										<section>
											<div class="inline-group">
												<label class="radio"><input type="radio" checked=""
													name="payment_type" id="payment_type" value="1" required><i class="rounded-x"></i>Visa</label> <label
													class="radio"><input type="radio"
													name="payment_type" id="payment_type" value="2" required><i class="rounded-x"></i>MasterCard</label>
												<label class="radio"><input type="radio"
													name="payment_type" id="payment_type" value="3" required><i class="rounded-x"></i>PayPal</label>
											</div>
										</section>

										<section>
											<label class="input"> <input type="text" id="name_on_card" name="name_on_card"
												placeholder="Name on card" required>
											</label>
										</section>

										<div class="row">
											<section class="col col-2">
												<label class="input"> <input type="text" id="card_no1" name="card_no1"
													placeholder="Card number" required>
												</label>
											</section>
											<section class="col col-2">
												<label class="input"> <input type="text" id="card_no2" name="card_no2"
													placeholder="Card number">
												</label>
											</section>
											<section class="col col-2">
												<label class="input"> <input type="text" id="card_no3" name="card_no3"
													placeholder="Card number" required>
												</label>
											</section>
											<section class="col col-2">
												<label class="input"> <input type="text" id="card_no4" name="card_no4"
													placeholder="Card number" required>
												</label>
											</section>
											<section class="col col-2">
											</section>
											<section class="col col-2">
												<label class="input"> <input type="text" name="cvv2"
													id="cvv2" placeholder="CVV2" required>
												</label>
											</section>
										</div>

										<div class="row">
											<label class="label col col-4">Expiration date</label>
											<section class="col col-5">
												<label class="select"> <select name="valid_month" id="valid_month" required>
														<option disabled selected="" value="0">--Month--</option>
														<option value="01">January</option>
														<option value="02">February</option>
														<option value="03">March</option>
														<option value="04">April</option>
														<option value="05">May</option>
														<option value="06">June</option>
														<option value="07">July</option>
														<option value="08">August</option>
														<option value="09">September</option>
														<option value="10">October</option>
														<option value="11">November</option>
														<option value="12">December</option>
												</select> <i></i>
												</label>
											</section>
											<section class="col col-3">
												<label class="input"> <input type="text"
													placeholder="Year" id="valid_year" name="valid_year" required>
												</label>
											</section>
										</div>
										<div class="row text-right">
											<button type="button" id="btnCancelTab3" class="btn-u btn-u-default">Cancel</button>
											<button type="button" id="btnSaveChangeTab3" class="btn-u btn-u-sea">Save Changes</button>
											<!-- <input type="hidden" value="${member.org_uuid}" id="org_uuid" name="org_uuid"/>
											<input type="hidden" value="${member.member_uuid}" id="member_uuid" name="member_uuid"/>
											 -->

										</div>
										<!--End Checkout-Form-->
									</form>
								</div>


							</div>
						</div>
					</div>
				</div>
				<!-- End Profile Content -->
			</div>
			<!--/row-->
		</div>
		<!--/container-->
		<!--=== End Content Part ===-->
	</div>

	<script src="/views/profile.js?r=${random}"></script>
</body>
</html>