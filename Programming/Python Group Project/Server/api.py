import flask
from flask import request, redirect
import apiDB
import apiLog
import threading
import bcrypt
import requests
import generatePairKey,json, randomText
from Crypto.PublicKey import RSA
import base64
import random, os
from flask_mail import Message, Mail
from itsdangerous import URLSafeTimedSerializer, SignatureExpired
from apiJwt import issueJWT, checkJWT
from zipfile import ZipFile

app = flask.Flask(__name__)
apiDB.initialize()
sharedkey={}
aad={}
nonce={}

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

def sendMail(title, recipient, message, attachment=False):
    msg = Message(title, sender = 'macblowfish@gmail.com', recipients = [recipient])
    msg.html = message
    if attachment == True:
        with ZipFile("log.zip","w") as newzip:
            newzip.write("log.log")

        with app.open_resource("log.zip") as fp:
            msg.attach("log.zip", "log/zip", fp.read())
    mail.send(msg)

def setup():
    private_key, public_key = generatePairKey.generatePairKeys()
    generatePairKey.generatePairKey('Server', private_key, public_key)

@app.route("/")
def default():
    return flask.jsonify("{}")

@app.route("/register", methods = ["POST"]) #TODO try catch for invalid input
def register():
    try:
        NewUser = decryption(request.data.decode("UTF-8"),request.headers["id"])
        NewUser = json.loads(NewUser)
        username = NewUser['username']
        password = NewUser['password']
        email = NewUser['email']
        #TODO check username
        #TODO check password
        response = apiDB.register(username,password,email=email)
        if response["Success"] == False:
            return response["Error"],400
        apiLog.logInfo('Registered user, username: {} email: {} role: Client'.format(username, email))
        return flask.jsonify(json.dumps(response)),200
    except Exception as e:
        print(e)
        return 'Error', 400

@app.route("/login", methods=["POST"])
def login():
    LoginUser = request.data.decode("UTF-8")
    try:
        #LoginUser = decryption(LoginUser,request.headers['id'])
        LoginUser = json.loads(LoginUser)
    except:
        return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content"})),400

    print(LoginUser)
    username = ""
    password = ""
    try:
        username = LoginUser["username"]
        password = LoginUser["password"]
    except:
        return flask.jsonify({"Success":False, "Error":"Error Content Unfound"}),400

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
        sendMail('One Time Passcode', emailaddr, "Please use this OTP: "+otp)
        apiLog.logInfo('Login {} {}'.format(username, True))
        return flask.jsonify(json.dumps({"Success":True, "Status":"One Time Password", "Username":username})),200
    except Exception as e:
        print(e)
        apiLog.logWarn('Login {} {}'.format(username, False))
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
        return flask.jsonify(Data),200
    elif allowAccess(['Client'],request) == True:
        Data={}
        Data[username] = username
        return flask.jsonify(Data),200
    else:
        apiLog.logError("{} raised {}".format(username, response["Error"]))
        return flask.jsonify(response), 400
    
    #action = json.loads(request.data.decode("UTF-8"))

@app.route("/data/<string:patientusername>", methods=["GET"])
def getData(patientusername):
    #TODO check jwt check role
    response = checkJWT(request.headers["JWT"])
    if response["Success"] == False:
        apiLog.logError(response["Error"])
        return flask.jsonify(response),400
    
    username = response["username"]
    role = apiDB.getrole(username)
    if role == "Client" and patientusername != username:
        apiLog.logWarn("{} unauthorized access".format(username))
        return "unauthorized access",400

    User = apiDB.getUser(patientusername)
    if User == False:
        return "No Such user",400
    apiLog.logInfo("{} accessed {}'s data".format(username, patientusername))
    return flask.jsonify(User),200

# @app.route("/data/UpdateData/<string:patientusername>")
# def updatePW(patientusername):
# 	response = checkJWT(request.headers["JWT"])
# 	username = response["username"]
# 	try:
# 		LoginUser = json.loads(LoginUser)
# 	except:
# 		print("Error A")
# 		return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content-type"})),400
# 	password = ""
# 	try:
# 		password = LoginUser["password"]
# 	except:
# 		print("Error B")
# 		return flask.jsonify(json.dumps({"Success":False, "Error":"Error Content Unfound"})),400
# 	if allowAccess(['Staff','Permission_Admin','Client'],request) == True:
# 		if patientusername != username:
# 			apiLog.logWarn("{} unauthorized access".format(username))
# 			return flask.jsonify(json.dumps(response["Error"])),400
# 	return flask.jsonify(apiDB.update(username, password)), 200

@app.route("/data/update_record/<string:username>", methods=["POST"])
def add_records(username):
    if allowAccess(['Staff'], request)!=True:
        return 'JWT Error', 400

    header = request.headers
    result = checkJWT(header["JWT"])    #TODO decryption
    writer = result["username"]

    content = request.data.decode("UTF-8")
    #TODO decrypt

    if apiDB.addData(username, content, writer) == True:
        return "Success", 200
    return "Write data Error", 400


@app.route("/updatePassword", methods=["POST"])
def updatePassword():
    if allowAccess(['Client', 'Staff', 'Permission_Admin'], request) != True:
        return 'JWT Error', 400
    header = request.headers
    result = checkJWT(header["JWT"])
    username = result["username"]
    
    data = request.data.decode("UTF-8")
    #TODO maybe decrypt
    JSONresult = json.loads(data)
    if apiDB.changePassword(username, JSONresult["newPassword"]) == True:
        return 'Success',200
    return "Error changing password",400

@app.route("/forgotPasswordEmail/<string:username>", methods=["POST"])
def forgotPasswordEmail(username):
    if request.method == 'POST':
        #header = request.headers
        #username = response["username"]
        #TODO Show message on page that an email will be sent shortly
        #TODO Return user to login page
        mailaddr = apiDB.getEmail(username)
        print(username)
        print(mailaddr)
        mailkey = URLSafeTimedSerializer('forgotfish') #TODO Maybe move this to a secure position (encrypted perhaps)
        token = mailkey.dumps(username, salt='forgot-password')
        sendMail('Changing Password', mailaddr, "<a href=https://localhost:5000/forgot_pass/" + token + ">Follow This link for changing your password</a>", False)
        
	    #TODO Log the forgot password activity
        return flask.jsonify(json.dumps({"Success":True, "Status":'Sent forgot password link'})),200
    else:	
        return flask.jsonify(json.dumps({"Success":False, "Status":'Failed to send forgot password link'}))

@app.route("/forgot_change/<token>", methods=["POST"])
def forgotpwchange(token):
    try:
        mailkey = URLSafeTimedSerializer('forgotfish') #TODO move this to a secure position
        username = mailkey.loads(token, salt='forgot-password')
        userDB = apiDB.allUserNoPassword()
        if username in userDB:
            data = request.data.decode("UTF-8") #TODO encryption needed
            #TODO maybe decrypt
            JSONresult = json.loads(data)
            #TODO Log the activity
            if apiDB.changePassword(username, JSONresult["newPassword"]) == True:
                return flask.jsonify(json.dumps({"Success":True, "Status":'Password Changed'})),200
            return flask.jsonify(json.dumps({"Success":False, "Status":'Failed to change password'})),400
        else:
            return flask.jsonify(json.dumps({"Success":False, "Status":'Error changing password'})),400
    except Exception as e:
        print(e)
        return flask.jsonify(json.dumps({"Success":False, "Status":'Failed to change password'})),400

@app.route("/confirm/OneTimeCode/<string:passcode>", methods=["POST", "GET"])    #TODO after the login check the code and return jwt @Chua can you get the username from the passcode? use a global dictionary to store passcode to username at line 15
def OneTimeCode(passcode):
    try:
        mailkey = URLSafeTimedSerializer('onetimeblow') #TODO move this to a secure position (encrypted perhaps)
        username = mailkey.loads(passcode, salt='oneblowfish',max_age=180)
        print(username) #TODO a request that changes emailVerify boolean to true and compare username to database
        apiDB.emailVerify(username)
        apiLog.logInfo("OTP verified for {}".format(username))
        return flask.jsonify(json.dumps({"Success":True, "Status":'Passed OTP', "JWT":str(issueJWT(username).decode("UTF-8"))})),200
    except Exception as e:
        print(e)
        apiLog.logError("Failed to verify OTP")
        return flask.jsonify(json.dumps({"Success":False, "Status":str(e)})),400

@app.route("/send_email/<string:username>", methods=['POST'])
def send_email(username):
    if request.method == 'POST':
        mailaddr = apiDB.getEmail(username)
        print(username)
        print(mailaddr)
        mailkey = URLSafeTimedSerializer('blowfish') #TODO move this to a secure position (encrypted perhaps)
        token = mailkey.dumps(username, salt='email-confirm')
        sendMail('Verification Code', mailaddr, "<a href=http://127.0.0.1:3864/confirm_email/"+token+">Follow this link for account activation</a>")
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
        apiLog.logInfo("Email verified for {}".format(username))
        return redirect('https://localhost:5000/login')
    except Exception:
        apiLog.logWarn("Email verification failed")
        return 'The token does not match'

@app.route("/request/permission/<string:role>/<string:usernameT>")
def requestPermission(role,usernameT):
    
    #TODO check jwt
    if allowAccess(['Staff','Permission_Admin','Client'],request) == True:	#DO ADMIN HAVE TO REQUEST PERMISSION?
        response = checkJWT(request.headers["JWT"])
        username = response["username"]
    #TODO compare to the username 
    #result = checkJWT(request.headers["JWT"])
    #if result["Success"] == True and result["username"]==currentusername:
        if username == usernameT:
            apiLog.logInfo('Permission requested, username: {} role: {}'.format(username, role))
            return flask.jsonify(apiDB.requestPermission(username, role))
        return flask.jsonify(json.loads("{'Success':False,'Error':Incorrect username}"))
    apiLog.logWarn("{} has Incorrect JWT".format(username))
    return 'JWT Incorrect'

@app.route("/permission")
def listPermission():
    if allowAccess(['Permission_Admin'],request) == True:
        apiLog.logInfo("Admin request to list all permissions")
        return flask.jsonify(apiDB.listPermission())
    apiLog.logWarn("{} attempted to list permissions".format(""))
    return 'Error'

@app.route("/grant/permission", methods=["POST"])
def grantPermission():
    if allowAccess(['Permission_Admin'],request) == False:
        apiLog.logWarn("{} attmepted to grant permission")
        return flask.jsonify({"Success":False, "Error":"No Permission"})
    result = checkJWT(request.headers["JWT"])
    username = result["username"]
    content = request.data.decode("UTF-8")
    #TODO check the username and the corresponding role to the dictionary then grant permission
    apiLog.logInfo("Permission granted by {}".format(username))
    return flask.jsonify(json.loads("{'Success':True}"))

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
        # reply_content['nonce'] = os.urandom(12)
        encrypted = generatePairKey.encryptMessageWithPublicKey(json.dumps(reply_content),Client_public_key).decode('UTF-8')
        return encrypted, 200
    except:
        return "", 400

@app.route('/existUser/<string:username>', methods=["GET", "POST"])
def checkUser(username):
    output = apiDB.getUser(username)
    if output == False:
        return 'False'
    return 'True'

if __name__=="__main__":
    apiDB.initialize()
    apiLog.startBackup()
    setup()
    app.run(debug=True, port=int(3864))
