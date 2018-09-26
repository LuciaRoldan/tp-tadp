class Patron

  attr_accessor :matchers, :bloque
  def initialize(matchers, bloque)
    @matchers = matchers
    @bloque = Bloque.new(&bloque)
  end

  def ejecutar_bloque(objeto_a_evaluarse)
    puts('Lo que devuelve el bloque es: ', bloque.instance_eval(&bloque.bloque))
    bloque.instance_eval(&bloque.bloque)
  end

  def evaluar_matchers(cosa)
    @matchers.all? do |matcher|

      if(matcher.is_a?(Symbol))
        @bloque.define_singleton_method(matcher) {cosa}
        puts(bloque.respond_to?(matcher))
        true
      else
        matcher.call(cosa)
      end
      end
  end
end