/***************************************************************************
 * Programa:  Regla de Simpson para Distribución t-STUDENT
 * Clase:     Output
 * Autor:     Karla Sofía Castro Pérez
 * Fecha:     4-Diciembre-2024
 *
 * Descripción:
 *   Encapsula la operación de escritura de resultados completos a un
 *   archivo de texto. Proporciona un método estático writeFullReport que
 *   recibe todos los datos del proceso iterativo y los escribe en formato
 *   idéntico al mostrado en consola.
 *
 * Archivo de salida:
 *   Nombre: Out4a.txt
 *   Formato: Texto plano con formato idéntico a la salida de consola
 *   Contenido:
 *     - Encabezado y parámetros de entrada
 *     - Resultados de cada iteración con valores de P y diferencias
 *     - Resumen final del cálculo
 *
 * Manejo de errores:
 *   - Captura IOException durante operaciones de archivo
 *   - Muestra mensaje de error en consola sin interrumpir ejecución
 *   - Cierra recursos adecuadamente en bloque finally
 *
 * Uso:
 *   Output.writeFullReport(xInicial, xFinal, dof, numSegInicial, 
 *                          resultadosIteraciones, pFinal, precisionAlcanzada);
 ***************************************************************************/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Clase Output.
 * <p>
 * Proporciona funcionalidad para escribir un reporte completo del cálculo
 * en un archivo de texto externo. El formato de salida es idéntico al
 * mostrado en la consola durante la ejecución del programa.
 */
public class Output
{
    /**
     * Clase interna para almacenar los resultados de una iteración.
     * <p>
     * Utilizada para pasar información estructurada sobre cada iteración
     * al método de escritura.
     */
    public static class ResultadoIteracion
    {
        /** Número de la iteración */
        public int intIteracion;
        
        /** Número de segmentos utilizados en esta iteración */
        public int intSegmentos;
        
        /** Valor de P calculado en esta iteración */
        public double dblPActual;
        
        /** Diferencia con la iteración anterior (0.0 para iteración 1) */
        public double dblDiferencia;
        
        /** Indica si se alcanzó la precisión en esta iteración */
        public boolean blnPrecisionAlcanzada;
        
        /**
         * Constructor para crear un resultado de iteración.
         *
         * @param intIteracionParam número de la iteración
         * @param intSegmentosParam número de segmentos utilizados
         * @param dblPActualParam   valor de P calculado
         * @param dblDiferenciaParam diferencia con iteración anterior
         * @param blnPrecisionAlcanzadaParam indica si se alcanzó precisión
         */
        public ResultadoIteracion(int intIteracionParam, int intSegmentosParam,
                                 double dblPActualParam, double dblDiferenciaParam,
                                 boolean blnPrecisionAlcanzadaParam)
        {
            this.intIteracion = intIteracionParam;
            this.intSegmentos = intSegmentosParam;
            this.dblPActual = dblPActualParam;
            this.dblDiferencia = dblDiferenciaParam;
            this.blnPrecisionAlcanzada = blnPrecisionAlcanzadaParam;
        }
    }
    
    /**
     * Escribe el reporte completo del cálculo en el archivo Out4a.txt.
     * <p>
     * Crea o sobrescribe el archivo Out4a.txt en el directorio actual
     * y escribe toda la información mostrada en consola durante la
     * ejecución del programa, incluyendo cada iteración.
     *
     * @param dblXInicial       límite inferior de integración
     * @param dblXFinal         límite superior de integración
     * @param intDof            grados de libertad
     * @param intNumSegInicial  número inicial de segmentos
     * @param lstResultados     lista con resultados de todas las iteraciones
     * @param dblPFinal         valor final calculado de la integral
     * @param dblPrecisionFinal precisión final alcanzada
     *
     * @throws IOException si ocurre error durante la escritura del archivo
     */
    public static void writeFullReport(double dblXInicial, double dblXFinal,
                                      int intDof, int intNumSegInicial,
                                      List<ResultadoIteracion> lstResultados,
                                      double dblPFinal, double dblPrecisionFinal) 
                                      throws IOException
    {
        /** Escritor de texto para el archivo de salida */
        PrintWriter objPrintWriter = null;
        
        /** Nombre del archivo de salida (constante) */
        final String STR_OUT_FILE = "Out4a.txt";
        
        try 
        {
            /*----------------------------------------------------------*/
            /*  Crear y abrir archivo para escritura                    */
            /*----------------------------------------------------------*/
            
            objPrintWriter = new PrintWriter(new FileWriter(STR_OUT_FILE));
            
            /*----------------------------------------------------------*/
            /*  Escribir encabezado idéntico a consola                  */
            /*----------------------------------------------------------*/
            
            objPrintWriter.println("\n=== CÁLCULO DE INTEGRAL t-STUDENT CON REGLA DE SIMPSON ===");
            objPrintWriter.println("Por favor ingrese los siguientes valores:");
            objPrintWriter.println();
            objPrintWriter.printf("Ingrese x inicial (límite inferior): %.3f%n", dblXInicial);
            objPrintWriter.printf("Ingrese x final (límite superior): %.3f%n", dblXFinal);
            objPrintWriter.printf("Ingrese grados de libertad (dof, entero > 0): %d%n", intDof);
            objPrintWriter.printf("Ingrese número inicial de segmentos (par): %d%n", intNumSegInicial);
            
            /*----------------------------------------------------------*/
            /*  Escribir sección de cálculo iterativo                   */
            /*----------------------------------------------------------*/
            
            objPrintWriter.println("\n=== INICIANDO CÁLCULO ITERATIVO ===");
            objPrintWriter.println("Precisión requerida: ±0.00001");
            objPrintWriter.println("Segmentos iniciales: " + intNumSegInicial);
            
            // Escribir resultados de cada iteración
            for (ResultadoIteracion objResultado : lstResultados)
            {
                objPrintWriter.printf("%nIteración %d:%n", objResultado.intIteracion);
                objPrintWriter.printf("  Segmentos: %d%n", objResultado.intSegmentos);
                objPrintWriter.printf("  P = %.10f%n", objResultado.dblPActual);
                
                // Mostrar diferencia solo a partir de la segunda iteración
                if (objResultado.intIteracion > 1)
                {
                    objPrintWriter.printf("  Diferencia = %.10f%n", objResultado.dblDiferencia);
                    
                    // Indicar si se alcanzó la precisión requerida
                    if (objResultado.blnPrecisionAlcanzada)
                    {
                        objPrintWriter.println("  ✓ Precisión alcanzada");
                    }
                }
            }
            
            /*----------------------------------------------------------*/
            /*  Escribir resumen final idéntico a consola               */
            /*----------------------------------------------------------*/
            
            objPrintWriter.println("\n=== CÁLCULO COMPLETADO ===");
            objPrintWriter.printf("Iteraciones totales: %d%n", lstResultados.size());
            
            // Obtener número de segmentos finales (última iteración)
            int intSegmentosFinales = lstResultados.get(lstResultados.size() - 1).intSegmentos;
            objPrintWriter.printf("Segmentos finales: %d%n", intSegmentosFinales);
            
            objPrintWriter.printf("Precisión alcanzada: %.10f%n", dblPrecisionFinal);
            objPrintWriter.println("Resultado final guardado en Out4a.txt");
            
        } 
        catch (IOException objEx) 
        {
            /*----------------------------------------------------------*/
            /*  Manejo de errores de escritura de archivo               */
            /*----------------------------------------------------------*/
            
            System.err.println("ERROR: No se pudo escribir el archivo de salida.");
            System.err.println("       Archivo: " + STR_OUT_FILE);
            System.err.println("       Mensaje: " + objEx.getMessage());
            
            // Relanzar la excepción para manejo en nivel superior
            throw objEx;
        } 
        finally 
        {
            /*----------------------------------------------------------*/
            /*  Cerrar recursos en bloque finally para garantizar       */
            /*  liberación incluso si ocurre excepción                  */
            /*----------------------------------------------------------*/
            
            if (objPrintWriter != null) 
            {
                objPrintWriter.close();
            }
        }
    }
}