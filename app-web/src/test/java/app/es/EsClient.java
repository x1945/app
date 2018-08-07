package app.es;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsClient {

	private static final Logger LOG = LoggerFactory.getLogger(EsClient.class);

	// 用於提供單例的TransportClient BulkProcessor
	private static TransportClient client = null;
	private static BulkProcessor bulkProcessor = null;
	private static int count = 0;
	private static long time = 0;
	// private static final String host = "localhost";
	private static final String host = "192.168.1.96";
	private static final int port = 9300;

	// 【獲取TransportClient 的方法】
	public static TransportClient getClient() {
		try {
			if (client == null) {
				LOG.debug("host[{}] port[{}]", host, port);
				count = 0;
				time = 0;
				client = new PreBuiltTransportClient(Settings.EMPTY)
						.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
			}// if
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	// 關閉
	public static void close() {
		try {
			if (bulkProcessor != null)
				bulkProcessor.awaitClose(3, TimeUnit.MINUTES);// 阻塞至所有的請求線程處理完畢後，斷開連接資源
			LOG.info("共提交{}個文檔，TookInMillis[{}]", count, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (client != null)
			client.close();
	}

	// 【設置自動提交文檔】
	public static BulkProcessor getBulkProcessor() {
		// 自動批量提交方式
		if (bulkProcessor == null) {
			try {
				bulkProcessor = BulkProcessor.builder(getClient(),
						new BulkProcessor.Listener() {

							@Override
							public void beforeBulk(long executionId, BulkRequest request) {
								// 提交前調用
							}

							@Override
							public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
								// 提交結束後調用（無論成功或失敗）
								count += response.getItems().length;
								time += response.getTook().millis();
								LOG.info("提交{}個文檔，TookInMillis[{}] hasFailures[{}]",
										String.format("%04d", response.getItems().length),
										String.format("%06d", response.getTook().millis()),
										response.hasFailures());
								if (response.hasFailures())
									LOG.error("{}", response.buildFailureMessage());
							}

							@Override
							public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
								// 提交結束且失敗時調用
								LOG.error("有文檔提交失敗！after failure: {}", failure.toString());
							}
						})
						.setBulkActions(1000)// 文檔數量達到1000時提交
						.setBulkSize(new ByteSizeValue(100, ByteSizeUnit.MB))// 總文檔體積達到100MB時提交 //
						.setFlushInterval(TimeValue.timeValueSeconds(30))// 每30S提交一次（無論文檔數量、體積是否達到閾值）
						.setConcurrentRequests(10)// 加1後為可並行的提交請求數，即設為0代表只可1個請求並行，設為1為2個並行
						.build();
				// staticBulkProcessor.awaitClose(10, TimeUnit.MINUTES);//關閉，如有未提交完成的文檔則等待完成，最多等待10分鐘
			} catch (Exception e) {// 關閉時拋出異常
				e.printStackTrace();
			}
		}// if
		return bulkProcessor;
	}

	/**
	 * 儲存文件
	 * 
	 * @param indexData
	 * @return
	 */
	public static boolean saveDoc(IndexData2 indexData) {
		return saveDoc("coa-index", "fulltext", indexData);
	}

	/**
	 * 儲存文件
	 * 
	 * @param indexName
	 * @param indexType
	 * @param indexData
	 * @return
	 */
	public static boolean saveDoc(String indexName, String indexType, IndexData2 indexData) {
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("pid", indexData.getPid());
		doc.put("cpid", indexData.getCpid());
		doc.put("cname", indexData.getCname());
		doc.put("yr", indexData.getYr());
		doc.put("category", indexData.getCategory());
		doc.put("type", indexData.getType());
		doc.put("director_name", indexData.getDirector_name());
		doc.put("director_dept", indexData.getDirector_dept());
		doc.put("divisioin_id", indexData.getDivisioin_id());
		doc.put("segid", indexData.getSegid());
		doc.put("eid", indexData.getEid());
		doc.put("real_domain_id", indexData.getReal_domain_id());
		doc.put("domain_id", indexData.getDomain_id());
		doc.put("promote_id", indexData.getPromote_id());
		doc.put("content1", indexData.getContent1());
		doc.put("content2", indexData.getContent2());

		boolean result = false;
		try {
			IndexRequest indexRequest = new IndexRequest(indexName, indexType, indexData.getPid())
					.source(doc);
			UpdateRequest updateRequest = new UpdateRequest(indexName, indexType, indexData.getPid())
					.doc(doc)
					.upsert(indexRequest);
			getClient().update(updateRequest).get();
			result = true;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
}
