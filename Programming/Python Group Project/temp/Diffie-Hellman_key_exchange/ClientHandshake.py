import generatePairKey
import randomText
from generatePairKey import generatePairKey, saveKeyInPublicSpace, generatePublicVariable, getPublicVariable, encryptMessageFromPublicNetwork,encryptMessage, decryptMessage, readPublicKey
import random
import socket
import json

def sending(message):
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(('127.0.0.1', 8080))
    client.send(message)
    from_server = client.recv(4096)
    print(from_server.decode('UTF-8'))
    client.close()

def SharedKeyAlgorithm(sharedprime, sharedbase, secretvariable):
    return (sharedbase ** secretvariable)% sharedprime

def handshake():
    Servername = 'Server'
    secret_variable = random.randint(1,1000000000)
    generatePairKey(name)
    generatePublicVariable(name)
    sharedprime,sharedbase = getPublicVariable(name)
    partialkey = SharedKeyAlgorithm(sharedprime,sharedbase,secret_variable)
    First_content = {
                    "id":name,
                    "sharedprime":str(sharedprime),
                    "sharedbase":str(sharedbase),
                    "partialkey": partialkey,
                    "publickey":readPublicKey(name)
                    }
    FirstMessage = {
                    "handshake": "True",
                    "cipher":"RSA", 
                    "message": encryptMessageFromPublicNetwork(str(json.dumps(First_content)),Servername).decode('UTF')
                    }
    sending(json.dumps(FirstMessage).encode('UTF-8'))

handshake()



    





