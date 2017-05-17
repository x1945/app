//onload
$(function() {
	$('#Register').click(function() {
		if ($('#registerForm').valid()) {
			$.confirm($.i18n.confirm.sure, function(result) {
				if (result) {
					$.ajax({
						type : 'POST',
						url : '/register/add',
						data : $('#registerForm').toJSON(),
						success : function(result) {
							$.success($.i18n.register.success, function() {
								// 導向登入頁
								location.href = '/login';
							});
						}
					});
				}
			});
		}
	});

	$('#testAjax').click(function() {
		$.ajax({
			// type : 'POST',
			url : '/register/test',
			data : {
				test : '測試'
			},
			success : function(result) {
				$.success('測試成功！', function() {
					// 導向登入頁
					// location.href = '/login';
				});
			}
		});
	});
	// $.success('執行成功！', function() {
	// $.error('執行失敗！ xxxxxxx xxxxxxxxxx');
	// });
	// $.error('執行失敗！ xxxxxxx xxxxxxxxxx xxxxxxxxxxx xxxxxxxxxxxx');
});