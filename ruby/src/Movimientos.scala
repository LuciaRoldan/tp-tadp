sealed trait Movimiento

case object comerSemilla extends Movimiento
case object dejarseFajar extends Movimiento
case object cargarKi extends Movimiento
case object usarItem(item: Item, enemigo: Guerrero) extends Movimiento
case object comerOponente(enemigo: Guerrero) extends Movimiento
case object convertirseEnMono extends Movimiento
case object convertirseEnSS extends Movimiento
case object fusion(x: Fusionable) extends Movimiento
case object hacerMagia(f: Guerrero, Guerrero => Guerero) extends Movimiento
case object atacar(ataque: Ataque) extends Movimiento