# Markdown入门笔记 #
Markdown支持HTML标签的使用，Markdown的作用不是取代HTML，Markdown的作用是用于使HTML文档更容易书写，不在Markdown范围内的标签都可以直接使用HTML撰写

# 目录

[标题](#标题)

* 标题
* 引用
* 代码区块
* 列表
* 分割线
* 链接
* 强调
* 代码
* 图片

---------- 

# 标题

**  标题  **
> 最高阶标题
 `===`
 第二阶标题
 `---`
 使用`#`，表示1-6级标题
 `# 一级标题`
 `## 二级标题`
 ……
 `###### 六级标题` 

效果：

最高阶标题
===

第二阶标题
---

使用`#`，表示1-6级标题
# 一级标题
## 二级标题
 ……
###### 六级标题

** 引用 **
允许只在段落的第一行加上`>`，区块内也允许使用Markdown语法包括标题、列表、代码块
> `>` 引用区域
 `>` 引用区域
 `>>` 嵌套引用

 > `>` 引用区域
  引用区域
 `>>` 嵌套引用

效果：

> 引用1
>> 引用2

** 代码区块 **
普通块
void main(){
	System.out.printlin("hellow world");
}
**方法1：**在代码前增加四个空格或一个制表符建立代码块

&nbsp;&nbsp;&nbsp;&nbsp;void main(){
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.printlin("hellow world");
&nbsp;&nbsp;&nbsp;&nbsp;}

效果：

	void main(){
		System.out.printlin("hellow world");
	}

**方法2：**使用\``` ```来包围代码

> \```
 void main(){
	System.out.printlin("hellow world");
}
 \```

```
void main(){
	System.out.printlin("hellow world");
}
```

** 列表 ** 
Markdown支持有序和无序列表
无序使用`*`、`+`、`-`标记
```
* a
* b
* c
```
效果

* a
* b
* c

有序列表使用数字加英文句点
```
1. a
2. b
3. c
```
但实际上标记上的数字并不影响排序
```
1. a
1. b
1. c
```
效果都为

1. a
1. b
1. c

但是建议有序从1开始
如果要在列表内使用引用那么`>`就必须缩进

** 分割线 ** 
分割线使用三个以上的`*`、`-`来插入，行内不能有其他东西但可以在符号中间插入空格

** 链接 **
链接分为行内式和参考式，不管使用哪种链接文字都是用[方括号]来标记
如果建立行内式直接在方括号后面更上圆括号并插入网址，如果想加上title文字在网址后面用双引号把文字内容括起来
```
This is [an example](http://example.com/ "Title") inline link.
[This link](http://example.net/) has no title attribute.
```
效果
This is [an example](http://example.com/ "Title") inline link.
[This link](http://example.net/) has no title attribute.
如果链接的是同样的主机资源还能使用相对路径
```
See my [About](/about/) page for details.
```
参考式是在链接文字的方括号后面再加上另一个方括号，在第二个方括号里面填入标记。
```
This is [an example][id] reference-style link.
```
然后再文件任意处将这个标记内容定义出来
```
[id]: http://example.com/  "Optional Title Here"
```
链接内容定义形式为

* 方括号（前面可以选择性地加上至多三个空格来缩进），里面输入链接文字
* 接着一个冒号
* 接着一个以上的空格或制表符
* 接着链接的网址
* 选择性地接着 title 内容，可以用单引号、双引号或是括弧包着

链接网址可以用尖括号括起来，也可将title属性放到下一行，定义的网址至在生成链接是使用，不会直接在文件中出现的。
链接标示可以有字母、数字、空白、标点符号，但**不区分大小写**

隐式链接标记可以让你省略链接标记，这种情况下链接标记等于链接文字
```
[Google][]
[Google]: http://google.com/
```
** 强调 **
使用`*`和`_`来标记强调字符，一个表示斜体，两个表示粗体，三个表示黑斜体
```
*single asterisks*
_single underscores_
**double asterisks**
__double underscores__
```
效果
*single asterisks*
_single underscores_
**double asterisks**
__double underscores__
但如果你的`*`和`_`两边都有空白的话它就会被当成普通符号

** 代码 **
如果要标记代码的话可以使用反引号`` ` ``
如果要在代码区段内容插入反引号的话可以使用多个反引号来开始和结束代码区段
在代码区段内容所有的`&`和尖括号都会被转成HTML转义字符
```
Please don't use any `<blink>` tags.
```

** 图片 **
图片与链接类似也分为行内式与参考式。
行内式
```
![Alt text](/path/to/img.jpg)
![Alt text](/path/to/img.jpg "Optional title")
```
说明

* 一个惊叹号 `!`
* 接着一个方括号，里面放上图片的替代文字
* 接着一个普通括号，里面放上图片的网址，最后还可以用引号包住并加上 选择性的 'title' 文字。

参考式
```
![Alt text][id]
[id]: url/to/image  "Optional title attribute"
```
Markdown无法指定图片宽高，如果需要可以使用普通的`<img>`

** 其他 **
自动链接
使用尖括号括起网址或邮箱他会自动转换为带链接的文字