## Auth-main
带有权限验证、菜单管理、用户管理、角色管理的后台管理系统框架。前后端分离的**后端**。

### Auth-Web模块
提供权限验证、菜单管理、用户管理、角色管理功能的controller和service，本身集成swagger-ui，spring-security，spring-boot-starter-validation。

### Auth-Dao模块
Auth-Web模块的具体实现，集成JPA，Mysql。

### Auth-Security-Session模块
配置spring-security的拦截策略和相关配置，提供session+cookie的登录方式。本身集成auth-dao模块。提供验证码登录。