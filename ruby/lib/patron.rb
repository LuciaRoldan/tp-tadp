class Patron

  attr_accessor :matchers, :bloque
  def initialize(matchers, bloque)
    @matchers = matchers
    @bloque = Bloque.new(&bloque)
  end

  def ejecutar_bloque(objeto_a_evaluarse)
    matchers.each do |matcher|
      if(matcher.is_a?(Symbol))
        @bloque.define_singleton_method(matcher) {objeto_a_evaluarse}
      else
        matcher.bindings.each do |key, value|
          @bloque.define_singleton_method(key) {value}
        end
      end
    end
    puts('Lo que devuelve el bloque es: ', bloque.instance_eval(&bloque.bloque))
    bloque.instance_eval(&bloque.bloque)
  end

  def evaluar_matchers(cosa)
    @matchers.all? do |matcher|
      (matcher.is_a?(Symbol))? true : matcher.instance_exec(cosa, &matcher.bloque)
    end
  end

end