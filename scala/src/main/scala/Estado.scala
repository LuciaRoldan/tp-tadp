package DragonBall

abstract case class Estado(roundsFajado: Int) {
 def dejarseFajar: Estado
}

case class Normal(override val roundsFajado: Int) extends Estado(roundsFajado: Int) {
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = roundsFajado + 1);
  }
}


case class Muerto(override val roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = 0)
  }
}
case class Inconsciente(override val roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar(): Estado ={
    this.copy(roundsFajado = 0)
  }
}