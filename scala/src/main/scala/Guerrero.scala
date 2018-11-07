package DragonBall


import Movimiento._


   abstract class Guerrero ( val estado: Estado, val nombre: String, val inventario: List[Item]){
     def perderMunicion():Guerrero = ???

     def tieneMunicion: Boolean = ???

     type Contrincantes = (Guerrero, Guerrero)
     type PlanDeAtaque = List[Movimiento]

     def cambiarEstado(nuevoEstado: Estado):Guerrero = this.copear(nuevoEstado)

     def tieneItem(item: Item): Boolean = inventario.contains(item)

    def contraatacar(enemigo: Guerrero): Contrincantes = (enemigo, this)

     def copear(nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero


     /*def copy(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero ={
       new Guerrero(ki= nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
     }*/

    def pelearContra(oponente: Guerrero)(plan: PlanDeAtaque): Unit ={
      plan.foldLeft((this, oponente)) {
        case((atacante, atacado), movimiento) =>
          atacante.pelearRound(movimiento, atacado)
      }
    }

    def pelearRound(movimiento: Movimiento, oponente: Guerrero):Contrincantes ={
      val(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      oponenteModificado.contraatacar(yo)
    }

    def hacerMovimiento(movimiento: Movimiento, contrincantes: Contrincantes): Contrincantes ={
      (movimiento, contrincantes._1.estado) match{
        case(comerSemilla,_) => movimiento(contrincantes)
        case(_,Muerto) => contrincantes
        case(_,Inconsciente) => contrincantes
        case(_,_) => movimiento(contrincantes)
      }
    }

  }

  abstract class Biologico(val ki:Int, estado: Estado, nombre: String, inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item]){

    def cambiarKi(cantidad: Int):Biologico //new Biologico(ki = cantidad, estado= estado, nombre= nombre, inventario= inventario)
  }

  case class Androide(override val estado: Estado, override val nombre: String, override val inventario: List[Item], bateria: Int) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item]) {
    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Androide =
      new Androide(estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario, bateria = bateria)
  }

  case class Sayajin(override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int, tieneCola: Boolean) extends Biologico(ki: Int, estado :Estado, nombre: String, inventario: List[Item]){

    def perderCola :Sayajin = {this.copy(tieneCola = false)}

    override def cambiarKi(cantidad: Int): Sayajin ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Sayajin =
      new Sayajin(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, nivelSS = nivelSS, tieneCola = tieneCola)
  }

  case class Humano(override val estado: Estado,  override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int): Humano ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero = ???
  }

  case class Fusionado (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int, guerreroOriginal: Guerrero) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
   override def cambiarKi(cantidad: Int): Fusionado ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero = ???
  }

  case class Namekusein (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int): Namekusein ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero = ???
  }

  case class Monstruo (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int)  ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero = ???
  }

  case class Mono (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], sayayin: Sayajin) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){

    def getSayajin :Sayajin = sayayin

    override def cambiarKi(cantidad: Int) ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero = ???
  }




