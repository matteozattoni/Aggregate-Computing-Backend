package adapters.protelis

import org.protelis.lang.datatype.DeviceUID
import java.io.Serializable
import java.util.*

class ServerUID(private val uuid: UUID): DeviceUID, Comparable<ServerUID>, Serializable {
    constructor(): this(UUID.randomUUID())

    fun getUID() = uuid

    override fun compareTo(other: ServerUID): Int {
        return uuid.compareTo(other.uuid)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ServerUID -> uuid == other.uuid
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun toString(): String {
        return uuid.toString()
    }
}

