class TypeMatcher < ProcMatcher
  attr_accessor :clase

  def initialize(clase)
    @clase = clase
  end

  def call(objeto)
    objeto.is_a?(clase)
  end

  def bindear(objeto_a_evaluar)
    Hash.new
  end

end