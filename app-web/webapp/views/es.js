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

function keyEvent(event) {
	var key = event.keyCode || event.which;
	if (key == 13) {
		elasticSearch($('#search').val());
	}
}

function elasticSearch(word){
	console.log('serach:'+word);
	$.ajax({
		type : 'POST',
		url : '/app/es/search',
		data : {
			word : word || ''
		},
		success : function(result) {
//			$.success('測試成功！', function() {
				// 導向登入頁
				// location.href = '/login';
//			});
			console.log(result);
			var sr = jQuery.parseJSON(result.sr);
			console.log(sr);
			var html = [];
			console.log(sr.hits.total);
			if (sr.hits.total > 0){
				$.each(sr.hits.hits ,function(i, data){
					console.log(i)
					html.push('<p>');
					html.push('文章'+data._id);
					html.push('</p><p>');
					$.each(data.highlight.content, function(j, v){
						html.push(v);
					});
					html.push('</p>');
				});
			}else{
//				html.push('<p>查無資料，可試著用以下關鍵字查詢</p>');
				$.each(result.analyze || [], function(i, v){
					if (v && v.length >= 2)
						html.push('<a href="javascript:void(0)" onclick="elasticSearch(\''+v+'\');">'+v+'</a><br/>');
				});
				if (html.length > 0){
					html.unshift('<p>查無資料，可試著用以下關鍵字查詢。</p>');
				}else{
					html.push('<p>查無資料。</p>');
				}
			}
			console.log(html.join(''));
			$('#searchReslut').html(html.join(''));
		}
	});
}
