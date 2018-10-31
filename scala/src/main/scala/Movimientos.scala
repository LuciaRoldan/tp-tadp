import Guerrero._

type Movimiento = Contrincantes => Contrincantes

object cargarKi extends Movimiento {
  def apply(contrincantes: Contrincantes) : Contrincantes = {
    val (atacante, atacado) = contrincantes
     atacante match {
        case atacante : Sayajin => (atacante.disminuirKi(atacante.ki + atacante.nivelSS * 150), atacado)
        case _ : Androide => contrincantes
        case atacante : Guerrero => (atacante.disminuirKi(atacante.ki + 100), atacado)
    }
  }
}




