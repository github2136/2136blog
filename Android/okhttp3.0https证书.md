okhttp3.0https证书
===
https://developer.android.google.cn/training/articles/security-ssl?hl=zh_cn

Android系统默认内置知名CA证书，当使用知名CA证书的https请求时不需做任何特殊操作，但当出现`javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.`错误时原因有很多，其中包括：
1. 颁发服务器证书的 CA 未知
1. 服务器证书不是 CA 签名的，而是自签名的
1. 服务器配置缺少中间 CA

处理方法有
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
    val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType).apply {
        load(null, null)
    }
    for ((index, caInput) in inputStream.withIndex()) {
        val ca: X509Certificate = caInput.use {
            cf.generateCertificate(it) as X509Certificate
        }
        keyStore.setCertificateEntry("ca$index", ca)
    }
    
    val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
        init(keyStore)
    }
    val sslContext: SSLContext = SSLContext.getInstance("TLS").apply {
        init(null, tmf.trustManagers, null)
    }
    OkHttpClient().newBuilder()          
          //https请求忽略证书
          .sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
          //不验证主机信息，不建议这么做
          .hostnameVerifier(HostnameVerifier { hostname, session -> true })
          //或者 
          .hostnameVerifier(HostnameVerifier { _, session ->
                    HttpsURLConnection.getDefaultHostnameVerifier().run {
                        verify("www.baidu.com", session)
                    }
                })
          //https请求忽略证书
          .build()
    ```

网站证书导出
---

![1](okhttp3.0https证书\1.png)
![2](okhttp3.0https证书\2.png)
![3](okhttp3.0https证书\3.png)