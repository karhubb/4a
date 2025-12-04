/**
 * Asignación de Programa: PSP 3A
 * Nombre: [Karla Sofía Castro Pérez]
 * Fecha: [2025-12-03]
 * Descripción: Clase para escribir resultados en archivos de salida
 */

/**
 * Clase para manejar y procesar datos
 */
public class Data {

    /**
     * Convierte un String en un array de líneas separadas
     * @param data String con contenido a dividir
     * @return array de Strings con cada línea del contenido
     */
    public String[] saveData(String data) {
        // Dividir el string usando el salto de línea como separador
        return data.split("\n");
    }
}