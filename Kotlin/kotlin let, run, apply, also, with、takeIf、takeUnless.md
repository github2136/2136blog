## let
在函数内操作对象，并返回值。`it`表示为当前对象
```kotlin
val len = text?.let {
    println("get length of $it")
    it.length
} ?: 0
println("Length of $text is $len")
```
## run
在函数内操作对象，并返回值，run可以在不使用`this`的情况下调用属性、方法，`this`表示为当前对象
```kotlin
val len = text?.run {
    println("get length of $this")
    length //`this` can be omitted
} ?: 0
println("Length of $text is $len")
```

```kotlin
val date: Int = Calendar.getInstance().run {
    set(Calendar.YEAR, 2030)
    get(Calendar.DAY_OF_YEAR) //return value of run
}
println(date)
```
## also
在函数内操作对象，并返当前对象，`it`表示当前对象
```kotlin
val result = "testLet".also {            
    println(it.length)
}
println(result)
```

## apply
在函数内操作对象，并返当前对象，可以在不使用`it`的情况下调用属性、方法，`it`表示当前对象
```kotlin 
ArrayList<String>().apply {
    add("testApply")
    add("testApply")
    add("testApply")
    println("this = " + this)
}.let { println(it) }
```
## with
`with`里面加入对象，在函数内操作，可以在不使用`this`的情况下调用属性、方法,`this`表示当前对象，直接返回对象
```kotlin
with(ArrayList<String>()) {
    add("testWith")
    add("testWith")
    add("testWith")
    println("this = " + this)
}.let { println(it) }
```
## takeIf
在函数里操作对象，如果最后返回`true`则返回对象否则返回`null`，`it`表示当前对象
```kotlin
val date1 = Date().takeIf {
    // 是否在2018年元旦后
    it.before(Date(2018 - 1900, 0, 1))
}
println("date = $date1")
```

## takeUnless
与`takeIf`相反
```
val date = Date().takeUnless {
    // 是否在2018年元旦后
    it.after(Date(2018 - 1900, 0, 1))
}

println("date = $date")
```
https://www.jianshu.com/p/5c4a954d2b2c