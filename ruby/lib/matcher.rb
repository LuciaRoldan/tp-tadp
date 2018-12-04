class Matcher

  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
    @bindings = Hash.new
  end

  def call(objeto_a_evaluarse)
    bloque.call(objeto_a_evaluarse)
  end



end