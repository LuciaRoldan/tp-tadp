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

  /*object cambiarVida {
    def apply(guerrero: Guerrero, vida: Int): Guerrero ={
      guerrero match{
        case guerrero: Biologico => guerrero.cambiarKi(vida)
        case guerrero: Androide => guerrero.cambiarBateria(vida)
      }
    }
  }

  object getVida {
    def apply(guerrero: Guerrero): Int ={
      guerrero match{
        case guerrero: Biologico => guerrero.ki
        case guerrero: Androide => guerrero.bateria
      }
    }
  }*/

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

  case object DejarseFajar extends Movimiento{
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      val atacanteFajado = atacante.cambiarEstado(atacante.estado.dejarseFajar)
      printf("me deje fajar")
      (atacanteFajado, atacado)
    }
  }

  class usarItem(item: Item) extends Movimiento {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      if (atacante.tieneItem(item)) {
        (item, atacante, atacado) match {
          case (ArmaRoma, _ :Androide, _) => (atacante, atacado)
          case (ArmaRoma, _, atacado :Biologico) if atacado.ki < 300 =>(atacante, atacado.cambiarEstado(Inconsciente(atacado.estado.roundsFajado)))
          case (ArmaRoma, _, _) => (atacante, atacado)
          case (ArmaFilosa, _, atacado :Sayajin) if atacado.tieneCola =>  (atacante, atacado.perderCola.cambiarKi(1))
          case (ArmaFilosa, _, atacado :Mono) => (atacante, atacado.getSayajin.perderCola.cambiarKi(1))
          case (ArmaFilosa, _, _ :Androide) => (atacante, atacado)
          case (ArmaFilosa, atacante :Biologico, atacado :Biologico) => (atacante, atacado.cambiarKi(atacado.ki - atacante.ki/100))
          case (ArmaDeFuego, atacante :Biologico, atacado :Humano) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 20))}
                                      else (atacante, atacado)
          case (ArmaDeFuego, atacante, atacado :Namekusein) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 10))}
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
    override def apply(v1: (Guerrero, Guerrero)): (Guerrero, Guerrero) = ???
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
      if(amigo.asInstanceOf[Guerrero].estado.isInstanceOf[Normal]){
        atacante match{
          case atacante: Fusionable => (atacante.fusionar(atacante.asInstanceOf[Biologico], amigo.asInstanceOf[Biologico]), atacado)
          case _ => (atacante, atacado)
        }
      } else { contrincantes }
    }
  }

  /*class hacerAtaque(ataque: Ataque) extends Movimiento{
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      (ataque, atacante, atacado) match{
        case (MuchosGolpesNinja, atacante: Humano, atacado: Androide) => (atacante.cambiarKi(-10), atacado)
        case (MuchosGolpesNinja, atacante: Biologico, atacado: Biologico) => if(atacado.ki < atacante.ki){(atacante, atacado.cambiarKi(atacado.ki - 20))}
                                                                              else {(atacante.cambiarKi(atacante.ki - 20), atacado)}
        case (MuchosGolpesNinja, atacante: Biologico, atacado: Androide) => if(atacado.bateria  < atacante.ki){(atacante, atacado.cambiarBateria(atacado.bateria - 20))}
                                                                            else {(atacante.cambiarKi(atacante.ki - 20), atacado)}
        case (MuchosGolpesNinja, atacante:Androide, atacado: Biologico) =>  if(atacado.ki  < atacante.bateria){(atacante, atacado.cambiarKi(atacado.ki - 20))}
                                                                            else {(atacante.cambiarBateria(atacante.bateria - 20), atacado)}

        case (Explotar, atacante: Monstruo, atacado: Namekusein) => (atacante.cambiarKi(0), atacado.cambiarKi(math.max(atacado.ki - atacante.ki*2, 1)))
        case (Explotar, atacante: Monstruo, atacado: Androide) => (atacante.cambiarKi(0), atacado.cambiarBateria(atacado.bateria - atacante.ki*2))
        case (Explotar, atacante: Monstruo, atacado: Biologico) => (atacante.cambiarKi(0), atacado.cambiarKi(atacado.ki - atacante.ki*2))
        case (Explotar, atacante: Androide, atacado: Androide) => (atacante.cambiarBateria(0), atacado.cambiarBateria(atacado.bateria - atacante.bateria*3))
        case (Explotar, atacante: Androide, atacado: Biologico) => (atacante.cambiarBateria(0), atacado.cambiarKi(atacado.ki - atacante.bateria*3))
        case (Explotar, atacante: Androide, atacado: Androide) => throw new NoPuedeHacerEseAtaqueException

        case (Onda(energia), atacante: Biologico , _) => if(atacante.ki < energia){(atacante, atacado)} else {
              case (Onda(energia), atacante: Biologico, atacado: Monstruo) => {(atacante.cambiarKi(atacante.ki - energia), atacado.cambiarKi(atacado.ki - energia/2))}
              case (Onda(energia), atacante: Biologico, atacado: Androide) => {(atacante.cambiarKi(atacante.ki - energia), atacado.cambiarBateria(atacado.bateria + energia*2))}
              case (Onda(energia), atacante: Biologico, atacado: Biologico) => {(atacante.cambiarKi(atacante.ki - energia), atacado.cambiarKi(atacado.ki - energia*2))}
              }
        case (Onda(energia), atacante: Androide, _) => if(atacante.bateria < energia){(atacante, atacado)} else {
              case (Onda(energia), atacante: Androide, atacado: Monstruo) => {(atacante.cambiarBateria(atacante.bateria - energia), atacado.cambiarKi(atacado.ki - energia/2))}
              case (Onda(energia), atacante: Androide, atacado: Androide) => {(atacante.cambiarBateria(atacante.bateria - energia), atacado.cambiarBateria(atacado.bateria + energia*2))}
              case (Onda(energia), atacante: Androide, atacado: Biologico) => {(atacante.cambiarBateria(atacante.bateria - energia), atacado.cambiarKi(atacado.ki - energia*2))}
        }
      }
    }
  }*/


  class hacerAtaqueTurbina(ataque: Ataque) extends Movimiento {
    override def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      (ataque, atacante, atacado) match {
        case (MuchosGolpesNinja, atacante: Humano, atacado: Androide) => (atacante.cambiarVida(atacante.getVida - 10), atacado)
        case (MuchosGolpesNinja, atacante, atacado) => if (atacado.getVida < atacante.getVida) {(atacante, atacado.cambiarVida(atacado.getVida - 20))}
              else {(atacante.cambiarVida(atacante.getVida - 10 ), atacado)}

        case (Explotar, atacante: Monstruo, atacado: Namekusein) => (atacante.cambiarVida(0), atacado.cambiarVida(math.max(atacado.getVida - atacante.getVida * 2, 1)))
        case (Explotar, atacante: Monstruo, atacado) => (atacante.cambiarVida(0), atacado.cambiarVida(atacado.getVida - atacante.getVida * 2))
        case (Explotar, atacante: Androide, atacado: Namekusein) => (atacante.cambiarVida(0), atacado.cambiarVida(math.max(atacado.getVida - atacante.getVida * 3, 1)))
        case (Explotar, atacante: Androide, atacado) => (atacante.cambiarVida(0), atacado.cambiarVida(atacado.getVida - atacante.getVida * 3))

        case (Onda(energia), atacante, _) => if (atacante.getVida() < energia) {
          (atacante, atacado)
        } else {
          (ataque, atacante, atacado) match {
            case (Onda(energia), atacante, atacado: Monstruo) => {(atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida - energia / 2))}
            case (Onda(energia), atacante, atacado: Androide) => {(atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida + energia * 2))}
            case (Onda(energia), atacante, atacado) => {(atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida - energia * 2))}
          }
        }

        case (Genkidama, atacante, atacado: Androide) => {(atacante, atacado.cambiarVida(atacado.getVida + 10^atacado.estado.roundsFajado))}
        case (Genkidama, atacante, atacado) => {(atacante, atacado.cambiarVida(atacado.getVida - scala.math.pow(10, atacado.estado.roundsFajado).toInt))}

        case (_, _, _) => throw new NoPuedeHacerEseAtaqueException
      }
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




