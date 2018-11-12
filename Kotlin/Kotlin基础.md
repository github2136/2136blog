# Kotlin 基础
Kotlin文件结尾为kt
## 定义包
包声明在文件顶部，目录与包名的结构无需匹配
```kotlin 
package my.demo

import java.util.*
```
如果导入的包名出现冲突可使用`as`消除歧义
```
import foo.Bar // Bar 可访问
import bar.Bar as bBar // bBar 代表“bar.Bar”
```
## 定义函数
函数关键字为`fun`，函数参数使用 Pascal 表示法定义，即 name: type。参数用逗号隔开。每个参数必须有显式类型
```kotlin
fun printSum(a: Int, b: Int) {
    println("sum of $a and $
}
```
函数都有返回值，默认返回值为`Unit`可不写如果有返回值
```kotlin
fun sum(a: Int, b: Int): Int {
    return a + b
}
```
如果将表达式作为函数体、返回值类型自动推断的函数
```kotlin
fun double(x: Int): Int = x * 2
```
当返回值可由编译器推断时，显示声明返回是可选的
```kotlin
fun double(x: Int) = x * 2
```
参数可以有默认值，这样可以减少重载
```kotlin
fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size) { …… }
```
当重写方法时不可参数不可有默认值
```kotlin
open class A {
    open fun foo(i: Int = 10) { …… }
}

class B : A() {
    override fun foo(i: Int) { …… }  // 不能有默认值
}
```
如果一个默认参数在一个无默认值的参数钱，那么可以通过参数名来设置值
```kotlin
fun foo(bar: Int = 0, baz: Int) { …… }

foo(baz = 1) // 使用默认值 bar = 0
```

```kotlin
fun foo(bar: Int = 0, baz: Int = 1, qux: () -> Unit) { …… }

foo(1) { println("hello") } // 使用默认值 baz = 1
foo { println("hello") }    // 使用两个默认值 bar = 0 与 baz = 1
```

使用`*`将可变数量参数已命名形式传入
```kotlin
fun foo(vararg strings: String) { …… }

foo(strings = *arrayOf("a", "b", "c"))
```