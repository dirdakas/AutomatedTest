package Webdriver;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class StactTraceToHtml {
	private StringBuilder sb = null;
	private Throwable e;

	public StactTraceToHtml(Throwable e) {
		this.e = e;
	}

	private void println(String line, boolean full) {
		if (full) {
			sb.append(StringEscapeUtils.escapeHtml(line) + "&#13;");
			//sb.append(StringEscapeUtils.escapeHtml(line) + "&#10;&#13;");
		} else {
			sb.append("<div>" + StringUtils.replace(StringEscapeUtils.escapeHtml(line), " ", "&nbsp;") + "</div>\n");	
		}
	}
	
	private String shortenText(Throwable t) {
		String str = t.toString();
		int i = str.indexOf("Build info: version:");
		return (i >= 0) ? str.substring(0, i) : str;
	}

	public String printStackTrace(boolean full) {
		sb = new StringBuilder();
		println(shortenText(e), full);
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			if (full || trace[i].getClassName().startsWith("lt")) {
				println("\tat " + trace[i], full);	
			}
		}
		if (e.getCause() != null) {
			printStackTraceAsCause(e.getCause(), trace, full);
		}
		return sb.toString();
	}	

    /**
     * Print our stack trace as a cause for the specified stack trace.
     */
    private void printStackTraceAsCause(Throwable t, StackTraceElement[] causedTrace, boolean full)
    {
        // Compute number of frames in common between this and caused
        StackTraceElement[] trace = t.getStackTrace();
        int m = trace.length-1, n = causedTrace.length-1;
        while (m >= 0 && n >=0 && trace[m].equals(causedTrace[n])) {
            m--; n--;
        }
        int framesInCommon = trace.length - 1 - m;

        println("Caused by: " + shortenText(t), full);
        for (int i=0; i <= m; i++) {
        	if (full || trace[i].getClassName().startsWith("lt")) {
        		println("\tat " + trace[i], full);	
        	}
        }
        if (framesInCommon != 0)
            println("\t... " + framesInCommon + " more", full);

        // Recurse if we have a cause
        if (t.getCause() != null) {
            printStackTraceAsCause(t.getCause(), trace, full);
        }
    }	
}
