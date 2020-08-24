#https://cryptography.io/en/latest/hazmat/primitives/key-derivation-functions/
#https://cryptography.io/en/latest/hazmat/primitives/key-derivation-functions/

import os
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.ciphers.aead import ChaCha20Poly1305

backend = default_backend()
salt = os.urandom(16)
salt = os.urandom(16)
# derive
kdf = PBKDF2HMAC(
    algorithm=hashes.SHA256(),
    length=32,
    salt=salt,
    iterations=100000,
    backend=backend
)
key = kdf.derive(b"my great password")
print(str(key))

data = b"bitch"
aad = b'something' #id
chacha = ChaCha20Poly1305(key)
nonce = os.urandom(12)

ct = chacha.encrypt(nonce, data, aad)
print(chacha.decrypt(nonce,ct,aad))



