import org.scalatest.{FreeSpec, Matchers}

import Estado._
import main.scala.Guerrero._
import Item._
import Movimientos._



class DragonBallSpec extends FlatSpec with Matchers {
  "Un pokemon" should "realizar actividad descansar y recuperar su energia" in {
    val pokemon = Pokemon(Especies.charmander, energia = 20, energiaMaxima = 100)

    //    pokemon.descansar
    //    descansar(pokemon)
    //    realizarActividad(descansar, pokemon)

    val pokemonDescansado = pokemon.realizarActividad(descansar)

    pokemonDescansado.energia should be(pokemon.energiaMaxima)
  }

}