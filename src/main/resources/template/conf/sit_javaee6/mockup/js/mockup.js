$(function() {
	var num = 1;
	
		
	var showHoverBox = function (jqo) {
		var hoverBoxStyle = function(jqo) {
			return "left:" + (jqo.position().left - 4) + "px;" +
			";top:" + (jqo.position().top - 4) + "px;" +
			"width:" + (jqo.outerWidth() + 4 ) + "px;" + 
			"height:" + (jqo.outerHeight() + 4) + "px;";
		};
		var style = hoverBoxStyle(jqo);
		$("body").append("<div class='shownum-hover-box' style='" + style + "'><em class='shownum'>(" + num++ + ")</em></div>");

		$("#header").click(function() {
			$("div.shownum-hover-box").toggle();
		});

	};
	
	var showWrapBox = function(jqo) {
		jqo.wrap("<span class='shownum-wrap-box'></span>").after("<em class='shownum'>(" + num++ + ")</em>");
	};
	
	$(".shownum").each(function() {
//		showHoverBox($(this));
		showWrapBox($(this));
	});
	
	$("#header").click(function() {
		$("span.shownum-wrap-box").toggleClass("shownum-wrap-box-off");
		$("em.shownum").toggle();
	});
	
	$("#header").click();

	
	$("table.datatable").each(function() {
		var tbody = $(this).find("tbody");
		for(var i = 0; i < 2; i++) {
			tbody.append(tbody.find("tr").clone());
		}
		
	});
});