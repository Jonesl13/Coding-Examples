This will be a documentation on how the api works

When first turn on the Client server, a handshake will be established. <br>
A **shared key** will be generated in the Client server used for encryptiona and descryption. <br>
A temporary userid **UserId** will also be generated for linking the **shared key**

***
POST http://localhost:3864/register/{$username}

header:
```
UserId: string
```
body: encrypt with shared key
```
    json:{
        "username":username(string),
        "password":password(string),
        "email":email(string),
        "firstName":name(string),
        "lastName":name(string),
        "dob": mm-dd-yyyy(string),
    }

```

return:
```
    valid input -> 200 JWT

    Invalid password/username-> render html
```
***

POST http://localhost:3864/login/{$username}



body:(shared key encryption)
```
json:
{
    "username":username(string),
    "password":password(string),
}
```
return:
```

    valid -> JWT 
             return html valid login

    Invalid -> return invalid html login
```
***

GET http://localhost:3864/Users/protection?token={$JWTToken}

URI:
```
JWTToken
```

***
GET http://localhost:3864/Patient/{$patient_name}/protection?token={$JWTToken}

URI:
```
patient_name
JWTToken
```

***
GET http://localhost:3864/Patient