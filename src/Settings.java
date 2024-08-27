import java.io.Serializable;

public class Settings implements Serializable {
    private String downloadDirectory;
    private String proxyHost;
    private int proxyPort;

    public Settings() {
        this.downloadDirectory = System.getProperty("user.home") + "/Downloads";
        this.proxyHost = "";
        this.proxyPort = 0;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
