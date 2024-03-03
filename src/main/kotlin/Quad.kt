
enum class TipoQuad(val desc: String) {
    CUADRICICLOS_LIGEROS("Cuadricíclos ligeros"),
    CUADRICICLOS_NO_LIGEROS("Cuadricíclos no ligeros"),
    VEHICULOS_ESPAECIALES("Vehiculos especiales")
}

class Quad(nombre: String,
           marca: String,
           modelo: String,
           capacidadCombustible: Float,
           combustibleActual: Float,
           kilometrosActuales: Float,
           val cilindrada: Cilindrada,
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {
    val tipo: TipoQuad = TipoQuad.entries.toTypedArray().random()

    override var paradas: Int = 0
    override var rondas: Int = 0

    companion object{
        // La cantidad de kilómetros que puede recorrer la motocicleta por litro de combustible.
        const val KM_LITRO_MOTO: Float = 20.0f
        // El consumo de combustible al realizar un caballito.
        const val CABALLITO: Float = 6.5f
    }

    override fun obtenerInfo() = "$nombre Quad(km = $kilometrosActuales, km=$kilometrosActuales, combustible = ${combustibleActual.redondeo(2)} L)"

    /**
     * Calcula la cantidad de kilómetros que puede recorrer el quad por litro de combustible, considerando la cilindrada.
     * @return La cantidad de kilómetros que puede recorrer el quad por litro de combustible.
     */
    fun calcularKmLitroPorCC(): Float {
        return KM_LITRO_MOTO - ((1000 - cilindrada.desc) / 1000)
    }

    /**
     * Calcula la autonomía del quad en kilómetros.
     * @return La autonomía del quad en kilómetros.
     */
    override fun calcularAutonomia(): Float {
        return if (cilindrada.desc == 1000) {
            (combustibleActual * KM_LITRO_MOTO) / 2
        } else {
            (combustibleActual * calcularKmLitroPorCC()) / 2
        }
    }

    /**
     * Realiza un viaje con del quad.
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
     * Realiza un caballito con el quad.
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
        return "Quad(nombre=$nombre, capacidad=$capacidadCombustible L, combustible=$combustibleActual L, cc=${cilindrada.desc}, tipo=${tipo.desc})"
    }
}