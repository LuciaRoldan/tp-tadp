class ProcMatcher

  attr_accessor :combinator
  def initialize()
    @combinator
  end

  def call(objeto_a_evaluarse) end

  def and (*matchers)
    combinator = AndCombinator.new(matchers.concat([self]))
  end

  def or (*matchers)
    combinator = OrCombinator.new(matchers.concat([self]))
  end

  def not
    combinator = NotCombinator.new(self)
  end

  def get_bindings(objeto_a_evaluarse)
    combinator.get_bindings(objeto_a_evaluarse)
  end

end

class ValMatcher < ProcMatcher
  attr_accessor :valor

  def initialize(valor)
    @valor = valor
    super()
  end


  def call(objeto)
    objeto == @valor
  end
end

class TypeMatcher < ProcMatcher
  attr_accessor :clase

  def initialize(clase)
    @clase = clase
    super()
  end

  def call(objeto)
    objeto.is_a?(@clase)
  end
end

class DuckMatcher < ProcMatcher
  attr_accessor :mensajes

  def initialize(mensajes)
    @mensajes = mensajes
    super()
  end

  def call(objeto)
    @mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  }
  end

end

class SymbolMatcher < ProcMatcher

  attr_accessor :bindings

  def initialize(simbolo)
    @simbolo = simbolo
    super()
  end

  def call(objeto)
    true
  end

  def get_bindings(objeto_a_evaluarse)
    binding_del_symbol = {@simbolo => objeto_a_evaluarse}
    super.merge(binding_del_symbol)
  end

end

class ListMatcher < ProcMatcher
  attr_accessor :lista, :match_size, :bindings

  def initialize(lista, match_size)
    @match_size = match_size
    @lista = lista
    super()
  end

  def call(otraLista)
    tuplas = @lista.zip(otraLista)
    tuplas.all? do
                  |a, b| (a == b || a.is_a?(Symbol) || (a.is_a?(ProcMatcher)? a.call(b): false ))
    end &&
        otraLista.is_a?(Array) &&
        ((@match_size)? (otraLista.length == lista.length) : true )
  end

  def get_bindings(otraLista)

    bindings_del_list = @lista.zip(otraLista) # Genero una lista de tuplas [(1,1), (:a,1), (:b,2)]
    .select { |a, _| a.is_a?(Symbol) } # Dejo solo las tuplas que tengan un simbolo como primer elemento [(:a,1), (:b,2)]
    .reduce(Hash.new) { |hash, (a,b)| hash.merge({a => b})} # Genero un hash con las tuplas filtradas {a => 1, b => 2}

    bindings_de_los_combinators = super

    bindings_de_los_combinators.merge(bindings_del_list)
  end
end