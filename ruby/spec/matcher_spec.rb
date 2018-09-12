describe 'call' do
  let (:matcher) { Matcher.new() }

  it 'siempre matchea el tipo' do
    expect(:A.call('hola')).to eq(true)
  end

  it 'siempre matchea el tipo' do
    :A.call('hola')
    expect('hola').to eq(:A)
  end

end

describe 'val' do

  let (:matcher) { Matcher.new() }

  it '5 es igual a 5' do
    expect(matcher.val(5).call(5)).to eq(true)
  end

  it '5 es distinto al caracter 5' do
    expect(matcher.val(5).call('5')).to eq(false)
  end

  it '5 es distinto a 4' do
    expect(matcher.val(5).call(4)).to eq(false)
  end

end

describe 'type' do

  let (:matcher) { Matcher.new() }

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

  let (:matcher) { Matcher.new() }

  let (:an_array)  {[1, 2, 3, 4]}

  it 'Una lista es igual a si misma' do
    expect(matcher.list([1, 2, 3, 4], true).call(an_array)).to eq(true)
  end

  it 'Una lista es igual a si misma' do
    expect(matcher.list([1, 2, 3, 4], false).call(an_array)).to eq(true)
  end

  it 'Los primeros elementos de una lista son iguales a los primeros elementos de si misma' do
    expect(matcher.list([1, 2, 3], true).call(an_array)).to eq(false)
  end

  it 'Los primeros elementos de una lista es distinta a si misma' do
    expect(matcher.list([1, 2, 3], false).call(an_array)).to eq(true)
  end

  it 'La lista desordenada es distinta a la lista' do
    expect(matcher.list([2, 1, 3, 4], true).call(an_array)).to eq(false)
  end

  it 'La lista desordenada es distinta a la lista' do
    expect(matcher.list([2, 1, 3, 4], false).call(an_array)).to eq(false)
  end

  it 'Si no se especifica march_size se considera true' do
    expect(matcher.list([1, 2, 3]).call(an_array)).to eq(false)
  end

end

describe 'duck' do

  let (:matcher) { Matcher.new() }

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

  let (:matcher) { Matcher.new() }

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).and(matcher.type(Fixnum),matcher.val(5)).call(5)).to eq(true)
  end

  it '5 es igual que string 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).and(matcher.type(Fixnum),matcher.val('5')).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:hola).and(matcher.type(Fixnum),matcher.val(5)).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Fixnum).and(matcher.duck(:+),matcher.val(5)).call(5)).to eq(true)
  end
end

describe 'or' do

  let (:matcher) { Matcher.new() }

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.duck(:+).or(matcher.type(Array),matcher.val(7)).call(5)).to eq(true)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Array).or(matcher.duck(:+),matcher.val(7)).call(5)).to eq(true)
  end
end

describe 'not' do

  let (:matcher) { Matcher.new() }

  it '5 es un fixnum' do
    expect(matcher.type(Fixnum).not.call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(matcher.type(Fixnum).and(matcher.duck(:+),matcher.val(5)).not.call(5)).to eq(false)
  end
end

describe 'match' do

  let (:matcher) { Matcher.new() }

  it '2 no es string' do
    expect(matches?(2)do

      with(matcher.type(String)){'hola'}
      with(:unaCosa) {'hola'}
    end).to eq('hola')
  end

  it '2 no es un numero' do
    expect(matches?(2)do
      with(matcher.type(Number), duck(:+)){'hola'}
    end).to eq('hola')
  end
end

