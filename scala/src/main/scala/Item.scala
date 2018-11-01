sealed class Item

sealed class Arma extends Item

case object armaFilosa extends Arma
case object armaRoma extends Arma
case object armaDeFuego extends Arma

case object esfera extends Item
case object semilla extends Item