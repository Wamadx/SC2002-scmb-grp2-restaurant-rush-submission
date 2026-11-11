# SC2002 Restaurant Rush

A Java command-line, turn-based restaurant management game. The player is the
restaurant manager: staff time is limited, so the manager decides which
customers and orders come first.

- **Course:** SC2002 Object Oriented Design & Programming
- **Lab group / group:** SCMB / Group 2
- **Repository:** `https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission`

## Build & run

From the repository root.

macOS / Linux / Git Bash:
```bash
javac -d bin $(find src -name '*.java')
java -cp bin Main
```

Windows PowerShell:
```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp bin Main
```

Reproducible Stage 1 runs (PowerShell):
```powershell
Get-Content docs/stage1/stage1-victory-input.txt | java -cp bin Main
Get-Content docs/stage1/stage1-defeat-input.txt  | java -cp bin Main
```

## The three stages

Each stage is a separate runnable version tagged in this repository.

| Stage | Adds | Git tag |
|---|---|---|
| 1 | Regular customers, menu, Waiter/Chef/Cashier, full turn sequence, win/lose | `stage-1` |
| 2 | VIP and Critic customers | `stage-2` |
| 3 | Host, ComboMeal, Happy Hour | `stage-3` |

### Stage 1 → 2

- **New files:** <!-- list -->
- **Changed / deleted files:** <!-- list, with reasons -->
- **Compare:** <!-- commit/compare link -->

### Stage 2 → 3

- **New files:** <!-- list -->
- **Changed / deleted files:** <!-- list, with reasons -->
- **Compare:** <!-- commit/compare link -->

## Video timestamps

| # | Segment | Timestamp |
|---|---|---|
| 1 | Game launching from the submitted project | |
| 2 | Meaningful manager choices, incl. a constrained-staff decision | |
| 3 | A dish going READY → SERVED → payment | |
| 4 | A VIP or Critic behaviour | |
| 5 | A Stage 3 feature | |
| 6 | One victory and one defeat | |
| 7 | Stage tag comparisons and the diagrams | |

## Team
Muhammad, U2521130K

## AI History
- [Muhammad's AI history](https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/tree/muhammad-ai/ai-history)
