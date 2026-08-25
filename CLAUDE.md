@AGENTS.md

After each code update, ensure `test/ui-test-plan.md` is updated if needed, then run the UI tests (prefer the `test-ui` skill; otherwise run `python3 scripts/run-ui-tests.py`). Always include the session transcript path (`_temp/ui-test-session.txt`) in the report, and stop immediately on the first failure with actual vs expected output locations.

## JUnit test coverage target (50%)

Aim for JUnit tests to cover the top ~50% highest-value methods (prioritise complex, core, or critical business logic over simple getters/formatting).

After every code change, update/add JUnit tests as needed to keep meeting this ~50% target, and run the JUnit test suite to verify it still passes.
