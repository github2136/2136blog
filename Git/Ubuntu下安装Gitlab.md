Ubuntu下安装Gitlab
===
Gitlab分为[CE社区免费版](https://about.gitlab.com/install/#ubuntu?version=ce)和[EE企业收费版](https://about.gitlab.com/install/#ubuntu)

* 首先更新安装`openssh`

  ```
  sudo apt-get update
  sudo apt-get install -y curl openssh-server ca-certificates
  ```

* 安装邮件发送服务（非必要功能，可以使用`SMTP`服务替代）

  ```
  sudo apt-get install -y postfix
  ```

* 添加`gitlab`地址到软件下载库中

  ```
  curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ee/script.deb.sh | sudo bash
  ```

* 下载并安装最新的`gitlab`

  ```
  //下载最新版
  sudo EXTERNAL_URL="http://gitlab.example.com" apt-get install gitlab-ee
  //下载指定版本
  sudo EXTERNAL_URL="http://gitlab.example.com" apt-get install gitlab-ee=12.3.9-ee.0
  ```

  **EXTERNAL_URL**表示是`gitlab`地址`EXTERNAL_URL="https://gitlab.example.com"`如果要修改这个域名可以使用`sudo gedit /etc/gitlab/gitlab.rb`来编辑文件

* `sudo gitlab-ctl reconfigure`重新加载配置

* 如果出现502`Whoops, GitLab is taking too much time to respond`有可能是机器配置不够

* 卸载`gitlab`，使用`sudo apt-get --purge remove gitlab-ee`然后执行下面命令删除所有文件
  ```
sudo rm -rf /opt/gitlab
sudo rm -rf /etc/gitlab
sudo rm -rf /var/opt/gitlab
sudo rm -rf /var/log/gitlab
  ```

* 常用命令

  * `sudo gitlab-ctl reconfigure`重新加载配置
* `sudo gitlab-ctl status`查看运行状态
  
  * `sudo gitlab-ctl stop`停止服务
* `sudo gitlab-ctl start`开始服务
  
  * `sudo gitlab-ctl restart`重启服务

如果在卸载`gitlab`重新安装时卡在`ruby_block[supervise_redis_sleep] action run`则可以使用以下方法解决

* 按住CTRL+C强制结束
* 执行`sudo systemctl restart gitlab-runsvdir`
* 执行`sudo gitlab-ctl reconfigure`，如果有出现红色错误可以尝试再次执行`sudo gitlab-ctl reconfigure`

## Gitlab汉化

`gitlab`新版中已经有大部分的中文汉化，点击`右上角头像`->`Setting`->`preferences`->`Language`。

如果要打汉化补丁可查看

* 首先查看`gitlab`版本``https://gitlab.com/xhang/gitlab`，此补丁处于`12.3`版本
