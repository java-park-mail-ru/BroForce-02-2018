# BroForce-02-2018

# Sign Up 
POST
/api/join
{"login" : "mikhail", "email" : "masyagin1998@yandex.ru", "password" : 1234}

# Update User
PUT
/api/join
{"login" : "mikhail", "email" : "masyagin1998newemail@yandex.ru", "password" : 1234}

# Check, if user is logged in
GET
/api/auth

# Sign In
POST
/api/auth
{"login" : "mikhail", "password" : 1234}

# Sign Out
DELETE
/api/auth
