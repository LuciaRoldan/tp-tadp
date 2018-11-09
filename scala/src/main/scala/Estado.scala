package DragonBall

abstract class Estado(roundsFajado: Int) {
 def dejarseFajar: Estado
}

case class Normal(roundsFajado: Int) extends Estado(roundsFajado: Int) {
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = roundsFajado + 1);
  }
}


case class Muerto(roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = 0)
  }
}
case class Inconsciente(roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = 0)
  }
}