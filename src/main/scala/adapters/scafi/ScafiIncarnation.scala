package adapters.scafi

import it.unibo.scafi.incarnations.BasicAbstractIncarnation

object ScafiIncarnation extends BasicAbstractIncarnation with Serializable {
  def emptyContext(id: ID): ScafiIncarnation.CONTEXT = ScafiIncarnation.factory.context(id, Map())
}
