package DragonBall

sealed class Item

sealed class Arma extends Item
//agregar comportamiento a las armas?
case object ArmaFilosa extends Arma
case object ArmaRoma extends Arma
case class ArmaDeFuego(municion: Int, tipo :TipoArmaDeFuego) extends Arma

abstract class TipoArmaDeFuego
case object TipoEscopeta extends TipoArmaDeFuego

case object Esfera extends Item
case object Semilla extends Item

case object FotoDeLaLuna extends Item

case class EsferasDelDragon(cantidad: Int) extends Item