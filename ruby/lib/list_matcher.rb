class ListMatcher < ProcMatcher

    attr_accessor :bloque, :bindings, :lista_simbolos

    def initialize(lista_simbolos, &bloque)
      @lista_simbolos = lista_simbolos
      @bloque = bloque
    end

    def bindear(objeto_a_evaluar)

      puts('Empezando a bindear')

      bindeado = Hash.new
      tuplas = lista_simbolos.zip(objeto_a_evaluar)

      puts('tuplas: ', tuplas)

      tuplas.each do |a, b|
        if a.is_a?(Symbol)

          puts('bindeando a: ', a, ' con ', b, a.bindear(b))

          bindeado.merge!(a.bindear(b))
        end
      end

      puts('bindeado al final: ', bindeado)

      bindeado

    end

end