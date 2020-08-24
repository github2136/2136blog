OpenCycleMap瓦片图
 ===

坐标系为`wgs84` https://www.thunderforest.com

瓦片图地址：https://tile.thunderforest.com/cycle/15/26932/13656.png，所有瓦片图均为256X256的png图片，最低缩放级别为0，瓦片图编号为`{z}/{x}/{y}`，`z`是缩放级别，`x`是从左向右编号，`y`是从右向左编号，缩放等级`0-22`，地图使用`WGS-84`坐标系

```
https://{s}.tile.thunderforest.com/{style}/{z}/{x}/{y}{scale}.{format}?apikey={apikey}
```

* s:(可选)服务地址分别有`a`/`b`/`c`三个地址

* style:地图类型例如`cycle` 或`transport`[具体查看](https://www.thunderforest.com/maps/)

* z:缩放级别

* x:横向编号

* y:纵向编号

* scale:(可选)比例尺例如`@2x`

* format:格式

  * .png:标准图片格式等于png256
  * .png256
* .png128
  * .png64
* .png32
  * .jpg:jpge压缩格式等于jpge80
* .jpg70:70%图片质量
  * .jpg80:80%图片质量
* .jpg90:90%图片质量

* apikey:签名key