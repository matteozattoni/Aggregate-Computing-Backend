package devices

abstract class AbstractDevice(private val id: Int) : Device {

    override fun getID(): Int = id

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is AbstractDevice -> id == other.id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id
    }
}