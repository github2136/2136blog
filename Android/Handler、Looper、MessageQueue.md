### Looper
用于为线程循环读取消息的类。线程默认情况下没有与Looper相关的消息循环。
### Handler
用于发送和处理与线程的MessageQueue相关的Message和Runnable，每个Handler实例都与单个线程和该线程的MessageQueue关联。当创建一个新的Handler时它就会绑定到创建它的线程/消息队列上。
### MessageQueue
消息队列，消息不能直接添加到MessageQueue，而是通过与Looper关联的Handler。

每个线程只能有一个Looper，每个Looper只有一个MessageQueue，每个MessageQueue可对应多个Handler，只能向UI线程的Handler发送修改view的信息

http://blog.csdn.net/jiangwei0910410003/article/details/17021809