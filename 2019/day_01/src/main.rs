use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn lines_from_file(filename: impl AsRef<Path>) -> Vec<String> {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|l| l.expect("Could not parse line"))
        .collect()
}

fn part_01(input: &Vec<String>) -> i64 {
    input
        .iter()
        .map(|m| m.parse::<i64>().unwrap())
        .map(|m| (m / 3) - 2)
        .sum()
}

fn part_02(input: &Vec<String>) -> i64 {
    let mut fuel: Vec<i64> = input
        .iter()
        .map(|m| m.parse::<i64>().unwrap())
        .collect();

    let mut total_fuel: i64 = 0;

    while !fuel.is_empty() {
        fuel = fuel.iter()
            .map(|m| (m / 3) - 2)
            .collect();

        fuel.retain(|&el| el > 0);

        total_fuel += fuel.iter().sum::<i64>();
    }

    total_fuel
}

fn main() {
    let input = lines_from_file("input/input.txt");

    println!("Part 01: {}", part_01(&input));
    println!("Part 02: {}", part_02(&input));
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_returs_value_for_part_1() {
        let input = lines_from_file("input/input.txt");

        assert_eq!(part_01(&input), 3342050);
    }

    #[test]
    fn it_returns_value_for_part_2() {
        let input = lines_from_file("input/input.txt");

        assert_eq!(part_02(&input), 5010211);
    }
}
