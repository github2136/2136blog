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