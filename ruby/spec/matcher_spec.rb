

describe 'val' do

  let (:matcher) { Evaluator.new() }

  it '5 es igual a 5' do
    self.expect(matcher.instance_eval{val(5).call(5)}).to eq(true)
  end

  it '5 es igual a 5' do
    self.expect(matcher.val(5).call(5)).to eq(true)
  end

  it '5 es distinto al caracter 5' do
    expect(matcher.val(5).call('5')).to eq(false)
  end

  it '5 es distinto a 4' do
    expect(matcher.val(5).call(4)).to eq(false)
  end

end

describe 'type' do

  let (:matcher) { Evaluator.new() }

  it '5 es de tipo Integer' do
    expect(matcher.type(Integer).call(5)).to eq(true)
  end

  it 'Un string no es un simbolo' do
    expect(matcher.type(Symbol).call("Trust me, I'm a Symbol...")).to eq(false)
  end

  it 'Un simbolo es de tipo Symbol' do
    expect(matcher.type(Symbol).call(:a_real_symbol)).to eq(true)
  end

end

describe 'list' do

  let (:matcher) { Evaluator.new() }

  let (:an_array)  {[1, 2, 3, 4]}

  it 'Una lista es igual a si misma' do
    proc = matcher.list([1, 2, 3, 4], true)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(true)
  end

  it 'Una lista es igual a si misma' do
    proc = matcher.list([1, 2, 3, 4], false)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(true)
  end

  it 'Los primeros elementos de una lista son iguales a los primeros elementos de si misma' do
    proc = matcher.list([1, 2, 3], true)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(false)
  end

  it 'Los primeros elementos de una lista es distinta a si misma' do
    proc = matcher.list([1, 2, 3], false)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(true)
  end

  it 'La lista desordenada es distinta a la lista' do
    proc = matcher.list([2, 1, 3, 4], true)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(false)
  end

  it 'La lista desordenada es distinta a la lista' do
    proc = matcher.list([2, 1, 3, 4], false)
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(false)
  end

  it 'Si no se especifica march_size se considera true' do
    proc = matcher.list([1, 2, 3])
    expect(proc.instance_exec(an_array, &proc.bloque)).to eq(false)
  end

end

describe 'duck' do

  let (:matcher) { Evaluator.new() }

  class Psyduck

    def cuack
      'psy..duck'
    end

    def fly
      '(headache)'
    end

  end

  class Dragon

    def fly
      'do some flying'
    end

  end

  let (:psyduck) { Psyduck.new() }
  let (:dragon) { Dragon.new() }


  it 'Psyduck responde a cuack y fly' do
    expect(matcher.duck(:cuack, :fly).call(psyduck)).to eq(true)
  end

  it 'Psyduck responde a cuack y fly' do
    expect(matcher.duck(:cuack, :fly).call(psyduck)).to eq(true)
  end

  it 'Dragon no responde a cuack y fly' do
    expect(matcher.duck(:cuack, :fly).call(dragon)).to eq(false)
  end

  it 'Dragon responde fly' do
    expect(matcher.duck(:fly).call(dragon)).to eq(true)
  end

  it 'Object responde a to_s' do
    expect(matcher.duck(:to_s).call(Object.new)).to eq(true)
  end

end

describe 'and' do

  let (:matcher) { Evaluator.new() }

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).and(matcher.type(Fixnum), matcher.val(5)).call(5)).to eq(true)
  end

  it '5 es igual que string 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).and(matcher.type(Fixnum), matcher.val('5')).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:hola).and(matcher.type(Fixnum), matcher.val(5)).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Fixnum).and(matcher.duck(:+),matcher.val(5)).call(5)).to eq(true)
  end
end

describe 'or' do

  let (:matcher) { Evaluator.new() }

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).or(matcher.type(Array),matcher.val(7)).call(5)).to eq(true)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Array).or(matcher.duck(:+),matcher.val(7)).call(5)).to eq(true)
  end
end

describe 'not' do

  let (:matcher) { Evaluator.new() }

  it '5 es un fixnum' do
    expect(matcher.type(Fixnum).not.call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Fixnum).and(matcher.duck(:+), matcher.val(5)).not.call(5)).to eq(false)
  end
end

describe 'match' do

  let (:matcher) { Evaluator.new() }

  it '2 es Numeric' do
    expect(matches?(2)do
      with(type(String)){'2 es String'}
      with(type(Numeric)){'hola'}
    end).to eq('hola')
  end

  it '2 es Numeric y sabe sumar' do
    expect(matches?(2)do
      with(type(Numeric), duck(:+)){'hola'}
    end).to eq('hola')
  end

  it 'x entiende hola' do
    x = Object.new
    x.send(:define_singleton_method, :hola) { 'hola' }
    expect(matches?(x) do
      with(duck(:hola)) { 'chau!' }
      with(type(Object)) { 'aca no llego' }
    end).to eq('chau!')
  end

  it 'y entiende hola' do
    y = Object.new
    y.send(:define_singleton_method, :hola) { 'hola' }
    expect(matches?(y) do
      with(duck(:+)) { 'chau!' }
      with(type(String)) { 'aca no llego' }
      otherwise {'hola'}
    end).to eq('hola')
  end
end

describe 'ninguno matchea' do
  it '[lol] no es un Numeric ni un String' do
    expect{matches?(['lol'])do
      with(type(Numeric)) { 'soy Numeric' }
      with(type(String)) {'soy String'}
    end}.to raise_error('Ningun patron matchea. Agregar un otherwise')
  end
end

describe 'binding' do

  it '2 no es string' do
    expect(matches?([1, 2])do
      with(:a_list, list([:a, :b])) { a_list + [a, b] }
    end).to eq([1,2,1,2])
  end

  it 'Bindeo de array' do
    expect(matches?([1,2])do
      with(type(Array), list([:a, :b], false)) { a + b }
    end).to eq(3)
  end

  it 'Bindeo de array y verificaciones' do
    expect(matches?([1, 2, 4, 'hola', Object.new])do
      with(type(Array), list([:a, 2, :b, type(String), duck(:is_a?)], false)) { a * b }
    end).to eq(4)
  end

  it 'Se bindean los elementos de la lista' do
    expect(matches?([1,2])do
      with(list [:a, :b]) { a + b }
    end).to eq(3)
  end

end
