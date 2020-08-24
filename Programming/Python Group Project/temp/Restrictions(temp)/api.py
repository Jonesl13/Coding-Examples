import flask
from flask import request, redirect
import apiDB
import apiLog
import bcrypt
import requests
import generatePairKey,json, randomText
from Crypto.PublicKey import RSA
import base64
import random
from flask_mail import Message, Mail
from itsdangerous import URLSafeTimedSerializer, SignatureExpired
from apiJwt import issueJWT, checkJWT

app = flask.Flask(__name__)
apiDB.initialize()
sharedkey={}

app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'macblowfish@gmail.com'
app.config['MAIL_PASSWORD'] = 'bl0wf1sh'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
mail = Mail(app)

def decryption(message, id):
    key = int(sharedkey[id])
    encrypted_message = ""
    for c in message.split("|"):
        try:
            encrypted_message += chr(((int(c)-key))%256)
        except ValueError:
            pass
    return encrypted_message


def allowAccess(roles, request):
    header = request.headers
    response = ''
    if "JWT" in header:
        UserId = header["id"]
        #TODO might need to decrypt
        UserJWT = header["JWT"]
        JWTresult = checkJWT(UserJWT)
        if JWTresult["Success"]==True:
            username = JWTresult["username"]
            UserRole = apiDB.getrole(username)
            if UserRole in roles:
                return True
            else:
                return 'role inaccept'
        else:
            return 'JWT invalid'
    else:
        return 'JWT missing'

def setup():
    private_key, public_key = generatePairKey.generatePairKeys()
    generatePairKey.generatePairKey('Server', private_key, public_key)

@app.route("/")
def default():
    return flask.jsonify("{}")

@app.route("/register", methods = ["POST"]) #TODO try catch for invalid input
def register():
    NewUser = decryption(request.data.decode("UTF-8"),request.headers["id"])
    #TODO get id
    #TODO NewUser = decryption(NewUser, id)
    NewUser = json.loads(NewUser)
    username = NewUser['username']
    password = NewUser['password']
    email = NewUser['email']
    #TODO check username
    #TODO check password
    response = apiDB.register(username,password,email=email)
    #TODO return JWT | return response and return jwt only in login
    #return flask.jsonify(apiDB.register(username=username,hashedpassword = bcrypt.hashpw(password.encode('UTF-8'),bcrypt.gensalt()).decode('UTF-8'))), 200
    #return flask.jsonify(json.dumps({"JWT":issueJWT(username).decode("UTF-8")})),200
    return flask.jsonify(json.dumps(response)),200

@app.route("/login", methods=["POST"])
def login():
    LoginUser = request.data.decode("UTF-8")
    #LoginUser = decryption(LoginUser,request.headers['id'])
    print(LoginUser)
    try:
        LoginUser = json.loads(LoginUser)
    except:
        print("Error A")
        return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content-type"})),400

    print(LoginUser)
    username = ""
    password = ""
    try:
        username = LoginUser["username"]
        password = LoginUser["password"]
    except:
        print("Error B")
        return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content Unfound"})),400

    try:
        #TODO check username and check password 
        response = apiDB.login(username,password)
        if response["Success"] == False:
            return flask.jsonify(json.dumps(response)),400
        #TODO if true to one time password(send email)
        emailaddr = apiDB.getEmail(username)
        #print({"Success":True, "Status":"One Time Password", "Username":username})
        mailkey = URLSafeTimedSerializer('onetimeblow') #TODO move this to a secure position (encrypted perhaps)
        otp = mailkey.dumps(username, salt='oneblowfish')
        msg = Message('One Time Passcode', sender = 'macblowfish@gmail.com', recipients = [emailaddr])
        msg.html = "Please use this OTP: "+otp
        mail.send(msg)
        return flask.jsonify(json.dumps({"Success":True, "Status":"One Time Password", "Username":username})),200
    except Exception as e:
        print(e)
        return flask.jsonify(json.dumps({"Success":False, "Error":"Error Unknown"})),400

@app.route("/data", methods=["GET"])
def processData():
	response = checkJWT(request.headers["JWT"])
	username = response["username"] 
    #TODO check the jwt (DONE)
	if allowAccess(['Staff','Permission_Admin'],request) == True:
	    Data = apiDB.MenuUser()
	    apiLog.logInfo("{} accessed database".format(username))
        #TODO record log
	    return flask.jsonify(json.dumps(Data)),200
    else:
        if allowAccess(['Client'],request) == True:
        #TODO log
            Data={}
            Data[username] = username
            return flask.jsonify(Data),200
	else:
		apiLog.logError("{} raised {}".format(username, response["Error"]))
		return flask.jsonify(json.dumps(response)),400

@app.route("/data/<string:patientusername>", methods=["GET"])
def getData(patientusername):
	response = checkJWT(request.headers["JWT"])
	username = response["username"]
    #TODO check jwt check role
	if allowAccess(['Staff','Permission_Admin','Client'],request) == True:
		if patientusername != username:
			apiLog.logWarn("{} unauthorized access".format(username))
			return flask.jsonify(json.dumps(response["Error"])),400
    #response = checkJWT(request.headers["JWT"])
    #if response["Success"] == False:
        #apiLog.logError(response["Error"])
        #return flask.jsonify(json.dumps(response)),400
    #username = response["username"]
    #role = apiDB.getrole(username)
    #if role == "Client" and patientusername != username:
        #apiLog.logWarn("{} unauthorized access".format(username))
        #return flask.jsonify(json.dumps(response["Error"])),400
		User = apiDB.getUser(patientusername)
		print(User)
		apiLog.logInfo("{} accessed {}'s data".format(username, patientusername))
		return flask.jsonify(json.dumps(User)),200
	else:
		apiLog.logError(response["Error"])
		return flask.jsonify(json.dumps(response)),400

@app.route("/data/UpdateData/<string:patientusername>")
def updatePW():
	response = checkJWT(request.headers["JWT"])
	username = response["username"]
	try:
		LoginUser = json.loads(LoginUser)
	except:
		print("Error A")
		return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content-type"})),400
	password = ""
	try:
		password = LoginUser["password"]
	except:
		print("Error B")
		return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content Unfound"})),400
	if allowAccess(['Staff','Permission_Admin','Client'],request) == True:
		if patientusername != username:
			apiLog.logWarn("{} unauthorized access".format(username))
			return flask.jsonify(json.dumps(response["Error"])),400
	return flask.jsonify(apiDB.update(username, password))
	

@app.route("/confirm/OneTimeCode/<string:passcode>", methods=["POST", "GET"])    #TODO after the login check the code and return jwt @Chua can you get the username from the passcode? use a global dictionary to store passcode to username at line 15
def OneTimeCode(passcode):
    try:
        mailkey = URLSafeTimedSerializer('onetimeblow') #TODO move this to a secure position (encrypted perhaps)
        username = mailkey.loads(passcode, salt='oneblowfish',max_age=180)
        print(username) #TODO a request that changes emailVerify boolean to true and compare username to database
        #apiLog.logInfo("OTP verified for {}", username)
        return flask.jsonify(json.dumps({"Success":True, "Status":'Passed OTP', "JWT":str(issueJWT(username).decode("UTF-8"))})),200
    except Exception as e:
        print(e)
        apiLog.logError("Failed to verify OTP")
        return flask.jsonify(json.dumps({"Success":False, "Status":e})),400

@app.route("/send_email/<username>", methods=['POST'])
def send_email(username):
    if request.method == 'POST':
        mailaddr = apiDB.getEmail(username)
        mailkey = URLSafeTimedSerializer('blowfish') #TODO move this to a secure position (encrypted perhaps)
        token = mailkey.dumps(username, salt='email-confirm')
        msg = Message('Verification Code', sender = 'macblowfish@gmail.com', recipients = [mailaddr])
        msg.html = "<a href=http://127.0.0.1:3864/confirm_email/"+token+">Follow this link for account activation</a>"
        mail.send(msg)
        apiLog.logInfo("Sent email verification to {}".format(mailaddr))
        return flask.jsonify(json.dumps({"Success":True, "Status":'Sent verification code'})),200
    else:
        apiLog.logError("Failed to send email verification")
        return flask.jsonify(json.dumps({"Success":False, "Status":'Failed to send verification code'})),400

@app.route("/confirm_email/<token>")
def confirm_email(token):
    try:
        mailkey = URLSafeTimedSerializer('blowfish') #TODO move this to a secure position (encrypted perhaps)
        username = mailkey.loads(token, salt='email-confirm')
        #TODO a request that changes emailVerify boolean to true and compare username to database

        return redirect('https://localhost:5000/login')
    except Exception:
        return 'The token does not match'

@app.route("/Users")
def listUser():
    #TODO check JWT
	if allowAccess(['Staff','Permission_Admin'], request) == True:
		return flask.jsonify(apiDB.allUser())
	return flask.jsonify(json.loads("{'Success':False,'Error':Permission not granted}"))
	#return 'Access Denied'

@app.route("/request/permission/<string:role>/<string:username>")
def requestPermission(role,currentusername):
	response = checkJWT(request.headers["JWT"])
	username = response["username"]
    #TODO check jwt
	if allowAccess(['Staff','Permission_Admin','Client'],request) == True:	#DO ADMIN HAVE TO REQUEST PERMISSION?
    #TODO compare to the username 
    #result = checkJWT(request.headers["JWT"])
    #if result["Success"] == True and result["username"]==currentusername:
		if currentusername == username:
			return flask.jsonify(apiDB.requestPermission(username, role))
		return flask.jsonify(json.loads("{'Success':False,'Error':Incorrect username}"))
	return 'JWT Incorrect'

@app.route("/permission")
def listPermission():
	if allowAccess(['Permission_Admin'],request) == True:
		return flask.jsonify(apiDB.listPermission())
	return 'Error'

@app.route("/grant/permission", methods=["POST"])
def grantPermission(currentusername):		#UPDATE: Added param for check both role and username
	response = checkJWT(request.headers["JWT"])
	username = response["username"]
	#TODO check the username and the corresponding role to the dictionary then grant permission
	if allowAccess(['Permission_Admin'],request) == True:
		if currentusername != username:
			apiLog.logWarn("{} unauthorized access".format(username))
			return flask.jsonify(json.dumps(response["Error"])),400
		content = request.data.decode("UTF-8")
		return flask.jsonify(json.loads("{'Success':True}"))
	else:
		return flask.jsonify({"Success":False, "Error":"No Permission"})


@app.route("/handshake", methods=['POST'])
def handshake():
    try: 
        temp = json.loads(request.data.decode('UTF-8')) #bytes
        secret_variable = random.randint(1,1000)
        Client_public_key = RSA.importKey(temp['public_key'].encode('UTF-8'))
        Client_content = json.loads(generatePairKey.decryptMessage(temp['encrypted'], 'Server'))
        Client_prime = int(Client_content['sharedPrime'],16)
        Client_base = int(Client_content['sharedBase'],16)
        Client_partial_key = int(Client_content['partial_key'],16)
        Client_id = randomText.randomTextGenerator()
        Client_shared_key = generatePairKey.sharedkey(Client_prime, Client_partial_key, secret_variable)
        sharedkey[Client_id] = Client_shared_key
        partial_shared_key = generatePairKey.sharedkey(Client_prime, Client_base, secret_variable)
        reply_content = {}
        reply_content['partial_key'] = hex(int(partial_shared_key))
        reply_content['id'] = Client_id
        encrypted = generatePairKey.encryptMessageWithPublicKey(json.dumps(reply_content),Client_public_key).decode('UTF-8')
        return encrypted, 200
    except:
        return "", 400


if __name__=="__main__":
    apiDB.initialize()
    setup()
    app.run(debug=True, port=int(3864))
    
#print(processData())
#print(listUser())
#print(requestPermission())
#print(listPermission())
#print(grantPermission())
#print(getData())

