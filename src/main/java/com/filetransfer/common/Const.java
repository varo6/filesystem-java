package common;

public final class Const {

    private Const() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_USER=1;
    public static final int TYPE_TEXT=2;
    public static final int TYPE_CLOSE=3;
    public static final int INT_LENGTH = 4;
    public static final int DOUBLE_LENGTH = 8;
    public static final int CHAR_LENGTH = 2;
    public static final int HEADER_LENGTH = INT_LENGTH*2;

    public static final String BASE_DIRECTORY = "/path/to/base";
    public static final int MAX_CONNECTIONS = 100;
    public static final String DEFAULT_FILE_EXTENSION = ".txt";


    public static final class Errors {
        public static final String FILE_NOT_FOUND = "File not found";
        public static final String ACCESS_DENIED = "Access denied";
    }
}
