abstract class Guerrero {
  
  var ki: Int
  var nombre: String
  var inventario: List[Item]
}


case class Androide extends Guerrero(nombre: String, inventario: List[Item], bateria: Int)

case class Sayajin extends Guerrero(ki: Int, nombre: String, inventario: List[Item], nivelSS: Int)

case class Humano extends Guerrero(ki: Int, nombre: String, inventario: List[Item])

case class Fusionado extends Guerrero(ki: Int, nombre: String, inventario: List[Item], guerreroOriginal: Guerrero)

case class Namekusein extends Guerrero(ki: Int, nombre: String, inventario: List[Item])

case class Monstruo extends Guerrero(ki: Int, nombre: String, inventario: List[Item])

