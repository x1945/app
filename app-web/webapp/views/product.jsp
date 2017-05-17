<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="random" value="<%=Math.random()%>" />
<c:set var="page_url" value="${requestScope['javax.servlet.forward.servlet_path']}" />

<html>
<head>
<title>Product List</title>
</head>
<body>
	<!--=== End Header v5 ===-->
	<div class="" style="background: #f8f8f8">
		<!-- Breadcrumbs v5 -->
		<div class="container padding-top-10">
			<ul class="breadcrumb-v5">
				<li><a href="index.html"><i class="fa fa-home"></i></a></li>
				<li><a href="#">Clothe</a></li>
				<li class="active">New In</li>
			</ul>
		</div>

		<!-- End Breadcrumbs v5 -->
		<!--=== Content Part ===-->
		<div class="content container padding-top-0">
			<div class="row">

				<div class="col-md-12">
					<div class="row margin-bottom-5">
						<div class="col-sm-4 result-category">
							<h2>Men</h2>
							<small class="shop-rgba-dark badge-results">${page.total} Results</small>
						</div>
						<div class="col-sm-8">
							<ul class="list-inline clear-both">

								<li class="sort-list-btn">
									<h3>Sort By :</h3>
									<div class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
											All <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="#">All</a></li>
											<li><a href="#">Sales Unit -- Unit Box</a></li>
											<li><a href="#">Sales Unit -- Carton</a></li>
											<li><a href="#">Popularity</a></li>
											<li><a href="#">Best Sales</a></li>
											<li><a href="#">Top Last Week Sales</a></li>
											<li><a href="#">New Arrived</a></li>
										</ul>
									</div>
								</li>
								<li class="sort-list-btn">
									<h3>Show :</h3>
									<div class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
											${page.pageSize == 9999 ? 'All' : page.pageSize} <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="${page_url}?pageSize=9999">All</a></li>
											<li><a href="${page_url}?pageSize=20">20</a></li>
											<li><a href="${page_url}?pageSize=10">10</a></li>
										</ul>
									</div>
								</li>
							</ul>
						</div>
					</div>
					<!--/end result category-->

					<div class="filter-results">
						<div class="row illustration-v2 margin-bottom-30">
							<c:forEach items="${page.list}" var="data">
								<div class="col-md-2">
									<div class="product-img product-img-brd">
										<a href="#"><img class="full-width img-responsive" src="${data.file_url}" alt=""></a> <a class="product-review" href="product-detail.html">Quick review</a> <a class="add-to-cart"
											href="#"><i class="fa fa-shopping-cart"></i>Add to cart</a>
										<!--<div class="shop-rgba-dark-green rgba-banner">New</div> -->
									</div>
									<div class="product-description product-description-brd margin-bottom-30">
										<div class="overflow-h margin-bottom-5">
											<div class="pull-left product-name">
												<h4 class="title-price">
													<a href="product-detail.html">${data.product_name_eng}</a>
												</h4>
											</div>
											<div class="product-price">
												<span class="title-price">$${data.sell_price} / Carton</span> <span class="title-price line-through">$${data.dealar_price}</span>
											</div>
										</div>

									</div>
								</div>
							</c:forEach>
						</div>

					</div>
					<!--/end filter resilts-->

					<!-- pagination  -->
					<jsp:include page="/panels/Pagination.jsp" />
					<!--/end pagination-->
				</div>
			</div>
			<!--/end row-->
		</div>
		<!--/end container-->
		<!--=== End Content Part ===-->
	</div>

	<script src="/views/product.js?r=${random}"></script>
</body>
</html>