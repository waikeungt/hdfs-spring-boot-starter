package bio.nvwa.boot.hdfs;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;

/**
 * <p>自定义HDFS client</p>
 * <p>只是实现了普通的获得HDFS client的方法</p>
 * <p>具体实现还是使用HDFS的{@link FileSystem FileSystem}</p>
 * @see FileSystem HDFS的FileSystem
 * @see DistributedFileSystem HDFS的DistributedFileSystem
 * @author waikeungt
 * @version 1.0
 */
public class HdfsClient extends DistributedFileSystem {

	private HdfsClient() {}

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
			e.printStackTrace();
		}
	}
}
