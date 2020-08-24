import bcrypt

password = b"The password"

hashed = bcrypt.hashpw(password, bcrypt.gensalt()) #generate random salt
#explanation: it generates a shadow password with the salt, so that when it checks the hashed password it will remove the salt will specific algotihm based on https://en.wikipedia.org/wiki/Bcrypt


if bcrypt.checkpw(password, hashed):
    print("Correct")
else:
    print("False")

IncorrectPassword = b"Password W"

IncorrectHashed = bcrypt.hashpw(IncorrectPassword, bcrypt.gensalt())

if bcrypt.checkpw(password, IncorrectHashed):
    print("Correct")
else:
    print("Incorrect")