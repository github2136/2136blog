## Git使用流程

### 基本设置

设置全局或单项目的用户名邮箱

* 全局设置
  ```
  $ git config --global user.name "Your Name"
  $ git config --global user.email "email@example.com"
  ```
  
* 单项目设置

  ```
  $ git config user.name "Your Name"
  $ git config user.email "email@example.com"
  ```

### 项目克隆

跳转至需要下载项目的目录，使用`$ git clone`命令克隆项目

### 切换到开发分支

```
$ git switch develop
```

### 新功能开发

在开发分支基础上增加一个新功能分支

```
$ git checkout -b feature/one
```

### 提交代码

在代码完成后先增加后提交

增加

```
$ git add a.java
```

提交（提交说明为必填项）

```
$ git commit -m "提交说明"
```

### 合并代码

在新功能完成并测试后，就可以把代码合并到开发分支，使用非快进可以保留分支信息

```
$ git switch develop
$ git merge feature/one --no-ff -m "合并说明"
```

合并完成后删除开发分支

```
$ git branch -d feature/one
```

### 提交到远程服务器

```
$ git push
```

### 拉取代码

```
$ git pull
```

### 线上Bug修复

首先将正在修改的代码存储起来

```
$ git stash
```

然后切换到主分支增加一个hotfix/issue-001分支

```
$ git checkout -b hotfix/issue-001
```

完成bug修复提交代码，合并到`master`分支，删除分支

```
$ git merge hotfix/issue-001 --no-ff -m "hotfix"
```

线上有bug一般来说开发版也会有这个bug，使用`cherry-pick`将指定的提交代码到当前分支（f053bb3表示提交id）

```
$ git cherry-pick  f053bb3
```

