package com.test;

import com.api.RedisAdapter;
import com.model.User;

public class Test {
	public static void main(String[] args) {
		RedisAdapter adapter = new RedisAdapter("192.168.5.82", 6379);
		long time = System.nanoTime();
//		for(int i = 0 ; i < 10 ; i ++){
//			User u = new User();
//			u.setId(i);
//			adapter.addQueueElement("mytest", u);
//		}
		System.out.println(System.nanoTime() - time);
		Long len = adapter.getResource().llen("mytest".getBytes());
		
		
		User ua = (User) adapter.getQueueElement("mytest");
		System.out.println(ua.getId());
		System.out.println(len);
//		String s = adapter.getPool().getResource().get("user");
//		System.out.println(s);
	}
}
