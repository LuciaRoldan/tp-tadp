package DragonBall


import Movimiento._


abstract class Guerrero(val estado: Estado, val nombre: String, val inventario: List[Item], val listaDeMovimientos: PlanDeAtaque) {

     def perderMunicion():Guerrero = ???

     def tieneMunicion: Boolean = ???

     type Contrincantes = (Guerrero, Guerrero)
     type PlanDeAtaque = List[Movimiento]

     def cambiarEstado(nuevoEstado: Estado):Guerrero = this.copear(nuevoEstado)

     def tieneItem(item: Item): Boolean = inventario.contains(item)

    def contraatacar(enemigo: Guerrero): Contrincantes = {
      val movimiento: Movimiento = this.movimientoMasEfectivoContra(enemigo)(diferenciaKiAtacante)
      val (yoModificado, enemigoModificado) = this.hacerMovimiento(movimiento, (this,enemigo))
      (enemigoModificado, yoModificado)
    }

     def copear(nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero


     /*def copy(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero ={
       new Guerrero(ki= nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
     }*/

    def pelearContra(oponente: Guerrero)(plan: PlanDeAtaque) ={
      plan.foldLeft((this, oponente)) {
        case((atacante, atacado), movimiento) if (atacante.estado == Muerto || atacado.estado == Muerto) => (atacante, atacado)
        case((atacante, atacado), movimiento) => atacante.pelearRound(movimiento, atacado)
      }
    }

    def pelearRound(movimiento: Movimiento, oponente: Guerrero):Contrincantes ={
      val(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      oponenteModificado.contraatacar(yo)
    }

    def hacerMovimiento(movimiento: Movimiento, contrincantes: Contrincantes): Contrincantes ={
      (movimiento, contrincantes._1.estado) match{
        case(ComerSemilla,_) => movimiento(contrincantes)
        case(_,Muerto) => contrincantes
        case(_,Inconsciente) => contrincantes
        case(_,_) => movimiento(contrincantes)
      }
    }

     def movimientoMasEfectivoContra(atacado: Guerrero) (criterio: Criterio) : Movimiento = {
       this.listaDeMovimientos.maxBy( movimiento => criterio((this, atacado), hacerMovimiento(movimiento, (this,atacado))))
     }

     def planDeAtaqueContra(atacado :Guerrero, cantidadDeRounds :Int) (criterio: Criterio): PlanDeAtaque ={
       List.fill(cantidadDeRounds)(0).foldLeft((List(): List[Movimiento],(this, atacado))){
         case ((lista, contrincantes), _) =>
           (
             lista :+ movimientoMasEfectivoContra(atacado)(criterio),
             hacerMovimiento(this.movimientoMasEfectivoContra(atacado)(criterio), contrincantes)
           )
         // en cada iteracion se suma el mejor movimiento a la lista vacia inicial de movimientos
         // y se calcula el estado de los guerreros despues de aplicar el mejor movimiento
       }._1 // retorna el primer elemento de la tupla (listaDeMejoresMovimientos, contrincantes)

     }

  }


  abstract class Biologico(val ki: Int, val kiMaximo: Int, estado: Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque) {

    def cambiarKi(cantidad: Int): Biologico
  }

  case class Androide(override val estado: Estado, override val nombre: String, override val inventario: List[Item], bateria: Int , override val listaDeMovimientos: PlanDeAtaque) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){
    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Androide =
      new Androide(estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario, bateria = bateria, listaDeMovimientos = listaDeMovimientos)

  }

  case class Sayajin(override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int, tieneCola: Boolean, override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki: Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){

    def perderCola :Sayajin = {this.copy(tieneCola = false)}

    override def cambiarKi(cantidad: Int): Sayajin ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Sayajin =
      new Sayajin(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, nivelSS = nivelSS, tieneCola = tieneCola, listaDeMovimientos = listaDeMovimientos)

    def convertirseEnMono(): Guerrero ={
      if(this.pudeConvertirseEnMono){
        new Mono(estado = this.estado, ki = this.kiMaximo*3, kiMaximo = this.kiMaximo*3, nombre = this.nombre, inventario = this.inventario, this, listaDeMovimientos = this.listaDeMovimientos)
      } else {this}
    }

    def pudeConvertirseEnMono() ={
      inventario.contains(FotoDeLaLuna) && this.tieneCola && this.nivelSS <= 1
    }

    def aumentarNivelSS(): Sayajin = {
      if(this.puedeAumentarNivelSS()){
        this.copy(nivelSS = nivelSS + 1, kiMaximo = kiMaximo * (nivelSS + 1))
      } else {this}
    }

    def puedeAumentarNivelSS(): Boolean ={
      ki >= kiMaximo/2
    }
  }

  case class Humano(override val estado: Estado,  override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){
    override def cambiarKi(cantidad: Int): Humano ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero =
      new Humano(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos)
  }

  case class Fusionado (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], val guerreroOriginal: Guerrero, override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){
   override def cambiarKi(cantidad: Int): Fusionado ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero =
      new Fusionado(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, guerreroOriginal = guerreroOriginal, listaDeMovimientos = listaDeMovimientos)

  }

  case class Namekusein (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){
    override def cambiarKi(cantidad: Int): Namekusein ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero =
      new Namekusein(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos)

  }

  case class Monstruo (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item],  override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){
    override def cambiarKi(cantidad: Int)  ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero =
      new Monstruo(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos)

  }

  case class Mono (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], val sayajin: Sayajin, override val listaDeMovimientos: PlanDeAtaque) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque){

    def getSayajin :Sayajin = sayajin

    override def cambiarKi(cantidad: Int) ={
      this.copy(ki = cantidad)
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item]): Guerrero =
      new Mono(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, sayajin = sayajin, listaDeMovimientos = listaDeMovimientos)

  }




