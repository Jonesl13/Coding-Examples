from flask import Flask, render_template, request, redirect, url_for, render_template_string
#from OpenSSL import SSL #For TLS connection
from appHandshake import handshake, encryption, decryption
#context = SSL.Context(SSL.TLSv1_2_METHOD)
#context.use_certificate('mycert.crt')
#context.use_privatekey('myprivatekey.key')

import password_checker, requests
#password_checker.statChecker('')

import json

app = Flask(__name__)
MyID, key = handshake()
jwt = ""

@app.route('/', methods=['GET','POST'])
def hello():
	return redirect(url_for("home"))

@app.route('/home', methods=['GET','POST'])
def home():
	return render_template('index.html')

@app.route('/login')
def login():
	return render_template('login.html')

@app.route('/error')
def error():
	return render_template('error_page.html')

@app.route('/forgotpw')
def forgotpw():
	return render_template('forgot_pw.html')

@app.route('/forgot_success')
def forgot_success():
	return render_template('forgot_success.html')

@app.route('/changepw_success')
def changepw_success():
	return render_template('changepw_success.html')

@app.route('/update_password')
def update_password():
	try:
		if jwt=="":
			raise Exception
		return render_template('update_pass.html')

	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/updating', methods=["POST"])
def updating():

	print("qwertyuiopoiuytsafghjkl;")
	try:
		password = request.form['passConfirm']
		content={"newPassword":password}
		content = json.dumps(content)
		print(content)
		response = requests.request("POST","http://127.0.0.1:3864/updatePassword",headers={"id":MyID, "JWT":jwt},data=content)
		print(response.text)
		return redirect(url_for('login'))
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/forgot_pw', methods=["POST"])
def forgotpwcheck():
	try:
		input = request.form['username']
		print("User input for username: " + input)
		#For check user
		response = requests.request("POST","http://127.0.0.1:3864/existUser/" + input)
		userExist = response.text
		#Status = json.loads(json.loads(response.text))
		if userExist == 'True':
			#Redirect to the success page
			response = requests.request("POST","http://127.0.0.1:3864/forgotPasswordEmail/" + input)
			Status = json.loads(json.loads(response.text))
			if Status["Success"] == True:
				return redirect(url_for('forgot_success'))
			else:
				return redirect(url_for('error'))
		else:
			return redirect(url_for('error'))		
				
	except Exception as e:
		print(e)
		
		#response = requests.request("POST","https://127.0.0.1:3864/forgotPassword",message="Error")
		return 

@app.route('/forgot_pass/<string:token>')
def forgot_pass(token):
	try:
		return render_template_string('''
		{%extends 'menu_bar.html'%}
        <head>
		<link rel="stylesheet" href="static/css/index.css">
		<script src="static/js/ext_script.js"></script>
		</head>
        {%block title%}Forgot Password{%endblock%}
        {%block content%}
        <header>Changing Password</header><br>
        <div style="width:400px; margin:auto">
	        <form action="/forgotpwsuccess/{{token}}" method="post" class="form">
		    <div class="field">Password: <input type="password" placeholder="Enter password" id="password" name="password"  onkeyup='passwordValidate();' required></div>
		    <span id='passStrength'></span>
		    <div class="field">Confirm Password: <input type="password" placeholder="Enter password" id="passConfirm" name="passConfirm" onkeyup='passwordValidate();' required></div> 
		    <span id='passmsg'></span>

		    <div class="field" style="text-align:center"><input class="button" type="submit" value="Submit"></div>
            </form>
        </div>
        {%endblock%}
	    ''', token = token)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/forgotpwsuccess/<string:token>', methods=["POST"])
def forgotpwsuccess(token):
	try:
		input = request.form["passConfirm"]
		print(input)
		content = {"newPassword":input}
		print(content)
		content = json.dumps(content)
		print(content)
		response = requests.request("POST","http://127.0.0.1:3864/forgot_change/" + token,data=content)
		response = json.loads(json.loads(response.text))

		if response["Success"] == True:
			return redirect(url_for('changepw_success'))
		else:
			return redirect(url_for('error'))
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/logging', methods=["POST"])
def logging():
	try:
		username = request.form['username']
		password = request.form['password']
		content={"username":username, "password":password}
		content = json.dumps(content)
		print(content)
		#content = encryption(content,key)
		response = requests.request("POST","http://127.0.0.1:3864/login",headers={"id":MyID},data=content)
		#TODO check the "Success" then go to the one time password page
		print(response.text) 
		response = json.loads(json.loads(response.text))
		print(response["Success"])
		if str(response["Success"]) == "False":
			return loginError(response["Error"])
		#TODO else go beck to login
		return redirect(url_for("one_time_passcode"))
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

def loginError(error):
	return render_template_string('''{%extends 'loginError.html'%}
	{%block error%}{{error}}{%endblock%}
	''',error = error)

@app.route('/menu',methods=["GET"])
def menu():
	
	print("Menu::"+jwt)
	print("ID::"+MyID)
	try:
		response = requests.request("GET", "http://127.0.0.1:3864/data", headers={"id":MyID, "JWT":jwt})
		labels = json.loads(response.text)
	
		return render_template_string('''
		{%extends 'menu_bar.html'%}
		{%block title%}Medical Records{%endblock%}
		{%block content%}
		<header>Medical Records</header><br>
		{% for title,value in labels.items() %}
		<form action="/{{title}}" method="get" class="form">
			<div class="field" style="text-align:center"><input class="button" type="submit" value="{{value}}"></div>
			</form>
		{% endfor %}
		{%endblock%}
		''', labels=labels)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))


@app.route('/<string:username>', methods=["GET"])
def get_records(username):
	try:
		response = requests.request("GET","http://127.0.0.1:3864/data/"+username,headers={"id":MyID, "JWT":jwt})
		print(response.text)
		UserDict = json.loads(response.text)
		return render_template("PersonalPage.html", labels=UserDict, username = username)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/create_record',methods=["GET"])
def create_record():
	
	print("Menu::"+jwt)
	print("ID::"+MyID)
	try:
		response = requests.request("GET", "http://127.0.0.1:3864/data", headers={"id":MyID, "JWT":jwt})
		labels = json.loads(response.text)
	
		return render_template_string('''
		{%extends 'menu_bar.html'%}
		{%block title%}Create Record{%endblock%}
		{%block content%}
		<header>Clients</header><br>
		{% for title,value in labels.items() %}
		<form action="/create_record/{{title}}" method="get" class="form">
			<div class="field" style="text-align:center"><input class="button" type="submit" value="{{value}}"></div>
			</form>
		{% endfor %}
		{%endblock%}
		''', labels=labels)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))


@app.route('/create_record/<string:username>', methods=["GET"])
def new_record(username):
	try:
		
		return render_template_string('''
		{%extends 'menu_bar.html'%}
		{%block title%}Create Record{%endblock%}
		{%block content%}
		<header>New record for {{username}}</header><br>
		
		<form action="/send_record/{{username}}" method="post" class="form" id="usrform">
			<div class="field" style="text-align:center"><input class="button" type="submit" value="Submit Record"></div>
		</form>
		<textarea rows="4" cols="50" name="reporttext" form="usrform" id="reporttext"></textarea>
	
		{%endblock%}
		''', username = username)

	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route('/send_record/<string:username>', methods=["POST"])
def send_record(username):
	try:
		
		reporttext = request.form['reporttext']
		
		newReport = {
			"username": username,
			"report": reporttext,
		}
		
		token = json.dumps(newReport)
		
		encrypt = encryption(token, key)
		
		response = requests.request("POST", "http://127.0.0.1:3864/data/update_record/"+username, headers={"id":MyID}, data=encrypt)
		print(int(response.status_code))
	
		if response.status_code != 200:
				return redirect(url_for('error'))
		reply = response.text
		print(type(reply))
		return redirect(url_for("home"))
		
		
	except Exception as e:
		print(e)
		return redirect(url_for('error'))
		
@app.route('/request_permission', methods=["GET"])
def request_permission():
	try:
		
		print("Menu::"+jwt)
		print("ID::"+MyID)
	
		response = requests.request("GET", "http://127.0.0.1:3864/data", headers={"id":MyID, "JWT":jwt})
		labels = json.loads(response.text)
	
		return render_template_string('''
		{%extends 'menu_bar.html'%}
		{%block title%}New Request{%endblock%}
		{%block content%}
		<header>New Request</header><br>
		{% for title,value in labels.items() %}
		<form action="/new_request/{{title}}" method="get" class="form">
			<div class="field" style="text-align:center"><input class="button" type="submit" value="{{value}}"></div>
			</form>
		{% endfor %}
		{%endblock%}
		''', labels=labels)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))
	
	
@app.route('/new_request/<string:username>', methods=["GET"])
def new_request(username):
	try:
		return render_template("request_permission.html", username = username)
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

		
@app.route('/send_request/<string:username>',methods=["POST"])
def send_request(username):
	
	try:
		request = request.form['request']
		
		newReport = {
			"username": username,
			"request": request,
		}
		
		token = json.dumps(newReport)
		
		encrypt = encryption(token, key)
		
		response = requests.request("POST", "http://127.0.0.1:3864/data/new_request/"+username, headers={"id":MyID}, data=encrypt)
		#TODO check return of response (return back to register page if needed)
		print(int(response.status_code))
	
		if response.status_code != 200:
				return redirect(url_for('error'))
		reply = response.text
		print(type(reply))
		return redirect(url_for("home"))
		
		
	except Exception as e:
		print(e)
		return redirect(url_for('error'))



@app.route('/register')
def register():
	return render_template('register.html')

@app.route('/one_time_passcode', methods=['GET','POST'])
def one_time_passcode():
	try:
		if request.method == 'POST':
			input = request.form['onetimepass']
			response = requests.request("POST", "http://127.0.0.1:3864/confirm/OneTimeCode/"+input)
			Status = json.loads(json.loads(response.text))
			#TODO if it went pass it will then go back to menu with jwt 
			#TODO if true
			if Status["Success"]==True:
				#TODO save jwt
				global jwt
				jwt = Status["JWT"]
				#TODO redirect to the medical records
				return redirect(url_for('menu'))
			#TODO else if False
			#TODO use loginError(error) to render the page
			return loginError(Status["Error"])	
			#return redirect(url_for('menu'))
		if request.method == 'GET':
			return render_template('otp.html')
	except Exception as e:
		print(e)
		return redirect(url_for('error'))
		
		
@app.route('/registering', methods=['POST'])
def registering():	
	try:
		if request.method == 'POST':
			firstname = request.form['firstname']
			lastname = request.form['lastname']
			dob = request.form['dob']
			username = request.form['username']
			email = request.form['email']
			password = request.form['password']
			firstquestion = request.form['firstquestion']
			firstanswer = request.form['firstanswer']
			secondquestion = request.form['secondquestion']
			secondanswer = request.form['secondanswer']
			thirdquestion = request.form['thirdquestion']
			thirdanswer = request.form['thirdanswer']
	
			newUser = {
				"firstname": firstname,
				"lastname": lastname,
				"dob": dob,
				"username": username,
				"email": email,
				"password": password,
				"firstquestion": firstquestion,
				"firstanswer": firstanswer,
				"secondquestion": secondquestion,
				"secondanswer": secondanswer,
				"thirdquestion": thirdquestion,
				"thirdanswer": thirdanswer
			}
		
			token = json.dumps(newUser)
		
			encrypt = encryption(token, key)
		
			response = requests.request("POST", "http://127.0.0.1:3864/register", headers={"id":MyID}, data=encrypt)
			#TODO check return of response (return back to register page if needed)
			print(int(response.status_code))
			if response.status_code != 200:
				return redirect(url_for("register"))
			send_email = requests.request("POST", "http://127.0.0.1:3864/send_email/"+username)

			reply = response.text
			print(type(reply))
			return redirect(url_for('login'))
		
		return redirect(url_for('login'))
	except Exception as e:
		print(e)
		return redirect(url_for('error'))

@app.route("/logout")
def logout():
	global jwt
	jwt = ""
	return redirect(url_for("home"))

if __name__== "__main__":
    app.run(debug=True, ssl_context=('server.crt','server.key'))
    #app.run(debug=True, ssl_context=context)
