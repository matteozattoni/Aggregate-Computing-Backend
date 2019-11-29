package devices

interface Device {

    fun getID(): Int

    fun execute()

    fun getSensor(sensorName: String): Any

}