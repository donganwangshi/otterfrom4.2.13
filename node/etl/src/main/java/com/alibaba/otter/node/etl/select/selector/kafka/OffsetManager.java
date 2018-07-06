package com.alibaba.otter.node.etl.select.selector.kafka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.otter.node.common.redis.RedisClient;

/**
 * The partition offset are stored in an external storage. In this case in a
 * file system.
 * 将分区的offset信息保存再外部存储中，当前offset管理器，存储的是文件系统。
 * <p/>
 */
public class OffsetManager {
	private String storagePrefix;
	
	private RedisClient redisClient;
	
	public OffsetManager(RedisClient redisClient, String storagePrefix) {
		this.storagePrefix = storagePrefix;
		this.redisClient = redisClient;
	}

	/**
	 * Overwrite the offset for the topic in an external storage.
	 * 保存offset，注意会重写offset
	 * @param topic Topic name.
	 * @param partition  Partition of the topic.
	 * @param offset offset to be stored.
	 */
	public void saveOffsetInExternalStore(String topic, int partition, long offset) {
		try {
			//todo: save to redis ,zk, kafka
//			FileWriter writer = new FileWriter(storageName(topic, partition), false);//append模式为false，重写文件内容
//			BufferedWriter bufferedWriter = new BufferedWriter(writer);
//			bufferedWriter.write(offset + "");
//			bufferedWriter.flush();
//			bufferedWriter.close();
			redisClient.set(storageName(topic, partition), String.valueOf(offset));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * 从offset存储文件中，读取topic对应分区的offset
	 * @param topic
	 * @param partition
	 * @return the last offset + 1 for the provided topic and partition.
	 */
	@SuppressWarnings({ "resource" })
	public long readOffsetFromExternalStore(String topic, int partition) {
		long ret = 0;
		try {
//			Stream<String> stream = Files.lines(Paths.get(storageName(topic, partition)));
//			return Long.parseLong(stream.collect(Collectors.toList()).get(0)) + 1;
			String offset = redisClient.get(storageName(topic, partition));
			ret = StringUtils.isEmpty(offset)? 0: Long.parseLong(offset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * topic分区offset信息存储位置
	 * @param topic
	 * @param partition
	 * @return
	 */
	private String storageName(String topic, int partition) {
		return "../position/"+storagePrefix + "_" + topic + "_" + partition;
	}

}
