# BroForce-02-2018

# Team list
- Масягин Михаил АПО-22
- Мельников Владислав АПО-22
- Полещук Виталий АПО-22
- Саркисян Артур АПО-21


|  | url | Тело запроса |
| ------ | ------ | ------ |
| Зарегистрироваться | /api/signup | ```{"login":"vitalya", "password":"1324qwer", "email":"vitalya@mail.ru"}```
| Авторизоваться | /api/signin | ```{"login":"vitalya", "password":"1324qwer"}```
| Запросить пользователя текущей сессии | /api/loginfo | -
| Изменить пароль | /api/newpassword | ```{"password":"1324qwer", "change":"1234"}```
| Изменить логин | /api/newlogin | ```{"password":"1324qwer", "change":"vitalik"}```
| Изменить email | /api/newemail | ```{"password":"1324qwer", "change":"vitalya@yandex.ru"}```
| Изменить аватар | /api/newavatar | ```{"password":"1324qwer", "change":"_NEW_AVATAR_"}```
| Разлогиниться | /api/logout | -
