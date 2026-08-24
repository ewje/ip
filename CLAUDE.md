@AGENTS.md

After each code update, ensure `test/ui-test-plan.md` is updated if needed, then run the UI tests (prefer the `test-ui` skill; otherwise run `python3 scripts/run-ui-tests.py`). Always include the session transcript path (`_temp/ui-test-session.txt`) in the report, and stop immediately on the first failure with actual vs expected output locations.
