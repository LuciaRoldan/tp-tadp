import DragonBall.Movimiento.cargarKi
import DragonBall.{Normal, Sayajin}
import org.scalatest.FunSuite

/*object CubeCalculator extends App {
  def cube(x: Int) = {
    x * x * x
  }
}*/

class CubeCalculatorTest extends FunSuite {
  test("CubeCalculator.cube")
    val goku = Sayajin(estado = Normal, ki = 100, nombre = "GOKU", inventario = List(), nivelSS = 1)

    val vegeta = Sayajin(estado = Normal, ki = 100, nombre = "VEGETA", inventario = List(), nivelSS = 1)


    val gokuKiCargado = cargarKi(goku, vegeta)._1

    assert(gokuKiCargado.asInstanceOf[Sayajin].ki === 250)
  }
}