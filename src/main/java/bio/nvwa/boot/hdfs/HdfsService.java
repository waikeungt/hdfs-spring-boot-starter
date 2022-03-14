package bio.nvwa.boot.hdfs;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URI;
import java.security.MessageDigest;

/**
 * 描述：一般使用HDFS的类，只需注入使用<br/>
 * 实现了HDFS的常用方法<br/>
 * 不够的话{@link #getHdfsClient()}拿HdfsClient，记得{@link #returnHdfsClient(HdfsClient)}
 *
 * @author waikeungt
 * @version 1.0
 * @date 2020/3/7 9:35
 */
@Service
public class HdfsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsService.class);

	private final ThreadLocal<HdfsClient> threadLocal = new ThreadLocal<>();

	private final HdfsPool hdfsPool;

	public HdfsService(HdfsPool hdfsPool) {
		this.hdfsPool = hdfsPool;
	}

	protected void setHdfsClient(HdfsClient hdfsClient) {
		this.threadLocal.set(hdfsClient);
	}

	protected void removeThreadLocal() {
		threadLocal.remove();
	}

	public HdfsClient getHdfsClient() throws Exception {
		return this.hdfsPool.borrowObject();
	}

	public void returnHdfsClient(HdfsClient hdfsClient) {
		this.hdfsPool.returnObject(hdfsClient);
	}

	/**
	 * 获得URI，详见{@link HdfsClient#getUri FileSystem.getUri()}
	 *
	 * @return {@link URI URI}
	 */
	@HdfsAnnotation
	public URI getUri() {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.getUri();
	}

	/**
	 * 获得输出输入流，详见{@link HdfsClient#open FileSystem.getUri(Path f, int bufferSize)}
	 *
	 * @param f          地址
	 * @param bufferSize buff大小
	 * @return {@link FSDataInputStream}输入流
	 * @throws IOException 异常
	 */
	@HdfsAnnotation
	public FSDataInputStream open(Path f, int bufferSize) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.open(f, bufferSize);
	}

	@HdfsAnnotation
	public FSDataOutputStream create(Path f, FsPermission permission, boolean overwrite, int bufferSize, short replication, long blockSize, Progressable progress) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.create(f, permission, overwrite, bufferSize, replication, blockSize, progress);
	}

	/**
	 * 在指定的路径上创建FSDataOutputStream，一般用这个比较多<br/>
	 * 测试过，应该线程安全
	 *
	 * @param f          要创建的文件名
	 * @param overwrite  覆盖，如果文件已经存在，那么如果是“true”，文件将被重写，如果“false”会引发错误
	 * @param bufferSize 缓冲
	 * @see HdfsClient#create(Path, boolean, int)
	 */
	@HdfsAnnotation
	public FSDataOutputStream create(Path f, boolean overwrite, int bufferSize) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.create(f, overwrite, bufferSize);
	}

	@HdfsAnnotation
	public FSDataOutputStream append(Path f, int bufferSize, Progressable progress) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.append(f, bufferSize, progress);
	}

	/**
	 * 重命名
	 *
	 * @param src 要重命名的路径
	 * @param dst 重命名后的新路径
	 * @return 如果重命名成功，则为true
	 * @throws IOException IO异常
	 * @see HdfsClient#rename(Path, Path)
	 */
	@HdfsAnnotation
	public boolean rename(Path src, Path dst) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.rename(src, dst);
	}

	@HdfsAnnotation
	public boolean delete(Path f, boolean recursive) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.delete(f, recursive);
	}

	@HdfsAnnotation
	public FileStatus[] listStatus(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.listStatus(f);
	}

	@HdfsAnnotation
	public void setWorkingDirectory(Path newDir) {
		HdfsClient hdfsClient = threadLocal.get();
		hdfsClient.setWorkingDirectory(newDir);
	}

	@HdfsAnnotation
	public Path getWorkingDirectory() {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.getWorkingDirectory();
	}

	@HdfsAnnotation
	public boolean mkdirs(Path f, FsPermission permission) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.mkdirs(f, permission);
	}

	@HdfsAnnotation
	public boolean mkdirs(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.mkdirs(f);
	}

	@HdfsAnnotation
	public FileStatus getFileStatus(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.getFileStatus(f);
	}

	/**
	 * 文件/目录是否存在
	 *
	 * @param f 文件/目录的Path对象
	 * @return true 存在 false 不存在
	 * @throws IOException IO异常
	 */
	@HdfsAnnotation
	public boolean exists(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.exists(f);
	}

	/**
	 * 获取文件的长度（字节）
	 *
	 * @param f 地址
	 * @return 文件的长度（字节）
	 * @throws Exception 异常
	 * @see HdfsClient#getFileStatus(Path)
	 * @see FileStatus#getLen()
	 */
	@HdfsAnnotation
	public long getLen(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		return hdfsClient.getFileStatus(f).getLen();
	}

	@HdfsAnnotation
	public String getMd5(Path f) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		FSDataInputStream os = hdfsClient.open(f);
		byte[] buffer = new byte[5242880];
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try {
			while (true) {
				int bytesRead = os.read(buffer);
				if (bytesRead <= -1) {
					break;
				} else if (bytesRead > 0) {
					md5.update(buffer, 0, bytesRead);
				}
			}
		} finally {
			os.close();
		}
		byte[] result = md5.digest();
		return DatatypeConverter.printHexBinary(result);
	}

	/**
	 * 下载
	 *
	 * @param src        要下载的地址
	 * @param dst        目标地址
	 * @param delSrc     删除HDFS文件
	 * @param overwrite  覆盖
	 * @param bufferSize bufferSize
	 * @return 下载情况
	 * @throws Exception 异常
	 */
	@HdfsAnnotation
	public boolean downFile(String src, String dst, final boolean delSrc, final boolean overwrite, final int bufferSize) throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		Path srcPath = new Path(src);
		if (!hdfsClient.exists(srcPath)) {
			LOGGER.warn("下载{}到{}时，文件不存在，下载失败", src, dst);
			return false;
		}
		if (hdfsClient.getFileStatus(srcPath).isDirectory()) {
			LOGGER.warn("下载{}到{}时，该地址是目录，下载失败", src, dst);
			return false;
		}
		File srcFile = new File(src);
		if (srcFile.exists()) {
			File dstFile = new File(dst);
			if (!dstFile.canWrite()) {
				LOGGER.warn("下载{}到{}时，本地文件没有写权限，下载失败", src, dst);
				return false;
			}
			if (overwrite) {
				if (FileUtils.deleteQuietly(dstFile)) {
					LOGGER.warn("下载{}到{}时，删除本地文件失败", src, dst);
					return false;
				}
			} else {
				LOGGER.warn("下载{}到{}时，本地有该地址，且不许覆盖，下载失败", src, dst);
				return false;
			}
		}

		try (
				FSDataInputStream input = hdfsClient.open(srcPath, bufferSize);
				OutputStream output = new FileOutputStream(dst)
		) {
			byte[] buffer = new byte[bufferSize];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
		} catch (Exception e) {
			LOGGER.error("下载{}到{}时出错", src, dst, e);
			return false;
		}

		if (delSrc) {
			if (hdfsClient.delete(srcPath, false)) {
				LOGGER.warn("下载{}到{}时，下载完成，但删除HDFS文件是失败", src, dst);
				return false;
			}
		}
		return true;
	}

	/**
	 * 读文本文件，只适用小文件，大文件请买好内存条
	 *
	 * @param src        文件地址
	 * @param bufferSize 缓冲大小
	 * @param charsets   编码
	 * @return 文本内容
	 * @throws Exception 文件不存在、内存等异常
	 */
	@HdfsAnnotation
	public String readTextFile(String src, final int bufferSize, String charsets) throws Exception {
		if (bufferSize <= 0) {
			LOGGER.warn("bufferSize <= 0？信不信我System.exit(0)？");
			throw new Exception("bufferSize <= 0");
		}
		HdfsClient hdfsClient = threadLocal.get();
		Path srcPath = new Path(src);
		if (!hdfsClient.exists(srcPath)) {
			LOGGER.warn("文件{}不存在", src);
			throw new FileNotFoundException("文件不存在");
		}
		if (hdfsClient.getFileStatus(srcPath).isDirectory()) {
			LOGGER.warn("地址{}不是文件", src);
			throw new Exception("地址{}不是文件");
		}
		StringBuilder builder = new StringBuilder();
		try (
				FSDataInputStream dis = hdfsClient.open(srcPath, bufferSize);
				InputStreamReader inputStreamReader = new InputStreamReader(dis, charsets);
				BufferedReader bf = new BufferedReader(inputStreamReader, bufferSize)
		) {
			String line;
			while ((line = bf.readLine()) != null) {
				builder.append(line).append("\n");
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 1, builder.length());
			}
		} catch (Exception e) {
			LOGGER.error("读{}时出错", src, e);
			throw e;
		}
		return builder.toString();
	}

	@HdfsAnnotation
	public void test() throws Exception {
		HdfsClient hdfsClient = threadLocal.get();
		boolean exists = hdfsClient.exists(new Path("/test/"));
		LOGGER.info("{}", exists);
	}
}
