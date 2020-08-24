import json, shutil, os, bcrypt
from datetime import datetime
userPath = 'Database/User.txt'
PermPath = 'Database/PermissionRequest.txt'
RolePath = 'Database/Role.txt'
log = 'Database/log.txt'


def initialize():
	try:
		os.mkdir("Database")
	except:
		pass

def register(username, password, email="", role="Client"):      #for testing can be used for searching as it's dict
	Userjson = readJsonDict(userPath)
	if username in Userjson:
		return{"Success":False,"Error":"Username in use"}

	if email == "":
		return{"Success":False, "Error":"email missing"}

	hashedpassword = bcrypt.hashpw(password.encode('UTF-8'), bcrypt.gensalt())
	Userjson[username] = {"password":hashedpassword.decode('UTF-8'), "role":role, "email":email, "emailVerify":False}
	writeJson(userPath, Userjson)
	return {"Success":True, "username":username, "role":role, "email":email, "emailVerify":False}

def requestPermission(username, role):
	PermJson = readJsonDict(PermPath)
	requestRole = {"username":username, "role":role}
	PermJson[username] = role
	writeJson(PermPath, PermJson)
	return requestRole

def listPermission():
	return readJsonDict(PermPath)

def login(username, password):
	UserJson = readJsonDict(userPath)
	if username in UserJson:
		temp = UserJson[username]
		if bcrypt.checkpw(password.encode('UTF-8'), temp["password"].encode('UTF-8')):
			return {"Success":True}
		else:
			return {"Success":False,"Error":"Password Incorrect"}
	else:
		return {"Success":False,"Error":"Username does not exist"}

def allUser():
	Users = readJsonDict(userPath)
	return Users

def allUserNoPassword():
	Users = readJsonDict(userPath)
	for key,value in Users.items():
	   del value["password"]
	return Users

def changePassword(username, newPassword):
	try:
		UserJson = readJsonDict(userPath)
		User = UserJson[username]
		User["password"] = bcrypt.hashpw(newPassword.encode('UTF-8'), bcrypt.gensalt()).decode("UTF-8")
		UserJson[username] = User
		writeJson(userPath,UserJson)
		return True
	except Exception as e:
		#writeJson(userPath, UserJson)
		return False


def allRole(role):
	UserJson = readJsonDict(userPath)
	roleList = []
	for User in UserJson:
		if User["role"] == role:
			roleList.append(User)    
	return roleList
	
def getUser(username):
	try:
		UserJson = readJsonDict(userPath)
		User = UserJson[username]
		del User["password"]
		return User
	except:
		return False

def readJson(path):
	output = []
	try:
		file = open(path,'r')
		output = json.loads(file.read())
		file.close()
	except:
		pass
	return output   #output list

def readJsonDict(path):
	output = {}
	try:
		file = open(path,'r')
		output = json.loads(file.read())
		file.close()
	except:
		pass
	return output   #output dict
	
def getrole(username):
	User = getUser(username)
	return User["role"]

def writeJson(path, jsonArray):
	print(jsonArray)
	file = open(path,'wb')
	file.write(json.dumps(jsonArray).encode('UTF-8'))
	file.close()

def roleSetup():
	file = open(RolePath, 'wb')
	file.write()
	

def filterData(data, param, value):
	for key,value in data:
		if value[param] == value:
			del data[key]
	return data

def getEmail(username):
	UserJson = readJsonDict(userPath)
	User = UserJson[username]
	return User["email"]

def emailVerify(username):
	AUser = allUser()
	User = AUser[username]
	User['emailVerify'] = True
	AUser[username] = User
	writeJson(userPath, AUser)

def MenuUser():
	AUser = allUser()
	output = {}
	for key, value in AUser.items():
		output[key]=key #change it to names 
	return output

def addData(username, content, writer):
	try:
		UserJson = readJsonDict(userPath)
		User = UserJson[username]
		now = datetime.now()
		date_time = now.strftime("%m/%d/%Y, %H:%M:%S")
		date_time = date_time + " Author: " + writer
		User[date_time] = content
		UserJson[username] = User
		writeJson(userPath,UserJson)
		return True
	except Exception as e:
		#writeJson(userPath, UserJson)
		return False


