Git修改提交历史用户邮箱
===
在git提交时可能会因为忘了修改项目的`user.name user.email`从而使用了全局的`user.name user.email`，在git中可以修改提交历史记录中的用户和邮箱
* 首先先设置项目的`user.name user.email`防止后续提交还是错误的提交人、邮箱
*  新建一个`email.sh`放在项目根目录
    ```sh
    #!/bin/sh

    git filter-branch --env-filter '

    OLD_NAME="oldname"
    OLD_EMAIL="oldemial@qq.com"
    CORRECT_NAME="newname"
    CORRECT_EMAIL="newname@163.com"

    if [ "$GIT_COMMITTER_NAME" = "$OLD_NAME" ]
    then
        export GIT_COMMITTER_NAME="$CORRECT_NAME"    
    fi
    if [ "$GIT_AUTHOR_NAME" = "$OLD_NAME" ]
    then
        export GIT_AUTHOR_NAME="$CORRECT_NAME"    
    fi
    if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]
    then
        export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
    fi
    if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]
    then
        export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
    fi
    ' --tag-name-filter cat -- --branches --tags
    ```
    这个会将旧名和旧邮箱都替换为新名字和新邮箱，如果项目只有一个人修改，想全部修改可以使用
    ```sh
    #!/bin/sh

    git filter-branch --env-filter '

    CORRECT_NAME="newname"
    CORRECT_EMAIL="newemail@163.com"
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
    ' --tag-name-filter cat -- --branches --tags
    ```
* 然后执行`./email.sh`如果显示
  ```
  Rewrite 7ff194a985d2ea86a63214f772830a82297794c2 (2/34) (1 seconds passed, remaining 16 predicted)
  Rewrite 1f46e5fe06a236bbf0054c1b459449baac0da8fe (2/34) (1 seconds passed, remaining 16 predicted)
  Rewrite e38917e51bbf94ab4306c444202a2cf447031795 (2/34) (1 seconds passed, remaining 16 predicted)
  ```
  则表示已经开始修改，如果提交历史较多会需要一些时间
* 如果执行失败提示已经有备份则执行`git filter-branch -f --index-filter 'git rm --cached --ignore-unmatch Rakefile' HEAD`
* 如果提示`Cannot rewrite branches: You have unstaged changes.`则需要将工作区内容暂存`$ git stash`然后再执行`./email.sh`
* 当上面执行完成后`git push origin --force --all`提交数据即可