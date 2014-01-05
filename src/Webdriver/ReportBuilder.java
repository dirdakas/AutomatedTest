package Webdriver;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class ReportBuilder {

	public static final String REPORT_FOLDER = "report";

	public static void prepareReport(int threadCount, long duration, TestItem... testItems) throws Exception {
		int failCount = calculateFailCount(testItems);
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(REPORT_FOLDER + "/index.html"), "UTF-8");
		out.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"><style>body {font-family:Arial;} span{font-size:12px} table {border:1px solid #d8d8d8;border-collapse:collapse;font-size:12px;} td {border:1px solid #d8d8d8;border-collapse:collapse;padding:8px;text-align:left;} .tr-bg {background-color:#F7F7F7;} .center {text-align:center;} .bold {font-weight:bold;} .log div {font-size:12px; font-family:courier} a {text-decoration:none} </style></head><body>\r\n");
		out.write("<span style=color:green;font-weight:bold>passed: " + (testItems.length - failCount) + "</span>&nbsp;&nbsp;");
		out.write("<span style=color:red;font-weight:bold>failed: " + failCount + "</span>&nbsp;&nbsp;");
		out.write("<span style=color:black;font-weight:bold>total: " + testItems.length + "</span>&nbsp;&nbsp;");
		out.write("<span style=color:black;font-size:12px>actual duration: " + formatDuration(duration) + "</span>&nbsp;&nbsp;\n");
		out.write("<span style=font-size:12px>cumulative duration: " + formatDuration(calculateCumulative(testItems)) + "</span>&nbsp;&nbsp;\n");
		out.write("<span style=font-size:12px>threads: " + threadCount + "</span>\n");
		out.write("<span style=font-size:12px>cpu: " + Runtime.getRuntime().availableProcessors() + "</span>\n");

		out.write("<table cellpadding=0 cellspacing=0>\n");
		out.write("<tr class=tr-bg><td class=bold>test</td>");
		out.write("<td class=bold>screenshot</td>");
		out.write("<td class=bold width=300px>error</td>");
		out.write("<td class=bold>client log</td>");
		out.write("<td class=bold>server log</td></tr>");
		int i = 1;
		for (TestItem testItem : testItems) {
			out.write(String.format("<td><span>%d. </span><span style=color:%s;font-weight:bold>%s</span><span>&nbsp;(%s)</span></td>", i, testItem.isSuccess() ? "green" : "red", testItem.getTitle(),
					formatDuration(testItem.getDuration())));
			if (testItem.isSuccess()) {
				out.write("<td>-</td>\n");
			} else {
				String file = i + "_" + testItem.getTitle() + ".png";
				FileOutputStream fos = new FileOutputStream(REPORT_FOLDER + "/" + file);
				try {
					IOUtils.write(testItem.getScreenshot(), fos);
				} finally {
					IOUtils.closeQuietly(fos);
				}
				out.write("<td><a href=" + file + "><img src=" + file + " width=160 height=90></a></td>\n");
			}
			if (testItem.getTestExteption() != null) {
				StactTraceToHtml sth = (new StactTraceToHtml(testItem.getTestExteption()));
				out.write("<td class=log><span style=color:blue title=\"" + sth.printStackTrace(true) + "\">" + sth.printStackTrace(false) + "</span></td>\n");
			} else {
				out.write("<td>-</td>\n");
			}
			List<String> log = testItem.getTestLog();
			if (log != null && !log.isEmpty()) {
				out.write("<td class=log><span style=color:blue title=\"" + printLog(log, true) + "\">" + printLog(log, false) + "</span></td>\n");
			} else {
				out.write("<td>-</td>\n");
			}
			String file = i + "_" + testItem.getTitle() + ".log";
			FileOutputStream fos = new FileOutputStream(REPORT_FOLDER + "/" + file);
			try {
				for (byte[] arr : testItem.getServerFullLog()) {
					IOUtils.write(arr, fos);
				}
			} finally {
				IOUtils.closeQuietly(fos);
			}
			out.write("<td class=log><a href=" + file + ">" + printTail(testItem.getServerTailLog()) + "</a></td>");
			out.write("</tr>\n");
			i++;
		}
		out.write("</table>");
		out.write("</body></html>");
		IOUtils.closeQuietly(out);
		if (failCount > 0) {
			throw new RuntimeException("Failed test cases: " + failCount);
		}
	}

	public static void prepareFolder() {
		File folder = new File(REPORT_FOLDER);
		if (!folder.exists()) {
			folder.mkdir();
		} else {
			for (File file : folder.listFiles())
				FileUtils.deleteQuietly(file);
		}
	}

	private static long calculateCumulative(TestItem... testItems) {
		long result = 0;
		for (TestItem testItem : testItems) {
			result += testItem.getDuration();
		}
		return result;
	}

	private static int calculateFailCount(TestItem... testItems) {
		int failCount = 0;
		for (TestItem testItem : testItems) {
			failCount += testItem.isSuccess() ? 0 : 1;
		}
		return failCount;
	}

	private static String printTail(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String line : list) {
			sb.append("<div>" + StringUtils.replace(StringEscapeUtils.escapeHtml(line), " ", "&nbsp;") + "</div>\n");
		}
		return sb.toString();
	}

	private static String printLog(List<String> list, boolean full) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String line : list) {
			if (full) {
				sb.append(StringEscapeUtils.escapeHtml(line) + "&#13;");
			} else {
				if (i++ > list.size() - 18) {
					sb.append("<div>" + StringUtils.replace(StringEscapeUtils.escapeHtml(line), " ", "&nbsp;") + "</div>\n");
				}
			}
		}
		return sb.toString();
	}

	private static String formatDuration(long duration) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		return (minutes > 0 ? minutes + "min " : "") + (TimeUnit.MILLISECONDS.toSeconds(duration) - minutes * 60L) + "s";
	}
}