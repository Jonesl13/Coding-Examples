import generatePairKey
import json, random
import requests
from Crypto.PublicKey import RSA
import base64

def handshake():
    url = "http://127.0.0.1:3864/handshake"
    main_content = {}
    content = {}
    PrimeNumber, BaseNumber = generatePairKey.generatePublicVariable()
    private_key, public_key = generatePairKey.generatePairKeys()
    secret_variable = random.randint(1,1000)
    content['sharedPrime'] = hex(int(PrimeNumber))
    content['sharedBase'] = hex(int(BaseNumber))
    content['partial_key'] = hex(int(generatePairKey.sharedkey(int(PrimeNumber),int(BaseNumber),secret_variable)))
    encrypted_content = generatePairKey.encryptMessageFromPublicNetwork(json.dumps(content),'Server').decode('UTF-8')
    main_content['public_key'] = public_key.exportKey('PEM').decode('UTF-8')
    main_content['encrypted'] = encrypted_content
    payload = json.dumps(main_content)
    headers = {}
    print(type(payload))

    response = requests.request("POST", url, headers=headers, data=payload)
    reply = response.text
    decrypted = generatePairKey.decryptMessageWithPrivateKey(reply,private_key)
    decrypted_content = json.loads(decrypted)
    print(decrypted_content)
    MyID = decrypted_content['id']
    sharedkey = generatePairKey.sharedkey(PrimeNumber, int(decrypted_content['partial_key'],16),secret_variable)
    return MyID, sharedkey
    
def encryption(message, key):
    encrypted_message = ""
    for c in message:
        encrypted_message += str((ord(c)+key)%256)+"|"
    return encrypted_message

def decryption(message, key):
    encrypted_message = ""
    for c in message.split("|"):
        try:
            encrypted_message += chr(((int(c)-key))%256)
        except ValueError:
            pass
    return encrypted_message

ID, key = handshake()
msg = "fuck you bitch"
msg = encryption(msg,key)
print(msg)
print(decryption(msg,key))
print("This is the message")
print(type(msg))
headers={}
response = requests.request("POST", "http://127.0.0.1:3864/hello/"+ID, headers=headers, data = msg)
#print(response.text)
#troll = decryption(response.text,key)
#print(troll)




#print(requests.request("GET","http://127.0.0.1:3864/"+MyID).text)




