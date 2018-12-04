class DuckMatcher < ProcMatcher

  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
    @bindings = Hash.new
  end

  def bindear(objeto_a_evaluar)
    Hash.new
  end

end