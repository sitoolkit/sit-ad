$(function() {
	$("body").prepend("<input id='reload' type='checkbox' checked='checked' accesskey='r'/>"
			+ "<label for='reload'>auto reload(<u>R</u>)</label>");
	$("#reload").click(function() {
		reload();
	});
	setTimeout("reload();", 2000);
});

function reload() {
	if ($("#reload").is(":checked")) {
		window.location.reload();
	}
}