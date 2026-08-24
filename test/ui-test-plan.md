# UI Test Plan

This file records the **manual CLI-style UI test cases** for this project in a format that the `test-ui` Codex skill can execute.

## How it works

- Each test case is a fenced block with language `ui-test`.
- The runner executes test cases **in order** and **stops immediately** on the first failure.
- For each test case, the runner:
  1. Runs the command
  2. Sends the `stdin` text (if any)
  3. Captures combined console output (stdout + stderr)
  4. Compares output to `expected` (after normalization)
  5. Appends a transcript of the session to `_temp/ui-test-session.txt`

### Output normalization

To make tests stable across OSes/editors, comparison normalizes:
- `\r\n` → `\n`
- trailing whitespace at the end of lines is removed
- a final trailing newline is ignored

## Test cases

### TC0: Runner sanity check

```ui-test
id: TC0
aim: Verify the UI test runner can execute a command and compare output.
cmd: python3 -c "print('ok')"
expected: |
  ok
```

### TC1: Launch and exit

```ui-test
id: TC1
aim: Verify the app starts and can exit immediately.
cmd: sh -c "rm -f data/duke.txt; printf 'bye\n' | java -cp out/production/ip Duke"
stdin: |
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  Bye! Hope to see you again soon!
```

### TC2: Add a todo

```ui-test
id: TC2
aim: Verify a todo command adds a task and confirms it.
cmd: sh -c "rm -f data/duke.txt; printf 'todo read book\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  todo read book
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Got it! Here's the task you added:
    [T] [ ] read book
  Now you have 1 tasks in your list!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC3: Reject unknown commands

```ui-test
id: TC3
aim: Verify an unknown command shows the generic error message.
cmd: sh -c "rm -f data/duke.txt; printf 'xyz\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  xyz
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  I'm sorry, but Gary doesn't know what that means!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC4: Show an empty list

```ui-test
id: TC4
aim: Verify list prints the empty-task message when no tasks exist.
cmd: sh -c "rm -f data/duke.txt; printf 'list\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  list
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  These are the tasks you have in your list!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC5: Add a deadline

```ui-test
id: TC5
aim: Verify a deadline command stores the due date and confirms it.
cmd: sh -c "rm -f data/duke.txt; printf 'deadline submit report /by Friday\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  deadline submit report /by Friday
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Got it! Here's the task you added:
    [D] [ ] submit report (by: Friday)
  Now you have 1 tasks in your list!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC6: Add an event

```ui-test
id: TC6
aim: Verify an event command stores the start and end times and confirms it.
cmd: sh -c "rm -f data/duke.txt; printf 'event project meeting /from 2pm /to 3pm\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  event project meeting /from 2pm /to 3pm
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Got it! Here's the task you added:
    [E] [ ] project meeting (from: 2pm to: 3pm)
  Now you have 1 tasks in your list!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC7: Save a todo to disk

```ui-test
id: TC7
aim: Verify a todo is written to the hard disk when the task list changes.
cmd: sh -c "rm -f data/duke.txt; printf 'todo read book\nbye\n' | java -cp out/production/ip Duke; printf '\n---FILE---\n'; cat data/duke.txt"
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Got it! Here's the task you added:
    [T] [ ] read book
  Now you have 1 tasks in your list!
  ____________________________________________________________
  Bye! Hope to see you again soon!

  
  ---FILE---
  T | 0 | read book
```

### TC8: Load tasks from disk

```ui-test
id: TC8
aim: Verify the app loads saved tasks from the hard disk on startup.
cmd: sh -c "printf 'T | 1 | read book\nD | 0 | return book | June 6th\n' > data/duke.txt; printf 'list\nbye\n' | java -cp out/production/ip Duke"
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  These are the tasks you have in your list!
  1. [T] [X] read book
  2. [D] [ ] return book (by: June 6th)
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC9: Ignore blank input

```ui-test
id: TC9
aim: Verify blank input is rejected without crashing.
cmd: sh -c "rm -f data/duke.txt; printf '   \nbye\n' | java -cp out/production/ip Duke"
stdin: |
    
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Please enter a command.
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC10: Reject invalid mark input

```ui-test
id: TC10
aim: Verify mark rejects non-numeric task numbers.
cmd: sh -c "rm -f data/duke.txt; printf 'mark abc\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  mark abc
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  Please provide a valid number.
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC11: Reject invalid delete input

```ui-test
id: TC11
aim: Verify delete rejects task numbers that do not exist.
cmd: sh -c "rm -f data/duke.txt; printf 'delete 9\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  delete 9
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  I can't find a task with that number!
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC12: Reject incomplete deadline input

```ui-test
id: TC12
aim: Verify deadline rejects missing description or due date.
cmd: sh -c "rm -f data/duke.txt; printf 'deadline   /by friday\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  deadline   /by friday
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  The Deadline description and due date cannot be empty!
  Use ' /by ' to indicate a deadline!
  
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC13: Reject incomplete event input

```ui-test
id: TC13
aim: Verify event rejects missing start or end time.
cmd: sh -c "rm -f data/duke.txt; printf 'event meet /from 2pm\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  event meet /from 2pm
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  The Event description and times cannot be empty!
  Use ' /from ' and ' /to ' to indicate start and end times!
  
  ____________________________________________________________
  Bye! Hope to see you again soon!
```

### TC14: Skip malformed save file rows

```ui-test
id: TC14
aim: Verify invalid rows in the save file do not crash loading.
cmd: sh -c "printf 'bad line\nT | 1 | keep me\nE | 0 | missing parts\n' > data/duke.txt; printf 'list\nbye\n' | java -cp out/production/ip Duke"
stdin: |
  list
  bye
expected: |
   ================================================================== 
    ____                  
   / ___| __ _ _ __ _   _ 
  | |  _ / _` | '__| | | |
  | |_| | (_| | |  | |_| |
   \____|\__,_|_|   \__, |
                    |___/ 
   ================================================================== 
  HELLO! I'm GARY!
  How can I help you today?
  (Type bye to exit)

  ____________________________________________________________
  These are the tasks you have in your list!
  1. [T] [X] keep me
  ____________________________________________________________
  Bye! Hope to see you again soon!
```
