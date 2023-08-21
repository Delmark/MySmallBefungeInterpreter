# Befunge Interpreter
This is a [Befunge-93](https://en.wikipedia.org/wiki/Befunge) interpreter that interprets a series of instructions on a 2D field using a special pointer. By default, the pointer starts at the top-left corner of the field and moves right through the instructions, executing them.

This mini-interpreter is not finished and does not support output/input instructions, but it can execute almost all instructions written in Befuge-98. There is a lot [examples](https://esolangs.org/wiki/Befunge#Befunge-98).

## Supported Commands
- `0-9` Push this number onto the stack.
- `+` Addition: Pop a and b, then push a+b.
- `-` Subtraction: Pop a and b, then push b-a.
- `*` Multiplication: Pop a and b, then push a*b.
- `/` Integer division: Pop a and b, then push b/a, rounded down. If a is zero, push zero.
- `%` Modulo: Pop a and b, then push the b%a. If a is zero, push zero.
- `!` Logical NOT: Pop a value. If the value is zero, push 1; otherwise, push zero.
- \` (backtick) Greater than: Pop a and b, then push 1 if b>a, otherwise push zero.
- `>` Start moving right.
- `<` Start moving left.
- `^` Start moving up.
- `v` Start moving down.
- `?` Start moving in a random cardinal direction.
- `_` Pop a value; move right if value = 0, left otherwise.
- `|` Pop a value; move down if value = 0, up otherwise.
- `"` Start string mode: push each character's ASCII value all the way up to the next ".
- `:` Duplicate value on top of the stack. If there is nothing on top of the stack, push a 0.
- `\` Swap two values on top of the stack. If there is only one value, pretend there is an extra 0 on bottom of the stack.
- `$` Pop value from the stack and discard it.
- `.` Pop value and output as an integer.
- `,` Pop value and output the ASCII character represented by the integer code that is stored in the value.
- `#` Trampoline: Skip next cell.
- `p` A "put" call (a way to store a value for later use). Pop y, x and v, then change the character at the position (x,y) in the program to the character with ASCII value v.
- `g` A "get" call (a way to retrieve data in storage). Pop y and x, then push ASCII value of the character at that position in the program.
- `@` End program.
- ` ` (i.e. a space) No-op. Does nothing.
