Google瓦片图
===
google WMTS瓦片图地址为：http://mt0.google.cn/vt/lyrs=m&gl=cn&scale=1&x=26932&y=13656&z=15  
瓦片以经纬度（-180，90）为原点，所有瓦片图XY均为正数从0开始，每个等级的块数为 z<sup>2</sup>

* mt0:服务器有0-3，4个服务器
* lyrs:表示加载图层类型，可以加载多个图层使用`,`分隔即可，例如：`s,h`
  * h:道路线，道路名
  * m:底图，道路线，道路名
  * p:底图，道路名，地形
  * r:底图，道路名
  * s:卫星图，根据坐标系显示不同卫星图
  * t:仅地形
  * y:混合（卫星图，道路线，道路名）
* gl:坐标系cn表示gcj02坐标系，默认wgs84，仅在卫星图范围在中国境内时有效
* hl:底图文字
  * zh-CN:中文
  * en-US:英文
* x:瓦片横坐标
* y:瓦片纵坐标
* z:缩放级别
  * 卫星:0-14
  * 线路图:0-17
* scale:图片大小1(256),2(512),3(768),4(1024)