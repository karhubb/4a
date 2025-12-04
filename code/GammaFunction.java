/***************************************************************************
 * Programa:  Regla de Simpson para Distribución t-Student
 * Clase:     GammaFunction
 * Autor:     Karla Sofía Castro Pérez
 * Fecha:     4-Diciembre-2024
 *
 * Descripción:
 *   Implementa el cálculo de la función Gamma utilizando recursividad.
 *   Esta función es requerida para calcular la constante de normalización
 *   de la distribución t-Student.
 *
 * Fórmula de la función Gamma para enteros:
 *   Γ(n) = (n-1)! para n entero positivo
 *
 * Casos base:
 *   Γ(1) = 1
 *   Γ(0.5) = √π
 *
 * Relación recursiva:
 *   Γ(x) = (x-1) * Γ(x-1)
 *
 * Uso en distribución t-Student:
 *   Se requiere calcular Γ((dof+1)/2) y Γ(dof/2) para obtener el
 *   factor de normalización de la función de densidad de probabilidad.
 *
 * Advertencias:
 *   - Solo funciona correctamente para x > 0
 *   - Para valores muy grandes puede causar desbordamiento de pila
 *   - Está optimizada para los valores necesarios en este programa
 ***************************************************************************/

/**
 * Clase GammaFunction.
 * <p>
 * Proporciona métodos estáticos para calcular la función Gamma mediante
 * recursividad. Implementada específicamente para los valores requeridos
 * por la distribución t-Student.
 */
public class GammaFunction
{
    /**
     * Calcula la función Gamma mediante recursividad.
     * <p>
     * Implementación recursiva basada en:
     *   Γ(1) = 1
     *   Γ(0.5) = √π
     *   Γ(x) = (x-1) * Γ(x-1)
     *
     * @param dblX valor para el cual calcular Γ(x), debe ser > 0
     * @return valor de Γ(x)
     *
     * @throws IllegalArgumentException si dblX ≤ 0
     * @throws StackOverflowError si la recursividad es muy profunda
     */
    public static double calculate(double dblX)
    {
        /*--------------------------------------------------------------*/
        /*  Validación de parámetro de entrada                          */
        /*--------------------------------------------------------------*/
        
        if (dblX <= 0.0) 
        {
            throw new IllegalArgumentException(
                "GammaFunction.calculate: x debe ser > 0. Valor recibido: " + dblX);
        }
        
        /*--------------------------------------------------------------*/
        /*  Caso base: Γ(1) = 1                                         */
        /*--------------------------------------------------------------*/
        
        if (Math.abs(dblX - 1.0) < 1.0e-10) 
        {
            return 1.0;
        }
        
        /*--------------------------------------------------------------*/
        /*  Caso base: Γ(0.5) = √π                                      */
        /*--------------------------------------------------------------*/
        
        if (Math.abs(dblX - 0.5) < 1.0e-10) 
        {
            return Math.sqrt(Math.PI);
        }
        
        /*--------------------------------------------------------------*/
        /*  Relación recursiva: Γ(x) = (x-1) * Γ(x-1)                   */
        /*--------------------------------------------------------------*/
        
        return (dblX - 1.0) * calculate(dblX - 1.0);
    }
    
    /**
     * Calcula Γ((n+1)/2), que es comúnmente requerido en la
     * distribución t-Student.
     * <p>
     * Método auxiliar especializado para simplificar el cálculo
     * del numerador en la constante de normalización t.
     *
     * @param dblN valor n (generalmente grados de libertad)
     * @return Γ((n+1)/2)
     */
    public static double calculateHalf(double dblN)
    {
        // Calcular Γ((n+1)/2) usando el método recursivo principal
        return calculate((dblN + 1.0) / 2.0);
    }
}