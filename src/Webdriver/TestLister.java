package Webdriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class TestLister {
	public static Set<Method> getMethods(String packageName) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packageName)).setScanners(new MethodAnnotationsScanner()));
		return reflections.getMethodsAnnotatedWith(Test.class);
	}
	
	public static Set<Method> getBeforeClassMethod(String className) throws ClassNotFoundException {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(Class.forName(className))).setScanners(new MethodAnnotationsScanner()));
		return reflections.getMethodsAnnotatedWith(BeforeClass.class);
	}
	
	public static String buildString(String packageName) {
		List<String> methods = sortMethods(getMethods(packageName));
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String m : methods) {
			sb.append(m);
			if (i++ < methods.size()-1) {
				sb.append(',');
			}
		}
		return sb.toString();
	}
	
	public static List<String> sortMethods(Collection<Method> methods) {
		List<String> result = new ArrayList<String>();
		for (Method method : methods) {
			Comment c = method.getAnnotation(Comment.class);
			result.add(method.getDeclaringClass().getName() + ":" + method.getName() + (c != null ? " (" + c.value().replaceAll(",", ";") + ")" : ""));
		}
		Collections.sort(result);
		return result;
	}

	public static void main(String[] args) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("test-list.properties"), "UTF-8");
		out.write("tests=");
		out.write(buildString((args.length > 0) ? args[0] : "myTesTs"));
		IOUtils.closeQuietly(out);
	}

}