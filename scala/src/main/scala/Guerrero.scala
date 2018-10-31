import Hola.Movimiento

object Guerrero{
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]

  abstract class Guerrero {

    var estado: Estado
    var ki: Int
    var nombre: String
    var inventario: List[Item]

    def pelearContra(oponente: Guerrero)(plan: PlanDeAtaque): Unit ={
      plan.foldLeft((this, oponente)) {
        case((atacante, atacado), movimiento) =>
          atacante.pelearRound(movimiento, atacado)
      }
    }

    def pelearRound(movimiento: Movimiento, oponente: Guerrero):Contrincantes ={
      var(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      oponenteModificado.contraatacar(yo)
    }

    def hacerMovimiento(movimiento: Movimiento, contrincantes: Contrincantes): Contrincantes ={
      (movimiento, contrincantes._1.estado) match{
        case(comerSemilla,_) => movimiento(contrincantes)
        case(_,Muerto) => (contrincantes)
        case(_,Inconsciente) => (contrincantes)
        case(_,_) => movimiento(contrincantes)
      }
    }

  }



  case class Androide(estado: Estado, ki: Int, nombre: String, inventario: List[Item], bateria: Int) extends Guerrero

  case class Sayajin(estado: Estado, ki: Int, nombre: String, inventario: List[Item], nivelSS: Int) extends Guerrero

  case class Humano(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero

  case class Fusionado (estado: Estado, ki: Int, nombre: String, inventario: List[Item], guerreroOriginal: Guerrero) extends Guerrero

  case class Namekusein (estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero

  case class Monstruo (ki: Int, nombre: String, inventario: List[Item]) extends Guerrero


}

