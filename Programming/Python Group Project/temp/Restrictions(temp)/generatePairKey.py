from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
import base64, os, json, random, math, sys, shutil
from random import randint
from sympy import isprime


#can do pem or txt

def generatePairKey(name, private_key, public_key):
    try:
        os.mkdir('Network')
    except:
        pass
    try:
        os.mkdir('Network/'+name+'KeyFile')
    except:
        pass
    private_key_export = private_key.exportKey('PEM')
    public_key_export = public_key.exportKey('PEM')
    f = open('Network/'+name+'KeyFile/'+name+'Privatekey.txt','wb')
    f.write(private_key_export)
    f.close()
    f = open('Network/'+name+'KeyFile/'+name+'Publickey.txt','wb')
    f.write(public_key_export)
    f.close()

def generatePairKeys():
    private_key = RSA.generate(2048)
    public_key = private_key.publickey()
    return private_key, public_key

def saveKeyInPublicSpace(name):
    try:
        os.mkdir('Network/'+'Public_Network')
        pass
    except:
        pass
    f = open('Network/'+name + 'Keyfile/'+name+'Publickey.txt','r')
    d = open('Network/'+'Public_Network/'+name+'Publickey.txt','wb')
    d.write(RSA.importKey(f.read()).exportKey('PEM'))
    f.close()
    d.close()

def generatePublicVariable():
    #try:
    #    os.mkdir('Network/'+'Public_Network')
    #    pass
    #except:
    #    pass
    PrimeNumber = generateLargePrime(128)
    BaseNumber = randint(1,1000)
    #f = open('Network/'+'Public_Network/'+name+'PublicVariable.txt','wb')
    #content = '{ "sharedPrime": '+str(PrimeNumber)+',"sharedBase": '+str(BaseNumber)+'}'
    #f.write(content.encode('UTF-8'))
    #f.close()

    return PrimeNumber, BaseNumber

def generateLargePrime(k):
     r=100*(math.log(k,2)+1)
     t = r
     while r>0:
         n = random.randrange(2**(k-1),2**(k))
         r-=1
         if isprime(n) == True:
             return n
     return "Failure after "+str(t) + " tries."

def getPublicVariable(name):
    f = open('Network/'+'Public_Network/'+name+'PublicVariable.txt','r')
    j = json.loads(f.read())
    f.close()
    return(j["sharedPrime"], j["sharedBase"])
    

def encryptMessage(message, Selfname, Sendername):
    f = open('Network/'+Selfname+ 'KeyFile/'+Sendername+'Publickey.txt','r')
    opposite_public_key = RSA.importKey(f.read())
    #print(str(opposite_public_key))
    encryptor = PKCS1_OAEP.new(opposite_public_key)
    encrypted_msg = encryptor.encrypt(message.encode('UTF-8'))
    encoded_encrypted_msg = base64.b64encode(encrypted_msg)
    #print(encoded_encrypted_msg)
    f.close()
    return encoded_encrypted_msg

def encryptMessageWithPublicKey(message, key):
    encryptor = PKCS1_OAEP.new(key)
    encrypted_msg = encryptor.encrypt(message.encode('UTF-8'))
    encoded_encrypted_msg = base64.b64encode(encrypted_msg)
    #print(encoded_encrypted_msg)

    return encoded_encrypted_msg


def encryptMessageFromPublicNetwork(message, Sendername):
    f = open('Network/'+Sendername+'KeyFile/'+Sendername+'Publickey.txt','r')
    opposite_public_key = RSA.importKey(f.read())
    #print(str(opposite_public_key))
    encryptor = PKCS1_OAEP.new(opposite_public_key)
    encrypted_msg = encryptor.encrypt(message.encode('UTF-8'))
    encoded_encrypted_msg = base64.b64encode(encrypted_msg)
    #print(encoded_encrypted_msg)
    f.close()
    return encoded_encrypted_msg 

def decryptMessage(encoded_encrypted_msg, Selfname):
    f = open('Network/'+Selfname+'KeyFile/'+Selfname+'Privatekey.txt','r')
    private_key = RSA.importKey(f.read())
    encryptor_second = PKCS1_OAEP.new(private_key)
    decoded_encrypted_msg = base64.b64decode(encoded_encrypted_msg.encode("utf-8"))
    decoded_decrypted_msg = encryptor_second.decrypt(decoded_encrypted_msg)
    #print(decoded_decrypted_msg)
    f.close()
    return(decoded_decrypted_msg.decode("utf-8"))

def decryptMessageWithPrivateKey(encoded_encrypted_msg, key):
    encryptor_second = PKCS1_OAEP.new(key)
    decoded_encrypted_msg = base64.b64decode(encoded_encrypted_msg.encode("utf-8"))
    decoded_decrypted_msg = encryptor_second.decrypt(decoded_encrypted_msg)
    return(decoded_decrypted_msg.decode("utf-8"))

def readPublicKey(name):
    f = open('Network/'+name+'KeyFile/'+name+'Publickey.txt','r')
    output = f.read()
    f.close()
    return output

def sharedkey(PrimeNumber, BaseNumber, secret_Number):
    return (BaseNumber**secret_Number)%PrimeNumber