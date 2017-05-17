(function(window, $) {
	// 擴充jQuery功能
	$.extend($.fn, {
		toJSON : function() {
			var o = {};
			var a = this.serializeArray();
			$.each(a, function() {
				if (o[this.name]) {
					if (!o[this.name].push) {
						o[this.name] = [ o[this.name] ];
					}
					o[this.name].push(this.value || '');
				} else {
					o[this.name] = this.value || '';
				}
			});
			return o;
		}
	});

	// 擴充jQuery功能
	$.extend($, {
		success : function(msg, callback) {
			var title = '<span class="text-success" >';
			title += '<span class="glyphicon glyphicon-ok-sign" />';
			title += ' <b>' + $.i18n.title.success + '</b>';
			title += '</span>';
			setTimeout(function() {
				bootbox.alert({
					locale : $.i18n.locale,
					title : title,
					message : '<span class="modal-msg text-success">' + msg
							+ '<span>',
					callback : callback
				});
			}, $('.bootbox').size() > 0 ? 500 : 1);
		},
		error : function(msg, callback) {
			var title = '<span class="text-danger" >';
			title += '<span class="glyphicon glyphicon-exclamation-sign" />';
			title += ' <b>' + $.i18n.title.error + '</b>';
			title += '</span>';
			setTimeout(function() {
				bootbox.alert({
					locale : $.i18n.locale,
					title : title,
					message : '<span class="modal-msg text-danger">' + msg
							+ '<span>',
					callback : callback
				});
			}, $('.bootbox').size() > 0 ? 500 : 1);
		},
		confirm : function(msg, callback) {
			var title = '<span class="text-primary" >';
			title += '<span class="glyphicon glyphicon-question-sign" />';
			title += ' <b>' + $.i18n.title.confirm + '</b>';
			title += '</span>';
			bootbox
					.confirm({
						locale : $.i18n.locale,
						title : title,
						message : '<span class="modal-msg text-info">' + msg
								+ '<span>',
						callback : callback
					});
		}
	});

	/**
	 * jqeury extend bootbox (alert,confirm,prompt,dialog)
	 */
	// $.extend($, bootbox);
	/**
	 * jqeury extend toastr (info,success,error,warning)
	 */
	// $.extend($, toastr);
	/**
	 * fix jQuery's clone()
	 */
	(function(original) {
		jQuery.fn.clone = function() {
			var result = original.apply(this, arguments), my_textareas = this
					.find('textarea').add(this.filter('textarea')), result_textareas = result
					.find('textarea').add(result.filter('textarea')), my_selects = this
					.find('select').add(this.filter('select')), result_selects = result
					.find('select').add(result.filter('select'));

			for (var i = 0, l = my_textareas.length; i < l; ++i)
				$(result_textareas[i]).val($(my_textareas[i]).val());
			for (var i = 0, l = my_selects.length; i < l; ++i) {
				for (var j = 0, m = my_selects[i].options.length; j < m; ++j) {
					if (my_selects[i].options[j].selected === true) {
						result_selects[i].options[j].selected = true;
					}
				}
			}
			return result;
		};
	})(jQuery.fn.clone);

	// 預設的初始化動作
	$(function() {
		// setting toastr
		// toastr.options = {
		// progressBar : true,
		// closeButton : true,
		// timeOut : 3000
		// }

		// set jquery ajax
		$.ajaxSetup({
			// headers : {
			// 'X-CSRF-Token' : $('meta[name="csrf-token"]').attr('content')
			// },
			type : 'GET',
			cache : false,
			dataType : 'json',
			beforeSend : function(jqXHR, settings) {
				var ac = $('body').data('ajaxCount') || 0;
				$('body').data('ajaxCount', ++ac);
				$('#ajaxloading').show();
			},
			complete : function(jqXHR, textStatus) {
				var ac = $('body').data('ajaxCount') || 0;
				if (--ac <= 0)
					$('#ajaxloading').hide();
				$('body').data('ajaxCount', ac);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('jqXHR:', jqXHR);
				try {
					var json = $.parseJSON(jqXHR.responseText);
					console.log('json:', json);
					$.error(json.message);
				} catch (e) {
					$.error(textStatus + '<br/>' + errorThrown);
				}
			},
			success : function(data, textStatus) {
				$.success(textStatus);
			}
		});

		$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		});

		// set i18n
		// bootbox.setDefaults('locale', $.i18n.locale);
	});

	// beforeunload
	$(window).bind('beforeunload', function(e) {
		$('#ajaxloading').show();
	});

})(window, jQuery);