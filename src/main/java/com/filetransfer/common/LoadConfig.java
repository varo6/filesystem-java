package com.filetransfer.common;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class LoadConfig {
    public static Map<String, Object> loadConfig(String filePath) throws IOException {
        Yaml yaml = new Yaml();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return yaml.load(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new IOException("No se encontró el archivo de configuración: " + filePath, e);
        }
    }
}
