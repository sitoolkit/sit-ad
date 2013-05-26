$(function() {
	$("#menu").find("li ul").hide();
	$("#menu").find("li:has(ul) > a").prepend("<span class='menubtn'></span>");
	$("#menu").find("li:not(:has(ul)) > a").prepend("<span class='menuspace'></span>");

	$("span.menubtn").click(function() {
		$(this).blur();

			var target = $(this).parent().parent().find("ul");
			if(target.is(":visible")) {
				target.slideUp("fast");
				$(this).removeClass("menuopen");
			} else {
				target.slideDown("fast");
				$(this).addClass("menuopen");
			}

		return false;
	});
	
	$("a[href='']").click(function() {
		$(this).blur().find("span.menubtn").click();
		return false;
	});
});
