package devices

class DummyDevice(id: Int) : AbstractDevice(id) {
    override fun execute() {

    }

    override fun getSensor(sensorName: String): Any {
        return 0
    }
}