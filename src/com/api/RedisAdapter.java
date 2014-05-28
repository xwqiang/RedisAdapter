package com.api;
import java.io.IOException;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;


public class RedisAdapter {
	private String ip = "127.0.0.1";
	private int port = 6379;
	private JedisPool pool =  null;
	public RedisAdapter(String ip,int port){
		this.ip = ip;
		this.port = port;
		pool = getPool();
	}
	public Jedis getResource(){
		return pool.getResource();
	}
	public JedisPool getPool(){
		if(pool == null){
			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxActive(100);
			config.setMaxActive(300);  
            config.setMaxIdle(10);  
            config.setMaxWait(10);  
            config.setTestOnBorrow(true);  
            config.setTestOnReturn(true); 
			pool = new JedisPool(config,ip,port);
			pool.returnResource(pool.getResource());
		}
		return pool;
	}
	public void returnResource(JedisPool pool ,Jedis redis){
		if(redis != null){
			pool.returnResource(redis);
		}
	}
	public Object getQueueElement(String queueName) {
		Object result = null;
		Jedis jedis = null;
		jedis = pool.getResource();
		byte[] bytes = jedis.rpop(queueName.getBytes());
		try {
			result = SerializeUtil.unserialize(bytes);
		} catch (Exception e) {
			pool.returnResource(jedis);
			e.printStackTrace();
		} finally{
			returnResource(pool, jedis);
		}
		return result;
	}
	public  List<Object> getQueueElement(String queueName,int num) {
		List<Object> result = null;
		Jedis jedis = null;
		jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		
		try {
			for(int i = 0 ; i < num ; i++){
				jedis.rpop(queueName.getBytes());
				pipeline.rpop(queueName.getBytes());
			}
			result = (List<Object>) pipeline.syncAndReturnAll();
		} catch (Exception e) {
			pool.returnResource(jedis);
			e.printStackTrace();
		} finally{
			returnResource(pool, jedis);
		}
		return result;
	}
	public boolean addQueueElement(String queueName,Object element){
		boolean result = false;
		Jedis jedis = null;
		jedis = pool.getResource();
		long r = 0L;
		try {
			r = jedis.lpush(queueName.getBytes(), SerializeUtil.serialize(element));
			if(r == 1){
				result = true;
			}
		} catch (IOException e) {
			pool.returnResource(jedis);
			e.printStackTrace();
		}finally{
			returnResource(pool, jedis);
		}
		return result;
	}

}
