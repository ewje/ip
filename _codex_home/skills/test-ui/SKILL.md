---
name: test-ui
description: Run CLI-style UI tests from `test/ui-test-plan.md` by executing commands and comparing captured console output to expected output, stopping at the first failure and recording a full session transcript.
---

# Test UI

Use this skill to execute a sequence of CLI-style UI test cases recorded in `test/ui-test-plan.md`.

## Test plan format

- Test cases are fenced blocks with language `ui-test`.
- Each test case must include:
  - `id`: test case identifier
  - `aim`: what the test is verifying
  - `cmd`: command to run (executed from the repository root)
  - `stdin`: optional multi-line input sent to the program
  - `expected`: expected combined console output (stdout + stderr)

See `test/ui-test-plan.md` for the template.

## Run tests

From the repository root, run:

```bash
python3 scripts/run-ui-tests.py
```

## Results and failure behavior

- Always produce a session transcript at `_temp/ui-test-session.txt` containing the full console input/output of the test session.
- Stop immediately on the first failing test case and report:
  - which test case failed
  - where the transcript is
  - the expected vs actual output files:
    - `_temp/ui-test-expected.txt`
    - `_temp/ui-test-actual.txt`

