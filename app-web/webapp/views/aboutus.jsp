<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="random" value="<%=Math.random()%>" />

<html>
<head>
<title>About Us and Contact</title>
</head>
<body>
	<div class="" style="background:#f8f8f8">
		<!-- Breadcrumbs v5 -->
		<div class="container padding-top-10">
		
		    <ul class="breadcrumb-v5">
		        <li><a href="index.html"><i class="fa fa-home"></i></a></li>
		        <li class="active">About Us and Contact</li>
		    </ul> 
		</div>
		<!-- End Breadcrumbs v5 -->
	    <!--=== Content Part ===-->
	    <div class="container content profile">
	    	<div class="row">
	             <!-- Profile Content -->
				 <div class="col-md-1">&nbsp;
				 </div>
	            <div class="col-md-8">
	                <div class="headline"><h2>About Us</h2></div>
					 <div class="col-md-3"><img src="${bean.logo_file}" class="img-responsive"></div>
					 <p>${bean.introduction}</p>
					<!-- <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas feugiat. Et harum quidem rerum facilis est et expedita distinctio lorem ipsum dolor sit amet, consectetur adipiscing elit landitiis.</p>
	                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna. Sed et quam lacus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas feugiat.</p>
	                 -->
					<div class="headline"><h2>Our Brand</h2></div>
					 <div class="col-md-3"><img src="${bean.brand_file}" class="img-responsive"></div>
					 <p>${bean.brand}</p>
					<!-- <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas feugiat. Et harum quidem rerum facilis est et expedita distinctio lorem ipsum dolor sit amet, consectetur adipiscing elit landitiis.</p>
	                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna. Sed et quam lacus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas feugiat.</p>
	                -->
	                
	            </div><!--/col-md-9-->
	    		<div class="col-md-3">
	            	<!-- Contacts -->
	                <div class="headline"><h2>Contacts</h2></div>
	                <ul class="list-unstyled margin-bottom-30">
						<li>${bean.org_name}</li>
	                    <li><i class="fa fa-home"></i>&nbsp;&nbsp;${bean.org_addr}</li>
	                    <li><i class="fa fa-phone"></i>&nbsp;&nbsp;${bean.org_tel}</li>
	                    <li><a href="mailto:info@wegoluck.com"><i class="fa fa-envelope"></i>&nbsp;&nbsp;${bean.email}</a></li>
	                    <li><a href="#" target="_new"><i class="fa fa-globe"></i>&nbsp;&nbsp;${bean.web_url}</a></li>
	                </ul>	
	            </div><!--/col-md-3-->			
	            <!-- End Profile Content -->
	        </div><!--/row-->
	    </div><!--/container-->
	    <!--=== End Content Part ===-->
	</div>

</body>
</html>