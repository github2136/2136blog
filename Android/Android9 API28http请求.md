## CLEARTEXT communication to  not permitted by network security policy错误
Android9 API28默认禁止http请求必须使用https，如果需要http请求可以使用以下方法
* 在`AndroidManifest.xml`中`application`节点添加`android:usesCleartextTraffic="true"`
* 在`res`目录下添加`xml`目录再添加`network_security_config.xml`(名称可自定义)
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <base-config cleartextTrafficPermitted="true" />
    </network-security-config>

    ```
    然后在`AndroidManifest.xml`中`application`节点上添加`android:networkSecurityConfig="@xml/network_security_config"`
* 将`targetSdkVersion`改为27或更低的版本
