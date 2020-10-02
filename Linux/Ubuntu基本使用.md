# Ubuntu基本使用

### 查看已安装的软件

`dpkg --list`

### 软件安装

使用`sudo apt-get install 包名`安装软件，默认为下载安装最新版，如果要下载指定版本可以在包名后添加`=versioncode`指定版本

### 软件卸载

使用`sudo apt-get --purge remove 包名`（`--purge`是可选项，写上这个属性是将软件及其配置文件一并删除，如不需要删除配置文件，可执行`sudo apt-get remove 包名）

### 列出软件各个版本

1. 使用网站查询：https://packages.ubuntu.com/
1. `apt-cache madison <<package name>>`
1. `apt-cache policy <<package name>>`这个比上面的命令查询到的信息更详细

## 常用软件

* `net-tools`：ifconfig命令用于查看本机ip等信息
* `htop`：查看内存CPU占用