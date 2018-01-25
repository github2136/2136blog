# Python
#### 变量
直接定义不需要使用关键字
```
name = 'abc'
```
#### 文件头部编码
```
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
```
#### 集合
* list 使用中括号，有序，可变
```
classmates = ['Michael', 'Bob', 'Tracy']
classmates[0]#第一个
classmates[-1]#最后一个
classmates.append('Adam')#添加至尾部
classmates.insert(1, 'Jack')##添加至指定位置
classmates.pop()#移除最后一个
classmates.pop(1)#移除下标
classmates[1] = 'Sarah'#替换
```
* tuple 元组使用小括号，有序，不可变
```
classmates = ('Michael', 'Bob', 'Tracy')
#定义一个元素时使用
t = (1,)
```
* dict key-value集合无序，使用大括号，只能放入不可变对象
```
d = {'Michael': 95, 'Bob': 75, 'Tracy': 85}
d['Michael']#获取
d['Adam'] = 6#赋值
'Thomas' in d#判定key是否存在
#获取，不存在返回None或指定值
d.get('Thomas')
d.get('Thomas', -1)
d.pop('Bob')#移除某key-value
```
* set 无序不可重复集合，使用小括号中括号，只能放入不可变对象
```
 s = set([1, 2, 3])
 s.add(4)#添加
 s.remove(4)#移除
```
#### 条件判断
```
if <条件判断1>:
    <执行1>
elif <条件判断2>:
    <执行2>
elif <条件判断3>:
    <执行3>
else:
    <执行4>
```
#### 循环
* for
```
for x in [集合]：
    #操作
```
* while
```
while <条件判断>:
    #操作
```
`break`跳出循环 `continue`执行下一次循环