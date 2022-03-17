package bio.nvwa.boot.hdfs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：自动归还线程池对象
 *
 * @author waikeungt
 * @version 1.0
 */
@Aspect
@Component
public class HdfsPoolAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsPoolAspect.class);

	private final HdfsPool hdfsPool;

	public HdfsPoolAspect(HdfsPool hdfsPool) {
		this.hdfsPool = hdfsPool;
	}

	@Pointcut(value = "@annotation(bio.nvwa.boot.hdfs.HdfsAnnotation)")
	public void hdfsPointcut() {
	}

	private synchronized HdfsClient borrowObject() throws Exception {
		return hdfsPool.borrowObject();
	}

	@Around(value = "hdfsPointcut()")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		HdfsService hdfsService = (HdfsService) jp.getThis();
		HdfsClient hdfsClient = borrowObject();
		try {
			hdfsService.setHdfsClient(hdfsClient);
			Object o = jp.proceed();
			hdfsService.removeThreadLocal();
			hdfsPool.returnObject(hdfsClient);
			return o;
		} catch (Throwable e) {
			hdfsPool.returnObject(hdfsClient);
			LOGGER.error("执行HdfsService方法或returnObject报错", e);
			throw e;
		}
	}
}
