package bio.nvwa.boot.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>自定义HDFS client</p>
 * <p>只是实现了普通的获得HDFS client的方法</p>
 * <p>具体实现还是使用HDFS的{@link FileSystem}</p>
 * @see FileSystem HDFS的FileSystem
 * @see DistributedFileSystem HDFS的DistributedFileSystem
 * @author waikeungt
 * @version 1.0
 */
public class HdfsClient extends DistributedFileSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsClient.class);

	/**
	 * 通过{@link FileSystem}的{@link FileSystem#get(Configuration) get}方法创建
	 */
	private HdfsClient() {}

	/**
	 * 激活状态
	 */
	private boolean isActive = false;
	/**
	 * 是否激活
	 * @return true 是 false 否
	 */
	boolean isActive() {
		return isActive;
	}

	/**
	 * 设置激活
	 * @param b 激活状态
	 */
	void setActive(boolean b) {
		isActive = b;
	}

	/**
	 * 销毁
	 */
	void destroy() {
		isActive = false;
		try {
			close();
		} catch (Exception e) {
			LOGGER.error("销毁HdfsClient异常", e);
			try {
				close();
			} catch (Exception ex) {
				LOGGER.error("销毁HdfsClient再次异常", ex);
			}
		}
	}
}
