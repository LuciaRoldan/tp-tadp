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

      #tuplas.each do |a, b|
      #  if a.is_a?(Symbol)
      #    bindeado.merge!(a.bindear(b))
      #  end
      #end

      tuplas.each do |a, b|
        if a.respond_to?('is_bindeable')
          bindeado.merge!(a.bindear(b))
        end
      end



      bindeado

    end

end