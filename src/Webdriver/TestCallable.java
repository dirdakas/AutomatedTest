package Webdriver;


import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestCallable implements Callable<Void> {

	private AtomicInteger index;
	private TestItem[] testItems;
	private static ConcurrentHashMap<String, AtomicBoolean> testClasses = new ConcurrentHashMap<String, AtomicBoolean>();

	public TestCallable(AtomicInteger index, TestItem... testItems) {
		this.index = index;
		this.testItems = testItems;
//		this.testClasses = testClasses; 
	}

	public Void call() throws Exception {
		try {
			int i = index.incrementAndGet();
			while (i < testItems.length) {
				TestItem testItem = testItems[i];
				testItem.setSuccess(true);
				long start = 0;
				WebdriverTestCase testCase = null;
				try {
					testCase = (WebdriverTestCase) testItem.getTestClazz().newInstance();
					testCase.before();
					start = System.currentTimeMillis();
					testItem.getTestMethod().invoke(testCase, new Object[] {});
					testCase.after();
				} catch (Exception e) {
					Thread.sleep(2000);
					try {
						testItem.setScreenshot(testCase.getTester().screenshot());
					} catch (Exception se) {
						testItem.setScreenshot(new byte[] {});
						se.printStackTrace();
					}
					testItem.setSuccess(false);
					testItem.setTestExteption(e.getCause() != null ? e.getCause() : e);
				}
				testCase.destroy();
				//testItem.setServerTailLog(Arrays.asList("tail"));
				testItem.setServerFullLog(testCase.getTester().getServerLogs());
				testItem.setServerTailLog(testCase.getTester().getServerTailLog());
				testItem.setTestLog(testCase.getTester().getTestLog());
				testItem.setDuration(System.currentTimeMillis() - start);
				i = index.incrementAndGet();
				System.out.println("Test " + testItem.getTitle() + " " + (testItem.isSuccess() ? "passed" : "failed " + testItem.getTestExteption()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}