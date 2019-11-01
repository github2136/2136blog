# spring-boot
spring-boot项目初始化使用IDEA，File -> New -> Project -> Spring Initializr -> 选择Type(Gradle Project)/Language/Packaging/Java Version等信息 -> 勾选 Core -> DevTools/Web -> Web创建项目基本文件。
#### 开启热部署
* 添加`DevTools`
* File -> Settings -> Build,Execution,Deplyment -> Compiler，勾选 Build project automatically
* Shift+Ctrl+Alt+/ -> Registry -> 勾选 compiler.automake.allow.when.app.running
#### 单元测试