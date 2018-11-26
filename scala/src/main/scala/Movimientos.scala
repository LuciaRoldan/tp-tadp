package DragonBall

import DragonBall.Magia.Magia


object Movimiento {

  type Criterio = (Contrincantes, Contrincantes) => Int
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]
  type Movimiento = Contrincantes => Contrincantes
  //type ContrincantesBiologicos = (Biologico, Biologico)

  class NoTieneItemException extends Exception
  class NoPuedeHacerEseAtaqueException extends Exception

  //los criterios deben retornar <0 si no se cumplen
  val diferenciaKiAtacante: Criterio = (contAntes: Contrincantes, contDespues: Contrincantes) => {
    val (atacanteAntes, atacanteDespues) = (contAntes._1, contDespues._1)
    val kiDespues = (atacanteAntes, atacanteDespues) match {
      case (_: Androide, _: Androide) => 0
      case (antes: Biologico, despues: Biologico) => antes.ki.-(despues.ki)
    }
    kiDespues
  }

  val noMeMata: Criterio = (contAntes: Contrincantes, contDespues: Contrincantes) => {
    if (contDespues._1.getVida() > 0) { 1 }
    else -1
  }


  object cargarKi extends Movimiento{
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      atacante match {
        case atacante: Sayajin => (atacante.cambiarKi(atacante.ki + atacante.nivelSS * 150), atacado)
        case _: Androide => contrincantes
        case atacante: Biologico => (atacante.cambiarKi(atacante.ki + 100), atacado)
      }
    }
  }
  
  object pedirAyudaADios extends Movimiento{
    def apply(contrincantes: Contrincantes): Contrincantes = {
      contrincantes
    }
  }

  case object DejarseFajar extends Movimiento{
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      val atacanteFajado = atacante.dejarseFajar()
      (atacanteFajado, atacado)
    }
  }

  class usarItem(item: Item) extends Movimiento {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      if (atacante.tieneItem(item)) {
        (item, atacante, atacado) match {
          case (ArmaRoma, _ :Androide, _) => (atacante, atacado)
          case (ArmaRoma, _, atacado :Biologico) if atacado.ki < 300 =>(atacante, atacado.cambiarEstado(Inconsciente))
          case (ArmaRoma, _, _) => (atacante, atacado)
          case (ArmaFilosa, _, atacado :Sayajin) if atacado.tieneCola =>  (atacante, atacado.perderCola.cambiarKi(1))
          case (ArmaFilosa, _, atacado :Mono) => (atacante, atacado.getSayajin.perderCola.cambiarKi(1).cambiarEstado(Inconsciente))
          case (ArmaFilosa, _, _ :Androide) => (atacante, atacado)
          case (ArmaFilosa, atacante :Biologico, atacado :Biologico) => (atacante, atacado.cambiarKi(atacado.ki - atacante.ki/100))
          case (arma: ArmaDeFuego,atacante: Biologico ,atacado: Humano)=> if(arma.municion > 0) {(atacante.perderMunicion(arma), atacado.cambiarKi(atacado.ki - 20))}
                                      else (atacante, atacado)
          case (arma: ArmaDeFuego,atacante ,atacado :Namekusein) => if(arma.municion > 0 && atacado.estado == Inconsciente)
                                                                      {(atacante.perderMunicion(arma), atacado.cambiarKi(atacado.ki - 10))}
                                                                      else (atacante, atacado)
        }
      }
      else throw new NoTieneItemException
    }
  }

  object convertirseEnMono extends Movimiento {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      atacante match {
        case atacante: Sayajin => (atacante.convertirseEnMono, atacado)
        case _ => (atacante, atacado)
      }
    }
  }

  case object ComerSemilla extends Movimiento {
    override def apply(contrincantes: Contrincantes): Contrincantes = ( contrincantes._1.cambiarVida(contrincantes._1.getVidaMaxima()).cambiarEstado(Normal), contrincantes._2)
  }

  object convertirseEnSuperSayajin extends Movimiento{
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      atacante match{
        case atacante: Sayajin => (atacante.aumentarNivelSS(), atacado)
        case _ => (atacante, atacado)
      }
    }
  }

  class fusionarse(amigo: Fusionable) extends Movimiento{
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      (atacante, atacado) match{
          case (atacante: Fusionable, atacado :Fusionable) => if(atacante.estado == Normal && atacado.estado == Normal) {(atacante.fusionar(atacante, amigo), atacado)} else {contrincantes}
          case _ => (atacante, atacado)
        }
    }
  }



  class hacerAtaqueTurbina(ataque: Ataque) extends Movimiento {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      ataque.ejecutar(atacante, atacado)
    }
  }

  class comerseAlOponente() extends Movimiento{
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val atacante = contrincantes._1
      atacante match {
        case atacante: Monstruo => atacante.formaDeComer(contrincantes)
        case _ => contrincantes
      }
    }
  }

  class hacerMagia(truco: Magia) extends Movimiento {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      atacante match {
        case _: Monstruo => truco(contrincantes)
        case _: Namekusein => truco(contrincantes)
        case atacante if atacante.tiene7Esferas() => truco(atacante.copear(nuevoInventario = atacante.inventario.filter(_ != new EsferasDelDragon(7))), atacado)
        case _ => contrincantes
      }
    }
  }

}




