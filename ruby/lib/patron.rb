class Patron

  attr_accessor :matchers, :bloque
  def initialize(matchers, &bloque)
    @matchers = matchers
    @bloque = bloque
  end

  def ejecutar_bloque(objeto_a_evaluarse)
    #puts('Lo que devuelve el bloque es: ', bloque.instance_eval(&bloque.bloque))
    bloque.instance_eval( &bloque )
  end

  def agregar_bindings(objeto_a_evaluarse)
    matchers.each do |matcher|
      if(matcher.is_a?(Symbol))
        @bloque.define_singleton_method(matcher) {objeto_a_evaluarse}
      else
        puts('Bindeando una cosa que no es un Symbol')
        matcher.bind(objeto_a_evaluarse)
        matcher.bindings.each do |key, value|
          @bloque.define_singleton_method(key) {value}
        end
      end
    end
  end

  def matchea(objeto_a_evaluarse)
    @matchers.all? do |matcher|
      (matcher.is_a?(Symbol))? true : matcher.instance_exec(objeto_a_evaluarse, &matcher.bloque)
    end
  end

end