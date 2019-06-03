package ftp;

public class FTPFile {
    private final String name;
    private final String attrs;

    public FTPFile(String name, String attrs) {
        this.name = name;
        this.attrs = attrs;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return attrs.startsWith("d");
    }
}
