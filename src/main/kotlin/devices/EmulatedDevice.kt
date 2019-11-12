package devices

/**
 * Fully emulated Device
 */
class EmulatedDevice(id: Int) : AbstractDevice(id) {
    override fun getSensor(sensorName: String): Any {
        TODO("not implemented")
    }

    override fun execute() {

    }
}