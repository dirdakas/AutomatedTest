package WebExplorer;

import org.apache.commons.lang3.StringUtils;

public class Application {
	private String server = null;
	private String name = null;
	private String context = null;
	private Presentation presentation = null;
	private boolean serverLogAvailable;

	
	public boolean isServerLogAvailable() {
		return serverLogAvailable;
	}

	public void setServerLogAvailable(boolean serverLogAvailable) {
		this.serverLogAvailable = serverLogAvailable;
	}

	public Application(Presentation presentation, String server, String name, String context, boolean serverLogAvailable) {
		this.server = server;
		this.name = name;
		this.presentation = presentation;
		this.context = context;
		this.serverLogAvailable = serverLogAvailable;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public String getFullPath() {
		return server + "/" + name + context;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Application) {
			Application that = (Application)obj;
			return StringUtils.equals(server, that.getServer()) && StringUtils.equals(name, that.getName()) && StringUtils.equals(context, that.getContext());	
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return server.hashCode() ^ name.hashCode() ^ context.hashCode();
	}
}