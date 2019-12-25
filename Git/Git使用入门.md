Git入门笔记
===
> 笔记是在http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000 网站学习git时所留下的
## 安装Git
***
从https://git-for-windows.github.io 或https://git-scm.com/ 下载安装版或绿色版
安装完成后从Git -> Git Bash 打开界面
安装完成后设置用户名和邮箱
```
$ git config --global user.name "Your Name"
$ git config --global user.email "email@example.com"
```
使用`--global`来做全局设置，也可对单个项目设置
### Git更新
***
更新可以在官网下载安装包更新也可以使用命令更新
* 2.17.1之前的版本`$ git update`
* 2.17.1之后的版本`$ git update-git-for-windows`
## Git使用

### 创建版本库
***
首先创建一个空目录（Git Bash默认路径在`我的文档`下，使用`cd`切换到需要创建git项目的目录下）
```
mkdir testProject
cd testProject
pwd
/c/Users/Administrator/testProject
```
`pwd`命令用于显示当前目录  
使用`$ git init`命令把该目录变成git可以管理的仓库
```
$ git init
Initialized empty Git repository in C:/Users/Administrator/testProject/.git/
```

### 添加文件到 ###
***
第一步，使用`$ git add`命令将文件暂存到仓库
```
$ git add readme.txt
```
当执行该命名没有任何显示则表示添加成功
第二步，使用命令`$ git commit`将文件提交到仓库
```
$ git commit -m "wrote a readme file"
[master (root-commit) cfe8782] wrote a readme file
 1 file changed, 1 insertion(+)
 create mode 100644 readme.txt
```
命令中的`-m`后面添加的是提交说明
`$ git commit`命令执行成功后告诉我，一个文件修改，插入了1行内容
提交文件时可以同时提交多个文件
```
$ git add file1.txt
$ git add file2.txt file3.txt
$ git commit -m 'add 3 files'
```
如果提交时忘了加上`-m "msg"`，则会显示  
![git](/Git/img/git1.png)  
此时按下`inster`在最上方输入提交说明然后，按`Esc`在最下方输入`:wq`保存并提交

### 查看状态
***
使用`$ git status`查看当前项目状态，如果没有修改则会显示为
```
$ git status
On branch master
nothing to commit, working tree clean
```
如果有内容修改则会显示
```
$ git status
On branch master
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

        modified:   demo.txt

no changes added to commit (use "git add" and/or "git commit -a")
```
上面提示`demo.txt`被修改了   
使用`$ git diff`比较工作区和暂存区，`$ git diff HEAD -- filename`比较工作区和版本库的最新版本
```
$ git diff demo.txt
diff --git a/demo.txt b/demo.txt
index 74b316d..b410c2f 100644
--- a/demo.txt
+++ b/demo.txt
@@ -1 +1,2 @@
-git demo
\ No newline at end of file
+git demo
+status
\ No newline at end of file
```
`-`后面为删除的内容，`+`后面为添加的内容  
每次修改后都要先`add`然后`commit`，当使用`status`查看状态提示`nothing to commit, working tree clean`则表示本地以及提交完成了
### 版本退回
***
当使用`commit`命令提交过多次后可以使用`log`命令查看提交记录
```
$ git log
commit 75e662b773d11355a68c7d387fb1f34522904a7b (HEAD -> master)
Author: XX <xxx@qq.com>
Date:   Fri Dec 20 15:04:30 2019 +0800

    1

commit 158629582bcd899579b50e415c8acb411f0aec48
Author: XX <xxx@qq.com>
Date:   Fri Dec 20 14:54:03 2019 +0800

    c

commit 00015d666d6c74e66d4ff5316c7f1c4e4462badc
Author: XX <xxx@qq.com>
Date:   Fri Dec 20 14:53:28 2019 +0800

    com

commit 9bfc280b8886d763580caf3bd3c35a01aa5763db
Author: XX <xxx@qq.com>
Date:   Fri Dec 20 11:38:54 2019 +0800

    commit file
```
最上面显示最近的一次提交，每次提交一条记录`commit 9bfc280b8886d763580caf3bd3c35a01aa5763db`表示提交ID（提交ID是由SHA1计算出来的值），`Author: XX <xxx@qq.com>`表示作者和作者邮箱，`Date:   Fri Dec 20 11:38:54 2019 +0800`表示提交时间，`commit file`表示提交时输入的提交说明

可以添加` --pretty=oneline`参数减少日志展示信息
```
$ git log --pretty=oneline
75e662b773d11355a68c7d387fb1f34522904a7b (HEAD -> master) 1
158629582bcd899579b50e415c8acb411f0aec48 c
00015d666d6c74e66d4ff5316c7f1c4e4462badc com
9bfc280b8886d763580caf3bd3c35a01aa5763db commit file
```
`HEAD`表示为当前版本，`HEAD^`表示上一个版本，`HEAD^^`表示上上一个，每一个`^`表示向上跳一个版本，如果向上跳的版本太多可以使用`HEAD~数字`，`HEAD~100`表示向上跳100个版本
```
$ git reset --hard HEAD^
HEAD is now at 1586295 c
```
当再次查看时提交日志时会发现新提交的版本查不到了，只要命令行窗口没关，可以在使用
```
$ git reset --hard 75e662
HEAD is now at 75e662b 1
```
回到指定版本号，版本号不用写完整，但也不能太少，不然会找到多个版本

如果命令行页面关了忘了commit id，可以使用`reflog`查看操作日志
```
$ git reflog
75e662b (HEAD -> master) HEAD@{0}: reset: moving to 75e662
1586295 HEAD@{1}: reset: moving to HEAD^
75e662b (HEAD -> master) HEAD@{2}: commit: 1
1586295 HEAD@{3}: commit: c
00015d6 HEAD@{4}: commit: com
9bfc280 HEAD@{5}: commit (initial): commit file
```
### 工作区暂存区
***
git和其他版本控制系统有个不同的地方就是，有`暂存区`，在电脑里能看见的文件都在`工作区`，当使用`add`命令后文件的修改就到`暂存区`了，当使用`commit`命令时就将修改提交到本地当前分支
### 撤销修改
***
当文件修改后发现不想修改了，只要没有使用`add`到暂存区，就可以使用`checkout --`还原之前的修改
```
$ git checkout -- demo.txt
```
当如果已经使用`add`暂存到暂存区，那需要先使用`reset`把暂存区的内容撤销然后再使用`checkout --`还原
```
$ git reset HEAD demo.txt
$ git checkout -- demo.txt
```
如果已经使用`commit`提交到本地库，先使用`$ git reset`
```
$ git reset --hard HEAD^(向上跳一个版本，也可以指定commit id)
HEAD is now at 75e662b 1

$ git reset HEAD demo.txt

$ git checkout -- demo.txt
```
### 删除文件
***
如果是为使用`add`添加到暂存区的文件，直接删除即可，如果是使用过`add`命令，可以先删除文件再使用`$ git rm`
```
rm test.txt

$ git rm test.txt
rm 'test.txt'
```
如果文件误删，可以使用`$ git checkout -- test.txt`来还原

## 远程库
***
下面将以github为例子
### 创建并使用SSH Key
***
* 创建SSH Key
  `ssh-keygen -t rsa -C "youremail@example.com"`，然后回车选择文件保存位置及密码（密码可以为空），然后就可以在我的文档目录找到.ssh目录里面有`id_rsa`/`id_rsa.pub`两个文件，`id_rsa`为私钥不可公开，`id_rsa.pub`可以公开
* 登录打开github找到`settting`->`SSH and GPG keys`->`New SSH key`输入`title`，把`id_rsa.pub`使用文本编辑器打开，内容复制到`key`，点击`Add SSH key`完成
### 创建项目
***
* 选择`New repository`->输入项目名(项目说明为选填)->选择是否公开项目->然后创建一个空项目
* 在git终端上输入`$ git remote add origin git@github.com:github2136/gitDemo.git`关联远程库，这个内容可以从github的空项目中看到提示，默认为`http`需要切换到`ssh`，`origin`为默认名称，完全可以使用其他名称，如果关联远程库不小心输入错误可以使用`$ git remote rm origin`来删除然后重新输入
* `$ git push -u origin master`将本地项目推送到远程库，由于远程库是空的，我们第一次推送master分支时，加上了-u参数，Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支和远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令。
* 之后的提交可以直接使用`$ git push origin master`
### 远程库克隆
***
选择`Clone or Download`->`Clone with SSH`复制内容->跳转至需要下载项目的目录，执行`$ git clone git@github.com:github2136/gitDemo.git`->`cd gitDemo`
## 分支管理
***
https://blog.csdn.net/ShuSheng0007/article/details/80791849

### 创建分支
***
```
$ git checkout -b dev
Switched to a new branch 'dev'
```
命令上加上`-b`表示创建并切换，等于以下两条命令
```
$ git branch dev
$ git checkout dev
Switched to branch 'dev'
```
可以使用`$ git branch`查看所有分支
```
$ git branch
* dev
master
```
前面`*`表示是当前分支  
### 合并分支
***
当在`dev`做完修改并提交(`commit`)后切换到`master`会发现提交的内容看不到，这时就需要将`dev`内容合并到`master`上
```
$ git merge dev
Updating ef18a71..7a034fc
Fast-forward
dev file.txt | 1 +
1 file changed, 1 insertion(+)
create mode 100644 dev file.txt
```
当合并时提示`Fast-forward`则表示这次是快进模式，是直接把`master`指向`dev`，合并速度非常快，但不是每次都能是`Fast-forward`。当合并完成后就可删除`dev`分支了
```
$ git branch -d dev
Deleted branch dev (was 7a034fc).
```
在`git 2.23`版本及之后的版本可以使用`switch`来切换分支
```
$ git switch -c dev
```
切换到已有的分支
```
$ git switch master
```
* 查看分支：`git branch`
* 创建分支：`git branch <name>`
* 切换分支：`git checkout <name>`或者`git switch <name>`
* 创建+切换分支：`git checkout -b <name>`或者`git switch -c <name>`
* 合并某分支到当前分支：`git merge <name>`
* 删除分支：`git branch -d <name>`

如果在合并分支时出现`E325`错误，这个错误和`git`无关是`vim`的问题，`vim`编辑文件时会生成`MERGE_MSG.swp`文件，当正常关闭时这个文件会被删除，非正常关闭时这个文件不会被删除，当提示`E325`时，此时可以执行
```
git merge -abort  // 会删除 merge_* 文件
rm .git/.MERGE_MSG.sw* // 会删除 .MERGE_MSG.sw* 文件
```
接着可以重新合并分支了。如果只有`MERGE_MSG`可以不用执行`git merge -abort`直接删除`MERGE_MSG`即可
### 解决冲突
***
当不同分支合并出现冲突时是不能快速合并，在执行`git merge`命令后文件内会出现`<<<<<<<`、`=======`、`>>>>>>>`，用来表示不同分支的内容
```
xxxxx
<<<<<<< HEAD
master
=======
feature1
>>>>>>> feature1
```
当处理完冲突时可以再次提交
```
$ git add 'dev file.txt'

$ git commit -m 'merge commit'
[master 7faec70] merge commit
```
还可以使用`$ git log --graph --pretty=oneline  --abbrev-commit`查看分支合并情况
### 分支管理策略
***
默认合并时如果可以git会使用`Fast forward`模式，但这种模式下删除分支后悔丢掉分支信息。如果强制禁用`Fast forward`，git在合并时会生成一个新的`commit`这样就能在分支历史上看到分支信息  
禁用`Fast forward`
```
$ git merge --no-ff -m 'merge with no-ff' dev
Merge made by the 'recursive' strategy.
 dev file.txt | 3 ++-
 1 file changed, 2 insertions(+), 1 deletion(-)
```
**在实际开发中，我们应该按照几个基本原则进行分支管理：**
* 首先，master分支应该是非常稳定的，也就是仅用来发布新版本，平时不能在上面干活；
* 那在哪干活呢？干活都在dev分支上，也就是说，dev分支是不稳定的，到某个时候，比如1.0版本发布时，再把dev分支合并到master上，在master分支发布1.0版本；
* 你和你的小伙伴们每个人都在dev分支上干活，每个人都有自己的分支，时不时地往dev分支上合并就可以了。
### Bug分支
***
当新开发的内容做到一半的时候出现bug，需要立即修复，可以使用`$ git stash`将当前内容贮藏起来，然后切换到需要修复分支`$ git switch master`，开一个修复bug专用分支`$ git switch -c issue-001`，修复完成后回到主分支`$ git switch master`合并到需要的分支`$ git merge --no-ff  -m 'bug fix' issue-001`，然后删除修复分支`$ git branch -d issue-001`。

然后回到开发分支`$ git switch dev`，把之前放起来的恢复，先使用`$ git stash list`查看贮藏的内容，贮藏还原有两种方法
* 使用`git stash apply`恢复，但是恢复后，stash内容并不删除，如要要删除还需要使用`git stash drop`
* 使用`git stash pop`恢复并删除

如果有多个贮藏可以使用`$ git stash apply stash@{0}`来指定恢复的贮藏

假如在`master`分支修复了一个bug，大概率说明在`dev`上也有，此时可以使用`cherry-pick`命令将修改复制到当前分支，**注意输入的commit id是bug修复提交时的ID不是`master`的commit id**

### Feature分支
***
当有一个探索性功能时从`dev`拉出一个`feature-name`分支，当开发完成合并到`dev`分支，然后删除。但是如果这个分支不需要了执行`$ git branch -d feature-name`是无法删除的，会提示该分支未合并，此时需要`$ git branch -D feature-name`强制销毁，如果使用`-D`删除那么在git上是完全看不到相关提交记录

### 多人协作
***
使用`git remote`查看远程仓库名称
```
$ git remote
origin
```
`git remote -v`可以查看详情信息
```
$ git remote -v
origin  git@github.com:github2136/gitDemo.git (fetch)
origin  git@github.com:github2136/gitDemo.git (push)
```
`push`是推送权限，`fetch`是抓取权限，这个可以给账号设置只读权限  

* 推送分支
  推送分支就是把该分支上所有提交推送到远程库。推送时要指定分支`$ git push origin master`或`$ git push origin dev`。但不是所有分支都要推送到远程库
  * `master`分支是主分支，因此要时刻与远程同步；
  * `dev`分支是开发分支，团队所有成员都需要在上面工作，所以也需要与远程同步；
  * `bug`分支只用于在本地修复bug，就没必要推到远程了，除非老板要看看你每周到底修复了几个bug；
  * `feature`分支是否推到远程，取决于你是否和你的小伙伴合作在上面开发。
* 抓取分支
  多人协作时，大家会在`master`和`dev`分支上推送各自修改，但一般`clone`时只会看到`master`分支，如果其他人要在`dev`分支上开发就必须先获取`dev`分支到本地`$ git checkout -b dev origin/dev`

当向远程推送分支时如果已经有其他人改过代码则会提示
```
$ git push origin dev
To github.com:michaelliao/learngit.git
 ! [rejected]        dev -> dev (non-fast-forward)
error: failed to push some refs to 'git@github.com:michaelliao/learngit.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
```
按这个提示从新`git pull`拉取代码合并并处理冲突后就可以重新推送代码，如果在`git pull`时提示
```
$ git pull
There is no tracking information for the current branch.
Please specify which branch you want to merge with.
See git-pull(1) for details.

    git pull <remote> <branch>

If you wish to set tracking information for this branch you can do so with:

    git branch --set-upstream-to=origin/<branch> dev
```
则需要执行`$ git branch --set-upstream-to=origin/dev dev`
### Rebase
***
当项目别人先提交到远程后，自己提交(`push`)时会提示拒绝，必须先拉取(`pull`)，合并再提交(`push`)，这时查看日志(`log`)时会发现日志历史岔开了，如果不想有这种情况，拉取合并完成后直接使用变基(`rebase`)提交这样看到的提交日志就是一条线了
## 标签管理
***
在发布新版本时通常会在版本库中打上一个标签(tag)，这样查询某个发布版时更加容易
### 添加标签
***
首先切换到需要打标签的分支，然后使用`$ git tag v1.0`，打上标签。`$ git tag`查看所有标签  
默认标签是打在最新的`commit id`上，如果要打在指定`commit id`上可以使用`$ git tag v0.9 f52c633`，标签是按字符排序而不是添加时间排序  
如果要给标签添加说明可以使用`$ git tag -a v0.1 -m "version 0.1 released" 1094adb`，如果要查看标签详情可以使用`git show <tagname>`
### 操作标签
***
删除标签使用`$ git tag -d v0.1`，标签默认只在本地，如果要推送到远程使用`git push origin <tagname>`或推送所有标签`$ git push origin --tags`。如果要删除远程标签需要先删除本地标签然后再删除远程标签`git push origin :refs/tags/<tagname>`
## 自定义git
***
### 关联不同的库


### 忽略文件
当某些文件不打算提交到git库上时，可以先在项目根目录新建一个`.gitignore`里面配置[忽略文件](https://github.com/github/gitignore)，如果忽略文件有问题忽略了不想忽略的文件可以使用`$ git check-ignore -v App.class`这个将返回哪条规则忽略了这个文件，或者使用`$ git add -f App.class`强制提交。  
如果先提交了某文件然后又想不提交忽略该文件可以执行以下语句
```
git rm -r --cached .
git add .
git commit -m 'update .gitignore'
```
忽略文件说明
```
# 此为注释 – 将被 Git 忽略

*.cs       # 忽略所有 .cs 结尾的文件
!ABC.cs    # 但 ABC.cs 除外
/BLL       # 仅仅忽略项目根目录下的 BLL 文件，不包括 subdir/BLL
build/     # 忽略 build/ 目录下的所有文件
doc/*.txt  # 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
```
### 配置别名
git可以给各种命令设置各种别名，从而减少输入`$ git config --global alias.st status`，加上`--global`表示为全局，本电脑都可使用，不加为本项目。每个项目`.git/config`里面`[alias]`表示项目设置的别名，如果不要删除对应的内容即可
```
[alias]
    last = log -1
```
全局设置在`我的文档/.gitconfig`里。
`$ git config --list`表示查看设置，`$ git config --global --list`查看全局设置