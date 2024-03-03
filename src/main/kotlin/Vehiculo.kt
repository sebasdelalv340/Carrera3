import kotlin.random.Random


/**
 * Clase abstracta que representa un vehículo genérico.
 * @param nombre El nombre del vehículo.
 * @param marca La marca del vehículo.
 * @param modelo El modelo del vehículo.
 * @param capacidadCombustible La capacidad de combustible del vehículo en litros.
 * @param combustibleActual El nivel actual de combustible del vehículo en litros.
 * @param kilometrosActuales Los kilómetros totales recorridos por el vehículo.
 */
open class Vehiculo(nombre: String,
                    open var marca: String,
                    open var modelo: String,
                    capacidadCombustible: Float = 0f,
                    combustibleActual: Float = 0f,
                    var kilometrosActuales: Float = 0f) {
    // Nombre del vehículo. Se valida que no esté duplicado.
    open var nombre: String = comprobarNombre(nombre)
        get() = capitalizar(field)

    open val capacidadCombustible: Float = capacidadCombustible.sinDecimales()
    open var combustibleActual: Float = combustibleActual

    fun capitalizar(nombre: String): String {
        return nombre.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase()} }
    }


    open var paradas: Int = 0

    companion object {
        // Constante que representa la cantidad de kilómetros que puede recorrer el vehículo por litro de combustible.
        const val KM_LITRO: Float = 10.0f

        // Conjunto mutable que almacena los nombres de los vehículos para validar duplicados.
        private var listanombre: MutableSet<String> = mutableSetOf()

        /**
         * Función de validación para el nombre del vehículo.
         * @param nombre El nombre del vehículo a validar.
         * @return El nombre del vehículo si no está duplicado.
         * @throws IllegalArgumentException Si el nombre ya existe.
         */

        fun comprobarNombre(nombre: String): String {
            val nombreAComprobar = nombre.lowercase().trim()
            require(nombre.isNotEmpty()) {"El $nombre no puede estar vacío."}
            require(!listanombre.contains(nombreAComprobar)) {"El $nombre ya existe."}
            listanombre.add(nombreAComprobar)
            return nombreAComprobar
        }

    }

    /**
     * Obtiene la información básica del vehículo.
     * @return Información sobre la marca, modelo y autonomía del vehículo.
     */
    open fun obtenerInfo() = "$nombre Vehiculo(km = $kilometrosActuales, combustible = $combustibleActual L)"


    /**
     * Calcula la autonomía del vehículo en kilómetros.
     * @return La autonomía del vehículo en kilómetros.
     */
    open fun calcularAutonomia(): Float {
        return combustibleActual * KM_LITRO
    }

    /**
     * Realiza un viaje con el vehículo.
     * @param distancia La distancia del viaje en kilómetros.
     * @return La distancia restante que no pudo ser recorrida debido a la falta de combustible.
     */
    open fun realizaViaje(distancia: Float): Float {
        return if (distancia >= calcularAutonomia()) {
            val kmRecorre = calcularAutonomia()
            combustibleActual = 0.0f
            kilometrosActuales += calcularAutonomia()
            distancia - kmRecorre
        } else {
            combustibleActual -= (distancia / KM_LITRO)
            kilometrosActuales += distancia
            0.0f
        }
    }

    /**
     * Reposta combustible al vehículo.
     * @param cantidad La cantidad de combustible a repostar en litros.
     * @return La cantidad de combustible que se pudo repostar.
     */
    fun repostar(cantidad: Float = 0f): Float {
        val combustiblePrevio = combustibleActual
        if (cantidad <= 0 || (combustibleActual + cantidad) >= capacidadCombustible) {
            combustibleActual = capacidadCombustible
            return capacidadCombustible - combustiblePrevio
        } else {
            combustibleActual += cantidad
            return cantidad
        }
    }

    /**
     * Muestra toda la informacción del vehículo.
     * @return un string con la info del vehículo.
     */
    override fun toString(): String {
        return "Vehículo: $nombre, Marca: $marca, Modelo: $modelo, Combustible Actual: ${combustibleActual.redondeo(2)} L."
    }
}

