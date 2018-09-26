class Patron

  attr_accessor :matchers, :bloque
  def initialize(matchers, bloque)
    @matchers = matchers
    @bloque = bloque
  end

  def ejecutar_bloque(objeto_a_evaluarse)
    puts('Lo que devuelve el bloque es: ', bloque.call(objeto_a_evaluarse))
    bloque.call(objeto_a_evaluarse)
  end

  def evaluar_matchers(cosa)
    @matchers.all? do |matcher|
      if(matcher.is_a? Symbol)
        Evaluator.define_method(matcher) {cosa}
      end
    else
    matcher.call(cosa)
    end
  end

end