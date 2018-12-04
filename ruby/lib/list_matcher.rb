class ListMatcher < ProcMatcher

    attr_accessor :lista_simbolos, :match_size

    def initialize(lista_simbolos, match_size)
      @lista_simbolos = lista_simbolos
      @match_size = match_size
    end

    def call(otraLista)
      tuplas = lista_simbolos.zip(otraLista)
      tuplas.all? do |a, b|
        (a == b ||
            a.is_a?(Symbol) ||
            (a.is_a?(ProcMatcher)? a.call(b): false ))
      end &&
          otraLista.is_a?(Array) &&
          ((match_size)? (otraLista.length == lista_simbolos.length) : true )
    end

    def is_bindeable
      true
    end

    def bindear(objeto_a_evaluar)

      bindeado = Hash.new
      tuplas = lista_simbolos.zip(objeto_a_evaluar)

      tuplas.each do |a, b|
        if a.respond_to?('is_bindeable')
          puts('en list_matcher bindeando', a, b)
          puts(' ')
          bindeado.merge!(a.bindear(b))
        end
      end

      bindeado
    end

end