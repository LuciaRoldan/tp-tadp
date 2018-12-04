class ListMatcher < ProcMatcher

    attr_accessor :bloque, :bindings, :lista_simbolos

    def initialize(lista_simbolos, &bloque)
      @lista_simbolos = lista_simbolos
      @bloque = bloque
    end

    def bindear(objeto_a_evaluar)

      bindeado = Hash.new
      tuplas = lista_simbolos.zip(objeto_a_evaluar)

      tuplas.each do |a, b|
        if a.is_a?(Symbol)
          puts(a.bindear(b))
          bindeado.merge(a.bindear(b))
        end
      end
      puts('gg')
      puts(bindeado)
      bindeado

    end

      #tuplas = lista_simbolos.zip(objeto_a_evaluar)
      #tuplas.each do |a, b|
      #  if a.is_a?(Symbol)
      #    bindings[a] = b
      #    else a.bindear()
      #  end
      #end
    #end

end