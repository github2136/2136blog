spring-boot配置Mavne远程地址
===
默认idea的maven为http://repo.maven.apache.org ，可以打开idea目录下的`plugins\maven\lib\maven3\confsettings.xml`文件，找到`<mirrors>`节点添加
```xml
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>       
    </mirror>
```
来使用阿里云节点  
修改当前项目idea的.m2位文件夹置，如果没有下载maven使用idea默认maven可以从file->setting->Build,Execution,Deployment->Build Tools->Maven->Local repository修改保存位置  
修改默认项目idea的.m2位文件夹置，如果没有下载maven使用idea默认maven可以从file->Other Setting->Setting for New Proejct->Build,Execution,Deployment->Build Tools->Maven->Local repository修改保存位置