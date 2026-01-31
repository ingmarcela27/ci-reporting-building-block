/**
 * Gestiona la configuración del entorno de ejecución.
 *
 * Esta clase permite ejecutar los mismos tests en distintos entornos
 * (dev, qa, etc.) sin modificar el código fuente, cargando dinámicamente
 * los archivos de propiedades correspondientes.
 */

package config;

import java.io.InputStream;
import java.util.Properties;

public class EnvironmentManager {

    private static final Properties properties = new Properties();

    static {
        try {
            String env = System.getProperty("env", "qa");
            String fileName = "env/" + env + ".properties";

            InputStream input =
                    EnvironmentManager.class
                            .getClassLoader()
                            .getResourceAsStream(fileName);

            if (input == null) {
                throw new RuntimeException(
                        "Archivo de entorno no encontrado: " + fileName
                );
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración de entorno", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
