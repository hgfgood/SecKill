package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.dao.SecItemDao;
import com.seckill.entry.SecItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 数据访问ceng只是访问数据，不做逻辑
 * <p>
 * Created by hgf on 16-5-28.
 */
public class SeckItemCache {

    private Logger logger = LoggerFactory.getLogger(SeckItemCache.class);

    @Autowired
    private SecItemDao secItemDao;

    private JedisPool jedisPool;

    private RuntimeSchema<SecItem> secItemRuntimeSchema = RuntimeSchema.createFrom(com.seckill.entry.SecItem.class);

    public SeckItemCache(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    // 从缓存中获取
    public SecItem getSceItemFromCache(long secitemId) {
        SecItem item = null;
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "secitem:" + secitemId;
                //序列化获取对象的二进制数据，
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    item = secItemRuntimeSchema.newMessage();   //创建空对象，redis填充。
                    ProtostuffIOUtil.mergeFrom(bytes, item, secItemRuntimeSchema);
                }

            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        return item;
    }


    public String putSceItemToCache(SecItem secitem) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "secitem:" + secitem.getItemId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(secitem, secItemRuntimeSchema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60*60;//超时时间，单位s
                String result = jedis.setex(key.getBytes(),timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }
}
