class ProcMatcher

  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
  end

  def call(objeto_a_evaluarse)
    bloque.call(objeto_a_evaluarse)
  end



  def and (*matchers)
    return AndCombinator.new(([self]).concat(matchers)) do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && @bloque.call(callArgument)
    end
  end

  def or (*matchers)
    return OrCombinator.new(matchers.concat([self])) do
    |callArgument| matchers.any? {|matcher| matcher.call(callArgument)} || @bloque.call(callArgument)
    end
  end

  def not
    return NotCombinator.new(self) do
    |callArgument| !@bloque.call(callArgument)
    end
  end


end


class Combinator < ProcMatcher

  attr_accessor :bloque

  def initialize(&bloque)
    @bloque = bloque
  end

  def is_bindeable
    true
  end

end

class AndCombinator < Combinator

  attr_accessor :matchers

  def initialize(matchers, &bloque)
    super()
    @matchers = matchers
  end

  def bindear(objeto_a_evaluar)
    matchers.reduce(Hash.new) do |hash, matcher|
      puts('bindeando ', matcher)
      puts('me da ', matcher.bindear(objeto_a_evaluar))

      hash.merge!(matcher.bindear(objeto_a_evaluar))
    end
  end

end

class OrCombinator < Combinator

  attr_accessor :matchers

  def initialize(matchers, &bloque)
    super()
    @matchers = matchers
  end

end

class NotCombinator < Combinator

  attr_accessor :matcher

  def initialize(matcher, &bloque)
    super()
    @matcher = matcher
  end

end