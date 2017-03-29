Git入门笔记
=======
> 笔记是在http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000 网站学习git时所留下的

# 目录 #
<!-- MarkdownTOC -->

- 安装Git
- Git使用
	- 创建版本库
	- 添加文件到

<!-- /MarkdownTOC -->


## 安装Git ##
从https://git-for-windows.github.io 或https://git-scm.com/ 下载安装版或绿色版
安装完成后从Git -> Git Bash 打开界面
安装完成后设置用户名和邮箱
```
$ git config --global user.name "Your Name"
$ git config --global user.email "email@example.com"
```
使用`--global`来做全局设置，也可对单个项目设置

## Git使用 ##

### 创建版本库 ###
首先创建一个空目录
```
$ mkdir testProject
$ cd testProject
$ pwd
/c/Users/Administrator/testProject
```
使用`git init`命令把该目录变成git可以管理的仓库
```
$ git init
Initialized empty Git repository in C:/Users/Administrator/testProject/.git/
```

### 添加文件到 ###

第一步，使用`git add`命令将文件添加到仓库
```
$ git add readme.txt
```
当执行该命名没有任何显示则表示添加成功
第二步，使用命令`git commit`将文件提交到仓库
```
$ git commit -m "wrote a readme file"
[master (root-commit) cfe8782] wrote a readme file
 1 file changed, 1 insertion(+)
 create mode 100644 readme.txt
```
命令中的`-m`后面添加的是提交说明
`git commit`命令执行成功后告诉我，一个文件修改，插入了1行内容
提交文件时可以同时提交多个文件
```
$ git add file1.txt
$ git add file2.txt file3.txt
$ git commit -m 'add 3 files'
```
