import Guerrero._

type Movimiento = Contrincantes => Contrincantes

object cargarKi extends Movimiento {
  def apply(contrincantes: Contrincantes) : Contrincantes = {
    val (atacante, atacado) = contrincantes
     atacante match {
        case _ : Sayajin => (atacante.copy(ki= atacante.ki + atacante.nivelSS * 150), atacado)
        case _ : Androide => contrincantes
        case atacante => (atacante.copy(ki= atacante.ki + 100), atacado)
    }
  }
}




