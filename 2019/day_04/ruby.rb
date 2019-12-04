from=124075
to=580769

total = 0

(from...to).each do |num|
  next unless num.to_s.match(/(\d)\1+.+$/)
  chars = num.to_s.chars
  sorted = chars.sort
  next unless chars == sorted

  total += 1
end
puts total

total = 0

(from...to).each do |num|
  next unless num.to_s.match(/(\d)\1*$/)
  chars = num.to_s.chars
  sorted = chars.sort
  next unless chars == sorted

  hh = chars.uniq.map { |x| [x, chars.count(x)] }.to_h
  next unless hh.values.include?(2)

  total += 1
end

puts total
