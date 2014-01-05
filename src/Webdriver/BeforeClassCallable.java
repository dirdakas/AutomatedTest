package Webdriver;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class BeforeClassCallable implements Callable<Void> {

	private AtomicInteger index;
	private String[] testClasses; 

	public BeforeClassCallable(AtomicInteger index, String... testClasses) {
		this.index = index;
		this.testClasses = testClasses; 
	}

	public Void call() throws Exception {
		try {
			int i = index.incrementAndGet();
			while (i < testClasses.length) {
				try {
					Set<Method> methods = TestLister.getBeforeClassMethod(testClasses[i]);
					for (Method method : methods) {
						if(method.getDeclaringClass().equals(Class.forName(testClasses[i]))){
							method.invoke(null, new Object[] {});
						}
					}
				} catch (Exception e) {
					Thread.sleep(2000);

				}
				i = index.incrementAndGet();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}