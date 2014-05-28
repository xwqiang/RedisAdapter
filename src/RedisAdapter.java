import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisAdapter {
	private String ip = "127.0.0.1";
	private int port = 6379;
	private JedisPool pool =  null;
	public RedisAdapter(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	public JedisPool getPool(){
		if(pool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(100);
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
	public <T> T getQueueElement(String queueName) throws IOException, ClassNotFoundException{
		T result = null;
		Jedis jedis = null;
		jedis = pool.getResource();
		byte[] tmp = jedis.rpop(queueName.getBytes());
		result = (T) new ObjectInputStream(new ByteArrayInputStream(tmp)).readObject();
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RedisAdapter adapter = new RedisAdapter("192.168.5.82", 6379);
		String s = adapter.getPool().getResource().get("user");
		System.out.println(s);
	}

}
