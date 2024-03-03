
enum class Cilindrada(val desc: Int) {
    CC125(125),
    CC250(250),
    CC400(400),
    CC500(500),
    CC750(750),
    CC900(900),
    CC1000(1000)
}


/**
 * Clase que representa una motocicleta.
 * @param nombre El nombre de la motocicleta.
 * @param marca La marca de la motocicleta.
 * @param modelo El modelo de la motocicleta.
 * @param capacidadCombustible La capacidad de combustible de la motocicleta en litros.
 * @param combustibleActual El nivel actual de combustible de la motocicleta en litros.
 * @param kilometrosActuales Los kilómetros totales recorridos por la motocicleta.
 * @param cilindrada La cilindrada de la motocicleta en centímetros cúbicos (cc).
 */
open class Motocicleta(nombre: String,
                       marca: String,
                       modelo: String,
                       capacidadCombustible: Float,
                       combustibleActual: Float,
                       kilometrosActuales: Float,
                       val cilindrada: Cilindrada
    ): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {

    override var paradas: Int = 0
    override var rondas: Int = 0


    companion object{
        // La cantidad de kilómetros que puede recorrer la motocicleta por litro de combustible.
        const val KM_LITRO_MOTO: Float = 20.0f
        // El consumo de combustible al realizar un caballito.
        const val CABALLITO: Float = 6.5f
    }

    override fun obtenerInfo() = "$nombre Motocicleta(km = $kilometrosActuales, combustible = ${combustibleActual.redondeo(2)} L)"

    /**
     * Calcula la autonomía de la motocicleta en kilómetros.
     * @return La autonomía de la motocicleta en kilómetros.
     */
    override fun calcularAutonomia(): Float {
        return if (cilindrada.desc == 1000) {
            combustibleActual * KM_LITRO_MOTO
        } else {
            combustibleActual * calcularKmLitroPorCC()
        }
    }

    /**
     * Calcula la cantidad de kilómetros que puede recorrer la motocicleta por litro de combustible, considerando la cilindrada.
     * @return La cantidad de kilómetros que puede recorrer la motocicleta por litro de combustible.
     */
    fun calcularKmLitroPorCC(): Float {
        return KM_LITRO_MOTO - ((1000 - cilindrada.desc) / 1000)
    }

    /**
     * Realiza un viaje con la motocicleta.
     * @param distancia La distancia del viaje en kilómetros.
     * @return La distancia restante que no pudo ser recorrida debido a la falta de combustible.
     */
    override fun realizaViaje(distancia: Float): Float {
        if (distancia >= calcularAutonomia()) {
            val kmRecorre = calcularAutonomia().redondeo(2)
            combustibleActual = 0.0f
            kilometrosActuales += calcularAutonomia()
            return distancia - kmRecorre
        } else {
            combustibleActual -= (distancia / calcularKmLitroPorCC())
            kilometrosActuales += distancia
            return  0.0f
        }
    }

    /**
     * Realiza un caballito con la motocicleta.
     * @return El nivel de combustible restante después de realizar el caballito.
     */
    fun realizaCaballito(): Float {
        combustibleActual -= (CABALLITO / calcularKmLitroPorCC())
        return if (combustibleActual < 0) {
            combustibleActual = 0f
            0f
        } else {
            combustibleActual.redondeo(2)
        }
    }

    /**
     * Muestra toda la informacción del vehículo.
     * @return un string con la info del vehículo.
     */
    override fun toString(): String {
        return "Motocicleta(nombre=$nombre, marca=$marca, modelo=$modelo, capacidad=$capacidadCombustible L, combustible=$combustibleActual L, cc=${cilindrada.desc})"
    }
}