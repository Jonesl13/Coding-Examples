import json
import jwt
from randomText import randomTextGenerator
from time import time

key = randomTextGenerator()

def issueJWT(username, exptime=600):    #in seconds
    return jwt.encode({'username':username, 'exp':int(time()+exptime)},key)

def checkJWT(JWToken):  #byte form input
    try:
        payload = jwt.decode(JWToken,key)
    except jwt.exceptions.InvalidSignatureError:
        return{"Success":False, "Error":"Incorrect Signature"}
    except jwt.exceptions.ExpiredSignatureError:
        return{"Success":False, "Error":"Expire Token"}
    except Exception as e:
        print(e)
        return{"Success":False, "Error":"Unknown"}
    return{"Success":True, "username":payload["username"]}
     
