package DragonBall

sealed class Item

sealed class Arma extends Item

case object ArmaFilosa extends Arma
case object ArmaRoma extends Arma
case class ArmaDeFuego(municion: Int) extends Arma

case object Esfera extends Item
case object Semilla extends Item

case object FotoDeLaLuna extends Item

case class EsferasDelDragon(cantidad: Int) extends Item