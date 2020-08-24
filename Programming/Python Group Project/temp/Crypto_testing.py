from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
import base64


private_key = RSA.generate(2048)
public_key = private_key.publickey()

private_key_export = private_key.exportKey('PEM')
public_key_export = public_key.exportKey('PEM')


f = open('Privatekey.pem','wb')
f.write(private_key_export)
f.close()
f = open('Publickey.pem','wb')
f.write(public_key_export)
f.close()


message = b'This is the message'

encryptor = PKCS1_OAEP.new(public_key)
encrypted_msg = encryptor.encrypt(message)
encoded_encrypted_msg = base64.b64encode(encrypted_msg)

f = open('Encrypted_message.txt', 'wb')
f.write(encoded_encrypted_msg)
f.close()

encryptor_second = PKCS1_OAEP.new(private_key)
decoded_encrypted_msg = base64.b64decode(encoded_encrypted_msg)
decoded_decrypted_msg = encryptor_second.decrypt(decoded_encrypted_msg)
print(decoded_decrypted_msg)




