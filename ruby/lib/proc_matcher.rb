class ProcMatcher

  attr_accessor :bloque, :bindings, :lista

  def initialize(&bloque)
    @bloque = bloque
    @lista = []
    @bindings = Hash.new
  end

  def agregar_bindings(bindings)
    @bindings = @bindings.merge(bindings)
  end

  def bind(objeto_a_evaluarse)
    if(@lista != [])
      tuplas = @lista.zip(objeto_a_evaluarse)
      hashes = Hash[tuplas.select{ |tupla| tupla[0].is_a?(Symbol) }]
      agregar_bindings(Hash[hashes])
    end
  end

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