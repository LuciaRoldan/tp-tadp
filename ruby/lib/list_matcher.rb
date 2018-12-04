class ListMatcher < ProcMatcher

    attr_accessor :bloque, :lista_simbolos

    def initialize(lista_simbolos, &bloque)
      @lista_simbolos = lista_simbolos
      @bloque = bloque
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