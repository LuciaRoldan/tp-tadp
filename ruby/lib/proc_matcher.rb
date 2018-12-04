class ProcMatcher

  def initialize
  end

  def call(objeto_a_evaluarse)
    bloque.call(objeto_a_evaluarse)
  end



  def and (*matchers)
    return AndCombinator.new(([self]).concat(matchers))
  end

  def or (*matchers)
    return OrCombinator.new(matchers.concat([self]))
  end

  def not
    return NotCombinator.new(self)
  end


end


class Combinator < ProcMatcher

  def is_bindeable
    true
  end

end

class AndCombinator < Combinator

  attr_accessor :matchers

  def initialize(matchers)
    @matchers = matchers
  end

  def bindear(objeto_a_evaluar)
    matchers.reduce(Hash.new) do |hash, matcher|
      puts('bindeando ', matcher)
      puts('me da ', matcher.bindear(objeto_a_evaluar))

      hash.merge!(matcher.bindear(objeto_a_evaluar))
    end
  end

  def call(callArgument)
    matchers.all? {|matcher| matcher.call(callArgument)}
  end

end

class OrCombinator < Combinator

  attr_accessor :matchers

  def initialize(matchers)
    @matchers = matchers
  end

  def bindear(objeto_a_evaluar)
    matchers.reduce(Hash.new) do |hash, matcher|
      puts('bindeando ', matcher)
      puts('me da ', matcher.bindear(objeto_a_evaluar))

      hash.merge!(matcher.bindear(objeto_a_evaluar))
    end
  end

  def call(callArgument)
    matchers.any? {|matcher| matcher.call(callArgument)}
  end

end

class NotCombinator < Combinator

  attr_accessor :matcher

  def initialize(matcher)
    @matcher = matcher
  end

  def bindear(objeto_a_evaluar)
      hash = Hash.new
      hash.merge!(matcher.bindear(objeto_a_evaluar))
  end

  def call(callArgument)
    !@matcher.call(callArgument)
  end

end