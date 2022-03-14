package bio.nvwa.boot.hdfs;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * HDFS对象池
 * <ul>
 *     使用方法，要注入本类对象
 *     <li>直接使用本类提供的快捷方法，无须获得HdfsClient和returnObject，因为本类实现了</li>
 *     <li>用{@link HdfsPool#borrowObject() borrowObject()}方法获得{@link HdfsClient}，该类提供了一些方法，并且可以使用{@link HdfsClient}记得returnObject</li>
 * </ul>
 * @author waikeungt
 * @version 1.0
 * @date 2020/3/7 9:35
 */
public class HdfsPool extends GenericObjectPool<HdfsClient> {

	public HdfsPool(PooledObjectFactory<HdfsClient> factory) {
		super(factory);
	}

	public HdfsPool(PooledObjectFactory<HdfsClient> factory, GenericObjectPoolConfig<HdfsClient> config) {
		super(factory, config);
	}

	public HdfsPool(PooledObjectFactory<HdfsClient> factory, GenericObjectPoolConfig<HdfsClient> config, AbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}
}
