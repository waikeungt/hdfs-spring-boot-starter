package bio.nvwa.boot.hdfs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：自动归还对象池对象
 *
 * @author waikeungt
 * @version 1.0
 */
@Aspect
@Component
public class HdfsPoolAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsPoolAspect.class);

	/**
	 * 对象池
	 */
	private final HdfsPool hdfsPool;

	public HdfsPoolAspect(HdfsPool hdfsPool) {
		this.hdfsPool = hdfsPool;
	}

	/**
	 * 作用于注解了{@link HdfsAnnotation @HdfsAnnotation}的方法
	 */
	@Pointcut(value = "@annotation(bio.nvwa.boot.hdfs.HdfsAnnotation)")
	public void hdfsPointcut() {
	}

	/**
	 * 将获得对象池对象的操作锁定
	 * @return HdfsClient {@link HdfsClient}
	 * @throws Exception 获得对象池对象的操作异常
	 */
	private synchronized HdfsClient borrowObject() throws Exception {
		return hdfsPool.borrowObject();
	}

	/**
	 * 切面操作，在{@link HdfsService HdfsService}注解了{@link HdfsAnnotation @HdfsAnnotation}的方法运
	 * 行前注入{@link HdfsClient}对象，并且运行后自动归还对象到对象池
	 * @param jp ProceedingJoinPoint
	 * @return -
	 * @throws Throwable 执行HdfsService方法或returnObject异常
	 */
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
