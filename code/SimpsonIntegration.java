/***************************************************************************
 * Programa:  Regla de Simpson para Distribución t-Student
 * Clase:     SimpsonIntegration
 * Autor:     Karla Sofía Castro Pérez
 * Fecha:     4-Diciembre-2024
 *
 * Descripción:
 *   Contiene la implementación de dos funciones clave:
 *     1. Función de densidad de probabilidad t-Student (tDistribution)
 *     2. Integración numérica usando la Regla de Simpson (calculateP)
 *
 * Fórmula de densidad t-Student:
 *   f(t) = [Γ((ν+1)/2)] / [√(νπ) * Γ(ν/2)] * [1 + t²/ν]^(-(ν+1)/2)
 *   donde ν = grados de libertad (dof)
 *
 * Regla de Simpson compuesta:
 *   ∫[a,b] f(x) dx ≈ (h/3)[f(x₀) + 4f(x₁) + 2f(x₂) + 4f(x₃) + ... + f(x_n)]
 *   donde h = (b-a)/n, n debe ser par
 *
 * Uso:
 *   Para calcular la integral P = ∫[xInicial,xFinal] f(t) dt:
 *   double p = SimpsonIntegration.calculateP(xInicial, xFinal, numSeg, dof);
 *
 * Advertencias:
 *   - numSeg debe ser número par (la función lanzará excepción si es impar)
 *   - La precisión mejora al aumentar numSeg, pero incrementa costo computacional
 *   - Para dof pequeños (< 3), la función tiene colas más pesadas
 ***************************************************************************/

/**
 * Clase SimpsonIntegration.
 * <p>
 * Proporciona métodos estáticos para calcular la función de densidad
 * t-Student y realizar integración numérica mediante la Regla de Simpson
 * compuesta.
 */
public class SimpsonIntegration
{
    /**
     * Calcula el valor de la función de densidad de probabilidad
     * t-Student en un punto dado.
     * <p>
     * Implementación de la fórmula:
     *   f(t) = coeff * [1 + t²/dof]^(-(dof+1)/2)
     *   donde coeff = Γ((dof+1)/2) / [√(dof*π) * Γ(dof/2)]
     *
     * @param dblX   punto en el que evaluar la función
     * @param intDof grados de libertad de la distribución t
     * @return valor de la función de densidad en dblX
     *
     * @throws IllegalArgumentException si intDof ≤ 0
     */
    public static double tDistribution(double dblX, int intDof)
    {
        /*--------------------------------------------------------------*/
        /*  Validación de parámetros                                    */
        /*--------------------------------------------------------------*/
        
        if (intDof <= 0) 
        {
            throw new IllegalArgumentException(
                "tDistribution: dof debe ser > 0. Valor recibido: " + intDof);
        }
        
        /*--------------------------------------------------------------*/
        /*  Cálculo de constantes usando función Gamma                  */
        /*--------------------------------------------------------------*/
        
        // Calcular Γ((dof+1)/2) - numerador
        double dblGammaNumerador = GammaFunction.calculateHalf(intDof);
        
        // Calcular Γ(dof/2) - parte del denominador
        double dblGammaDenominador = GammaFunction.calculate(intDof / 2.0);
        
        // Calcular coeficiente de normalización completo
        double dblCoeficiente = dblGammaNumerador / 
                               (Math.sqrt(intDof * Math.PI) * dblGammaDenominador);
        
        /*--------------------------------------------------------------*/
        /*  Cálculo del término dependiente de x                        */
        /*--------------------------------------------------------------*/
        
        // Calcular (1 + x²/dof)
        double dblTerminoBase = 1.0 + (dblX * dblX) / intDof;
        
        // Calcular exponente: -(dof+1)/2
        double dblExponente = -(intDof + 1.0) / 2.0;
        
        // Calcular término de potencia
        double dblTerminoPotencia = Math.pow(dblTerminoBase, dblExponente);
        
        /*--------------------------------------------------------------*/
        /*  Valor final de la función de densidad                       */
        /*--------------------------------------------------------------*/
        
        return dblCoeficiente * dblTerminoPotencia;
    }
    
    /**
     * Calcula la integral de la función t-Student usando la Regla de Simpson.
     * <p>
     * Implementación de la Regla de Simpson compuesta:
     *   1. Divide el intervalo [a,b] en n segmentos iguales (n par)
     *   2. Evalúa la función en cada punto de la partición
     *   3. Aplica coeficientes 1,4,2,4,...,2,4,1
     *   4. Multiplica por h/3 para obtener la aproximación
     *
     * @param dblXInicial   límite inferior de integración
     * @param dblXFinal     límite superior de integración
     * @param intNumSeg     número de segmentos (debe ser par)
     * @param intDof        grados de libertad
     * @return valor aproximado de la integral P
     *
     * @throws IllegalArgumentException si intNumSeg no es par
     * @throws IllegalArgumentException si intDof ≤ 0
     */
    public static double calculateP(double dblXInicial, double dblXFinal,
                                   int intNumSeg, int intDof)
    {
        /*--------------------------------------------------------------*/
        /*  Validación de parámetros                                    */
        /*--------------------------------------------------------------*/
        
        if (intNumSeg % 2 != 0) 
        {
            throw new IllegalArgumentException(
                "calculateP: número de segmentos debe ser par. Valor recibido: " + intNumSeg);
        }
        
        if (intDof <= 0) 
        {
            throw new IllegalArgumentException(
                "calculateP: dof debe ser > 0. Valor recibido: " + intDof);
        }
        
        /*--------------------------------------------------------------*/
        /*  Inicialización de variables para la Regla de Simpson        */
        /*--------------------------------------------------------------*/
        
        // Calcular ancho de cada segmento: h = (b-a)/n
        double dblH = (dblXFinal - dblXInicial) / intNumSeg;
        
        // Inicializar suma con valores en los extremos: f(a) + f(b)
        double dblSuma = tDistribution(dblXInicial, intDof) + 
                        tDistribution(dblXFinal, intDof);
        
        /*--------------------------------------------------------------*/
        /*  Suma de términos internos con coeficientes 4 y 2            */
        /*--------------------------------------------------------------*/
        
        for (int intI = 1; intI < intNumSeg; intI++) 
        {
            // Calcular punto actual: x_i = a + i*h
            double dblXActual = dblXInicial + intI * dblH;
            
            // Evaluar función t-Student en punto actual
            double dblFx = tDistribution(dblXActual, intDof);
            
            // Aplicar coeficientes de Simpson:
            // - Índices impares: coeficiente 4
            // - Índices pares: coeficiente 2
            if (intI % 2 == 1) 
            {
                dblSuma += 4.0 * dblFx;   // Términos impares
            } 
            else 
            {
                dblSuma += 2.0 * dblFx;   // Términos pares
            }
        }
        
        /*--------------------------------------------------------------*/
        /*  Aplicar factor final de Simpson: (h/3) * suma               */
        /*--------------------------------------------------------------*/
        
        return (dblH / 3.0) * dblSuma;
    }
}