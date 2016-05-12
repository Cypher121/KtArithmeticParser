# KtArithmeticParser
A simple calculator for arithmetic expressions, written in Kotlin.

Main goal of the project was creating a purely functional syntax parser. As such implementation contains no state or side effects, except for I/O.

##Usage
Launch the program, enter expression on first line and variable values on the second

    (x*y + 8 - z) / 2
    x=1 y=2 z=3
    
    3.5

Supported operations:
    Multiplication: x*y
    Division: x/y
    Summation: x+y
    Subtraction: x-y

