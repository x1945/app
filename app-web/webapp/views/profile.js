//onload
$(function() {
	//Tab1===========
	$('#changeName').click(function() {
		//第一個tab,change Name
		$("#sectionName").css("display" , "none");
		$("#sectionFirstname").css("display" , "");
		$("#sectionlastname").css("display" , "");
				
		$("#firstname").attr("readonly" , false);
		$("#lastname").attr("readonly" , false);
		$('#changeName').hide();
	});
	
	$('#changeEmail').click(function() {
		//第一個tab,change email
		$("#sectionTextEmail").css("display" , "none");
		$("#sectionEmail").css("display" , "");		
		
		$("#sky-form1 #email").attr("readonly" , false);
		$('#changeEmail').hide();
	});
	
	$('#changeAddress').click(function() {
		//第一個tab,change address
		$("#sectionTextAddress").css("display" , "none");
		$("#sectionAddress").css("display" , "");		
		
		$("#address").attr("readonly" , false);
		$('#changeAddress').hide();
	});
	
	$('#btnCancelTab1').click(function() {

		//第一個tab,change Name
		$("#sectionName").css("display" , "");
		$("#sectionFirstname").css("display" , "none");
		$("#sectionlastname").css("display" , "none");		
		$("#firstname").attr("readonly" , true);
		$("#lastname").attr("readonly" , true);
		$('#changeName').show();
		//第一個tab,change email
		$("#sectionTextEmail").css("display" , "");
		$("#sectionEmail").css("display" , "none");
		$("#sky-form1 #email").attr("readonly" , true);
		$('#changeEmail').show();
		//第一個tab,change address
		$("#sectionTextAddress").css("display" , "");
		$("#sectionAddress").css("display" , "none");
		
		$("#address").attr("readonly" , true);
		$('#changeAddress').show();
		
	});
	
	$('#btnSaveChangeTab1').click(function() {
		if ($('#sky-form1').valid()) {
			$.confirm('Are you sure?', function(result) {
				if (result) {
					$.ajax({
						type : 'POST',
						url : '/profile/save',
						data : $('#sky-form1').toJSON(),
						success : function(result) {
							$.success('儲存成功！', function() {
								// 重load本頁
								location.href = '/profile';
							});
						}
					});
				}
			});
		}
	});
	
	//Tab2===========
	$('#btnSaveChangeTab2').click(function() {		
		if ($('#sky-form2').valid()) {
			$.confirm('Are you sure?', function(result) {
				if (result) {
					$.ajax({
						type : 'POST',
						url : '/profile/changePassword',
						data : $('#sky-form2').toJSON(),
						success : function(result) {
							$.success('修改密碼成功，請重新登入！', function() {
								// 執行登出...暫用reload
								location.href = '/logout';
							});
						}
					});
				}
			});
		}
	});

	$('#btnCancelTab2').click(function() {

		$("#sky-form2 #email").val("");
		$("#password").val("");
		$("#confirmPassword").val("");
		
	});
	
	//Tab3===========
	$('#btnSaveChangeTab3').click(function() {		
		if ($('#sky-form3').valid()) {
			$.confirm('Are you sure?', function(result) {
				if (result) {
					$.ajax({
						type : 'POST',
						url : '/profile/setPayment',
						data : $('#sky-form3').toJSON(),
						success : function(result) {
							$.success('儲存成功！', function() {
								//reload
								location.href = '/profile';
							});
						}
					});
				}
			});
		}
	});
	
	$('#btnCancelTab3').click(function() {
		$("input:radio[id='payment_type'][value='Visa']").attr('checked', true);
		$("#name_on_card").val("");
		$("#card_no1").val("");
		$("#card_no2").val("");
		$("#card_no3").val("");
		$("#card_no4").val("");
		$("#cvv2").val("");
		$("#valid_month").val("0");
		$("#valid_year").val("");
		
	});
});