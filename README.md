# crowd-funding
基于springboot实现了项目资金筹集网站，由单一架构的管理员系统和分布式架构的客户系统组成，但存在部分未实现功能。

## 后台管理系统
  后台管理系统为基于SSM架构实现的单一架构，使用 Maven 作为构建管理和依赖管理工具，同时使用 SpringSecurity 接管项目的登录、登录检查、权限验证，并实现了基于角色的权限控制（ RBAC 模型），
使用 MyBatis 的 PageHelper 插件实现分页，在页面上使用了 Pagination 实现数字页码。
## 客户系统
基于SpringBoot+SpringCloud实现的分布式架构，基于Redis的SpringSession实现了用户登录信息的存储和服务器同步，同时使用了Thymeleaf作为服务器端渲染工具，调用第三方接口给用户手机发送短信验证码，
使用阿里云 OSS 对象存储服务保存用户上传的图片，订单和支付功能暂未实现。
