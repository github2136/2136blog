okhttp3.0https证书
===
当在`okhttp`中使用`https`请求出现`javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.`错误时表示找不到受信的证书，造成此问题的原因是使用了非正规的CA签发的证书，处理方式有以下几种
* 使用正规机构证书（需要付费）

* 忽略证书校验（不安全）

    ```kotlin
    val sslContext = SSLContext.getInstance("TLS")
    val x509 = arrayOf(object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun getAcceptedIssuers(): Array<X509Certificate> {
          return arrayOf()
        }
    })
    sslContext.init(null, x509, SecureRandom())
    //https请求忽略证书
    OkHttpClient().newBuilder()          
          //https请求忽略证书
          .sslSocketFactory(sslContext.socketFactory, x509[0])
          .hostnameVerifier(HostnameVerifier { hostname, session -> true })
          //https请求忽略证书
          .build()
    ```

* 在APP中下载或内置证书（当这么设置后由CA机构发布的证书也无法通过验证）

    ```kotlin
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val password = "password".toCharArray()
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    val `in`: InputStream? = null
    keyStore.load(`in`, password)
    
    val certificates = certificateFactory.generateCertificate(app.assets.open("ca.cer"))
    val certificateAlias = "0"
    keyStore.setCertificateEntry(certificateAlias, certificates)
    
    val keyManagerFactory = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm()
    )
    keyManagerFactory.init(keyStore, password)
    val trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm()
    )
    trustManagerFactory.init(keyStore)
    
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
    OkHttpClient().newBuilder()          
          //https请求忽略证书
          .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
          //不验证主机信息，不建议这么做
          .hostnameVerifier(HostnameVerifier { hostname, session -> true })
          //https请求忽略证书
          .build()
    ```

网站证书导出
---

![1](okhttp3.0https证书\1.png)
![2](okhttp3.0https证书\2.png)
![3](okhttp3.0https证书\3.png)