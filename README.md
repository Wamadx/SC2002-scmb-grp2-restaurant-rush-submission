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

- **New files:**
 - `src/entity/VIPCustomer.java` — VIP customer: loses 5 satisfaction per waiting turn, pays 10% less.
  - `src/entity/CriticCustomer.java` — Critic customer: loses 12 satisfaction per waiting turn, always orders Pasta.
  - `src/entity/Money.java` — applies a percentage to an amount in cents and rounds to the nearest cent.
  - `docs/stage2/` — reproducible victory and defeat inputs and outputs for Stage 2.
- **Changed / deleted files:** no files deleted
- `src/entity/Customer.java` — added `getTypeName()` and `calculateBill()` so subclasses can override them; the customer label now shows the type.
  - `src/entity/Order.java` — the bill is calculated once when the order is taken and stored in cents.
  - `src/entity/MenuItem.java`, `src/entity/Menu.java` — prices changed from `double` dollars to `int` cents, because Stage 2 introduces fractional prices (VIP discount).
  - `src/entity/Restaurant.java` — revenue stored in cents.
  - `src/control/GameController.java` — revenue target expressed in cents.
  - `src/control/TurnController.java` — the turn-4 arrival is a VIP and the turn-10 arrival is a Critic. No type checks were added; behaviour is dispatched through the customer object.
  - `src/boundary/ConsoleUI.java` — formats cents as dollars; welcome text updated for Stage 2.
- **Compare:** (https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/compare/stage-1...stage-2
)

### Stage 2 → 3

- **New files:**
 - `src/entity/Host.java` — new staff role: seats the longest-waiting customer each turn.
  - `src/entity/HappyHour.java` — pricing policy: can be switched on once per game, lasts two turns, 20% off new orders.
  - `src/entity/ComboMeal.java` — menu item made of two existing items at 90% of their combined price.
  - `docs/stage3/` — reproducible victory and defeat inputs and outputs for Stage 3.
- **Changed / deleted files:** (o files deleted)
- - `src/entity/Restaurant.java` — owns the `Host` and the `HappyHour` policy.
  - `src/entity/Menu.java` — adds the Salad + Pasta combo.
  - `src/entity/Customer.java` — menu rule changed because the menu now has four items; added `recoverSatisfaction()` for Happy Hour.
  - `src/entity/Order.java`, `src/entity/Waiter.java` — an order takes a price percentage, so the Happy Hour discount is locked when the order is taken.
  - `src/entity/WaitingQueue.java` — added `moveToTable()`, the seating step shared by the Waiter and the Host.
  - `src/control/TurnController.java` — new turn steps: Happy Hour choice, satisfaction recovery, and the Host's action before the Waiter.
  - `src/boundary/ConsoleUI.java` — shows the Happy Hour status and the Host's task; welcome text updated for Stage 3.
- **Compare:**(https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/compare/stage-2...stage-3)
## Video timestamps

The video is 14:52 long and has four sections.

| # | Section | Timestamp |
|---|---|---|
| 1 | Game overview and live demonstration (Muhammad) | 00:00 – 03:02 |
| 2 | Class diagrams and sequence diagram (Navya and Shehan) | 03:03 – 09:04 |
| 3 | How the code works and OODP principles (Pi Haoliang and Rindler Mark Barre) | 09:05 – 13:39 |
| 4 | Contributions and learning points (Muhammad) | 13:40 – 14:52 |

## Team
MUHAMMAD BIN MD ALI | U2521130K
NAVYA KAVASKER      | U2522369H
PI HAOLIANG         | U2522540K
RINDLER MARK BARRE  |
ZHANG SHEHAN        |

## AI History
- [Muhammad's AI history](https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/tree/muhammad-ai/ai-history)
- [PI HAOLIANG's AI history](https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/tree/PI-HAOLIANG-ai)
- [NAVYA KAVASKER's AI history](https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/tree/Navya-ai)
- [ZHANG SHEHAN's AI History](https://github.com/Wamadx/SC2002-scmb-grp2-restaurant-rush-submission/tree/ZHANG-SHEHAN-ai))
