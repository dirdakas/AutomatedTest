package Webdriver;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;


public class TestRunner {

	public static void main(String[] args) throws Exception {
		runTests(args[0]);
	}

	public static void runTests(String testLine) throws Exception {
		List<TestItem> testItems = new ArrayList<TestItem>();
		if (!"".equals(testLine)) {
			String[] webdriverTests = testLine.split(",");
			for (String wt : webdriverTests) {
				String[] pair = wt.split(":");
				testItems.add(new TestItem(Class.forName(pair[0]), StringUtils.substringBefore(pair[1], " (")));
			}
			TestItem[] testItemsArray = testItems.toArray(new TestItem[testItems.size()]);
			runBeforeClass(getTestClasses(testItemsArray));
			runTests(testItemsArray);
		}
	}

	private static void runTests(TestItem... testItems) throws Exception {
		ReportBuilder.prepareFolder();
		int maxThreadCount = Integer.parseInt(System.getProperty("threadCount", "10"));
		int threadCount = maxThreadCount < testItems.length ? maxThreadCount : testItems.length;
		AtomicInteger index = new AtomicInteger(-1);
		long start = System.currentTimeMillis();
		ExecutorService executorService = Executors.newCachedThreadPool();
//		ConcurrentHashMap<String, AtomicBoolean> testClasses = prepareTestClassMap(testItems);
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(new TestCallable(index, testItems));
			Thread.sleep(10000);
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(3, TimeUnit.HOURS);
		} finally {
			ReportBuilder.prepareReport(threadCount, System.currentTimeMillis() - start, testItems);
		}
	}
	
	private static void runBeforeClass(String... testClasses) throws Exception {
		int maxThreadCount = Integer.parseInt(System.getProperty("threadCount", "10"));
		int threadCount = maxThreadCount < testClasses.length ? maxThreadCount : testClasses.length;
		AtomicInteger index = new AtomicInteger(-1);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(new BeforeClassCallable(index, testClasses));
			Thread.sleep(2000);
		}
		executorService.shutdown();
		executorService.awaitTermination(3, TimeUnit.HOURS);
	}
	
	private static String[] getTestClasses(TestItem... testItems){
		Set<String> testClasses = new HashSet<String>();
		for (TestItem testItem : testItems) {
			testClasses.add(testItem.getTestClazz().getCanonicalName());
		}
		return testClasses.toArray(new String[testClasses.size()]);
	}
			
}