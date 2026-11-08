# Goblin Clan (NeoForge 1.21.1 + GeckoLib)

- Minecraft 1.21.1, NeoForge 21.1.220, GeckoLib 4.8.4, Java 21
- Mod id: `goblin_clan`, package: `com.goblinclan`

| Mob | Summon | Animations |
|---|---|---|
| Goblin | `/summon goblin_clan:goblin` | idle, walk, attack (authored) |
| Goblin Scout | `/summon goblin_clan:goblin_scout` | idle, walk, attack, cackle; glow layer |
| Goblin Shaman | `/summon goblin_clan:goblin_shaman` | idle, walk, attack (authored, keeps the staff pose) |
| Goblin Chieftain | `/summon goblin_clan:goblin_chieftain` | idle, walk, run, attack, slam, roar + enraged variants; glowing eyes |
| Shrek | `/summon goblin_clan:shrek` | idle, walk, run, punch_right, punch_left, smash, roar, ground_slam (AoE), laugh, hurt, death; 60 HP ogre, model from `model-source/shrek.bbmodel` |

All five have spawn eggs in the Spawn Eggs creative tab. They do not spawn naturally yet.

## Assets (`src/main/resources/assets/goblin_clan/`)
- `geo/entity/<name>.geo.json`, `animations/entity/<name>.animation.json`, `textures/entity/<name>.png`
- Glow layers: `<name>_glowmask.png` (chieftain eyes, scout glow)
- Notes: goblin and shaman animations were written for this mod; replace the JSON with your own Blockbench export if you prefer.
  Shaman geo: `leftLeg` pivot moved to the hip so the leg swings correctly.

## Shrek model
`model-source/shrek.bbmodel` is the Blockbench source. The game uses the exported `geo/entity/shrek.geo.json` + `textures/entity/shrek.png`.
To edit: open the .bbmodel in Blockbench, export Bedrock geometry, replace the geo json (keep bone names `head`, `body`, `right_arm`, `left_arm`, `right_leg`, `left_leg`).

## Build
GitHub Actions builds on every push (`.github/workflows/main.yml`); the jar is in the run artifacts and in Releases.
