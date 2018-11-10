package DragonBall

sealed class Ataque

sealed class AtaqueFisico extends Ataque
sealed class AtaqueDeEnergia extends Ataque


case object MuchosGolpesNinja extends AtaqueFisico
case object Explotar extends AtaqueFisico

case class Onda(energia: Int) extends AtaqueDeEnergia
case object Genkidama extends AtaqueDeEnergia