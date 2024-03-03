class Camion(nombre: String,
             marca: String,
             modelo: String,
             capacidadCombustible: Float,
             combustibleActual: Float,
             kilometrosActuales: Float,
             val peso: Float
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {
    override var capacidadCombustible: Float = capacidadCombustible.sinDecimales()
    override var combustibleActual: Float = combustibleActual.redondeo(2)

    override var paradas: Int = 0
    override var rondas: Int = 0

    init {
        require(capacidadCombustible in 90f..150f) {"La capacidad debe ser un valor entre 90 y 150 litros."}
        require(peso in 1000f..10000f) {"El peso debe ser un valor entre 1000 y 10000 kg."}
        require(capacidadCombustible in 90f..150f) {"La capacidad debe ser una valor entre 90 y 150 litros."}
    }


    companion object {
        const val KM_LITRO_CAMION = 6.25f
        const val REDUX_AUTONOMIA_CAMION = 1000f
        const val REDUX_PESO = 0.2
        const val DERRAPE_GAS = 7.5f
    }

    override fun obtenerInfo() = "$nombre Camión(km = $kilometrosActuales, combustible = ${combustibleActual.redondeo(2)} L)"

    private fun calcularReduccionPorPeso(): Float {
        val reduccionKm = (peso / REDUX_AUTONOMIA_CAMION) * REDUX_PESO
        return reduccionKm.toFloat()
    }


    override fun calcularAutonomia(): Float {
        return combustibleActual * (KM_LITRO_CAMION - calcularReduccionPorPeso())
    }

    override fun realizaViaje(distancia: Float): Float {
        if (distancia >= calcularAutonomia()) {
            val kmRecorre = calcularAutonomia()
            combustibleActual = 0.0f
            kilometrosActuales += calcularAutonomia()
            return distancia - kmRecorre
        } else {
            combustibleActual -= distancia / (KM_LITRO_CAMION - calcularReduccionPorPeso())
            kilometrosActuales += distancia
            return 0.0f
        }
    }

    fun realizaDerrape(): Float {
        val derrapeFactor =  DERRAPE_GAS
        combustibleActual -= derrapeFactor / calcularAutonomia()
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
    override fun toString() = "Camión(nombre=$nombre, capacidad=$capacidadCombustible L, combustible=$combustibleActual L, peso=$peso kg)"
}