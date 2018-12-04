class Combinator < ProcMatcher
  #Porque un combinator entiende los mensajes and, or, not y call
  attr_accessor :matchers

  def initialize(matchers)
    @matchers = matchers.map do
    |matcher| if matcher.is_a?(Symbol)
                SymbolMatcher.new(matcher)
              else matcher
              end
    end
  end

  def get_bindings(objeto_a_evaluarse)
    @matchers.reduce(Hash.new) do |hash, matcher|
      hash.merge(matcher.get_bindings(objeto_a_evaluarse))
    end
  end

end

class AndCombinator < Combinator
  def call(objeto)
    @matchers.all? {|matcher| matcher.call(objeto)}
  end
end

class OrCombinator < Combinator
  def call(objeto)
    @matchers.any? {|matcher| matcher.call(objeto)}
  end
end

class NotCombinator < Combinator

  attr_accessor :matcher

  def initialize(matcher)
    @matcher = matcher
  end

  def call(objeto)
    !@matcher.call(objeto)
  end
end