import com.prestrive.com.service.ucenter.ServiceUcenterApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = ServiceUcenterApplication.class)
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Value("${JwtToken.expire}")
    Integer expire;

    @Test
    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("test","string",5, TimeUnit.SECONDS);

        System.out.println(valueOperations.get("test").toString());
        System.out.println((String) valueOperations.get("test"));
        System.out.println(valueOperations.get("test"));
        System.out.println("string".equals(valueOperations.get("test").toString()));//true
        System.out.println("string".equals(valueOperations.get("test"))); //true
    }

    @Test
    public void test2(){
        System.out.println(expire);

        System.out.println();
    }
}
