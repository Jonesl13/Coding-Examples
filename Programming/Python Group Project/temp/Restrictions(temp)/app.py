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
	return render_template('index.html')

@app.route('/login')
def login():
	return render_template('login.html')

@app.route('/logging', methods=["POST"])
def logging():
	username = request.form['username']
	password = request.form['password']
	content={"username":username, "password":password}
	content = json.dumps(content)
	print(content)
	#content = encryption(content,key)
	response = requests.request("POST","http://127.0.0.1:3864/login",headers={"id":MyID},data=content)
	#TODO check the "Success" then go to the one time password page 
	response = json.loads(response.text)
	response = json.loads(response)
	print(response["Success"])
	if str(response["Success"]) == "False":
		return loginError(response["Error"])
	#TODO else go beck to login
	return redirect(url_for("one_time_passcode"))

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
		first = json.loads(response.text)
		labels = json.loads(first)
	
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
		return "Something is wrong",400



@app.route('/<string:username>', methods=["GET"])
def get_records(username):
	response = requests.request("GET","http://127.0.0.1:3864/data/"+username,headers={"id":MyID, "JWT":jwt})
	print(response.text)
	UserDict = json.loads(json.loads(response.text))
	x = '{ "name":"John", "age":30, "city":"New York"}'
	labels = json.loads(x)
	return render_template("PersonalPage.html", labels=UserDict, username = username)

@app.route('/register')
def register():
	return render_template('register.html')

@app.route('/one_time_passcode', methods=['GET','POST'])
def one_time_passcode():
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

	
@app.route('/registering', methods=['POST'])
def registering():	

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

		send_email = requests.request("POST", "http://127.0.0.1:3864/send_email/"+username)

		reply = response.text
		print(type(reply))
		return redirect(url_for('login'))
		
	return redirect(url_for('login'))

if __name__== "__main__":
    app.run(debug=True, ssl_context=('server.crt','server.key'))
    #app.run(debug=True, ssl_context=context)
