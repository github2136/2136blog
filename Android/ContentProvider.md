ContentProvider
---

[官方文档](https://developer.android.google.cn/guide/topics/providers/content-providers)
内容提供程序有助于应用管理其自身和其他应用所存储数据的访问，并提供与其他应用共享数据的方法。它们会封装数据，并提供用于定义数据安全性的机制。内容提供程序是一种标准接口，可将一个进程中的数据与另一个进程中运行的代码进行连。实现内容提供程序大有好处。最重要的是，通过配置内容提供程序，您可以使其他应用安全地访问和修改您的应用数据

## 访问ContentProvider

如要访问内容提供程序中的数据，您可以客户端的形式使用应用的 `Context` 中的 `ContentResolver` 对象，从而与提供程序进行通信。`ContentResolver` 对象会与提供程序对象（即实现 `ContentProvider` 的类实例）进行通信。提供程序对象从客户端接收数据请求、执行请求的操作并返回结果。此对象的某些方法可调用提供程序对象（`ContentProvider` 某个具体子类的实例）中的同名方法。`ContentResolver` 方法可提供持续存储的基本“CRUD”（创建、检索、更新和删除）功能。

### 读取数据

以读取联系人为例

- 首先添加联系人读取权限`<uses-permission android:name="android.permission.READ_CONTACTS"/>`（注意动态权限获取）
- 使用`Context`获取`ContentResolver`，`context.contentResolver`
- 通过`contentResolver`查询记录获得`Cursor`

    ```kotlin
    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME
        ),
        null, null, null
    )
    ```

- 从`Cursor`中展示数据

    ```kotlin
    cursor?.apply {
        val wordIndex = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val frequencyIndex = getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME)
        while (moveToNext()) {
            Log.e("xxxx", "${getString(wordIndex)} ${getString(frequencyIndex)}")
        }
    }
    ```

 `query(Uri,projection,selection,selectionArgs,sortOrder)` 

| 参数          | 说明                                              |
| ------------- | ------------------------------------------------- |
| Uri           | 查询的应用及表名                                  |
| projection    | 查询的列                                          |
| selection     | Where条件，为了防止sql注入语句中需要使用`?`占位符 |
| selectionArgs | Where参数                                         |
| sortOrder     | 排序                                              |

#### Uri

**内容 URI** 是用于在提供程序中标识数据的 URI。内容 URI 包括整个提供程序的符号名称（其**授权**）和指向表的名称（**路径**）。当您调用客户端方法来访问提供程序中的表时，该表的内容 URI 将是其参数之一。

例如前面的联系人Uri为`content://com.android.contacts/contacts`

* `content://`主题
* `com.android.contacts`表示为授权信息
* `contacts`表示表名
许多提供程序均允许您将 ID 值追加至 URI 末尾，从而访问表中的单个行
`val singleUri: Uri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI, 4)`

`Uri` 、 `Uri.Builder`和`ContentUris` 类包含一些便捷方法，可用于根据字符串构建格式规范的 URI 对象。

### 插入数据

插入数据使用`ContentResolver.insert()`方法

```kotlin
val newValues = ContentValues().apply {
    put(UserDictionary.Words.APP_ID, "example.user")
    put(UserDictionary.Words.LOCALE, "en_US")
    put(UserDictionary.Words.WORD, "insert")
    put(UserDictionary.Words.FREQUENCY, "100")
    putNull(UserDictionary.Words.FREQUENCY)
}
val newUri = contentResolver.insert(UserDictionary.Words.CONTENT_URI, newValues)
```

插入后会返回新记录的`Uri`，不想设置值得列可以使用`putNull`方法

### 更新数据

更新数据也是需要用到`ContentValues`只需要添加需要修改的列，然后添加条件

```kotlin
contentResolver.update(
        UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
        updateValues,                      // the columns to update
        selectionClause,                   // the column to select on
        selectionArgs                      // the value to compare to
)
```

修改后会返回修改的记录条数

### 删除数据

删除数据使用`contentResolver.delete`，设置好`Uri`和删除条件即可，删除后会返回删除记录的条数

## ContentProvider数据类型

`ContentProvider`可以提供多种不同的数据类型。用户字典提供程序仅提供文本，但提供程序也能提供以下格式：

- 整型
- 长整型（长）
- 浮点型
- 长浮点型（双精度）

`ContentProvider`经常使用的另一种数据类型是作为 64KB 字节的数组实现的二进制大型对象 (BLOB)。您可以查看 `Cursor` 类的“获取”方法，从而查看可用数据类型。

`ContentProvider`还会为每个`Uri`维护`MIME`，如果要查询对应`Uri`的`MIME`可以使用``contentResolver.getType()`

### ContentProvider的替代形式

#### 批量访问

批量访问提供程序适用于插入大量行，或通过同一方法调用在多个表中插入行，或者通常用于以事务（原子操作）的形式跨进程边界执行一组操作。

如要在“批量模式”下访问提供程序，您可以创建 `ContentProviderOperation` 对象数组，然后使用 `ContentResolver.applyBatch()` 将其分派给内容提供程序。您需向此方法传递内容提供程序的授权，而非特定的内容 URI。如此一来，数组中的每个 `ContentProviderOperation` 对象都能适用于其他表。调用 `ContentResolver.applyBatch()` 会返回结果数组。

`ContactsContract.RawContacts` 协定类的说明包含展示批量注入的代码段。联系人管理器示例应用包含在其 `ContactAdder.java` 源文件中进行批量访问的示例。

### 通过Intent访问数据

tent 可以提供对内容提供程序的间接访问。即使应用没有访问权限，您也可通过以下方式允许用户访问提供程序中的数据：从拥有权限的应用中获取回结果 Intent，或者通过激活拥有权限的应用并允许用户使用该应用。

通过临时权限获取访问权限

即使没有适当的访问权限，您也可通过以下方式访问内容提供程序中的数据：将 Intent 发送至拥有权限的应用，然后收回包含“URI”权限的结果 Intent。以下是特定内容 URI 的权限，其将持续至接收该权限的 Activity 结束。拥有永久权限的应用会在结果 Intent 中设置标记，从而授予临时权限：

- **读取权限：**`FLAG_GRANT_READ_URI_PERMISSION`
- **写入权限：**`FLAG_GRANT_WRITE_URI_PERMISSION`

## 协定类

 协定类可定义一些常量，帮助应用使用内容 URI、列名称、Intent 操作以及内容提供程序的其他功能。提供程序不会自动包含协定类，因此提供程序的开发者需定义这些类，并将其提供给其他开发者。Android 平台中的许多提供程序在 `android.provider` 软件包中均拥有对应的协定类。

## MIME 类型引用

内容提供程序可以返回标准 MIME 媒体类型和/或自定义 MIME 类型字符串。

MIME 类型拥有以下格式

 ```
type/subtype
 ```

自定义 MIME 类型字符串（也称为“特定于供应商”的 MIME 类型）拥有更复杂的`类型`和`子类型`值。`类型`值始终为

```
vnd.android.cursor.dir
```

（多行）或

```
vnd.android.cursor.item
```

`子类型`特定于提供程序。Android 内置提供程序通常拥有简单的子类型。例如，当“通讯录”应用为电话号码创建行时，它会在行中设置以下 MIME 类型：

```
vnd.android.cursor.item/phone_v2
```

请注意，子类型值只是 `phone_v2`。

其他提供程序开发者可能会根据提供程序的授权和表名称创建自己的子类型模式。例如，假设提供程序包含列车时刻表。提供程序的授权是 `com.example.trains`，并包含表 Line1、Line2 和 Line3。在响应表 Line1 的内容 URI

```
content://com.example.trains/Line1
```

时，提供程序会返回 MIME 类型

```
vnd.android.cursor.dir/vnd.example.line1
```

在响应表 Line2 中第 5 行的内容 URI

```
content://com.example.trains/Line2/5
```

时，提供程序会返回 MIME 类型

```
vnd.android.cursor.item/vnd.example.line2
```

大多数内容提供程序都会为其使用的 MIME 类型定义协定类常量。例如，联系人提供程序协定类 `ContactsContract.RawContacts` 会为单个原始联系人行的 MIME 类型定义常量 `CONTENT_ITEM_TYPE`。

## 创建ContentProvider

### Uri

**内容 URI** 是用于在提供程序中标识数据的 URI。内容 URI 包含整个提供程序的符号名称（提供程序的**授权**）和指向表格或文件的名称（**路径**）。可选 ID 部分指向表格中的单个行。`ContentProvider` 的每个数据访问方法均将内容 URI 作为参数；您可以利用这一点确定要访问的表格、行或文件。

为了避免与其他ContentProvider冲突建议使用包名来设置授权。例如`com.example.<appname>.provider`

通常开发者会追加指向丹哥表格的路径从而根据权限创建`Uri`。例如有两个表`table1`、`table2`则`Uri`为`com.example..provider/table1` 和 `com.example..provider/table2`

按照约定`ContentProvider`会接收末尾拥有ID值得`Uri`，进而提供对表内单行的访问。同样按照约定，提供程序会将该 ID 值与表的 `_ID` 列进行匹配，并对匹配的行执行请求访问。

使用`UriMatcher`可以用来匹配不同的`Uri`类型

* `*`：匹配由任意长度的任何有效字符组成的字符串
* `#`：匹配由任意长度的数字字符组成的字符串

```kotlin
private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {   
    //将content://com.example.app.provider/table3 设置为code:1
    addURI("com.example.app.provider", "table3", 1)
    //将content://com.example.app.provider/table3/ID 设置为code:2
    addURI("com.example.app.provider", "table3/#", 2)
}
//匹配Uri返回对应的
sUriMatcher.match(uri)
```

`ContentUris.withAppendedId(uri, 1)`给`Uri`追加`Id`，`ContentUris.parseId(uri)`解析`Uri`中的`Id`，`ContentUris.appendId(uriBuilder, 1)`给`UriBuilder`追加`Id`

### ContentProvider类

创建ContentProvider 子类实现`onCreate`、`query`、`insert`、`update`、`delete`、`getType`这几个方法，除了`onCreate`以外其他方法均可由多个线程调用，因此他们必须是线程安全的，`onCreate`中应该避免执行冗长的操作

#### 获取MIME

获取`MIME`有两个方法，[MIME类型](https://www.iana.org/assignments/media-types/media-types.xhtml)

* `getType`获取数据的`MIME`
* `getStreamTypes()`返回文件的`MIME`

#### 实现权限

在`Manifest`中添加声明

```xml
<provider
    android:name=".ExampleProvider"
    android:authorities="${applicationId}.provider">
</provider>
```

* `android:authorities`：授权，在系统中表示`ContentProvider`
* `android:name`：`ContentProvider`实现类

* 权限控制

    以下属性会指定其他应用访问提供程序数据时所须的权限：

    * `android:grantUriPermssions`：临时权限标志。
    * `android:permission`：统一提供程序范围读取/写入权限。
    * `android:readPermission`：提供程序范围读取权限。
    * `android:writePermission`：提供程序范围写入权限。

* 启动和控制属性

    这些属性决定了 Android 系统启动提供程序的方式和时间、提供程序的进程特性以及其他运行时设置：

    - `android:enabled`：允许系统启动提供程序的标志。
    - `android:exported`：允许其他应用使用此提供程序的标志。
    - `android:initOrder`：在同一进程中，此提供程序相对于其他提供程序的启动顺序。
    - `android:multiProcess`：允许系统在与调用客户端相同的进程中启动提供程序的标志。
    - `android:process`：供提供程序运行的进程的名称。
    - `android:syncable`：指示提供程序的数据将与服务器上的数据进行同步的标志。

    如需了解这些属性的完整信息，请参阅开发指南的 [<provider>](https://developer.android.google.cn/guide/topics/manifest/provider-element) 元素主题。

* 信息属性
    - `android:icon`：包含提供程序图标的可绘制对象资源。该图标会出现在应用列表 (*Settings* > *Apps* > *All*) 提供程序的标签旁边。
    - `android:label`：描述提供程序和/或其数据的信息标签。该标签会出现在应用列表 (*Settings* > *Apps* > *All*) 中。

#### 数据

如果查询的数据是从`SqlLite`中获取直接返回`Cursor`即可，如果不是则可以使用`MatrixCursor`来自行拼装数据

```kotlin
val cursor = MatrixCursor(arrayOf("name", "pwd", "_id"))//添加列名
data1.forEach { d ->
    cursor.addRow(arrayOf(d.name, d.pwd, d._id))//设置值
}
```

```kotlin
companion object {
    val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider"
    val CONTENT_URI = Uri.parse("content://$AUTHORITY")
    val CONTENT_URI_DATA_1 = Uri.withAppendedPath(CONTENT_URI, "data1")
    val CONTENT_URI_DATA_2 = Uri.withAppendedPath(CONTENT_URI, "data2")

    private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "data1", 1)//默认CRUD
        addURI(AUTHORITY, "data1/#", 2)//针对某ID的记录的操作一般是修改或删除
        addURI(AUTHORITY, "data2", 3)
        addURI(AUTHORITY, "data2/#", 4)
    }
}
```

## ContentObserver
`ContentObserver`观察指定`Uri`引起的`ContentProvider`中数据变化的通知

* 新建监听回调类

    ```kotlin
    val contentObserver by lazy {
        object : ContentObserver(Handler(Looper.myLooper())) {
            override fun onChange(selfChange: Boolean) {
                //selfChange如果是自动更改通知则为true
                //旧版方法，
                super.onChange(selfChange)
            }
    
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                //新版方法
                super.onChange(selfChange, uri)
            }
        }
    }
    ```

* 在添加、修改、删除时调用`context?.contentResolver?.notifyChange(uri, null)`发送通知

* 添加监听` getContentResolver().registerContentObserver（uri,false,contentObserver）`第二个参数如果为`false`表示监听指定的`Uri`或其父串， `true`表示`Uri`的派生`Uri`也会监听。如果如果传入的`Uri`为`content://xxxx/yyy`，如果为`false`就只会监听`content://xxxx/yyy`或`content://xxxx/`，如果为`true`则还会监听`content://xxxx/yyy/1`

**以上代码表示本应用内使用**

## 多进程访问（其他应用使用）

创建`ContentProvider`与之前的代码几乎相同，只是在`manifest`的`provider`节点中需要添加`android:exported="true"`允许其他进程访问即可

如果需要增加权限控制则首先需要在`ContentProvider`项目的`manifest`中添加`<permission android:name="${applicationId}.XXX" android:protectionLevel="normal" />`声明一个权限，然后在`provider`节点增加

* `android:permission`读写权限，不能同时和下面两个同时使用，不然其他应用无法正常使用
* `android:readPermission`读取权限
* `android :writePermission`写入权限

其他应用使用时添加`<uses-permission android:name="com.example.contentproviderdemo.XXX" />`