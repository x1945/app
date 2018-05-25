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
	$('#count').html(0);
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
//			console.log(result);
			var sr = jQuery.parseJSON(result.sr);
//			console.log(sr);
			var html = [];
			console.log(sr.hits.total);
			if (sr.hits.total > 0){
				$('#count').html(sr.hits.total);
				$.each(sr.hits.hits ,function(i, data){
					console.log(data);
					if (data._source){
						var s = /_A/.test(data._source.pid) ? '研究結案報告' : '計劃書';
						//　"計畫編號", "計畫名稱", "年度", "計畫類別", "計畫型式", "計畫主持人", "計畫主持人單位"
						html.push('<ul>');
						html.push('<li><b><t1>PID：</t1>'+data._source.pid+'</b></li>');
						html.push('<li><b><t1>計畫年度：</t1>'+data._source.yr+'年度 '+s+'</b></li>');
						html.push('<li><b><t1>計畫編號：</t1>'+data._source.cid+'</b></li>');
						html.push('<li><b><t1>計畫名稱：</t1>'+data._source.cname+'</b></li>');
						html.push('<li><b><t1>計畫類別：</t1>'+data._source.category+'('+data._source.type+')</b></li>');
//						html.push('<li><b><t1>計畫型式：</t1>'+data._source.type+'</b></li>');
						html.push('<li><b><t1>計畫主持人：</t1>'+data._source.director_name+'('+data._source.director_dept+')</b></li>');
//						html.push('<li><b><t1>計畫主持人單位：</t1>'+data._source.director_dept+'</b></li>');
						html.push('</ul>');
					}
					html.push('<p>');
//					html.push('文章'+data._id);
//					html.push('</p><p>');
					$.each(data.highlight.content, function(j, v){
						html.push(v);
					});
					html.push('</p><hr/>');
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
