class Patron

  attr_accessor :matchers, :bloque
  def initialize(matchers, &bloque)
        matchers.each do |matcher| if matcher.is_a?(Symbol)
          matcher = ProcMatcher.new do |objeto_a_evaluarse|
            pm.agregar_bindings_de_listas(matcher, objeto_a_evaluarse)
            true
            end
                                   end
        end
  @matchers = matchers
  @bloque = bloque
  end

  def ejecutar_bloque()
    agregar_bindings()
    bloque.instance_eval( &bloque )
  end

  def agregar_bindings()
    matchers.each do |matcher|

      matcher.agregar_bindings_al_contexto(@bloque)


      #if(matcher.is_a?(Symbol))
      #  @bloque.define_singleton_method(matcher) {objeto_a_evaluarse}
      #else
      #  puts('Bindeando una cosa que no es un Symbol')
      #  matcher.bind(objeto_a_evaluarse)
      #  matcher.bindings.each do |key, value|
      #    @bloque.define_singleton_method(key) {value}
      #  end
      end
  end

  def matchea(objeto_a_evaluarse)
    @matchers.all? do |matcher|
      matcher.call(objeto_a_evaluarse)
    end
  end

end