function resize(pic, coeff) {
	var w = Math.round(pic.width * coeff);
	var h = Math.round(pic.height * coeff);

	if (w > 1 && h > 1 && h<1000 && w<1000) {
		pic.width = w;
		pic.heigth = h;
	}
}

function enlarge(element){
	resize(element, 1.1);
}

function decrease(element){
	resize(element, 0.9);
}
