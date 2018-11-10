package DragonBall

import DragonBall.Movimiento.Contrincantes

object Magia{
  type Magia = Contrincantes => Contrincantes

  object dejarInconcienteAlOponente extends Magia {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante,atacado.cambiarEstado(Inconsciente))
    }
  }

  class aumentarVidaPropiaYDisminuirLaDelEnemigo(vida: Int) extends Magia {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante.cambiarVida(atacante.getVida() + vida), atacado.cambiarVida(atacado.getVida() - vida))
    }
  }

  object vaciarInventarioEnemigo extends Magia {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante, atacado.copear(nuevoInventario = List()))
    }
  }
}




