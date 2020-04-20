package com.fkzm.rpcprovider;

import com.fkzm.rpcprovider.entity.Employee;
import com.fkzm.rpcprovider.utils.CacheUtils;
import com.google.gson.Gson;
import org.bouncycastle.util.Strings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
class RpcProviderApplicationTests {
    @Resource(name = "stringRedisTemplate")
    RedisTemplate<String, Object> template;
    @Resource(name = "redisMessageListenerContainer")
    RedisMessageListenerContainer listenerContainer;
    @Autowired
    JedisConnectionFactory jedisConnectionFactory;
    Logger logger = LoggerFactory.getLogger(RpcProviderApplicationTests.class);

    @Test
    void contextLoads() throws IOException {
        Gson gson = new Gson();


        Map<String, Object> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");
        map.put("emp", new Employee(112, "小黄"));
        System.out.println(template.hasKey("k5"));
        System.out.println(template.opsForValue().get("k1"));
        BoundValueOperations<String, Object> k5 = template.boundValueOps("k5");
//        k5.set(("100"));
//        k5.increment();
//        k5.increment(12);
//        k5.increment(12.32232);
//        //Jackson将 key,value 序列化成字节流存储
//        k5.append("我要这铁棒有何用");
//        System.out.println(k5.get(0, 10));//125.32232�
//        //猜测:数字序列是按 Ascii 码存储,或者 统一编码,数据库中真是存储的是字节
//        logger.error(k5.get(0, 11));
//        k5.set("1.", 1);//11..3223 ,猜想正确,这就是所谓的无状态存储,无类型,所有的针对 value操作都是基于字节序列进行的
//        logger.debug(k5.get(0, 10));
////        template.setEnableTransactionSupport(false);
//        System.out.println(k5.getAndSet("abcdef"));
//        template.delete("k5");
//        BoundValueOperations<String, Object> k51 = template.boundValueOps("k5");
//        //绑定一个 key后,即便这个 key不存在也可以操作它
//        k51.set("中国");
//        logger.info(String.valueOf(k51.size()));//返回字节长度,而非字符长度,size和 length是不一样的
//        k51.set(gson.toJson(map));
//        logger.info((String) k51.get());
//        System.out.println(gson.htmlSafe());
//        BoundListOperations<String, Object> list1 = k5.getOperations().boundListOps("list1");
//        list1.leftPush("1312312");
//        Object r=list1.index(2);
//       if(r instanceof String ){//没有这个必要,不加转换返回的都是 String(UTF-8的形式)
//           String s=(String) r;
//           System.out.println(s);
//       }
//       for(int i=0;i<10000;i++) {
//           list1.leftPushIfPresent(UuidUtils.generateUuid());
//       }

//       boolean flag=true;
//       while(flag){
//          r=list1.leftPop(2, TimeUnit.SECONDS);
//          if(r==null)flag=false;
//          else if(r instanceof String ){
//              System.out.println(r);
//          }
//
//       }

        //所有带超时的都是阻塞原语
        BoundHashOperations<String, Object, Object> map1 = template.boundHashOps("map1");
//        map1.putIfAbsent("a","3232");
//        ScanOptions options=ScanOptions.scanOptions().count(10).match("[0-9]").build();
//        //count 是告诉 redis一次增量迭代最多返回多少个元素
//        //match: 对于 scan来说,它是 dbName 的 pattern
//        //对于 hscan来说,它是 key的 pattern
//        //对于sscan来说,它是 无序 Set 的 element的 pattern
//        //对于zscan来说,它是有序 Set的 element的 pattern
//        System.out.println(options.toOptionString());
//        Cursor<Map.Entry<Object, Object>> scan = map1.scan(options);
//
//        while(scan.hasNext()){
//            Map.Entry<Object, Object> next = scan.next();
//            System.out.println(next.getKey()+": "+next.getValue());
//        }
//        scan.close();
//        map1.put("a", "中国你好");
//        System.out.println("hashKey 的 value长度: "+map1.lengthOfValue("a"));//12 ,同样是字节长度
//        Object execute = map1.getOperations().execute((RedisCallback<Object>) RedisConnection::isPipelined);
//
//        //lamda 表达式里谁被强制转型了,是 传进来的 lamda接口被转型了
//        //因为 execute有两个接收回调函数的重载,不给 lamda指定一下具体类型,会造成歧义,编译无法执行
//        System.out.println(execute);
//        execute=map1.getOperations().execute((RedisCallback<Object>)RedisConnection::keyCommands);
//        assert execute != null;
//        System.out.println(execute.toString());
////        execute=map1.getOperations().execute((RedisCallback<Object>) connection -> connection.set("abcdefg".getBytes(), "1234567".getBytes()));
//        List<Object> exec = map1.getOperations().exec();
//        exec.forEach(System.out::println);


        //  template.execute(XXX)    RedisTemplate是 对 RedisOperation 的高一层的封装,它比 BoundKeyOperations<K> 多继承了 RedisAccessor,可以根据 connectionFactory生成不同的连接
        //Redis 是单进程单线程的,只能以队列的方式模拟并发

        //虽然可以用 BoundKeyOperations 执行另一个 BoundKeyOperations,但总感觉怪怪的
        //这两个家伙都是从 template那里得到的 connectionFactory,也就是说,不能切换
//        //而 template 有 setConnectionFactory 方法,支持动态切换
//        template.execute(new SessionCallback<Object>() {
//            @Override
//            public <K,V> Object execute(RedisOperations<K,V> operations) throws DataAccessException {
//                operations.multi();
//               for(int i=0;i<100;i++){
//                   operations.convertAndSend("channel:test", String.format("我是消息%s号+%tT", i,new Date()));
//                   operations.opsForHash().put((K) "map2","大猪蹄子"+i, "我是大猪蹄子"+i);
//               }
//               operations.exec();
//
//                return "ok";
//            }
//        });

//       template.execute(new SessionCallback<Object>() {
//
//           @Override
//           public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
//               operations.multi();
//
//               Cursor<Map.Entry<Object, Object>> cursorForMap2 = operations.opsForHash().scan((K) "map2", ScanOptions.scanOptions().match("*").count(10).build());
//
//               while(cursorForMap2.hasNext()){
//                   Map.Entry<Object, Object> next = cursorForMap2.next();
//                   System.out.println("key: "+next.getKey()+"=====>"+"value; "+next.getValue());
//               }
//
//               operations.exec();
//               try {
//                   cursorForMap2.close();
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//               return null;
//           }
//       });

        template.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                operations.convertAndSend("first", "hello");
                operations.exec();

                return null;
            }
        });


    }

    @Test
    public void testMessageListener() {
        listenerContainer.addMessageListener(new MessageListenerAdapter() {

            @Override
            public void onMessage(Message message, byte[] pattern) {
                Assert.notNull(message, "消息为null1");
                byte[] channel = message.getChannel();
                byte[] body = message.getBody();
                String sChannel = Strings.fromByteArray(channel);
                String sBody = Strings.fromByteArray(body);
                System.out.printf("channel: %s,message: %s", sChannel, sBody);
            }
        }, new ChannelTopic("chatsChannel1"));

        while (true) {
            listenerContainer.start();
        }
    }

    @Test
    public void testCluster() {
//        Set<RedisNode> clusterNodes = Objects.requireNonNull(jedisConnectionFactory.getClusterConfiguration()).getClusterNodes();
//        if(!clusterNodes.isEmpty()){
//            for(RedisNode node :clusterNodes){
//                System.out.println(node.asString());
//            }
//        }
//        template.opsForValue().set("bar2", "foo2");
        RedisClusterConnection clusterConnection = jedisConnectionFactory.getClusterConnection();

//        Set<RedisNode> clusterNodes = jedisConnectionFactory.getClusterConfiguration().getClusterNodes();
        long start = System.currentTimeMillis();
//
//        clusterNodes.forEach(e->{
//
//            RedisClusterNode redisClusterNode = new RedisClusterNode(e.getHost(), e.getPort());
//            for(int i=0;i<1000000;i++) {
//               e
//            }

//        });
//        for(int i=0;i<100000;i++){
//            clusterConnection.get(new byte[]{(byte) i});
//        }
        template.opsForValue().set("prod:6", "10000");


        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setResultType(String.class);

        script.setScriptText("local count = redis.call('get',KEYS[1])" +
                " local a = tonumber(count)" +
                " local b = tonumber(ARGV[1])" +
                " if a > b then" +
                "   redis.call('set',KEYS[1],count-b)" +
                "   return '1'" +
                " end" +
                " return '0'"
        );
        String r = template.execute(script, Collections.singletonList("prod:6"), "10");

        while (true) {

            if ("0".equals(r)) break;
            System.out.println(r);
            long stop = System.currentTimeMillis();
            System.out.println("总用时: " + (stop - start));


        }
    }

    @Test
    public void testLua() {
        template.opsForValue().set("stock:100", "100000");
        Random random = new Random(System.currentTimeMillis());

        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setResultType(String.class);
        StringBuilder sb = new StringBuilder();
        sb.append("local stock = redis.call('get',KEYS[1])\n")
                .append("local inta = tonumber(stock)\n")
                .append("local intb = tonumber(ARGV[1])\n")
                .append("if inta >= intb then\n")
                .append("  redis.call('set',KEYS[1],inta-intb)\n")
                .append("  return '1'\n")
                .append("end\n")
                .append("return '0'\n");
        script.setScriptText(sb.toString());
        for (int i = 0; i < 10000; i++) {
            String r = template.execute(script, Collections.singletonList("stock:100"), (random.nextInt(100) + 1) + "");
            System.out.println(r);
        }


    }

    @Test
    public void testUtils() throws InterruptedException {

        Random random = new Random(System.currentTimeMillis());
        template.opsForValue().set("stock:101", "10000000");
//        DefaultRedisScript<String> script=new DefaultRedisScript<>(CacheUtils.getReduceStockScriptInstance());
//        script.setResultType(String.class);
//        for(int i=0;i<10000;i++){
//            String r=template.execute(script,Collections.singletonList("stock:101"), (random.nextInt(100) + 1) + "");
//            System.out.println(r);
//        }
        List<Callable<String>> callableList = new ArrayList<>();
        DefaultRedisScript<String> script = new DefaultRedisScript<>(CacheUtils.getReduceStockScriptInstance());
        script.setResultType(String.class);
        for (int i = 0; i < 100; i++) {

            callableList.add(() -> {
                for (int i1 = 0; i1 < 10000; i1++) {
                    String r = template.execute(script, Collections.singletonList("stock:101"), (random.nextInt(100) + 1) + "");
                    assert r != null;
                    System.out.println(Thread.currentThread().getId() + ":" + (r.equals("1") ? "秒杀成功" : "秒杀失败"));
                }
                return "ok";
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.invokeAll(callableList);
    }

    @Test
    public void testMiaoSha() throws InterruptedException {
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setResultType(String.class);
        StringBuilder sb = new StringBuilder();
        sb.append("local amount = redis.call('get',KEYS[1])")
                .append("local num = tonumber(ARGV[1])\n")
                .append("amount = tonumber(amount)\n")
                .append("local exist = redis.call('sismember',KEYS[2],ARGV[2])\n")
                .append("if exist ==1 then\n")
                .append("\treturn '2'\n")
                .append("end\n")
                .append("if amount-num >= 0 then\n")
                .append("\tlocal left=redis.call('decrby',KEYS[1],num)\n")
                .append("\tredis.call('sadd',KEYS[2],ARGV[2])\n")
                .append("\treturn tostring(left)\n")
                .append("end\n")
                .append("return '-1'");


        script.setScriptText(sb.toString());

        //每个用户id只能秒杀一次,数量限制在10件之内,统计客户端内统计的秒杀数量和总数量是否统一
        //统计器需要同步
        AtomicLong atomicLong = new AtomicLong();
        Random random = new Random(System.currentTimeMillis());
        template.opsForValue().set("{miaosha}:stock102", "78367000056");
        template.delete("{miaosha}:usergroup");
        template.opsForSet().add("{miaosha}:usergroup", "");
        List<Callable<Long>> clients = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            clients.add(() -> {
                String r = "";
                String amount = (random.nextInt(100000) + 1) + "";
                long num = Long.parseLong(amount);

                int tryNum = random.nextInt(10000) + 1;

                for (int j = 0; j < tryNum; j++) {

                        String userId = random.nextInt()+"";
                        r = template.execute(script, Arrays.asList("{miaosha}:stock102", "{miaosha}:usergroup"), amount, userId);


                    if ("2".equals(r)) {
                        logger.error("{}: 对不起,已经购买过了", userId);
                    } else if ("-1".equals(r)) {//有可能更新完库存后恰好为0,就会不走下边的统计
                        logger.error("对不起秒杀已经结束");
                    } else {
                        logger.error("user{}  秒杀成功,秒杀了{}件商品,目前剩余{}件商品", userId, amount, r);
                        atomicLong.updateAndGet(e -> e + num);
                    }
                }
                return 1L;
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(2000);
        executorService.invokeAll(clients);
        logger.error("总购买数: " + atomicLong.toString());


    }


}

