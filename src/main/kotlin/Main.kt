import kotlin.random.Random


fun main() {
    val marcasAutomovil: List<String> = listOf("Ford", "Renault", "Audi", "Mercedes", "Volvo")
    val modelosAutomovil: List<String> = listOf("Focus", "Megane", "A4", "CLA", "V40")

    val marcasMotocicleta: List<String> = listOf("Derbi", "Yamaha", "Kymco", "Honda", "Suzuki")
    val modelosMotocicleta: List<String> = listOf("Variant", "Tmax", "Superdink", "CBR", "GSX")

    val listaVehiculos: MutableList<Vehiculo> = mutableListOf()

    var numParticipantes = 0

    try {
        print("Introduce el número de participantes: ")
        numParticipantes = readln().toInt()
    } catch (e: NumberFormatException) {
        println("*Error* - Por favor ingrese un número válido.")
    }

    /**
     * Registra un vehículo de forma aleatoria por cada nombre introducido, lo añade a una lista y muestra los datos de cada vehículo.
     */
    for (i in 1..numParticipantes) {
        var nombre = ""
        do {
            try {
                print("\nNombre del vehículo $i -> ")
                nombre = readlnOrNull() ?: throw IllegalArgumentException("¡Nombre inválido!")
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (nombre.isEmpty())

        val vehiculo: Vehiculo = when (Random.nextInt(4)) {
            0 -> {
                val indiceAleatorio = marcasAutomovil.indices.random()
                val marca = marcasAutomovil[indiceAleatorio]
                val modelo = modelosAutomovil[indiceAleatorio]
                val capacidadCombustible = (30..60).random().toFloat()
                val combustibleActual = capacidadCombustible * ((2..10).random() / 10f)
                val kilometrosActuales = 0f
                val esHibrido: Hibrido = Hibrido.entries.toTypedArray().random()
                Automovil(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, esHibrido)
            }
            1 -> {
                val indiceAleatorio = marcasMotocicleta.indices.random()
                val marca = marcasMotocicleta[indiceAleatorio]
                val modelo = modelosMotocicleta[indiceAleatorio]
                val capacidadCombustible = (15..30).random().toFloat()
                val combustibleActual = capacidadCombustible * ((2..10).random() / 10f)
                val kilometrosActuales = 0f
                val cilindrada: Cilindrada = Cilindrada.entries.toTypedArray().random()
                Motocicleta(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, cilindrada)
            }
            2 -> {
                val marca = ""
                val modelo = ""
                val capacidadCombustible = (90..150).random().toFloat()
                val combustibleActual = capacidadCombustible * ((2..10).random() / 10f)
                val kilometrosActuales = 0f
                val peso = (1000..10000).random().toFloat()
                Camion(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, peso)
            }
            else -> {
                val marca = ""
                val modelo = ""
                val capacidadCombustible = (15..30).random().toFloat()
                val combustibleActual = capacidadCombustible * ((2..10).random() / 10f)
                val kilometrosActuales = 0f
                val cilindrada: Cilindrada = Cilindrada.entries.toTypedArray().random()
                Quad(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, cilindrada)
            }
        }
        listaVehiculos.add(vehiculo)
        println("Te ha tocado un $vehiculo")
    }

    val competicion: Carrera = Carrera("GranPrix", 1000f, listaVehiculos, false)

    competicion.iniciarCarrera()

}