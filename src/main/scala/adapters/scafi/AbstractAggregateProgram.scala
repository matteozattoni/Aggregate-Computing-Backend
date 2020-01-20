package adapters.scafi

/**
 * The only purpose for this class is to permit katlin to define Aggregate Programs making use
 * of all the needed implicits
 */
abstract class AbstractAggregateProgram extends ScafiIncarnation.AggregateProgram

class def0[R](body: kotlin.jvm.functions.Function0[R]) extends ( () => R) {
  override def apply(): R = body.invoke()
}
class def1[P,R](body: kotlin.jvm.functions.Function1[P,R]) extends (P => R) {
  override def apply(v1: P): R = body.invoke(v1)
}
class def2[P1,P2,R](body: kotlin.jvm.functions.Function2[P1,P2,R]) extends ((P1,P2) => R) {
  override def apply(v1: P1, v2: P2): R = body.invoke(v1,v2)
}
