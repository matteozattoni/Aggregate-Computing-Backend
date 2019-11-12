package devices

interface Device {

    fun execute()

    fun getSensor(sensorName: String): Any

}