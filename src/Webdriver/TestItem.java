package Webdriver;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

public class TestItem {
	private Class<?> testClazz = null;
	private Method testMethod;
	
	private long duration = 0;
	private boolean success = true;
	private Throwable testExteption = null;
	
	private byte[] screenshot = null;
	private List<String> testLog = Collections.emptyList();
	private List<String> serverTailLog = Collections.emptyList();
	private List<byte[]> serverFullLog = Collections.emptyList();
	
	public TestItem() {
	}
	
	public Class<?> getTestClazz() {
		return testClazz;
	}

	public TestItem(Class<?> testClazz, String testMethodName) {
		this.testMethod = MethodUtils.getAccessibleMethod(testClazz, testMethodName, new Class[]{});
		this.testClazz = testClazz;
	}
	
	public String getTitle() {
		return testClazz.getSimpleName() + "." + testMethod.getName();
	}	
	
	public Method getTestMethod() {
		return testMethod;
	}
	
	public void setTestMethod(Method testMethod) {
		this.testMethod = testMethod;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Throwable getTestExteption() {
		return testExteption;
	}
	
	public void setTestExteption(Throwable e) {
		this.testExteption = e;
	}
	
	public List<String> getTestLog() {
		return testLog;
	}
	
	public void setTestLog(List<String> testLog) {
		this.testLog = testLog;
	}
	
	public List<String> getServerTailLog() {
		return serverTailLog;
	}
	
	public void setServerTailLog(List<String> serverTailLog) {
		this.serverTailLog = serverTailLog;
	}
	
	public byte[] getScreenshot() {
		return screenshot;
	}
	
	public void setScreenshot(byte[] screenshot) {
		this.screenshot = screenshot;
	}

	public void setServerFullLog(List<byte[]> serverFullLog) {
		this.serverFullLog = serverFullLog;
		
	}	
	
	public List<byte[]> getServerFullLog() {
		return serverFullLog;
	}
}