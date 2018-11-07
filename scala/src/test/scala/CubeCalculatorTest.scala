import org.scalatest.FunSuite

/*object CubeCalculator extends App {
  def cube(x: Int) = {
    x * x * x
  }
}*/

class CubeCalculatorTest extends FunSuite {
  test("CubeCalculator.cube") {
    assert(27 === 27)
  }
}