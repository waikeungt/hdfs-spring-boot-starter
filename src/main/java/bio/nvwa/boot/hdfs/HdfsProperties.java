package bio.nvwa.boot.hdfs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>对象池配置</p>
 * ConfigurationProperties是为了避免有些没写
 * @author waikeungt
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "hadoop.hdfs")
public class HdfsProperties {

	/**
	 * 对象池最大空闲数
	 */
	private int maxIdle = 5;
	/**
	 * 对象池最大总数
	 */
	private int maxTotal = 20;
	/**
	 * 对象池最小空闲数
	 */
	private int minIdle = 2;
	/**
	 * 对象池初始化连接数
	 */
	private int initialSize = 3;
	/**
	 * hdfs defaultFs配置
	 */
	private String defaultFs;
	/**
	 * hdfs HA的nameservices设置，非HA可不写
	 */
	private String nameservices;
	/**
	 * hdfs HA设置，非HA可不写
	 */
	private String ha;
	/**
	 * hdfs HA的rpcAddress设置，非HA可不写
	 */
	private String rpcAddress;

	/**
	 * 获得对象池最大空闲数
	 * @return 对象池最大空闲
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * 设置对象池最大空闲数
	 * @param maxIdle 对象池最大空闲数
	 */
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * 获得对象池最大总数
	 * @return 对象池最大总数
	 */
	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * 设置对象池最大总数
	 * @param maxTotal 对象池最大总数
	 */
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	/**
	 * 获得对象池最小空闲数
	 * @return 对象池最小空闲数
	 */
	public int getMinIdle() {
		return minIdle;
	}

	/**
	 * 设置对象池最小空闲数
	 * @param minIdle 对象池最小空闲数
	 */
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	/**
	 * 获得对象池初始化连接数
	 * @return 对象池初始化连接数
	 */
	public int getInitialSize() {
		return initialSize;
	}

	/**
	 * 设置对象池初始化连接数
	 * @param initialSize 对象池初始化连接数
	 */
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * 获得hdfs defaultFs配置
	 * @return hdfs defaultFs配置
	 */
	public String getDefaultFs() {
		return defaultFs;
	}

	/**
	 * 设置hdfs defaultFs配置
	 * @param defaultFs hdfs defaultFs配置
	 */
	public void setDefaultFs(String defaultFs) {
		this.defaultFs = defaultFs;
	}

	/**
	 * 获得hdfs HA的nameservices设置，非HA可不写
	 * @return hdfs HA的nameservices设置，非HA可不写
	 */
	public String getNameservices() {
		return nameservices;
	}

	/**
	 * 设置hdfs HA的nameservices设置，非HA可不写
	 * @param nameservices hdfs HA的nameservices设置，非HA可不写
	 */
	public void setNameservices(String nameservices) {
		this.nameservices = nameservices;
	}

	/**
	 * 获得hdfs HA设置，非HA可不写
	 * @return hdfs HA设置，非HA可不写
	 */
	public String getHa() {
		return ha;
	}

	/**
	 * 设置hdfs HA设置，非HA可不写
	 * @param ha hdfs HA设置，非HA可不写
	 */
	public void setHa(String ha) {
		this.ha = ha;
	}

	/**
	 * 获得hdfs HA的rpcAddress设置，非HA可不写
	 * @return hdfs HA的rpcAddress设置，非HA可不写
	 */
	public String getRpcAddress() {
		return rpcAddress;
	}

	/**
	 * 设置hdfs HA的rpcAddress设置，非HA可不写
	 * @param rpcAddress hdfs HA的rpcAddress设置，非HA可不写
	 */
	public void setRpcAddress(String rpcAddress) {
		this.rpcAddress = rpcAddress;
	}
}
