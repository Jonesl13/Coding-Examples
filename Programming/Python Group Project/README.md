# SCC363CW1

Email Address: macblowfish@gmail.com Password: bl0wf1sh

API KEY for email-verifier: AIzaSyAUl1pl7Zv1dPvyQfD4JONLcl16UmjD4KU


Python3.7 is used in this program

Following are the libraries added.

To simply add all the libraries, go to the directory
```
pip3 install -r requirements.txt
```

*Flask server libraries*
```
pip3 install flask /*from flask import Flask*/
pip3 install pyopenssl /*for ssl but not working at the moment*/ 
```
[HTTPS](https://blog.miguelgrinberg.com/post/running-your-flask-application-over-https)
[HTTPS secure connection](https://www.freecodecamp.org/news/how-to-get-https-working-on-your-local-development-environment-in-5-minutes-7af615770eec/)

[HTML making request](https://www.w3schools.com/xml/ajax_xmlhttprequest_send.asp)

*making request*[geeksforgeeks](https://www.geeksforgeeks.org/get-post-requests-using-python/)
```
pip3 install request
```


*create certificate*
```
openssl req -x509 -newkey rsa:4096 -nodes -out cert.pem -keyout key.pem -days 365

Just import rootCA.pem to the certificates
KEY PASSWORD: blowfish

```
Enter:
```
UK
Lancashire
Lancaster
Lancaster University
SCC
localhost
.
```

*password hashing libraries*
```
pip3 install bcrypt /* import bcrypt */
```

*password strength libraries*
```
pip3 install regex /*import re*/
pip3 install password-strength /* from password_strength import PasswordPolicy */
```
To start the server, enter server directory then
```
python app.py /*python3 app.py (if you have both python2 & python3)*/
```
