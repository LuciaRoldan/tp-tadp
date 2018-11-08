package DragonBall

sealed class Item

sealed class Arma extends Item

case object ArmaFilosa extends Arma
case object ArmaRoma extends Arma
case object ArmaDeFuego extends Arma

case object Esfera extends Item
case object Semilla extends Item

case object FotoDeLaLuna extends Item