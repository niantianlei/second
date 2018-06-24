package nian.shop.task;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nian.shop.bloomFilter.BloomFilter;
import nian.shop.bloomFilter.BloomFilterCache;
import nian.shop.dao.SecondUserDao;
import nian.shop.entity.SecondUser;

/**
* @author created by NianTianlei
* @createDate on 2018年6月24日 下午7:59:09
*/
@Component
public class BloomFilterTask {
	private static Logger log = LoggerFactory.getLogger(BloomFilterTask.class);
	@Autowired
	SecondUserDao secondUserDao;
	
	@PostConstruct
	public void initBloomFilterCache() {
		log.info("初始化bloomFilter");
		List<SecondUser> list = secondUserDao.queryAllSecondUser();
		BloomFilter bloomFilter = new BloomFilter();
		list.forEach(secondUser -> {
			bloomFilter.add(secondUser.getId().toString());
		});
		BloomFilterCache.bloomFilter = bloomFilter;
		log.info("bloomFilter初始化完毕！");
//		System.out.println("测试结果1：" + BloomFilterCache.bloomFilter.check("13000000981"));
//		Long phoneTest = 33000000981l;
//		System.out.println("测试结果2：" + BloomFilterCache.bloomFilter.check(phoneTest.toString()));

	}
}
