package com.filetransfer.common;

import java.io.File;
import java.net.URISyntaxException;

public final class Const {

    private Const() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_USER = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_CLOSE = 3;
    public static final int TYPE_COMMAND = 4;

    public static final int INT_LENGTH = 4;
    public static final int DOUBLE_LENGTH = 8;
    public static final int CHAR_LENGTH = 2;
    public static final int HEADER_LENGTH = INT_LENGTH*2;
    public static final int MAX_THREADS = 10;

    public static final String BASE_DIRECTORY = getBaseDirectory();
    public static final String FILE_SYSTEM_DIRECTORY = BASE_DIRECTORY + File.separator + "FileSystem";
    public static final String STORAGE_DIRECTORY = FILE_SYSTEM_DIRECTORY + File.separator + "storage";
    public static final String CONFIG_FILE_PATH = FILE_SYSTEM_DIRECTORY + File.separator + "config.yml";

    public static final int MAX_CONNECTIONS = 10;
    public static final String DEFAULT_FILE_EXTENSION = ".txt";


    public static final class Errors {
        public static final String FILE_NOT_FOUND = "File not found";
        public static final String ACCESS_DENIED = "Access denied";
    }

    private static String getBaseDirectory() {
        try {
            return new File(Const.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getParentFile()
                    .getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la ruta base.", e);
        }
    }
}
