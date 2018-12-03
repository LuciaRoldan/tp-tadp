class ProcMatcher

  attr_accessor :bloque, :bindings, :lista

  def initialize(&bloque)
    @bloque = bloque
    @lista = []
    @bindings = Hash.new
  end

  #def agregar_bindings(bindings)
  #  @bindings = @bindings.merge(bindings)
  #end

  def agregar_bindings_al_contexto(contexto)
    @bindings.each do |key, value|
      contexto.define_singleton_method(key) {value}
    end
  end


  def agregar_bindings_de_listas(lista_simbolos, lista_valores)
    tuplas = lista_simbolos.zip(lista_valores)
    tuplas.each do |a, b|
          if(a.is_a?(Symbol))
              bindings[a] = b
          end
    end
  end

  #def bind(objeto_a_evaluarse)
  #  if(@lista != [])
  #    tuplas = @lista.zip(objeto_a_evaluarse)
  #    hashes = Hash[tuplas.select{ |tupla| tupla[0].is_a?(Symbol) }]
  #    agregar_bindings(Hash[hashes])
  #  end
  #end

  def call(objeto_a_evaluarse)
    @bloque.call(objeto_a_evaluarse)
  end

  def and (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && @bloque.call(callArgument)
    end
  end

  def or (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.any? {|matcher| matcher.call(callArgument)} || @bloque.call(callArgument)
    end
  end

  def not
    return ProcMatcher.new do
    |callArgument| !@bloque.call(callArgument)
    end
  end

end