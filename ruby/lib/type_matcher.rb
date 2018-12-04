class TypeMatcher < ProcMatcher
  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
    @bindings = Hash.new
  end

  def bindear(objeto_a_evaluar)
  end

end