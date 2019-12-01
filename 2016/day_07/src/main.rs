use std::{
    str,
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

use pcre::Pcre;

#[derive(Debug, PartialEq)]
pub struct IPv7 {
    pub within_squares: Vec<String>,
    pub outside_squares: Vec<String>
}

fn lines_from_file(filename: impl AsRef<Path>) -> Vec<String> {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|l| l.expect("Could not parse line"))
        .collect()
}

fn tokenize(ip: String) -> IPv7 {
    let mut within_squares = vec![];
    let mut outside_squares = vec![];

    let mut outside_regexp = Pcre::compile(r"(?<!\[)(\w+)(?![\w]*[\]])").unwrap();
    for result in outside_regexp.matches(ip.as_str()) {
        outside_squares.push(result.group(0).to_string())
    }

    let mut inside_regexp = Pcre::compile(r"(?<=\[)\w+(?=[\w]*[\]])").unwrap();
    for result in inside_regexp.matches(ip.as_str()) {
        within_squares.push(result.group(0).to_string())
    }

    IPv7 { within_squares: within_squares, outside_squares: outside_squares }
}

fn is_abba(ip: String) -> bool {
    ip.as_bytes().windows(4).any( |w| w[0] == w[3] && w[1] == w[2] && w[0] != w[1] )
}

fn find_abas(ip: String) -> Vec<String> {
    ip.as_bytes()
        .windows(3)
        .filter( |w| w[0] == w[2] && w[0] != w[1] )
        .map(|c| str::from_utf8(c).unwrap().to_string())
        .collect::<Vec<_>>()
}

fn supports_tls(ipv7: &IPv7) -> bool {
    let within = &ipv7.within_squares;
    let outside = &ipv7.outside_squares;

    outside.iter().any(|w| is_abba(w.to_string())) && !within.iter().any(|w| is_abba(w.to_string()))
}

fn aba_to_bab(aba: String) -> String {
    let bytes = aba.as_bytes();

    str::from_utf8(&[bytes[1], bytes[0], bytes[1]]).unwrap().to_string()
}

fn to_babs(abas: Vec<String>) -> Vec<String> {
    abas.iter().map(|aba| aba_to_bab(aba.to_string())).collect::<Vec<_>>()
}

fn any_bab_in_brackets(babs: Vec<String> , within: &Vec<String>) -> bool {
    babs.iter().any(|bab| within.iter().any(|w| w.contains(bab)))
}

fn supports_ssl(ipv7: &IPv7) -> bool {
    let within = &ipv7.within_squares;
    let outside = &ipv7.outside_squares;

    outside.iter()
        .map(|o| find_abas(o.to_string()))
        .map(|abas| to_babs(abas))
        .any(|babs| any_bab_in_brackets(babs, within))
}

fn part_01(input: &Vec<String>) -> usize {
    input
        .iter()
        .map(|ip| tokenize(ip.to_string()))
        .filter(|ip| supports_tls(ip))
        .count()
}

fn part_02(input: &Vec<String>) -> usize {
    input
        .iter()
        .map(|ip| tokenize(ip.to_string()))
        .filter(|ip| supports_ssl(ip))
        .count()
}

fn main() {
    let input = lines_from_file("/Users/michal.stolarczyk/code/learning/adventofcode/2016/day_07/input/input.txt");

    println!("Part 01: {}", part_01(&input));
    println!("Part 02: {}", part_02(&input));
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_tokenizes_string() {
        let ipv7 = IPv7 { within_squares: vec!["acca".to_string()], outside_squares: vec!["abba".to_string(), "appa".to_string()] };
        assert_eq!(tokenize("abba[acca]appa".to_string()), ipv7);
    }

    #[test]
    fn it_recognizes_abba() {
        assert_eq!(is_abba("abba".to_string()), true);
        assert_eq!(is_abba("aaaa".to_string()), false);
        assert_eq!(is_abba("abbaqorp".to_string()), true);
        assert_eq!(is_abba("qorpabbaqorp".to_string()), true);
        assert_eq!(is_abba("abba[mnop]qrst".to_string()), true);
        assert_eq!(is_abba("abpa[mnop]qrst".to_string()), false);
    }

    #[test]
    fn it_recognizes_tls() {
        let ipv7 = IPv7 { within_squares: vec!["aca".to_string()], outside_squares: vec!["oabbaa".to_string(), "anka".to_string()] };
        assert!(supports_tls(&ipv7));

        let ipv7 = IPv7 { within_squares: vec!["acca".to_string()], outside_squares: vec!["oabbaa".to_string(), "anka".to_string()] };
        assert_eq!(supports_tls(&ipv7), false);
    }

    #[test]
    fn it_supports_ssl() {
        let ipv7 = IPv7 { within_squares: vec!["bab".to_string()], outside_squares: vec!["aba".to_string(), "xyz".to_string()] };
        assert!(supports_ssl(&ipv7));

        let ipv7 = IPv7 { within_squares: vec!["xyx".to_string()], outside_squares: vec!["xyx".to_string(), "xyx".to_string()] };
        assert_eq!(supports_ssl(&ipv7), false);

        let ipv7 = IPv7 { within_squares: vec!["bzb".to_string()], outside_squares: vec!["zazbz".to_string(), "cdb".to_string()] };
        assert_eq!(supports_ssl(&ipv7), true);

        let ipv7 = IPv7 { within_squares: vec!["kek".to_string()], outside_squares: vec!["aaa".to_string(), "eke".to_string()] };
        assert_eq!(supports_ssl(&ipv7), true);
    }
}
