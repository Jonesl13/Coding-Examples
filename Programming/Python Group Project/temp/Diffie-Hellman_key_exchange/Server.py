import flask, random
from flask import request

import generatePairKey,json, randomText
from Crypto.PublicKey import RSA
import base64

app = flask.Flask(__name__)
sharedkey={}

def decryption(message, id):
    key = int(sharedkey[id])
    encrypted_message = ""
    for c in message.split("|"):
        try:
            encrypted_message += chr(((int(c)-key))%256)
        except ValueError:
            pass
    return encrypted_message

@app.route("/handshake", methods=['POST'])
def handshake():
    secret_variable = random.randint(1,1000)
    temp = json.loads(request.data.decode('UTF-8')) #bytes
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

@app.route("/<string:id>")
def check(id):
    return str(sharedkey[id])

@app.route("/hello/<string:id>",methods=["POST"])
def reply(id):
    print(decryption(request.data.decode("UTF-8"),id))
    #print(request.data)
    return "Helloworld",200



def setup():
    private_key, public_key = generatePairKey.generatePairKeys()
    generatePairKey.generatePairKey('Server', private_key, public_key)

if __name__=="__main__":
    setup()
    app.run(debug=True, port=int(3864))