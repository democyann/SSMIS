function checkOne(id) {
	var box = document.getElementById(id);
	if (box.checked) {
		box.checked = false;
	} else {
		box.checked = true;
	}
}

