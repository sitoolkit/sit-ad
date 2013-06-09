$(function() {
	
	$("table.datatable").each(function() {
		var tbody = $(this).find("tbody");
		for(var i = 0; i < 2; i++) {
			var newTr = tbody.find("tr").clone();
			tbody.append(newTr);
			newTr.find(".documented").removeClass("documented");
		}
		
	});

	var num = 1;

	$("#logo").css("cursor", "pointer")
		.click(function() {
			if (num == 1) {
				$(".documented").each(function() {
					if ($(this).attr("id").match(/.*[2-9]$/)) {
						return;
					}
					$(this).attr("title", "(" + num++ + ")")
						.tooltip({
							position : {my: "left top", at: "right bottom" },
							tooltipClass : "numberingTooltip"
						})
						.tooltip("open");
				});
			} else {
				$(".numberingTooltip").toggle();
			}
		});
});
