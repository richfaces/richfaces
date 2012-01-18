function resize(id, coeff) {
	var pic = document.getElementById(id);

	var w = Math.round(pic.width * coeff);
	var h = Math.round(pic.height * coeff);

	if (w > 1 && h > 1 && h<1000 && w<1000) {
		pic.width = w;
		pic.heigth = h;
	}
}

function enlarge(id){
	resize(id, 1.1);
}

function decrease(id){
	resize(id, 0.9);
}
