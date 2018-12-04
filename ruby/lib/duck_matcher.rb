class DuckMatcher < ProcMatcher

  attr_accessor :mensajes

  def initialize(mensajes)
    @mensajes = mensajes
  end

  def call(objeto)
    mensajes.all? { |mensaje| objeto.respond_to?(mensaje) }
  end

  def bindear(objeto_a_evaluar)
    Hash.new
  end

end