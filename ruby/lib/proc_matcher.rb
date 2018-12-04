class ProcMatcher

  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
    @bindings = Hash.new
  end


  #def agregar_bindings_de_listas(lista_simbolos, lista_valores)
  #  tuplas = lista_simbolos.zip(lista_valores)
  #  tuplas.each do |a, b|
  #        if a.is_a?(Symbol)
  #            bindings[a] = b
  #        end
  #  end
  #end

  #def agregar_binding(simbolo, valor)
  #  bindings[simbolo] = valor
  #end

  def call(objeto_a_evaluarse)
    bloque.call(objeto_a_evaluarse)
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