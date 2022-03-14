package bio.nvwa.boot.hdfs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 对象池配置<br/>
 * ConfigurationProperties是为了避免有些没写
 * @author waikeungt
 * @version 1.0
 * @date 2020/3/7 9:35
 */
@Component
@ConfigurationProperties(prefix = "hadoop.hdfs")
public class HdfsProperties {

	/**
	 * 对象池最大空闲
	 */
	private int maxIdle = 5;
	/**
	 * 对象池最大总数
	 */
	private int maxTotal = 20;
	/**
	 * 对象池最小空闲
	 */
	private int minIdle = 2;
	/**
	 * 对象池初始化连接数
	 */
	private int initialSize = 3;
	/**
	 * hdfs defaultFs
	 */
	private String defaultFs;
	/**
	 * hdfs HA设置，非HA可不写
	 */
	private String nameservices;
	/**
	 * hdfs HA设置，非HA可不写
	 */
	private String ha;
	/**
	 * hdfs HA设置，非HA可不写
	 */
	private String rpcAddress;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public String getDefaultFs() {
		return defaultFs;
	}

	public void setDefaultFs(String defaultFs) {
		this.defaultFs = defaultFs;
	}

	public String getNameservices() {
		return nameservices;
	}

	public void setNameservices(String nameservices) {
		this.nameservices = nameservices;
	}

	public String getHa() {
		return ha;
	}

	public void setHa(String ha) {
		this.ha = ha;
	}

	public String getRpcAddress() {
		return rpcAddress;
	}

	public void setRpcAddress(String rpcAddress) {
		this.rpcAddress = rpcAddress;
	}
}
