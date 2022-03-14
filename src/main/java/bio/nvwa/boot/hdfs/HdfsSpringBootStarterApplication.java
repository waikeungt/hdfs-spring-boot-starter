package bio.nvwa.boot.hdfs;

import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HdfsSpringBootStarterApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(HdfsSpringBootStarterApplication.class);

	private static HdfsService hdfsService;

	@Autowired
	public void setHdfsService(HdfsService hdfsService) {
		HdfsSpringBootStarterApplication.hdfsService = hdfsService;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.hdfs.maxIdle", "10");
		System.setProperty("hadoop.hdfs.maxTotal", "20");
		System.setProperty("hadoop.hdfs.minIdle", "2");
		System.setProperty("hadoop.hdfs.initialSize", "2");
		System.setProperty("hadoop.hdfs.maxWaitMillis", "3000");
		System.setProperty("hadoop.hdfs.defaultFs", "hdfs://localhost:9820");
		LOGGER.info("准备启动spring boot");
		SpringApplication.run(HdfsSpringBootStarterApplication.class, args);
		LOGGER.info("准备spring boot完成");
		LOGGER.info("准备测试");
		String pathStr = "/tmp/";
		boolean test = hdfsService.exists(new Path(pathStr));
		LOGGER.info("测试目录{}：{}", pathStr, test);
		LOGGER.info("测试完成");
	}

}
