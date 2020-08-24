import json, shutil, os, bcrypt

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
    hashedpassword = bcrypt.hashpw(password.encode('UTF-8'), bcrypt.gensalt())
    Userjson[username] = {"password":hashedpassword.decode('UTF-8'), "role":role}
    writeJson(userPath, Userjson)
    return {"Success":True, "username":username, "password":hashedpassword.decode('UTF-8'), "role":role, "email":email, "emailVeirfy":False}

def requestPermission(username, role):
    #global Permission
    PermJson = readJson(PermPath)
    requestRole = {"username":username, "role":role}
    PermJson.append(requestRole)
    writeJson(PermPath, PermJson)
    return requestRole

def login(username, password):
	UserJson = readJsonDict(userPath)
	if username in UserJson:
		if bcrypt.checkpw(password.encode('UTF-8'), temp["password"].encode('UTF-8')):
			return {"Success":True}
		else:
			return {"Success":False,"Error":"Password Incorrect"}
	else:
		return {"Success":False,"Error":"Username does not exist"}
		
def update(username, password, role):
	UserJson = readJsonDict(userPath)
	hashedpassword = bcrypt.hashpw(password.encode('UTF-8'),bcrypt.gensalt())
	if role != getrole(username):
		UserJson[username] = {"password":hashedpassword.decode('UTF-8'), "role":role}
		writeJson(userPath, UserJson)
		return {"Success":True, "password":hashedpassword.decode('UTF-8'), "role":role}
	UserJson[username] = {"password":hashedpassword.decode('UTF-8'), "role":getrole(username)}
	writeJson(userPath, UserJson)
	return {"Success":True, "password":hashedpassword.decode('UTF-8'), "role":getrole(username)}
		
def allUser():
    #global userPath
    return readJson(userPath)

def allRole(role):
    #global userPath
    UserJson = readJson(userPath)
    roleList = []
    for User in UserJson:
        if User["role"] == role:
            roleList.append(User)    
    return roleList
    
def getUser(username):
    UserJson = readJson(userPath)
    User = UserJson[username]
    del User["password"]
    return User

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

def checkJWT():
	pass

def writeJson(path, jsonArray):
    file = open(path,'wb')
    file.write(json.dumps(jsonArray).encode('UTF-8'))
    file.close()

def roleSetup():
    file = open(RolePath, 'wb')
    file.write()
    
initialize()
#print(register('lololol', 12463476264))
#print(register('heyyea', 12354765845, role='Admin'))
#print(login('heyyea', '1235845'))
#print(getUser('heyyea'))
print(register('heyyea', '12354765845', role='Admin'))
print(update('heyyea', '123547658499999999', role='Staff'))
