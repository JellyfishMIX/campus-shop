package com.imooc.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriter {
    // Redis链接池对象
    private JedisPool jedisPool;

    public JedisPoolWriter(final JedisPoolConfig jedisPoolConfig, final String host, final int port) {
        try {
            jedisPool = new JedisPool(jedisPoolConfig, host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter & Setter

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
