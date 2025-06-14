package com.mall.common.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mall.common.service.RedisService;
import com.mall.common.service.impl.RedisServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import java.time.Duration;

@Configuration
public class BaseRedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory ,
                                                      KryoRedisSerializer kryoRedisSerializer){
        RedisTemplate<String , Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(kryoRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(kryoRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    static public class KryoRedisSerializer implements RedisSerializer<Object>{
        private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(()->{
            Kryo kryo =new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setReferences(true);
            return kryo;
        });

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            if (o==null){
                return null;
            }
            try(Output output = new Output(1024,-1)){
                Kryo kryo = kryoThreadLocal.get();
                kryo.writeClassAndObject(output,o);
                return output.toBytes();
            }
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes==null || bytes.length ==0){
                return null;
            }
            try(Input input = new Input(bytes)){
                Kryo kryo = kryoThreadLocal.get();
                return kryo.readClassAndObject(input);
            }
        }

        public void remove(){
            kryoThreadLocal.remove();
        }
    }

    @Bean
    public KryoRedisSerializer kryoRedisSerializer(){
        return new KryoRedisSerializer();
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory,KryoRedisSerializer kryoRedisSerializer){
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(kryoRedisSerializer))
                .entryTtl(Duration.ofDays(1));

        return new RedisCacheManager(cacheWriter,redisCacheConfiguration);
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

}
