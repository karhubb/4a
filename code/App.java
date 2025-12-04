/***************************************************************************
 * Programa:  Regla de Simpson para Distribución t-Student
 * Clase:     App
 * Autor:     Karla Sofía Castro Pérez
 * Fecha:     4-Diciembre-2024
 *
 * Descripción:
 *   Punto de entrada principal del programa. Crea una instancia de la
 *   clase Logic4a y ejecuta el procesamiento principal.
 *
 * Uso:
 *   Compilar: javac App.java Logic4a.java GammaFunction.java 
 *             SimpsonIntegration.java Output.java
 *   Ejecutar: java App
 *
 *   El programa solicitará interactivamente:
 *     1. x inicial (límite inferior de integración)
 *     2. x final (límite superior de integración)  
 *     3. Grados de libertad (dof)
 *     4. Número inicial de segmentos (debe ser par)
 *
 * Advertencias:
 *   - El número de segmentos debe ser par para la Regla de Simpson
 *   - Los grados de libertad deben ser enteros positivos
 *   - x final debe ser mayor que x inicial para integración positiva
 ***************************************************************************/

/**
 * Clase App.
 * <p>
 * Proporciona el método main como punto único de entrada al programa.
 * Su única responsabilidad es crear la instancia de Logic4a y ejecutar
 * el flujo principal.
 */
public class App
{
    /**
     * Método principal (main) del programa.
     * <p>
     * Declara e inicializa el objeto que contiene la lógica principal
     * del programa, luego ejecuta el proceso completo.
     *
     * @param args argumentos de línea de comandos (no utilizados en esta versión)
     * @throws java.io.IOException si ocurre error durante la escritura del archivo de salida
     */
    public static void main(String[] args) throws java.io.IOException
    {
        /** Objeto que controla toda la lógica del programa */
        Logic4a objLogic;
        
        // Crear instancia de la lógica principal
        objLogic = new Logic4a();
        
        // Ejecutar el flujo principal del programa
        objLogic.execute();
    }
}