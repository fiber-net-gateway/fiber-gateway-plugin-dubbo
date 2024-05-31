# fiber-gateway-plugin

## dubbo plugin
 using dubbo3/nacos/generic call
### example
```javascript
directive demoService from dubbo "com.test.dubbo.DemoService";

req.discardBody();
let user = demoService.createUser("user_name");
if (user.age == 0) {
    user.age = 20;
    user.name = demoService.$dynamicInvoke("createUser", [user.name + "333"]).name;
}
let user2 = demoService.create({"name":"33333", age: user.age, male: !user.male});
return {dubboResult: user, state: 0,user2};
```
### api
- explaining
```javascript
directive demoService from dubbo "com.test.dubbo.DemoService" 3000; // 3000 is timeout millis
// -> call com.test.dubbo.DemoService#xxx(param1, param2)
demoService.xxx(param1, param2);
// call with dynamic method name. -> call com.test.dubbo.DemoService#xxx(param1, param2)
demoService.$dynamicInvoke("xxx", [param1, param2])
```
- provider interface
```java
package com.test.dubbo;

public interface DemoService {

    User createUser(String name);
    User create(User user);

    class User {
        private String name;
        private int age;
        private boolean male;
        // ... setter getter
    }
}
```