package nian.shop.aspect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.nian.LogTools.LogKV;
import com.nian.LogTools.LogMessage;

/**
 * @author created by NianTianlei
 * @createDate on 2018年7月15日 下午5:44:37
 */
@Aspect
@Component
public class LogAspect {
	private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut("within(nian.shop.service..*) ")
	public void log() {

	}
	
	@Before("log()")
	public void logAround(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (args != null) {
			String[] paramStrs = new String[args.length];
			for (int i = args.length - 1; i >= 0; i--) {
				paramStrs[i] = JSON.toJSONString(args[i]);
			}

			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();

			LogMessage logMessage = new LogMessage();
			logMessage.setDltag("request_in");
			List<LogKV> logKVList = Lists.newArrayList();
			LogKV classLog = new LogKV("SecondKillRequest", className + "." + methodName);
			logKVList.add(classLog);

			int index = 1;
			for (String paramStr : paramStrs) {
				try {
					Map<String, Object> map = JSON.parseObject(paramStr, Map.class);
					for (Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						LogKV logKV = new LogKV(key, value);
						logKVList.add(logKV);
					}

				} catch(Exception e) {
					String key = "arg" + index;
					LogKV logKV = new LogKV(key, paramStr);
					logKVList.add(logKV);
				}
				index++;
			}
			logMessage.setLogElements(logKVList);
			LOG.info(logMessage.toString());
		}
	}
}
