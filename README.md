# goods-seckill
高性能电商秒杀解决方案

### 初级方案，借助mysql排他锁完成抢购
参考：https://www.cnblogs.com/boblogsbo/p/5602122.html

这种方案一般应对并发量较为小的秒杀场景可以使用，秒杀过程中QPS在50左右的话，可以使用

不足：所有请求都会访问mysql数据库，查询库存；并且在库存足够的情况下，在减库存的时候，还会使用排他锁。
高并发情况下，这样直接操作数据库性能比较低

		记录：
		请求次数	耗时
		5000次	30秒
		
		初级goods秒杀思路：请求直接打到mysql                    
                                           
		只用mysql，这种方案在模拟5000次秒杀请求需要耗时30秒。           
		                                           
		如果在真实环境中，算上网络阻塞，可能时间更长，其实这时候数据库已经吃不消了。     
		                                           
		需要短时间内应对大量的读请求，和写请求，
		
		正常情况下30秒时间的秒杀其实是没有的，一般都会在5 - 10秒之内完成所有秒杀

### 
