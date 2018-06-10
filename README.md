# goods-seckill
高性能电商秒杀解决方案

### 初级方案，mysql排他锁
参考：https://www.cnblogs.com/boblogsbo/p/5602122.html

不足：所有请求都会访问mysql数据库，查询库存；并且在库存足够的情况下，在减库存的时候，还会使用排他锁。
高并发情况下，这样直接操作数据库性能比较低
### 
