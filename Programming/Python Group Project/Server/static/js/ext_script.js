dob.max = new Date().toISOString().split("T")[0];

function passwordValidate() {
	var pass = document.getElementById('password');
	var pass2 = document.getElementById('passConfirm');
	
	if(passwordScore(pass.value) < 20){
		document.getElementById('passStrength').style.color = 'red';
		document.getElementById('passStrength').innerHTML = "Password is Weak";
	}
	
	else if(passwordScore(pass.value) > 60){
		document.getElementById('passStrength').style.color = 'green';
		document.getElementById('passStrength').innerHTML = "Password is Strong";
	}
	
	else{
		document.getElementById('passStrength').style.color = 'orange';
		document.getElementById('passStrength').innerHTML = "Password is Average";
	}
	
	if (pass.value == pass2.value) {
		document.getElementById('passmsg').style.color = 'green';
		document.getElementById('passmsg').innerHTML = 'Passwords Match';
		
		
	} else {
		document.getElementById('passmsg').style.color = 'red';
		document.getElementById('passmsg').innerHTML = 'Passwords Don\'t Match';
	}
	
	if (pass.value == '') {
		document.getElementById('passmsg').innerHTML = '';
		document.getElementById('passStrength').innerHTML = '';
	}
}

function passwordScore(password) {
	var score = 0;
	
	if(!password) {
		return score;
	}
	
	var letters = new Object();
	for (var i=0; i<password.length; i++){
		letters[password[i]] = (letters[password[i]] || 0) + 1;
		score += 5.0 / letters[password[i]];
	}
	
	var variations = {
		digits: /\d/.test(password),
		lower: /[a-z]/.test(password),
		upper: /[A-Z]/.test(password),
		nonWords: /\W/.test(password),
	}
	
	variationCount = 0;
	for (var check in variations) {
		variationCount += (variations[check] == true) ? 1 : 0;
	}
	score += (variationCount -1) * 10;
	
		
	return parseInt(score);
}
