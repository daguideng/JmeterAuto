package com.atguigu.gmall.controller.redisUtil;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 集群环境下Jedis操作
 */

//@RestController
public class ClusterPassWd {


    @Value("${jedis.node_0}")
    private static String jedis_node_0;

    @Value("${jedis.node_1}")
    private static String jedis_node_1;

    @Value("${jedis.node_2}")
    private static String jedis_node_2;

    @Value("${jedis.node_3}")
    private static String jedis_node_3;

    @Value("${jedis.node_4}")
    private static String jedis_node_4;

    @Value("${jedis.node_5}")
    private static String jedis_node_5;

    @Value("${redis.pawd}")
    private static String redis_pawd;

    private static JedisCluster jedis;




    public static void  jedisBasePwd() {
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort(jedis_node_0.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));
        hostAndPortsSet.add(new HostAndPort(jedis_node_1.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));
        hostAndPortsSet.add(new HostAndPort(jedis_node_2.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));
        hostAndPortsSet.add(new HostAndPort(jedis_node_3.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));
        hostAndPortsSet.add(new HostAndPort(jedis_node_4.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));
        hostAndPortsSet.add(new HostAndPort(jedis_node_5.split(":")[0], Integer.valueOf(jedis_node_0.split(":")[1])));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        jedis = new JedisCluster(hostAndPortsSet,jedisPoolConfig);
     //   return jedis ;
    }

    /**
     * 测试key:value数据
     * 集群中flushDB、keys废弃
     */

    @RequestMapping(value = "/passwd/setkey", method = RequestMethod.GET)
    public Map<String,Object> testKey(@RequestParam(required = true) String  key, @RequestParam(required = true)  String value) throws InterruptedException {

       this.jedisBasePwd();
        //System.out.println("清空数据："+jedis.flushDB());
        System.out.println("判断某个键是否存在："+jedis.exists(key));
        System.out.println("新增<'key','value'>的键值对："+jedis.set(key, value));
        System.out.println("是否存在:"+jedis.exists(key));
        //Set<String> keys = jedis.keys("*");
        System.out.println("设置键key的过期时间为30m:"+jedis.expire(key, 1800));
       // TimeUnit.SECONDS.sleep(2); // 线程睡眠2秒System.out.println("查看键username的剩余生存时间："+jedis.ttl("username"));
        System.out.println("查看键key的剩余生存时间："+jedis.ttl(key));
        System.out.println("移除键keye的生存时间："+jedis.persist(key));
        System.out.println("查看键key的剩余生存时间："+jedis.ttl(key));
        System.out.println("查看键key所存储的值的类型："+jedis.type(key));

        Map<String,Object> result = new HashMap();
        Map<String,String> map = new HashMap();
        map.put(key,value);
        result.put("result",map);
        return result ;

    }

    /***
     * 字符串操作
     * memcached和redis同样有append的操作，但是memcached有prepend的操作，redis中并没有。
     * 集群中flushDB、keys、del(多个值)、mset(多个值)废弃
     * @throws InterruptedException
     */
    public void testString() throws InterruptedException {
        this.jedisBasePwd();
        //jedis.flushDB();
        System.out.println("===========增加数据===========");
        System.out.println(jedis.set("key1","value1"));
        System.out.println(jedis.set("key2","value2"));
        System.out.println(jedis.set("key3", "value3"));
        System.out.println("删除键key2:"+jedis.del("key2"));
        System.out.println("获取键key2:"+jedis.get("key2"));
        System.out.println("修改key1:"+jedis.set("key1", "value1Changed"));
        System.out.println("获取key1的值："+jedis.get("key1"));
        System.out.println("在key3后面加入值："+jedis.append("key3", "End"));
        System.out.println("key3的值："+jedis.get("key3"));
        //命令的时候才会去连接连接，集群中连接是对一个节点连接，不能判断多个key经过crc16算法所对应的槽在一个节点上，不支持多key获取、删除
        //System.out.println("增加多个键值对："+jedis.mset("key01","value01","key02","value02"));
        //System.out.println("获取多个键值对："+jedis.mget("key01","key02","key03"));
        //System.out.println("获取多个键值对："+jedis.mget("key01","key02","key03","key04"));
        //System.out.println("删除多个键值对："+jedis.del(new String[]{"key01","key02"}));
        //System.out.println("获取多个键值对："+jedis.mget("key01","key02","key03"));

        //jedis.flushDB();
        System.out.println("===========新增键值对防止覆盖原先值==============");
        System.out.println(jedis.setnx("key1", "value1"));
        System.out.println(jedis.setnx("key2", "value2"));
        System.out.println(jedis.setnx("key2", "value2-new"));
        System.out.println(jedis.get("key1"));
        System.out.println(jedis.get("key2"));

        System.out.println("===========新增键值对并设置有效时间=============");
        System.out.println(jedis.setex("key3", 2, "value3"));
        System.out.println(jedis.get("key3"));
        TimeUnit.SECONDS.sleep(3);
        System.out.println(jedis.get("key3"));

        System.out.println("===========获取原值，更新为新值==========");//GETSET is an atomic set this value and return the old value command.
        System.out.println(jedis.getSet("key2", "key2GetSet"));
        System.out.println(jedis.get("key2"));
        System.out.println("获得key2的值的字串："+jedis.getrange("key2", 2, 4)); // 相当截取字符串的第二个位置-第四个位置的字符串
    }

    /***
     * 整数和浮点数
     */
    public void testNumber() {
         this.jedisBasePwd();
        jedis.set("key1", "1");
        jedis.set("key2", "2");
        jedis.set("key3", "2.3");
        System.out.println("key1的值："+jedis.get("key1"));
        System.out.println("key2的值："+jedis.get("key2"));
        System.out.println("key1的值加1："+jedis.incr("key1"));
        System.out.println("获取key1的值："+jedis.get("key1"));
        System.out.println("key2的值减1："+jedis.decr("key2"));
        System.out.println("获取key2的值："+jedis.get("key2"));
        System.out.println("将key1的值加上整数5："+jedis.incrBy("key1", 5));
        System.out.println("获取key1的值："+jedis.get("key1"));
        System.out.println("将key2的值减去整数5："+jedis.decrBy("key2", 5));
        System.out.println("获取key2的值："+jedis.get("key2"));
        System.out.println("key3的值："+jedis.get("key3"));
        // 这里会报错，因为key3不是整数不能做计算:redis.clients.jedis.exceptions.JedisDataException: ERR value is not an integer or out of range
        // System.out.println("key2的值减1："+jedis.decr("key3"));
    }

    /***
     * 列表
     */
    public void testList() {
         this.jedisBasePwd();
        System.out.println("===========添加一个list===========");
        jedis.lpush("collections", "ArrayList", "Vector", "Stack", "HashMap", "WeakHashMap", "LinkedHashMap");
        jedis.lpush("collections", "HashSet"); // 叠加
        jedis.lpush("collections", "TreeSet"); // 叠加
        jedis.lpush("collections", "TreeMap"); // 叠加
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));//-1代表倒数第一个元素，-2代表倒数第二个元素
        System.out.println("collections区间0-3的元素："+jedis.lrange("collections",0,3)); // 前面4个值
        System.out.println("===============================");
        // 删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
        System.out.println("删除指定元素个数："+jedis.lrem("collections", 2, "HashMap"));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("删除下表0-3区间之外的元素："+jedis.ltrim("collections", 0, 3));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("collections列表出栈（左端）："+jedis.lpop("collections"));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("collections添加元素，从列表右端，与lpush相对应："+jedis.rpush("collections", "EnumMap"));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("collections列表出栈（右端）："+jedis.rpop("collections"));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("修改collections指定下标1的内容："+jedis.lset("collections", 1, "LinkedArrayList"));
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        System.out.println("===============================");
        System.out.println("collections的长度："+jedis.llen("collections"));
        System.out.println("获取collections下标为2的元素："+jedis.lindex("collections", 2));
        System.out.println("===============================");
        jedis.lpush("sortedList", "3","6","2","0","7","4");
        System.out.println("sortedList排序前："+jedis.lrange("sortedList", 0, -1));
        System.out.println(jedis.sort("sortedList"));
        System.out.println("sortedList排序后："+jedis.lrange("sortedList", 0, -1));
    }

    /***
     * set集合
     */

    public void testSet() {
         this.jedisBasePwd();
        System.out.println("============向集合中添加元素============");
        System.out.println(jedis.sadd("eleSet", "e1","e2","e4","e3","e0","e8","e7","e5"));
        System.out.println(jedis.sadd("eleSet", "e6"));
        System.out.println(jedis.sadd("eleSet", "e6")); // 返回0，集合中已经存在
        System.out.println("eleSet的所有元素为："+jedis.smembers("eleSet"));
        System.out.println("删除一个元素e0："+jedis.srem("eleSet", "e0"));
        System.out.println("eleSet的所有元素为："+jedis.smembers("eleSet"));
        System.out.println("删除两个元素e7和e6："+jedis.srem("eleSet", "e7","e6"));
        System.out.println("eleSet的所有元素为："+jedis.smembers("eleSet"));
        System.out.println("随机的移除集合中的一个元素："+jedis.spop("eleSet"));
        System.out.println("随机的移除集合中的一个元素："+jedis.spop("eleSet"));
        System.out.println("eleSet的所有元素为："+jedis.smembers("eleSet"));
        System.out.println("eleSet中包含元素的个数："+jedis.scard("eleSet"));
        System.out.println("e3是否在eleSet中："+jedis.sismember("eleSet", "e3"));
        System.out.println("e1是否在eleSet中："+jedis.sismember("eleSet", "e1"));
        System.out.println("e5是否在eleSet中："+jedis.sismember("eleSet", "e5"));

        // 集群下并存会报错：redis.clients.jedis.exceptions.JedisClusterException: No way to dispatch this command to Redis Cluster because keys have different slots.
        // Redis集群，从key1集合与key2集合并存、交集、差集，两个键经过crc16算法可能有不同的槽。
        /*System.out.println("=================================");
        System.out.println(jedis.sadd("eleSet1", "e1","e2","e4","e3","e0","e8","e7","e5"));
        System.out.println(jedis.sadd("eleSet2", "e1","e2","e4","e3","e0","e8"));
        System.out.println("将eleSet1中删除e1并存入eleSet3中："+jedis.smove("eleSet1", "eleSet3", "e1"));
        System.out.println("将eleSet1中删除e2并存入eleSet3中："+jedis.smove("eleSet1", "eleSet3", "e2"));
        System.out.println("eleSet1中的元素："+jedis.smembers("eleSet1"));
        System.out.println("eleSet3中的元素："+jedis.smembers("eleSet3"));*/

        /*System.out.println("============集合运算=================");
        System.out.println("eleSet1中的元素："+jedis.smembers("eleSet1"));
        System.out.println("eleSet2中的元素："+jedis.smembers("eleSet2"));
        System.out.println("eleSet1和eleSet2的交集:"+jedis.sinter("eleSet1","eleSet2"));
        System.out.println("eleSet1和eleSet2的并集:"+jedis.sunion("eleSet1","eleSet2"));
        System.out.println("eleSet1和eleSet2的差集:"+jedis.sdiff("eleSet1","eleSet2"));*/
        jedis.del("eleSet");
        jedis.del("eleSet1");
        jedis.del("eleSet2");
        jedis.del("eleSet3");
    }

    /***
     * 散列
     */

    public void testHash() {
         this.jedisBasePwd();
        Map<String,String> map = new HashMap<String,String>();
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
        map.put("key4","value4");
        jedis.hmset("hash",map);
        jedis.hset("hash", "key5", "value5");
        System.out.println("散列hash的所有键值对为："+jedis.hgetAll("hash"));//return Map<String,String>
        System.out.println("散列hash的所有键为："+jedis.hkeys("hash"));//return Set<String>
        System.out.println("散列hash的所有值为："+jedis.hvals("hash"));//return List<String>
        System.out.println("将key6保存的值加上一个整数，如果key6不存在则添加key6："+jedis.hincrBy("hash", "key6", 6));
        System.out.println("散列hash的所有键值对为："+jedis.hgetAll("hash"));
        System.out.println("将key6保存的值加上一个整数，如果key6不存在则添加key6："+jedis.hincrBy("hash", "key6", 3));
        System.out.println("散列hash的所有键值对为："+jedis.hgetAll("hash"));
        System.out.println("删除一个或者多个键值对："+jedis.hdel("hash", "key2"));
        System.out.println("散列hash的所有键值对为："+jedis.hgetAll("hash"));
        System.out.println("散列hash中键值对的个数："+jedis.hlen("hash"));
        System.out.println("判断hash中是否存在key2："+jedis.hexists("hash","key2"));
        System.out.println("判断hash中是否存在key3："+jedis.hexists("hash","key3"));
        System.out.println("获取hash中的值："+jedis.hmget("hash","key3"));
        System.out.println("获取hash中的值："+jedis.hmget("hash","key3","key4"));
    }

    /**
     * 有序集合
     */

    public void testSortedSet() {
        this.jedisBasePwd();
        Map<String,Double> map = new HashMap<String,Double>();
        map.put("key2",1.2);
        map.put("key3",4.0);
        map.put("key4",5.0);
        map.put("key5",0.2);
        // 将一个或多个 member 元素及其 score 值加入到有序集 key 当中，如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值
        // score 值可以是整数值或双精度浮点数
        System.out.println(jedis.zadd("zset", 3,"key1"));
        System.out.println(jedis.zadd("zset",map));
        System.out.println("zset中的所有元素："+jedis.zrange("zset", 0, -1));
        System.out.println("zset中的所有元素："+jedis.zrangeWithScores("zset", 0, -1));
        System.out.println("zset中的所有元素："+jedis.zrangeByScore("zset", 0,100));
        System.out.println("zset中的所有元素："+jedis.zrangeByScoreWithScores("zset", 0,100));
        System.out.println("zset中key2的分值："+jedis.zscore("zset", "key2"));
        System.out.println("zset中key2的排名："+jedis.zrank("zset", "key2"));
        System.out.println("删除zset中的元素key3："+jedis.zrem("zset", "key3"));
        System.out.println("zset中的所有元素："+jedis.zrange("zset", 0, -1));
        System.out.println("zset中元素的个数："+jedis.zcard("zset"));
        System.out.println("zset中分值在1-4之间的元素的个数："+jedis.zcount("zset", 1, 4));
        System.out.println("key2的分值加上5："+jedis.zincrby("zset", 5, "key2"));
        System.out.println("key3的分值加上4："+jedis.zincrby("zset", 4, "key3"));
        System.out.println("zset中的所有元素："+jedis.zrange("zset", 0, -1));
    }

    /**
     * 排序
     */
    public void testSort() {
        this.jedisBasePwd();
        jedis.lpush("collections", "ArrayList", "Vector", "Stack", "HashMap", "WeakHashMap", "LinkedHashMap");
        System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
        SortingParams sortingParameters = new SortingParams();
        // 当数据集中保存的是字符串值时，你可以用 ALPHA,默认是升序
        System.out.println("alpha排序方式：" + jedis.sort("collections",sortingParameters.alpha()));
        System.out.println("===============================");
        jedis.lpush("sortedList", "3","6","2","0","7","4");
        System.out.println("sortedList排序前："+jedis.lrange("sortedList", 0, -1));
        System.out.println("升序："+jedis.sort("sortedList", sortingParameters.asc()));
        System.out.println("降序："+jedis.sort("sortedList", sortingParameters.desc()));
        System.out.println("===============================");
        // 集群下不支持分割表排序
        /*jedis.lpush("userlist", "33");
        jedis.lpush("userlist", "22");
        jedis.lpush("userlist", "55");
        jedis.lpush("userlist", "11");
        jedis.hset("user:66", "name", "66");
        jedis.hset("user:55", "name", "55");
        jedis.hset("user:33", "name", "33");
        jedis.hset("user:22", "name", "79");
        jedis.hset("user:11", "name", "24");
        jedis.hset("user:11", "add", "beijing");
        jedis.hset("user:22", "add", "shanghai");
        jedis.hset("user:33", "add", "guangzhou");
        jedis.hset("user:55", "add", "chongqing");
        jedis.hset("user:66", "add", "xi'an");
        sortingParameters = new SortingParams();
        // 符号 "->" 用于分割哈希表的键名(key name)和索引域(hash field)，格式为 "key->field"
        sortingParameters.get("user:*->name");
        sortingParameters.get("user:*->add");
        System.out.println(jedis.sort("userlist",sortingParameters));*/
    }
}