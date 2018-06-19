package miaosha;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import com.miaosha.Application;
import com.miaosha.config.redis.RedisService;
import com.miaosha.config.redis.UserKey;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {
	
	@Autowired
	private RedisService redisService;
	
	@Test
	public void test() {
		System.out.println(redisService.incr(UserKey.getById, "1"));
		System.out.println(redisService.get(UserKey.getById, "1", Long.class));
	}

}
