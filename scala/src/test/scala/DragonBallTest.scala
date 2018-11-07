import DragonBall.Movimiento.{cargarKi, usarItem}
import DragonBall.{ArmaFilosa, Normal, Sayajin}
import org.scalatest.FunSuite

class DragonBallTest extends FunSuite {

  val goku = Sayajin(estado = Normal, ki = 100, nombre = "GOKU", inventario = List(ArmaFilosa), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa)))
  val vegeta = Sayajin(estado = Normal, ki = 100, nombre = "VEGETA", inventario = List(), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa)))

  test("Goku carga su ki") {
    val gokuKiCargado = cargarKi(goku, vegeta)._1

    assert(gokuKiCargado == goku.copy(ki = 250))
  }
  test("Goku y Vegeta pelean un round") {
    val nuevoVegeta = goku.pelearRound(new usarItem(ArmaFilosa), vegeta)._2

    assert(!nuevoVegeta.asInstanceOf[Sayajin].tieneCola)
  }
}
