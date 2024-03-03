import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random


/**
 * Realiza el redondeo de un número de punto flotante a dos decimales.
 *
 * Esta función toma el valor actual de tipo Float y lo redondea a dos decimales.
 * Utiliza el método de redondeo estándar para redondear el número al entero más cercano.
 *
 * @return El valor de punto flotante redondeado a dos decimales.
 */
fun Float.redondeo(posiciones: Int): Float {
    val factor = 10.0.pow(posiciones.toDouble()).toFloat()
    return (this * factor).roundToInt() / factor
}

/**
 * Transforma un valor Float a un Float sin decimales.
 *
 * Esta función toma el valor actual de tipo Float y lo redondea a un entero y lo vuelve a transformar en FLoat.
 *
 * @return El valor de punto flotante sin decimales.
 */
fun Float.sinDecimales(): Float {
    return this.roundToInt().toFloat()
}


/**
 * Clase que representa una carrera entre varios vehículos.
 *
 * @property nombreCarrera El nombre de la carrera.
 * @property distanciaTotal La distancia total de la carrera en kilómetros.
 * @property participantes La lista de vehículos que participan en la carrera.
 * @property estadoCarrera Indica si la carrera está en curso o ha finalizado.
 * @property historialAcciones Un mapa que almacena el historial de acciones realizadas por cada vehículo durante la carrera.
 * @property posiciones Una lista mutable de pares que representa las posiciones actuales de los vehículos en la carrera.
 */
class Carrera(private val nombreCarrera: String,
              private val distanciaTotal: Float,
              private val participantes: List<Vehiculo> = listOf(),
              private var estadoCarrera: Boolean) {

    private var historialAcciones: MutableMap<String, MutableList<String>> = mutableMapOf()

    init {
        require(distanciaTotal >= 1000) { "La distancia total de la carrera debe ser al menos 1000 km." }
        participantes.forEach { vehiculo -> inicializaDatosParticipante(vehiculo) }
    }

    /**
    * Contiene la constante de los kilómetros por tramo.
     */
    companion object {
        const val KM_TRAMO = 20f
        var numRonda: Int = 1
    }

    /**
     * Representa el resultado final de un vehículo en la carrera, incluyendo su posición final, el kilometraje total recorrido,
     * el número de paradas para repostar, y un historial detallado de todas las acciones realizadas durante la carrera.
     *
     * @property vehiculo El [Vehiculo] al que pertenece este resultado.
     * @property posicion La posición final del vehículo en la carrera, donde una posición menor indica un mejor rendimiento.
     * @property kilometraje El total de kilómetros recorridos por el vehículo durante la carrera.
     * @property paradasRepostaje El número de veces que el vehículo tuvo que repostar combustible durante la carrera.
     * @property historialAcciones Una lista de cadenas que describen las acciones realizadas por el vehículo a lo largo de la carrera, proporcionando un registro detallado de su rendimiento y estrategias.
     */
    data class ResultadoCarrera(
        val vehiculo: Vehiculo,
        val posicion: Int,
        val kilometraje: Float,
        val paradasRepostaje: Int,
        val historialAcciones: MutableList<String>?
    )

    /**
     * Proporciona una representación en cadena de texto de la instancia de la carrera, incluyendo detalles clave como
     * el nombre de la carrera, la distancia total a recorrer, la lista de participantes, el estado actual de la carrera
     * (en curso o finalizada), el historial de acciones realizadas por los vehículos durante la carrera y las posiciones
     * actuales de los participantes.
     *
     * @return Una cadena de texto que describe los atributos principales de la carrera, incluyendo el nombre,
     * distancia total, participantes, estado actual, historial de acciones y posiciones de los vehículos participantes.
     */
    override fun toString(): String {
        return "NombreCarrera: $nombreCarrera, DistanciaTotal: $distanciaTotal, Participantes: $participantes, EstadoCarrera: $estadoCarrera, HistorialAcciones: $historialAcciones." }

    /**
     * Inicializa los datos de un participante en la carrera, preparando su historial de acciones y estableciendo
     * su posición inicial. Este método se llama automáticamente al agregar un nuevo vehículo a la carrera.
     *
     * @param vehiculo El [Vehiculo] cuyos datos se inicializan.
     */
    private fun inicializaDatosParticipante(vehiculo: Vehiculo) {
        historialAcciones[vehiculo.nombre] = mutableListOf()
    }

    /**
     * Función para iniciar la carrera.
     * Esta función ejecuta la carrera hasta que se determina un ganador.
     */
    fun iniciarCarrera() {
        estadoCarrera = true
        println("¡Comienza la carrera!")
        while (estadoCarrera) {
            val vehiculo = participantes.random()
            avanzarVehiculo(vehiculo)
            mostrarClasificacionParcial()
            determinarGanador()
        }
        mostrarClasificacionFinal()
    }

    /**
    * Muestra la clasificación por posición.
     */
    private fun mostrarClasificacionParcial() {
        println("\n* Clasificación Parcial (ronda ${numRonda++}):\n")
        var posicion = 1
        participantes.toList().sortedByDescending { it.kilometrosActuales }.forEach {
            println("${posicion++} -> " + it.obtenerInfo())
        }
        println()
    }

    private fun mostrarClasificacionFinal() {
        println("\n* Clasificación Final:\n")
        var posicion = 1
        participantes.toList().sortedByDescending { it.kilometrosActuales }.forEach {
            println("${posicion++} -> ${it.nombre} (${it.kilometrosActuales.redondeo(2)} kms)")
        }
        println()
    }


    /**
    * El vehículo avanza una distancia y registra la acción.
    *
    * @param vehiculo El vehiculo que avanza.
     * @param distanciaTramo La distancia que avanza el vehiculo.
     */
    private fun avanzarTramo(vehiculo: Vehiculo, distanciaTramo: Float) {
        var distanciaNoRecorrida = vehiculo.realizaViaje(distanciaTramo).redondeo(2)
        registraAccion(vehiculo.nombre, "${vehiculo.nombre} ha recorrido ${distanciaTramo - distanciaNoRecorrida} km."
        )
        while (distanciaNoRecorrida > 0) {

            repostarVehiculo(vehiculo)

            val distanciaRestante = distanciaNoRecorrida
            distanciaNoRecorrida = vehiculo.realizaViaje(distanciaRestante).redondeo(2)
            registraAccion(vehiculo.nombre, "${vehiculo.nombre} ha recorrido ${distanciaRestante - distanciaNoRecorrida} km.")
        }
    }

    /**
     * Obtiene una distancia aleatoria entre 10 y 200 kilómetros.
     *
     * @param vehiculo El vehículo que recorre dicha distancia.
     * @return La distancia aleatoria.
     */
    private fun obtenerDistanciaAleatoria(vehiculo: Vehiculo): Float {
        val distanciaAleatoria = (1000..20000).random().toFloat() / 100

        return if (distanciaAleatoria + vehiculo.kilometrosActuales > distanciaTotal) {
            distanciaTotal - vehiculo.kilometrosActuales
        } else {
            distanciaAleatoria
        }
    }

    /**
     * Avanza un vehículo en la carrera.
     *
     * @param vehiculo El vehículo que avanza.
     */
    private fun avanzarVehiculo(vehiculo: Vehiculo) {
        val distanciaAleatoria = obtenerDistanciaAleatoria(vehiculo).redondeo(2)
        val numTramos = (distanciaAleatoria / KM_TRAMO).toInt()
        val distanciaRestante = distanciaAleatoria % KM_TRAMO
        val repeticionesFiligrana = (0..3).random()

        for (i in (1..numTramos)) {
            avanzarTramo(vehiculo, KM_TRAMO)
            repeat(repeticionesFiligrana) {realizarFiligrana(vehiculo) }
            ajustarKm(vehiculo)
        }
        avanzarTramo(vehiculo, distanciaRestante)
    }

    /**
     * Ajusta a 0 los kilómetros del vehículo si son negativos
     */
    private fun ajustarKm(vehiculo: Vehiculo) {
        if (vehiculo.kilometrosActuales < 0) {
            vehiculo.kilometrosActuales = 0.0f
        }
    }

    /**
     * Realiza el repostaje de combustible completo a un vehículo.
     *
     * @param vehiculo El vehículo que realiza el repostaje.
     */
    private fun repostarVehiculo(vehiculo: Vehiculo) {
        val combustibleRepostado = vehiculo.repostar().redondeo(2)

        val accionRespostar = "${vehiculo.nombre} ha repostado $combustibleRepostado litros."

        registraAccion(vehiculo.nombre, accionRespostar)
        vehiculo.paradas++
    }


    /**
     * Realiza una acción de filigrana con el vehículo.
     *
     * @param vehiculo El vehículo que realiza la filigrana.
     */
    private fun realizarFiligrana(vehiculo: Vehiculo) {
        val retrasokm = (10..50).random().toFloat()
        when (vehiculo) {
            is Automovil -> {
                vehiculo.realizaDerrape().redondeo(2)
                registraAccion(vehiculo.nombre, "Derrape: Combustible restante ${vehiculo.combustibleActual.redondeo(2)} L.")
                vehiculo.kilometrosActuales -= retrasokm
                registraAccion(vehiculo.nombre, "${vehiculo.nombre} se ha retrasado $retrasokm (km=${vehiculo.kilometrosActuales.redondeo(2)}).")
            }

            is Motocicleta -> {
                vehiculo.realizaCaballito().redondeo(2)
                registraAccion(vehiculo.nombre, "Caballito: Combustible restante ${vehiculo.combustibleActual.redondeo(2)} L.")
                vehiculo.kilometrosActuales -= retrasokm
                registraAccion(vehiculo.nombre, "${vehiculo.nombre} se ha retrasado $retrasokm (km=${vehiculo.kilometrosActuales.redondeo(2)}).")
            }

            is Camion -> {
                vehiculo.realizaDerrape().redondeo(2)
                registraAccion(vehiculo.nombre, "Derrape: Combustible restante ${vehiculo.combustibleActual.redondeo(2)} L.")
                vehiculo.kilometrosActuales -= retrasokm
                registraAccion(vehiculo.nombre, "${vehiculo.nombre} se ha retrasado $retrasokm (km=${vehiculo.kilometrosActuales.redondeo(2)}).")
            }

            is Quad -> {
                vehiculo.realizaCaballito().redondeo(2)
                registraAccion(vehiculo.nombre, "Caballito: Combustible restante ${vehiculo.combustibleActual.redondeo(2)} L.")
                vehiculo.kilometrosActuales -= retrasokm
                registraAccion(vehiculo.nombre, "${vehiculo.nombre} se ha retrasado $retrasokm (km=${vehiculo.kilometrosActuales.redondeo(2)}).")
            }
        }
    }


    /**
     * Determina al ganador de la carrera.
     */
    private fun determinarGanador() {
        for (vehiculo in participantes) {
            if (vehiculo.kilometrosActuales >= distanciaTotal) {
                println("\n¡Carrera finalizada!")
                println("\n${vehiculo.nombre} gana la carrera.\n")
                estadoCarrera = false
                break
            }
        }
    }


    /**
     * Obtiene los resultados finales de la carrera para todos los vehículos.
     *
     * @return Una lista de resultados de la carrera para cada vehículo.
     */
    fun obtenerResultados(): MutableList<ResultadoCarrera> {
        val listaParticipantes = participantes.sortedByDescending { it.kilometrosActuales }
        val resultadoCarrera: MutableList<ResultadoCarrera> = mutableListOf()
        for (vehiculo in listaParticipantes) {
            resultadoCarrera.add(
                ResultadoCarrera(
                    vehiculo,
                    participantes.indexOf(vehiculo),
                    vehiculo.kilometrosActuales,
                    vehiculo.paradas,
                    historialAcciones[vehiculo.nombre]
                )
            )
        }
        return resultadoCarrera
    }

    /**
     * Registra una acción realizada por un vehículo durante la carrera.
     *
     * @param vehiculo El nombre del vehículo.
     * @param accion La acción realizada por el vehículo.
     */
    private fun registraAccion(vehiculo: String, accion: String) {
        val listaAcciones = historialAcciones[vehiculo]
        if (listaAcciones != null) {
            listaAcciones.add(accion)
            historialAcciones[vehiculo] = listaAcciones
        } else {
            historialAcciones[vehiculo] = mutableListOf(accion)
        }
    }

    /**
     * Muestra el historial de acciones de cada vehículo según la clasificación.
     */
    fun obtenerHistorialDetallado() {
        println("\n* Historial Detallado:\n")
        var posicion = 1
        obtenerResultados().forEach {
            print("${posicion++} -> ")
            println(it.vehiculo.nombre + "\n" + (it.historialAcciones?.joinToString("\n")))
            println()
        }
    }
}