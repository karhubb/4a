/***************************************************************************
 * Programa:  Regla de Simpson para Distribución t-Student  
 * Clase:     Logic4a
 * Autor:     Karla Sofía Castro Pérez
 * Fecha:     4-Diciembre-2024
 *
 * Descripción:
 *   Clase controladora principal que coordina todo el flujo del programa:
 *     - Lee los cuatro parámetros de entrada desde la consola
 *     - Orquesta el proceso iterativo de integración numérica
 *     - Controla la convergencia del cálculo
 *     - Muestra resultados intermedios en consola
 *     - Invoca la escritura del resultado final
 *
 * Variables miembro:
 *   dblXInicial   : límite inferior de integración
 *   dblXFinal     : límite superior de integración  
 *   intDof        : grados de libertad de la distribución t
 *   intNumSeg     : número inicial de segmentos (debe ser par)
 *   dblPFinal     : valor final calculado de la integral P
 *   lstResultados : lista que almacena resultados de cada iteración
 *
 * Dependencias:
 *   - Utiliza SimpsonIntegration para el cálculo numérico
 *   - Utiliza Output para escritura de archivos
 *   - Utiliza java.util.Scanner para entrada por consola
 *   - Utiliza java.util.ArrayList para almacenar iteraciones
 ***************************************************************************/

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Logic4a.
 * <p>
 * Responsable de la coordinación general del programa. Implementa el
 * ciclo iterativo que duplica segmentos hasta alcanzar la precisión
 * requerida de ±0.00001.
 */
public class Logic4a
{
    /*------------------------------------------------------------------*/
    /*  Attributes                                                      */
    /*------------------------------------------------------------------*/
    
    /** Límite inferior de integración (valor de x inicial) */
    private double dblXInicial;
    
    /** Límite superior de integración (valor de x final) */
    private double dblXFinal;
    
    /** Grados de libertad de la distribución t-Student */
    private int intDof;
    
    /** Número inicial de segmentos para Simpson (debe ser par) */
    private int intNumSeg;
    
    /** Valor final calculado de la integral P(x) */
    private double dblPFinal;
    
    /** Lista para almacenar resultados de cada iteración */
    private List<Output.ResultadoIteracion> lstResultados;
    
    /*------------------------------------------------------------------*/
    /*  Public Methods                                                  */
    /*------------------------------------------------------------------*/
    
    /**
     * Método principal que ejecuta el flujo completo del programa.
     * <p>
     * Secuencia de operaciones:
     *   1. Obtener parámetros de entrada desde la consola
     *   2. Inicializar variables para el ciclo iterativo
     *   3. Ejecutar ciclo do-while que duplica segmentos hasta convergencia
     *   4. Mostrar resultados de cada iteración en consola
     *   5. Guardar reporte completo en archivo mediante clase Output
     *
     * Condición de convergencia:
     *   |P_actual - P_anterior| < 0.00001
     *
     * @throws IOException si ocurre error al escribir el archivo de salida
     */
    public void execute() throws IOException
    {
        // Inicializar lista de resultados
        lstResultados = new ArrayList<>();
        
        // Obtener los cuatro parámetros requeridos del usuario
        getInputs();
        
        /*--------------------------------------------------------------*/
        /*  Inicialización de variables para el ciclo iterativo         */
        /*--------------------------------------------------------------*/
        
        /** Valor de P calculado en la iteración actual */
        double dblPActual = 0.0;
        
        /** Valor de P calculado en la iteración anterior */
        double dblPAnterior = 0.0;
        
        /** Diferencia absoluta entre iteraciones sucesivas */
        double dblDiferencia = 1.0;  // Inicializar con valor mayor al umbral
        
        /** Contador de iteraciones realizadas */
        int intIteracion = 1;
        
        /** Indica si se alcanzó la precisión requerida */
        boolean blnPrecisionAlcanzada = false;
        
        /** Número de segmentos actual (se modifica durante las iteraciones) */
        int intSegmentosActuales = intNumSeg;
        
        System.out.println("\n=== INICIANDO CÁLCULO ITERATIVO ===");
        System.out.println("Precisión requerida: ±0.00001");
        System.out.println("Segmentos iniciales: " + intNumSeg);
        
        /*--------------------------------------------------------------*/
        /*  Ciclo iterativo principal                                   */
        /*--------------------------------------------------------------*/
        
        do 
        {
            // Guardar valor anterior si no es la primera iteración
            if (intIteracion > 1) 
            {
                dblPAnterior = dblPActual;
            }
            
            // Calcular P actual usando Regla de Simpson
            dblPActual = SimpsonIntegration.calculateP(dblXInicial, dblXFinal, 
                                                       intSegmentosActuales, intDof);
            
            // Calcular diferencia absoluta a partir de la segunda iteración
            if (intIteracion > 1) 
            {
                dblDiferencia = Math.abs(dblPActual - dblPAnterior);
                blnPrecisionAlcanzada = (dblDiferencia < 0.00001);
            }
            
            // Almacenar resultado de esta iteración
            Output.ResultadoIteracion objResultado = 
                new Output.ResultadoIteracion(intIteracion, intSegmentosActuales,
                                             dblPActual, dblDiferencia,
                                             blnPrecisionAlcanzada);
            lstResultados.add(objResultado);
            
            // Mostrar resultados de la iteración actual en consola
            printIteration(objResultado);
            
            // Duplicar número de segmentos para siguiente iteración
            intSegmentosActuales = intSegmentosActuales * 2;
            intIteracion++;
            
        } while (intIteracion <= 2 || dblDiferencia >= 0.00001);
        // Continuar hasta alcanzar precisión, con mínimo 2 iteraciones
        
        /*--------------------------------------------------------------*/
        /*  Finalización y escritura de resultados                      */
        /*--------------------------------------------------------------*/
        
    // Guardar valor final de P
    dblPFinal = dblPActual;
    
    // Escribir reporte completo en archivo mediante clase Output
    Output.writeFullReport(dblXInicial, dblXFinal, intDof, intNumSeg,
                          lstResultados, dblPFinal, dblDiferencia);
    
    // MODIFICACIÓN AQUÍ: Imprimir solo X, dof y p en formato tabla
    System.out.println("\n=== CÁLCULO COMPLETADO ===");
    System.out.printf("X: %.4f    dof: %d    p: %.6f%n", 
                     dblXFinal, intDof, dblPFinal);
    }
    
    /*------------------------------------------------------------------*/
    /*  Private Methods                                                 */
    /*------------------------------------------------------------------*/
    
    /**
     * Lee los cuatro parámetros de entrada desde la consola.
     * <p>
     * Solicita y valida:
     *   1. x inicial (double)
     *   2. x final (double)  
     *   3. Grados de libertad (int, debe ser > 0)
     *   4. Número inicial de segmentos (int, debe ser par)
     *
     * Advertencias:
     *   - Si el número de segmentos es impar, se incrementa en 1 para hacerlo par
     *   - Si dof es negativo, se convierte a positivo
     */
    private void getInputs()
    {
        /** Scanner para leer entrada desde la consola */
        Scanner objScanner = new Scanner(System.in);
        
        System.out.println("\n=== CÁLCULO DE INTEGRAL t-STUDENT CON REGLA DE SIMPSON ===");
        System.out.println("Por favor ingrese los siguientes valores:");
        System.out.println();
        
        // Solicitar límite inferior de integración
        System.out.print("Ingrese x inicial (límite inferior): ");
        dblXInicial = objScanner.nextDouble();
        
        // Solicitar límite superior de integración
        System.out.print("Ingrese x final (límite superior): ");
        dblXFinal = objScanner.nextDouble();
        
        /*--------------------------------------------------------------*/
        /*  Validación básica de límites de integración                 */
        /*--------------------------------------------------------------*/
        
        if (dblXFinal < dblXInicial) 
        {
            System.out.println("Nota: x final es menor que x inicial.");
            System.out.println("      Se calculará la integral en sentido inverso.");
        }
        
        // Solicitar grados de libertad
        System.out.print("Ingrese grados de libertad (dof, entero > 0): ");
        intDof = objScanner.nextInt();
        
        // Validar que dof sea positivo
        if (intDof <= 0) 
        {
            System.out.println("Advertencia: dof debe ser positivo.");
            System.out.println("            Usando valor absoluto.");
            intDof = Math.abs(intDof);
        }
        
        // Solicitar número inicial de segmentos
        System.out.print("Ingrese número inicial de segmentos (par): ");
        intNumSeg = objScanner.nextInt();
        
        // Asegurar que el número de segmentos sea par (requisito de Simpson)
        if (intNumSeg % 2 != 0) 
        {
            System.out.println("Advertencia: Simpson requiere segmentos pares.");
            System.out.println("            Ajustando " + intNumSeg + " a " + (intNumSeg + 1));
            intNumSeg++;  // Incrementar para hacerlo par
        }
        
        // Validar que el número de segmentos sea positivo
        if (intNumSeg <= 0) 
        {
            System.out.println("Advertencia: número de segmentos debe ser positivo.");
            System.out.println("            Usando valor mínimo de 2.");
            intNumSeg = 2;
        }
        
        // Cerrar el scanner para liberar recursos
        objScanner.close();
    }
    
    /**
     * Imprime los resultados de una iteración en formato legible.
     * <p>
     * Muestra:
     *   - Número de iteración
     *   - Número de segmentos utilizados
     *   - Valor calculado de P
     *   - Diferencia con iteración anterior (a partir de la iteración 2)
     *
     * @param objResultado objeto con los datos de la iteración actual
     */
    private void printIteration(Output.ResultadoIteracion objResultado)
    {
        System.out.printf("%nIteración %d:%n", objResultado.intIteracion);
        System.out.printf("  Segmentos: %d%n", objResultado.intSegmentos);
        System.out.printf("  P = %.10f%n", objResultado.dblPActual);
        
        // Mostrar diferencia solo a partir de la segunda iteración
        if (objResultado.intIteracion > 1) 
        {
            System.out.printf("  Diferencia = %.10f%n", objResultado.dblDiferencia);
            
            // Indicar si se alcanzó la precisión requerida
            if (objResultado.blnPrecisionAlcanzada) 
            {
                System.out.println("  ✓ Precisión alcanzada");
            }
        }
    }
    
    /**
     * Retorna el valor final calculado de P.
     *
     * @return valor final de la integral P
     */
    public double getPFinal()
    {
        return dblPFinal;
    }
}