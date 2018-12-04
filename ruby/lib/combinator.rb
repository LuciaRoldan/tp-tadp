class Combinator < ProcMatcher

  attr_accessor :matchers

  def initialize(matchers)
    @matchers = matchers.map do
    |matcher| if matcher.is_a?(Symbol)
                SymbolMatcher.new(matcher)
              else matcher
              end
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