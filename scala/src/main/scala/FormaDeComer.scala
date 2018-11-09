package DragonBall

import DragonBall.Movimiento.Contrincantes

object FormaDeComer{
  type FormaDeComer = Contrincantes => Contrincantes

  object FormaDeComerDeMajinBuu extends FormaDeComer {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante: Monstruo, atacado) = contrincantes
      return (atacante.cambiarMovimientosAdquiridos(atacado.listaDeMovimientos),atacado.cambiarEstado(new Muerto(0)))
    }
  }

  object FormaDeComerDeCell extends FormaDeComer {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante: Monstruo, atacado) = contrincantes
      return (atacante.cambiarMovimientosAdquiridos(atacado.listaDeMovimientos ++ atacante.movimientosAdquiridos)
        ,atacado.cambiarEstado(new Muerto(0)))
    }
  }
}




