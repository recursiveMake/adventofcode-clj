#!/usr/bin/python

import sys

def read_puzzle(string):
    steps = [s.strip(' ') for s in string.split(',')]
    return steps

def turn(current, turn):
    if turn == 'L':
        current -= 1
    else:
        current += 1
    return (4 + current) % 4

def move(steps):
    direction = 0
    distances = {'0': 0, '1': 0, '2': 0, '3': 0}

    for step in steps:
        direction = turn(direction, step[0])
        distances[str(direction)] += int(step[1:])
    print distances
    vertical = abs(distances['0'] - distances['3'])
    horizontal = abs(distances['1'] - distances['2'])
    return vertical+horizontal

if __name__ == '__main__':
    
    line = sys.argv[1]
    print 'Line:', line
    steps = read_puzzle(line)
    result = move(steps)
    print 'Result:', result
