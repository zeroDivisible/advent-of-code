require 'set'

input = File.readlines('input.txt')

# step 1
twos = 0
threes = 0

input.each do |line|
  counts = line.each_char.to_a.inject(Hash.new(0)) { |h,v| h[v] += 1; h }
  twos += 1 if counts.values.include?(2)
  threes += 1 if counts.values.include?(3)
end

puts twos * threes

# step 2
lines = []

input.each do |line|
  lines << line.each_char.to_a
end

(0...(lines.length)).each do |a|
  (a+1...(lines.length)).each do |b|
    diff = {}
    (0...lines[a].length).each do |c|
      unless lines[a][c] == lines[b][c]
        diff[c] = lines[a][c]
      end
    end

    if diff.length == 1
      lines[a].slice!(diff.keys.first)
      puts lines[a].join
    end
  end
end
