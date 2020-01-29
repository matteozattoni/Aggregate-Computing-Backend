package adapters.scafi;

import org.junit.jupiter.api.Test
import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import org.junit.jupiter.api.Assertions._

import ScafiIncarnation._

class ExportWrapperTest {

    private val fileName = "export.ser"

    val export = new ExportImpl()
    export.put(new PathImpl(List()), 10)
    export.put(new PathImpl(List(new FoldHood[Int](0))), 20)
    export.put(new PathImpl(List(new Nbr[Int](0))), 1)

    @Test
    def serialize() {
        val wrap = new ExportWrapper(export)

        //serialize
        val out: ObjectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName))
        out.writeObject(wrap)
        out.close()

        //deserialize
        val in = new ObjectInputStream(new FileInputStream(fileName))
        val des = in.readObject.asInstanceOf[ExportWrapper]
        in.close()

        //deserialized object must be correct
        assertEquals(export.getAll.toSet, des.export.getAll.toSet)
    }
}